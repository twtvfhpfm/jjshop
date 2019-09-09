create table Coupon(
	id int not null auto_increment,
	uid int not null,
	goodsId int not null,
	orderId varchar(32) not null,
	priceCond decimal(12,2) not null,
	type int not null, -- 0:register 1:grant 2:prize
	numCond int not null,
	amount decimal(12,2) not null,
	isDeleted int not null,
	isUsed int not null,
	beginTime datetime not null,
	endTime datetime not null,
	createTime datetime default current_timestamp,
	primary key (id)
);