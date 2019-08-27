create table Address(
	id int not null auto_increment,
	uid int not null,
	name varchar(200) not null,
	mobile varchar(200) not null,
	province varchar(32) not null,
	city varchar(32) not null,
	county varchar(32) not null,
	addressDetail varchar(200) not null,
	areaCode varchar(32) not null,
	isDefault int default 0 not null,
	isDeleted int not null default 0,
	createTime datetime default current_timestamp,
	primary key (id)
);