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

UPDATE dbversion SET version = '1.6';
commit;
