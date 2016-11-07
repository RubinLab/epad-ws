--DROP TABLE if EXISTS pixel_values;
CREATE TABLE pixel_values (
  id integer unsigned NOT NULL AUTO_INCREMENT,
  file_path varchar(256),
  image_uid varchar(128),
  frame_num integer,
  value mediumtext,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

UPDATE dbversion SET version = '2.3';
commit;
