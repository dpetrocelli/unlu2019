package class7.distsync;

import java.io.IOException;

public class CoordinatorStarter {

	QueueConn qc;
	String ip;
	
	public CoordinatorStarter() {
		this.ip = "localhost";
		this.qc = new QueueConn (this.ip);
		System.out.println("[COORD] - Arrancó coordinador");
		while (true) {
			try {
				if (this.qc.queueChannel.queueDeclarePassive(this.qc.releaseTokenQueue).getMessageCount()>0) {
					System.out.println("[COORD] - Hay release ");
					int qUpdated = this.qc.queueChannel.queueDeclarePassive(this.qc.updatedServers).getMessageCount();
					int qActives = this.qc.queueChannel.queueDeclarePassive(this.qc.activeServers).getMessageCount();
					if (qUpdated == qActives) {
						System.out.println("[COORD] - actualizacion de todos los nodos ok");
						this.qc.queueChannel.queuePurge(this.qc.requestTokenQueue);
						this.qc.queueChannel.queuePurge(this.qc.releaseTokenQueue);
						this.qc.queueChannel.queuePurge(this.qc.sharedPublicValue);
						this.qc.queueChannel.queuePurge(this.qc.updatedServers);
						System.out.println("[COORD] - Cola de token lista para acceder nuevo servidor");
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
