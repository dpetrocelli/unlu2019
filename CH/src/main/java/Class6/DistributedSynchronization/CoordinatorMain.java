package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;




public class CoordinatorMain {
	String ip;
	UtilsConnection uc;
	private final Logger log = LoggerFactory.getLogger(CoordinatorMain.class);
	ArrayList<Servers> listServers;
	int cont;
	
	public CoordinatorMain (String string) {
		this.ip = string;
		this.uc = new UtilsConnection (ip);
		this.listServers = new ArrayList<Servers>();
		this.cont = 0;
		log.info(" Rabbit MQ Connection established ");
		this.loop();
	}
	
	private void loop() {
		// loop Operating as a Coordinator
		int activeServers = 0;
		int updatedServers = 0;
		boolean equals;
		log.info(" Coordinator has Started ");
		
		// [STEP -1] - Generar Thread para validar los servidores activos
		CoordinatorThreadActiveServers ctas = new CoordinatorThreadActiveServers (this.listServers, this.uc, this.ip, (ch.qos.logback.classic.Logger) this.log);
		Thread ctasThread = new Thread (ctas);
		ctasThread.start();
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.asignarToken();
				
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
					
					
					/* ES LO QUE VAMOS A CAMBIAR 
					 * Definir pasos -> 
					 * 1) Servidores -> Cola con su nombre -> Asginar token
					 * CH-QUEUEREQUEST-S1
					 * 2) coordinador: -> Registrar en un AList IPServer, NombreCola (arriba)
					 * 
					 * 3) UtilsConnection -> Servidor -> escribir el request (token)
					 * */
					/*
					this.uc.queueChannel.basicGet(this.uc.requestQueue, true); // -> (0)
					log.info(" [SYNC-RELEASED] Request Queue has been erased ");
					*/
					
					
					
					log.info(" [SYNC-RELEASED] Synchronizing loop has been finished ");
					
					this.asignarToken();
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

	
	private void asignarToken() {
		// 1ra vez tengo que agarra un server de la lista de servidores
		// Asignarle un token
		// obtener de la lista de servidores -> indice
		this.log.info("DONDE ESTAMOS");
		String serverName = "";
			
		try{
			synchronized (this.listServers) {
				if (this.cont>(this.listServers.size()-1)) {
					this.cont = 0;
				}
				this.log.info("verifico tamanio lista: cont: "+this.cont);
				this.log.info("size: "+this.listServers.size());
				Servers s = this.listServers.get(this.cont);
				this.log.info("obtuvo server e incremento");
				this.cont++;
				
				serverName = s.queueName;
				log.info("NOMBRE SERVER:" +serverName);
				this.log.info("voy a hacer publish");
				try {
					this.uc.queueChannel.basicPublish("", serverName, MessageProperties.PERSISTENT_TEXT_PLAIN, "nada".getBytes());
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.log.info("hizo publish");
			}
			
		}catch (Exception e) {
				System.out.println(e.getMessage());
		}
		
		
	}

	public static void main(String[] args) {
		int thread = (int) Thread.currentThread().getId();
		String packetName=CoordinatorMain.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		CoordinatorMain cm = new CoordinatorMain("localhost");
	}
}
