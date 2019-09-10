create table Prize(
	id int not null auto_increment,
	amount decimal(12,2) not null,
	num int not null,
	expireDays int not null,
	primary key (id)
);