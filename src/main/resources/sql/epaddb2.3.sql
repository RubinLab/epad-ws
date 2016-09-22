ALTER TABLE project_pluginparameter ADD COLUMN type varchar(20) NULL;
ALTER TABLE project_pluginparameter ADD COLUMN description varchar(150) NULL;

UPDATE dbversion SET version = '2.2';
commit;
