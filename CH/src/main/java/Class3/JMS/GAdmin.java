package Class3.JMS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class GAdmin {

	
	
	public GAdmin () {
		
		String path = "/home/soporte/Documents/glassfish5/bin/asadmin ";
		System.out.println(" Administrador de Glassfish");
		
		boolean option = true;
		while (option) {
			System.out.println(" Ingrese la opción a ejecutar");
			Scanner sc = new Scanner (System.in);
			String cmd = sc.nextLine();
			
			switch (cmd) {
			case "exit":
				option = false;
				break;
			default:
				this.ejecutar(path, cmd);
				break;
			}
		}
		
	}
	
	private void ejecutar(String path, String cmd) {
		
		try {
			Process p = Runtime.getRuntime().exec(path+cmd);
			BufferedReader input = new BufferedReader (new InputStreamReader (p.getInputStream()));
			String line = "";
			while (true) {
				line = input.readLine();
				if (line == null) break;
				System.out.println(" Line: "+line);
			}
			p.waitFor();
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
		GAdmin ga = new GAdmin();
	}

}
