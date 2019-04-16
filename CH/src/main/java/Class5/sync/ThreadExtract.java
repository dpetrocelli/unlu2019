package Class5.sync;

public class ThreadExtract implements Runnable {
	SharedObject so;
	int amount;

	public ThreadExtract(SharedObject so, int i) {
		this.so = so;
		this.amount = i;
	}

	@Override
	public void run() {
		while (true) {
			
			if (this.amount <= this.so.getCurrentBalance()) {
				// ACA ME SAQUEN
				synchronized (this.so) {
					this.so.discMoney(this.amount);
				}
				
			}else{
				System.out.println("[CRIT] - Not enought money - "+this.so.getCurrentBalance());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		
	}

}
