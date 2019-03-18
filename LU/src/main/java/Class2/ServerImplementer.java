package Class2;

import java.rmi.RemoteException;

public class ServerImplementer implements IRemote{

	public String getWeather(String city) throws RemoteException {
		
		return "nublado";
	}

}
