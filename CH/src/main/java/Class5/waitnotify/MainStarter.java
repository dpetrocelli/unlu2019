package Class5.waitnotify;

public class MainStarter {

	public static void main(String[] args) {
		
		Book so = new Book ();
		System.out.println(" Book has been created : ");
		
		WaitProducer prod = new WaitProducer (so);
		Thread prodThread = new Thread (prod);
		prodThread.start();		
		
		
		WaitConsumer cons = new WaitConsumer (so);
		Thread consThread = new Thread (cons);
		consThread.start();
		
		try {
			prodThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
