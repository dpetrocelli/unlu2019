package class6.waitnotify;

public class Productor implements Runnable {
	ObjetoCompartido oc ;
	String msg;
	public Productor(ObjetoCompartido oc, String string) {
		this.oc = oc;
		this.msg = string;
	}

	@Override
	public void run() {
		
		this.oc.escribirMensaje(this.msg);
		
	}

}
