package exchange.gbc.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exchange.gbc.models.TradeBooking;

/*
 * Chose JDBC implementation over JPA for trade booking to gain better performance  
 * 
 */
@Repository("tradeBookingJdbcRepo")
public class TradeBookingJdbcRepo {
    private final JdbcTemplate JT;
    private static final ZoneId utcZone = ZoneId.of("UTC");
    private static final ZonedDateTimeConverter zdConverter = new ZonedDateTimeConverter();
    @Autowired
    public TradeBookingJdbcRepo(JdbcTemplate jdbcTemplate) {
        this.JT = jdbcTemplate;
    }
    
    private static final class TradeBookingRowMapper implements RowMapper<TradeBooking> {

		@Override
		public TradeBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
			TradeBooking b = new TradeBooking();
			b.setId(rs.getLong(1));			
			b.setBookingTime(zdConverter.convertToEntityAttribute(rs.getTimestamp(2)));
			b.setStockSymbol(rs.getString(3));
			b.setBuySell(rs.getShort(4));
			b.setQuantity(rs.getDouble(5));
			b.setPrice(rs.getDouble(6));
			
			return b;
		}
    	
    }
    
    /*
     *  Records a trade and returns the ID of the recorded traded.
     *  insertParam values are assumed to be matching the TradeBooking bean properties in order and type
     *  Necessary validation is to be supplied by invoking service.
     *  
     */
    public Long bookTrade(Object[] insertParams ) {

    	Long tradeId = JT.queryForObject("SELECT NEXT VALUE FOR trade_booking_seq FROM dual ", Long.class);
    	insertParams[0] = tradeId;
    	JT.update ( " insert into trade_booking values (? , ?, ?, ?, ? , ?)" , insertParams);
    	return tradeId;
   
    }

    /*
     *  Created this for use by setUp() methods of the test classes
     */
	public int deleteAllTrades() {
		return JT.update("delete from trade_booking");
		
	}
    
   
    
}
