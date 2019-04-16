package Class5.waitnotify;

public class Book {
	// Bank Account
	String buffer;
	boolean full;
	
	public Book () {
		this.buffer = "";
		this.full = false;
		
	}
	
	// Productor
	public synchronized void writeContent(String content) {
		// if boolean -> false -> escribo
		// if boolean -> lleno -> duermo esperando vacio
		while (this.full==true) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// full -> false 0
		System.out.println(" Producer is writing ");
		this.buffer = content;
		this.full = true; // 1
		notifyAll();
	}
	
	public synchronized void readContent () {
		//if boolean -> lleno -> read
		//if boolean -> false -> sleep
		
				while (!(this.full==true)) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// full -> true 1
				System.out.println(" Consumer is reading ");
				System.out.println(" MSG: "+this.buffer);
				this.full = false; // 0
				notifyAll();
	}
	
	
}
