package Class2.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteInt extends Remote {
	
	public int getAge (Person p) throws RemoteException;
	
	public Vectores sumaVectores (Vectores v) throws RemoteException;
	
	public ArrayList<Integer> sumaVectoresConError (ArrayList<Integer> v1, ArrayList<Integer> v2) throws RemoteException;
	
}
