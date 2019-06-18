package ClassFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {
	int port; 
	public ServerSocketTest (int port) {
		this.port = port;
		this.startServer();
		
	}
	
	private void startServer() {
		// TODO Auto-generated method stub
		
			ServerSocket ss = null;
			try {
				ss = new ServerSocket (this.port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (true) {
				try {
					Socket s = ss.accept();
					BufferedReader inputChannel = new BufferedReader (new InputStreamReader (s.getInputStream()));
					PrintWriter outputChannel = outputChannel = new PrintWriter (s.getOutputStream(), true);
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}

	public static void main(String[] args) {
		ServerSocketTest s = new ServerSocketTest(9000);
	}

}
