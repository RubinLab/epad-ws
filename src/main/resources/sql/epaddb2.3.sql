ALTER TABLE project_pluginparameter ADD COLUMN type varchar(20) NULL;
ALTER TABLE project_pluginparameter ADD COLUMN description varchar(150) NULL;

DROP TABLE if exists project_template;
CREATE TABLE project_template (id integer unsigned NOT NULL AUTO_INCREMENT,
project_id integer unsigned,
template_id integer unsigned,
enabled tinyint(1),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX uk_project_template_ind on project_template(project_id,template_id);
CONSTRAINT FK_project_template_tid FOREIGN KEY (template_id) REFERENCES template(id);
CONSTRAINT FK_project_template_pid FOREIGN KEY (project_id) REFERENCES project(id);

DROP TABLE if exists template;
CREATE TABLE template (id integer unsigned NOT NULL AUTO_INCREMENT,
--templateType
templateLevelType varchar(128), 
templateUID varchar(128),
templateName varchar(128),
authors varchar(128),
version varchar(10),
templateCreationDate timestamp NULL,
templateDescription varchar(256),
codingSchemeVersion varchar(10),
--codeMeaning
templateType varchar(128), 
--codevalue
templateCode varchar(128),
codingSchemeDesignator varchar(128),
modality varchar(12),
file_id integer unsigned,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
PRIMARY KEY (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CONSTRAINT FK_template_file FOREIGN KEY (file_id) REFERENCES epad_file(id);



UPDATE dbversion SET version = '2.2';
commit;
