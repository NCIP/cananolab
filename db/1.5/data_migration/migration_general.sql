USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

ALTER TABLE lab_file RENAME file;

CREATE TABLE organization
(
	organization_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	streetAddress1 VARCHAR(200),
	streetAddress2 VARCHAR(200),
	city VARCHAR(100),
	state VARCHAR(100),
	postal_code VARCHAR(10),
	country VARCHAR(100),
	created_date DATE NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (organization_pk_id),
	UNIQUE (name),
	UNIQUE (organization_pk_id)
) TYPE=InnoDB
;
INSERT INTO organization (organization_pk_id, name, streetAddress1, city, state, country, postal_code,created_date, created_by)
SELECT DISTINCT
	source.source_pk_id,
	organization_name,
	address,
	city,
	state,
	country,
	postal_code,
	min(nanoparticle_sample.created_date),
	nanoparticle_sample.created_by
FROM source, nanoparticle_sample
where nanoparticle_sample.source_pk_id = source.source_pk_id
group by nanoparticle_sample.source_pk_id;


CREATE TABLE point_of_contact
(
	poc_pk_id BIGINT NOT NULL,
	role VARCHAR(200),
	first_name VARCHAR(200),
	last_name VARCHAR(200),
	middle_initial VARCHAR(50),
	phone VARCHAR(50),
	email VARCHAR(200),
	created_date DATE NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	organization_pk_id BIGINT,
	PRIMARY KEY (poc_pk_id),
	KEY (organization_pk_id)
) TYPE=InnoDB
;
ALTER TABLE point_of_contact ADD CONSTRAINT FK_point_of_contact_organization 
	FOREIGN KEY (organization_pk_id) REFERENCES organization (organization_pk_id)
;

INSERT INTO point_of_contact (poc_pk_id, organization_pk_id, created_date, created_by)
SELECT DISTINCT
	source.source_pk_id,
	source.source_pk_id,
	min(nanoparticle_sample.created_date),
	nanoparticle_sample.created_by
FROM source, nanoparticle_sample
where nanoparticle_sample.source_pk_id = source.source_pk_id
group by nanoparticle_sample.source_pk_id;

DROP TABLE IF EXISTS source;


CREATE TABLE nanoparticle_sample_other_poc
(
	particle_sample_pk_id BIGINT NOT NULL,
	poc_pk_id BIGINT NOT NULL,
	PRIMARY KEY (particle_sample_pk_id, poc_pk_id),
	KEY (particle_sample_pk_id),
	KEY (poc_pk_id)
) TYPE=InnoDB
;
ALTER TABLE nanoparticle_sample_other_poc ADD CONSTRAINT FK_nanoparticle_sample_other_poc_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;
ALTER TABLE nanoparticle_sample_other_poc ADD CONSTRAINT FK_nanoparticle_sample_other_poc_point_of_contact
	FOREIGN KEY (poc_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE publication ADD COLUMN abstract TEXT;

ALTER TABLE nanoparticle_sample
	DROP FOREIGN KEY FK_nanoparticle_sample_source; 
ALTER TABLE nanoparticle_sample
	DROP KEY source_pk_id; 
ALTER TABLE nanoparticle_sample 
	CHANGE source_pk_id primary_contact_pk_id BIGINT;
ALTER TABLE nanoparticle_sample ADD CONSTRAINT FK_nanoparticle_sample_point_of_contact
	FOREIGN KEY (primary_contact_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

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
	DROP INDEX particle_sample_pk_id,
	DROP INDEX file_pk_id
;

ALTER TABLE nanoparticle_sample_publication
	CHANGE file_pk_id publication_pk_id BIGINT NOT NULL
;

ALTER TABLE nanoparticle_sample_publication
    ADD PRIMARY KEY (particle_sample_pk_id, publication_pk_id),
	ADD CONSTRAINT FK_nanoparticle_sample_publication_publication FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;


DROP TABLE IF EXISTS sample_management;
DROP TABLE IF EXISTS sample_container;
DROP TABLE IF EXISTS sample_container_storage;
DROP TABLE IF EXISTS storage;
  
-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Manufacturer');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Investigator');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;

 
-- End of script

