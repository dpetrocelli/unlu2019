package class4.rabbitmq;

public class Message {
	String header;
	Object body;
	
	public Message(String header, Object body) {
		super();
		this.header = header;
		this.body = body;
		// video -> clase -> objeto
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
	public void setBody(Object body) {
		this.body = body;
	}
	
}
