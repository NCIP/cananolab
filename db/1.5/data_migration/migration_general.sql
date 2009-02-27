USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

--migration to csm 4.1
source csm/migration_to_csm_4.1.sql

--drop obsolete tables
DROP TABLE IF EXISTS sample_management;
DROP TABLE IF EXISTS sample_container;
DROP TABLE IF EXISTS sample_container_storage;
DROP TABLE IF EXISTS storage;
DROP TABLE IF EXISTS associated_file;

ALTER TABLE publication ADD COLUMN abstract TEXT;
ALTER TABLE protocol ADD COLUMN protocol_abbreviation VARCHAR(200);

--update protocol_abbreviation to be the same as protocol name
update protocol
set protocol_abbreviation=protocol_name;

update common_lookup
set value='physico-chemical assay'
where name='Protocol' and attribute='type'
and value='physical assay';

--change lab_file to file
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

ALTER TABLE nanoparticle_entity_lab_file RENAME nanoparticle_entity_file;
ALTER TABLE nanoparticle_entity_file
 DROP FOREIGN KEY FK_nanoparticle_entity_lab_file_lab_file;
ALTER TABLE nanoparticle_entity_file
 DROP FOREIGN KEY FK_nanoparticle_entity_lab_file_nanoparticle_entity;
ALTER TABLE nanoparticle_entity_file ADD CONSTRAINT FK_nanoparticle_entity_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id);
ALTER TABLE nanoparticle_entity_file ADD CONSTRAINT FK_nanoparticle_entity_file_nanoparticle_entity
	FOREIGN KEY (nanoparticle_entity_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE keyword_lab_file RENAME keyword_file;
ALTER TABLE canano.keyword_file
 DROP FOREIGN KEY FK_keyword_lab_file_keyword,
 DROP FOREIGN KEY FK_keyword_lab_file_lab_file,
 CHANGE lab_file_pk_id file_pk_id BIGINT(20) NOT NULL;
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

ALTER TABLE nanoparticle_sample_publication
	DROP FOREIGN KEY FK_nanoparticle_sample_publication_publication,
	DROP FOREIGN KEY FK_nanoparticle_sample_publication_nanoparticle_sample
;

ALTER TABLE nanoparticle_sample_publication
	DROP INDEX particle_sample_pk_id,
	DROP INDEX file_pk_id
;

ALTER TABLE nanoparticle_sample_publication
	CHANGE file_pk_id publication_pk_id BIGINT NOT NULL
;

delete
from nanoparticle_sample_publication
where particle_sample_pk_id not in
(select particle_sample_pk_id from nanoparticle_sample);

ALTER TABLE nanoparticle_sample_publication
   ADD PRIMARY KEY (particle_sample_pk_id, publication_pk_id),
   ADD CONSTRAINT FK_nanoparticle_sample_publication_nanoparticle_sample FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id),
   ADD CONSTRAINT FK_nanoparticle_sample_publication FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;
--end of lab_file to file

-- fix, some records exist in csm_protection_group not in csm_protection_element
INSERT INTO canano.csm_protection_element (
	protection_element_name,
	object_id,
	application_id,
	update_date
)SELECT
	protection_group_name,
	protection_group_name,
	application_id,
	sysdate()
FROM canano.csm_protection_group
where protection_group_name not in (
select protection_element_name from csm_protection_element
)
;
INSERT INTO canano.csm_pg_pe (
	protection_group_id,
	protection_element_id,
	update_date
)SELECT
	g.protection_group_id,
	e.protection_element_id,
	sysdate()
FROM canano.csm_protection_group g, canano.csm_protection_element e
where  e.update_date = CURRENT_DATE()
and g.protection_group_name = e.protection_element_name
;
--end of csm fix

--source enhancement
source poc_migration.sql

--instrument and technique
source instrument_migration.sql

-- datum and condition
source datum_migration.sql

--characterization
source characterization_migration.sql

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script

