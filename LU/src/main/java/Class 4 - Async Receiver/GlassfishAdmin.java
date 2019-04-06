package JMSClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GlassfishAdmin {
	String basePath; 
	public GlassfishAdmin (String path){
		this.basePath = path;
		
		String localPath = "src/file.sh";
		
		File f = new File(localPath);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write("#!/bin/bash \n");
			fw.write(this.basePath+" \n");
			fw.write("./asadmin start-domain domain1 \n");
			fw.close();
		
			// Creacion del process
			String[] cmdArray = {"/bin/bash", "-c", "bash "+localPath};
			Process xyz = Runtime.getRuntime().exec(cmdArray);
			BufferedReader bf = new BufferedReader (new InputStreamReader (xyz.getInputStream()));
			
			String line;
			String totalLines="";
			while (((line = bf.readLine())!=null)){
				System.out.println("LINE: "+line);
				totalLines+=line+"|";
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
		public static void main(String[] args) {
			GlassfishAdmin ga = new GlassfishAdmin("cd /Users/davidpetrocelli/Desktop/glassfish5/bin/");
		}
}
