create table SkuList(
	id int not null auto_increment,
	goodsId int not null,
	price decimal(12, 2) not null,
	superiorPrice decimal(12,2) not null,
	s1Id int not null,
	s2Id int not null,
	s3Id int not null,
	stockNum int not null,
	createTime datetime default current_timestamp,
	isDeleted int not null default 0,
	primary key (id)
);