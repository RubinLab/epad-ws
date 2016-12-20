ALTER TABLE annotations ADD COLUMN is_dicomsr tinyint(1);

UPDATE dbversion SET version = '2.4';
commit;
