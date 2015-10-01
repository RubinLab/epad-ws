ALTER TABLE plugin ADD COLUMN developer varchar(128);
ALTER TABLE plugin ADD COLUMN documentation varchar(2000);
ALTER TABLE plugin ADD COLUMN rateTotal integer;
ALTER TABLE plugin ADD COLUMN rateCount integer;

UPDATE dbversion SET version = '1.7';
commit;
