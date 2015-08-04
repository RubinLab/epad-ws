ALTER TABLE worklist DROP INDEX worklist_ind;
CREATE UNIQUE INDEX worklist_ind on worklist(worklistid);

ALTER TABLE project_user ADD COLUMN defaulttemplate varchar(128);

CREATE TABLE project_file (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
file_id integer unsigned,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_project_file_file (file_id),
CONSTRAINT FK_project_file_file FOREIGN KEY (file_id) REFERENCES epad_file(id),
KEY FK_project_file_project (project_id),
CONSTRAINT FK_project_file_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_file_ind on project_file(project_id,file_id);

DROP TABLE if exists worklist_subject;
DROP TABLE if exists worklist_study;
DROP TABLE if exists worklist;
CREATE TABLE worklist (id integer unsigned NOT NULL AUTO_INCREMENT,
worklistid varchar(128),
description varchar(1000),
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
CONSTRAINT FK_worklist_user FOREIGN KEY (user_id) REFERENCES user(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_ind on worklist(worklistid);

CREATE TABLE worklist_subject (id integer unsigned NOT NULL AUTO_INCREMENT,
worklist_id integer unsigned,
subject_id integer unsigned,
project_id integer unsigned,
sortorder integer unsigned,
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
CONSTRAINT FK_worklistsubject_worklist FOREIGN KEY (worklist_id) REFERENCES worklist(id),
KEY FK_worklistsubject_project (project_id),
CONSTRAINT FK_worklistsubject_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_subject_ind on worklist_subject(worklist_id,subject_id);

CREATE TABLE worklist_study (id integer unsigned NOT NULL AUTO_INCREMENT,
worklist_id integer unsigned,
study_id integer unsigned,
project_id integer unsigned,
sortorder integer unsigned,
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
CONSTRAINT FK_workliststudy_worklist FOREIGN KEY (worklist_id) REFERENCES worklist(id),
KEY FK_workliststudy_project (project_id),
CONSTRAINT FK_workliststudy_project FOREIGN KEY (project_id) REFERENCES project(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX worklist_study_ind on worklist_study(worklist_id,study_id);

DROP TABLE if exists eventlog;
CREATE TABLE eventlog (id integer unsigned NOT NULL AUTO_INCREMENT,
projectID varchar(128),
subjectuid varchar(128),
studyUID varchar(128),
seriesUID varchar(128),
imageUID varchar(128),
aimID varchar(128),
username varchar(128),
function varchar(128),
params varchar(128),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX eventlog_ind1 on eventlog(username);

UPDATE dbversion SET version = '1.6';
commit;
