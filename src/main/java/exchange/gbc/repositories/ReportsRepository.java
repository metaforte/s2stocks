package exchange.gbc.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

import exchange.gbc.models.Stock;
import exchange.gbc.models.TradeBooking;

/**
 * 
 * @author Manas
 * 1. This class will hold all adhoc report related Queries
 * 2. TradeQueryServices class will use this class and Stock class to provide single Report Service
 * 
 */
@Repository
public class ReportsRepository {
	private final JdbcTemplate JT;
	private static final ZoneId utcZone = ZoneId.of("UTC");
	private static final ZonedDateTimeConverter zdConverter = new ZonedDateTimeConverter();
	private static final Logger logger = LoggerFactory.getLogger(ReportsRepository.class);
	@Autowired
	public ReportsRepository(JdbcTemplate jdbcTemplate) {
		this.JT = jdbcTemplate;
	}

	/*
	 * Returns the VW price for trades booked after a given time stamp.
	 * Timestamp is assumed to be in UTC. It should be validated by calling service
	 * 
	 */
	public Double getVWStockPrice(String stockSymbol,ZonedDateTime bookedAfter ) {
		String sql = "SELECT SUM(price*quantity)/SUM(quantity) FROM trade_booking where stock_symbol=? and booking_time >= ?";
		Double vwStockPrice = JT.queryForObject(sql,
				new Object[]{stockSymbol,zdConverter.convertToDatabaseColumn(bookedAfter)},
				Double.class);
		return vwStockPrice;
	}
	
	/**
	 * 
	 * @returns the list of all Volume Weighted stock prices for all stocks.
	 * No checks are made to see if booking date is past date
	 * All trades are assumed to be from current date
	 * 
	 */
	public List<Map<String, Object>> getAllTradesVWPrices () {
		String sql = "SELECT stock_symbol, SUM(price*quantity)/SUM(quantity) AS VWPRICE FROM trade_booking group by stock_symbol";
		List<Map<String, Object>> vwPrices= this.JT.queryForList(sql);
		logger.debug(vwPrices.toString());
		
		return vwPrices;
	}
	
}
