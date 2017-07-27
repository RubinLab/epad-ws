CREATE TABLE IF NOT EXISTS user_flaggedimage (id integer unsigned NOT NULL AUTO_INCREMENT,
username varchar(128),
image_uid varchar(128),
project_id varchar(128),
subject_id varchar(128),
study_id varchar(128),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

UPDATE dbversion SET version = '2.7';
commit;
