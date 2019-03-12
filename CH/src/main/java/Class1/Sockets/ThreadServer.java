package Class1.Sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable{
	Socket client;
	BufferedReader inputChannel;
	PrintWriter outputChannel;
	
	public ThreadServer (Socket client) {
		this.client = client;
		try {
			this.inputChannel = new BufferedReader (new InputStreamReader (this.client.getInputStream()));
			this.outputChannel = new PrintWriter (this.client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void run() {
		System.out.println(" Client connected from port: "+client.getPort());
			
		String msg;
		try {
			msg = this.inputChannel.readLine();
			System.out.println(" MSG: "+msg);
			msg+=" from the server";
			this.outputChannel.println(msg);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.client.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
}
