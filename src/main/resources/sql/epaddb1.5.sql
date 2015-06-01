DROP TABLE if exists worklist_study;
DROP TABLE if exists worklist_subject;
DROP TABLE if exists worklist;

CREATE TABLE worklist (id integer unsigned NOT NULL AUTO_INCREMENT,
worklistid varchar(128),
description varchar(1000),
project_id integer unsigned,
user_id integer unsigned,
status varchar(256),
startdate date,
completedate date,
duedate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_worklist_user (user_id),
CONSTRAINT FK_worklist_user FOREIGN KEY (user_id) REFERENCES user(id),
KEY FK_worklist_project (project_id),
CONSTRAINT FK_worklist_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_ind on worklist(project_id,user_id);

CREATE TABLE worklist_subject (id integer unsigned NOT NULL AUTO_INCREMENT,
worklist_id integer unsigned,
subject_id integer unsigned,
status varchar(256),
startdate date,
completedate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_worklistsubject_subject (subject_id),
CONSTRAINT FK_worklistsubject_subject FOREIGN KEY (subject_id) REFERENCES subject(id),
KEY FK_worklistsubject_worklist (worklist_id),
CONSTRAINT FK_worklistsubject_worklist FOREIGN KEY (worklist_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_subject_ind on worklist_subject(worklist_id,subject_id);

CREATE TABLE worklist_study (id integer unsigned NOT NULL AUTO_INCREMENT,
worklist_id integer unsigned,
study_id integer unsigned,
status varchar(256),
startdate date,
completedate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_workliststudy_study (study_id),
CONSTRAINT FK_workliststudy_study FOREIGN KEY (study_id) REFERENCES study(id),
KEY FK_workliststudy_worklist (worklist_id),
CONSTRAINT FK_workliststudy_worklist FOREIGN KEY (worklist_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_study_ind on worklist_study(worklist_id,study_id);

UPDATE dbversion SET version = '1.5';
commit;

