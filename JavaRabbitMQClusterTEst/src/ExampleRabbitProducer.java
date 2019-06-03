

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ExampleRabbitProducer {
	String rabbitIP;
	
	QueueConn cq;
	
	public ExampleRabbitProducer(String rabbitIP) {
		this.rabbitIP = rabbitIP;
		//this.cq = new QueueConn (this.rabbitIP);
		for (int i=0; i<1000; i++){
			this.cq = new QueueConn (this.rabbitIP);
			try {
			Thread.sleep(100);
			System.out.println("CONNECTION TO RABBIT OK");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		/*
		int option;
		int qMessages;
		BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.println(" choose option");
			System.out.println(" 1 - get messages ");
			System.out.println(" 2 - push messages ");
			
			
			try {
				option=Integer.parseInt(buffer.readLine());
				
				if (option == 1) {
					qMessages = this.cq.queueChannel.queueDeclarePassive(this.cq.queueName).getMessageCount();
					if ((qMessages>0)) {
						for (int i=0; i<qMessages; i++) {
							GetResponse data = this.cq.queueChannel.basicGet(this.cq.queueName, true);
							String msg = new String (data.getBody());
							System.out.println("MSG: "+msg);
							
						}
						
					}else System.err.println("NO MSG in queue");
				}else {
					if (option ==2) {
						System.out.println(" Please input MSG to save in Queue");
						String msg = buffer.readLine();
						this.cq.queueChannel.basicPublish("",this.cq.queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
					}else {
						System.out.println(" please re-input");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		*/
	}

	
		
	
	public static void main(String[] args) {		
		try {
			System.out.println("Ingrese IP del servicio rabbitMQ");
			BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
			String rabbitIP=buffer.readLine();
			ExampleRabbitProducer erp = new ExampleRabbitProducer(rabbitIP);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
