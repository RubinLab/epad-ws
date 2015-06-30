ALTER TABLE worklist DROP INDEX worklist_ind;
CREATE UNIQUE INDEX worklist_ind on worklist(worklistid);

UPDATE dbversion SET version = '1.6';
commit;
