package Class2.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class RMIClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
			
			try {
				Registry clientRMI = LocateRegistry.getRegistry("localhost", 9000);
				
				String[] services = clientRMI.list();
				
				for (String service : services) {
					System.out.println(" SERV: "+service);
				}
				RemoteInt cliStub = (RemoteInt) clientRMI.lookup("getClima");
				//Person p = new Person("juancito", "perez", 24);
				
				Vectores v = new Vectores(new ArrayList<Integer>(), new ArrayList<Integer>(), new ArrayList<Integer>());
				
				for (int i = 0; i<5; i++) {
					
					v.v1.add(i*2);
					v.v2.add(i*3);
				}
				
				System.out.println(" TAMAÑO DE V2 en cliente: " +v.v2.size());
				
				v = cliStub.sumaVectores(v);
				
				for (Integer value : v.v3) {
					System.out.println("value:"+value);
				}
				
				System.out.println(" V2 al volver (cliente) : "+v.v2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
