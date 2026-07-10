-- Runs once on first MySQL container start (docker-entrypoint-initdb.d).
-- productdb is created by MYSQL_DATABASE; the rest here.

CREATE DATABASE IF NOT EXISTS orderdb;
CREATE DATABASE IF NOT EXISTS authdb;

-- Debezium needs a user with replication privileges — it connects like a MySQL
-- replica and reads the binlog. Same setup as production, minus TLS.
CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED BY 'dbz';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT, LOCK TABLES ON *.* TO 'debezium'@'%';
FLUSH PRIVILEGES;
