package Class2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemote extends Remote{
	public String getWeather (String city) throws RemoteException;
}
