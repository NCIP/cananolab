DROP DATABASE IF EXISTS canano;
CREATE DATABASE canano
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;

DELETE FROM mysql.user WHERE User='cananolab_app';

GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO 'cananolab_app'@'localhost' IDENTIFIED BY 'go!234' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO 'cananolab_app'@'%' IDENTIFIED BY 'go!234' WITH GRANT OPTION;

select count(*) from information_schema.tables where table_schema='canano' and table_type='BASE TABLE';
