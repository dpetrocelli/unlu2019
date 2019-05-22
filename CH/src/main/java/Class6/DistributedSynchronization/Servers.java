package Class6.DistributedSynchronization;

import java.io.Serializable;

public class Servers implements Serializable{
	String serverip;
	String queueName;
	
	public Servers(String serverip, String queueName) {
		this.serverip = serverip;
		this.queueName = queueName;
	}

}
