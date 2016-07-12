drop table dual;
create table dual (c char(1));

drop table stock;
create table stock (
   stock_symbol VARCHAR(10),
   type varchar(10),
   last_dividend DOUBLE,
   fixed_dividend DOUBLE,
   par_value DOUBLE,
   PRIMARY KEY(stock_symbol)
);
   

drop table trade_booking;
create table trade_booking (
	id BIGINT, 
	booking_time TIMESTAMP, 
	stock_symbol VARCHAR(10) REFERENCES stock(stock_symbol), 
	buy_sell SMALLINT,
	quantity DOUBLE , 
	price DOUBLE,
	primary key (Id));
	
create sequence trade_booking_seq AS BIGINT START WITH 100001;