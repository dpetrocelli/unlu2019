package Class6.DistributedSynchronization;

public class ServerMain {

	public static void main(String[] args) {
		ServerStarter ss = new ServerStarter("localhost", "localhost|RequestQueue-server1");
		// RequestQueue-server1
		Thread ssThread = new Thread (ss);
		ssThread.start();
		
		
		ServerStarter ss2 = new ServerStarter("localhost", "localhost|RequestQueue-server2");
		Thread ssThread2 = new Thread (ss2);
		ssThread2.start();
		
	}

}
