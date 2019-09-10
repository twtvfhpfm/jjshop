create table PrizeDraw(
	id int not null auto_increment,
	uid int not null,
	orderId varchar(32) not null,
	couponId int not null default 0,
	isUsed int not null default 0,
	createTime datetime default current_timestamp,
	primary key (id)
);