package Class6.DistributedSynchronization;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ServerUpdateThread implements Runnable{
	String ip;
	UtilsConnection uc;
	private final Logger log = LoggerFactory.getLogger(ServerRequestThread.class);
	public ServerUpdateThread(String ip, UtilsConnection uc) {
		this.ip = ip;
		this.uc = uc;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerUpdateThread.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
	}

	@Override
	public void run() {
		log.info(" [SYNC-REPLICATEDDATA] - Update Thread looper started.. Waiting for Data");
		boolean loop = true;
		while (true) {
			// Validate if SharedData it is empty (no replica) 
			try {
				
				//[STEP 0] - check shared queue
				if ((loop) && (this.uc.queueChannel.queueDeclarePassive(this.uc.sharedDataQueue).getMessageCount()>0)){
					log.info(" [SYNC-REPLICATEDDATA] - Data Replication started ");

					//[STEP 1] - Obtain data from shared queue 
					GetResponse data = this.uc.queueChannel.basicGet(this.uc.sharedDataQueue, false);
					String msg = new String (data.getBody());
					log.info(" [SYNC-REPLICATEDDATA] - Server has read MSG "+msg);
					long idForAck = data.getEnvelope().getDeliveryTag();
					this.uc.queueChannel.basicNack(idForAck, false, true);
					
					// [STEP 2] - Increase Nº of updated servers
					
					this.uc.queueChannel.basicPublish("", this.uc.updatedServersQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
					log.info(" [SYNC-REPLICATEDDATA] - Notify Server has been updateded");
					
					loop = false;
					

				}else {
					if (this.uc.queueChannel.queueDeclarePassive(this.uc.sharedDataQueue).getMessageCount()==0) {
						loop = true;
					}
					log.info(" [SYNC-REPLICATEDDATA] - Server updated before, waiting next loop ");
					Thread.sleep(5000);
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
