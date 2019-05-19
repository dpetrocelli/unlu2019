package class7.distsync;

public class TestStarter {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		TestProducer tp = new TestProducer();
		Thread tpThread = new Thread (tp);
		tpThread.start();
		
		
		TestProducer tp2 = new TestProducer();
		Thread tp2Thread = new Thread (tp2);
		tp2Thread.start();
		
	}

}
