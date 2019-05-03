package class6.waitnotify;

public class Iniciador {

	public static void main(String[] args) {
		ObjetoCompartido oc = new ObjetoCompartido ();
		
		Productor p = new Productor (oc, "hola");
		Thread pThread = new Thread (p);
		
		Consumidor c = new Consumidor (oc);
		Thread cThread = new Thread (c);
		
		
		pThread.start();
		cThread.start();
	}

}
