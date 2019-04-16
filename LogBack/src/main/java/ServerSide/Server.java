package ServerSide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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

import utils.Message;
import utils.VideoChunk;

public class Server {
	int port;
	ObjectInputStream inputChannel;
	ObjectOutputStream outputChannel;
	private final Logger log = LoggerFactory.getLogger(Server.class);
	// Structure for RabbitMQService
	private static String enterQueue = "enterQueue";
	ConnectionFactory factory;
	Connection enterConnection;
	Channel enterChannel;
	String username;
	String password;
	Gson gson;
	public Server(int port) {
		this.port = port;
		gson = new Gson();
		try {
			
			ServerSocket serverS = new ServerSocket(this.port);
			log.info("The server has started on port: "+this.port);
			this.connectToRabbitMQServices ();
			log.info("RabbitMQServices has recently started ");
			String msgIn;		
			while (true) {
				Socket client = serverS.accept();
				log.info("Client Connected from: "+client.getInetAddress()+":"+client.getPort());
				outputChannel = new ObjectOutputStream (client.getOutputStream());
				inputChannel = new ObjectInputStream(client.getInputStream());
				Message msg = (Message) this.inputChannel.readObject();
				VideoChunk vc = gson.fromJson((String) msg.getBody(), VideoChunk.class);
				if (msg.getHeader().equals("uploadChunk")) {
					log.info("Server has received: "+vc.getChunkName());
					byte[] msgForQueueing = vc.getBytes();
					this.enterChannel.basicPublish("", this.enterQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, msgForQueueing);
					log.info(" MSG: "+((VideoChunk) msg.getBody()).getChunkName()+" saved in enteredQueue" );
					outputChannel.writeObject("Received and Saved");
				}else {
					log.info("Server has received getJob request");
					if (this.enterChannel.queueDeclarePassive(this.enterQueue).getMessageCount()>0) {
						log.info("an object has found in queue");
						//data = this.enterChannel.basicGet(this.enterQueue, true);
						GetResponse data = this.enterChannel.basicGet(this.enterQueue,false);
						
						byte[] response = data.getBody();
						/*VideoChunk vx = new VideoChunk(null, null, null);
						vx.fromBytes(response);
						log.info("Object "+vx.getChunkData()+" has been prepaired to be sent");
						msg = new Message (null, response);
						outputChannel.writeObject(msg);
						log.info("Object "+vx.getChunkData()+" has been sent");*/
					}else {
						log.info("No object");
						outputChannel.writeObject(null);
					}
				
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}

	private void connectToRabbitMQServices() {
		this.factory = new ConnectionFactory();
		this.factory.setHost("localhost");
		this.username= "admin";
		this.password = "admin";
		this.factory.setUsername(this.username);
		this.factory.setPassword(this.password);
		try {
			// connection and Channels
			this.enterConnection = this.factory.newConnection();
			this.enterChannel = this.enterConnection.createChannel();
			boolean durable= true;
			this.enterChannel.queueDeclare(this.enterQueue, durable, false, false, null);
	  		log.info("Base queue has been declared");
	  		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		
		String packetName=Server.class.getSimpleName().toString();
		System.setProperty("log.name",packetName);//Defino el nombre del archivo de log
		Server s = new Server(9000);
		}

}
