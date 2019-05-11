package class7.distsync;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;




public class ServerSideMain implements Runnable{
	
	QueueConn qc;
	String ip;
	String serverName;
	String publicValue;
	
	private final Logger log = LoggerFactory.getLogger(ServerSideMain.class);
	public ServerSideMain (String ip, String serverName, String publicValue) {
		this.ip = ip;
		this.serverName = serverName;
		this.publicValue = publicValue;
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerSideMain.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
	}
	public void run() {
		// Conexión a la cola y creacion si es necesario
		this.qc = new QueueConn (this.ip);
		
		/*
		 * T1 - Agregar Server Cola Activos (Verificar que ya esté) / Eliminación
		 * T2 - Acceso a token 
		 * T3 - Descarga de contenido público
		 */
		
		// T1 - Agregar Server cola
		// [STEP 0] - validar si ya existo
		//this.queueChannel.basicGet(this.queueName, true);
		// NAcK ->
		//.purge()
		// .cant - > this.queueChannel.basicPublish("", this.queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
		// if (this.uc.queueChannel.queueDeclarePassive(this.uc.activeServersQueue).getMessageCount()==0
		
		try {
			
			this.qc.queueChannel.basicPublish("",this.qc.activeServers, MessageProperties.PERSISTENT_TEXT_PLAIN, this.serverName.getBytes());
			
			log.info("[ADDSERVER] - Servidor agregado a la cola" );
			
			// THREAD 1 - Manejo de Acceso a Token
			ServerTokenRequestThread strt = new ServerTokenRequestThread (this.qc, this.serverName, this.publicValue);
			Thread strtThread = new Thread (strt);
			strtThread.start();
			Thread.sleep(5000);
			
			// Thread 2 - Actualizar datos de cola shared
			ServerUpdateThread sut = new ServerUpdateThread (this.qc, this.serverName);
			Thread sutThread = new Thread (sut);
			sutThread.start();
			sutThread.join();
			// ELIMINAR SERVIDOR AL TERMINAR
			boolean finish = false;
			long idForAck;
			while (!finish) {
				GetResponse data = this.qc.queueChannel.basicGet(this.qc.activeServers, false);
				String msg = new String (data.getBody());
				idForAck = data.getEnvelope().getDeliveryTag();
				
				log.info("[LOOKFOR] - Server: "+msg);
				if (msg.equals(this.serverName)){
					this.qc.queueChannel.basicAck(idForAck, true);
					log.info("[REMSERV] - Remove: "+msg);
					finish = true;
				}else {
					this.qc.queueChannel.basicNack(idForAck, false, true);
				}
			}
			this.qc.queueChannel.close();
			this.qc.queueConnection.close();
			
		} catch (IOException | InterruptedException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}


}
