package Class5.sync;

public class ThreadIncrement implements Runnable {
	SharedObject so;
	int amount;
	
	public ThreadIncrement(SharedObject so, int i) {
		this.so = so;
		this.amount = i;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this.so) {
				this.so.addMoney(this.amount);
			}
			
			
		}
		
		
		
	}

}
