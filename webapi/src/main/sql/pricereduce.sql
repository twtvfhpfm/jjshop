create table PriceReduce(
	id int not null auto_increment,
	goodsId int not null,
	amount decimal(12, 2) not null,
	superiorAmount decimal(12, 2) not null,
	transportFee decimal(12, 2) not null,
	numCond int not null,
	primary key (id)
);