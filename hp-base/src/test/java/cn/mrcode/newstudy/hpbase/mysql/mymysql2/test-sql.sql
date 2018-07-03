create table employee (id int not null primary key,name varchar(100),sharding_id int not null);

insert into employee(id,name,sharding_id) values(1, 'I am db1',10000);
insert into employee(id,name,sharding_id) values(2, 'I am db2',10010);
insert into employee(id,name,sharding_id) values(3, 'I am db3',10020);
insert into employee(id,name,sharding_id) values(4, 'I am db1',10000);
insert into employee(id,name,sharding_id) values(5, 'I am db2',10010);
insert into employee(id,name,sharding_id) values(6, 'I am db3',10020);