package exchange.gbc.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This represents Stock Entries in stock table
 * All stocks are populated at the time of start up from data.sql
 * 
 * Note: As per specs "all number values are in pennies". 
 *  UI layer should handle converting pennies to pounds
 */
@Entity
@Table(name="stock")
public class Stock {
	private static final Logger logger = LoggerFactory.getLogger(Stock.class);
	@Id
	@Column(name="stock_symbol") 
	private String stockSymbol;
	
	@Column(name="type")
	private String type;

	@Column (name="last_dividend")
	private double lastDividend; // can be linked to a seperate class that provides historical entries
	@Column(name="fixed_dividend")
	private double fixedDividend;
	@Column (name="par_value")
	private double parValue;
	
	
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getLastDividend() {
		return lastDividend;
	}
	public void setLastDividend(double lastDividend) {
		this.lastDividend = lastDividend;
	}
	public double getFixedDividend() {
		return fixedDividend;
	}
	public void setFixedDividend(double fixedDividend) {
		this.fixedDividend = fixedDividend;
	}
	public double getParValue() {
		return parValue;
	}
	public void setParValue(double parValue) {
		this.parValue = parValue;
	}
	@Override
	public String toString() {
		return "Stock [stockSymbol=" + stockSymbol + ", lastDividend="
				+ lastDividend + ", fixedDividend=" + fixedDividend
				+ ", parValue=" + parValue + "]";
	}
	
	
	public double getDividendYield(double price) {
		
		 //Limit the lowest precision of allowed value, to prevent infinity values
		if (price <= 0.000_000_1d || Double.isNaN(price))
			return 0d;
		if (this.type.equals("Common"))
			return this.lastDividend/price;
		else if (this.type.equals("Preferred"))
			return (this.fixedDividend*this.parValue)/price;
		else
			return 0d;
	}
	
	/*
	 * 
	 * The definition given is price/dividend.
	 * Interpreted it as lastDividend
	 * 
	 */
	public double getPERatio(double price) {

		 //Limit the lowest precision of allowed value, to prevent infinity values
		if (lastDividend <= 0.000_000_1d )// || Double.isNaN(lastDividend))
			return 0d;
		if (price <= 0.000_000_1d || Double.isNaN(price) || Double.isInfinite(price))
			return 0;
		
		return price/lastDividend;
		
	}
	
	
}
