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

class sharesindex {
    @Usage("GBCE All Share Index using the geometric mean of the Volume Weighted Stock Price for all shares")
    @Command
    def main(InvocationContext context) {
        Map<String,Object>  attributes= context.getAttributes();
    	BeanFactory beanFactory = (BeanFactory) attributes.get("spring.beanfactory");
		TradeQueryService tq = (TradeQueryService)beanFactory.getBean("tradeQueryService");
		Double sharesIndex= tq.getAllSharesIndex()
		StringBuilder sb = new StringBuilder();
		
		sb.append("|").append("-"*22).append("|\r\n")
		sb.append("|").append(String.format(" %-20s ","All Shares Index")).append("|\r\n")
		sb.append("|").append("-"*22).append("|\r\n")
		if (sharesIndex==null || Double.isNaN(sharesIndex)||Double.isInfinite(sharesIndex))
			sb.append("|").append(String.format(" %-20.6f ",0d)).append("|\r\n")
		else
		    sb.append("|").append(String.format(" %-20.6f ",sharesIndex)).append("|\r\n")
		
     	sb.append("|").append("-"*22).append("|\r\n")
     }
}