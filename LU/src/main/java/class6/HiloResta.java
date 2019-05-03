package class6;

public class HiloResta implements Runnable{
	ObjetoCompartido oc;
	int valorARestar;
	
	public HiloResta(ObjetoCompartido oc, int i) {
		this.oc = oc;
		this.valorARestar = i ;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this.oc) {
				if (this.valorARestar <= this.oc.saldo) {
					this.oc.restarSaldo(this.valorARestar);
				} else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(" ERROR , no hay suficiente saldo" );
				}
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
