package class7.distsync;

import java.io.IOException;
import java.util.ArrayList;

import com.rabbitmq.client.MessageProperties;

public class CoordinatorStarter {

	QueueConn qc;
	String ip;
	ArrayList<Servers> serverList;
	int cont;
	
	public CoordinatorStarter() {
		this.ip = "localhost";
		this.qc = new QueueConn (this.ip);
		System.out.println("[COORD] - Arrancó coordinador");
		this.serverList = new ArrayList<Servers>();
		this.cont = 0;
		ActiveServerList act = new ActiveServerList (this.serverList, this.qc);
		Thread actThread = new Thread (act);
		actThread.start();
		
		try {
			Thread.sleep(3000);
			while (this.serverList.size()<1) {
				Thread.sleep(5000);
			}
			// ASIGNAR PRIMER TOKEN
			Servers s = this.serverList.get(this.cont);
			// al ser local la el SGColas es el mismo
			s.getQueueName();
			this.qc.queueChannel.basicPublish("",s.getQueueName(), MessageProperties.PERSISTENT_TEXT_PLAIN, "token".getBytes());
		} catch (InterruptedException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		// Parte de verificacion de release
		while (true) {
			try {
				// SI habia mensaje de release ->
				if (this.qc.queueChannel.queueDeclarePassive(this.qc.releaseTokenQueue).getMessageCount()>0) {
					System.out.println("[COORD] - Hay release ");
					int qUpdated = this.qc.queueChannel.queueDeclarePassive(this.qc.updatedServers).getMessageCount();
					int qActives = this.qc.queueChannel.queueDeclarePassive(this.qc.activeServers).getMessageCount();
					// -> si la cantidad de activos = cantidad de actualizados
					if (qUpdated == qActives) {
						System.out.println("[COORD] - actualizacion de todos los nodos ok");
						
						/*
						 * vaciar la cola de actualizados
						 * vaciar la cola de release
						 * vaciar cola de token -> asignación de token a servidor
						 */
						this.qc.queueChannel.queuePurge(this.qc.releaseTokenQueue);
						this.qc.queueChannel.queuePurge(this.qc.sharedPublicValue);
						this.qc.queueChannel.queuePurge(this.qc.updatedServers);
						System.out.println("[COORD] - Cola de token lista para acceder nuevo servidor");
						
						this.cont++;
						if (this.cont>(this.serverList.size()-1)) {
							this.cont=0;
						}
						Servers s = this.serverList.get(this.cont);
						// al ser local la el SGColas es el mismo
						s.getQueueName();
						this.qc.queueChannel.basicPublish("",s.getQueueName(), MessageProperties.PERSISTENT_TEXT_PLAIN, "token".getBytes());
					}
				}else {
					System.out.println(" ESPERANDO RELEASE");
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
	}
	
	public static void main(String[] args) {
		CoordinatorStarter cs = new CoordinatorStarter();
	}
}
