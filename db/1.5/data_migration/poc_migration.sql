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
ALTER TABLE nanoparticle_sample
	DROP FOREIGN KEY FK_nanoparticle_sample_source;
ALTER TABLE nanoparticle_sample
	DROP KEY source_pk_id;
ALTER TABLE nanoparticle_sample
	CHANGE source_pk_id primary_contact_pk_id BIGINT;
ALTER TABLE nanoparticle_sample ADD CONSTRAINT FK_nanoparticle_sample_point_of_contact
	FOREIGN KEY (primary_contact_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE characterization ADD COLUMN poc_pk_id BIGINT;

UPDATE characterization c, point_of_contact p, organization o
SET c.poc_pk_id = p.poc_pk_id
WHERE p.organization_pk_id = o.organization_pk_id
AND o.name = c.source
;

ALTER TABLE characterization DROP COLUMN source;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Manufacturer');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Investigator');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;