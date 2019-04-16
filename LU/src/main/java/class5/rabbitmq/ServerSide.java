package class5.rabbitmq;

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
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;


public class ServerSide {
	String ip;
	int port;
	private final Logger log = LoggerFactory.getLogger(ServerSide.class);
	String username;
	String password;
	ConnectionFactory connectionFactory;
	Connection queueConnection;
	Channel queueChannel;
	String queueName;
	String finishedQueue;
	Gson googleJson;
	
	public ServerSide(String string, int i) {
		this.ip = string;
		this.port = i;
		this.username = "admin";
		this.password = "admin";
		this.queueName = "JavaEnterQueue";
		this.finishedQueue = "JavaFinishedQueue";
		this.configureConnectionToRabbit();
		log.info(" rabbit mq Connection established ");
		this.configureServer();
		
	}
	
	private void configureConnectionToRabbit() {
		
		try {
			this.googleJson = new Gson();
			this.connectionFactory = new ConnectionFactory();
			this.connectionFactory.setHost(this.ip);
			this.connectionFactory.setUsername(this.username);
			this.connectionFactory.setPassword(this.password);
			this.queueConnection = this.connectionFactory.newConnection();
			this.queueChannel = this.queueConnection.createChannel();
			this.queueChannel.queueDeclare(this.queueName, true, false, false, null);
			this.queueChannel.queueDeclare(this.finishedQueue, true, false, false, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public void configureServer () {
		ServerSocket ss;
		try {
			
			ss = new ServerSocket (this.port);
			log.info("Server has started on port: "+this.port);
			while (true) {
				Socket client = ss.accept(); 
				ObjectOutputStream outputChannel = new ObjectOutputStream (client.getOutputStream());
				ObjectInputStream inputChannel = new ObjectInputStream (client.getInputStream());
				log.info("Channels have been created" );
				
				Message decodedMsg = (Message) inputChannel.readObject();

				
				if (decodedMsg.header.equals("uploadFile")) {
					// is a client
					log.info("client has sent msg");
					String body = (String) decodedMsg.getBody();
					this.queueChannel.basicPublish("", this.queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
					
				}else {
					if (decodedMsg.header.equals("getJob")) {
						log.info("Server has received worker request" );
						GetResponse data = this.queueChannel.basicGet(this.queueName, true);
						byte[] responseByte = data.getBody();
						String body = new String(responseByte, "UTF-8");// worker
						Message newMsg = new Message ("null", body);
						outputChannel.writeObject(newMsg);
						log.info("Server has sent msg to worker");
					}else {
						//uploadFinishedJob
						log.info("Worker FINISHED JOB");
						String body = (String) decodedMsg.getBody();
						this.queueChannel.basicPublish("", this.finishedQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int thread = (int) Thread.currentThread().getId();
		String packetName=ServerSide.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		ServerSide ss = new ServerSide ("localhost", 8080);
	}

}
