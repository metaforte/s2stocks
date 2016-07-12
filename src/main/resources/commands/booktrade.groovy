import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.cli.Option
import org.crsh.command.InvocationContext
import java.util.List
import java.util.Map
import org.crsh.cli.Required
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

import exchange.gbc.models.Stock
import exchange.gbc.services.TradeQueryService
import exchange.gbc.services.TradeBookingService
import exchange.gbc.models.TradeBooking
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;

class booktrade {
    @Usage("Records a trade booking into the database")
    @Command
    def main(InvocationContext context,      
     		@Usage("stock symbol") @Option(names=["s","stock"]) @Required String stock,
     		@Usage("booking type (buy/sell)") @Option(names=["i","buysell"]) @Required String buySell,
     		
     		@Usage("booking price")  @Option(names=["p","price"]) @Required String price,
     		@Usage("booking quantity")  @Option(names=["q","quantity"]) @Required String quantity     
     ) {
        Map<String,Object>  attributes= context.getAttributes();
    	BeanFactory beanFactory = (BeanFactory) attributes.get("spring.beanfactory");
		TradeQueryService tq = (TradeQueryService)beanFactory.getBean("tradeQueryService");
		TradeBookingService bookingSrv = (TradeBookingService)beanFactory.getBean("tradeBookingService")
		
		
		StringBuilder sb = new StringBuilder();
     	ZonedDateTime bookingTime = ZonedDateTime.now(ZoneId.of("UTC"))
     	
     	Short buySellFlag =0;
     	if (buySell.equalsIgnoreCase("buy"))
     		buySellFlag=1
     	else if(buySell.equalsIgnoreCase("sell"))
     		buySellFlag=2
     	else
     		throw new RuntimeException ("Invalid buySell Indicator supplied "+buySell)
     	
     	Double quantityVal = Double.parseDouble(quantity)
     	Double priceVal = Double.parseDouble(price)
     	
	 	TradeBooking booking = new TradeBooking(bookingTime,stock,buySellFlag,quantityVal,priceVal)
		
     	bookingSrv.bookTrade(booking)
     	
     	return "Trade is booked \r\n"+booking
     	
     
     }
}