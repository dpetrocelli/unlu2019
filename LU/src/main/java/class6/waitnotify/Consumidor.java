package class6.waitnotify;

public class Consumidor implements Runnable {
	ObjetoCompartido oc ;
	
	public Consumidor(ObjetoCompartido oc) {
		// TODO Auto-generated constructor stub
		this.oc = oc;
	}

	@Override
	public void run() {
		this.oc.leerMensaje();
		
	}

}
