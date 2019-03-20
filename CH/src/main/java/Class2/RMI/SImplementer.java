package Class2.RMI;

import java.rmi.RemoteException;

public class SImplementer implements RemoteInt{

	public int getAge(Person p) throws RemoteException {
		
		return p.getAge();
	}
	
	public String getName (Person p) {
		return p.getName();
	}
}
