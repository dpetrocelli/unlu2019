package resto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GlassfishAdmin {
	
	public GlassfishAdmin () {
		System.out.println("Define asadmin path: ");
		Scanner scan= new Scanner(System.in);
	    //String text= scan.nextLine();
		String path = "/home/soporte/Documents/glassfish5/bin/";
		
		byte option = -1;
		String command = path;
		
		while (option!= 0) {
			String commandChoice = null;
			System.out.println(" Choose an option");
			System.out.println(" 1 - List domains ");
			System.out.println(" 2 - Start Domain N#");
			System.out.println(" 3 - Stop Domain N#");
			System.out.println(" 4 - List JMS resources");
			System.out.println(" 5 - Create Queue");
			option = (byte) scan.nextInt();
			switch (option) {
			case 1:
				commandChoice=command+"asadmin list-domains";
				this.runCommand(commandChoice);
				break;
			
			case 2: 
				commandChoice=command+"asadmin start-domain domain1";
				this.runCommand(commandChoice);
				break;
				
			case 3: 
				commandChoice=command+"asadmin stop-domain domain1";
				this.runCommand(commandChoice);
				break;
			
			case 4: 
				commandChoice=command+"asadmin list-jms-resources";
				this.runCommand(commandChoice);
				break;
			
			case 5: 
				System.out.println(" Input Queue Name");
				Scanner console = new Scanner(System.in);
				String name = console.nextLine();
				commandChoice=command+"asadmin create-jms-resource --restype javax.jms.Queue --property Name=PhysicalQueue jms/"+name;
				this.runCommand(commandChoice);
				break;	
			default:
				System.out.println(" Is not a valid option, re-enter");
				break;
			}
			
		}
		
	    
	}
	
	private void runCommand (String command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            String s;
				while ((s = br.readLine()) != null)
	                System.out.println("line: " + s);
	        p.waitFor();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public static void main(String[] args) {
		GlassfishAdmin ga = new GlassfishAdmin();

	}

}