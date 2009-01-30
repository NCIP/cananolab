use canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

INSERT INTO hibernate_unique_key(next_hi)
VALUES (1);

ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT into instrument (type,manufacturer,created_date,created_by) values ('Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering Instrument',null,sysdate(),'SEED_DATA');
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
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography System','Agilent',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('High Performance Liquid Chromatography System','Shimadzu',sysdate(),'SEED_DATA');
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
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Size Exclusion Chromatography with Multi-Angle Light Scattering Instrument','Wyatt Technologies',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Thermo Electron',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Tecan',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Spectrophotometer','Molecular Devices',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('X-Ray Photoelectron Spectroscope',null,sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Microplate Reader','Biorad',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Matrix Assisted Laser Desorption Ionisation - Time Of Flight Mass Spectrometer','Waters',sysdate(),'SEED_DATA');
INSERT into instrument (type,manufacturer,created_date,created_by) values ('Confocal Microscope','Carl Zeiss',sysdate(),'SEED_DATA');


ALTER TABLE canano.instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script
