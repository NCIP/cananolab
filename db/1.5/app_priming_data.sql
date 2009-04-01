use canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

INSERT INTO hibernate_unique_key(next_hi)
VALUES (1);


ALTER TABLE canano.technique
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

ALTER TABLE canano.technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into instrument (type,manufacturer,created_date,created_by) values ('AFFF-MALS Instrument',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Atomic Force Microscope','Molecular Imaging',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Capillary Electrophoresis Instrument',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Hemocytometer',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coulter Counter','Beckman/Coulter',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Brookhaven Instruments',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Malvern',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Molecular Devices',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Dynamic Light Scattering Instrument','Wyatt Technologies',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electron Microprobe',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electrophoretic Light Scattering Instrument','Brookhaven Instruments',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Electrophoretic Light Scattering Instrument','Malvern',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coagulation Monitor',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Energy Dispersive Spectrometer','EDAX',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Environmental Transmission Electron Microscope','JEOL',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Environmental Transmission Electron Microscope','Philips',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coagulation Monitor','Diagnostica Stago',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Flow Cytometer','Becton Dickinson',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Surface Area and Pore Size Analyzer','Quantachrome Instruments',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope','Hitachi',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope','Brookhaven Instruments',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography Instrument','Agilent',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography Instrument','Shimadzu',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Transmission Electron Microscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Imaging System','Kodak',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Laser Doppler Velocimeter',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Liquid Chromatography System','Amersham',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Hemocytometer',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Coulter Counter',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Phase Light Scattering Instrument','Brookhaven Instruments',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Phase Light Scattering Instrument','Malvern',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Thermal Cycler','Biorad',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Powder Diffractometer',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scintillation Counter','Beckman/Coulter',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Refractometer','Waters',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Auger Spectrometer',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Electron Microscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Probe Microscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Scanning Tunneling Microscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('SEC-MALS Instrument','Wyatt Technologies',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Thermo Electron',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Tecan',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Molecular Devices',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('X-Ray Photoelectron Spectroscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Microplate Reader','Biorad',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('MALDI-TOF Mass Spectrometer','Waters',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Confocal Microscope','Carl Zeiss',sysdate(),'SEED_DATA');

ALTER TABLE canano.instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script
