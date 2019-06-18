package ClassFinal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class AwsConnect {
	String ip;
	int port;
	String username;
	String password;
	String pathKey;
	String pathDestination;
	String pathOrigin;
	JSch jsch;
	Session session;
	
	public AwsConnect (String ip2, int port2, String username, String password, String pathKey2, String pathDestination2, String pathOrigin) {
		this.ip = ip2;
		this.port = port2;
		this.username = username;
		this.password = password;
		this.pathKey = pathKey2;
		this.pathDestination = pathDestination2;
		this.pathOrigin = pathOrigin;
		this.createJSCH();
	}
	
	
	private void createJSCH() {
		
		
		try {
			System.out.println(" Creating JsCH Object Connection");
			this.jsch = new JSch();
			// Primer parámetro, configuración básica de la sesión 
			this.session = jsch.getSession(this.username, this.ip, this.port);
			System.out.println(" Creating JsCH Session ");
			// Check hostkey
			this.session.setConfig("StrictHostKeyChecking", "no");
	       // Verificar si voy por password o voy por .pem (pub/Priv)
			if (this.password.length()>2) {
	        	this.session.setPassword(password);
	        }else {
	        	this.jsch.addIdentity(this.pathKey);
	        }
			
	        this.session.connect(); 
	        System.out.println(" Session configured and established");
	        
	        
	        // Que camino tomo 
	        // 1) -> ejecutar un comando ssh
	        // 2) -> ....copiar scp
	        
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}

	private void execCommands(String commands) {
		String command = commands;
		
		
		try {
			ChannelExec channelExec = (ChannelExec) this.session.openChannel("exec");
			
			// Accedo al canal de entrada
			InputStream in = channelExec.getInputStream();
			// paso los parámetros y funciones a ejecutar
			channelExec.setCommand(command);
			// Ponelo a ejecutar
			channelExec.connect();
			
			BufferedReader buffer = new BufferedReader (new InputStreamReader (in));
			String line = "";
			
			while ((line = buffer.readLine())!= null) {
				System.out.println(" LINE: " + line);
			}
			
			channelExec.disconnect();
			
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void copyToRemote() {
		// TODO Auto-generated method stub
		  ChannelSftp SftpChannel;
		try {
			SftpChannel = (ChannelSftp) this.session.openChannel("sftp");
			System.out.println("FTP CHannel Created");
	        SftpChannel.connect();
	        
	        System.out.println(" Trying to Copy Files");
	        SftpChannel.put(this.pathOrigin, this.pathDestination);
	        System.out.println(" Copy has been successful");
	        
	        SftpChannel.disconnect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
          
	}
	
	private void removeSession() {
		this.session.disconnect();
		
	}
	public static void main(String[] args) {
		String ip = "ec2-3-86-69-114.compute-1.amazonaws.com";
		int port = 22;
		String username = "ubuntu";
		String password = "";
		String pathKey = "/home/soporte/Desktop/aws/aws pems/sd-ch-cloud1.pem";
		String pathDestination = "/home/ubuntu/";
		// String pathDestination = "/tmp/";
		String pathOrigin = "/home/soporte/Desktop/ServerSocketTest.java";
		
		AwsConnect awsc = new AwsConnect (ip, port, username, password, pathKey, pathDestination, pathOrigin);
		awsc.copyToRemote ();
		
		boolean exit = false;
		Scanner keyboard = new Scanner(System.in);
		String command = "";
		while (!exit) {
			System.out.println(" Please input remote command to execute, 'exit' to terminate" );
			command = keyboard.nextLine();
			if (command.equals("exit")) {
				exit = true;
			}else {
				awsc.execCommands(command);
			}
		}
		
		
		awsc.removeSession();

	}


	


	


	

}
