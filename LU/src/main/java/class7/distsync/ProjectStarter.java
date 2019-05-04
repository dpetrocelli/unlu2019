package class7.distsync;

public class ProjectStarter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSideMain ssm = new ServerSideMain("localhost", "server1");
		Thread ssmThread = new Thread (ssm);
		ssmThread.start();
		
	}

}
