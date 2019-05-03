package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ServerAddThread implements Runnable {
	String ip;
	UtilsConnection uc;
	String serverName;
	private final Logger log = LoggerFactory.getLogger(ServerAddThread.class);
	
	public ServerAddThread(String ip, UtilsConnection uc, String name) {
		this.ip = ip;
		this.uc = uc;
		this.serverName = name;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerAddThread.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
	}

	@Override
	public void run() {
		
		// [STEP 0] - check if server appears If empty -> add
		try {
			if (this.uc.queueChannel.queueDeclarePassive(this.uc.activeServersQueue).getMessageCount()==0){
				this.uc.queueChannel.basicPublish("", this.uc.activeServersQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
				log.info(" [SYNC-ADDSERVER] - Empty Queue - Server has been added ");
			}else{
				
				ArrayList<Integer> ids = new ArrayList<Integer>();
				int msgCount = this.uc.queueChannel.queueDeclarePassive(this.uc.activeServersQueue).getMessageCount();
				int counter = 0;
				boolean found = false;
				while ((!found) && counter<msgCount) {
					GetResponse data = this.uc.queueChannel.basicGet(this.uc.activeServersQueue, false);
					String msg = new String (data.getBody());
					log.info(" [SYNC-ADDSERVER] - Server has been found in Active Servers: "+msg);
					long idForAck = data.getEnvelope().getDeliveryTag();
					ids.add((int) idForAck);
					if (msg.equals(this.serverName)) found = true;
					counter+=1;
				}
				for (Integer integer : ids) {
					this.uc.queueChannel.basicNack(integer, false, true);
				}
				
				if (!(found)) {
					log.info(" [SYNC-ADDSERVER] - Iterated but not found ");
					this.uc.queueChannel.basicPublish("", this.uc.activeServersQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
					log.info(" [SYNC-ADDSERVER] - Server has been added (More than 1 server) ");
				}else {
					log.info(" [SYNC-ADDSERVER] - Server has been found -> Not ADDED ");
				}
				
			}
			try {
				this.uc.queueChannel.close();
				this.uc.queueConnection.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
