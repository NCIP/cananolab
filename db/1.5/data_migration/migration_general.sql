USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

-- common_lookup
source common_lookup_migration.sql;

-- drop obsolete tables
DROP TABLE IF EXISTS sample_management;
DROP TABLE IF EXISTS sample_container;
DROP TABLE IF EXISTS sample_container_storage;
DROP TABLE IF EXISTS storage;
DROP TABLE IF EXISTS associated_file;

-- merge protocol_file and protocol
ALTER TABLE protocol
    ADD COLUMN protocol_abbreviation VARCHAR(200),
    ADD COLUMN protocol_version VARCHAR(200),
	ADD COLUMN file_pk_id BIGINT
;
ALTER TABLE protocol ADD CONSTRAINT FK_protocol_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

-- update protocol_abbreviation to be the same as protocol name
update protocol
set protocol_abbreviation=protocol_name;

-- update protocol_version and file_pk_id
update protocol a, protocol_file b, lab_file c
set a.protocol_version=c.version,
a.file_pk_id=c.file_pk_id
where a.protocol_pk_id=b.protocol_pk_id
and b.protocol_file_pk_id=c.file_pk_id;

--drop protocol_file
DROP TABLE IF EXISTS protocol_file;

-- update foreign key in characterization from protocol_file_pk_id  to protocol_pk_id
ALTER TABLE canano.characterization
 DROP FOREIGN KEY FK_characterization_protocol_file,
 CHANGE protocol_file_pk_id protocol_pk_id BIGINT;

ALTER TABLE canano.characterization
 ADD CONSTRAINT FK_characterization_protocol FOREIGN KEY (protocol_pk_id) REFERENCES canano.protocol (protocol_pk_id);

update characterization a, protocol b
set a.protocol_pk_id=b.protocol_pk_id
where a.protocol_pk_id=b.file_pk_id
;

-- change lab_file to file
ALTER TABLE lab_file DROP COLUMN version;
ALTER TABLE lab_file RENAME file;
ALTER TABLE composition_lab_file RENAME composition_file;
ALTER TABLE composition_file
 DROP FOREIGN KEY FK_composition_lab_file_lab_file;
ALTER TABLE composition_file ADD CONSTRAINT FK_composition_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE chemical_association_lab_file RENAME chemical_association_file;
ALTER TABLE chemical_association_file
 DROP FOREIGN KEY FK_chemical_association_lab_file_chemical_association;
ALTER TABLE chemical_association_file
 DROP FOREIGN KEY FK_chemical_association_lab_file_lab_file;
ALTER TABLE chemical_association_file ADD CONSTRAINT FK_chemical_association_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id);
ALTER TABLE chemical_association_file ADD CONSTRAINT FK_chemical_association_file_chemical_association
	FOREIGN KEY (chemical_association_pk_id) REFERENCES chemical_association (chemical_association_pk_id)
;

ALTER TABLE keyword_lab_file RENAME keyword_file;
ALTER TABLE keyword_file
 DROP FOREIGN KEY FK_keyword_lab_file_keyword,
 DROP FOREIGN KEY FK_keyword_lab_file_lab_file,
 CHANGE lab_file_pk_id file_pk_id BIGINT NOT NULL;
ALTER TABLE keyword_file ADD CONSTRAINT FK_keyword_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id);
ALTER TABLE keyword_file ADD CONSTRAINT FK_keyword_file_keyword
	FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id);

ALTER TABLE functionalizing_entity_lab_file RENAME functionalizing_entity_file;
ALTER TABLE functionalizing_entity_file
	DROP FOREIGN KEY FK_functionalizing_entity_lab_file_functionalizing_entity;
ALTER TABLE functionalizing_entity_file
	DROP FOREIGN KEY FK_functionalizing_entity_lab_file_lab_file;
ALTER TABLE functionalizing_entity_file ADD CONSTRAINT FK_functionalizing_entity_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id);
ALTER TABLE functionalizing_entity_file ADD CONSTRAINT FK_functionalizing_entity_file_functionalizing_entity
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

DELETE FROM nanoparticle_sample_publication
where particle_sample_pk_id not in
(select particle_sample_pk_id from nanoparticle_sample);
-- end of lab_file to file

-- update publication category
update publication
set category='proceeding'
where category='in proceedings';

-- missing constraint between author and publication
ALTER TABLE author RENAME author0;
ALTER TABLE author_publication RENAME author_publication0;

CREATE TABLE author_publication
(
	author_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	PRIMARY KEY (author_pk_id, publication_pk_id),
	KEY (author_pk_id),
	KEY (publication_pk_id)
) TYPE=InnoDB
;

CREATE TABLE author
(
	author_pk_id BIGINT NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	initial VARCHAR(10),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (author_pk_id),
	UNIQUE (author_pk_id)
) TYPE=InnoDB
;

INSERT INTO author (author_pk_id, first_name, last_name, initial, created_by, created_date)
SELECT  author_pk_id, first_name, last_name, initial, created_by, created_date FROM author0;

INSERT INTO author_publication (author_pk_id, publication_pk_id)
SELECT author_pk_id, publication_pk_id FROM author_publication0;

DROP TABLE author0;
DROP TABLE author_publication0;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_author
	FOREIGN KEY (author_pk_id) REFERENCES author (author_pk_id)
;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_publication
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;

-- missing constraint between composition and file
ALTER TABLE composition_file RENAME composition_file0;

CREATE TABLE composition_file
(
	composition_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (composition_pk_id, file_pk_id),
	KEY (composition_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;

INSERT INTO composition_file (composition_pk_id, file_pk_id)
SELECT * FROM composition_file0;

DROP TABLE composition_file0;

ALTER TABLE composition_file ADD CONSTRAINT FK_composition_file_composition
	FOREIGN KEY (composition_pk_id) REFERENCES composition (composition_pk_id)
;

ALTER TABLE composition_file ADD CONSTRAINT FK_composition_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;


-- nanoparticle_sample to sample
source sample.sql

-- nanoparticle_entity to nanomaterial_entity
source nanomaterial_entity.sql

-- source enhancement
source poc_migration.sql

-- instrument and technique
source instrument_migration.sql

-- datum and condition
source datum_migration.sql

-- characterization
source characterization_migration.sql

-- csm migration
source csm_migration.sql
 
--update affected values because changes in common_lookup
update chemical_association
set attachment_bond_type=lcase(attachment_bond_type);

update datum
set name='size', value_type='RMS'
where name='RMS-size';

update datum
set name='size', value_type='Z-average'
where name='Z-average';

update datum
set name=lcase(name)
where name in ('Aspect Ratio', 'Diameter', 'Height', 'Length', 'Surface Area')
or name like ('Width%');

DROP TABLE derived_datum;
DROP TABLE derived_bioassay_data;
-- set datum drop down for assay endpoints

DROP TABLE IF EXISTS composition_temp;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
 
-- End of script

