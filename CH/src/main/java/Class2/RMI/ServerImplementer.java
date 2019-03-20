package Class2.RMI;

import java.rmi.RemoteException;

import resto.IRemote;

public class ServerImplementer implements IRemote{

	public String getWeather(String city) throws RemoteException {
		
		return "nublado";
	}

	public Person changePerson(Person p, int age) throws RemoteException {
		// TODO Auto-generated method stub
		p.setAge(age);
		return p;
	}

}
