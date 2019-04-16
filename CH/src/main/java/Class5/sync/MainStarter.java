package Class5.sync;

public class MainStarter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SharedObject so = new SharedObject (500);
		System.out.println(" INIT VALUE: "+so.getCurrentBalance());
		ThreadIncrement ti = new ThreadIncrement(so, 100);
		Thread tiThread = new Thread (ti);
		
		ThreadExtract te = new ThreadExtract (so, 250);
		Thread teThread = new Thread (te);
		
		teThread.start();		
		tiThread.start();
		
		try {
			teThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
