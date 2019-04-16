package class5.rabbitmq;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;



public class ClientSide {
	Logger log = LoggerFactory.getLogger(ClientSide.class);
	String ip;
	int port;
	Gson googleJson;
	public ClientSide (String ip, int port) {
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
			
			BodyType1 bt1 = new BodyType1("hola", "/home/soporte/Desktop/basedeejemplo");
			bt1.getData();
			this.googleJson = new Gson();
			String encodedbt1 = this.googleJson.toJson(bt1);
						
			Message msg = new Message ("uploadFile",encodedbt1);
			
			outputObject.writeObject(msg);
			log.info(" LLEGO ");
			
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
		String logname = ClientSide.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",logname);
		ClientSide cs = new ClientSide ("localhost", 8080);
	}

}