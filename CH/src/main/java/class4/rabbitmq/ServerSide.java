package class4.rabbitmq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;




public class ServerSide {

	int port;
	Logger log = LoggerFactory.getLogger(ServerSide.class);
	String ip;
	String username;
	String password;
	
	ConnectionFactory queueConnectionFactory;
	Connection queueConnection;
	Channel queueChannel;
	String queue;
	
	public ServerSide (int port) {
		
		try {
			this.port = port;
			this.username = "admin";
			this.password = "admin";
			this.ip = "localhost";
			this.queue = "chEnterQueue";
			// [STEP 0] - FACTORIA DE CONEXION
			this.queueConnectionFactory = new ConnectionFactory();
			this.queueConnectionFactory.setHost(this.ip);
			this.queueConnectionFactory.setUsername(this.username);
			this.queueConnectionFactory.setPassword(this.password);
			// [STEP 1] - QueueConnection
			this.queueConnection = this.queueConnectionFactory.newConnection();
			// [STEP 2] - ChannelConnection
			this.queueChannel = this.queueConnection.createChannel();
			// [STEP 3] - EnterQueue -> Client -> upload Messages
			this.queueChannel.queueDeclare(this.queue, true, false, false, null);
			log.info("RabbitMQ Queue System has started correctly");
			
			ServerSocket ss = new ServerSocket (this.port);
			log.info(" Server has started on port: "+port);
			
			while (true) {
				Socket client = ss.accept();
				log.info(" Server has received client from IP/PORT: "+client.getInetAddress().getHostName());
				ObjectOutputStream outputObject = new ObjectOutputStream (client.getOutputStream());
				ObjectInputStream inputObject = new ObjectInputStream (client.getInputStream());
				
				
				String msg = (String) inputObject.readObject();
				Gson gson = new Gson ();
				Message decodedMsg = gson.fromJson(msg, Message.class);
				
				if (decodedMsg.getHeader().equals("uploadMessage")) {
					log.info("LLEGAMOS");
					byte[] finalData = msg.getBytes();
					this.queueChannel.basicPublish("", this.queue, MessageProperties.PERSISTENT_TEXT_PLAIN, finalData);
				}else {
					if (msg.equals("getJob")) {
						// Get msg from EnterQueue
					}else {
						// Save finishedJob in FinishedQueue
					}
					
				}
			}
			// [STEP X] - Finished Task -> Worker -> upload Solved Task
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public static void main(String[] args) {
		String logname = ServerSide.class.getSimpleName().toString();
		System.setProperty("log.name",logname);
		ServerSide s = new ServerSide(9000);
	}
}

