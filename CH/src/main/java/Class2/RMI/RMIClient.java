package Class2.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			
			try {
				Registry clientRMI = LocateRegistry.getRegistry("localhost", 9000);
				
				String[] services = clientRMI.list();
				
				for (String service : services) {
					System.out.println(" SERV: "+service);
				}
				RemoteInt cliStub = (RemoteInt) clientRMI.lookup("obtener edad persona");
				Person p = new Person("juancito", "perez", 24);
				
				int age = cliStub.getAge(p);
				System.out.println(" AGe is : "+age);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
