create table User(
	id int not null auto_increment,
	username varchar(200) default '' not null,
	password varchar(200) default '' not null,
	createTime datetime default current_timestamp,
	role int default 0 not null,
	primary key (id)
);