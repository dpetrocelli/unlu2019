package Class1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializerToFile {
	public SerializerToFile (String filepath) {
		try {
			FileOutputStream fos = new FileOutputStream(filepath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			Person p = new Person ("david", "petrocelli", 8);
			
			oos.writeObject(p);
			oos.flush();
			oos.close();
			fos.close();
			
			FileInputStream fis = new FileInputStream (filepath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			Person p2 = (Person) ois.readObject();
			System.out.println("Person Name: "+p2.getName());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SerializerToFile stf = new SerializerToFile("src/main/java/saveFile");
	}

}
