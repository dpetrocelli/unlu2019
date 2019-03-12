package Class1.Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientC1 {
	
	public ClientC1 (String serverIp, int serverPort) {
		try {
			Socket s = new Socket (serverIp, serverPort);
			System.out.println(" Client Connected to Server");
			BufferedReader inputChannel = new BufferedReader (new InputStreamReader (s.getInputStream()));
			PrintWriter outputChannel = new PrintWriter (s.getOutputStream(), true);
			
			outputChannel.println("msg from client");
			
			String msgFromServer= inputChannel.readLine();
			System.out.println(" Msg from ServeR: "+msgFromServer);
			
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
		ClientC1 c = new ClientC1 ("localhost", 9000);

	}

}
