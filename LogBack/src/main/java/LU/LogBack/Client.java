package LU.LogBack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Client {
	private final Logger log = LoggerFactory.getLogger(Client.class);
	
	public Client(String serverIp, int port){
		try {
			Socket s = new Socket(serverIp,port);
			log.info("Connected to SV");
			BufferedReader inputChannel = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter outputChannel = new PrintWriter (s.getOutputStream(), true);
	
			outputChannel.println("I'm the new client ");
			log.info(inputChannel.readLine());
			
		} catch (Exception e) {
			
		}
		
	}
	public static void main(String[] args) {
		int thread = (int) Thread.currentThread().getId();
		String packetName=Client.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		Client c = new Client ("localhost", 9000);
	}
}
