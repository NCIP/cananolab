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


DROP TABLE IF EXISTS sample_management;
DROP TABLE IF EXISTS sample_container;
DROP TABLE IF EXISTS sample_container_storage;
DROP TABLE IF EXISTS storage;


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Manufacturer');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('PointOfContact','role','Investigator');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;


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
--end of fix

--instrument and technique
CREATE TABLE technique
(
	technique_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	abbreviation VARCHAR(50),
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (technique_pk_id)
) TYPE=InnoDB
;

ALTER TABLE canano.technique
 CHANGE technique_pk_id technique_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into technique (type,abbreviation,created_date,created_by) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering','AFFF-MALLS',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Atomic Force Microscopy','AFM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Capillary Electrophoresis',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Cell Counting',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Coagulation Detection',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Confocal Laser Scanning Microscopy','CLSM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Dynamic Light Scattering','DLS',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Electron Microprobe Analysis','EMPA',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Electrophoretic Light Scattering',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Energy Dispersive Spectroscopy','EDS',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Environmental Transmission Electron Microscopy','ETEM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Flow Cytometry',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Focused Ion Beam - Scanning Electron Microscopy','FIB-SEM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Gas Sorption',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Performance Liquid Chromatography','HPLC',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Resolution Scanning Electron Microscopy','HR-SEM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Resolution Transmission Electron Microscopy','HRTEM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Imaging',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Laser Doppler Velocimetry',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Liquid Chromatography','LC',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Microplate Analysis ',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight','MALDI-TOF',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Particle Quantitation',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Phase Analysis Light Scattering',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Polymerase Chain Reaction',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Powder Diffraction',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Radiation Quantiation',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Refractometry ',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Auger Spectrometry',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Electron Microscopy',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Probe Microscopy ','SPM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Tunneling  Microscopy',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Size Exclusion Chromatography with Multi-Angle Light Scattering','SEC-MALLS',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Spectrophotometry',null,sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Transmission Electron Microscopy','TEM',sysdate(),'DATA_MIGRATION');
INSERT into technique (type,abbreviation,created_date,created_by) values ('X-Ray Photoelectron Spectroscopy',null,sysdate(),'DATA_MIGRATION');

ALTER TABLE canano.technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;


ALTER TABLE canano.instrument
 ADD model_name VARCHAR(200) AFTER manufacturer,
 ADD created_date DATETIME NOT NULL,
 ADD created_by VARCHAR(200) NOT NULL,
 DROP abbreviation;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into common_lookup(attribute,name,value) values ('instrument','Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering','Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Atomic Force Microscopy','Atomic Force Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Capillary Electrophoresis','Capillary Electrophoresis Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Cell Counting','Hemocytometer');
insert into common_lookup(attribute,name,value) values ('instrument','Cell Counting','Coulter Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Dynamic Light Scattering','Dynamic Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Electron Microprobe Analysis','Electron Microprobe');
insert into common_lookup(attribute,name,value) values ('instrument','Electrophoretic Light Scattering','Electrophoretic Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Energy Dispersive Spectroscopy','Energy Dispersive Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Environmental Transmission Electron Microscopy','Environmental Transmission Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Coagulation Detection','Coagulation Monitor');
insert into common_lookup(attribute,name,value) values ('instrument','Gas Sorption','Surface Area and Pore Size Analyzer');
insert into common_lookup(attribute,name,value) values ('instrument','Flow Cytometry','Flow Cytometer');
insert into common_lookup(attribute,name,value) values ('instrument','Focused Ion Beam - Scanning Electron Microscopy','Scanning Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','High Performance Liquid Chromatography','High Performance Liquid Chromatography System');
insert into common_lookup(attribute,name,value) values ('instrument','High Resolution Scanning Electron Microscopy','Scanning Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','High Resolution Transmission Electron Microscopy','Transmission Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Imaging','Imaging System');
insert into common_lookup(attribute,name,value) values ('instrument','Laser Doppler Velocimetry','Laser Doppler Velocimeter');
insert into common_lookup(attribute,name,value) values ('instrument','Liquid Chromatography','Liquid Chromatography System');
insert into common_lookup(attribute,name,value) values ('instrument','Particle Quantitation','Hemocytometer');
insert into common_lookup(attribute,name,value) values ('instrument','Particle Quantitation','Coulter Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Phase Analysis Light Scattering','Phase Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Polymerase Chain Reaction','Thermal Cycler');
insert into common_lookup(attribute,name,value) values ('instrument','Powder Diffraction','Powder Diffractometer');
insert into common_lookup(attribute,name,value) values ('instrument','Radiation Quantiation','Scintillation Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Refractometry','Refractometer');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Auger Spectrometry','Scanning Auger Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Electron Microscopy','Scanning Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Probe Microscopy','Scanning Probe Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Tunneling  Microscopy','Scanning Tunneling  Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Size Exclusion Chromatography with Multi-Angle Light Scattering','Size Exclusion Chromatography with Multi-Angle Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Spectrophotometry','Spectrophotometer');
insert into common_lookup(attribute,name,value) values ('instrument','Transmission Electron Microscopy','Transmission Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','X-Ray Photoelectron Spectroscopy','X-Ray Photoelectron Spectroscope');
insert into common_lookup(attribute,name,value) values ('instrument','Microplate Analysis','Microplate Reader');
insert into common_lookup(attribute,name,value) values ('instrument','Matrix Assisted Laser Desorption Ionisation - Time Of Flight','Matrix Assisted Laser Desorption Ionisation - Time Of Flight Mass Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Confocal Laser Scanning Microscopy','Confocal Microscope');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;

CREATE TABLE experiment_config_instrument
(
	experiment_config_pk_id BIGINT NOT NULL,
	instrument_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_config_pk_id, instrument_pk_id),
	KEY (experiment_config_pk_id),
	KEY (instrument_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_config
(
	experiment_config_id BIGINT NOT NULL,
	description TEXT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	characterization_pk_id BIGINT,
	technique_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_config_id),
	KEY (characterization_pk_id),
	KEY (technique_pk_id)
) TYPE=InnoDB
;

INSERT INTO experiment_config(experiment_config_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT i.instrument_config_pk_id, i.description, i.created_date, i.created_by,
	c.characterization_pk_id,i.instrument_pk_id
FROM canano.instrument_config i, characterization c
WHERE c.instrument_config_pk_id = i.instrument_config_pk_id
;

CREATE TABLE tmp_technique_id_mapping
(
	technique_pk_id BIGINT NOT NULL,
	instrument_pk_id BIGINT NOT NULL
) TYPE=InnoDB
;

INSERT INTO tmp_technique_id_mapping(technique_pk_id, instrument_pk_id)
	SELECT DISTINCT technique.technique_pk_id, instrument.instrument_pk_id
	FROM technique, instrument, common_lookup
	WHERE common_lookup.attribute = 'instrument'
	and instrument.type = common_lookup.name
	and technique.type = common_lookup.name
	and technique_pk_id is not null;


-- update technique_pk_id from fresh insert of technique
UPDATE experiment_config e1, tmp_technique_id_mapping m
SET e1.technique_pk_id = m.technique_pk_id
WHERE m.instrument_pk_id=e1.technique_pk_id
;

-- use created_by as placeholder to store manuafacturer data
UPDATE instrument_config ic
SET ic.created_by = '';

UPDATE instrument_config ic, instrument i
SET ic.created_by = i.manufacturer
WHERE ic.instrument_pk_id = i.instrument_pk_id;


DELETE FROM canano.instrument;

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT AUTO_INCREMENT NOT NULL;
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering Instrument',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Atomic Force Microscope','Molecular Imaging',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Capillary Electrophoresis Instrument',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Hemocytometer',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coulter Counter','Beckman/Coulter',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Brookhaven Instruments',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Malvern',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Molecular Devices',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Wyatt Technologies',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electron Microprobe',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electrophoretic Light Scattering Instrument','Brookhaven Instruments',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electrophoretic Light Scattering Instrument','Malvern',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coagulation Monitor',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Energy Dispersive Spectrometer','EDAX',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Environmental Transmission Electron Microscope','JEOL',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Environmental Transmission Electron Microscope','Philips',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coagulation Monitor','Diagnostica Stago',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Flow Cytometer','Becton Dickinson',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Surface Area and Pore Size Analyzer','Quantachrome Instruments',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope','Hitachi',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope','Brookhaven Instruments',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography System','Agilent',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography System','Shimadzu',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Transmission Electron Microscope',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Imaging System','Kodak',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Laser Doppler Velocimeter',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Liquid Chromatography System','Amersham',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Hemocytometer',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coulter Counter',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Phase Light Scattering Instrument','Brookhaven Instruments',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Phase Light Scattering Instrument','Malvern',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Thermal Cycler','Biorad',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Powder Diffractometer',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scintillation Counter','Beckman/Coulter',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Refractometer','Waters',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Auger Spectrometer',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Probe Microscope',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Tunneling Microscope',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Size Exclusion Chromatography with Multi-Angle Light Scattering Instrument','Wyatt Technologies',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Thermo Electron',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Tecan',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Molecular Devices',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('X-Ray Photoelectron Spectroscope',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Microplate Reader','Biorad',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight Mass Spectrometer','Waters',sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Confocal Microscope','Carl Zeiss',sysdate(),'DATA_MIGRATION');

ALTER TABLE canano.instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;

INSERT INTO experiment_config_instrument(experiment_config_pk_id,instrument_pk_id)
SELECT DISTINCT ec.experiment_config_id, i.instrument_pk_id
FROM instrument i, experiment_config ec, common_lookup cl, technique t, instrument_config ic
WHERE cl.attribute = 'instrument'
AND  t.technique_pk_id = ec.technique_pk_id
AND t.type = cl.name
AND cl.value = i.type
AND ec.experiment_config_id = ic.instrument_config_pk_id
AND i.manufacturer = ic.created_by
;

ALTER TABLE canano.characterization
 DROP FOREIGN KEY FK_characterization_instrument_config
;
ALTER TABLE canano.characterization
 DROP instrument_config_pk_id;


DROP TABLE canano.instrument_config;
DROP TABLE tmp_technique_id_mapping;

ALTER TABLE experiment_config_instrument ADD CONSTRAINT FK_experiment_config_instrument_experiment_config
	FOREIGN KEY (experiment_config_pk_id) REFERENCES experiment_config (experiment_config_id)
;

ALTER TABLE experiment_config_instrument ADD CONSTRAINT FK_experiment_config_instrument_instrument
	FOREIGN KEY (instrument_pk_id) REFERENCES instrument (instrument_pk_id)
;

ALTER TABLE experiment_config ADD CONSTRAINT FK_experiment_config_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE experiment_config ADD CONSTRAINT FK_experiment_config_technique
	FOREIGN KEY (technique_pk_id) REFERENCES technique (technique_pk_id)
;


-- todo: derived datum
CREATE TABLE datum
(
	datum_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	value VARCHAR(200) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	data_set_pk_id BIGINT,
	data_row_pk_id BIGINT,
	characterization_pk_id BIGINT,
	PRIMARY KEY (datum_pk_id),
	KEY (characterization_pk_id),
	KEY (data_row_pk_id)
) TYPE=InnoDB
;

CREATE TABLE datum_condition
(
	datum_pk_id BIGINT NOT NULL,
	condition_pk_id BIGINT NOT NULL,
	PRIMARY KEY (datum_pk_id, condition_pk_id),
	KEY (condition_pk_id),
	KEY (datum_pk_id)
) TYPE=InnoDB
;


CREATE TABLE data_set
(
	data_set_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT,
	PRIMARY KEY (data_set_pk_id),
	UNIQUE (data_set_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE data_row
(
	data_row_pk_id BIGINT NOT NULL,
	PRIMARY KEY (data_row_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_condition
(
	condition_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	property VARCHAR(200),
	value VARCHAR(200) NOT NULL,
	value_unit VARCHAR(200),
	value_type VARCHAR(200),
	PRIMARY KEY (condition_pk_id)
) TYPE=InnoDB
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_data_row
	FOREIGN KEY (data_row_pk_id) REFERENCES data_row (data_row_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_condition
	FOREIGN KEY (condition_pk_id) REFERENCES experiment_condition (condition_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_data_row
	FOREIGN KEY (datum_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE data_set ADD CONSTRAINT FK_data_set_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

INSERT INTO data_set(data_set_pk_id, file_pk_id)
	SELECT derived_bioassay_data_pk_id, file_pk_id
	FROM derived_bioassay_data
;

INSERT INTO data_row(data_row_pk_id)
SELECT datum_pk_id
FROM derived_datum
;

INSERT INTO datum(datum_pk_id, name, value, value_type, value_unit,
	description, created_by, created_date, data_set_pk_id, data_row_pk_id,
	characterization_pk_id)
	SELECT dd.datum_pk_id, dd.datum_name, dd.value, dd.value_type,
		dd.value_unit, dd.description, dd.created_by, dd.created_date,
		dd.derived_bioassay_data_pk_id, dd.datum_pk_id,
		b.characterization_pk_id
	FROM derived_datum dd, derived_bioassay_data b
	WHERE dd.derived_bioassay_data_pk_id = b.derived_bioassay_data_pk_id
;

ALTER TABLE characterization ADD COLUMN poc_pk_id BIGINT;

UPDATE characterization c, point_of_contact p, organization o
SET c.poc_pk_id = p.poc_pk_id
WHERE p.organization_pk_id = o.organization_pk_id
AND o.name = c.source
;

ALTER TABLE characterization change description design_method_description TEXT;

ALTER TABLE characterization ADD COLUMN
analysis_conclusion TEXT,
other_char_assay_category VARCHAR(200),
other_char_name VARCHAR(200),
assay_type VARCHAR(200);

--ALTER TABLE characterization DROP COLUMN source;
--DROP TABLE derived_datum;
--DROP TABLE derived_bioassay_data;

DROP TABLE associated_file;


-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script

