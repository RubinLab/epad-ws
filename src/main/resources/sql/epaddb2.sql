ALTER TABLE plugin ADD COLUMN processmultipleaims tinyint(1) NULL DEFAULT FALSE;

UPDATE dbversion SET version = '2.0';
commit;
