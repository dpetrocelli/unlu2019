package Class2;

import java.rmi.RemoteException;

public class ServerImplementer implements RemoteInt {

	public String getName(Person p) throws RemoteException {
		// TODO Auto-generated method stub
		return p.getName();
	}

}
