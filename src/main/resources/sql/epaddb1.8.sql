ALTER TABLE epadstatistics  ADD COLUMN numOfFiles integer;

UPDATE dbversion SET version = '1.7';
commit;
