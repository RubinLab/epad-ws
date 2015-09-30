ALTER TABLE worklist ADD COLUMN name varchar(128);

UPDATE dbversion SET version = '1.62';
commit;
