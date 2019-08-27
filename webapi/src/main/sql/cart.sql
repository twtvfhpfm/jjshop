create table Cart(
	id int not null auto_increment,
	uid int not null, -- user id
	goodsId int not null,
	skuListId int not null,
	price decimal(12,2) not null,
	superiorPrice decimal(12, 2) not null,
	num int not null,
	ordered int default 0 not null,
	createTime datetime default current_timestamp,
	primary key (id)
);