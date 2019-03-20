package resto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientUDP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			DatagramSocket clientds = new DatagramSocket();
			byte[] msg = new byte[1024];
			msg = "msg to send".getBytes();
			InetAddress ipServer = InetAddress.getByName("127.0.0.1");
			DatagramPacket dp = new DatagramPacket(msg, msg.length, ipServer, 9000);
			System.out.println(" MSG prepared");
			clientds.send(dp);
			System.out.println(" MSG has been sent");
			
			byte[] buffer = new byte[1024];
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
			System.out.println(" READING ");
			clientds.receive(datagramPacket);
			byte[] msg2 = datagramPacket.getData();
			String msgReceived = new String(msg2);
			
			System.out.println("MSG received from esrver: "+msgReceived);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
