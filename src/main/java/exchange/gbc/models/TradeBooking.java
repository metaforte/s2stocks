package exchange.gbc.models;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Manas
 *
 *1. This Bean is backed by trade_booking table and holds the trade booking entries
 * Both Jdbc, and JPA DAO classes will be created to be used in different scenarios.
 * 
 *2. All Trades are to be recorded at UTC time stamp. The Jdbc DAO will validate the booking time stamp
 *  
 *3. Used Doubles and Shorts instead of double to preserve nulls and make Data entry easy.
 *	Services are responsible for handling nulls when calculations are to be done
 *	or data is to be displayed to user
 *
 *4. Assumption: This table will have trades for only one business day.
 *    A separate service should be created for archiving trades at the end of day.
 *  
 *5.  Queries will not check if booking date is past date.
 *
 *6. As per specs "all number values are in pennies". 
 *  UI layer should handle converting pennies to pounds
 */
@Entity
@Table(name = "trade_booking")
public class TradeBooking {

	
	@Id
	@Column(name = "id") //Generated from sequence
	private Long id;

	/* Zone should be UTC. To be validated by DAOs and Services
	 * Care should be taken to store only UTC time stamps into DB.
	 * User time zones are to be handled at UI layer.
	 */
	@Column(name = "booking_time")
	private ZonedDateTime bookingTime;

	@Column(name = "stock_symbol") //Simple string is chosen in stead of @ManyToOne to simplify jdbc code
	private String stockSymbol;

	@Column(name = "buy_sell")
	private Short buySell;

	 
	@Column(name = "quantity")
	private Double quantity;

	@Column(name = "price")
	private Double price;

	@Override
	public String toString() {
		return "TradeBooking [id=" + id + ", bookingTime=" + bookingTime
				+ ", stockSymbol=" + stockSymbol + ", buySell=" + buySell
				+ ", quantity=" + quantity + ", price=" + price + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ZonedDateTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(ZonedDateTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public Short getBuySell() {
		return buySell;
	}

	public void setBuySell(Short buySell) {
		this.buySell = buySell;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public TradeBooking(ZonedDateTime bookingTime, String stockSymbol, Short buySell, Double quantity, Double price) {
		this.bookingTime = bookingTime;
		this.stockSymbol = stockSymbol;
		this.buySell = buySell;
		this.quantity = quantity;
		this.price = price;
	}
	
	public TradeBooking(ZonedDateTime bookingTime, String stockSymbol, short buySell, double quantity, double price) {
		this(bookingTime,stockSymbol,Short.valueOf(buySell),Double.valueOf(quantity),Double.valueOf(price));
	}

	public TradeBooking() {
		
	}

	
}
