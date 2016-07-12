package exchange.gbc.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import exchange.gbc.models.TradeBooking;

/*
 * Used for simple queries. Will not be used for data entry
 * Data entry services should use the JDBC based DAO
 * 
 */
@Repository
public interface TradeBookingRepository  extends PagingAndSortingRepository<TradeBooking,Long> {

	public List<TradeBooking> findByStockSymbol(String stockSymbol);
}
