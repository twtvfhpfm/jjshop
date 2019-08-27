create table Logistics(
	id int not null auto_increment,
	orderId varchar(32) not null,
	customerName varchar(50) not null default '',
	shipperCode varchar(10) not null default '',
	logisticCode varchar(30) not null default '',
	createTime datetime default current_timestamp,
	primary key (id)
);