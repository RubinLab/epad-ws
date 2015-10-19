ALTER TABLE epadstatistics  ADD COLUMN numOfFiles integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfPlugins integer;
ALTER TABLE epadstatistics  ADD COLUMN numOfTemplates integer;

UPDATE dbversion SET version = '1.7';
commit;
