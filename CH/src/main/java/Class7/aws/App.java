package Class7.aws;


import java.io.File;
import java.util.ListIterator;

import com.jcraft.jsch.SftpException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	/*
    	 * 1 . Creo la VM
    	 * 2. Espero hasta que la vm esté creada. CREACION OK SSH responde
    	 * 3. Copiar nuestro proyecto de JAVA (SFTP) - App.java /  
    	 * 4. Instalar lo necesario (Java)
    	 * 5. javac App.java
    	 * 6. java App IP:PUERTO conectar
    	 */
        try {
        	System.out.println( "Hello World!" );
            String basePath = "src/main/java/";
            String file = basePath+"my.azureauth";
            
            /*JschActivities jschFtp = new JschActivities("104.43.241.159", 22, "azureuser", "MagicLander159357!", file, "/tmp/prueba2");
            jschFtp.configureFtpSession();
			jschFtp.copyToRemoteServer();
			jschFtp.closeFtpSession();
			*/
            JschActivities jschExec = new JschActivities("104.43.241.159", 22, "azureuser", "MagicLander159357!", null, null);
            jschExec.configureExecSession("");
        }catch (Exception e) {
			System.out.println(e.getMessage());
		}
       
    }
}
