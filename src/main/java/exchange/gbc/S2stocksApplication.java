package exchange.gbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class S2stocksApplication {
	private static final Logger logger = LoggerFactory.getLogger(S2stocksApplication.class);
	public static void main(String[] args){
		SpringApplication.run(S2stocksApplication.class, args);
		
		Thread t = new Thread(new StartBackground ());
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			logger.info("Application is interrupted. Exiting");
		}
		
	}
	
	//This thread is used to keep the application running
	//I chose this approach as easiest route.
	//While the application is running, crash shell can be used to ssh to the app and use it. 
	//See README.md for more details
	static class StartBackground implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					logger.warn(e.getMessage(),e);
				}
				
			}
			
		}
		
	}
}
