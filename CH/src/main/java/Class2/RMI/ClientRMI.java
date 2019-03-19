package Class2.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Registry clientRegistry = LocateRegistry.getRegistry("127.0.0.1", 9000);
			
			String[] services = clientRegistry.list();
			
			for (String string : services) {
				System.out.println("SERV: "+string);
			}
			
			IRemote remoteInterface = (IRemote) clientRegistry.lookup("getClima");
			
			String weather = remoteInterface.getWeather("Buenos Aires");
			System.out.println("weather is: "+weather);
			
			Person p = new Person("david", "petrocelli", 29);
			
			p = remoteInterface.changePerson(p, 48);
			
			System.out.println("Person age: "+p.getAge());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
