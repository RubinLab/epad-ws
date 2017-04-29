ALTER TABLE subject ADD COLUMN displayuid varchar(128);
UPDATE subject set displayuid=subjectuid;

UPDATE dbversion SET version = '2.6';
commit;
