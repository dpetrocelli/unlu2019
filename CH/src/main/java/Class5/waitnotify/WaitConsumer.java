package Class5.waitnotify;

public class WaitConsumer implements Runnable{
	Book book;
	
	public WaitConsumer (Book book) {
		this.book = book;
	}
	@Override
	public void run() {
		while (true) {
			this.book.readContent();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
