ALTER TABLE project_pluginparameter ADD COLUMN type varchar(20) NULL;
ALTER TABLE project_pluginparameter ADD COLUMN description varchar(150) NULL;

DROP TABLE if exists project_template;
CREATE TABLE project_template (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
templatename varchar(128),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX uk_project_template_ind on project_template(project_id,templatename);


DROP TABLE if exists template;
CREATE TABLE template (id integer unsigned NOT NULL AUTO_INCREMENT,
templateName varchar(128),
templateCode varchar(128), --codevalue
templateType varchar(128), --codeMeaning
templateDescription varchar(128),
modality varchar(12),
templateUID varchar(128),
templateLevelType varchar(128),
fileName varchar(128),
filePath varchar(512),
fileType varchar(64),
description varchar(512),
author varchar(128),
length integer,
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX uk_templatecode_ind on template(project_id,templateCode);


UPDATE dbversion SET version = '2.2';
commit;
