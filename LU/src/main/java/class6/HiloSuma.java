package class6;

public class HiloSuma implements Runnable{
	ObjetoCompartido oc;
	int valorASumar;
	
	public HiloSuma(ObjetoCompartido oc, int i) {
		this.oc = oc;
		this.valorASumar = i;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this.oc) {
				this.oc.sumarSaldo(this.valorASumar);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

}
