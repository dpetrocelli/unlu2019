package Class3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GlassfishAdmin {
	public GlassfishAdmin (String path) {
		try {
			boolean exit = false;
			String optionSelected = null;
			while (!exit) {
				
				System.out.println(" INGRESE OPCION DE GLASSFISH");
				System.out.println(" ---------------------------");
				Thread.sleep(10);
				System.err.println(" 1 - List domains");
				System.err.println(" 2 - Start domain");
				System.err.println(" 3 - Stop Domain");
				System.err.println(" 9 - Exit");
				Scanner sc = new Scanner(System.in);
				int option = sc.nextInt();
				
				switch (option) {
				case 1:
					optionSelected = "list-domains";
					break;
				case 2:
					optionSelected = "start-domain domain1";
					break;
				case 3:
					optionSelected = "stop-domain domain1";
					break;
				case 9:
					exit = true;
					break;
				default:
					System.out.println(" option not supported");
				}
			
				Process p = Runtime.getRuntime().exec(path+optionSelected);
				BufferedReader br = new BufferedReader (new InputStreamReader (p.getInputStream()));
				String line = null;
				while ((line = br.readLine())!= null) {
					System.out.println(" Line: "+line);
				}
				p.waitFor();
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GlassfishAdmin ga = new GlassfishAdmin ("/home/soporte/Documents/glassfish5/bin/asadmin ");
	}

}
