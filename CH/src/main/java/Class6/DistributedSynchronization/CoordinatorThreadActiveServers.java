package Class6.DistributedSynchronization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.kohsuke.rngom.ast.om.ParsedPattern;

import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

import ch.qos.logback.classic.Logger;

public class CoordinatorThreadActiveServers implements Runnable{
	ArrayList<Servers> listServers;
	UtilsConnection uc;
	String ip;
	Logger log;
	ArrayList<Long> listIds;
	
	public CoordinatorThreadActiveServers(ArrayList<Servers> listServers, UtilsConnection uc, String ip, Logger log) {
		this.listServers = listServers;
		this.uc = uc;
		this.ip = ip;
		this.log = log;
	}

	@Override
	public void run() {
		this.log.info("[COOR-ACT-SERVERS] - Thread Started");
		
		
		while (true) {
			this.log.info("[COOR-ACT-SERVERS] - Revise news");
			this.listIds = new ArrayList<Long>();
			// -> borro y cargo todo
			// -> leo 1 msg -> me fijo si está en la cola, 
			// -> 
			// [STEP 0] - Clean 
			synchronized (this.listServers) {
				
				
				try {
					int qMess = this.uc.queueChannel.queueDeclarePassive(this.uc.activeServersQueue).getMessageCount();
					
					// IF 0 
					for (int i =0; i<qMess; i++) {
						log.info(" [COOR-ACT-SERVERS]  - Leyendo msg "+i);

						//[STEP 1] - Obtain data from shared queue 
						GetResponse data = this.uc.queueChannel.basicGet(this.uc.activeServersQueue, false);
						String msg = new String (data.getBody());
						log.info(" [COOR-ACT-SERVERS] - MSG: "+msg);
						//[STEP 2] - Guardar id para hacer ack
						this.listIds.add(data.getEnvelope().getDeliveryTag());
						
						//[STEP 3] - Meter datos en lista de Vecinos
						//ip(localhost)|server (cola)
						String[] spli = msg.split(Pattern.quote("|"));
						String serverip = spli[0];
						String queueName = spli[1];
						Servers s = new Servers (serverip,queueName);
						boolean found = false;
						for (Servers serv : this.listServers) {
							
							if (serv.queueName.equals(s.queueName)) {
								found=true;
							}
						}
						if (!found) {
							this.listServers.add(s);
							//this.log.info("SERVER :"+s.queueName);
						}

						
					}
					this.log.info(String.valueOf(this.listServers.size()));
					
					
					for (Long long1 : listIds) {
						this.uc.queueChannel.basicNack(long1, false, true);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
