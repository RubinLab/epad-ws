mysql -u pacs -ppacs  -e 'drop database epaddb;'
mysql -u pacs -ppacs  -e 'create database epaddb;'
mysql -u pacs -ppacs  -e 'drop database pacsdb;'
mysql -u pacs -ppacs  -e 'create database pacsdb;'
mysql -u pacs -ppacs epaddb < epaddb.sql
mysql -u pacs -ppacs pacsdb < pacsdb.sql