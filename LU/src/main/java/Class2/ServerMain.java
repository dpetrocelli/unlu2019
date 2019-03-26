package Class2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		try {
			// STEP 0 - Instancia del objeto ServerImplementer
			ServerImplementer si = new ServerImplementer();
			ServerImplementer si2 = new ServerImplementer();
			System.out.println(" Implementador ok");
			// STEP 1 - Crear el servidor RMI
			Registry server = LocateRegistry.createRegistry(80);
			System.out.println(" Servicio RMI Iniciado");
			
			RemoteInt serviceA = (RemoteInt) UnicastRemoteObject.exportObject(si, 8000);
			RemoteInt serviceB = (RemoteInt) UnicastRemoteObject.exportObject(si2, 8001);
			System.out.println(" Asignación por interfaz ok");
			server.rebind("nosdormimos", serviceA);
			server.rebind("nos despertamos", serviceB);
			
			System.out.println(" bind de servicio JNDI Ok");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
