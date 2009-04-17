--instrument and technique
ALTER TABLE common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT AUTO_INCREMENT NOT NULL;

insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Atomic Force Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Capillary Electrophoresis');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Cell Counting');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Coagulation Detection');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Confocal Laser Scanning Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Colony Counting');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Dynamic Light Scattering');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Electron Microprobe Analysis');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Electrophoretic Light Scattering');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Energy Dispersive Spectroscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Environmental Transmission Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Fast Protein Liquid Chromatography');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Flow Cytometry');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Focused Ion Beam - Scanning Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Gas Sorption');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Gel Filtration Chromatography');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'High Performance Liquid Chromatography');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'High Resolution Scanning Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'High Resolution Transmission Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Imaging');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Mass Quantitation');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Matrix Assisted Laser Desorption Ionisation - Time Of Flight');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Microplate Analysis');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Particle Quantitation');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Polymerase Chain Reaction');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Powder Diffraction');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Radiation Quantiation');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Refractometry');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Scanning Auger Spectrometry');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Scanning Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Scanning Probe Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Scanning Tunneling  Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Size Exclusion Chromatography with Multi-Angle Laser Light Scattering');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Spectrophotometry');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Transmission Electron Microscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'X-Ray Photoelectron Spectroscopy');
insert into common_lookup (name, attribute, value) values ('Technique', 'type', 'Zeta Potential Analysis');

insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','abbreviation', 'AFFF-MALLS');
insert into common_lookup (name, attribute, value) values ('Atomic Force Microscopy','abbreviation','AFM');
insert into common_lookup (name, attribute, value) values ('Confocal Laser Scanning Microscopy','abbreviation','CLSM');
insert into common_lookup (name, attribute, value) values ('Dynamic Light Scattering','abbreviation','DLS');
insert into common_lookup (name, attribute, value) values ('Electron Microprobe Analysis','abbreviation','EMPA');
insert into common_lookup (name, attribute, value) values ('Energy Dispersive Spectroscopy','abbreviation','EDS');
insert into common_lookup (name, attribute, value) values ('Environmental Transmission Electron Microscopy','abbreviation','ETEM');
insert into common_lookup (name, attribute, value) values ('Focused Ion Beam - Scanning Electron Microscopy','abbreviation','FIB-SEM');
insert into common_lookup (name, attribute, value) values ('High Performance Liquid Chromatography','abbreviation','HPLC');
insert into common_lookup (name, attribute, value) values ('High Resolution Scanning Electron Microscopy','abbreviation','HR-SEM');
insert into common_lookup (name, attribute, value) values ('High Resolution Transmission Electron Microscopy','abbreviation','HRTEM');
insert into common_lookup (name, attribute, value) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight','abbreviation','MALDI-TOF');
insert into common_lookup (name, attribute, value) values ('Polymerase Chain Reaction','abbreviation','PCR');
insert into common_lookup (name, attribute, value) values ('Scanning Auger Spectrometry','abbreviation','SAM');
insert into common_lookup (name, attribute, value) values ('Scanning Electron Microscopy','abbreviation','SEM');
insert into common_lookup (name, attribute, value) values ('Scanning Probe Microscopy ','abbreviation','SPM');
insert into common_lookup (name, attribute, value) values ('Scanning Tunneling  Microscopy','abbreviation','STM');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','abbreviation','SEC-MALLS');
insert into common_lookup (name, attribute, value) values ('Transmission Electron Microscopy','abbreviation','TEM');
insert into common_lookup (name, attribute, value) values ('X-Ray Photoelectron Spectroscopy','abbreviation','XPS');
insert into common_lookup (name, attribute, value) values ('Gel Filtration Chromatography','abbreviation','GFC');
insert into common_lookup (name, attribute, value) values ('Fast Protein Liquid Chromatography','abbreviation','FPLC');

insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','instrument','Photometer');
insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','instrument','Refractometer');
insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','instrument','Separation Column');
insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','instrument','Spectrophotometer');
insert into common_lookup (name, attribute, value) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Laser Light Scattering','instrument','Control Module');
insert into common_lookup (name, attribute, value) values ('Atomic Force Microscopy','instrument','Atomic Force Microscope');
insert into common_lookup (name, attribute, value) values ('Capillary Electrophoresis','instrument','Capillary Electrophoresis Instrument');
insert into common_lookup (name, attribute, value) values ('Cell Counting','instrument','Hemocytometer');
insert into common_lookup (name, attribute, value) values ('Cell Counting','instrument','Coulter Counter');
insert into common_lookup (name, attribute, value) values ('Colony Counting','instrument','Automated Colony Counter');
insert into common_lookup (name, attribute, value) values ('Dynamic Light Scattering','instrument','Dynamic Light Scattering Instrument');
insert into common_lookup (name, attribute, value) values ('Electron Microprobe Analysis','instrument','Electron Microprobe');
insert into common_lookup (name, attribute, value) values ('Electrophoretic Light Scattering','instrument','Electrophoretic Light Scattering Instrument');
insert into common_lookup (name, attribute, value) values ('Energy Dispersive Spectroscopy','instrument','Energy Dispersive Spectrometer');
insert into common_lookup (name, attribute, value) values ('Environmental Transmission Electron Microscopy','instrument','Environmental Transmission Electron Microscope');
insert into common_lookup (name, attribute, value) values ('Coagulation Detection','instrument','Coagulation Monitor');
insert into common_lookup (name, attribute, value) values ('Gas Sorption','instrument','Gas Sorption Detector');
insert into common_lookup (name, attribute, value) values ('Flow Cytometry','instrument','Flow Cytometer');
insert into common_lookup (name, attribute, value) values ('Focused Ion Beam - Scanning Electron Microscopy','instrument','Scanning Electron Microscope');
insert into common_lookup (name, attribute, value) values ('High Performance Liquid Chromatography','instrument','Separation Column');
insert into common_lookup (name, attribute, value) values ('High Performance Liquid Chromatography','instrument','Spectrophotometer');
insert into common_lookup (name, attribute, value) values ('High Performance Liquid Chromatography','instrument','Control Module');
insert into common_lookup (name, attribute, value) values ('High Performance Liquid Chromatography','instrument','Fraction Collector');
insert into common_lookup (name, attribute, value) values ('High Resolution Scanning Electron Microscopy','instrument','Scanning Electron Microscope');
insert into common_lookup (name, attribute, value) values ('High Resolution Transmission Electron Microscopy','instrument','Transmission Electron Microscope');
insert into common_lookup (name, attribute, value) values ('Imaging','instrument','Imaging System');
insert into common_lookup (name, attribute, value) values ('Particle Quantitation','instrument','Hemocytometer');
insert into common_lookup (name, attribute, value) values ('Particle Quantitation','instrument','Coulter Counter');
insert into common_lookup (name, attribute, value) values ('Polymerase Chain Reaction','instrument','Thermal Cycler');
insert into common_lookup (name, attribute, value) values ('Powder Diffraction','instrument','Powder Diffractometer');
insert into common_lookup (name, attribute, value) values ('Radiation Quantiation','instrument','Scintillation Counter');
insert into common_lookup (name, attribute, value) values ('Refractometry','instrument','Refractometer');
insert into common_lookup (name, attribute, value) values ('Scanning Auger Spectrometry','instrument','Scanning Auger Spectrometer');
insert into common_lookup (name, attribute, value) values ('Scanning Electron Microscopy','instrument','Scanning Electron Microscope');
insert into common_lookup (name, attribute, value) values ('Scanning Probe Microscopy','instrument','Scanning Probe Microscope');
insert into common_lookup (name, attribute, value) values ('Scanning Tunneling  Microscopy','instrument','Scanning Tunneling Microscope');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','instrument','Photometer');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','instrument','Refractometer');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','instrument','Separation Column');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','instrument','Spectrophotometer');
insert into common_lookup (name, attribute, value) values ('Size Exclusion Chromatography with Multi-Angle Laser Light Scattering','instrument','Control Module');
insert into common_lookup (name, attribute, value) values ('Spectrophotometry','instrument','Spectrophotometer');
insert into common_lookup (name, attribute, value) values ('Transmission Electron Microscopy','instrument','Transmission Electron Microscope');
insert into common_lookup (name, attribute, value) values ('X-Ray Photoelectron Spectroscopy','instrument','X-Ray Photoelectron Spectrometer');
insert into common_lookup (name, attribute, value) values ('Microplate Analysis','instrument','Microplate Reader');
insert into common_lookup (name, attribute, value) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight','instrument','MALDI-TOF Mass Spectrometer');
insert into common_lookup (name, attribute, value) values ('Confocal Laser Scanning Microscopy','instrument','Confocal Microscope System');
insert into common_lookup (name, attribute, value) values ('Zeta Potential Analysis','instrument','Zeta Potential Analyzer');
insert into common_lookup (name, attribute, value) values ('Fast Protein Liquid Chromatography','instrument','Separation Column');
insert into common_lookup (name, attribute, value) values ('Fast Protein Liquid Chromatography','instrument','Spectrophotometer');
insert into common_lookup (name, attribute, value) values ('Fast Protein Liquid Chromatography','instrument','Control Module');
insert into common_lookup (name, attribute, value) values ('Fast Protein Liquid Chromatography','instrument','Fraction Collector');
insert into common_lookup (name, attribute, value) values ('Mass Quantitation','instrument','Analytical Balance');
insert into common_lookup (name, attribute, value) values ('Gel Filtration Chromatography','instrument','Gel Filtration Column');

insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Agilent');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Amersham');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Beckman/Coulter');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Becton Dickinson');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'BioLogics');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Biorad');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'BioTek');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Brookhaven Instruments');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Carl Zeiss');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Diagnostica Stago');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'EDAX');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Hitachi');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'JEOL');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Kodak');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Malvern');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Micromeritics');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Molecular Devices');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Molecular Imaging');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Philips');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Quantachrome Instruments');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Shimadzu');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Tecan');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Thermo Electron');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Waters');
insert into common_lookup (name, attribute, value) values ('Instrument', 'manufacturer', 'Wyatt Technologies');

ALTER TABLE common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

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
	experiment_config_pk_id BIGINT NOT NULL,
	description TEXT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	characterization_pk_id BIGINT,
	technique_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_config_pk_id),
	KEY (characterization_pk_id),
	KEY (technique_pk_id)
) TYPE=InnoDB
;

-- delete instruments that are not used
DELETE FROM instrument
WHERE NOT EXISTS
  ( SELECT b.instrument_pk_id
     FROM instrument_config b, characterization c
     WHERE instrument.instrument_pk_id=b.instrument_pk_id
     and b.instrument_config_pk_id=c.instrument_config_pk_id)
;

-- update to make sure no spelling differences
update instrument
set type='Size Exclusion Chromatography with Multi-Angle Laser Light Scattering'
where type='Size-Exclusion Chromatography and Multiangle Laser Light Scattering';

update instrument
set type='Dynamic Light Scattering'
where type='Zetasizer';

update instrument
set type='Zeta Potential Analyzer'
where type='Phase Analysis Light Scattering';

update instrument
set type='Gas Sorption Detector'
where type='Surface Area and Pore Size Analyzer';

-- create a temp table to hold instruments that can't be migrated
CREATE TABLE instrument_to_review
(
	instrument_pk_id BIGINT NOT NULL,
	instrument_config_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NOT NULL,
	instrument_type VARCHAR(200),
	description TEXT
) TYPE=InnoDB
;

-- create a temp table to hold techniques that can't be migrated
CREATE TABLE technique_to_review
(
	instrument_pk_id BIGINT NOT NULL,
	instrument_config_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NOT NULL,
	instrument_type VARCHAR(200),
	description TEXT
) TYPE=InnoDB
;


-- move instruments that map to mulitple techniques to the review table
-- these need manual curation to insert into the 1.5 technique table
insert into instrument_to_review
(instrument_pk_id, instrument_config_pk_id, characterization_pk_id, instrument_type, description)
select i.instrument_pk_id,  ic.instrument_config_pk_id, c.characterization_pk_id,i.type, ic.description
from instrument i, common_lookup cl, instrument_config ic, characterization c
where i.type=cl.value
and i.instrument_pk_id=ic.instrument_pk_id
and ic.instrument_config_pk_id=c.instrument_config_pk_id
and cl.attribute='instrument'
group by i.instrument_pk_id, i.type, ic.instrument_config_pk_id, ic.description, c.characterization_pk_id
having count(distinct cl.name)>1;

-- move instruments that are techniques having more than one instruments to the review table
-- these need manual curation to insert to the 1.5 instrument table
insert into technique_to_review
(instrument_pk_id, instrument_config_pk_id, characterization_pk_id, instrument_type, description)
select i.instrument_pk_id,  ic.instrument_config_pk_id, c.characterization_pk_id,i.type, ic.description
from instrument i, common_lookup cl, instrument_config ic, characterization c
where cl.attribute='instrument'
and i.type=cl.name
and i.instrument_pk_id=ic.instrument_pk_id
and ic.instrument_config_pk_id=c.instrument_config_pk_id
group by i.instrument_pk_id, i.type, ic.instrument_config_pk_id, ic.description, c.characterization_pk_id
having count(distinct cl.value)>1;

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT AUTO_INCREMENT NOT NULL;

-- insert into techniques based on technique to instrument mapping
INSERT into technique (type,created_date,created_by)
select distinct c.name, sysdate(), 'DATA_MIGRATION'
from
(select a.name
from common_lookup a, instrument b, instrument_to_review c
where a.attribute='instrument'
 and a.value=b.type
 and b.instrument_pk_id!=c.instrument_pk_id
 union
 select a.name
 from common_lookup a, instrument b
 where a.attribute='instrument'
 and a.name=b.type) c
 ;

-- update technique abbreviation
update technique t, common_lookup cl
set t.abbreviation=cl.value
where cl.attribute='abbreviation'
and cl.name=t.type;

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

ALTER TABLE instrument
 ADD model_name VARCHAR(200) AFTER manufacturer,
 ADD created_date DATETIME NOT NULL,
 ADD created_by VARCHAR(200) NOT NULL,
 DROP abbreviation;

-- update model_name with description in instrument_config
UPDATE instrument a, instrument_config b
set a.model_name=substr(b.description, 1, 200)
where a.instrument_pk_id=b.instrument_pk_id;

-- update manufacturer spelling
update instrument
set manufacturer='Diagnostica Stago'
where manufacturer='Diagnostica';

-- insert into experiment_config using matching from technique to instrument
INSERT INTO experiment_config(experiment_config_pk_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT distinct ic.instrument_config_pk_id, ic.description, ic.created_date, ic.created_by,
	c.characterization_pk_id,t.technique_pk_id
FROM instrument_config ic, characterization c, instrument i, common_lookup cl, technique t, instrument_to_review ir
WHERE c.instrument_config_pk_id = ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
and cl.attribute='instrument'
and cl.value=i.type
and cl.name=t.type
and i.instrument_pk_id !=ir.instrument_pk_id
;

-- insert into experiment_config using matching of instrument type to technique type
INSERT INTO experiment_config(experiment_config_pk_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT ic.instrument_config_pk_id, ic.description, ic.created_date, ic.created_by,
	c.characterization_pk_id,t.technique_pk_id
FROM instrument_config ic, characterization c, instrument i,  technique t
WHERE c.instrument_config_pk_id = ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
and i.type=t.type
;

-- update instrument type with mapping from technique where there is a one to one mapping between technique and instrument
update instrument i, common_lookup cl, technique_to_review tr
set i.type=cl.value
where cl.attribute='instrument'
and i.type=cl.name
and i.type!=tr.instrument_type;

-- delete the technique that needs to be reviewed
delete from instrument
where instrument_pk_id in
(select instrument_pk_id
from technique_to_review);

delete from  instrument_config
where instrument_pk_id in
(select instrument_pk_id
from technique_to_review);

INSERT INTO experiment_config_instrument(experiment_config_pk_id, instrument_pk_id)
SELECT ic.instrument_config_pk_id, ic.instrument_pk_id
FROM instrument_config ic, characterization c, instrument i
where c.instrument_config_pk_id=ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
;

ALTER TABLE characterization
 DROP FOREIGN KEY FK_characterization_instrument_config
;
ALTER TABLE characterization
 DROP instrument_config_pk_id;

DROP TABLE instrument_config;

update instrument
set created_date=sysdate(),
created_by='DATA_MIGRATION';

ALTER TABLE experiment_config_instrument ADD CONSTRAINT FK_experiment_config_instrument_experiment_config
	FOREIGN KEY (experiment_config_pk_id) REFERENCES experiment_config (experiment_config_pk_id)
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

