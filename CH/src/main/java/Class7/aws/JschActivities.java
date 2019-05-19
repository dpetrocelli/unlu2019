package test.mavenproject;





import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;



import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class JschActivities {
	String hostname, username, password, copyFrom, copyTo; 
	int port;
	Session session;
	JSch jsch;
	ChannelSftp sftpChannel;
	Channel channel;
	
	
	public JschActivities (String host, int port, String user, String pwd, String origin, String destination){
    	this.hostname = host;
    	this.username = user;
    	this.password = pwd;
    	this.port = port;
    	this.copyFrom = origin;
    	this.copyTo = destination;
    	this.jsch = null;
    	this.session = null;
    	this.sftpChannel = null;
    	this.channel = null;
    }
	
	private static String readFromKeyboard () throws IOException{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
		return br.readLine();
	}
	
	public void configureFtpSession(){
		jsch = new JSch();
        //Session session = null;
        System.out.println("Trying to connect.....");
        try {
            session = jsch.getSession(username, hostname, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(); 
            
            System.out.println("CONNECTED");
            // ssh -p 22 azureuser@104.43.241.159 + PWD
            // 1 - COPY
            // o 2 - EXEC
            channel = session.openChannel("sftp");
            System.out.println("FTP CHannel Created");
            channel.connect();
            // Cast to ChannelSFTP
            sftpChannel = (ChannelSftp) channel; 
		} catch (JSchException e) {
	        e.printStackTrace();  
	    }
    }
	public void closeFtpSession (){
		sftpChannel.exit();
        session.disconnect();
        System.out.println("Closing session...");
	}
	public void copyFromRemoteServer() throws SftpException{
		sftpChannel.get(copyTo, copyFrom);
        System.out.println("Elements coppied to Manager Node");
	}
	public void copyToRemoteServer () throws SftpException{
		System.out.println("trying to copy");
		sftpChannel.put(copyFrom, copyTo);
        System.out.println("Elements replicated to Azure Node");
    }
	
	public void configureExecSession(String filename){
		jsch = new JSch();
        //Session session = null;
        System.out.println("JSCH CREATED .....");
        try {
            session = jsch.getSession(username, hostname, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            
            session.connect(); 
            System.out.println("EXEC SESSION CONNECTED .....");
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            System.out.println("JSCH as EXEC OK.....");
            // Gets an InputStream for this channel. All data arriving in as messages from the remote side can be read from this stream.
            InputStream in = channelExec.getInputStream();

            // Set the command that you want to execute
            // In our case its the remote shell script
            String commandExec = "sh "+filename;
            
            System.err.println("JSCH PREVIOUS To REMOTE EXC.....");
            
            //channelExec.setCommand("sudo -s apt-get update && sudo -s apt-get install gzip -y -f");
            channelExec.setCommand("sudo -s apt-get update && sudo -s apt-get install default-jdk -y -f");
                       // Execute the command
            channelExec.connect();
            
            //channelExec.setCommand("sudo -s apt-get install default-jre -y -f");
            //channelExec.setCommand("sudo -s ls -a");
            
            // Read the output from the input stream we set above
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            
            //Read each line from the buffered reader and add it to result list
            // You can also simple print the result here 
            try {
            	
				while ((line = reader.readLine()) != null)
				{
				    System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.err.println("END OF EXEC");
            //retrieve the exit status of the remote command corresponding to this channel
            int exitStatus = channelExec.getExitStatus();

            //Safely disconnect channel and disconnect session. If not done then it may cause resource leak
            channelExec.disconnect();
            session.disconnect();

            if(exitStatus < 0){
               // System.out.println("Done, but exit status not set!");
            }
            else if(exitStatus > 0){
               // System.out.println("Done, but with error!");
            }
            else{
               // System.out.println("Done!");
            }
            
		} catch (JSchException e) {
	        e.printStackTrace();  
	    } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
	
	
	
	
	
	
	   
}
