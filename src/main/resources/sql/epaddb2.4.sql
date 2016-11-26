
CREATE TABLE if not exists pixel_values (
  id integer unsigned NOT NULL AUTO_INCREMENT,
  file_path varchar(256),
  image_uid varchar(128),
  frame_num integer,
  value mediumtext,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


update lexicon set CODE_MEANING='Standard Deviation', CODE_VALUE='R-10047', description='Standard Deviation', SCHEMA_DESIGNATOR='SRT' 
where  CODE_VALUE='99EPADA5';
update lexicon set CODE_MEANING='Mean', CODE_VALUE='R-00317', description='Mean', SCHEMA_DESIGNATOR='SRT' 
where  CODE_VALUE='RID39224';
update lexicon set CODE_MEANING='Minimum', CODE_VALUE='R-404FB', description='Minimum', SCHEMA_DESIGNATOR='SRT' 
where  CODE_VALUE='99EPADA6';
update lexicon set CODE_MEANING='Maximum', CODE_VALUE='G-A437', description='Maximum', SCHEMA_DESIGNATOR='SRT' 
where  CODE_VALUE='99EPADA7';


CREATE TABLE if not exists epadstatistics_template (id integer unsigned NOT NULL AUTO_INCREMENT,
host varchar(128),
templateLevelType varchar(128),
templateName varchar(128),
authors varchar(128),
version varchar(10),
templateDescription varchar(256),
templateType varchar(128),
templateCode varchar(128),
numOfAims Integer,
templateText mediumtext,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;


UPDATE dbversion SET version = '2.4';
commit;
