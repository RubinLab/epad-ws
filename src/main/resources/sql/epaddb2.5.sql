CREATE TABLE epadstatistics_monthly (id integer unsigned NOT NULL AUTO_INCREMENT,
numOfUsers Integer,
numOfProjects Integer,
numOfPatients Integer,
numOfStudies Integer,
numOfSeries Integer,
numOfAims Integer,
numOfDSOs Integer,
numOfWorkLists Integer,
numOfPacs Integer,
numOfAutoQueries Integer,
numOfFiles Integer,
numOfPlugins Integer,
numOfTemplates Integer,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE annotations ADD COLUMN is_dicomsr tinyint(1);


UPDATE dbversion SET version = '2.4';
commit;
