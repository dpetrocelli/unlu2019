package JMSClass;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class ReceiverAsync {
	ArrayList<String> msgs;
	public ReceiverAsync (){
		this.msgs = new ArrayList<String>();
		
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
			
			QueueSession qs = qc.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			System.out.println(" Queue Session ");
			
			
			// START Manual ASYNC READER 
			ThreadAsync tas = new ThreadAsync(qc, qs, queue, this.msgs);
			Thread tasThread = new Thread(tas);
			tasThread.start();
			
			while (true){
				// verify changes
			}
			//qc.close();
			
			
		} catch (NamingException | JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ReceiverAsync rc = new ReceiverAsync();
	}
}
