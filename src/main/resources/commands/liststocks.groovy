package commands

import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import java.util.List
import java.util.Map

import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

import exchange.gbc.models.Stock
import exchange.gbc.services.TradeQueryService

class liststocks {

    @Usage("Lists all available stocks in the DB")
    @Command
    def main(InvocationContext context) {
    	  Map<String,Object>  attributes= context.getAttributes();
		  BeanFactory beanFactory = (BeanFactory) attributes.get("spring.beanfactory");
		  TradeQueryService tq = (TradeQueryService)beanFactory.getBean("tradeQueryService");
		  StringBuilder sb = new StringBuilder();
		  List<Stock> stocks = tq.getAllStocks();
		  sb.append("Stocks\r\n===============\r\n");
		  
		  for(Stock s:stocks)
			  sb.append(s.getStockSymbol()).append('\r').append('\n');
		  
		  return new String(sb);
        
    }

}