create table Goods(
	id int not null auto_increment,
	categoryId int not null,
	price decimal(12, 2) not null,
	superiorPrice decimal(12, 2) not null,
	remain int not null,
	sales int not null default 0,
	title varchar(200) default '' not null,
	description varchar(20000) default '',
	thumb varchar(500) default '',
	createTime datetime default current_timestamp,
	isDeleted int not null default 0,
	primary key (id)
);