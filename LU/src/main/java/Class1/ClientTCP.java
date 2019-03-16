package Class1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTCP {
	
	public ClientTCP (String ip, int port) {
		try {
			Socket s = new Socket (ip, port);
			System.out.println(" Client connection ok");
			BufferedReader inputChannel = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter outputChannel = new PrintWriter (s.getOutputStream(),true);
			
			outputChannel.println ("msg from client");
			
			String response = inputChannel.readLine();
			
			System.out.println(" Respons from serveR: "+response);
			s.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		ClientTCP ctcp = new ClientTCP ("localhost", 9000);

	}

}
