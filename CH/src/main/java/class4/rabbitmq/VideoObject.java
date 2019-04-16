package class4.rabbitmq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class VideoObject {
	
	String name;
	String path;
	byte[] data;
	
	public VideoObject (String path) {
		this.path = path;
		this.loadFromDisk();
	}
	public void loadFromDisk () {
		try {
			this.data = Files.readAllBytes(new File(this.path).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void saveToDisk () {
		try (FileOutputStream fos = new FileOutputStream(this.path)) {
			   fos.write(this.data);
			   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
