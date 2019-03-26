package Class2;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry clientRMI = LocateRegistry.getRegistry("localhost", 9000);
			System.out.println(" Cliente conectado ok");
			
			//String[] services = clientRMI.list();
			
			RemoteInt ri = (RemoteInt) clientRMI.lookup("nosdormimos");
			
			pi pix = new pi ();
			
			int suma = (Integer) pix.ejecutar();
			// int suma = (Integer) ri.funcion(pi);
			Person p = new Person("david", "petro", 115);
			
			String name = ri.getName(p);
			System.out.println("NAME: "+name);
			/*
			for (String service : services) {
				System.out.println(" SERV: "+service);
			}*/
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
