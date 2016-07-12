package exchange.gbc;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import exchange.gbc.models.TradeBooking;
import exchange.gbc.repositories.ReportsRepository;
import exchange.gbc.repositories.StocksRepository;
import exchange.gbc.repositories.TradeBookingJdbcRepo;
import exchange.gbc.repositories.TradeBookingRepository;
import exchange.gbc.services.TradeBookingService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = S2stocksApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TradeBookingTests {
	private static final Logger logger = LoggerFactory.getLogger(TradeQueryTests.class);
	@Autowired
	TradeBookingRepository tradeRepo;

	@Autowired
	TradeBookingService tradeBookingService;
	
	ZonedDateTime bookTime = ZonedDateTime.now(ZoneId.of("UTC"));
	
	
	@Before
	public void setUp() throws Exception {
		tradeBookingService.deleteAllTrades();
	}
	@Test
	public void a001_testBookings() {
		ZonedDateTime bookedAt = ZonedDateTime.now(ZoneId.of("UTC"));
		// String stockSymbol, short buySell, double quantity, double price)
		TradeBooking[] tradeBookings = {
				new TradeBooking(bookedAt.minusMinutes(6), "TEA", (short) 1, 11, 12),
				new TradeBooking(bookedAt.minusMinutes(5), "TEA", (short) 1, 12, 13),
				new TradeBooking(bookedAt.minusMinutes(4), "TEA", (short) 1, 13, 13),
				new TradeBooking(bookedAt.minusMinutes(3), "TEA", (short) 1, 14, 13),
				new TradeBooking(bookedAt.minusMinutes(2), "TEA", (short) 1, 15, 13),
				new TradeBooking(bookedAt.minusMinutes(1), "TEA", (short) 1, 16, 13),

		};
		for (TradeBooking aBooking : tradeBookings) {
			tradeBookingService.bookTrade(aBooking);
			logger.debug(aBooking.toString());
		}

		assertEquals(tradeRepo.findByStockSymbol("TEA").size(), 6);

	}


	//========================Validation tests using error data

	@Test(expected=NullPointerException.class)
	public void a00_bookingTimeValidation() {
		tradeBookingService.bookTrade(
			new TradeBooking(null, "JOE", (short) 1, 11, 12));
	}

	@Test(expected=org.springframework.dao.DataIntegrityViolationException.class)
	public void a01_symbolValidation() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "XXX", Short.valueOf((short)1), Double.valueOf(12), Double.valueOf(13)));
	}
	
	@Test(expected=RuntimeException.class)
	public void a02_buySellCodeValidation1() {
		tradeBookingService.bookTrade(
				new TradeBooking(bookTime , "JOE", null, Double.valueOf(12), Double.valueOf(13)));
	}

	@Test(expected=RuntimeException.class)
	public void a02_buySellCodeValidation2() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "JOE", Short.valueOf((short)3), Double.valueOf(12), Double.valueOf(13)));
	}
	
	@Test(expected=RuntimeException.class)
	public void a03_quantityValidation1() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "JOE", Short.valueOf((short)3), null, Double.valueOf(13)));
	}
		
	@Test(expected=RuntimeException.class)
	public void a03_quantityValidation2() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "JOE", Short.valueOf((short)3), Double.valueOf(0.000_000_000_1d), Double.valueOf(13)));
	}
	
	@Test(expected=RuntimeException.class)
	public void a03_priceValidation1() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "JOE", Short.valueOf((short)3), Double.valueOf(0.1d), null));
	}

	@Test(expected=RuntimeException.class)
	public void a03_priceValidation2() {
		tradeBookingService.bookTrade(
			new TradeBooking(bookTime , "JOE", Short.valueOf((short)3), Double.valueOf(0.1d), Double.valueOf(0.000_000_000_1d)));
	}

	
}
