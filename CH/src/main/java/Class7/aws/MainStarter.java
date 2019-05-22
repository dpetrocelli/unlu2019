package Class7.aws;

public class MainStarter {

	public static void main(String[] args) {
		System.out.println("Trying to connect to AWS (SSH)");
		JschActivities jsch = new JschActivities("/home/soporte/Desktop/unlu-lujan-keypair.pem", "ec2-35-171-153-12.compute-1.amazonaws.com", 22, "ubuntu", "", "", "");
		jsch.configureExecSession();

	}

}
