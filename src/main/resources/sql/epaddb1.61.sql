ALTER TABLE eventlog ADD COLUMN filename varchar(128);
ALTER TABLE eventlog ADD COLUMN error tinyint(1);

UPDATE dbversion SET version = '1.61';
commit;
