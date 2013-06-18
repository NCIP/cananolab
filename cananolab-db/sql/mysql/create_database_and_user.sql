/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

DROP DATABASE IF EXISTS canano;
CREATE DATABASE canano
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;

GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO '@database.user@'@'localhost' IDENTIFIED BY '@database.password@' WITH GRANT OPTION;
GRANT SELECT,INSERT,UPDATE,DELETE ON canano.* TO '@database.user@'@'%' IDENTIFIED BY '@database.password@' WITH GRANT OPTION;
