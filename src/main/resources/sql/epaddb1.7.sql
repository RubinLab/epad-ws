ALTER TABLE worklist ADD COLUMN name varchar(128);
ALTER TABLE plugin ADD COLUMN developer varchar(128);
ALTER TABLE plugin ADD COLUMN documentation varchar(2000);
ALTER TABLE plugin ADD COLUMN rateTotal integer;
ALTER TABLE plugin ADD COLUMN rateCount integer;
ALTER TABLE worklist_subject DROP INDEX worklist_subject_ind;
ALTER TABLE worklist_study DROP INDEX worklist_study_ind;
CREATE UNIQUE INDEX worklist_subject_ind on worklist_subject(worklist_id,subject_id, project_id);
CREATE UNIQUE INDEX worklist_study_ind on worklist_study(worklist_id,study_id, project_id);

UPDATE dbversion SET version = '1.7';
commit;
