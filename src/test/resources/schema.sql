
CREATE TABLE student(sid int PRIMARY KEY auto_increment,name varchar(100));

CREATE TABLE subject(subid int PRIMARY KEY auto_increment,subname varchar(100));

CREATE TABLE relation(sid int,subid int);



