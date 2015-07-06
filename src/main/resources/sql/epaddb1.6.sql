ALTER TABLE worklist DROP INDEX worklist_ind;
CREATE UNIQUE INDEX worklist_ind on worklist(worklistid);

ALTER TABLE project_user ADD COLUMN defaulttemplate varchar(128);

UPDATE dbversion SET version = '1.6';
commit;
