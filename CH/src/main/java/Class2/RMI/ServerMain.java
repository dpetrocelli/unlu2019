package Class2.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;

import resto.IRemote;

public class ServerMain {

	public static void main(String[] args) {
		
		
		
		try {
			
			SImplementer si = new SImplementer();
			System.out.println(" Server Implementer has been created");
			
			Registry serverRmiRegistry = LocateRegistry.createRegistry(9000);
			System.out.println(" Server RMI has started on port 9000");
			
			RemoteInt serverStub = (RemoteInt) UnicastRemoteObject.exportObject(si, 8000);
			System.out.println(" Server Object has published through IRemote");
			
			serverRmiRegistry.rebind("getClima", serverStub);
			System.out.println(" Server has bind service and Name");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
