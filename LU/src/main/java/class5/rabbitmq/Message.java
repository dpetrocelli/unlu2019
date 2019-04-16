package class5.rabbitmq;

import java.io.Serializable;

public class Message implements Serializable {
	String header;
	String body;
	
	public Message(String header, String body) {
		super();
		this.header = header;
		this.body = body;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
