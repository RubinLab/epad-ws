CREATE TABLE epadstatistics_template (id integer unsigned NOT NULL AUTO_INCREMENT,
host varchar(128),
templateLevelType varchar(128),
templateName varchar(128),
authors varchar(128),
version varchar(10),
templateDescription varchar(256),
templateType varchar(128),
templateCode varchar(128),
numOfAims Integer,
templateText medium,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;


UPDATE dbversion SET version = '2.32';
commit;
