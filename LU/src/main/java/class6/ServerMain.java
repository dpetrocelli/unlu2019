package class6;

public class ServerMain {

	public static void main(String[] args) {
		
		ObjetoCompartido oc = new ObjetoCompartido (500);
		
		HiloSuma hs = new HiloSuma (oc, 100);
		Thread hsThread = new Thread (hs);
				
		HiloResta hr = new HiloResta (oc, 200);
		Thread hrThread = new Thread (hr);
		
		hsThread.start();
		hrThread.start();

	}

}
