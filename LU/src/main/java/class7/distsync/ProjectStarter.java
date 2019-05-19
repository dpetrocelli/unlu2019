package class7.distsync;

public class ProjectStarter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSideMain ssm = new ServerSideMain("localhost", "server1", "10");
		Thread ssmThread = new Thread (ssm);
		ssmThread.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerSideMain ssm2 = new ServerSideMain("localhost", "server2", "35");
		Thread ssm2Thread = new Thread (ssm2);
		ssm2Thread.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerSideMain ssm3= new ServerSideMain("localhost", "server3", "183");
		Thread ssm3Thread = new Thread (ssm3);
		ssm3Thread.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ServerSideMain ssm4= new ServerSideMain("localhost", "server4", "1834");
		Thread ssm4Thread = new Thread (ssm4);
		ssm4Thread.start();
		
	}

}
