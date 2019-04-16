package class5.rabbitmq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.derby.impl.sql.catalog.SYSPERMSRowFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;



public class WorkerSide {
	Logger log = LoggerFactory.getLogger(ClientSide.class);
	String ip;
	int port;
	Gson googleJson;
	
	public WorkerSide (String ip, int port) {
		this.ip= ip;
		this.port = port;
		this.startClient();
	}
	private void startClient() {
		try {
			log.info("Client has started");
			Socket s = new Socket (this.ip, this.port);
			log.info("Client connected to the server");
			ObjectInputStream inputObject = new ObjectInputStream (s.getInputStream());
			ObjectOutputStream outputObject = new ObjectOutputStream (s.getOutputStream());
			
			/*BodyType1 bt1 = new BodyType1("hola", "/home/soporte/Desktop/basedeejemplo");
			bt1.getData();
			this.googleJson = new Gson();
			String encodedbt1 = this.googleJson.toJson(bt1);
						
			Message msg = new Message ("uploadFile",encodedbt1);
			
			outputObject.writeObject(msg);
			log.info(" LLEGO ");
			*/
			log.info("worker has created Request");
			Message msg = new Message ("getJob",null);
			outputObject.writeObject(msg);
			log.info(" worker has sent request");
			try {
				Message responseMsg = (Message) inputObject.readObject();
				log.info(" Worker has received task" );
				String body = (String) responseMsg.getBody();
				this.googleJson = new Gson();
				BodyType1 bt1 = this.googleJson.fromJson(body, BodyType1.class);
				bt1.setPath("/home/soporte/Desktop/cambiar");
				bt1.setData();
				log.info("Worker saved file" );
				Thread.sleep(10000);
				String encodedBt1 = this.googleJson.toJson(bt1);
				Message saveInServer = new Message ("uploadFinishedJob", encodedBt1);
				log.info("MSG to SERVER: "+saveInServer);
				outputObject.writeObject(saveInServer);
				log.info(" MSG to server ok" );
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int thread = (int) Thread.currentThread().getId();
		String logname = WorkerSide.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",logname);
		WorkerSide ws = new WorkerSide ("localhost", 8080);
	}

}