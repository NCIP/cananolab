use canano;

INSERT INTO hibernate_unique_key(next_hi)
VALUES (1);

INSERT INTO instrument(instrument_pk_id,type,abbreviation,manufacturer) 
values (1,'Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering','AFFF-MALLS','Wyatt'),
 (2,'Atomic Force Microscope','AFM','Molecular Imaging'),
 (3,'Capillary Electrophoresis',null,'NA'),
 (4,'Clot Detection System',null,'Diagnostica'),
 (5,'Coulter Counter',null,'Beckman/Coulter'),
 (6,'Dynamic Light Scattering','DLS','Malvern'),
 (7,'Dynamic Light Scattering','DLS','Wyatt Technologies'),
 (8,'Energy Dispersive Spectroscopy','EDS','EDAX'),
 (9,'Flow Cytometer',null,'Becton Dickinson'),
 (10,'Hemocytometer',null,'Unknown'),
 (11,'High Performance Liquid Chromatography','HPLC','Agilent'),
 (12,'High Performance Liquid Chromatography','HPLC','Shimadzu'),
 (13,'Imaging System',null,'Kodak'),
 (14,'Liquid Chromatography','LC','Amersham'),
 (15,'Refractometer',null,'Waters'),
 (16,'Scintillation Counter',null,'Beckman/Coulter'),
 (17,'Size Exclusion Chromatography with Multi-Angle Light Scattering','SEC-MALLS','Wyatt'),
 (18,'Spectrophotometer',null,'Molecular Devices'),
 (19,'Spectrophotometer',null,'Tecan'),
 (20,'Spectrophotometer',null,'Thermo Electron'),
 (21,'Thermal Cycler',null,'Biorad'),
 (22,'Zetasizer',null,'Malvern');

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script
