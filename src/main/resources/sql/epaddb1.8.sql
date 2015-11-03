ALTER TABLE epadstatistics  ADD COLUMN numOfFiles integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfPlugins integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfTemplates integer;
ALTER TABLE events ADD COLUMN series_uid varchar(128);
ALTER TABLE events ADD COLUMN study_uid varchar(128);
ALTER TABLE events ADD COLUMN project_id varchar(128);
ALTER TABLE events ADD COLUMN project_name varchar(128);
ALTER TABLE events ADD COLUMN error varchar(5);

UPDATE dbversion SET version = '1.7';
commit;
