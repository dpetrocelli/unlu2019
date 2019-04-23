package class5.rabbitmq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ThreadServer implements Runnable {
	Socket s;
	Logger log;
	Channel queueChannel;
	String queueName;
	String finishedQueue;
	
	
	public ThreadServer(Socket client, Logger log, String queueName2, Channel queueChannel2, String finishedQueue2) {
		this.s = client;
		this.log = log;
		this.finishedQueue = finishedQueue2;
		this.queueName = queueName2;
		this.queueChannel = queueChannel2;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectOutputStream outputChannel = new ObjectOutputStream (this.s.getOutputStream());
			ObjectInputStream inputChannel = new ObjectInputStream (this.s.getInputStream());
			log.info("Channels have been created" );
			boolean finish = false;
			while (!(finish)) {
				
				try {
					Message decodedMsg = (Message) inputChannel.readObject();
					if (decodedMsg.header.equals("uploadFile")) {
						// is a client
						log.info("MSG received from client");
						String body = (String) decodedMsg.getBody();
						this.queueChannel.basicPublish("", this.queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
						
					}else {
						if (decodedMsg.header.equals("getJob")) {
							log.info("Server has received worker request (get job) " );
							GetResponse data = this.queueChannel.basicGet(this.queueName, true);
							byte[] responseByte = data.getBody();
							String body = new String(responseByte, "UTF-8");// worker
							Message newMsg = new Message ("null", body);
							outputChannel.writeObject(newMsg);
							log.info("Server has sent msg to worker");
						}else {
							//uploadFinishedJob
							log.info("Server has received worker -> FINISHED JOB");
							String body = (String) decodedMsg.getBody();
							log.info("MSG:"+body);
							this.queueChannel.basicPublish("", this.finishedQueue, MessageProperties.PERSISTENT_TEXT_PLAIN, body.getBytes());
							log.info("Server has saved worker finished task");
						}
					}
				}catch (Exception e) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				
			
			
		}
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
