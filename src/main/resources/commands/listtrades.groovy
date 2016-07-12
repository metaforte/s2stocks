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

class listtrades {
    @Usage("Lists all available trades in the database")
    @Command
    def main(InvocationContext context, 
        @Usage("stock symbol") @Option(names=["s","stock"]) String stock) {
     
        Map<String,Object>  attributes= context.getAttributes();
    	BeanFactory beanFactory = (BeanFactory) attributes.get("spring.beanfactory");
		TradeBookingService tq = (TradeBookingService)beanFactory.getBean("tradeBookingService");
		List<TradeBooking> tradesList= tq.listAllTrades()
		StringBuilder sb = new StringBuilder();
		
		sb.append("|").append("-"*127).append("|\r\n")
		sb.append("|").append(String.format(" %-20s ","Id"))
		sb.append("|").append(String.format(" %-30s ","Booking Time"))
		sb.append("|").append(String.format(" %-12s ","Stock Symbol"))
		sb.append("|").append(String.format(" %-8s ","Buy/Sell"))
		sb.append("|").append(String.format(" %20s ","Quantity"))
		sb.append("|").append(String.format(" %20s ","Price")).append("|\r\n")
		sb.append("|").append("-"*127).append("|\r\n")
		System.out.println(new String(sb));
		for (TradeBooking b:tradesList) {
			sb.append(String.format("| %20d ",b.getId()));
			sb.append(String.format("| %-30s ",b.getBookingTime()));
			sb.append(String.format("| %-12s ",b.getStockSymbol()));
			
			if (b.getBuySell()==1) {
			  sb.append(String.format("| %-8s ","BUY"));
			}
			else if (b.getBuySell()==2) {
			  sb.append(String.format("| %-8s ","SELL"));	
			}
			sb.append(String.format("| %20.6f ",b.getQuantity()));
			sb.append(String.format("| %20.6f ",b.getPrice())) ;
			sb.append("|\r\n");
		}     	    	
     	sb.append("|").append("-"*127).append("|\r\n")
     }
}