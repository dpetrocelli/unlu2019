package LU.LogBack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	int port;
	BufferedReader inputChannel;
	PrintWriter outputChannel;
	private final Logger log = LoggerFactory.getLogger(Server.class);
	
	public Server(int port) {
		this.port = port;
		try {
			
			ServerSocket serverS = new ServerSocket(this.port);
			log.info("The server has started on port: "+this.port);
			String msgIn;		
			while (true) {
				Socket client = serverS.accept();
				log.info("Client Connected from: "+client.getInetAddress()+":"+client.getPort());
				outputChannel = new PrintWriter (client.getOutputStream(), true);//Flujo de Salida del Cliente
				inputChannel = new BufferedReader (new InputStreamReader (client.getInputStream()));//Flujo Entrante
				msgIn = this.inputChannel.readLine();
				log.info("Server has received: "+msgIn);
				outputChannel.println("MSG : "+msgIn+" has been received from Client - This is the reply");
				
				
				
			}
		} catch (Exception e) {
			System.out.println("Socket on port \"+port+\" is used");
			System.out.println(e.getMessage());
		}
		
		
	}

	public static void main(String[] args) {
		
		String packetName=Server.class.getSimpleName().toString();
		System.setProperty("log.name",packetName);//Defino el nombre del archivo de log
		Server s = new Server(9000);
		}

}
