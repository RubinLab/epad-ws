ALTER TABLE epadstatistics  ADD COLUMN numOfFiles integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfPlugins integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfTemplates integer;
ALTER TABLE events ADD COLUMN series_uid varchar(128);
ALTER TABLE events ADD COLUMN study_uid varchar(128);
ALTER TABLE events ADD COLUMN project_id varchar(128);
ALTER TABLE events ADD COLUMN project_name varchar(128);
ALTER TABLE events ADD COLUMN error varchar(5);

CREATE INDEX epad_files_ind1 on epad_files(file_status);
CREATE INDEX epad_files_ind2 on epad_files(file_path);
CREATE INDEX series_status_ind on series_status(series_iuid);

UPDATE dbversion SET version = '1.8';
commit;
