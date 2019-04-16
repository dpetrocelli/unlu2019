package WorkerSide;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ServerSide.Server;
import utils.Message;
import utils.VideoChunk;


public class Worker {
	String serverIp;
	int serverPort;
	private final Logger log = LoggerFactory.getLogger(Server.class);
		public Worker (String ip, int port) {
			this.serverIp = ip;
			this.serverPort = port;
			this.activateWorker();
		}
			
		private void activateWorker() {
			
			
			while (true) {
				
					// [STEP-1] - Looping obtaining object
				try {
					//[STEP 1.1] - Connect to the RabbitMQServer
					Socket s = new Socket(this.serverIp,this.serverPort);
					log.info("Worker Node has connected to the server");
					ObjectInputStream inputChannel = new ObjectInputStream (s.getInputStream());
					ObjectOutputStream outputChannel = new ObjectOutputStream (s.getOutputStream());
					
					// [STEP 1.1] - Define basic paths 
					String baseAppPath = "/home/soporte/Documents/unluLujanRabbit/";
					String FFMpegBasePath = baseAppPath+"binaries/";
					String path = baseAppPath+"videos/";
					log.info("Base path and names has been defined" );
					
					// [STEP 1.2] - Create msg only with headers
					Message msg = new Message("getJob", null);
					outputChannel.writeObject(msg);
            		log.info("getJob has been sent to the server");
            		msg = (Message) inputChannel.readObject();
            		
            		log.info("Message "+ ((VideoChunk) msg.getBody()).getChunkData()+" has been received from the server");
					// [STEP 1.3] - Save to disk to apply filter
            		VideoChunk vc = (VideoChunk) msg.getBody();
            		vc.saveToDisk();
            		
            		String exportFilteredName = vc.getChunkName().replaceFirst("[.][^.]+$", "");
            		exportFilteredName = exportFilteredName+"-filtered.mp4";
            		String ffmpegParameters = "ffmpeg -y -i "+vc.getChunkName()+" -vf hue=s=0 -c:a copy "+exportFilteredName;
					this.applyFilteredsplitVideo(ffmpegParameters);
					log.info("Filter Applied on video "+exportFilteredName);
					// [STEP 1.4] - Apply ffmpeg filter
					
					
					
				}catch (Exception e) {
					System.err.println(" NOT a valid MSG ");
				}
				
		        
		       try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		
	private void applyFilteredsplitVideo (String cmdString) throws IOException{
	        
	        try {
				Process shellProcess = Runtime.getRuntime().exec(cmdString);
				shellProcess.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
		
		
	
	public static void main(String[] args) throws IOException {
		String packetName=Worker.class.getSimpleName().toString();
		System.setProperty("log.name",packetName);
		System.out.println("Please Input Server IP");
		Scanner keyboard = new Scanner(System.in);
		String ipSpringServer = keyboard.nextLine();
		System.out.println("Please intput Server port");
		int port = Integer.parseInt(keyboard.nextLine());
		Worker wk = new Worker(ipSpringServer,port);
		
		/*
		 * Worker activities:
		 * 1. First download Job (get to enterQueue)
		 * 2. Rearm MSG where
		 * 		-> String userAndQueueName
		 * 		-> filename (input)
		 * 		-> #part
		 * 		-> total parts
		 * 		-> typeOfService (current - FFMPEg compression
		 * 		-> binary data[]
		 * 		-> profile to apply (params)
		 * 
		 * 3. Save binary in localdisk
		 * 4. Apply filter (ffmpeg) and save in new place
		 * 5. Create new Message Structure (new encoded file)
		 * 6. create POST request to queue identify by String userAndQueueName
		 * All done.
		 */		
			
	}
}