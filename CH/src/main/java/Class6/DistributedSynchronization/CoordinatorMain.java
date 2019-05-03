package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;




public class CoordinatorMain {
	String ip;
	UtilsConnection uc;
	private final Logger log = LoggerFactory.getLogger(CoordinatorMain.class);
	
	
	public CoordinatorMain (String string) {
		this.ip = string;
		this.uc = new UtilsConnection (ip);
		log.info(" Rabbit MQ Connection established ");
		this.loop();
	}
	
	private void loop() {
		// loop Operating as a Coordinator
		int activeServers = 0;
		int updatedServers = 0;
		boolean equals;
		log.info(" Coordinator has Started ");
		while (true) {
			// Validate if releasedQueue has a ReleasedMsg
			try {
				//[STEP 0] - check Released queue
				if (this.uc.queueChannel.queueDeclarePassive(this.uc.releasedQueue).getMessageCount()>0){
					log.info(" [SYNC-RELEASED] - Released Token has Arrived");
					// We have a released msg
					//[STEP 1] - Validate if every Single server has updated their public value
					equals = false;
					while (!equals) {
						activeServers = this.uc.queueChannel.queueDeclarePassive(this.uc.activeServersQueue).getMessageCount();
						updatedServers = this.uc.queueChannel.queueDeclarePassive(this.uc.updatedServersQueue).getMessageCount();
						if (activeServers==updatedServers) equals =true;else {
							log.info(" [SYNC-UPDATED] Some server remains to be updated");
							Thread.sleep(3000);
						} 
					}
					log.info(" [SYNC-RELEASED] Server UPDATes have finished ");
					log.info(" [SYNC-RELEASED] TokenFree has started ");
					// [STEP 2] - Remove Released and remove Lock from RequestToken 
					this.uc.queueChannel.basicGet(this.uc.releasedQueue, true); // -> (0)
					log.info(" [SYNC-RELEASED] Released Queue has been erased ");
					
					/*
					 *  [STEP 3] - Remove data from:
					 *  SharedQueue -> empty for prepair following replies
					 *  RequestQueue -> empty to unlock for new token assignation
					 */
					this.uc.queueChannel.basicGet(this.uc.requestQueue, true); // -> (0)
					log.info(" [SYNC-RELEASED] Request Queue has been erased ");
					
					this.uc.queueChannel.basicGet(this.uc.sharedDataQueue, true); // -> (0)
					log.info(" [SYNC-RELEASED] Shared Data Queue has been erased ");
					
					// purge queue
					/*
					 *  Opt 1 - 
					 *  	a) obtener cantidad de msgs de UpdatedQueue
					 *  	b) for hasta N basicget -> autoack ;)
					 *  
					 *  Opt 2 -
					 *  	purge -> vaciar
					 */
					this.uc.queueChannel.queuePurge(this.uc.updatedServersQueue);
					log.info(" [SYNC-RELEASED] Updated Queue has been erased ");
					
					log.info(" [SYNC-RELEASED] Synchronizing loop has been finished ");
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

	
	public static void main(String[] args) {
		int thread = (int) Thread.currentThread().getId();
		String packetName=CoordinatorMain.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		CoordinatorMain cm = new CoordinatorMain("localhost");
	}
}
