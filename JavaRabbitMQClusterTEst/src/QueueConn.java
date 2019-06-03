


import java.io.IOException;
import java.util.concurrent.TimeoutException;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class QueueConn {
	
	String username;
	String password;
	ConnectionFactory connectionFactory;
	Connection queueConnection;
	Channel queueChannel;
	String queueName;
	
	
	public QueueConn (String ip) {
		
		try {
			this.username = "admin";
			this.password = "admin";
			this.queueName = "SD-testqueue";
			this.connectionFactory = new ConnectionFactory();
			this.connectionFactory.setHost(ip);
			this.connectionFactory.setPort(5672);
			this.connectionFactory.setUsername(this.username);
			this.connectionFactory.setPassword(this.password);
			this.queueConnection = this.connectionFactory.newConnection();
			this.queueChannel = this.queueConnection.createChannel();
			this.queueChannel.queueDeclare(this.queueName, true, false, false, null);
			
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
