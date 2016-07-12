##Super Simple Stock Market

###Build steps

 * This exercise is developed using Java SE 8, Spring Boot , and Spring tool suite as the IDE.
 * Clone the repository 
 * In STS, navigate to File->Import -> Maven -> Existing Maven projects to import the project
 
###Using and testing the application
  
  Three JUnit classes are provided to test different parts of the functionalities
  
  * StockSymbolsTests class has test cases for DividendYield and P/E ratio calculation
  * TradeBookingTests class has test cases for recording trades and also covers validation errors
  * TradeQueryTests class has tests for querying Volume Weighted Price calculation and All Shares Index calculation
  
###Design

####Data model

The application uses Apache Derby as the in memory DB to store trades. "schema.sql" has the table definitions. The tables are created at the time of start up and stock symbols are populated from "data.sql". 

The stocks table contains the stock symbol, and last dividend value and fixed dividend values. No separate tables are created to track these values or corresponding historical values. This is done to keep the application simple.

The trade_bookings table requires a sequence to generate trade ids. All trade booking times are assumed to be in UTC time zone. Application layer enforces this assumption and ensures data integrity.

All values are in pennies. No conversion is done to pounds. UI layer has to do the necessary conversion

####Entity layer

Stock.java and TradeBooking.java are the Entity beans for the stocks, trade_booking tables.

####Data access layer

#####Stock table's DAO class

StockRepository.java is created extending Spring Data's PagingAndSortingRepository. This is used for querying stock symbols during testing. No other functionality is used inside the application.

#####Trade Booking table's DAO class

For TradeBooking.java entity has two DAO classes. One is based on SpringJDBC and the other is based on JPA. The JDBC class should be used for Data entry operation for better memory utilization and performance. The JPA based DAO is used for simple queries.

A Converter class is defined to convert ZonedDateTime to java.sql.Timestamp.

#####A DAO for Report queries
ReportsRepository.java was created to hold SQLs for general report queries. These are kept outside the above DAOs as they are intended to be used for only one table.


####Service layer
Two service classes are defined as entry point for all data entry operations and report queries.
UI layer, message listeners, and API clients should be programmed against these service classes.

* TradeBookingService.java provides the method for trade booking. It validates the input booking object and invokes the JDBC DAO. No over loaded methods are provided.

* TradeQueryService.java provides methods for retrieving dividend yield, P/E Ratio, VWPrice, and all share index prices 

##### NOTE
The services and DAOs are implemented directly as classes instead of defining interfaces with backing implementations to keep the code base simple for this exercise

###Running the application

The application can be run via: 

* Either inside the IDE via the project context menu->Run as->Spring Boot App menu
* Or from command prompt using java -jar target\s2stocks-0.0.1-SNAPSHOT.jar

The application keeps running and accepts connection from SSH based on the "Crash" Spring boot plugin.

####Command line Interface
The Crash shell that comes as part of spring boot is extended to provide command line user interface

To login to the application, use PuTTY and connect to localhost:2000 via SSH
The credentials are "sa", "sa"
Run help to list the available commands

#####AAvailable UI commands

     % help
     Try one of these commands with the -h or --help switch:
     
     NAME        DESCRIPTION
     booktrade   Records a trade booking into the database
     divYield    For a given stock, and a given price as input, calculate the dividend yield
     liststocks  Lists all available stocks in the DB
     listtrades  Lists all available trades in the database
     pe-ratio    Finds the P/E Ratio for a given stock at a given price
     sharesindex GBCE All Share Index using the geometric mean of the Volume Weighted Stock Price for all shares
     vwprice     Volume Weighted Stock Price for a given stock from the last 5 minutes of trades
     
#####Command Examples


     % booktrade -s POP -i buy -q 10 -p 12

     
     % divYield -s GIN -p 12
     |--------------------------------------------------------------------|
     |                Stock |                Price |       Dividend Yield |
     |----------------------|----------------------|----------------------|
     |                  GIN |           12.0000000 |            0.1666667 |
     |--------------------------------------------------------------------|


     
     % pe-ratio  -s GIN -p 12
      |--------------------------------------------------------------------|
      |                Stock |                Price |       Dividend Yield |
      |----------------------|----------------------|----------------------|
      |                  GIN |           12.0000000 |            1.5000000 |
      |--------------------------------------------------------------------|
     
     
     % vwprice -s POP
     |---------------------------------------------|
     |VW Price of last 5 minutes trades            |
     |---------------------------------------------|
     |           13.000000                         |
     |---------------------------------------------|


     % sharesindex
      |----------------------|
      | All Shares Index     |
      |----------------------|
      | 13.000000            |
      |----------------------|

      % listtrades
      |-------------------------------------------------------------------------------------------------------------------------------|
      | Id                   | Booking Time                   | Stock Symbol | Buy/Sell |             Quantity |                Price |
      |-------------------------------------------------------------------------------------------------------------------------------|
      |               100001 | 2016-07-12T15:00:10.685Z[UTC]  | POP          | BUY      |            10.000000 |            12.000000 |
      |               100002 | 2016-07-12T15:00:33.638Z[UTC]  | POP          | BUY      |            10.000000 |            14.000000 |
      |-------------------------------------------------------------------------------------------------------------------------------|

     



