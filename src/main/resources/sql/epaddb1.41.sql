DROP TABLE if exists nondicom_series;

CREATE TABLE nondicom_series (id integer unsigned NOT NULL AUTO_INCREMENT,
seriesuid varchar(128),
study_id integer unsigned,
description varchar(1000),
seriesdate date,
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_projectsubjectstudy_study (study_id),
CONSTRAINT FK_series_study FOREIGN KEY (study_id) REFERENCES study(id)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX project_seriesuid_ind on nondicom_series(seriesuid);

ALTER TABLE study add description varchar(1000);

ALTER TABLE epad_file add enabled tinyint(1);

CREATE TABLE reviewer (id integer unsigned NOT NULL AUTO_INCREMENT,
reviewer varchar(128),
reviewee varchar(128),
creator varchar(128),
createdtime timestamp,
updatetime timestamp,
updated_by varchar(64),
PRIMARY KEY (id),
KEY FK_reviewer_user (reviewer),
CONSTRAINT FK_reviewer_user FOREIGN KEY (reviewer) REFERENCES user(username),
KEY FK_reviewee_user (reviewee),
CONSTRAINT FK_reviewee_user FOREIGN KEY (reviewee) REFERENCES user(username)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE UNIQUE INDEX uk_reviewer_ind on reviewer(reviewer,reviewee);

UPDATE dbversion SET version = '1.41';
commit;


