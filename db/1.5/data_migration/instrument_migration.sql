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

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into technique (type,abbreviation,created_date,created_by) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering','AFFF-MALS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Atomic Force Microscopy','AFM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Capillary Electrophoresis',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Cell Counting',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Coagulation Detection',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Confocal Laser Scanning Microscopy','CLSM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Dynamic Light Scattering','DLS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Electron Microprobe Analysis','EMPA',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Electrophoretic Light Scattering',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Energy Dispersive Spectroscopy','EDS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Environmental Transmission Electron Microscopy','ETEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Flow Cytometry',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Focused Ion Beam - Scanning Electron Microscopy','FIB-SEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Gas Sorption',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Performance Liquid Chromatography','HPLC',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Resolution Scanning Electron Microscopy','HR-SEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('High Resolution Transmission Electron Microscopy','HRTEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Imaging',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Laser Doppler Velocimetry','LDV',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Liquid Chromatography','LC',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Microplate Analysis ',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight','MALDI-TOF',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Particle Quantitation',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Phase Analysis Light Scattering','PALS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Polymerase Chain Reaction','PCR',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Powder Diffraction',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Radiation Quantiation',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Refractometry ',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Auger Spectrometry','SAM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Electron Microscopy','SEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Probe Microscopy ','SPM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Tunneling  Microscopy','STM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Size-Exclusion Chromatography with Multi-Angle Light Scattering','SEC-MALS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Spectrophotometry',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Transmission Electron Microscopy','TEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('X-Ray Photoelectron Spectroscopy','XPS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Colony Counting',null,sysdate(),'SEED_DATA');

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

ALTER TABLE instrument
 ADD model_name VARCHAR(200) AFTER manufacturer,
 ADD created_date DATETIME NOT NULL,
 ADD created_by VARCHAR(200) NOT NULL,
 DROP abbreviation;

--update model_name with description in instrument_config
UPDATE instrument a, instrument_config b
set a.model_name=substr(b.description, 1, 200)
where a.instrument_pk_id=b.instrument_pk_id;

--update scopy with scope
UPDATE instrument
set type=replace(type, 'scopy', 'scope');

update instrument
set type=replace(type, 'Chromatography and Multiangle', 'Chromatography with Multiangle');

update instrument
set type='Coagulation Monitor'
where type='Clot Detection System';

update instrument
set type=replace(type, 'Scattering', 'Scattering Instrument');

update instrument
set type='Dynamic Light Scattering Instrument'
where type='Zetasizer';

update instrument
set type=replace(type, 'Liquid Chromatography', 'Liquid Chromatography Instrument');

update instrument
set type=replace(type, 'Time Of Flight', 'Time Of Flight Instrument');

update instrument
set type='Confocal Microscope'
where type='Confocal Laser Scanning Microscope';

update instrument
set type='SEC-MALS Instrument'
where type='Size-Exclusion Chromatography with Multiangle Laser Light Scattering Instrument';

update instrument
set type='MALDI-TOF Mass Spectrometer'
where type='Matrix Assisted Laser Desorption Ionisation - Time Of Flight Instrument';

update instrument
set type='Energy Dispersive Spectrometer'
where type='Energy Dispersive Spectroscope';

update instrument
set model_name=null
where model_name='Model S-4700'
and manufacturer='Brookhaven Instruments';

update instrument
set manufacturer='Diagnostica Stago'
where manufacturer='Diagnostica';

update instrument
set created_date=sysdate();

--update model name
update instrument
set model_name='Synergy HT'
where type='Microplate Reader'
and manufacturer='BioTek'
and model_name like '%Synergy HT%';

update instrument
set model_name='AcuCount1000'
where type='Automated Colony Counter'
and manufacturer='BioLogics'
and model_name like '%AcuCount1000%';

update instrument
set model_name='ZetaPALS'
where type='Dynamic Light Scattering Instrument'
and manufacturer='Brookhaven Instruments'
and model_name like '%ZetaPALS%';

update instrument
set model_name='JEM-1200 EX'
where type='Transmission Electron Microscope'
and manufacturer='JEOL'
and model_name like '%JEM-1200 EX%';

update instrument
set model_name='ZetaPALS'
where type='Dynamic'
and manufacturer='Brookhaven'
and model_name like '%ZetaPALS%';

update instrument
set model_name='Elzone'
where type='Coulter Counter'
and manufacturer='Micromeritics'
and model_name like '%Elzone%';

update instrument
set model_name='Micromass TofSpec-2E'
where type='MALDI-TOF Mass Spectrometer'
and manufacturer='Waters'
and model_name like '%Micromass TofSpec-2E%';

update instrument
set model_name='CM100'
where type='Transmission Electron Microscope'
and manufacturer='Philips'
and model_name like '%CM 100%';

update instrument
set model_name=null
where type='SEC-MALS Instrument'
and manufacturer='Wyatt Technologies'
and model_name like '%DAWN EOS%';

update instrument
set model_name=null
where type='SEC-MALS Instrument'
and manufacturer='Waters'
and model_name like '%Alliance 2690%';

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into instrument (type,manufacturer,model_name, created_date,created_by) values ('AFFF-MALS Instrument',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Capillary Electrophoresis Instrument',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Hemocytometer',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Electron Microprobe',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Environmental Transmission Electron Microscope','JEOL',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Environmental Transmission Electron Microscope','Philips',null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Laser Doppler Velocimeter',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Powder Diffractometer',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Scanning Auger Spectrometer',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Scanning Probe Microscope',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('Scanning Tunneling Microscope',null,null,sysdate(),'DATA_MIGRATION');
INSERT into instrument (type,manufacturer,model_name,created_date,created_by) values ('X-Ray Photoelectron Spectrometer',null,null,sysdate(),'DATA_MIGRATION');

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;

ALTER TABLE common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT AUTO_INCREMENT NOT NULL;

insert into common_lookup(attribute,name,value) values ('instrument','Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering','AFFF-MALS Instrument');
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
insert into common_lookup(attribute,name,value) values ('instrument','High Performance Liquid Chromatography','High Performance Liquid Chromatography Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','High Resolution Scanning Electron Microscopy','Scanning Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','High Resolution Transmission Electron Microscopy','Transmission Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Imaging','Imaging System');
insert into common_lookup(attribute,name,value) values ('instrument','Laser Doppler Velocimetry','Laser Doppler Velocimeter');
insert into common_lookup(attribute,name,value) values ('instrument','Liquid Chromatography','Liquid Chromatography Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Particle Quantitation','Hemocytometer');
insert into common_lookup(attribute,name,value) values ('instrument','Particle Quantitation','Coulter Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Phase Analysis Light Scattering','Phase Analysis Light Scattering Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Polymerase Chain Reaction','Thermal Cycler');
insert into common_lookup(attribute,name,value) values ('instrument','Powder Diffraction','Powder Diffractometer');
insert into common_lookup(attribute,name,value) values ('instrument','Radiation Quantiation','Scintillation Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Refractometry','Refractometer');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Auger Spectrometry','Scanning Auger Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Electron Microscopy','Scanning Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Probe Microscopy','Scanning Probe Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Scanning Tunneling  Microscopy','Scanning Tunneling  Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Size-Exclusion Chromatography with Multi-Angle Light Scattering','SEC-MALS Instrument');
insert into common_lookup(attribute,name,value) values ('instrument','Spectrophotometry','Spectrophotometer');
insert into common_lookup(attribute,name,value) values ('instrument','Transmission Electron Microscopy','Transmission Electron Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','X-Ray Photoelectron Spectroscopy','X-Ray Photoelectron Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Microplate Analysis','Microplate Reader');
insert into common_lookup(attribute,name,value) values ('instrument','Matrix Assisted Laser Desorption Ionisation - Time Of Flight','MALDI-TOF Mass Spectrometer');
insert into common_lookup(attribute,name,value) values ('instrument','Confocal Laser Scanning Microscopy','Confocal Microscope');
insert into common_lookup(attribute,name,value) values ('instrument','Colony Counting','Automated Colony Counter');
insert into common_lookup(attribute,name,value) values ('instrument','Dynamic Light Scattering','Zeta Potential Analyzer');  -- TODO review this

ALTER TABLE common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

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

INSERT INTO experiment_config(experiment_config_pk_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT i.instrument_config_pk_id, i.description, i.created_date, i.created_by,
	c.characterization_pk_id,i.instrument_pk_id
FROM instrument_config i, characterization c
WHERE c.instrument_config_pk_id = i.instrument_config_pk_id
;

INSERT INTO experiment_config_instrument(experiment_config_pk_id, instrument_pk_id)
SELECT i.instrument_config_pk_id, i.instrument_pk_id
FROM instrument_config i, characterization c
where c.instrument_config_pk_id=i.instrument_config_pk_id
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
	and (instrument.type = common_lookup.name
	and technique.type = common_lookup.name)
	or (instrument.type=common_lookup.value
	and technique.type=common_lookup.name)
	and technique_pk_id is not null;

-- update technique_pk_id from fresh insert of technique
UPDATE experiment_config e1, tmp_technique_id_mapping m
SET e1.technique_pk_id = m.technique_pk_id
WHERE m.instrument_pk_id=e1.technique_pk_id
;

ALTER TABLE characterization
 DROP FOREIGN KEY FK_characterization_instrument_config
;
ALTER TABLE characterization
 DROP instrument_config_pk_id;


DROP TABLE instrument_config;
DROP TABLE tmp_technique_id_mapping;

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

