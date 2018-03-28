create index STUDYUID_INDEX ON annotations (StudyUID);
create index PATIENTID_INDEX ON annotations (PatientID);
create index USER_FOREIGN_KEY_INDEX ON annotations (userloginname);

UPDATE dbversion SET version = '3.0';
commit;
