#!/bin/bash
rm -f /tmp/epaddb_*.sql.gz
mysqldump -u pacs -ppacs epaddb | gzip > /tmp/epaddb_`date '+%a'`.sql.gz && rsync -ave ssh /tmp/epad*.sql.gz epad@backupHost:/home/mysql-Backups/
rm -f /tmp/pacsdb_*.sql.gz
mysqldump -u pacs -ppacs pacsdb | gzip > /tmp/pacsdb_`date '+%a'`.sql.gz && rsync -ave ssh /tmp/pacs*.sql.gz epad@backupHost:/home/mysql-Backups/


