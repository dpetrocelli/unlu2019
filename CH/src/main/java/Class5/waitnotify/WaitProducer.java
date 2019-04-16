package Class5.waitnotify;

public class WaitProducer implements Runnable{
	Book book;
	
	public WaitProducer (Book book) {
		this.book = book;
	}
	@Override
	public void run() {
		while (true) {
			this.book.writeContent("hola" );
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
