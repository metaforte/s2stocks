package exchange.gbc.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import exchange.gbc.models.Stock;

/**
 * 
 * @author Manas
 * Used for simple queries in tests
 * No data entry services are developed.
 * All stocks are populated at the time of start up from data.sql
 * 
 */
@Repository
public interface StocksRepository  extends PagingAndSortingRepository<Stock,String> {

}

