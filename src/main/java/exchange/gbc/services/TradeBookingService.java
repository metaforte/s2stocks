package exchange.gbc.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exchange.gbc.models.TradeBooking;
import exchange.gbc.repositories.TradeBookingJdbcRepo;
import exchange.gbc.repositories.TradeBookingRepository;
import exchange.gbc.repositories.ZonedDateTimeConverter;

/*
 *  This provides single interface to do all data entry operations on the trade_booking table.
 *  It uses both JPA and JDBC DAO classes, based on performance requirement or ease of development.
 *   
 */
@Service("tradeBookingService")
public class TradeBookingService {
    private static final ZoneId utcZone = ZoneId.of("UTC");
    private static final ZonedDateTimeConverter zdConverter = new ZonedDateTimeConverter();    
    
    @Autowired
    private TradeBookingJdbcRepo tradeBookingJdbcRepo;
    
    @Autowired
    private TradeBookingRepository tradeBookingRepo;
    
    /**
     * 
     * @param booking
     * Validate the trade booking bean and invoke the JDBC service to store the bean.
     * If trade is successfully booked, the booking bean will be updated with the trade id 
     * generated in the database
     * 
     */
	public void bookTrade(TradeBooking booking) {
		
		//Validations
    	ZonedDateTime bookingTime = booking.getBookingTime();
    	
    	Objects.requireNonNull(bookingTime,"bookingTime");
    	
    	ZoneId bookingZone = bookingTime.getZone();
    	if  (! Objects.equals( bookingZone ,utcZone))
    		throw new RuntimeException ("booking time should be supplied in UTC time zone : received "+bookingZone);
    	
    	Short buySell = booking.getBuySell();
    	if (! (buySell==1 || buySell==2))
    		throw new RuntimeException ("Invalid Buy Sell indicator: "+buySell);

    	Double quantity =booking.getQuantity();
    	
    	if (quantity<0.000_000_1d ) //lowest precision allowed
    		throw new RuntimeException ("Invalid quantity"+quantity);
    	
    	Double price = booking.getPrice();
    	
    	if (price<0.000_000_1d)
    		throw new RuntimeException ("Invalid price"+price);
    	
    	//Construct Insert Statement Params
    	Object[] insertParams = {
    			null, //ID is not supplied. 
    			zdConverter.convertToDatabaseColumn(bookingTime),
    			booking.getStockSymbol(),
    			buySell,
    			quantity,
    			price
    	};
	
    	//Invoke Spring JDBC Repository. 
    	//If a trade is booked successfully in the DB, this method returns the trade ID of the trade 
    	Long tradeId = tradeBookingJdbcRepo.bookTrade(insertParams);
    	booking.setId(tradeId);
	}

	public int deleteAllTrades() {
		return tradeBookingJdbcRepo.deleteAllTrades();
		
	}
	
	public Iterable<TradeBooking> listAllTrades() {
		return tradeBookingRepo.findAll();
	}
}
