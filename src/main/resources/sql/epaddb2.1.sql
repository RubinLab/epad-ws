DROP TABLE if exists project_subject_study_series_user_status;
CREATE TABLE project_subject_study_series_user_status (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
subject_id integer unsigned,
study_id integer unsigned,
series_uid varchar(256),
user_id integer unsigned,
annotationStatus integer,
startdate date,
completedate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_psssustatus_project (project_id),
CONSTRAINT FK_psssustatus_project FOREIGN KEY (project_id) REFERENCES project(id),
KEY FK_psssustatus_subject (subject_id),
CONSTRAINT FK_psssustatus_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
KEY FK_psssustatus_study (study_id),
CONSTRAINT FK_psssustatus_study FOREIGN KEY (study_id) REFERENCES study(id),
KEY FK_psssustatus_series (series_uid),
CONSTRAINT FK_psssustatus_series FOREIGN KEY (series_uid) REFERENCES series_status(series_iuid),
KEY FK_psssustatus_user (user_id),
CONSTRAINT FK_psssustatus_user FOREIGN KEY (user_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX psssustatus_user on project_subject_study_series_user_status(project_id,subject_id,study_id,series_uid,user_id);


UPDATE dbversion SET version = '2.0';
commit;
