--DROP TABLE if EXISTS pixel_values;
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

UPDATE dbversion SET version = '2.32';
commit;
