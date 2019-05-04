package class7.distsync;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueConn {
	
	String username;
	String password;
	ConnectionFactory connectionFactory;
	Connection queueConnection;
	Channel queueChannel;
	String requestTokenQueue;
	String releaseTokenQueue;
	String sharedPublicValue;
	String updatedServers;
	String activeServers;
	Gson googleJson;
	
	public QueueConn (String ip) {
		
		try {
			this.username = "admin";
			this.password = "admin";
			this.requestTokenQueue = "LU-DISTSYNC-RequestTokenQueue";
			this.releaseTokenQueue = "LU-DISTSYNC-ReleaseTokenQueue";
			this.sharedPublicValue = "LU-DISTSYNC-SharedPublicValue";
			this.updatedServers = "LU-DISTSYNC-UpdatedServers";
			this.activeServers = "LU-DISTSYNC-ActiveServers";
			this.connectionFactory = new ConnectionFactory();
			this.connectionFactory.setHost(ip);
			this.connectionFactory.setUsername(this.username);
			this.connectionFactory.setPassword(this.password);
			this.queueConnection = this.connectionFactory.newConnection();
			this.queueChannel = this.queueConnection.createChannel();
			this.queueChannel.queueDeclare(this.requestTokenQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.releaseTokenQueue, true, false, false, null);
			this.queueChannel.queueDeclare(this.sharedPublicValue, true, false, false, null);
			this.queueChannel.queueDeclare(this.activeServers, true, false, false, null);
			this.queueChannel.queueDeclare(this.updatedServers, true, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
