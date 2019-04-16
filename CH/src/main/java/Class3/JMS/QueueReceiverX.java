package Class3.JMS;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class QueueReceiverX {
	String ip;
	int port;
	public QueueReceiverX (String ip, int port) {
		this.ip = ip;
		this.port = port;
		Hashtable htProp = new Hashtable<>();
		htProp.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		htProp.put(Context.PROVIDER_URL, ip+":"+port);
		
		try {
			InitialContext contexto = new InitialContext(htProp);
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) contexto.lookup("java:comp/DefaultJMSConnectionFactory");
			
			QueueConnection qconn = (QueueConnection) qcf.createQueueConnection();
			qconn.start();
			QueueSession qsess = (QueueSession) qconn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			
			Queue queue = (Queue) contexto.lookup("ch");
			// IDEM
			
			QueueReceiver receiver = qsess.createReceiver(queue);
			while (true) {
				TextMessage tm = (TextMessage) receiver.receive();
				
				System.out.println("MSG: "+tm.getText());
				//tm.acknowledge();
			}
		
			
			
			
			
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		QueueReceiverX qrx = new QueueReceiverX ("localhost",8080);
	}

}
