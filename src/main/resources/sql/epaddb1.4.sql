
DROP TABLE if exists users;

CREATE TABLE project (id integer unsigned NOT NULL AUTO_INCREMENT,
name varchar(128),
projectid varchar(128),
type varchar(32),
description varchar(1000),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into project values(null,'Unassigned','unassigned','Public','Default Project','admin',sysdate(),sysdate(),'admin');
commit;
CREATE UNIQUE INDEX project_projectid_ind on project(projectid);
CREATE UNIQUE INDEX project_name_ind on project(name);

CREATE TABLE user (id integer unsigned NOT NULL AUTO_INCREMENT,
username varchar(128),
firstname varchar(256),
lastname varchar(256),
email varchar(256),
password varchar(256),
permissions varchar(2000),
enabled tinyint(1),
admin tinyint(1),
passwordexpired tinyint(1),
passwordupdate date,
lastlogin timestamp,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX user_username_ind on user(username);
insert into user values(null,'admin','Epad','Admin','willrett@stanford.edu','admin','',1,1,1,null,null,'admin',sysdate(),sysdate(),'admin');
commit;

CREATE TABLE subject (id integer unsigned NOT NULL AUTO_INCREMENT,
subjectuid varchar(128),
name varchar(256),
gender varchar(16),
dob date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX subject_subjectuid_ind on subject(subjectuid);

CREATE TABLE study (id integer unsigned NOT NULL AUTO_INCREMENT,
studyuid varchar(128),
studydate date,
subject_id integer unsigned,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_study_subject (subject_id),
CONSTRAINT FK_study_subject FOREIGN KEY (subject_id) REFERENCES subject(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX study_studyuid_ind on study(studyuid);

CREATE TABLE project_user (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
user_id integer unsigned,
role varchar(64),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_project_user_user (user_id),
CONSTRAINT FK_project_user_user FOREIGN KEY (user_id) REFERENCES user(id),
KEY FK_project_user_project (project_id),
CONSTRAINT FK_project_user_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_user_ind on project_user(project_id,user_id);

CREATE TABLE project_subject (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
subject_id integer unsigned,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_projectsubject_subject (subject_id),
CONSTRAINT FK_projectsubject_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
KEY FK_projectsubject_project (project_id),
CONSTRAINT FK_projectsubject_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_subject_ind on project_subject(project_id,subject_id);

CREATE TABLE project_subject_study (id integer unsigned NOT NULL AUTO_INCREMENT,
proj_subj_id integer unsigned,
study_id integer unsigned,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_projectsubjectstudy_projectsubject (proj_subj_id),
CONSTRAINT FK_projectsubjectstudy_projectsubject FOREIGN KEY (proj_subj_id) REFERENCES project_subject(id),
KEY FK_projectsubjectstudy_study (study_id),
CONSTRAINT FK_projectsubjectstudy_study FOREIGN KEY (study_id) REFERENCES study(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_subject_study_ind on project_subject_study(proj_subj_id,study_id);

CREATE TABLE project_subject_user (id integer unsigned NOT NULL AUTO_INCREMENT,
proj_subj_id integer unsigned,
user_id integer unsigned,
status varchar(64),
statustime timestamp,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_projectsubjectuser_projectsubject (proj_subj_id),
CONSTRAINT FK_projectsubjectuser_projectsubject FOREIGN KEY (proj_subj_id) REFERENCES project_subject(id),
KEY FK_projectsubjectuser_user (user_id),
CONSTRAINT FK_projectsubjectuser_user FOREIGN KEY (user_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_subject_user_ind on project_subject_user(proj_subj_id,user_id);

CREATE TABLE epad_file (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
subject_id integer unsigned,
study_id integer unsigned,
series_uid varchar(256),
name varchar(128),
filepath varchar(512),
filetype varchar(64),
mimetype varchar(64),
description varchar(512),
length integer,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_epadfile_study (study_id),
CONSTRAINT FK_epadfile_study FOREIGN KEY (study_id) REFERENCES study(id),
KEY FK_epadfile_subject (subject_id),
CONSTRAINT FK_epadfile_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
KEY FK_epadfile_project (project_id),
CONSTRAINT FK_epadfile_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE remote_pac_query (id integer unsigned NOT NULL AUTO_INCREMENT,
pacid varchar(64),
requestor varchar(128),
subject_id integer unsigned,
project_id integer unsigned,
modality varchar(8),
period varchar(8),
laststudydate varchar(8),
enabled tinyint(1),
lastquerytime timestamp,
lastquerystatus varchar(1024),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_query_project (project_id),
CONSTRAINT FK_query_project FOREIGN KEY (project_id) REFERENCES project(id),
KEY FK_query_subject (subject_id),
CONSTRAINT FK_query_subject FOREIGN KEY (subject_id) REFERENCES subject(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX remotepacquery_pacid_subject on remote_pac_query(pacid,subject_id);

ALTER TABLE dbversion MODIFY COLUMN version varchar(6);

UPDATE dbversion SET version = '1.4';
commit;


