create table Sku(
	id int not null auto_increment,
	goodsId int not null,
	imgId int not null,
	keyName varchar(256) not null,
	valueName varchar(256) not null,
	skuOrder int not null,-- s1, s2, s3
	createTime datetime default current_timestamp,
	isDeleted int not null default 0,
	primary key (id)
);