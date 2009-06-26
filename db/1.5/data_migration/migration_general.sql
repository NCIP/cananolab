USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

-- migration to csm 4.1
source csm/migration_to_csm_4.1.sql

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

update common_lookup
set value='physico-chemical assay'
where name='Protocol' and attribute='type'
and value='physical assay';

-- change lab_file to file
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

update common_lookup
set value='proceeding'
where value='in proceedings';

-- fix, some records exist in csm_protection_group not in csm_protection_element
INSERT INTO csm_protection_element (
	protection_element_name,
	object_id,
	application_id,
	update_date
)SELECT
	protection_group_name,
	protection_group_name,
	application_id,
	sysdate()
FROM csm_protection_group
where protection_group_name not in (
select protection_element_name from csm_protection_element
)
;
INSERT INTO csm_pg_pe (
	protection_group_id,
	protection_element_id,
	update_date
)SELECT
	g.protection_group_id,
	e.protection_element_id,
	sysdate()
FROM csm_protection_group g, csm_protection_element e
where  e.update_date = CURRENT_DATE()
and g.protection_group_name = e.protection_element_name
;
-- end of csm fix

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


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT AUTO_INCREMENT NOT NULL;
INSERT INTO common_lookup(name,attribute,value) values ('Entrapment','displayName','entrapment');
INSERT INTO common_lookup(name,attribute,value) values('dimension', 'unit', ' nm');
DELETE FROM common_lookup
where name='SampleContainer';

DELETE FROM common_lookup
where name='Cytotoxicity'
and attribute='cellLine';

update common_lookup
set name='File'
where name='LabFile';

update common_lookup
set value='2D-rectangle'
where value='2D-retangle';

update common_lookup
set name='NanomaterialEntity', value='Nanomaterial Entity'
where name='NanoparticleEntity'
and attribute='displayName';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

DROP TABLE IF EXISTS composition_temp;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script

