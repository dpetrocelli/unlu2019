package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class UtilsConnection {
	String ip;
	String username;
	String password;
	ConnectionFactory connectionFactory;
	Connection queueConnection;
	Channel queueChannel;
	String requestQueue;
	String releasedQueue;
	String activeServersQueue;
	String updatedServersQueue;
	String sharedDataQueue;
	private Gson googleJson;
	
	public UtilsConnection (String ip) {
		this.ip = ip;
		this.username = "admin";
		this.password = "admin";
		this.requestQueue = "DistSync-RequestQueue";
		this.releasedQueue = "DistSync-ReleasedQueue";
		this.activeServersQueue = "DistSync-ActiveServers";
		this.updatedServersQueue = "DistSync-UpdatedServers";
		this.sharedDataQueue = "DistSync-SharedGlobalData";
		this.googleJson = new Gson();
		this.configureConnectionToRabbit();
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
			this.queueChannel.queueDeclare(this.requestQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.releasedQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.activeServersQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.sharedDataQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.updatedServersQueue, true, false, false, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
