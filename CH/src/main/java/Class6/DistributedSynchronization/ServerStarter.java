package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.MessageProperties;

public class ServerStarter implements Runnable{
	String ip;
	String name;
	UtilsConnection uc;
	private final Logger log = LoggerFactory.getLogger(ServerStarter.class);
	
	public ServerStarter (String ip, String name) {
		this.ip = ip;
		this.name = name;
		this.uc = new UtilsConnection (ip);
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerStarter.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		log.info(" rabbit mq Connection established ");
		
	}
	
	
	@Override
	public void run() {
		
		// [STEP 0] - Crear su cola (Con su nombre)
		String[] spli = this.name.split(Pattern.quote("|"));
		String serverip = spli[0];
		String queueName = spli[1];
		try {
			this.uc.queueChannel.queueDeclare(queueName, true, false, false, null);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// [STEP 0.5] - Crear la cola para manejar la concurrencia de los threads mios
		// [STEP 1] - ADDSERVER
		
		ServerAddThread sat = new ServerAddThread (this.ip, this.uc, this.name);
		Thread satThread = new Thread (sat);
		satThread.start(); 
		
		try {
			satThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// [STEP 2] - PREPAIR REQUEST
		// Recreate the connection
		
		/*this.uc = new UtilsConnection(this.ip);
		
		ServerRequestThread srt = new ServerRequestThread (this.ip, this.uc);
		Thread srtThread = new Thread (srt);
		srtThread.start();
	
		
		// [STEP 3] - UPDATE VALUES
		ServerUpdateThread sut = new ServerUpdateThread (this.ip, this.uc);
		Thread sutThread = new Thread (sut);
		sutThread.start();
		*/
	}

}
