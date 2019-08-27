create table ChargeCode(
  id int not null auto_increment,
  type int not null, -- 0:wechat 1:alipay
  amount decimal(12,2) not null,
	data mediumblob not null,
	primary key (id)
);