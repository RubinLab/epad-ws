rsync -avzh DicomProxy/resources epad@$1:/epad/DicomProxy/
mysqldump -u pacs -ppacs epaddb > epaddb.sql
mysqldump -u pacs -ppacs pacsdb > pacsdb.sql
rsync epaddb.sql epad@$1:
rsync pacsdb.sql epad@$1: