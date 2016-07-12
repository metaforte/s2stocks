package exchange.gbc.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exchange.gbc.models.Stock;
import exchange.gbc.repositories.ReportsRepository;
import exchange.gbc.repositories.StocksRepository;
/*
 * This class should be used by UI and other API client or message listener
 * to provide necessary reporting services.
 * All new reports should be used exposed via this service and be backed ReportsRepository DAO class.
 * No authentication is done by this at present.
 */
@Service
public class TradeQueryService {
	private static final Logger logger = LoggerFactory.getLogger(TradeQueryService.class);
 @Autowired
 ReportsRepository reportsRepository;
 
 @Autowired
 StocksRepository stocksRepository;
 public double getStockDividendYield(String stock, double price) {
	 Stock s = stocksRepository.findOne(stock);
	 return s.getDividendYield(price);
 }
 
 public double getStockPERatio(String stock, double price) {
	 Stock s = stocksRepository.findOne(stock);
	 return s.getPERatio(price);
 }
 public Double getVWStockPriceOfLast5MinTrades(Stock stock) {
	 ZonedDateTime bookedSince =  ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(5);
 	 return reportsRepository.getVWStockPrice( stock.getStockSymbol(),bookedSince);
 }
 public Double getVWStockPriceOfLast5MinTrades(String stockSymbol) {
	 ZonedDateTime bookedSince =  ZonedDateTime.now(ZoneId.of("UTC")).minusMinutes(5);
 	 return reportsRepository.getVWStockPrice( stockSymbol,bookedSince);
 } 
 public List<Map<String, Object>> getAllTradesVWPrices () {
	 return reportsRepository.getAllTradesVWPrices();
 }
 
 /**
  * 
  * @return Geometric mean of all Volume Weighted prices for all stocks in the trade_booking table.
  * Note: All trades are assumed to be from the same business date.
  */
 public Double getAllSharesIndex() {
	 List<Map<String, Object>> vwPricesList = getAllTradesVWPrices ();
	 int i=0;
	 double product=1d;
	 for (Map<String,Object> row:vwPricesList) {
		 Double vwPrice = (Double)row.get("VWPRICE");
		 if(vwPrice!=null && vwPrice>0) {
			 product *= vwPrice;
			 i++;
		 }
	 }
	 if (i==0) 
		 return 0d;
	 else
		 return Math.pow(product, 1d/i);
 }
 
 public List<Stock> getAllStocks() {
	 return (List<Stock>)stocksRepository.findAll();
 }
 

}
