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
INSERT into technique (type,abbreviation,created_date,created_by) values ('Microplate Analysis ',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight','MALDI-TOF',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Particle Quantitation',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Polymerase Chain Reaction','PCR',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Powder Diffraction',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Radiation Quantiation',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Refractometry',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Auger Spectrometry','SAM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Electron Microscopy','SEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Probe Microscopy ','SPM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Scanning Tunneling  Microscopy','STM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Size-Exclusion Chromatography with Multi-Angle Light Scattering','SEC-MALS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Spectrophotometry',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Transmission Electron Microscopy','TEM',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('X-Ray Photoelectron Spectroscopy','XPS',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Gel Filtration Chromatography','GFC',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Fast Protein Liquid Chromatography','FPLC',sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Zeta Potential Analysis',null,sysdate(),'SEED_DATA');
INSERT into technique (type,abbreviation,created_date,created_by) values ('Mass Quantitation',null,sysdate(),'SEED_DATA');

ALTER TABLE canano.technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into instrument (type,created_date,created_by) values ('Photometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Atomic Force Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Capillary Electrophoresis Instrument',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Hemocytometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Coulter Counter', sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Dynamic Light Scattering Instrument',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Electron Microprobe',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Electrophoretic Light Scattering Instrument',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Coagulation Monitor',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Energy Dispersive Spectrometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Environmental Transmission Electron Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Coagulation Monitor',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Flow Cytometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Surface Area and Pore Size Analyzer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scanning Electron Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Transmission Electron Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Imaging System',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Hemocytometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Coulter Counter',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Thermal Cycler',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Powder Diffractometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scintillation Counter',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Refractometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scanning Auger Spectrometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scanning Electron Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scanning Probe Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Scanning Tunneling Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Spectrophotometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('X-Ray Photoelectron Spectrometer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Microplate Reader',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Confocal Microscope',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Separation Column',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Absorbance Detector',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Control Module',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Fraction Collector',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Zeta Potential Analyzer',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Gel Filtration Column',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('Analytical Balance',sysdate(),'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('MALDI-TOF Mass Spectrometer', sysdate(), 'SEED_DATA');
INSERT into instrument (type,created_date,created_by) values ('MALDI-TOF Mass Spectrometer', sysdate(), 'SEED_DATA');
ALTER TABLE canano.instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script
