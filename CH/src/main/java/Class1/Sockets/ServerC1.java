package Class1.Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerC1 {
	int port;
	
	public ServerC1 (int port) {
		this.port = port;
		try {
			ServerSocket ss = new ServerSocket (this.port);
			System.out.println(" Server has started on port: "+port);
			int counter = 0;
			while (true) {
				Socket client = ss.accept();
				counter++;
				System.out.println("Client Nro:"+counter);
				// THREAD
				ThreadServer ts = new ThreadServer (client);
				Thread tsThread = new Thread (ts);
				tsThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(" Socket on port "+port+" is used ");
		}
	}
	public static void main(String[] args) {
		ServerC1 s = new ServerC1(9000);
	}
}
