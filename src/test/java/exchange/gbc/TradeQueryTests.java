package exchange.gbc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import exchange.gbc.models.Stock;
import exchange.gbc.models.TradeBooking;
import exchange.gbc.repositories.ReportsRepository;
import exchange.gbc.repositories.StocksRepository;
import exchange.gbc.repositories.TradeBookingJdbcRepo;
import exchange.gbc.repositories.TradeBookingRepository;
import exchange.gbc.services.TradeBookingService;
import exchange.gbc.services.TradeQueryService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = S2stocksApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TradeQueryTests {
	private static final Logger logger = LoggerFactory.getLogger(TradeQueryTests.class);
	@Autowired 
	TradeBookingRepository tradeRepo;
	
	@Autowired
	TradeBookingService tradeBookingService;
	
	@Autowired
	StocksRepository stocksRepository;
	
	@Autowired
	TradeQueryService tradeQueryService;
	@Before
	public void setUp() throws Exception {
		tradeBookingService.deleteAllTrades();
		ZonedDateTime bookedAt = ZonedDateTime.now(ZoneId.of("UTC"));
		//String stockSymbol,		short buySell, double quantity, double price)
		TradeBooking []trades = {
				new TradeBooking ( bookedAt.minusMinutes(4),"ALE",(short)1,10,12d),
				new TradeBooking ( bookedAt.minusMinutes(3),"ALE",(short)1,10,14),
				new TradeBooking ( bookedAt.minusMinutes(2), "ALE",(short)1,10,16),
				new TradeBooking ( bookedAt.minusMinutes(1), "ALE",(short)1,10,18),
				new TradeBooking ( bookedAt.minusMinutes(1),"ALE",(short)1,10,20),				
				new TradeBooking ( bookedAt.minusMinutes(4),"GIN",(short)1,10,12d),
				new TradeBooking ( bookedAt.minusMinutes(3),"GIN",(short)1,10,14),
				new TradeBooking ( bookedAt.minusMinutes(4),"POP",(short)1,10,16),
				new TradeBooking ( bookedAt.minusMinutes(3),"POP",(short)1,10,18),
				new TradeBooking ( bookedAt.minusMinutes(2),"POP",(short)1,10,20),
				new TradeBooking ( bookedAt.minusMinutes(3),"TEA",(short)1,10,16),
				new TradeBooking ( bookedAt.minusMinutes(2),"TEA",(short)1,10,18),
				new TradeBooking ( bookedAt.minusMinutes(1),"TEA",(short)1,10,20),

				
		};
		for (TradeBooking aBooking:trades) {
			tradeBookingService.bookTrade(aBooking); 
		}			
	}

	@After
	public void tearDown() throws Exception {
		logger.debug("==========tearDown==========");
	}


	@Test
	public void a01_testVWPrice() {
		double vwPrice = tradeQueryService.getVWStockPriceOfLast5MinTrades(stocksRepository.findOne("ALE"));
		assertEquals("getVWStockPriceOfLast5MinTrades",vwPrice,16.0d,0.0d);
	}

	@Test
	public void a02_testAllVWPrice() {
		List<Map<String,Object>> vwPrices = tradeQueryService.getAllTradesVWPrices();
		logger.debug(vwPrices.toString());
		
	}

	@Test
	public void a03_testAllSharesIndex() {
		assertEquals("AllSharesIndex",tradeQueryService.getAllSharesIndex(),16.11209,0.00001);
	}
	
}
