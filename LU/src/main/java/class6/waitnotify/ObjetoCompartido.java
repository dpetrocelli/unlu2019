package class6.waitnotify;

public class ObjetoCompartido {
	String buffer;
	boolean lleno;
	
	public ObjetoCompartido () {
		this.buffer = "";
		this.lleno = false;
	}
	
	public synchronized void escribirMensaje (String msg) {
		try {
			while (true) {
				Thread.sleep(1000);
				while (this.lleno) {
					wait();
				}
				// boolean - > false
				this.buffer = msg;
				this.lleno = true;
				System.out.println("[PROD] - MSG ENviado a la cola");
				notifyAll();
			}
			
		}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
		
		
	}
	
	public synchronized void leerMensaje () {
		try {
			while (true) {
				Thread.sleep(1000);
				
				while (!(this.lleno)) {
					wait();
				}
				// boolean - > false
				//String ret = this.buffer;
				this.lleno = false;
				System.out.println("[CONS] - MSG leido: "+buffer);
				notifyAll();
			}
			
		}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	}
}
