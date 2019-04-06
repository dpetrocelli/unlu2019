package JMSClass;

import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class SimpleSuscriber {
	public SimpleSuscriber () throws NamingException, JMSException{
		// First -> Hash with initial Parameters
				Hashtable base = new Hashtable<>();
				base.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
				base.put(Context.PROVIDER_URL, "127.0.0.1");
				// Second -> Define the Initial context with parameters
				Context jndiContext;
		
					jndiContext = new InitialContext(base);
					// Third -> Start with structure
					
					// TopicConnection Factory
					TopicConnectionFactory tcf = (TopicConnectionFactory) jndiContext.lookup("java:comp/DefaultJMSConnectionFactory");
					// Topic
					Topic top = (Topic) jndiContext.lookup("testtopic");
					
					// TopicConnection
					
					TopicConnection tConn = tcf.createTopicConnection();
					// TopicSession
					TopicSession tSess = tConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
					// Publisher
					
					TopicSubscriber consumer = tSess.createSubscriber(top);
					tConn.start();
					System.out.println("All things created");
					while (true){
							TextMessage tm = (TextMessage) consumer.receive();
							System.out.println("Topic Message received");
							System.out.println("tM is: "+tm.getText());
					}
	}
	public static void main(String[] args) throws NamingException, JMSException {
		// TODO Auto-generated method stub
		SimpleSuscriber sc = new SimpleSuscriber ();
	}

}
