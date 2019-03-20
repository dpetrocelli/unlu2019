package resto;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import Class2.RMI.RemoteInt;
import Class2.RMI.SImplementer;

public class ServerRMI {

	public static void main(String[] args) {
		
		
		
		try {
			// STEP 0 - create Server Implementer
			SImplementer si = new SImplementer();
			SImplementer si2 = new SImplementer();
			System.out.println(" OBJ IMP has been created");
			
			// STEP 1 - create Server RMI
			Registry serverRMI = LocateRegistry.createRegistry(9000);
			System.out.println(" Server RMI has started on port 9000");
			
			// STEP 2 - BIND OBJECT 
			RemoteInt stub = (RemoteInt) UnicastRemoteObject.exportObject(si,8000);
			RemoteInt stub2 = (RemoteInt) UnicastRemoteObject.exportObject(si2,8001);
			System.out.println(" Export OBJECT SI in port: 8000");
			
			// STEP 3 - service BIND
			serverRMI.rebind("obtener edad persona", stub);
			serverRMI.rebind("obtener nombre persona", stub);
			System.out.println(" Service published ");
		
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
