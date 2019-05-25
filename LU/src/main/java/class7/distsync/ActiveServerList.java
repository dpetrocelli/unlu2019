package class7.distsync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import com.rabbitmq.client.GetResponse;

public class ActiveServerList implements Runnable {
	
	ArrayList<Servers> serverList;
	QueueConn qc;
	ArrayList<Long> ackList;
	
	public ActiveServerList(ArrayList<Servers> serverList, QueueConn qc) {
		this.serverList = serverList;
		this.qc = qc;
	}

	@Override
	public void run() {
		
		int qMessages;
		long idForAck;
		// -> Thread para validar servidores activos.
		while (true) {
			
			this.ackList = new ArrayList<Long>();
			try {
				qMessages = (this.qc.queueChannel.queueDeclarePassive(this.qc.activeServers).getMessageCount());
				
				for (int i =0; i<qMessages; i++) {
					GetResponse data = this.qc.queueChannel.basicGet(this.qc.activeServers, false);
					String msg = new String (data.getBody());
					String[] parts = msg.split(Pattern.quote("|"));
					Servers s = new Servers(parts[0], parts[1]);
					idForAck = data.getEnvelope().getDeliveryTag();
					this.ackList.add(idForAck);
					
					boolean found = false;
					
					for (Servers server : this.serverList) {
						
						if (server.getQueueName().equals(s.getQueueName())) {
							found =true;
						}
					}
					if (!found) {
						this.serverList.add(s);
					}
					
					//System.out.println("[THREADACTIVE] - Servidor Activo: "+msg);
					
				}
				
				for (Long long1 : ackList) {
					this.qc.queueChannel.basicNack(long1, false, true);
				}
			}
			 catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			try {
				//System.out.println("[THREADACTIVE] - Tamaño lista: "+this.serverList.size());
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
