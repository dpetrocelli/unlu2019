package class7.distsync;

import java.io.IOException;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

public class ServerSideAddServerThread implements Runnable{
	QueueConn qc;
	String serverName;
	
	public ServerSideAddServerThread(QueueConn qc, String serverName) {
		this.qc = qc;
		this.serverName = serverName;
	}
	@Override
	public void run() {
		
		// AGREGAR
		// 1 opcion -> cola ctivos -> vacia
		
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
