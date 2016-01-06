ALTER TABLE eventlog MODIFY COLUMN filename varchar(250);

UPDATE dbversion SET version = '1.9';
commit;
