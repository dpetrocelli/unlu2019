package class7.distsync;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.MessageProperties;

public class ServerTokenRequestThread implements Runnable{

	QueueConn qc ; 
	String serverName;
	String value;
	private final Logger log = LoggerFactory.getLogger(ServerTokenRequestThread.class);
	public ServerTokenRequestThread(QueueConn qc, String serverName, String publicValue) {
		this.qc = qc;
		this.serverName = serverName;
		this.value = publicValue;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerTokenRequestThread.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
	}
	@Override
	public void run() {
		
		
		while (true) {
			/*
			 * Acceder al recurso compartido
			 * tomar cuando cola request = 0
			 * 	-> 1 ) cambiar estado cola request =1
			 *  -> 2 ) publicar valor en cola datos compartidos
			 *  -> 3 ) notificar Release
 			 */
			try {
				synchronized (this.qc) {
					if (this.qc.queueChannel.queueDeclarePassive(this.serverName).getMessageCount()==1) {
						log.info("[SERVERTOKEN] - Token obtenido");
						this.qc.queueChannel.basicGet(this.serverName, true);
						log.info("[SERVERTOKEN] - Cambio de condicion para bloquearme");
						// Obtuve token cambio estado
						//this.qc.queueChannel.basicPublish("", this.qc.requestTokenQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
						// publico valor en cola compartida
						//log.info("[SERVERTOKEN] - Cola token bloqueada");
						this.value+="0";
						this.qc.queueChannel.basicPublish("", this.qc.sharedPublicValue, MessageProperties.PERSISTENT_TEXT_PLAIN, this.value.getBytes());
						// notifico del release
						log.info("[SERVERTOKEN] - valor publicado");
						this.qc.queueChannel.basicPublish("", this.qc.releaseTokenQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
						log.info("[SERVERTOKEN] - notificación de release");
					}else {
						log.info("[SERVERTOKEN] - Esperando por token ");
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
