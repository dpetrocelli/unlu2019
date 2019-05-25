package class7.distsync;

import java.io.Serializable;

public class Servers implements Serializable{
	String ip;
	String queueName;
	
	
	public Servers(String ip, String queueName) {
		super();
		this.ip = ip;
		this.queueName = queueName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
}
