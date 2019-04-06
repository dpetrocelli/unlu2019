package JMSClass;

import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/*
import java.util.HashMap;
import java.util.Hashtable;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.messaging.jmq.jmsserver.core.Session;

public class SimplePublisher {
	public SimplePublisher (){
		
		// First -> Hash with initial Parameters
		Hashtable base = new Hashtable<>();
		base.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		base.put(Context.PROVIDER_URL, "127.0.0.1");
		// Second -> Define the Initial context with parameters
		Context jndiContext;
		try {
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
			TopicPublisher publisher = tSess.createPublisher(top);
			publisher.setDeliveryMode(DeliveryMode.PERSISTENT);
			tConn.start();
			System.out.println("publisher ready: now it going to send");
			TextMessage tm = tSess.createTextMessage("hola");
			publisher.publish(tm);
			System.out.println("publisher send msg");
			
			TextMessage tm2 = tSess.createTextMessage("hola2");
			publisher.publish(tm2);
			System.out.println("publisher send msg");
			publisher.
			TextMessage tm3 = tSess.createTextMessage("hola2");
			publisher.publish(tm3);
			System.out.println("publisher send msg");
			
			while (true);
			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		SimplePublisher sp = new SimplePublisher();

	}

}

package JMS_6_SimpleTopic;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/*
import com.sun.messaging.Topic;
import com.sun.messaging.TopicConnectionFactory;
import com.sun.messaging.jmq.jmsserver.core.Session;
import com.sun.messaging.jms.TopicConnection;
import com.sun.xml.ws.rx.rm.api.ReliableMessagingFeature.DeliveryAssurance;
*/
public class SimplePublisher {
	
	Hashtable ctxProp;
	Context jndiContext;
	ConnectionFactory connFactory;
	Topic topic;
	TopicConnection tCon;
	TopicSession topicSession;
	TopicPublisher topicPublisher;
	
	public SimplePublisher() throws NamingException, JMSException{
		/*
		
		 * CONNECTION FACTORY ( CLIENT --> PROVIDER) 
		 * A connection factory is the object a client uses to create a connection to a provider. 
		 * A connection factory encapsulates a set of connection configuration parameters that 
		 * has been defined by an administrator
		 * A context is a set of bindings that associates names with objects stored in a naming and directory service.
		 * JDNI -> 
		 * Using JNDI, JMS clients can browse a naming service and obtain references to administered objects without 
		 * knowing the details of the naming service or how it is implemented. JMS servers are usually be used in 
		 * combination with a standard JNDI driver 
		 */
		
		
		// Defines properties of JMS Service based on JNDI Naming
		ctxProp = new Hashtable();
		// DEFINE INITCONTEXTFACTORY
		ctxProp.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
		// PROVIDER URL -> IP:PORT where Service is running
		ctxProp.put(Context.PROVIDER_URL, "127.0.0.1:8080");
		jndiContext = null;
		
		// After defining all parameters, create the initialContext
		try {
			jndiContext = new InitialContext(ctxProp);
			System.out.println("Simple Publisher (TOPIC) has Started");
			System.out.println("Connection Factory Created and vinculated by JNDI");
		} catch (NamingException e) {
			System.out.println("Could not create JNDI API" + "context: "+ e.toString());
			e.printStackTrace();
			System.exit(1);
		}
		TopicConnectionFactory connFactory = (TopicConnectionFactory) jndiContext.lookup("java:comp/DefaultJMSConnectionFactory");
		
		System.out.println("Topic Connection Factory has successfully created");
		
		topic = (Topic) jndiContext.lookup("testtopic");

		tCon = (TopicConnection) connFactory.createConnection();

		topicSession = tCon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE); 
		
		topicPublisher = topicSession.createPublisher(topic);
		topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
	}
	
	public void sendTopicMessage (String text) throws JMSException {
    
	        	TextMessage message = topicSession.createTextMessage();
	        	message.setText(text);
	    		topicPublisher.publish(message);
	    		System.out.println("Message sent: "+message.getText());
	    	
	        } 
	

	 public void closeConnections () throws JMSException{
			topicPublisher.close();
            topicSession.close();
            tCon.close();
		 
	 }
	    public static void main(String[] args) throws Exception {
	        SimplePublisher pub = new SimplePublisher();
	        pub.sendTopicMessage("Hi World!");
	        pub.sendTopicMessage("Good Bye !");
	        pub.closeConnections();
	    }
	



}
