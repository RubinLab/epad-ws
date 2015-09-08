ALTER TABLE series_status ADD COLUMN default_tags varchar(256);

UPDATE dbversion SET version = '1.62';
commit;
