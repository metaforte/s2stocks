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

class vwprice {
    @Usage("Volume Weighted Stock Price for a given stock from the last 5 minutes of trades")
    @Command
    def main(InvocationContext context,
            @Usage("stock symbol") @Option(names=["s","stock"]) @Required String stock
    ) {
        Map<String,Object>  attributes= context.getAttributes();
    	BeanFactory beanFactory = (BeanFactory) attributes.get("spring.beanfactory");
		TradeQueryService tq = (TradeQueryService)beanFactory.getBean("tradeQueryService");
		Double vwPriceValue= tq.getVWStockPriceOfLast5MinTrades(stock)
		StringBuilder sb = new StringBuilder();
		
		sb.append("|").append("-"*45).append("|\r\n")
		 
		sb.append("|").append(String.format("%-45s","VW Price of last 5 minutes trades")).append("|\r\n")
		sb.append("|").append("-"*45).append("|\r\n")
		
		if (vwPriceValue==null || Double.isNaN(vwPriceValue)||Double.isInfinite(vwPriceValue))
			sb.append("|").append(String.format("%-45s","0")).append("|\r\n")
		else
		    sb.append("|").append(String.format("%-45s",String.format("%20.6f",vwPriceValue))).append("|\r\n")
     	sb.append("|").append("-"*45).append("|\r\n")
     }
}