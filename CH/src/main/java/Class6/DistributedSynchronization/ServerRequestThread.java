package Class6.DistributedSynchronization;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.MessageProperties;

public class ServerRequestThread implements Runnable{
	String ip;
	UtilsConnection uc;
	private final Logger log = LoggerFactory.getLogger(ServerRequestThread.class);
	
	public ServerRequestThread (String ip, UtilsConnection uc) {
		this.ip = ip;
		this.uc = uc;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerRequestThread.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		
	}
	@Override
	public void run() {
		
		log.info(" [SYNC-REQUEST] - Request Token started.. Waiting for Token");
		// Cola = 0 -> Token
		// Cola = 1 -> Wait()
		
		while (true) {
			// Validate if RequestQueue if empty (token enabled) 
			try {
				//[STEP 0] - check Released queue
				if (this.uc.queueChannel.queueDeclarePassive(this.uc.requestQueue).getMessageCount()==0){
					log.info(" [SYNC-REQUEST] - Request Token Obtained ");

					//[STEP 1] - Change state of requestQueue -> validate only 1 server 

					
					byte[] msg = "msg".getBytes();
					this.uc.queueChannel.basicPublish("", this.uc.requestQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
					log.info(" [SYNC-REQUEST] - Request Queue has been locked ");
					
					// [STEP 2] - publish msg in Shared Queue
					
					this.uc.queueChannel.basicPublish("", this.uc.sharedDataQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
					log.info(" [SYNC-REQUEST] - Public Content has been shared ");
					// [STEP 3] - change state in Released Queue
					
					this.uc.queueChannel.basicPublish("", this.uc.releasedQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, msg);
					log.info(" [SYNC-REQUEST] - Released Queue has been alerted ");
					
					log.info(" [SYNC-REQUEST] - Token has used and is returned to Admin Coordinator ");

				}else {
					log.info(" [SYNC-REQUEST] - Request Queue is locked ");
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
