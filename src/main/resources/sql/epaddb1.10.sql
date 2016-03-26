ALTER TABLE user MODIFY COLUMN lastlogin timestamp NULL;

UPDATE dbversion SET version = '1.10';
commit;
