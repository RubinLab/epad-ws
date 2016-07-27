ALTER TABLE epad_file DROP COLUMN templateleveltype; 
ALTER TABLE epad_file ADD COLUMN templateleveltype varchar(10) NULL;
UPDATE epad_file set templateleveltype = 'image' WHERE filetype = 'Template' and templateleveltype is null;

UPDATE dbversion SET version = '2.1';
commit;
