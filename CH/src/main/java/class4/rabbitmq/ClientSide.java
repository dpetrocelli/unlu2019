package class4.rabbitmq;

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
			
			VideoObject video = new VideoObject("/home/soporte/Desktop/basedeejemplo");
			log.info("video Object has been created");
			Message msg = new Message ("uploadMessage", video);
			log.info("Base msg created");
			Gson gson = new Gson();
			String msgEncoded = gson.toJson(msg);
			System.out.println("HOLI: "+msgEncoded);
			outputObject.writeObject(msgEncoded);
			log.info("Encoded Msg has been sent");
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
		ClientSide cs = new ClientSide ("localhost", 9000);
	}

}
