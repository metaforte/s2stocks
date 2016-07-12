package exchange.gbc;

import static org.junit.Assert.*;

import org.junit.After;

import org.junit.Before;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.runners.MethodSorters;
import exchange.gbc.models.Stock;
import exchange.gbc.repositories.ReportsRepository;
import exchange.gbc.repositories.StocksRepository;
import exchange.gbc.repositories.TradeBookingJdbcRepo;
import exchange.gbc.repositories.TradeBookingRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = S2stocksApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockSymbolTests {
	private static final Logger logger = LoggerFactory.getLogger(TradeQueryTests.class);
	
	@Autowired
	TradeBookingRepository tradeRepo;

	@Autowired
	TradeBookingJdbcRepo tradeJdbcRepo;

	@Autowired
	StocksRepository stocksRepository;

	@Autowired
	ReportsRepository reportsRepository;

	Stock tea, pop, gin;



	double[] prices = { Double.NaN, Double.NEGATIVE_INFINITY, Double.MIN_VALUE,
			Double.MIN_NORMAL, -1, 0, 1, 100, Double.POSITIVE_INFINITY };
	Double[] yields = new Double[prices.length];

	@Before
	public void setUp() throws Exception {
		logger.debug("==========setUp==========");
		tea = stocksRepository.findOne("TEA");
		pop = stocksRepository.findOne("POP");
		gin = stocksRepository.findOne("GIN");

	}

	@After
	public void tearDown() throws Exception {
		logger.debug("==========tearDown==========");
	}

	@Test
	public void a01_zeroDividendYieldForTea() {
		// When the last Dividend is zero, all yields should return 0
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d };
		logger.debug(tea.toString());
		for (int i = 0; i < prices.length; i++)
			yields[i] = tea.getDividendYield(prices[i]);
		assertArrayEquals(expected_yields, yields);

	}

	@Test
	public void a02_DividendYieldForPreferredStock() {
		// Non zero last dividends exist for Pop
		logger.debug(gin.toString());
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 2.0d, 0.02d, 0d };
		for (int i = 0; i < prices.length; i++) {
			yields[i] = gin.getDividendYield(prices[i]);
			// logger.debug("Gin's dividend yield for price:"+prices[i]+" is:"+yields[i]);

		}
		assertArrayEquals(yields, expected_yields);

	}

	@Test
	public void a03_DividendYieldForPop() {
		// Non zero last dividends exist for Pop
		logger.debug(pop.toString());
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 8.0d, 0.08d, 0d };
		for (int i = 0; i < prices.length; i++) {
			yields[i] = pop.getDividendYield(prices[i]);
			// logger.debug("Pop's dividend yield for price:"+prices[i]+" is:"+yields[i]);

		}
		assertArrayEquals(yields, expected_yields);

	}

	
	@Test
	public void a04_zeroPERatioForTea() {
		// When the last Dividend is zero, all yields should return 0
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d };
		logger.debug(tea.toString());
		for (int i = 0; i < prices.length; i++)
			yields[i] = tea.getPERatio(prices[i]);
		assertArrayEquals(expected_yields, yields);

	}

	@Test
	public void a05_PERatioForPreferredStock() {
		// Non zero last dividends exist for Pop
		logger.debug(gin.toString());
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 0.125d, 12.5d, 0d };
		for (int i = 0; i < prices.length; i++) {
			yields[i] = gin.getPERatio(prices[i]);
			// logger.debug("Gin's dividend yield for price:"+prices[i]+" is:"+yields[i]);

		}
		assertArrayEquals(expected_yields,yields);

	}

	@Test
	public void a06_PERatioForPop() {
		// Non zero last dividends exist for Pop
		logger.debug(pop.toString());
		Double[] expected_yields = { 0d, 0d, 0d, 0d, 0d, 0d, 0.125d, 12.5d, 0d };
		for (int i = 0; i < prices.length; i++) {
			yields[i] = pop.getPERatio(prices[i]);
			// logger.debug("Pop's dividend yield for price:"+prices[i]+" is:"+yields[i]);

		}
		assertArrayEquals( expected_yields,yields);

	}	
}
