package class7.distsync;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ServerUpdateThread implements Runnable{
	QueueConn qc ; 
	String serverName;
	private final Logger log = LoggerFactory.getLogger(ServerUpdateThread.class);
	
	public ServerUpdateThread(QueueConn qc , String serverName) {
		this.qc = qc;
		this.serverName = serverName;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerUpdateThread.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
	}
	@Override
	public void run() {
		
		/*
		 * Paso 1 - verificar si hay dato compartido.
		 * 	Si no -> bloqueado
		 *  Si hay dato: 
		 *  	a) leer dato (sin confirmar) -> re-encolar
		 *  	b) actualizar mi valor comparido
		 *  	c) avisar a cola de servidores actualizados -> mi estado actualizado
		 *  	d) paso para leer una sola vez
		 */
		long idForAck;
		String oldMsg = "";
		while (true) {
			
			try {
				if ((this.qc.queueChannel.queueDeclarePassive(this.qc.sharedPublicValue).getMessageCount()>0)) {
					// Validar no existencia en cola actualizados
					
					//STEP 1 - leer dato ok
					log.info("[SERVERUPDATE] - Dato en cola compartida disponible");
					
					GetResponse data = this.qc.queueChannel.basicGet(this.qc.sharedPublicValue, false);
					String msg = new String (data.getBody());
					idForAck = data.getEnvelope().getDeliveryTag();
					
					log.info(" MSG VIEJO: "+ oldMsg + " -- MSG EN COLA: "+ msg);
					// STEP 2
					log.info("[SERVERUPDATE] - MSG Leido ");
					this.qc.queueChannel.basicNack(idForAck, false, true);
					log.info("[SERVERUPDATE] - MSG Reencolado ");
					if (!(oldMsg.equals(msg))) {
						this.qc.queueChannel.basicPublish("",this.qc.updatedServers, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
						log.info("[SERVERUPDATE] - Actualicé cola de servidores actualizados");
						oldMsg = msg;
					}
				}else {
					
					log.info("[SERVERUPDATE] - Cola compartida vacia");
				}
			} catch (IOException e) {
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
