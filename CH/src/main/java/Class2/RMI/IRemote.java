package Class2.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemote extends Remote{
	public String getWeather (String city) throws RemoteException;
	public Person changePerson (Person p, int age) throws RemoteException;
}
