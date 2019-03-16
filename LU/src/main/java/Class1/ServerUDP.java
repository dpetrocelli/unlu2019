package Class1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUDP {
	int port;
	int bufferLength;
	public ServerUDP (int i, int bufferLength) {
		this.port = i;
		this.bufferLength = bufferLength;
		this.startServer();
	}
	private void startServer() {
		try {
			DatagramSocket serverUdp = new DatagramSocket(this.port);
			System.out.println("Datagram UDP Server has started on port "+this.port);
			
			// i have bufferlength, still buffer[] creation
			byte[] buffer = new byte[this.bufferLength];
			
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
			
			while (true) {
				
				serverUdp.receive(datagramPacket);
				InetAddress clientUdpAddress = datagramPacket.getAddress();
				int port = datagramPacket.getPort();
				System.out.println("Client - IP: "+datagramPacket.getAddress().getCanonicalHostName()+ " / Puerto: "+datagramPacket.getPort());
				byte[] msg = datagramPacket.getData();
				String msgReceived = new String(msg);
				
				System.out.println("MSG received from client: "+msgReceived);
				msgReceived = "changingData";
				byte[] resp = new byte[1024];
				// response
				DatagramPacket response = new DatagramPacket(resp, resp.length, clientUdpAddress, port);
				serverUdp.send(response);
				System.out.println(" Server has sent msg to client");
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String[] args) {
		ServerUDP sudp = new ServerUDP(9000, 1024);

	}

}
