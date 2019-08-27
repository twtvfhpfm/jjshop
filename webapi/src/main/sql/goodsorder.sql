create table GoodsOrder(
	id int not null auto_increment,
	orderId varchar(32) not null,
	uid int not null, -- user id
	cidList varchar(500) not null, -- cart id
	status int not null, -- 0: toPay 1: toDelivery 2: toReceipt 3: toComment 4: finish 5:canceld
	addressId int not null,
	totalPrice decimal(12,2) not null,
	totalSuperiorPrice decimal(12,2) not null,
	createTime datetime default current_timestamp,
	primary key (id)
);