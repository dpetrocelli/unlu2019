package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

public class VideoChunk implements Serializable{
	private static final long serialVersionUID = 1L;
	String chunkName;
	String chunkData;
	String path;
	
	public VideoChunk(String chunkName, String chunkData, String path) {
		super();
		this.path = path;
		this.chunkName = chunkName;
		this.chunkData = chunkData;
	}
	public String getChunkName() {
		return chunkName;
	}
	public void setChunkName(String chunkName) {
		this.chunkName = chunkName;
	}
	public String getChunkData() {
		return chunkData;
	}
	public void setChunkData(String chunkData) {
		this.chunkData = chunkData;
	}
	
	public void readFromDisk () {
		FileReader fr;
		try {
			fr = new FileReader(this.path);
			BufferedReader br = new BufferedReader (fr);
			String content  = "";
			String line = "";
			while ((line = br.readLine())!= null) {
				content+=line;
			}
			this.chunkData = content;
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveToDisk () {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(this.path));
			writer.write(this.chunkData);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	

	public byte[] getBytes () {
		String result = "";
		result+=this.chunkName+"|";
		result+=this.chunkData+"|";
		result+=this.path;
		
		return result.getBytes();
		
	}
	
	public void fromBytes (byte[] msg) {
		String localMsg;
		try {
			localMsg = new String (msg,"UTF-8");
			String[] msgParts = localMsg.split(Pattern.quote("|"));
			this.chunkName = new String (msgParts[0]);
			System.out.println(this.chunkName);
			this.chunkData = new String (msgParts[1]);
			
			this.path = new String (msgParts[2]);
			System.out.println(this.path);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
