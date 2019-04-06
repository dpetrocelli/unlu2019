package JMSClass;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class Browser {
	public Browser (){
		Hashtable ht = new Hashtable<>();
		ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		ht.put(Context.PROVIDER_URL, "127.0.0.1:8080");
		Context jndiContext = null;
		try {
			 jndiContext = new InitialContext(ht);
			
			QueueConnectionFactory qcf = (QueueConnectionFactory) jndiContext.lookup("java:comp/DefaultJMSConnectionFactory");
			System.out.println(" Queue Connection FAct created");
			
			Queue queue = (Queue) jndiContext.lookup("queue1");
			System.out.println(" Queue Created");
			
			QueueConnection qc = qcf.createQueueConnection();
			System.out.println(" Queue Connection ");
			
			QueueSession qs = qc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println(" Queue Session ");
			
			QueueBrowser qw = qs.createBrowser(queue);
			qc.start();
			Enumeration msgs = qw.getEnumeration();
			System.out.println(" .............");
			while (msgs.hasMoreElements()){
				TextMessage tm = (TextMessage) msgs.nextElement();
				System.out.println("MSG: "+tm.getText());
			}
			System.out.println(" .............");
			
			
			qc.close();
			
			
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("BOOTING Browser");
		Browser b = new Browser();
	}
}
