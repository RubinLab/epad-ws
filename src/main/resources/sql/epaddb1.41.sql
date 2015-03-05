DROP TABLE if exists nondicom_series;

CREATE TABLE nondicom_series (id integer unsigned NOT NULL AUTO_INCREMENT,
seriesuid varchar(128),
study_id integer unsigned,
description varchar(1000),
seriesdate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_projectsubjectstudy_study (study_id),
CONSTRAINT FK_series_study FOREIGN KEY (study_id) REFERENCES study(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_seriesuid_ind on nondicom_series(seriesuid);

UPDATE dbversion SET version = '1.41';
commit;


