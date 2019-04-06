package JMSClass;

import java.util.ArrayList;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

public class ThreadAsync implements Runnable{
	
	QueueSession qs; 
	Queue queue;
	ArrayList<String> msgs; 
	QueueConnection qc;
	ThreadAsync (QueueConnection qc, QueueSession qs, Queue queue, ArrayList<String> msgs){
		this.qs = qs;
		this.queue = queue;
		this.msgs = msgs;
		this.qc = qc;
		
	}
	
	@Override
	public void run() {
		QueueReceiver qr;
		try {
			qr = this.qs.createReceiver(this.queue);
			while (true){
				
				
				this.qc.start();
				TextMessage tm;
				while (true){
					tm = (TextMessage) qr.receiveNoWait();
					if (tm != null){
						break;
					}
					Thread.sleep(1000);
					
				}
				System.out.println("MSG "+tm.getText());
				//tm.acknowledge();
			}
		} catch (JMSException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
