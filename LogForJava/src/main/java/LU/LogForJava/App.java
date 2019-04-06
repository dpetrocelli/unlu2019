package LU.LogForJava;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
/**
 -- LEER ESTO -- 
  Setting Up Log4J 2 to Use the Properties File
Unlike its predecessor Log4J 1.x, Log4J 2 did not support configuration through the properties 
file when it was initially released. It was from Log4J 2.4 that support for the properties file 
was again added, but with a completely different syntax.
 
 */
public class App 
{
	
	static final Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	 BasicConfigurator.configure();
    	 logger.debug("Hello World!");
    		
    }
}
