package Class3;

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

public class Producer {
	public Producer (String ip, int port) {
		
		Hashtable htProp = new Hashtable<>();
		htProp.put(Context.PROVIDER_URL, ip+":"+port);
		htProp.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		
		try {
			InitialContext jndiContext = new InitialContext(htProp);
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) jndiContext.lookup("java:comp/DefaultJMSConnectionFactory");
			Queue queue = (Queue) jndiContext.lookup("lujan1030");
			QueueConnection qconn = qcf.createQueueConnection();
			
			QueueSession qs = qconn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			TextMessage tm = qs.createTextMessage("msg string");
			
			QueueSender qsender = qs.createSender(queue);
			
			qsender.setDeliveryMode(DeliveryMode.PERSISTENT);
			
			qsender.send(tm);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Producer p = new Producer ("127.0.0.1", 8080);

	}

}
