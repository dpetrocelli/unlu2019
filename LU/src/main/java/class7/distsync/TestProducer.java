package class7.distsync;

import java.io.IOException;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.GetResponse;

public class TestProducer implements Runnable{
	
	public TestProducer() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		QueueConn qc = new QueueConn ("localhost");
		/*try {
			qc.queueChannel.queueDeclare("resource.semaphore", true, false, false, null);
			String message = "resource";
			qc.queueChannel.basicPublish("", "resource.semaphore", null, message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		*/
		try {
			qc.queueChannel.queueDeclare("resource.semaphore", true, false, false, null);
			qc.queueChannel.basicQos(1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
					
					GetResponse data = qc.queueChannel.basicGet("resource.semaphore", true);
				
					System.out.println(" Accedi al token yo thread "+Thread.currentThread().getId());
								
					
				}
			 catch (IOException e) {
				// TODO Auto-generated catch block
				 System.out.println(" SIN ACCESO TOKEN yo"+Thread.currentThread().getId());
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
