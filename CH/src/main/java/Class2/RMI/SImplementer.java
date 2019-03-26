package Class2.RMI;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class SImplementer implements RemoteInt{

	public int getAge(Person p) throws RemoteException {
		
		return p.getAge();
	}
	
	public String getName (Person p) {
		return p.getName();
	}

	@Override
	public Vectores sumaVectores(Vectores v) throws RemoteException {
		
		for (int i =0; i<v.v1.size(); i++) {
			v.v3.add((v.v1.get(i)+v.v2.get(i)));
			
		}
		v.v2 = new ArrayList<Integer>();
		return v;
	}

	public ArrayList<Integer> sumaVectoresConError(ArrayList<Integer> v1, ArrayList<Integer> v2) throws RemoteException {
		ArrayList<Integer> v3 = new ArrayList<Integer>();
		
		for (int i =0; i<v1.size(); i++) {
			v3.add((v1.get(i)+v2.get(i)));
			
		}
		v2 = new ArrayList<Integer>();
		System.out.println(" TAMAÑO de V2: "+v2.size());
		return v3;
	}
}
