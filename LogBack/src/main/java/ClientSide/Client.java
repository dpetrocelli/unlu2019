package ClientSide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import utils.Message;
import utils.VideoChunk;


public class Client {
	private final Logger log = LoggerFactory.getLogger(Client.class);
	String serverIP;
	int serverPort;
	int chunkFragment;
	String user; 
	Gson gson;
	public Client(int chunkFragment, String serverIP, int port, String user){
		this.serverIP = serverIP;
		this.serverPort = port;
		this.chunkFragment=chunkFragment;
		this.user = user;
		gson = new Gson();
		
		try {
			
			//[STEP 0] - Connect to the RabbitMQServer
			Socket s = new Socket(this.serverIP,this.serverPort);
			log.info("Client Uploader connected to server");
			ObjectInputStream inputChannel = new ObjectInputStream (s.getInputStream());
			ObjectOutputStream outputChannel = new ObjectOutputStream (s.getOutputStream());
			
			// [STEP 1] - Define basic paths 
			String baseAppPath = "/home/soporte/Documents/unluLujanRabbit/";
			String FFMpegBasePath = baseAppPath+"binaries/";
			String path = baseAppPath+"videos/";
			log.info("Base path and names has been defined" );
			
			File pathFiles = new File(path);
	        File [] files = pathFiles.listFiles();
			
	        // [STEP 2] - For each file, obtain duration and split in parts 
	        for (File file : files) {
	        	
	        	
	        	String videoName = file.getName();
	        	videoName = videoName.replaceFirst("[.][^.]+$", "");
                log.info("Video has found and its name is: "+videoName);
                // [STEP 2.1] - Obtain duration 
                float videoDuration = this.obtainVideoDuration (FFMpegBasePath, file.toString());
                log.info("Video Duration: "+videoDuration);
                // [STEP 2.2] - Split into chunks
                String outputChunk= "";
                String part = "";
                int fragments = (int) videoDuration/this.chunkFragment;
                //for (int i=0; i < fragments; i++) {
                for (int i=0; i < 4; i++) {
                	part = videoName+"-part"+i+".mp4";
                	outputChunk = baseAppPath+"splittedVideos/"+part;
                	String ffmpegParams = FFMpegBasePath+"ffmpeg -y -i "+file+" -ss "+(i*this.chunkFragment)+" -t "+this.chunkFragment+ " "+outputChunk;
                	//log.info("Chunk: "+ffmpegParams);
                	this.splitVideo(ffmpegParams);
                	// STEP [2.3] - Once obtained, convert to Java Message and sent to RabbitMQ server
                	// First read from file created
                	VideoChunk vc = new VideoChunk(part, null, outputChunk);
            		vc.readFromDisk();
            		log.info("Video "+vc.getChunkName()+" has been prepaired");
            		// Once finished, send the message to the server
            		String objectCompressed = gson.toJson(vc);
            		
            		Message msg = new Message("uploadChunk", objectCompressed);
            		outputChannel.writeObject(msg);
            		log.info("Video "+vc.getChunkName()+" has been sent to the server");
            		String result = (String) inputChannel.readObject();
                }
			} 
			//outputChannel.println("I'm the new client ");
			//log.info(inputChannel.readLine());
			
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
	}
	
	private void splitVideo (String cmdString) throws IOException{
        
        try {
			Process shellProcess = Runtime.getRuntime().exec(cmdString);
			shellProcess.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	private float obtainVideoDuration(String FFMpegBasePath, String videoPath) {
        String task = FFMpegBasePath+"ffprobe -v error -show_entries format=duration "+videoPath+" -of default=noprint_wrappers=1:nokey=1";
        Process shellProcess;
        float duration = 0;
        try {
            shellProcess = Runtime.getRuntime().exec(task);
            //Read Process Standart Output
            BufferedReader outputFromTerminal = new BufferedReader(new InputStreamReader(shellProcess.getInputStream()));
            String line;
            while ((line = outputFromTerminal.readLine()) != null)     duration=Float.parseFloat(line);
            outputFromTerminal.close();
                
           
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return duration;
    }
    
	public static void main(String[] args) {
		int thread = (int) Thread.currentThread().getId();
		String packetName=Client.class.getSimpleName().toString()+"-"+thread;
		System.setProperty("log.name",packetName);
		Client c = new Client (10,"localhost", 9000,"david");
	}
}
