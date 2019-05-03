package class6;

public class ObjetoCompartido {
	int saldo;
	
	public ObjetoCompartido (int valorInicial) {
		this.saldo = valorInicial;
	}
	
	public void sumarSaldo (int valorSuma) {
		int aux = this.saldo;
		System.out.println("[SUMA] - Valor antes cambio: "+this.saldo);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		aux+= valorSuma;
		this.saldo = aux;
		System.out.println("[SUMA] - Valor DESPUES cambio: "+this.saldo);
		
	}
	
	public void restarSaldo (int valorResta) {
		System.err.println("[RESTA] - Valor antes cambio: "+this.saldo);
		try {
			
			Thread.sleep(1300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.saldo-=valorResta;
		System.err.println("[RESTA] - Valor DESP cambio: "+this.saldo);
	}
}
