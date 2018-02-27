CREATE TABLE annotations_v2.9 AS SELECT * FROM annotations;

UPDATE lexicon SET CODE_VALUE='C48870', SCHEMA_DESIGNATOR='NCI', SCHEMA_VERSION=null where  CODE_VALUE='99EPADD1';
UPDATE lexicon SET CODE_VALUE='jjv-5', SCHEMA_DESIGNATOR='99EPAD', SCHEMA_VERSION='1' where  CODE_VALUE='99EPADP1';

SET @plugin = (SELECT MAX(ID) FROM lexicon where CODE_VALUE='99EPADA2');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('vanderbilt', 'vanderbilt', 'Vanderbilt Plugin',@plugin,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('smp-1', 'sampleplugin', 'Sample Plugin',@plugin,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('rsz-1', 'riesz', 'Riesz Plugin',@plugin,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('lseg-1', 'lesionseg', 'Lesionseg Plugin',@plugin,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('ts-1', 'tedseg', 'Tedseg Plugin',@plugin,'99EPAD','1','admin');

INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('SEG', 'SEG Only', 'SEG Only','99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('ROI', 'ROI Only', 'ROI Only','99EPAD','1','admin');

SET @jjv = (SELECT MAX(ID) FROM lexicon where CODE_VALUE='jjv-5');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('jjv-1', 'Lung CT', 'Lung CT',@jjv,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('jjv-2', 'Liver CT', 'Liver CT',@jjv,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('jjv-3', 'Breast MRI', 'Breast MRI',@jjv,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('jjv-4', 'Bone X-ray', 'Bone X-ray',@jjv,'99EPAD','1','admin');

INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('RECIST', 'Tumor assessment', 'Tumor assessment','99EPAD','1','admin');
SET @recist = (SELECT MAX(ID) FROM lexicon where CODE_VALUE='RECIST');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S71', 'Target Lesion', 'Target Lesion',@recist,'99EPAD','1','admin'); 
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S72', 'Non-Target Lesion', 'Non-Target Lesion',@recist,'99EPAD','1','admin'); 
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S73', 'New Lesion', 'New Lesion',@recist,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S74', 'Resolved Lesion', 'Resolved Lesion',@recist,'99EPAD','1','admin'); 
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S81', 'Lesion Baseline Evaluation', 'Lesion Baseline Evaluation',@recist,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S82', 'Lesion Followup Evaluation', 'Lesion Followup Evaluation',@recist,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S83', 'tracked', 'tracked',@recist,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S84', 'not tracked', 'not tracked',@recist,'99EPAD','1','admin');

INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('RECIST_v2', 'Tumor assessment', 'Tumor assessment','99EPAD','1','admin');
SET @recistv2 = (SELECT MAX(ID) FROM lexicon where CODE_VALUE='RECIST_v2');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S99', 'undefined organ', 'undefined organ',@recistv2,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S90', 'FU Number (0=Baseline)', 'FU Number (0=Baseline)',@recistv2,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S75', 'Non-cancer Lesion', 'Non-cancer Lesion',@recistv2,'99EPAD','1','admin');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('S86', 'Evaluable', 'Evaluable',@recistv2,'99EPAD','1','admin');

SET @rsz = (SELECT MAX(ID) FROM lexicon where CODE_VALUE='rsz-1');
INSERT IGNORE INTO lexicon (CODE_VALUE, CODE_MEANING, description,PARENT_ID,SCHEMA_DESIGNATOR,SCHEMA_VERSION,creator) VALUES('rsz-2', 'energie', 'energie',@rsz,'99EPAD','1','admin');

UPDATE dbversion SET version = '3.0';
commit;
