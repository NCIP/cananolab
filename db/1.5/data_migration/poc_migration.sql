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

CREATE TABLE sample_other_poc
(
	sample_pk_id BIGINT NOT NULL,
	poc_pk_id BIGINT NOT NULL,
	PRIMARY KEY (sample_pk_id, poc_pk_id),
	KEY (sample_pk_id),
	KEY (poc_pk_id)
) TYPE=InnoDB
;
ALTER TABLE sample_other_poc ADD CONSTRAINT FK_sample_other_poc_sample
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;
ALTER TABLE sample_other_poc ADD CONSTRAINT FK_sample_other_poc_point_of_contact
	FOREIGN KEY (poc_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE sample ADD CONSTRAINT FK_sample_point_of_contact
	FOREIGN KEY (primary_contact_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE characterization ADD COLUMN poc_pk_id BIGINT;

UPDATE characterization c, point_of_contact p, organization o
SET c.poc_pk_id = p.poc_pk_id
WHERE p.organization_pk_id = o.organization_pk_id
AND o.name = c.source
;

ALTER TABLE characterization DROP COLUMN source;

DROP TABLE  nanoparticle_sample;

--update POC  to be public
INSERT into csm_protection_element (
	protection_element_name,
	object_id,
	application_id,
	update_date
)
SELECT
	distinct poc_pk_id,
	poc_pk_id,
	'2',
	sysdate()
FROM point_of_contact
where poc_pk_id not in
(select protection_element_name
from csm_protection_element);

INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct poc_pk_id,
	'2',
	'0',
	sysdate()
FROM point_of_contact
where poc_pk_id not in
(select protection_group_name
from csm_protection_group);

INSERT INTO csm_pg_pe
   (protection_group_id,
   protection_element_id,
   update_date)
SELECT
	distinct pg.protection_group_id,
	pe.protection_element_id,
	sysdate()
FROM point_of_contact, csm_protection_group pg, csm_protection_element pe
where poc_pk_id=pg.protection_group_name
and poc_pk_id=pe.protection_element_name
and poc_pk_id=pe.object_id
and not exists
(select c.protection_group_id, c.protection_element_id
from csm_pg_pe c
where pg.protection_group_id=c.protection_group_id
and pe.protection_element_id=c.protection_element_id)
;

INSERT into csm_user_group_role_pg (
	group_id,
	role_id,
	protection_group_id,
	update_date
)
SELECT
	g.group_id,
	cr.role_id,
	pg.protection_group_id,
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_role cr,
	point_of_contact poc
WHERE pg.protection_group_name = poc.poc_pk_id
AND g.group_name = 'Public'
AND cr.role_name = 'R'
;