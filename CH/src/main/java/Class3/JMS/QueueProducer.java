package Class3.JMS;

import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class QueueProducer {
	String ip;
	int port;
	
	public QueueProducer (String ip, int port) {
		this.ip = ip;
		this.port = port;
		
		Hashtable htProp = new Hashtable<>();
		htProp.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		htProp.put(Context.PROVIDER_URL, ip+":"+port);
		
		try {
			InitialContext contexto = new InitialContext(htProp);
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) contexto.lookup("java:comp/DefaultJMSConnectionFactory");
			
			QueueConnection qconn = (QueueConnection) qcf.createQueueConnection();
			
			QueueSession qsess = (QueueSession) qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Queue queue = (Queue) contexto.lookup("ch");
			
			QueueSender sender = qsess.createSender(queue);
			sender.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			TextMessage tm = (TextMessage) qsess.createTextMessage("holamsg");
			
			sender.send(tm);
			
			qconn.start();
			
			
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueueProducer qp = new QueueProducer ("127.0.0.1", 8080);
	}

}
