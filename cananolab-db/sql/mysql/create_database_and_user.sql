DROP DATABASE IF EXISTS canano;
CREATE DATABASE canano
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;

GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO '@database.user@'@'localhost' IDENTIFIED BY '@database.password@' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO '@database.user@'@'%' IDENTIFIED BY '@database.password@' WITH GRANT OPTION;
