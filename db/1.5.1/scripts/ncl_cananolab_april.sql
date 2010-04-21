-- phpMyAdmin SQL Dump
-- version 2.11.5.2
-- http://www.phpmyadmin.net
--
-- Host: sqlprod3
-- Generation Time: Apr 14, 2010 at 02:27 PM
-- Server version: 5.0.24
-- PHP Version: 5.2.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `cananolab`
--

-- --------------------------------------------------------

--
-- Table structure for table `agent`
--

CREATE TABLE IF NOT EXISTS `agent` (
  `agent_pk_id` bigint(20) NOT NULL,
  `discriminator` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `name` varchar(200) default NULL,
  `other` varchar(200) default NULL,
  `sequence` varchar(2000) default NULL,
  PRIMARY KEY  (`agent_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `agent`
--

INSERT INTO `agent` (`agent_pk_id`, `discriminator`, `description`, `name`, `other`, `sequence`) VALUES
(2719744, 'ImageContrastAgent', NULL, 'Magnevist', 'MRI', NULL),
(2719745, 'ImageContrastAgent', NULL, 'Magnevist', 'MRI', NULL),
(2719746, 'ImageContrastAgent', NULL, 'Magnevist', 'MRI', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `agent_target`
--

CREATE TABLE IF NOT EXISTS `agent_target` (
  `agent_target_pk_id` bigint(20) NOT NULL,
  `discriminator` varchar(200) default NULL,
  `name` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `list_index` bigint(20) default NULL,
  `agent_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`agent_target_pk_id`),
  KEY `fk_agent_target_agent` (`agent_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `agent_target`
--


-- --------------------------------------------------------

--
-- Table structure for table `associated_file`
--

CREATE TABLE IF NOT EXISTS `associated_file` (
  `associated_file_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`associated_file_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `associated_file`
--


-- --------------------------------------------------------

--
-- Table structure for table `bioassay_data_data_category`
--

CREATE TABLE IF NOT EXISTS `bioassay_data_data_category` (
  `derived_bioassay_data_pk_id` bigint(20) NOT NULL,
  `category_index` int(11) NOT NULL,
  `category_name` varchar(2000) default NULL,
  PRIMARY KEY  (`derived_bioassay_data_pk_id`,`category_index`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `bioassay_data_data_category`
--

INSERT INTO `bioassay_data_data_category` (`derived_bioassay_data_pk_id`, `category_index`, `category_name`) VALUES
(1540096, 0, 'Volume Distribution'),
(1540097, 0, 'Volume Distribution'),
(1540098, 0, 'Volume Distribution'),
(1540099, 0, 'Volume Distribution'),
(1540100, 0, 'Mass Distribution'),
(1540102, 0, 'Volume Distribution'),
(1540103, 0, 'Volume Distribution'),
(1540104, 0, 'Mass Distribution'),
(1540106, 0, 'Volume Distribution'),
(1540107, 0, 'Volume Distribution'),
(1540108, 0, 'Volume Distribution'),
(1540109, 0, 'Intensity Distribution'),
(1540110, 0, 'Number Distribution'),
(1540111, 0, 'Volume Distribution'),
(1540112, 0, 'Volume Distribution'),
(1540113, 0, 'Volume Distribution'),
(1540114, 0, 'Volume Distribution'),
(1540115, 0, 'Volume Distribution'),
(1540118, 0, 'Volume Distribution'),
(1540119, 0, 'Volume Distribution'),
(1540120, 0, 'Volume Distribution'),
(1540121, 0, 'Volume Distribution'),
(1900544, 0, 'Mass Distribution'),
(1900545, 0, 'Volume Distribution'),
(1900546, 0, 'Volume Distribution'),
(1900547, 0, 'Mass Distribution'),
(3801088, 0, 'Volume Distribution'),
(3801089, 0, 'Volume Distribution'),
(3801090, 0, 'Volume Distribution'),
(3801091, 0, 'Volume Distribution'),
(3801092, 0, 'Volume Distribution'),
(3801093, 0, 'Volume Distribution'),
(3801094, 0, 'Volume Distribution'),
(3801095, 0, 'Volume Distribution'),
(3801096, 0, 'Intensity Distribution'),
(4259840, 0, 'Volume Distribution'),
(4259842, 0, 'Mass Distribution'),
(4259854, 0, 'Volume Distribution'),
(4259859, 0, 'Mass Distribution'),
(4259865, 0, 'Volume Distribution'),
(4259866, 0, 'Volume Distribution'),
(4259867, 0, 'Volume Distribution'),
(4620288, 0, ''),
(4620291, 0, 'Volume Distribution'),
(4620292, 0, 'Volume Distribution');

-- --------------------------------------------------------

--
-- Table structure for table `carbon_nanotube_composition`
--

CREATE TABLE IF NOT EXISTS `carbon_nanotube_composition` (
  `chirality` varchar(100) default NULL,
  `growth_diameter` decimal(22,3) default NULL,
  `average_length` decimal(22,3) default NULL,
  `wall_type` varchar(100) default NULL,
  `cn_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`cn_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `carbon_nanotube_composition`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_nanotube`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_nanotube` BEFORE DELETE ON `cananolab`.`carbon_nanotube_composition`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id, 
       chirality, 
       growth_diameter, 
       average_length, 
       wall_type, 
       deleted_date, 
       table_source)
    values
     ( old.cn_composition_pk_id, 
       old.chirality, 
       old.growth_diameter, 
       old.average_length, 
       old.wall_type, 
       sysdate(), 
       'CARBON_NANOTUBE_COMPOSITION');
      
end
//
DELIMITER ;

--
-- Dumping data for table `carbon_nanotube_composition`
--

INSERT INTO `carbon_nanotube_composition` (`chirality`, `growth_diameter`, `average_length`, `wall_type`, `cn_composition_pk_id`) VALUES
(NULL, '20.000', NULL, 'Single (SWNT)', 2949120),
('1', '10.000', '12.000', 'Multiple (MWNT)', 3473408);

-- --------------------------------------------------------

--
-- Table structure for table `characterization`
--

CREATE TABLE IF NOT EXISTS `characterization` (
  `characterization_pk_id` bigint(20) NOT NULL,
  `classification` varchar(200) default NULL,
  `source` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `identifier_name` varchar(500) default NULL,
  `name` varchar(100) default NULL,
  `discriminator` varchar(50) default NULL,
  `created_date` datetime default NULL,
  `created_by` varchar(200) default NULL,
  `protocol_file_pk_id` bigint(20) default NULL,
  `instrument_config_pk_id` bigint(20) default NULL,
  `instrument_pk_id` bigint(20) default NULL,
  `char_protocol_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`characterization_pk_id`),
  KEY `sys_c00246819` (`instrument_config_pk_id`),
  KEY `sys_c00246820` (`protocol_file_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `characterization`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_characterization`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_characterization` BEFORE DELETE ON `cananolab`.`characterization`
 FOR EACH ROW begin
   
    insert into history_characterization
     ( characterization_pk_id,
       classification,
       source,description,
       identifier_name,
       name,
       discriminator,
       created_date,
       created_by,
       protocol_file_pk_id,
       instrument_config_pk_id, 
       deleted_date,
       table_source)
    values
     ( old.characterization_pk_id, 
       old.classification, 
       old.source, 
       old.description, 
       old.identifier_name, 
       old.name, 
       old.discriminator, 
       old.created_date, 
       old.created_by, 
       old.protocol_file_pk_id, 
       old.instrument_config_pk_id, 
       sysdate(),
       'CHARACTERIZATION');
end
//
DELIMITER ;

--
-- Dumping data for table `characterization`
--

INSERT INTO `characterization` (`characterization_pk_id`, `classification`, `source`, `description`, `identifier_name`, `name`, `discriminator`, `created_date`, `created_by`, `protocol_file_pk_id`, `instrument_config_pk_id`, `instrument_pk_id`, `char_protocol_pk_id`) VALUES
(1376256, 'Physical', 'Vendor', 'Tris (hydroxy)terminated dendrimer with diaminobutane (DAB) core and polyamidoamine (PAMAM) branching', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-11 09:10:11', 'janedoe', NULL, NULL, NULL, NULL),
(1376257, 'Physical', 'NCL', 'Statistics graph based on size distribution by volume from DLS', 'DNT 082006 (Saline 25)', 'Size', 'Size', '2006-12-11 09:20:33', 'janedoe', NULL, 1507328, NULL, NULL),
(1376258, 'Physical', 'NCL', 'Statistics graph based on size distribution by volume from DLS', 'DNT 082006 (PBS 25)', 'Size', 'Size', '2006-12-11 09:42:47', 'janedoe', NULL, 1507328, NULL, NULL),
(1376259, 'Physical', 'Vendor', 'Molecular weight provided by vendor', 'DNT 082006 (DNT)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 09:44:34', 'janedoe', NULL, NULL, NULL, NULL),
(1376260, 'Physical', 'NCL', 'Determination of molecular weight by SEC-MALLS', 'DNT 082006 (SEC-MALLS)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 09:52:54', 'janedoe', NULL, 1507344, NULL, NULL),
(1376261, 'Physical', 'NCL', 'Determination of molecular weight by AFFF-MALLS', 'DNT 082006 (AFFF-MALLS)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 09:58:53', 'janedoe', NULL, 1507332, NULL, NULL),
(1376262, 'Physical', 'NCL', 'Determination of purity by HPLC', 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2007-04-12 19:51:14', 'jennifer', NULL, NULL, NULL, NULL),
(1376263, 'Physical', 'Vendor', 'Pyrrolidone terminated dendrimer with diaminobutane (DAB) core and polyamidoamine (PAMAM) branching', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-11 10:16:56', 'janedoe', NULL, NULL, NULL, NULL),
(1376264, 'Physical', 'NCL', 'Molecular Weight as provided by the vendor', 'DNT 082006 (DNT)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 10:17:52', 'janedoe', NULL, NULL, NULL, NULL),
(1376265, 'Physical', 'NCL', NULL, 'DNT 082006 (SEC-MALLS)', 'Molecular Weight', 'MolecularWeight', '2007-04-12 20:14:01', 'jennifer', NULL, 1507344, NULL, NULL),
(1376266, 'Physical', 'NCL', NULL, 'DNT 082006 (AFFF-MALLS)', 'Molecular Weight', 'MolecularWeight', '2007-04-12 20:18:35', 'jennifer', NULL, 1507332, NULL, NULL),
(1376267, 'Physical', 'NCL', NULL, 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2006-12-11 10:30:53', 'janedoe', NULL, NULL, NULL, NULL),
(1376268, 'Physical', 'NCL', 'CooNA terminated dendrimer with diaminobutane (DAB) core and polyamidoamine (PAMAM) branching', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-14 09:51:10', 'janedoe', NULL, NULL, NULL, NULL),
(1376269, 'Physical', 'NCL', 'The effect of size based on 25 degree Celsius and Saline solvent', 'DNT 082006 (Saline 25)', 'Size', 'Size', '2006-12-11 10:46:08', 'janedoe', NULL, 1507328, NULL, NULL),
(1376270, 'Physical', 'NCL', 'DLS volume size distribution at 25 degrees Celsius in PBS solvent', 'DNT 082006 (PBS 25)', 'Size', 'Size', '2007-04-11 14:46:00', 'jennifer', NULL, 1507328, NULL, NULL),
(1376271, 'Physical', 'NCL', 'The effect of size based on 37 degree Celsius and PBS solvent', 'DNT 082006 (PBS 37)', 'Size', 'Size', '2006-12-14 16:28:21', 'janedoe', NULL, 1507328, NULL, NULL),
(1376272, 'Physical', 'NCL', 'NCL 22 in 10 mM NaCl', 'DNT 082006 (10 mM NaCL)', 'Size', 'Size', '2006-12-11 10:54:19', 'janedoe', NULL, 1507328, NULL, NULL),
(1376273, 'Physical', 'NCL', NULL, 'DNT 082006 (MALDI-TOF)', 'Molecular Weight', 'MolecularWeight', '2007-04-12 16:53:01', 'jennifer', NULL, NULL, NULL, NULL),
(1376274, 'Physical', 'NCL', 'Molecular weight provided by the vendor', 'DNT 082006 (DNT)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 11:17:53', 'janedoe', NULL, NULL, NULL, NULL),
(1376275, 'Physical', 'NCL', NULL, 'DNT 082006 (SEC-MALLS)', 'Molecular Weight', 'MolecularWeight', '2007-04-12 20:09:42', 'jennifer', NULL, 1507344, NULL, NULL),
(1376276, 'Physical', 'NCL', NULL, 'DNT 082006 (AFFF-MALLS)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 11:29:27', 'janedoe', NULL, 1507332, NULL, NULL),
(1376277, 'Physical', 'NCL', NULL, 'DNT 082006', 'Morphology', 'Morphology', '2006-12-14 16:31:20', 'janedoe', NULL, NULL, NULL, NULL),
(1376278, 'Physical', 'NCL', NULL, 'DNT 082006', 'Solubility', 'Solubility', '2006-12-11 12:34:19', 'janedoe', NULL, NULL, NULL, NULL),
(1376279, 'Physical', 'NCL', NULL, 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2007-04-12 17:07:44', 'jennifer', NULL, NULL, NULL, NULL),
(1376280, 'Physical', 'NCL', NULL, 'DNT 082006 (CE)', 'Purity', 'Purity', '2006-12-11 12:48:07', 'janedoe', NULL, NULL, NULL, NULL),
(1376281, 'Physical', 'Vendor', 'Pyrrolidinone terminated dendrimer with diaminobutane (DAB) core and polyamidoamine (PAMAM) branching. ', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-11 14:19:55', 'janedoe', NULL, NULL, NULL, NULL),
(1376282, 'Physical', 'NCL', NULL, 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2007-04-12 19:58:06', 'jennifer', NULL, NULL, NULL, NULL),
(1376283, 'Physical', 'NCL', 'Tris (hydroxyl) terminated dendrimer with diaminobutane (DAB) core and polyamidoamine (PAMAM) branching', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-11 14:27:25', 'janedoe', NULL, NULL, NULL, NULL),
(1376284, 'Physical', 'NCL', NULL, 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2007-04-12 19:55:46', 'jennifer', NULL, NULL, NULL, NULL),
(1376285, 'Physical', 'NCL', 'COONa terminated with diaminobutane (DAB) core and PAMAM dendrimer with Magnevist', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-14 09:44:11', 'janedoe', NULL, NULL, NULL, NULL),
(1376286, 'Physical', 'NCL', 'The effect of size based on 25 degree Celsius and Saline solvent', 'DNT 082006 (Saline 25)', 'Size', 'Size', '2006-12-11 14:49:08', 'janedoe', NULL, 1507328, NULL, NULL),
(1376287, 'Physical', 'NCL', 'The effect of size based on 25 degree Celsius and PBS solvent', 'DNT 082006 (PBS 25)', 'Size', 'Size', '2006-12-11 14:54:56', 'janedoe', NULL, 1507328, NULL, NULL),
(1376288, 'Physical', 'NCL', 'The effect of size based on 37 degree Celsius and PBS solvent', 'DNT 082006 (PBS 37)', 'Size', 'Size', '2006-12-11 15:15:20', 'janedoe', NULL, 1507328, NULL, NULL),
(1376289, 'Physical', 'NCL', 'Intensity and Volume weighted graphs for NCL22 and NCL23', 'DNT 082006 (Hydrodynamic ', 'Size', 'Size', '2006-12-11 15:25:57', 'janedoe', NULL, 1507328, NULL, NULL),
(1867776, 'Physical', 'NCL', NULL, 'DNT 082006 (MALDI-TOF)', 'Molecular Weight', 'MolecularWeight', '2007-04-12 16:52:07', 'jennifer', NULL, NULL, NULL, NULL),
(1867777, 'Physical', 'NCL', NULL, 'DNT 082006 (SEC-MALLS)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 16:03:38', 'janedoe', NULL, NULL, NULL, NULL),
(1867778, 'Physical', 'NCL', NULL, 'DNT 082006 (AFFF-MALLS)', 'Molecular Weight', 'MolecularWeight', '2006-12-11 16:15:06', 'janedoe', NULL, NULL, NULL, NULL),
(1867779, 'Physical', 'NCL', NULL, 'DNT 082006', 'Morphology', 'Morphology', '2006-12-14 16:32:22', 'janedoe', NULL, NULL, NULL, NULL),
(1867780, 'Physical', 'NCL', NULL, 'DNT 082006', 'Solubility', 'Solubility', '2006-12-11 16:18:04', 'janedoe', NULL, NULL, NULL, NULL),
(1867781, 'Physical', 'NCL', NULL, 'DNT 082006 (HPLC)', 'Purity', 'Purity', '2006-12-11 16:22:35', 'janedoe', NULL, NULL, NULL, NULL),
(1867782, 'Physical', 'NCL', NULL, 'DNT 082006 (CE)', 'Purity', 'Purity', '2006-12-11 16:30:54', 'janedoe', NULL, NULL, NULL, NULL),
(1867783, 'Physical', 'NCL', 'Magnevist', 'DNT 082006', 'Composition', 'DendrimerComp', '2006-12-11 16:35:24', 'janedoe', NULL, NULL, NULL, NULL),
(1867784, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT LLC', 'Cell Viability', 'CellViability', '2006-12-19 12:24:02', 'janedoe', 103, NULL, NULL, NULL),
(1867785, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 LDH LLC', 'Cell Viability', 'CellViability', '2006-12-11 17:16:09', 'janedoe', 102, NULL, NULL, NULL),
(1867786, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT Hep-G2', 'Cell Viability', 'CellViability', '2006-12-11 17:22:44', 'janedoe', 105, NULL, NULL, NULL),
(1867787, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 LDH Hep-G2', 'Cell Viability', 'CellViability', '2007-04-12 20:35:06', 'jennifer', 104, NULL, NULL, NULL),
(1867788, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT LLC', 'Cell Viability', 'CellViability', '2006-12-19 12:27:00', 'janedoe', 103, NULL, NULL, NULL),
(1867789, 'In Vitro', 'NCL', NULL, 'DNT 082006 LDH LLC', 'Cell Viability', 'CellViability', '2006-12-11 17:31:47', 'janedoe', 102, NULL, NULL, NULL),
(1867790, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT Hep-G2', 'Cell Viability', 'CellViability', '2006-12-11 17:34:07', 'janedoe', 105, NULL, NULL, NULL),
(1867791, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT LLC', 'Cell Viability', 'CellViability', '2006-12-11 17:39:42', 'janedoe', 103, NULL, NULL, NULL),
(1867792, 'In Vitro', 'NCL', NULL, 'DNT 082006 LDH LLC', 'Cell Viability', 'CellViability', '2006-12-11 17:43:49', 'janedoe', 102, NULL, NULL, NULL),
(1867793, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 MTT Hep-G2', 'Cell Viability', 'CellViability', '2006-12-11 17:47:28', 'janedoe', 105, NULL, NULL, NULL),
(1867794, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 082006 LDH Hep-G2', 'Cell Viability', 'CellViability', '2007-04-12 20:30:30', 'jennifer', 104, NULL, NULL, NULL),
(1867795, 'In Vitro', 'NCL', 'Analysis of nanoparticle hemolytic properties', 'DNT 082006', 'Hemolysis', 'Hemolysis', '2006-12-11 18:01:17', 'janedoe', 108, NULL, NULL, NULL),
(1867796, 'In Vitro', 'NCL', NULL, 'DNT 082006', 'Hemolysis', 'Hemolysis', '2006-12-11 18:03:00', 'janedoe', 108, NULL, NULL, NULL),
(1867797, 'In Vitro', 'NCL', 'Analysis of nanoparticle hemolytic properties', 'DNT 082006', 'Hemolysis', 'Hemolysis', '2006-12-11 18:05:05', 'janedoe', 108, NULL, NULL, NULL),
(1867798, 'In Vitro', 'NCL', 'Nanoparticle ability to induce platelet aggregation', 'DNT 082006', 'Platelet Aggregation', 'PlateletAggregation', '2006-12-11 18:16:07', 'janedoe', 112, NULL, NULL, NULL),
(1867799, 'In Vitro', 'NCL', NULL, 'DNT 082006', 'Platelet Aggregation', 'PlateletAggregation', '2006-12-11 18:18:23', 'janedoe', 112, NULL, NULL, NULL),
(1867800, 'In Vitro', 'NCL', 'Nanoparticle ability to induce platelet aggregation', 'DNT 082006', 'Platelet Aggregation', 'PlateletAggregation', '2006-12-11 18:19:51', 'janedoe', 112, NULL, NULL, NULL),
(1867801, 'In Vitro', 'NCL', 'Analysis of nanoparticle toxicity to bone marrow cells', 'DNT 082006', 'CFU_GM', 'CFU_GM', '2006-12-11 18:26:04', 'janedoe', 113, NULL, NULL, NULL),
(1867802, 'In Vitro', 'NCL', 'Analysis of nanoparticle toxicity to bone marrow cells', 'DNT 082006', 'CFU_GM', 'CFU_GM', '2006-12-11 18:27:34', 'janedoe', 113, NULL, NULL, NULL),
(1867803, 'In Vitro', 'NCL', 'Analysis of nanoparticle toxicity to bone marrow cells', 'DNT 082006', 'CFU_GM', 'CFU_GM', '2006-12-11 18:28:52', 'janedoe', 113, NULL, NULL, NULL),
(2424832, 'In Vitro', 'NCL', NULL, 'DNT 082006', 'Coagulation', 'Coagulation', '2007-04-13 07:57:04', 'jennifer', 111, NULL, NULL, NULL),
(2424833, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Coagulation', 'Coagulation', '2007-04-13 08:01:40', 'jennifer', 111, NULL, NULL, NULL),
(2424834, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Coagulation', 'Coagulation', '2007-04-13 08:06:45', 'jennifer', 111, NULL, NULL, NULL),
(2424835, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Plasma Protein Binding', 'PlasmaProteinBinding', '2006-12-13 15:59:04', 'janedoe', 114, NULL, NULL, NULL),
(2424836, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Oxidative Burst', 'OxidativeBurst', '2006-12-13 17:16:57', 'janedoe', 117, NULL, NULL, NULL),
(2424837, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Oxidative Burst', 'OxidativeBurst', '2006-12-13 17:22:20', 'janedoe', 117, NULL, NULL, NULL),
(2424838, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Phagocytosis', 'Phagocytosis', '2006-12-13 17:28:39', 'janedoe', 119, NULL, NULL, NULL),
(2424839, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Phagocytosis', 'Phagocytosis', '2006-12-13 17:30:07', 'janedoe', 119, NULL, NULL, NULL),
(2424840, 'In Vitro', 'NCL', NULL, 'DNT 082006', 'Phagocytosis', 'Phagocytosis', '2006-12-13 17:31:40', 'janedoe', 119, NULL, NULL, NULL),
(2424841, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Cytokine Induction', 'CytokineInduction', '2007-04-13 09:42:14', 'jennifer', 109, NULL, NULL, NULL),
(2424842, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'Cytokine Induction', 'CytokineInduction', '2007-04-13 09:37:22', 'jennifer', 109, NULL, NULL, NULL),
(2424843, 'In Vitro', 'NCL', NULL, 'DNT 082006', 'Cytokine Induction', 'CytokineInduction', '2007-04-13 09:40:11', 'jennifer', 109, NULL, NULL, NULL),
(2424844, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'NK Cell Cytotoxic Activity', 'NKCellCytotoxicActivity', '2007-04-13 10:02:01', 'jennifer', 110, NULL, NULL, NULL),
(2424845, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'NK Cell Cytotoxic Activity', 'NKCellCytotoxicActivity', '2007-04-13 09:59:18', 'jennifer', 110, NULL, NULL, NULL),
(2424846, 'In Vitro', 'NCL', NULL, 'DNT 082006 ', 'NK Cell Cytotoxic Activity', 'NKCellCytotoxicActivity', '2007-04-13 10:05:43', 'jennifer', 110, NULL, NULL, NULL),
(3768320, 'Physical', 'NCL', NULL, 'DNT 122006 DLS', 'Size', 'Size', '2007-04-11 14:55:36', 'jennifer', NULL, 1507328, NULL, NULL),
(3768321, 'Physical', 'NCL', NULL, 'DNT 122006 DLS', 'Size', 'Size', '2007-04-11 15:07:28', 'jennifer', NULL, 1507328, NULL, NULL),
(3768322, 'Physical', 'NCL', NULL, 'DNT 122006 DLS', 'Size', 'Size', '2007-04-11 15:40:32', 'jennifer', NULL, 1507328, NULL, NULL),
(3768323, 'Physical', 'NCL', NULL, 'DNT 122006 DLS Filtrati', 'Size', 'Size', '2007-04-11 16:52:35', 'jennifer', NULL, 1507328, NULL, NULL),
(3768324, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-11 21:00:08', 'jennifer', NULL, NULL, NULL, NULL),
(3768325, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-11 20:54:05', 'jennifer', NULL, NULL, NULL, NULL),
(3768326, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-11 20:57:37', 'jennifer', NULL, NULL, NULL, NULL),
(3768327, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-12 16:40:04', 'jennifer', NULL, NULL, NULL, NULL),
(3768328, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-12 16:42:50', 'jennifer', NULL, NULL, NULL, NULL),
(3768329, 'Physical', 'NCL', NULL, 'DNT 122006 UV-Vis', 'Purity', 'Purity', '2007-04-12 16:44:44', 'jennifer', NULL, NULL, NULL, NULL),
(3768330, 'In Vitro', 'NCL', 'Cell lines were treated for 6, 24, and 48 h with 0.004-1.0 mg/ML of sample', 'DNT 122006 LDH Hep-G2', 'Cell Viability', 'CellViability', '2007-04-12 20:41:44', 'jennifer', 104, NULL, NULL, NULL),
(3768331, 'In Vitro', 'NCL', NULL, 'DNT 122006', 'Leukocyte Proliferation', 'LeukocyteProliferation', '2007-04-13 10:02:19', 'jennifer', 116, NULL, NULL, NULL),
(3768332, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Leukocyte Proliferation', 'LeukocyteProliferation', '2007-04-13 08:28:28', 'jennifer', 116, NULL, NULL, NULL),
(3768333, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Leukocyte Proliferation', 'LeukocyteProliferation', '2007-04-13 08:30:17', 'jennifer', 116, NULL, NULL, NULL),
(3768334, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Complement Activation', 'ComplementActivation', '2007-04-13 08:33:33', 'jennifer', 115, NULL, NULL, NULL),
(3768335, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Complement Activation', 'ComplementActivation', '2007-04-13 08:36:19', 'jennifer', 115, NULL, NULL, NULL),
(3768336, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Complement Activation', 'ComplementActivation', '2007-04-13 08:37:57', 'jennifer', 115, NULL, NULL, NULL),
(3768337, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Chemotaxis', 'Chemotaxis', '2007-04-13 09:09:37', 'jennifer', 118, NULL, NULL, NULL),
(3768338, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Chemotaxis', 'Chemotaxis', '2007-04-13 09:12:33', 'jennifer', 118, NULL, NULL, NULL),
(3768339, 'In Vitro', 'NCL', NULL, 'DNT 122006 ', 'Chemotaxis', 'Chemotaxis', '2007-04-13 09:14:57', 'jennifer', 118, NULL, NULL, NULL),
(4227072, 'Physical', 'NCL', 'The Z-avg size, which is more sensitive to larger size species in the sample, of DF1 is 7.8 nm. The Z-avg size of DF1-mini is 8.9 nm. There is only a slight difference in the volume-weighted size (denoted as Â‘PeakÂ’ in Figure 2) between the two conjugates, with DF1 being slightly larger (4.8 nm) than DF1-mini (4.0 nm) as expected.  This data also suggests that under these buffer conditions, there is little or no aggregation in these samples.', 'C-60 052007', 'Size', 'Size', '2007-06-14 15:22:33', 'jennifer', NULL, 1507328, NULL, NULL),
(4227073, 'Physical', 'NCL', 'DF1 and DF1-mini have similar UV-Vis absorption spectra as expected. The peaks at 256 nm and 325 nm are at characteristic wavelengths for absorbance of the fullerene ring and are at approximately the same wavelengths (as expected) for DF1 and DF1-mini. The peaks around 200 nm are attributed to absorbance in the dendrimer branches. There is a small shift in the lambda max of this peak between the spectra of the DF1 and DF1-mini samples. The lambda max of the lowest wavelength peak in the DF1 spectrum is at a lower wavelength than that of the analogous peak in the DF1-mini spectrum.', 'C-60 052007', 'Purity', 'Purity', '2007-06-14 15:25:59', 'jennifer', NULL, NULL, NULL, NULL),
(4227074, 'Physical', 'NCL', NULL, 'C-60 052007', 'Molecular Weight', 'MolecularWeight', '2007-05-17 21:46:29', 'jennifer', NULL, NULL, NULL, NULL),
(4227075, 'Physical', 'NCL', NULL, 'C-60 052007 HPLC', 'Purity', 'Purity', '2007-05-17 21:56:29', 'jennifer', NULL, 4194307, NULL, NULL),
(4227076, 'Physical', 'NCL', 'Capillary Electrophoresis (CE) is a powerful chromatographic technique that separates analytes on the basis of electrophoretic mobility differences. Mobility is determined by the mass to charge ratio of the analyte. CE has high separation efficiencies, high sensitivity, short run times and high automation capability. Sample concentration 0.1 mg/mL in water, capillary Sample preparation: all stock samples were prepared in 50% acetonitrile as 1mg/mL solutions and stored at -40 Â°C. The stock solutions were diluted 10-fold with water before capillary electrophoresis (0.1mg/mL final concentration).  Running conditions: capillary: 60cm Ã—50 Âµm I.D; buffer: 10mM sodium tetraborate; separation voltage: 22kV; injection pressure: 20s/0.5 psi/20s; detector: UV (wavelength 200nm). The capillary was rinsed sequentially with 1N sodium hydroxide, water, and the CE buffer between runs.', 'C-60 052007 CE', 'Purity', 'Purity', '2007-05-31 13:09:48', 'jennifer', NULL, NULL, NULL, NULL),
(4227077, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 were cytotoxic to porcine renal proximal tubule (LLC-PK1) cells.\r\n\r\nCytotoxicity was determined as described in the NCL method, LLC-PK1 Kidney Cytotoxicity Assay (GTA-1). Briefly, test materials were solubilized via sonication and diluted to the desired assay concentrations in cell culture media (0.004-1 mg/mL). Cells were plated in a 96-well, microtiter plate format. Cells were preincubated for 24 hours prior to test material addition, reaching an approximate confluence of 80%. Cells were then exposed to test material for 6, 24, and 48 hours in the dark, and cytotoxicity was determined using the MTT cell viability and LDH membrane integrity assays. In order to estimate IC50 values, the MTT cytotoxicity assay dose-response curves were fit to a  sigmoidal Hill equation, E= Emax CÂƒÃ—Âƒ}ÂƒvCÂƒÃ—Âƒn+ IC50ÂƒÃ—),  using nonlinear regression analysis (Figure 14) (WinNonlin, Pharsight Corp., Mountain View, CA). \r\n', 'C-60 052007', 'Cell Viability', 'CellViability', '2007-05-31 13:14:24', 'jennifer', NULL, NULL, NULL, NULL),
(4227078, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 were cytotoxic to porcine renal proximal tubule (LLC-PK1) or human leukemia (HL-60) cell lines. \r\n\r\nCytotoxicity was determined by the trypan blue dye exclusion assay. Test materials were solubilized via sonication and diluted to the 0.25 mg/mL test concentration. LLC-PK1 and HL60 cells were plated in 35mm, 6-well plates. Cells were preincubated for 24 hours prior to test material addition, reaching an approximate confluence of 80%. Cells were then exposed to the test material for 24 hours in the dark, and cytotoxicity was determined using the trypan blue dye exclusion cell viability assay. Briefly, trypsinized LLC-PK1 and HL60 cells were diluted 1:10 with 0.08% trypan blue in PBS, stained for 5 minutes, and counted with a hemocytometer. Cell viability was determined by counting the number of live cells, which exclude the dye, versus the number of dead cells, which are stained. \r\n', 'C-60 052007 HL60', 'Cell Viability', 'CellViability', '2007-05-31 13:17:30', 'jennifer', NULL, NULL, NULL, NULL),
(4227079, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 influence reactive oxygen species (ROS) generation in porcine renal proximal tubule (LLC-PK1) and human hepatocarcinoma cells (Hep G2) cell lines.\r\n\r\nReactive oxygen species were measured as described in the Hepatocyte Primary ROS Assay (GTA-7). Briefly, test materials were solubilized via sonication and diluted to the 0.004-1 mg/mL test concentrations in cell culture media. LLC-PK1 cells and Sprague Dawley primary hepatocytes were plated in 96-well, microtiter plate format. Cells were preincubated for 24 hours prior to initial staining, reaching an approximate confluence of 80%. Cells were then incubated in the dark for 30 minutes at 37&#730;C with 40 &#61549;M DCFH, the redox active dye. The wells were then washed with media, and treated with test samples in the dark. Microtiter plate fluorescence was measured at multiple times from 0-4 hours on a spectrophotometer at excitation 485 nm and emission 530 nm. \r\n\r\n', 'C-60 052007', 'Oxidative Stress', 'OxidativeStress', '2007-05-31 13:27:52', 'jennifer', NULL, NULL, NULL, NULL),
(4227080, 'In Vitro', 'NCL', 'The objective of this study was to determine the effect of DF1 cotreatment on the induction of apoptosis by cisplatin in porcine renal proximal tubule (LLC-PK1) cells.\r\n\r\nIn this study, caspase 3 activation was used as a measure of apoptosis. Caspase 3 activity was measured as described in the LLC-PK1 Kidney Apoptosis Assay (GTA-5). Briefly, test materials were diluted to the 0.5 mg/mL test concentration in cell culture media, with or without 50 ÂƒÃM cisplatin. LLC-PK1 cells were plated in 35 mM, 6-well plate format. Cells were preincubated for 24 hours prior to addition of test sample, reaching an approximate confluence of 80%. Cells were treated with test samples in the dark for 24 hours. Following the 24 hour treatment period, the cells were imaged under a light microscope and prepared for caspase 3 activity measurement according to the protocol. Caspase 3 activity was measured using a microtiter plate spectrophotometer at excitation 415 nm and emission 505 nm. \r\n', 'C-60 052007', 'Caspase 3 Activation', 'Caspase3Activation', '2007-05-31 13:33:57', 'jennifer', NULL, NULL, NULL, NULL),
(4227081, 'In Vitro', 'NCL', 'The objective of this study was to evaluate four fullerene derivatives: AF1, AF3, DF1, and C3 in terms of their effects on the integrity of red blood cells. Stock solutions with theoretical concentration of 1mg/mL were used for each C60 derivative. The resulting final nanoparticle concentration was 125 ÂƒÃg/mL . NCL protocol ITA-1 was followed. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively.', 'C-60 052007', 'Hemolysis', 'Hemolysis', '2007-09-13 07:50:15', 'jennifer', 108, NULL, NULL, NULL),
(4227082, 'In Vitro', 'NCL', 'The objective of this study was to evaluate the four C60 derivatives: AF1, AF3, DF1, and C3, in terms of their myelosuppressive effects. Since previous toxicology studies (Figures 16-19) indicated DF1 was chemoprotective in kidney cell models, we also examined protection from cisplatin-induced myelosuppression. Stock solutions with theoretical concentrations of 1 mg/mL were used for each C60 derivative. The final nanoparticle concentrations of all formulations were 50 ÂƒÃg/mL. NCL protocol ITA-2 was followed. Hematopoietic stem cells were isolated from the bone marrow of 8-12 week old C57BL/6 mice. The cells were cultured for 12 days in MethoCult media supplemented with cytokines in the presence of PBS (a negative control), cisplatin (a positive control), and the C60 derivatives (to test for myelosuppressive properties) or a combination of cisplatin and the C60 derivatives (to test for chemoprotective properties). Two independent samples of each formulation were prepared and analyzed in duplicate.', 'C-60 052007', 'CFU_GM', 'CFU_GM', '2007-05-31 13:50:18', 'jennifer', NULL, NULL, NULL, NULL),
(4227083, 'Physical', 'NCL', 'The Z-avg size, which is more sensitive to larger size species in the sample, of DF1 is 7.8 nm. The Z-avg size of DF1-mini is 8.9 nm. There is only a slight difference in the volume-weighted size between the two conjugates, with DF1 being slightly larger (4.8 nm) than DF1-mini (4.0 nm) as expected.  This data also suggests that under these buffer conditions, there is little or no aggregation in these samples.', 'C-60 052007', 'Size', 'Size', '2007-06-14 15:17:38', 'jennifer', NULL, 1507328, NULL, NULL),
(4227084, 'In Vitro', 'NCL', NULL, 'C-60 052007', 'Cell Viability', 'CellViability', '2007-05-31 14:01:57', 'jennifer', NULL, NULL, NULL, NULL),
(4227085, 'In Vitro', 'NCL', NULL, 'C-60 062007', 'Cell Viability', 'CellViability', '2007-06-08 09:57:53', 'jennifer', 101, NULL, NULL, NULL),
(4227086, 'In Vitro', 'NCL', NULL, 'C-60 062007', 'Cell Viability', 'CellViability', '2007-06-08 10:04:40', 'jennifer', 101, NULL, NULL, NULL),
(4227087, 'Physical', 'NCL', 'DF1 and DF1-mini have similar UV-Vis absorption spectra as expected. The peaks at 256 nm and 325 nm are at characteristic wavelengths for absorbance of the fullerene ring and are at approximately the same wavelengths (as expected) for DF1 and DF1-mini. The peaks around 200 nm are attributed to absorbance in the dendrimer branches. There is a small shift in the lambda max of this peak between the spectra of the DF1 and DF1-mini samples. The lambda max of the lowest wavelength peak in the DF1 spectrum is at a lower wavelength than that of the analogous peak in the DF1-mini spectrum.', 'C-60 062007 UV-VIS', 'Purity', 'Purity', '2007-06-14 15:38:18', 'jennifer', NULL, NULL, NULL, NULL),
(4227088, 'Physical', 'NCL', 'The theoretical molecular weights of DF1 and DF1-mini are 2828 Da and 1452 Da, respectively.  The experimental molecular weight determined from the mass spectra were 2827 Da for DF1 and 1476 Da (which corresponds to 1452 Da plus the mass of a sodium ion) for DF1-mini. The spectrum of the DF1 sample (A) contains several peaks in addition to the peak corresponding to DF1, suggesting the sample contains impurities or fragments due to MS conditions. Minor peaks (e.g., the peak at 720 Da which corresponds to a fullerene ring without any dendritic branches) are also present in the spectrum of DF1-mini (B) and suggest that minor impurities are also present in this sample.', 'C-60 062007 MS', 'Molecular Weight', 'MolecularWeight', '2007-06-14 16:04:39', 'jennifer', NULL, NULL, NULL, NULL),
(4227089, 'Physical', 'NCL', 'For DF1-Mini, the UV spectra (data not shown) for the peaks (see Figure 6) highlighted in cyan in Table 2 are consistent with that of DF1-mini measured with a UV-Vis spectrophotometer.  The UV spectral analysis on the remaining two peaks showed a major peak at 225 nm and a minor peak at 254 nm suggesting that these impurities contain the fullerene component.  DF1-mini seems to be relatively purer than the next generation derivative (DF1). This is also consistent with mass spectrometry analysis on these two derivatives. Further analysis with LC-MS would provide additional details on the species present as impurities in these two samples.', 'C-60 062007 HPLC', 'Purity', 'Purity', '2007-06-14 16:11:59', 'jennifer', NULL, 4194307, NULL, NULL),
(4227090, 'Physical', 'NCL', 'The measured zeta potential (-53.6 mV) is negative as expected for a fullerene ring with carboxylated dendritic branches. \r\n', 'C-60 062007 ZP', 'Surface', 'Surface', '2007-06-14 16:20:36', 'jennifer', NULL, NULL, NULL, NULL),
(4227091, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 were cytotoxic to porcine renal proximal tubule (LLC-PK1) or human leukemia (HL-60) cell lines. \r\n\r\nCytotoxicity was determined by the trypan blue dye exclusion assay. Test materials were solubilized via sonication and diluted to the 0.25 mg/mL test concentration. LLC-PK1 and HL60 cells were plated in 35mm, 6-well plates. Cells were preincubated for 24 hours prior to test material addition, reaching an approximate confluence of 80%. Cells were then exposed to the test material for 24 hours in the dark, and cytotoxicity was determined using the trypan blue dye exclusion cell viability assay. Briefly, trypsinized LLC-PK1 and HL60 cells were diluted 1:10 with 0.08% trypan blue in PBS, stained for 5 minutes, and counted with a hemocytometer. Cell viability was determined by counting the number of live cells, which exclude the dye, versus the number of dead cells, which are stained. \r\n', 'C-60 062007 TB', 'Cell Viability', 'CellViability', '2007-06-14 16:37:23', 'jennifer', NULL, NULL, NULL, NULL),
(4227092, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 influence reactive oxygen species (ROS) generation in porcine renal proximal tubule (LLC-PK1) and human hepatocarcinoma cells (Hep G2) cell lines.\r\n\r\nReactive oxygen species were measured as described in the Hepatocyte Primary ROS Assay (GTA-7). Briefly, test materials were solubilized via sonication and diluted to the 0.004-1 mg/mL test concentrations in cell culture media. LLC-PK1 cells and Sprague Dawley primary hepatocytes were plated in 96-well, microtiter plate format. Cells were preincubated for 24 hours prior to initial staining, reaching an approximate confluence of 80%. Cells were then incubated in the dark for 30 minutes at 37&#730;C with 40 &#61549;M DCFH, the redox active dye. The wells were then washed with media, and treated with test samples in the dark. Microtiter plate fluorescence was measured at multiple times from 0-4 hours on a spectrophotometer at excitation 485 nm and emission 530 nm. \r\n\r\nAF3 was found to fluoresce at the assay wavelengths, and due to this interference was not included in the ROS assay. Treatment of LLC-PK1 cells with AF1 and C3 resulted in a supralinear dose-responsive increase in ROS generation (Figure 17A and C). Treatment of LLC-PK1 cells with DF1, however, resulted in a dose-responsive decrease in ROS generation (Figure 17B). \r\n\r\nTreatment of SD primary hepatocytes with AF1 and C3 resulted in a dose responsive increase in ROS generation (Figure18A and C). Treatment SD primary hepatocytes with DF1, however, resulted in a dose responsive decrease in ROS generation.\r\n', 'C-60 052007 ROS', 'Oxidative Stress', 'OxidativeStress', '2007-06-14 16:48:46', 'jennifer', 106, NULL, NULL, NULL),
(4227093, 'In Vitro', 'NCL', 'The objective of this study was to determine if AF1, AF3, DF1, or C3 influence reactive oxygen species (ROS) generation in porcine renal proximal tubule (LLC-PK1) and human hepatocarcinoma cells (Hep G2) cell lines.\r\n\r\nReactive oxygen species were measured as described in the Hepatocyte Primary ROS Assay (GTA-7). Briefly, test materials were solubilized via sonication and diluted to the 0.004-1 mg/mL test concentrations in cell culture media. LLC-PK1 cells and Sprague Dawley primary hepatocytes were plated in 96-well, microtiter plate format. Cells were preincubated for 24 hours prior to initial staining, reaching an approximate confluence of 80%. Cells were then incubated in the dark for 30 minutes at 37&#730;C with 40 &#61549;M DCFH, the redox active dye. The wells were then washed with media, and treated with test samples in the dark. Microtiter plate fluorescence was measured at multiple times from 0-4 hours on a spectrophotometer at excitation 485 nm and emission 530 nm. \r\n\r\nResults\r\nAF3 was found to fluoresce at the assay wavelengths, and due to this interference was not included in the ROS assay. Treatment of LLC-PK1 cells with AF1 and C3 resulted in a supralinear dose-responsive increase in ROS generation (Figure 17A and C). Treatment of LLC-PK1 cells with DF1, however, resulted in a dose-responsive decrease in ROS generation (Figure 17B). \r\n\r\nTreatment of SD primary hepatocytes with AF1 and C3 resulted in a dose responsive increase in ROS generation (Figure18A and C). Treatment SD primary hepatocytes with DF1, however, resulted in a dose responsive decrease in ROS generation.\r\n', 'C-60 052007 ROS', 'Oxidative Stress', 'OxidativeStress', '2007-06-14 16:52:41', 'jennifer', 106, NULL, NULL, NULL),
(4227094, 'Physical', 'NCL', 'Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', 'MK 0707', 'Size', 'Size', '2008-01-24 10:24:01', 'jennifer', NULL, 1507328, NULL, NULL),
(4227095, 'Physical', 'NCL', 'The hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using batch-mode DLS at 25Â° C. Samples were diluted either in water, 10 mM NaCl, PBS, or saline (154 mM NaCl) to give final concentrations of 1 mg/mL. A minimum of seven measurements were taken for each sample. ', 'MK Solvent 0707', 'Size', 'Size', '2007-07-13 10:33:32', 'jennifer', NULL, 1507328, NULL, NULL),
(4227096, 'Physical', 'NCL', 'The effect of concentration on size was studied via batch-mode DLS measurements at 25 Â°C. Samples were diluted in saline (154 mM NaCl) to give a 0.1, 1, and 10 mg/mL final concentrations of NCL48-1 and NCL49-1, and 0.25, 2.5, and 25 mg/mL final concentrations of NCL48-2 and NCL49-2. A minimum of ten measurements were taken for each sample.  ', 'MK Conc 0707', 'Size', 'Size', '2007-07-13 10:36:55', 'jennifer', NULL, 1507328, NULL, NULL),
(4227097, 'Physical', 'NCL', 'A folded capillary flow cell was used for zeta potential measurement at a voltage of 100 V. \r\nThe zeta potential of NCL48-1 and NCL49-1 at 25Â° C was measured at two different concentrations. Samples were diluted in 10 mM NaCl to give a 0.1 and 1 mg/mL final concentration. A minimum of ten measurements were taken for each sample. Samples of NCL48-2 and NCL49-2 were diluted in 10 mM NaCl to give a 1 mg/mL final concentration.\r\n', 'MK 0707', 'Surface', 'Surface', '2007-07-20 10:16:53', 'jennifer', NULL, NULL, NULL, NULL),
(4227098, 'In Vitro', 'NCL', 'The objective of this study was to determine the cytotoxicity of the ghost liposome (NCL48-1) and ceramide liposome (NCL49-1) in human hepatocarcinoma (Hep G2) cells.\r\n\r\nCytotoxicity was determined as described in the NCL method, Hep G2 Hepatocarcinoma Assay (GTA-2). Briefly, test materials were diluted to the desired assay concentrations in cell culture media (0.004-1.0 mg/mL). Cells were plated in 96-well, microtiter plate format. Cells were preincubated for 24h prior to test material addition, reaching an approximate confluence of 80%. Cells were then exposed to test material for 6, 24, and 48h in the dark, and cytotoxicity was determined using the MTT cell viability and LDH membrane integrity assays. In order to estimate IC50 values, the MTT and LDH cytotoxicity assay dose-response curves were fit to a  sigmoidal Hill equation, E=Emax C&#61543;/C&#61543;+IC50&#61543;), using nonlinear regression analysis (WinNonlin, Pharsight Corp., Mountain View, CA). \r\n', 'MK 0707', 'Cell Viability', 'CellViability', '2007-07-20 10:39:37', 'jennifer', NULL, NULL, NULL, NULL),
(4227099, 'In Vitro', 'NCL', 'The objective of this study was to determine the cytotoxicity of the ghost liposome (NCL48-1) and ceramide liposome (NCL49-1) in human hepatocarcinoma (Hep G2) cells.\r\n\r\nCytotoxicity was determined as described in the NCL method, Hep G2 Hepatocarcinoma Assay (GTA-2). Briefly, test materials were diluted to the desired assay concentrations in cell culture media (0.004-1.0 mg/mL). Cells were plated in 96-well, microtiter plate format. Cells were preincubated for 24h prior to test material addition, reaching an approximate confluence of 80%. Cells were then exposed to test material for 6, 24, and 48h in the dark, and cytotoxicity was determined using the MTT cell viability and LDH membrane integrity assays. In order to estimate IC50 values, the MTT and LDH cytotoxicity assay dose-response curves were fit to a  sigmoidal Hill equation, E=Emax C&#61543;/C&#61543;+IC50&#61543;), using nonlinear regression analysis (WinNonlin, Pharsight Corp., Mountain View, CA)\r\n', 'MK 0707', 'Cell Viability', 'CellViability', '2007-07-20 10:43:57', 'jennifer', NULL, NULL, NULL, NULL),
(4227100, 'In Vitro', 'NCL', 'The objective of this experiment was to evaluate liposome effects on integrity of red blood cells. NCL protocol ITA-1 was followed. Both nanoparticle formulations were tested at two concentrations: high (1mg/mL) and low (0.2 mg/mL for NCL48 and 0.0625 mg/mL for NCL49).', 'MK 0707', 'Hemolysis', 'Hemolysis', '2007-07-20 10:52:55', 'jennifer', NULL, NULL, NULL, NULL),
(4423680, 'In Vitro', 'NCL', 'The objective of this study was to evaluate four fullerene derivatives: AF1, AF3, DF1, and C3 in terms of their effects on the integrity of red blood cells. Stock solutions with theoretical concentration of 1mg/mL were used for each C60 derivative. The resulting final nanoparticle concentration was 125 ÂƒÃg/mL . NCL protocol ITA-1 was followed. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively.', 'copy_091307075016168', 'Hemolysis', 'Hemolysis', '2007-09-13 07:50:15', 'jennifer', 108, NULL, NULL, NULL),
(4423681, 'In Vitro', 'NCL', 'The objective of this study was to evaluate four fullerene derivatives: AF1, AF3, DF1, and C3 in terms of their effects on the integrity of red blood cells. Stock solutions with theoretical concentration of 1mg/mL were used for each C60 derivative. The resulting final nanoparticle concentration was 125 ÂƒÃg/mL . NCL protocol ITA-1 was followed. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively.', 'copy_091307075016181', 'Hemolysis', 'Hemolysis', '2007-09-13 07:50:15', 'jennifer', 108, NULL, NULL, NULL),
(4423682, 'In Vitro', 'NCL', 'The objective of this study was to evaluate four fullerene derivatives: AF1, AF3, DF1, and C3 in terms of their effects on the integrity of red blood cells. Stock solutions with theoretical concentration of 1mg/mL were used for each C60 derivative. The resulting final nanoparticle concentration was 125 ÂƒÃg/mL . NCL protocol ITA-1 was followed. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively.', 'copy_091307075016193', 'Hemolysis', 'Hemolysis', '2007-09-13 07:50:15', 'jennifer', 108, NULL, NULL, NULL),
(4423683, 'In Vitro', 'NCL', 'The objective of this study was to evaluate four fullerene derivatives: AF1, AF3, DF1, and C3 in terms of their effects on the integrity of red blood cells. Stock solutions with theoretical concentration of 1mg/mL were used for each C60 derivative. The resulting final nanoparticle concentration was 125 ÂƒÃg/mL . NCL protocol ITA-1 was followed. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively.', 'copy_091307075016206', 'Hemolysis', 'Hemolysis', '2007-09-13 07:50:15', 'jennifer', 108, NULL, NULL, NULL),
(4587520, 'In Vitro', 'NCL', '', 'C-60 012008', 'Cell Viability', 'CellViability', '2008-01-24 09:36:04', 'jennifer', 101, NULL, NULL, NULL),
(4587521, 'Physical', 'NCL', 'Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', 'copy_012408101908159', 'Size', 'Size', '2008-01-24 10:19:07', 'jennifer', NULL, 4653056, NULL, NULL),
(4587522, 'Physical', 'NCL', 'Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', 'copy_012408102401230', 'Size', 'Size', '2008-01-24 10:24:01', 'jennifer', NULL, 4653057, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `composing_element`
--

CREATE TABLE IF NOT EXISTS `composing_element` (
  `composing_element_pk_id` bigint(20) NOT NULL,
  `element_type` varchar(100) default NULL,
  `chemical_name` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `characterization_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  PRIMARY KEY  (`composing_element_pk_id`),
  KEY `sys_c00246831` (`characterization_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `composing_element`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_hist_composing_element`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_hist_composing_element` BEFORE UPDATE ON `cananolab`.`composing_element`
 FOR EACH ROW begin
   if new.characterization_pk_id is null then
       insert into history_composing_element
        ( composing_element_pk_id, 
          element_type, 
          chemical_name, 
          description, 
          characterization_pk_id, 
          list_index, 
          deleted_date )
       values
        (old.composing_element_pk_id, 
         old.element_type, 
         old.chemical_name, 
         old.description, 
         old.characterization_pk_id, 
         old.list_index, 
         sysdate() );
   else 
      delete from history_composing_element where composing_element_pk_id=old.composing_element_pk_id;
   end if;
end
//
DELIMITER ;

--
-- Dumping data for table `composing_element`
--

INSERT INTO `composing_element` (`composing_element_pk_id`, `element_type`, `chemical_name`, `description`, `characterization_pk_id`, `list_index`) VALUES
(1409024, 'core', 'Diaminobutane (DAB)', NULL, 1376256, 0),
(1409025, 'core', 'Diaminobutane (DAB)', NULL, 1376263, 0),
(1409026, 'core', 'Diaminobutane (DAB)', NULL, 1376268, 0),
(1409027, 'core', 'Diaminobutane (DAB)', NULL, 1376281, 0),
(1409028, 'core', 'Diaminobutane (DAB)', NULL, 1376283, 0),
(1409029, 'core', 'Diaminobutane (DAB)', NULL, 1376285, 0),
(2064384, 'core', NULL, NULL, 1867783, 0),
(3506176, NULL, 'carboxyl', NULL, 3473408, 0);

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE IF NOT EXISTS `contact` (
  `contact_pk_id` bigint(20) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `title` varchar(100) default NULL,
  `phone_number` varchar(15) default NULL,
  `email` varchar(100) default NULL,
  `update_date` datetime NOT NULL,
  `middle_name` varchar(100) default NULL,
  `fax` varchar(20) default NULL,
  `address` varchar(200) default NULL,
  `city` varchar(100) default NULL,
  `state` varchar(100) default NULL,
  `country` varchar(100) default NULL,
  `postal_code` varchar(10) default NULL,
  `pi_name` varchar(200) default NULL,
  PRIMARY KEY  (`contact_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contact`
--


-- --------------------------------------------------------

--
-- Table structure for table `container_storage_location`
--

CREATE TABLE IF NOT EXISTS `container_storage_location` (
  `sample_container_pk_id` bigint(20) NOT NULL,
  `storage_pk_id` bigint(20) NOT NULL,
  KEY `sys_c00246773` (`sample_container_pk_id`),
  KEY `sys_c00246774` (`storage_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `container_storage_location`
--

INSERT INTO `container_storage_location` (`sample_container_pk_id`, `storage_pk_id`) VALUES
(950274, 2),
(950275, 2),
(950276, 2),
(950277, 2),
(950278, 2),
(950279, 2),
(950280, 2),
(950281, 1),
(950282, 1),
(950283, 2),
(950284, 2),
(950285, 2),
(950286, 2),
(950287, 2),
(557056, 2),
(557056, 4),
(557057, 2),
(557057, 4),
(557058, 2),
(557058, 4),
(557059, 2),
(557059, 4),
(950272, 2),
(950273, 2);

-- --------------------------------------------------------

--
-- Table structure for table `csm_application`
--

CREATE TABLE IF NOT EXISTS `csm_application` (
  `application_id` bigint(20) NOT NULL auto_increment,
  `application_name` varchar(100) NOT NULL,
  `application_description` varchar(200) NOT NULL,
  `declarative_flag` tinyint(1) default NULL,
  `active_flag` tinyint(1) NOT NULL,
  `update_date` date NOT NULL,
  PRIMARY KEY  (`application_id`),
  UNIQUE KEY `uq_application_name` (`application_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `csm_application`
--

INSERT INTO `csm_application` (`application_id`, `application_name`, `application_description`, `declarative_flag`, `active_flag`, `update_date`) VALUES
(1, 'caNanoLab-upt', 'cancer Nanotechnology Laboratory Analysis Bench UPT', 0, 0, '2006-05-19'),
(2, 'caNanoLab', 'cancer Nanotechnology Laboratory Analysis Bench', 0, 0, '2006-05-19');

-- --------------------------------------------------------

--
-- Table structure for table `csm_group`
--

CREATE TABLE IF NOT EXISTS `csm_group` (
  `group_id` bigint(20) NOT NULL auto_increment,
  `group_name` varchar(100) NOT NULL,
  `group_desc` varchar(200) default NULL,
  `update_date` date NOT NULL,
  `application_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`group_id`),
  UNIQUE KEY `uq_group_group_name` (`application_id`,`group_name`),
  KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=58 ;

--
-- Dumping data for table `csm_group`
--

INSERT INTO `csm_group` (`group_id`, `group_name`, `group_desc`, `update_date`, `application_id`) VALUES
(6, 'NCL_PI', 'NCL principal investigators', '2006-07-14', 2),
(7, 'NCL_Researcher', 'NCL researcher', '2006-07-14', 2),
(8, 'CCNE_Researcher', 'Researchers at CCNEs', '2006-07-14', 2),
(9, 'NCL_Administrator', 'NCL administrator', '2006-07-31', 2),
(11, 'Public', 'caBIG and public', '2006-08-04', 2),
(12, 'FDA_Researcher', 'Researchers at FDA', '2006-09-12', 2),
(13, 'NIST_Researcher', 'Researchers at NIST', '2006-09-12', 2),
(14, 'Other_Research_Partners', 'Researchers at partners other than NIST and FDA', '2006-09-12', 2),
(50, 'NCL', NULL, '2006-12-14', 2),
(54, 'C-Sixty (CNI)', NULL, '2007-05-17', 2),
(55, 'NCI_PI', NULL, '2007-05-17', 2),
(57, 'Mark Kester PSU', NULL, '2007-06-08', 2);

-- --------------------------------------------------------

--
-- Table structure for table `csm_pg_pe`
--

CREATE TABLE IF NOT EXISTS `csm_pg_pe` (
  `pg_pe_id` bigint(20) NOT NULL auto_increment,
  `protection_group_id` bigint(20) NOT NULL,
  `protection_element_id` bigint(20) NOT NULL,
  `update_date` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`pg_pe_id`),
  UNIQUE KEY `uq_protection_group_protection_element_protection_group_id` (`protection_element_id`,`protection_group_id`),
  KEY `idx_protection_element_id` (`protection_element_id`),
  KEY `idx_protection_group_id` (`protection_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=496 ;

--
-- Dumping data for table `csm_pg_pe`
--

INSERT INTO `csm_pg_pe` (`pg_pe_id`, `protection_group_id`, `protection_element_id`, `update_date`) VALUES
(53, 51, 51, '2006-12-08'),
(55, 52, 52, '2006-12-11'),
(57, 53, 53, '2006-12-11'),
(59, 54, 54, '2006-12-11'),
(61, 55, 55, '2006-12-11'),
(63, 56, 56, '2006-12-11'),
(65, 57, 57, '2006-12-11'),
(67, 58, 58, '2006-12-11'),
(69, 59, 59, '2006-12-11'),
(71, 60, 60, '2006-12-11'),
(73, 61, 61, '2006-12-11'),
(75, 62, 62, '2006-12-11'),
(77, 63, 63, '2006-12-11'),
(79, 64, 64, '2006-12-11'),
(81, 65, 65, '2006-12-11'),
(83, 66, 66, '2006-12-11'),
(85, 67, 67, '2006-12-11'),
(87, 68, 68, '2006-12-11'),
(89, 69, 69, '2006-12-11'),
(91, 70, 70, '2006-12-11'),
(93, 71, 71, '2006-12-11'),
(95, 72, 72, '2006-12-11'),
(97, 73, 73, '2006-12-11'),
(99, 74, 74, '2006-12-11'),
(101, 75, 75, '2006-12-11'),
(103, 76, 76, '2006-12-11'),
(105, 77, 77, '2006-12-11'),
(107, 78, 78, '2006-12-11'),
(109, 79, 79, '2006-12-11'),
(111, 80, 80, '2006-12-11'),
(113, 81, 81, '2006-12-11'),
(115, 82, 82, '2006-12-11'),
(117, 83, 83, '2006-12-11'),
(119, 84, 84, '2006-12-11'),
(121, 85, 85, '2006-12-11'),
(123, 86, 86, '2006-12-11'),
(125, 87, 87, '2006-12-11'),
(127, 88, 88, '2006-12-11'),
(129, 89, 89, '2006-12-11'),
(131, 90, 90, '2006-12-11'),
(133, 91, 91, '2006-12-11'),
(135, 92, 92, '2006-12-11'),
(137, 93, 93, '2006-12-11'),
(139, 94, 94, '2006-12-11'),
(141, 95, 95, '2006-12-11'),
(143, 96, 96, '2006-12-11'),
(145, 97, 97, '2006-12-11'),
(147, 98, 98, '2006-12-11'),
(149, 99, 99, '2006-12-11'),
(151, 100, 100, '2006-12-11'),
(153, 101, 101, '2006-12-11'),
(155, 102, 102, '2006-12-11'),
(157, 103, 103, '2006-12-11'),
(159, 104, 104, '2006-12-11'),
(161, 105, 105, '2006-12-11'),
(163, 106, 106, '2006-12-11'),
(165, 107, 107, '2006-12-11'),
(167, 108, 108, '2006-12-11'),
(169, 109, 109, '2006-12-13'),
(171, 110, 110, '2006-12-13'),
(173, 111, 111, '2006-12-13'),
(175, 112, 112, '2006-12-13'),
(177, 113, 113, '2006-12-13'),
(179, 114, 114, '2006-12-13'),
(181, 115, 115, '2006-12-13'),
(183, 116, 116, '2006-12-13'),
(185, 117, 117, '2006-12-13'),
(187, 118, 118, '2006-12-13'),
(189, 119, 119, '2006-12-13'),
(191, 120, 120, '2006-12-13'),
(193, 121, 121, '2006-12-13'),
(195, 122, 122, '2006-12-13'),
(197, 123, 123, '2006-12-13'),
(199, 124, 124, '2006-12-13'),
(203, 126, 126, '2006-12-15'),
(205, 127, 127, '2006-12-20'),
(217, 133, 133, '2006-12-21'),
(219, 134, 134, '2006-12-21'),
(221, 135, 135, '2007-04-11'),
(223, 136, 136, '2007-04-11'),
(225, 137, 137, '2007-04-11'),
(227, 138, 138, '2007-04-11'),
(229, 139, 139, '2007-04-11'),
(231, 140, 140, '2007-04-11'),
(233, 141, 141, '2007-04-11'),
(235, 142, 142, '2007-04-11'),
(237, 143, 143, '2007-04-11'),
(239, 144, 144, '2007-04-11'),
(241, 145, 145, '2007-04-11'),
(243, 146, 146, '2007-04-11'),
(245, 147, 147, '2007-04-11'),
(247, 148, 148, '2007-04-11'),
(249, 149, 149, '2007-04-12'),
(251, 150, 150, '2007-04-12'),
(253, 151, 151, '2007-04-12'),
(255, 152, 152, '2007-04-12'),
(257, 153, 153, '2007-04-12'),
(259, 154, 154, '2007-04-12'),
(261, 155, 155, '2007-04-12'),
(263, 156, 156, '2007-04-12'),
(265, 157, 157, '2007-04-12'),
(267, 158, 158, '2007-04-12'),
(269, 159, 159, '2007-04-12'),
(271, 160, 160, '2007-04-12'),
(273, 161, 161, '2007-04-12'),
(275, 162, 162, '2007-04-12'),
(277, 163, 163, '2007-04-13'),
(279, 164, 164, '2007-04-13'),
(281, 165, 165, '2007-04-13'),
(283, 166, 166, '2007-04-13'),
(285, 167, 167, '2007-04-13'),
(287, 168, 168, '2007-04-13'),
(289, 169, 169, '2007-04-13'),
(291, 170, 170, '2007-04-13'),
(293, 171, 171, '2007-04-13'),
(295, 172, 172, '2007-04-13'),
(297, 173, 173, '2007-04-13'),
(299, 174, 174, '2007-04-13'),
(301, 175, 175, '2007-04-13'),
(303, 176, 176, '2007-04-13'),
(305, 177, 177, '2007-04-13'),
(307, 178, 178, '2007-04-13'),
(309, 179, 179, '2007-04-13'),
(311, 180, 180, '2007-04-13'),
(313, 181, 181, '2007-04-13'),
(315, 182, 182, '2007-04-13'),
(317, 183, 183, '2007-04-13'),
(319, 184, 184, '2007-04-13'),
(321, 185, 185, '2007-04-13'),
(323, 186, 186, '2007-04-13'),
(325, 187, 187, '2007-04-13'),
(327, 188, 188, '2007-04-13'),
(329, 189, 189, '2007-04-13'),
(331, 190, 190, '2007-04-13'),
(334, 192, 192, '2007-05-17'),
(336, 193, 193, '2007-05-17'),
(338, 194, 194, '2007-05-17'),
(340, 195, 195, '2007-05-17'),
(342, 196, 196, '2007-05-17'),
(344, 197, 197, '2007-05-31'),
(346, 198, 198, '2007-05-31'),
(348, 199, 199, '2007-05-31'),
(350, 200, 200, '2007-05-31'),
(352, 201, 201, '2007-05-31'),
(354, 202, 202, '2007-05-31'),
(356, 203, 203, '2007-05-31'),
(358, 204, 204, '2007-05-31'),
(360, 205, 205, '2007-05-31'),
(362, 206, 206, '2007-05-31'),
(364, 207, 207, '2007-05-31'),
(366, 208, 208, '2007-05-31'),
(368, 209, 209, '2007-05-31'),
(370, 210, 210, '2007-05-31'),
(372, 211, 211, '2007-05-31'),
(374, 212, 212, '2007-05-31'),
(376, 213, 213, '2007-05-31'),
(378, 214, 214, '2007-05-31'),
(380, 215, 215, '2007-05-31'),
(382, 216, 216, '2007-06-08'),
(384, 217, 217, '2007-06-08'),
(386, 218, 218, '2007-06-08'),
(388, 219, 219, '2007-06-08'),
(390, 220, 220, '2007-06-08'),
(392, 221, 221, '2007-06-08'),
(394, 222, 222, '2007-06-08'),
(396, 223, 223, '2007-06-14'),
(398, 224, 224, '2007-06-14'),
(400, 225, 225, '2007-06-14'),
(402, 226, 226, '2007-06-14'),
(404, 227, 227, '2007-06-14'),
(406, 228, 228, '2007-06-14'),
(408, 229, 229, '2007-06-14'),
(410, 230, 230, '2007-07-13'),
(412, 231, 231, '2007-07-13'),
(414, 232, 232, '2007-07-13'),
(416, 233, 233, '2007-07-20'),
(418, 234, 234, '2007-07-20'),
(420, 235, 235, '2007-07-20'),
(422, 236, 236, '2007-07-20'),
(426, 238, 238, '2007-08-31'),
(428, 239, 239, '2007-08-31'),
(430, 240, 240, '2007-08-31'),
(432, 241, 241, '2007-08-31'),
(434, 242, 242, '2007-08-31'),
(436, 243, 243, '2007-08-31'),
(438, 244, 244, '2007-08-31'),
(440, 245, 245, '2007-08-31'),
(442, 246, 246, '2007-08-31'),
(444, 247, 247, '2007-08-31'),
(446, 248, 248, '2007-08-31'),
(448, 249, 249, '2007-08-31'),
(450, 250, 250, '2007-08-31'),
(452, 251, 251, '2007-08-31'),
(454, 252, 252, '2007-08-31'),
(456, 253, 253, '2007-08-31'),
(458, 254, 254, '2007-08-31'),
(460, 255, 255, '2007-08-31'),
(462, 256, 256, '2007-08-31'),
(464, 257, 207, '2007-09-13'),
(466, 258, 257, '2007-09-13'),
(468, 259, 258, '2007-09-13'),
(470, 260, 259, '2007-09-13'),
(472, 261, 260, '2007-09-13'),
(474, 262, 261, '2007-11-16'),
(476, 263, 262, '2007-11-16'),
(478, 264, 263, '2007-11-16'),
(480, 265, 264, '2007-11-16'),
(482, 266, 265, '2007-11-16'),
(484, 267, 266, '2007-11-16'),
(486, 268, 267, '2007-11-16'),
(487, 269, 268, '2008-01-16'),
(488, 270, 269, '2008-01-16'),
(489, 271, 270, '2008-01-16'),
(490, 272, 271, '2008-01-16'),
(491, 273, 272, '2008-01-24'),
(492, 274, 273, '2008-01-24'),
(493, 275, 274, '2008-01-24'),
(494, 276, 275, '2008-01-24'),
(495, 277, 276, '2008-01-24');

-- --------------------------------------------------------

--
-- Table structure for table `csm_privilege`
--

CREATE TABLE IF NOT EXISTS `csm_privilege` (
  `privilege_id` bigint(20) NOT NULL auto_increment,
  `privilege_name` varchar(100) NOT NULL,
  `privilege_description` varchar(200) default NULL,
  `update_date` date NOT NULL,
  PRIMARY KEY  (`privilege_id`),
  UNIQUE KEY `uq_privilege_name` (`privilege_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `csm_privilege`
--

INSERT INTO `csm_privilege` (`privilege_id`, `privilege_name`, `privilege_description`, `update_date`) VALUES
(1, 'CREATE', 'This privilege grants permission to a user to create an entity.', '2006-05-19'),
(2, 'ACCESS', 'This privilege allows a user to access a particular resource.', '2006-05-19'),
(3, 'READ', 'This privilege permits the user to read data from a file, URL, socket, database, or an object.', '2006-05-19'),
(4, 'WRITE', 'This privilege allows a user to write data to a file, URL, socket, database, or object.', '2006-05-19'),
(5, 'UPDATE', 'This privilege grants permission at an entity level and signifies that the user is allowed to update and modify data for a particular entity.', '2006-05-19'),
(6, 'DELETE', 'This privilege permits a user to delete a logical entity.', '2006-05-19'),
(7, 'EXECUTE', 'This privilege allows a user to execute a particular resource.', '2006-05-19');

-- --------------------------------------------------------

--
-- Table structure for table `csm_protection_element`
--

CREATE TABLE IF NOT EXISTS `csm_protection_element` (
  `protection_element_id` bigint(20) NOT NULL auto_increment,
  `protection_element_name` varchar(100) NOT NULL,
  `protection_element_description` varchar(200) default NULL,
  `object_id` varchar(100) NOT NULL,
  `attribute` varchar(100) default NULL,
  `protection_element_type_id` decimal(10,0) default NULL,
  `application_id` bigint(20) NOT NULL,
  `update_date` date NOT NULL,
  PRIMARY KEY  (`protection_element_id`),
  UNIQUE KEY `uq_pe_pe_name_attribute_app_id` (`object_id`,`attribute`,`application_id`),
  KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=280 ;

--
-- Dumping data for table `csm_protection_element`
--

INSERT INTO `csm_protection_element` (`protection_element_id`, `protection_element_name`, `protection_element_description`, `object_id`, `attribute`, `protection_element_type_id`, `application_id`, `update_date`) VALUES
(1, 'caNanoLab-upt', 'cancer Nanotechnology Laboratory Analysis Bench UPT', 'caNanoLab-upt', NULL, NULL, 1, '2006-05-19'),
(2, 'caNanoLab', 'cancer Nanotechnology Laboratory Analysis Bench', 'caNanoLab', NULL, NULL, 1, '2006-05-19'),
(51, '1081344', NULL, '1081344', NULL, NULL, 2, '2006-12-08'),
(52, 'NCL-20-1', NULL, 'NCL-20-1', NULL, NULL, 2, '2006-12-11'),
(53, '1540096', NULL, '1540096', NULL, NULL, 2, '2006-12-11'),
(54, '1540097', NULL, '1540097', NULL, NULL, 2, '2006-12-11'),
(55, '1540099', NULL, '1540099', NULL, NULL, 2, '2006-12-11'),
(56, '1540100', NULL, '1540100', NULL, NULL, 2, '2006-12-11'),
(57, '1474564', NULL, '1474564', NULL, NULL, 2, '2006-12-11'),
(58, 'NCL-21-1', NULL, 'NCL-21-1', NULL, NULL, 2, '2006-12-11'),
(59, '1540105', NULL, '1540105', NULL, NULL, 2, '2006-12-11'),
(60, 'NCL-22-1', NULL, 'NCL-22-1', NULL, NULL, 2, '2006-12-11'),
(61, '1540111', NULL, '1540111', NULL, NULL, 2, '2006-12-11'),
(62, '1540115', NULL, '1540115', NULL, NULL, 2, '2006-12-11'),
(63, '1540117', NULL, '1540117', NULL, NULL, 2, '2006-12-11'),
(64, '1474569', NULL, '1474569', NULL, NULL, 2, '2006-12-11'),
(65, 'NCL-26-1', NULL, 'NCL-26-1', NULL, NULL, 2, '2006-12-11'),
(66, 'NCL-25-1', NULL, 'NCL-25-1', NULL, NULL, 2, '2006-12-11'),
(67, 'NCL-23-1', NULL, 'NCL-23-1', NULL, NULL, 2, '2006-12-11'),
(68, '1540118', NULL, '1540118', NULL, NULL, 2, '2006-12-11'),
(69, '1540119', NULL, '1540119', NULL, NULL, 2, '2006-12-11'),
(70, '1540120', NULL, '1540120', NULL, NULL, 2, '2006-12-11'),
(71, '1540121', NULL, '1540121', NULL, NULL, 2, '2006-12-11'),
(72, '1474574', NULL, '1474574', NULL, NULL, 2, '2006-12-11'),
(73, '1802240', NULL, '1802240', NULL, NULL, 2, '2006-12-11'),
(74, '1802241', NULL, '1802241', NULL, NULL, 2, '2006-12-11'),
(75, '1900544', NULL, '1900544', NULL, NULL, 2, '2006-12-11'),
(76, '1802243', NULL, '1802243', NULL, NULL, 2, '2006-12-11'),
(77, '1802244', NULL, '1802244', NULL, NULL, 2, '2006-12-11'),
(78, '1802245', NULL, '1802245', NULL, NULL, 2, '2006-12-11'),
(79, '1802246', NULL, '1802246', NULL, NULL, 2, '2006-12-11'),
(80, '1900545', NULL, '1900545', NULL, NULL, 2, '2006-12-11'),
(81, '1900546', NULL, '1900546', NULL, NULL, 2, '2006-12-11'),
(82, '1900547', NULL, '1900547', NULL, NULL, 2, '2006-12-11'),
(83, '1900548', NULL, '1900548', NULL, NULL, 2, '2006-12-11'),
(84, '1900549', NULL, '1900549', NULL, NULL, 2, '2006-12-11'),
(85, 'NCL-24-1', NULL, 'NCL-24-1', NULL, NULL, 2, '2006-12-11'),
(86, '1900550', NULL, '1900550', NULL, NULL, 2, '2006-12-11'),
(87, '1900551', NULL, '1900551', NULL, NULL, 2, '2006-12-11'),
(88, '1900552', NULL, '1900552', NULL, NULL, 2, '2006-12-11'),
(89, '1900554', NULL, '1900554', NULL, NULL, 2, '2006-12-11'),
(90, '1900555', NULL, '1900555', NULL, NULL, 2, '2006-12-11'),
(91, '1900556', NULL, '1900556', NULL, NULL, 2, '2006-12-11'),
(92, '1802258', NULL, '1802258', NULL, NULL, 2, '2006-12-11'),
(93, '1900557', NULL, '1900557', NULL, NULL, 2, '2006-12-11'),
(94, '1802260', NULL, '1802260', NULL, NULL, 2, '2006-12-11'),
(95, '1802261', NULL, '1802261', NULL, NULL, 2, '2006-12-11'),
(96, '1900558', NULL, '1900558', NULL, NULL, 2, '2006-12-11'),
(97, '1802263', NULL, '1802263', NULL, NULL, 2, '2006-12-11'),
(98, '1900559', NULL, '1900559', NULL, NULL, 2, '2006-12-11'),
(99, '1802265', NULL, '1802265', NULL, NULL, 2, '2006-12-11'),
(100, '1900561', NULL, '1900561', NULL, NULL, 2, '2006-12-11'),
(101, '1900562', NULL, '1900562', NULL, NULL, 2, '2006-12-11'),
(102, '1900563', NULL, '1900563', NULL, NULL, 2, '2006-12-11'),
(103, '1900564', NULL, '1900564', NULL, NULL, 2, '2006-12-11'),
(104, '1900565', NULL, '1900565', NULL, NULL, 2, '2006-12-11'),
(105, '1900566', NULL, '1900566', NULL, NULL, 2, '2006-12-11'),
(106, '1900567', NULL, '1900567', NULL, NULL, 2, '2006-12-11'),
(107, '1900568', NULL, '1900568', NULL, NULL, 2, '2006-12-11'),
(108, '1900569', NULL, '1900569', NULL, NULL, 2, '2006-12-11'),
(109, '2457600', NULL, '2457600', NULL, NULL, 2, '2006-12-13'),
(110, '2457601', NULL, '2457601', NULL, NULL, 2, '2006-12-13'),
(111, '2457602', NULL, '2457602', NULL, NULL, 2, '2006-12-13'),
(112, '2457603', NULL, '2457603', NULL, NULL, 2, '2006-12-13'),
(113, '2457604', NULL, '2457604', NULL, NULL, 2, '2006-12-13'),
(114, '2293765', NULL, '2293765', NULL, NULL, 2, '2006-12-13'),
(115, '2457605', NULL, '2457605', NULL, NULL, 2, '2006-12-13'),
(116, '2457606', NULL, '2457606', NULL, NULL, 2, '2006-12-13'),
(117, '2457607', NULL, '2457607', NULL, NULL, 2, '2006-12-13'),
(118, '2457608', NULL, '2457608', NULL, NULL, 2, '2006-12-13'),
(119, '2457609', NULL, '2457609', NULL, NULL, 2, '2006-12-13'),
(120, '2457610', NULL, '2457610', NULL, NULL, 2, '2006-12-13'),
(121, '2457611', NULL, '2457611', NULL, NULL, 2, '2006-12-13'),
(122, '2457612', NULL, '2457612', NULL, NULL, 2, '2006-12-13'),
(123, '2457613', NULL, '2457613', NULL, NULL, 2, '2006-12-13'),
(124, '2457614', NULL, '2457614', NULL, NULL, 2, '2006-12-13'),
(126, '2981888', NULL, '2981888', NULL, NULL, 2, '2006-12-15'),
(127, '3178496', NULL, '3178496', NULL, NULL, 2, '2006-12-20'),
(133, 'NCL-61', NULL, 'NCL-61', NULL, NULL, 2, '2006-12-21'),
(134, '3538944', NULL, '3538944', NULL, NULL, 2, '2006-12-21'),
(135, '1540107', NULL, '1540107', NULL, NULL, 2, '2007-04-11'),
(136, '3801088', NULL, '3801088', NULL, NULL, 2, '2007-04-11'),
(137, '3801089', NULL, '3801089', NULL, NULL, 2, '2007-04-11'),
(138, '3801090', NULL, '3801090', NULL, NULL, 2, '2007-04-11'),
(139, '3801091', NULL, '3801091', NULL, NULL, 2, '2007-04-11'),
(140, '3801092', NULL, '3801092', NULL, NULL, 2, '2007-04-11'),
(141, '3801093', NULL, '3801093', NULL, NULL, 2, '2007-04-11'),
(142, '3801094', NULL, '3801094', NULL, NULL, 2, '2007-04-11'),
(143, '3801095', NULL, '3801095', NULL, NULL, 2, '2007-04-11'),
(144, '3801096', NULL, '3801096', NULL, NULL, 2, '2007-04-11'),
(145, '3801097', NULL, '3801097', NULL, NULL, 2, '2007-04-11'),
(146, '3801098', NULL, '3801098', NULL, NULL, 2, '2007-04-11'),
(147, '3801099', NULL, '3801099', NULL, NULL, 2, '2007-04-11'),
(148, '3702797', NULL, '3702797', NULL, NULL, 2, '2007-04-11'),
(149, '3801100', NULL, '3801100', NULL, NULL, 2, '2007-04-12'),
(150, '3801101', NULL, '3801101', NULL, NULL, 2, '2007-04-12'),
(151, '3801102', NULL, '3801102', NULL, NULL, 2, '2007-04-12'),
(152, '1540116', NULL, '1540116', NULL, NULL, 2, '2007-04-12'),
(153, '3801103', NULL, '3801103', NULL, NULL, 2, '2007-04-12'),
(154, '3801104', NULL, '3801104', NULL, NULL, 2, '2007-04-12'),
(155, '3801105', NULL, '3801105', NULL, NULL, 2, '2007-04-12'),
(156, '1540113', NULL, '1540113', NULL, NULL, 2, '2007-04-12'),
(157, '1540114', NULL, '1540114', NULL, NULL, 2, '2007-04-12'),
(158, '1540103', NULL, '1540103', NULL, NULL, 2, '2007-04-12'),
(159, '1540104', NULL, '1540104', NULL, NULL, 2, '2007-04-12'),
(160, '3801106', NULL, '3801106', NULL, NULL, 2, '2007-04-12'),
(161, '1900553', NULL, '1900553', NULL, NULL, 2, '2007-04-12'),
(162, '3801107', NULL, '3801107', NULL, NULL, 2, '2007-04-12'),
(163, '3801108', NULL, '3801108', NULL, NULL, 2, '2007-04-13'),
(164, '3801109', NULL, '3801109', NULL, NULL, 2, '2007-04-13'),
(165, '3801110', NULL, '3801110', NULL, NULL, 2, '2007-04-13'),
(166, '3801111', NULL, '3801111', NULL, NULL, 2, '2007-04-13'),
(167, '3801112', NULL, '3801112', NULL, NULL, 2, '2007-04-13'),
(168, '3801113', NULL, '3801113', NULL, NULL, 2, '2007-04-13'),
(169, '3801114', NULL, '3801114', NULL, NULL, 2, '2007-04-13'),
(170, '3801115', NULL, '3801115', NULL, NULL, 2, '2007-04-13'),
(171, '3801116', NULL, '3801116', NULL, NULL, 2, '2007-04-13'),
(172, '3801117', NULL, '3801117', NULL, NULL, 2, '2007-04-13'),
(173, '3801118', NULL, '3801118', NULL, NULL, 2, '2007-04-13'),
(174, '3801119', NULL, '3801119', NULL, NULL, 2, '2007-04-13'),
(175, '3702824', NULL, '3702824', NULL, NULL, 2, '2007-04-13'),
(176, '3702825', NULL, '3702825', NULL, NULL, 2, '2007-04-13'),
(177, '3801120', NULL, '3801120', NULL, NULL, 2, '2007-04-13'),
(178, '3801121', NULL, '3801121', NULL, NULL, 2, '2007-04-13'),
(179, '3801122', NULL, '3801122', NULL, NULL, 2, '2007-04-13'),
(180, '3801123', NULL, '3801123', NULL, NULL, 2, '2007-04-13'),
(181, '3801124', NULL, '3801124', NULL, NULL, 2, '2007-04-13'),
(182, '3801126', NULL, '3801126', NULL, NULL, 2, '2007-04-13'),
(183, '3801125', NULL, '3801125', NULL, NULL, 2, '2007-04-13'),
(184, '3801127', NULL, '3801127', NULL, NULL, 2, '2007-04-13'),
(185, '3801128', NULL, '3801128', NULL, NULL, 2, '2007-04-13'),
(186, '3801129', NULL, '3801129', NULL, NULL, 2, '2007-04-13'),
(187, '3801130', NULL, '3801130', NULL, NULL, 2, '2007-04-13'),
(188, '3801131', NULL, '3801131', NULL, NULL, 2, '2007-04-13'),
(189, '3801132', NULL, '3801132', NULL, NULL, 2, '2007-04-13'),
(190, '3801133', NULL, '3801133', NULL, NULL, 2, '2007-04-13'),
(192, 'NCL-42', NULL, 'NCL-42', NULL, NULL, 2, '2007-05-17'),
(193, '4259840', NULL, '4259840', NULL, NULL, 2, '2007-05-17'),
(194, '4259841', NULL, '4259841', NULL, NULL, 2, '2007-05-17'),
(195, '4259842', NULL, '4259842', NULL, NULL, 2, '2007-05-17'),
(196, '4259843', NULL, '4259843', NULL, NULL, 2, '2007-05-17'),
(197, '4128772', NULL, '4128772', NULL, NULL, 2, '2007-05-31'),
(198, '4128773', NULL, '4128773', NULL, NULL, 2, '2007-05-31'),
(199, '4259844', NULL, '4259844', NULL, NULL, 2, '2007-05-31'),
(200, '4259845', NULL, '4259845', NULL, NULL, 2, '2007-05-31'),
(201, '4259846', NULL, '4259846', NULL, NULL, 2, '2007-05-31'),
(202, '4259847', NULL, '4259847', NULL, NULL, 2, '2007-05-31'),
(203, '4259848', NULL, '4259848', NULL, NULL, 2, '2007-05-31'),
(204, '4259849', NULL, '4259849', NULL, NULL, 2, '2007-05-31'),
(205, '4259850', NULL, '4259850', NULL, NULL, 2, '2007-05-31'),
(206, '4259851', NULL, '4259851', NULL, NULL, 2, '2007-05-31'),
(207, '4259852', NULL, '4259852', NULL, NULL, 2, '2007-05-31'),
(208, '4128783', NULL, '4128783', NULL, NULL, 2, '2007-05-31'),
(209, '4128784', NULL, '4128784', NULL, NULL, 2, '2007-05-31'),
(210, '4128785', NULL, '4128785', NULL, NULL, 2, '2007-05-31'),
(211, '4259853', NULL, '4259853', NULL, NULL, 2, '2007-05-31'),
(212, 'NCL-45', NULL, 'NCL-45', NULL, NULL, 2, '2007-05-31'),
(213, '4259854', NULL, '4259854', NULL, NULL, 2, '2007-05-31'),
(214, 'NCL-17', NULL, 'NCL-17', NULL, NULL, 2, '2007-05-31'),
(215, '4259855', NULL, '4259855', NULL, NULL, 2, '2007-05-31'),
(216, 'NCL-16', NULL, 'NCL-16', NULL, NULL, 2, '2007-06-08'),
(217, '4259856', NULL, '4259856', NULL, NULL, 2, '2007-06-08'),
(218, 'NCL-19', NULL, 'NCL-19', NULL, NULL, 2, '2007-06-08'),
(219, '4259857', NULL, '4259857', NULL, NULL, 2, '2007-06-08'),
(220, 'NCL-48', NULL, 'NCL-48', NULL, NULL, 2, '2007-06-08'),
(221, 'NCL-48-4', NULL, 'NCL-48-4', NULL, NULL, 2, '2007-06-08'),
(222, 'NCL-49', NULL, 'NCL-49', NULL, NULL, 2, '2007-06-08'),
(223, '4259858', NULL, '4259858', NULL, NULL, 2, '2007-06-14'),
(224, '4259859', NULL, '4259859', NULL, NULL, 2, '2007-06-14'),
(225, '4259860', NULL, '4259860', NULL, NULL, 2, '2007-06-14'),
(226, '4259861', NULL, '4259861', NULL, NULL, 2, '2007-06-14'),
(227, '4259862', NULL, '4259862', NULL, NULL, 2, '2007-06-14'),
(228, '4259863', NULL, '4259863', NULL, NULL, 2, '2007-06-14'),
(229, '4259864', NULL, '4259864', NULL, NULL, 2, '2007-06-14'),
(230, '4259865', NULL, '4259865', NULL, NULL, 2, '2007-07-13'),
(231, '4259866', NULL, '4259866', NULL, NULL, 2, '2007-07-13'),
(232, '4259867', NULL, '4259867', NULL, NULL, 2, '2007-07-13'),
(233, '4259868', NULL, '4259868', NULL, NULL, 2, '2007-07-20'),
(234, '4259869', NULL, '4259869', NULL, NULL, 2, '2007-07-20'),
(235, '4259870', NULL, '4259870', NULL, NULL, 2, '2007-07-20'),
(236, '4259871', NULL, '4259871', NULL, NULL, 2, '2007-07-20'),
(238, '101', NULL, '101', NULL, NULL, 2, '2007-08-31'),
(239, '102', NULL, '102', NULL, NULL, 2, '2007-08-31'),
(240, '103', NULL, '103', NULL, NULL, 2, '2007-08-31'),
(241, '104', NULL, '104', NULL, NULL, 2, '2007-08-31'),
(242, '105', NULL, '105', NULL, NULL, 2, '2007-08-31'),
(243, '106', NULL, '106', NULL, NULL, 2, '2007-08-31'),
(244, '107', NULL, '107', NULL, NULL, 2, '2007-08-31'),
(245, '108', NULL, '108', NULL, NULL, 2, '2007-08-31'),
(246, '109', NULL, '109', NULL, NULL, 2, '2007-08-31'),
(247, '110', NULL, '110', NULL, NULL, 2, '2007-08-31'),
(248, '111', NULL, '111', NULL, NULL, 2, '2007-08-31'),
(249, '112', NULL, '112', NULL, NULL, 2, '2007-08-31'),
(250, '113', NULL, '113', NULL, NULL, 2, '2007-08-31'),
(251, '114', NULL, '114', NULL, NULL, 2, '2007-08-31'),
(252, '115', NULL, '115', NULL, NULL, 2, '2007-08-31'),
(253, '116', NULL, '116', NULL, NULL, 2, '2007-08-31'),
(254, '117', NULL, '117', NULL, NULL, 2, '2007-08-31'),
(255, '118', NULL, '118', NULL, NULL, 2, '2007-08-31'),
(256, '119', NULL, '119', NULL, NULL, 2, '2007-08-31'),
(257, '4456448', NULL, '4456448', NULL, NULL, 2, '2007-09-13'),
(258, '4456449', NULL, '4456449', NULL, NULL, 2, '2007-09-13'),
(259, '4456450', NULL, '4456450', NULL, NULL, 2, '2007-09-13'),
(260, '4456451', NULL, '4456451', NULL, NULL, 2, '2007-09-13'),
(261, '4521984', NULL, '4521984', NULL, NULL, 2, '2007-11-16'),
(262, '4521985', NULL, '4521985', NULL, NULL, 2, '2007-11-16'),
(263, '4521986', NULL, '4521986', NULL, NULL, 2, '2007-11-16'),
(264, '4521987', NULL, '4521987', NULL, NULL, 2, '2007-11-16'),
(265, '4521988', NULL, '4521988', NULL, NULL, 2, '2007-11-16'),
(266, '4521989', NULL, '4521989', NULL, NULL, 2, '2007-11-16'),
(267, '4521990', NULL, '4521990', NULL, NULL, 2, '2007-11-16'),
(268, 'sample', '', 'sample', '', NULL, 2, '2008-01-16'),
(269, 'protocol', '', 'protocol', '', NULL, 2, '2008-01-16'),
(270, 'nanoparticle', '', 'nanoparticle', '', NULL, 2, '2008-01-16'),
(271, 'report', '', 'report', '', NULL, 2, '2008-01-16'),
(272, '4620288', NULL, '4620288', NULL, NULL, 2, '2008-01-24'),
(273, '4620291', NULL, '4620291', NULL, NULL, 2, '2008-01-24'),
(274, '4620292', NULL, '4620292', NULL, NULL, 2, '2008-01-24'),
(275, '4620293', NULL, '4620293', NULL, NULL, 2, '2008-01-24'),
(276, '4620294', NULL, '4620294', NULL, NULL, 2, '2008-01-24'),
(277, 'NCL-49-2', NULL, 'NCL-49-2', NULL, NULL, 2, '2008-01-24'),
(278, 'NCL-50-1', NULL, 'NCL-50-1', NULL, NULL, 2, '2008-01-24'),
(279, 'NCL-51-3', NULL, 'NCL-51-3', NULL, NULL, 2, '2008-01-24');

-- --------------------------------------------------------

--
-- Table structure for table `csm_protection_group`
--

CREATE TABLE IF NOT EXISTS `csm_protection_group` (
  `protection_group_id` bigint(20) NOT NULL auto_increment,
  `protection_group_name` varchar(100) NOT NULL,
  `protection_group_description` varchar(200) default NULL,
  `application_id` bigint(20) NOT NULL,
  `large_element_count_flag` tinyint(1) NOT NULL,
  `update_date` date NOT NULL,
  `parent_protection_group_id` bigint(20) default NULL,
  PRIMARY KEY  (`protection_group_id`),
  UNIQUE KEY `uq_protection_group_protection_group_name` (`application_id`,`protection_group_name`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_parent_protection_group_id` (`parent_protection_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=281 ;

--
-- Dumping data for table `csm_protection_group`
--

INSERT INTO `csm_protection_group` (`protection_group_id`, `protection_group_name`, `protection_group_description`, `application_id`, `large_element_count_flag`, `update_date`, `parent_protection_group_id`) VALUES
(51, '1081344', NULL, 2, 0, '2006-12-08', NULL),
(52, 'NCL-20-1', NULL, 2, 0, '2006-12-11', NULL),
(53, '1540096', NULL, 2, 0, '2006-12-11', NULL),
(54, '1540097', NULL, 2, 0, '2006-12-11', NULL),
(55, '1540099', NULL, 2, 0, '2006-12-11', NULL),
(56, '1540100', NULL, 2, 0, '2006-12-11', NULL),
(57, '1474564', NULL, 2, 0, '2006-12-11', NULL),
(58, 'NCL-21-1', NULL, 2, 0, '2006-12-11', NULL),
(59, '1540105', NULL, 2, 0, '2006-12-11', NULL),
(60, 'NCL-22-1', NULL, 2, 0, '2006-12-11', NULL),
(61, '1540111', NULL, 2, 0, '2006-12-11', NULL),
(62, '1540115', NULL, 2, 0, '2006-12-11', NULL),
(63, '1540117', NULL, 2, 0, '2006-12-11', NULL),
(64, '1474569', NULL, 2, 0, '2006-12-11', NULL),
(65, 'NCL-26-1', NULL, 2, 0, '2006-12-11', NULL),
(66, 'NCL-25-1', NULL, 2, 0, '2006-12-11', NULL),
(67, 'NCL-23-1', NULL, 2, 0, '2006-12-11', NULL),
(68, '1540118', NULL, 2, 0, '2006-12-11', NULL),
(69, '1540119', NULL, 2, 0, '2006-12-11', NULL),
(70, '1540120', NULL, 2, 0, '2006-12-11', NULL),
(71, '1540121', NULL, 2, 0, '2006-12-11', NULL),
(72, '1474574', NULL, 2, 0, '2006-12-11', NULL),
(73, '1802240', NULL, 2, 0, '2006-12-11', NULL),
(74, '1802241', NULL, 2, 0, '2006-12-11', NULL),
(75, '1900544', NULL, 2, 0, '2006-12-11', NULL),
(76, '1802243', NULL, 2, 0, '2006-12-11', NULL),
(77, '1802244', NULL, 2, 0, '2006-12-11', NULL),
(78, '1802245', NULL, 2, 0, '2006-12-11', NULL),
(79, '1802246', NULL, 2, 0, '2006-12-11', NULL),
(80, '1900545', NULL, 2, 0, '2006-12-11', NULL),
(81, '1900546', NULL, 2, 0, '2006-12-11', NULL),
(82, '1900547', NULL, 2, 0, '2006-12-11', NULL),
(83, '1900548', NULL, 2, 0, '2006-12-11', NULL),
(84, '1900549', NULL, 2, 0, '2006-12-11', NULL),
(85, 'NCL-24-1', NULL, 2, 0, '2006-12-11', NULL),
(86, '1900550', NULL, 2, 0, '2006-12-11', NULL),
(87, '1900551', NULL, 2, 0, '2006-12-11', NULL),
(88, '1900552', NULL, 2, 0, '2006-12-11', NULL),
(89, '1900554', NULL, 2, 0, '2006-12-11', NULL),
(90, '1900555', NULL, 2, 0, '2006-12-11', NULL),
(91, '1900556', NULL, 2, 0, '2006-12-11', NULL),
(92, '1802258', NULL, 2, 0, '2006-12-11', NULL),
(93, '1900557', NULL, 2, 0, '2006-12-11', NULL),
(94, '1802260', NULL, 2, 0, '2006-12-11', NULL),
(95, '1802261', NULL, 2, 0, '2006-12-11', NULL),
(96, '1900558', NULL, 2, 0, '2006-12-11', NULL),
(97, '1802263', NULL, 2, 0, '2006-12-11', NULL),
(98, '1900559', NULL, 2, 0, '2006-12-11', NULL),
(99, '1802265', NULL, 2, 0, '2006-12-11', NULL),
(100, '1900561', NULL, 2, 0, '2006-12-11', NULL),
(101, '1900562', NULL, 2, 0, '2006-12-11', NULL),
(102, '1900563', NULL, 2, 0, '2006-12-11', NULL),
(103, '1900564', NULL, 2, 0, '2006-12-11', NULL),
(104, '1900565', NULL, 2, 0, '2006-12-11', NULL),
(105, '1900566', NULL, 2, 0, '2006-12-11', NULL),
(106, '1900567', NULL, 2, 0, '2006-12-11', NULL),
(107, '1900568', NULL, 2, 0, '2006-12-11', NULL),
(108, '1900569', NULL, 2, 0, '2006-12-11', NULL),
(109, '2457600', NULL, 2, 0, '2006-12-13', NULL),
(110, '2457601', NULL, 2, 0, '2006-12-13', NULL),
(111, '2457602', NULL, 2, 0, '2006-12-13', NULL),
(112, '2457603', NULL, 2, 0, '2006-12-13', NULL),
(113, '2457604', NULL, 2, 0, '2006-12-13', NULL),
(114, '2293765', NULL, 2, 0, '2006-12-13', NULL),
(115, '2457605', NULL, 2, 0, '2006-12-13', NULL),
(116, '2457606', NULL, 2, 0, '2006-12-13', NULL),
(117, '2457607', NULL, 2, 0, '2006-12-13', NULL),
(118, '2457608', NULL, 2, 0, '2006-12-13', NULL),
(119, '2457609', NULL, 2, 0, '2006-12-13', NULL),
(120, '2457610', NULL, 2, 0, '2006-12-13', NULL),
(121, '2457611', NULL, 2, 0, '2006-12-13', NULL),
(122, '2457612', NULL, 2, 0, '2006-12-13', NULL),
(123, '2457613', NULL, 2, 0, '2006-12-13', NULL),
(124, '2457614', NULL, 2, 0, '2006-12-13', NULL),
(126, '2981888', NULL, 2, 0, '2006-12-15', NULL),
(127, '3178496', NULL, 2, 0, '2006-12-20', NULL),
(133, 'NCL-61', NULL, 2, 0, '2006-12-21', NULL),
(134, '3538944', NULL, 2, 0, '2006-12-21', NULL),
(135, '1540107', NULL, 2, 0, '2007-04-11', NULL),
(136, '3801088', NULL, 2, 0, '2007-04-11', NULL),
(137, '3801089', NULL, 2, 0, '2007-04-11', NULL),
(138, '3801090', NULL, 2, 0, '2007-04-11', NULL),
(139, '3801091', NULL, 2, 0, '2007-04-11', NULL),
(140, '3801092', NULL, 2, 0, '2007-04-11', NULL),
(141, '3801093', NULL, 2, 0, '2007-04-11', NULL),
(142, '3801094', NULL, 2, 0, '2007-04-11', NULL),
(143, '3801095', NULL, 2, 0, '2007-04-11', NULL),
(144, '3801096', NULL, 2, 0, '2007-04-11', NULL),
(145, '3801097', NULL, 2, 0, '2007-04-11', NULL),
(146, '3801098', NULL, 2, 0, '2007-04-11', NULL),
(147, '3801099', NULL, 2, 0, '2007-04-11', NULL),
(148, '3702797', NULL, 2, 0, '2007-04-11', NULL),
(149, '3801100', NULL, 2, 0, '2007-04-12', NULL),
(150, '3801101', NULL, 2, 0, '2007-04-12', NULL),
(151, '3801102', NULL, 2, 0, '2007-04-12', NULL),
(152, '1540116', NULL, 2, 0, '2007-04-12', NULL),
(153, '3801103', NULL, 2, 0, '2007-04-12', NULL),
(154, '3801104', NULL, 2, 0, '2007-04-12', NULL),
(155, '3801105', NULL, 2, 0, '2007-04-12', NULL),
(156, '1540113', NULL, 2, 0, '2007-04-12', NULL),
(157, '1540114', NULL, 2, 0, '2007-04-12', NULL),
(158, '1540103', NULL, 2, 0, '2007-04-12', NULL),
(159, '1540104', NULL, 2, 0, '2007-04-12', NULL),
(160, '3801106', NULL, 2, 0, '2007-04-12', NULL),
(161, '1900553', NULL, 2, 0, '2007-04-12', NULL),
(162, '3801107', NULL, 2, 0, '2007-04-12', NULL),
(163, '3801108', NULL, 2, 0, '2007-04-13', NULL),
(164, '3801109', NULL, 2, 0, '2007-04-13', NULL),
(165, '3801110', NULL, 2, 0, '2007-04-13', NULL),
(166, '3801111', NULL, 2, 0, '2007-04-13', NULL),
(167, '3801112', NULL, 2, 0, '2007-04-13', NULL),
(168, '3801113', NULL, 2, 0, '2007-04-13', NULL),
(169, '3801114', NULL, 2, 0, '2007-04-13', NULL),
(170, '3801115', NULL, 2, 0, '2007-04-13', NULL),
(171, '3801116', NULL, 2, 0, '2007-04-13', NULL),
(172, '3801117', NULL, 2, 0, '2007-04-13', NULL),
(173, '3801118', NULL, 2, 0, '2007-04-13', NULL),
(174, '3801119', NULL, 2, 0, '2007-04-13', NULL),
(175, '3702824', NULL, 2, 0, '2007-04-13', NULL),
(176, '3702825', NULL, 2, 0, '2007-04-13', NULL),
(177, '3801120', NULL, 2, 0, '2007-04-13', NULL),
(178, '3801121', NULL, 2, 0, '2007-04-13', NULL),
(179, '3801122', NULL, 2, 0, '2007-04-13', NULL),
(180, '3801123', NULL, 2, 0, '2007-04-13', NULL),
(181, '3801124', NULL, 2, 0, '2007-04-13', NULL),
(182, '3801126', NULL, 2, 0, '2007-04-13', NULL),
(183, '3801125', NULL, 2, 0, '2007-04-13', NULL),
(184, '3801127', NULL, 2, 0, '2007-04-13', NULL),
(185, '3801128', NULL, 2, 0, '2007-04-13', NULL),
(186, '3801129', NULL, 2, 0, '2007-04-13', NULL),
(187, '3801130', NULL, 2, 0, '2007-04-13', NULL),
(188, '3801131', NULL, 2, 0, '2007-04-13', NULL),
(189, '3801132', NULL, 2, 0, '2007-04-13', NULL),
(190, '3801133', NULL, 2, 0, '2007-04-13', NULL),
(192, 'NCL-42', NULL, 2, 0, '2007-05-17', NULL),
(193, '4259840', NULL, 2, 0, '2007-05-17', NULL),
(194, '4259841', NULL, 2, 0, '2007-05-17', NULL),
(195, '4259842', NULL, 2, 0, '2007-05-17', NULL),
(196, '4259843', NULL, 2, 0, '2007-05-17', NULL),
(197, '4128772', NULL, 2, 0, '2007-05-31', NULL),
(198, '4128773', NULL, 2, 0, '2007-05-31', NULL),
(199, '4259844', NULL, 2, 0, '2007-05-31', NULL),
(200, '4259845', NULL, 2, 0, '2007-05-31', NULL),
(201, '4259846', NULL, 2, 0, '2007-05-31', NULL),
(202, '4259847', NULL, 2, 0, '2007-05-31', NULL),
(203, '4259848', NULL, 2, 0, '2007-05-31', NULL),
(204, '4259849', NULL, 2, 0, '2007-05-31', NULL),
(205, '4259850', NULL, 2, 0, '2007-05-31', NULL),
(206, '4259851', NULL, 2, 0, '2007-05-31', NULL),
(207, '4128782', NULL, 2, 0, '2007-05-31', NULL),
(208, '4128783', NULL, 2, 0, '2007-05-31', NULL),
(209, '4128784', NULL, 2, 0, '2007-05-31', NULL),
(210, '4128785', NULL, 2, 0, '2007-05-31', NULL),
(211, '4259853', NULL, 2, 0, '2007-05-31', NULL),
(212, 'NCL-45', NULL, 2, 0, '2007-05-31', NULL),
(213, '4259854', NULL, 2, 0, '2007-05-31', NULL),
(214, 'NCL-17', NULL, 2, 0, '2007-05-31', NULL),
(215, '4259855', NULL, 2, 0, '2007-05-31', NULL),
(216, 'NCL-16', NULL, 2, 0, '2007-06-08', NULL),
(217, '4259856', NULL, 2, 0, '2007-06-08', NULL),
(218, 'NCL-19', NULL, 2, 0, '2007-06-08', NULL),
(219, '4259857', NULL, 2, 0, '2007-06-08', NULL),
(220, 'NCL-48', NULL, 2, 0, '2007-06-08', NULL),
(221, 'NCL-48-4', NULL, 2, 0, '2007-06-08', NULL),
(222, 'NCL-49', NULL, 2, 0, '2007-06-08', NULL),
(223, '4259858', NULL, 2, 0, '2007-06-14', NULL),
(224, '4259859', NULL, 2, 0, '2007-06-14', NULL),
(225, '4259860', NULL, 2, 0, '2007-06-14', NULL),
(226, '4259861', NULL, 2, 0, '2007-06-14', NULL),
(227, '4259862', NULL, 2, 0, '2007-06-14', NULL),
(228, '4259863', NULL, 2, 0, '2007-06-14', NULL),
(229, '4259864', NULL, 2, 0, '2007-06-14', NULL),
(230, '4259865', NULL, 2, 0, '2007-07-13', NULL),
(231, '4259866', NULL, 2, 0, '2007-07-13', NULL),
(232, '4259867', NULL, 2, 0, '2007-07-13', NULL),
(233, '4259868', NULL, 2, 0, '2007-07-20', NULL),
(234, '4259869', NULL, 2, 0, '2007-07-20', NULL),
(235, '4259870', NULL, 2, 0, '2007-07-20', NULL),
(236, '4259871', NULL, 2, 0, '2007-07-20', NULL),
(238, '101', NULL, 2, 0, '2007-08-31', NULL),
(239, '102', NULL, 2, 0, '2007-08-31', NULL),
(240, '103', NULL, 2, 0, '2007-08-31', NULL),
(241, '104', NULL, 2, 0, '2007-08-31', NULL),
(242, '105', NULL, 2, 0, '2007-08-31', NULL),
(243, '106', NULL, 2, 0, '2007-08-31', NULL),
(244, '107', NULL, 2, 0, '2007-08-31', NULL),
(245, '108', NULL, 2, 0, '2007-08-31', NULL),
(246, '109', NULL, 2, 0, '2007-08-31', NULL),
(247, '110', NULL, 2, 0, '2007-08-31', NULL),
(248, '111', NULL, 2, 0, '2007-08-31', NULL),
(249, '112', NULL, 2, 0, '2007-08-31', NULL),
(250, '113', NULL, 2, 0, '2007-08-31', NULL),
(251, '114', NULL, 2, 0, '2007-08-31', NULL),
(252, '115', NULL, 2, 0, '2007-08-31', NULL),
(253, '116', NULL, 2, 0, '2007-08-31', NULL),
(254, '117', NULL, 2, 0, '2007-08-31', NULL),
(255, '118', NULL, 2, 0, '2007-08-31', NULL),
(256, '119', NULL, 2, 0, '2007-08-31', NULL),
(257, '4259852', NULL, 2, 0, '2007-09-13', NULL),
(258, '4456448', NULL, 2, 0, '2007-09-13', NULL),
(259, '4456449', NULL, 2, 0, '2007-09-13', NULL),
(260, '4456450', NULL, 2, 0, '2007-09-13', NULL),
(261, '4456451', NULL, 2, 0, '2007-09-13', NULL),
(262, '4521984', NULL, 2, 0, '2007-11-16', NULL),
(263, '4521985', NULL, 2, 0, '2007-11-16', NULL),
(264, '4521986', NULL, 2, 0, '2007-11-16', NULL),
(265, '4521987', NULL, 2, 0, '2007-11-16', NULL),
(266, '4521988', NULL, 2, 0, '2007-11-16', NULL),
(267, '4521989', NULL, 2, 0, '2007-11-16', NULL),
(268, '4521990', NULL, 2, 0, '2007-11-16', NULL),
(269, 'sample', '', 2, 0, '2008-01-16', NULL),
(270, 'protocol', '', 2, 0, '2008-01-16', NULL),
(271, 'nanoparticle', '', 2, 0, '2008-01-16', NULL),
(272, 'report', '', 2, 0, '2008-01-16', NULL),
(273, '4620288', NULL, 2, 0, '2008-01-24', NULL),
(274, '4620291', NULL, 2, 0, '2008-01-24', NULL),
(275, '4620292', NULL, 2, 0, '2008-01-24', NULL),
(276, '4620293', NULL, 2, 0, '2008-01-24', NULL),
(277, '4620294', NULL, 2, 0, '2008-01-24', NULL),
(278, 'NCL-49-2', NULL, 2, 0, '2008-01-24', NULL),
(279, 'NCL-50-1', NULL, 2, 0, '2008-01-24', NULL),
(280, 'NCL-51-3', NULL, 2, 0, '2008-01-24', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `csm_role`
--

CREATE TABLE IF NOT EXISTS `csm_role` (
  `role_id` bigint(20) NOT NULL auto_increment,
  `role_name` varchar(100) NOT NULL,
  `role_description` varchar(200) default NULL,
  `application_id` bigint(20) NOT NULL,
  `active_flag` tinyint(1) NOT NULL,
  `update_date` date NOT NULL,
  PRIMARY KEY  (`role_id`),
  UNIQUE KEY `uq_role_role_name` (`application_id`,`role_name`),
  KEY `idx_application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=13 ;

--
-- Dumping data for table `csm_role`
--

INSERT INTO `csm_role` (`role_id`, `role_name`, `role_description`, `application_id`, `active_flag`, `update_date`) VALUES
(4, 'CARU', 'create access read update', 2, 1, '2006-07-11'),
(5, 'AR', 'acess read', 2, 1, '2006-07-11'),
(7, 'E', 'execute only', 2, 1, '2006-07-11'),
(8, 'D', 'delete only', 2, 1, '2006-07-11'),
(9, 'CARUD', 'create access read update delete', 2, 1, '2006-07-11'),
(10, 'R', 'read only', 2, 1, '2006-08-02'),
(11, 'CUR', 'create, update, read', 2, 1, '2008-01-16'),
(12, 'CURD', 'create, update, read, delete', 2, 1, '2008-01-16');

-- --------------------------------------------------------

--
-- Table structure for table `csm_role_privilege`
--

CREATE TABLE IF NOT EXISTS `csm_role_privilege` (
  `role_privilege_id` bigint(20) NOT NULL auto_increment,
  `role_id` bigint(20) NOT NULL,
  `privilege_id` bigint(20) NOT NULL,
  `update_date` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`role_privilege_id`),
  UNIQUE KEY `uq_role_privilege_role_id` (`privilege_id`,`role_id`),
  KEY `idx_privilege_id` (`privilege_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=34 ;

--
-- Dumping data for table `csm_role_privilege`
--

INSERT INTO `csm_role_privilege` (`role_privilege_id`, `role_id`, `privilege_id`, `update_date`) VALUES
(11, 4, 2, '2006-07-10'),
(12, 4, 5, '2006-07-10'),
(13, 4, 3, '2006-07-10'),
(14, 4, 1, '2006-07-10'),
(15, 7, 7, '2006-07-10'),
(16, 5, 3, '2006-07-10'),
(17, 5, 2, '2006-07-10'),
(18, 8, 6, '2006-07-10'),
(19, 9, 6, '2006-07-11'),
(20, 9, 2, '2006-07-11'),
(21, 9, 4, '2006-07-11'),
(22, 9, 1, '2006-07-11'),
(23, 9, 5, '2006-07-11'),
(24, 9, 3, '2006-07-11'),
(25, 9, 7, '2006-07-11'),
(26, 10, 3, '2006-08-02'),
(27, 11, 1, '2008-01-16'),
(28, 11, 3, '2008-01-16'),
(29, 11, 5, '2008-01-16'),
(30, 12, 1, '2008-01-16'),
(31, 12, 6, '2008-01-16'),
(32, 12, 3, '2008-01-16'),
(33, 12, 5, '2008-01-16');

-- --------------------------------------------------------

--
-- Table structure for table `csm_user`
--

CREATE TABLE IF NOT EXISTS `csm_user` (
  `user_id` bigint(20) NOT NULL auto_increment,
  `login_name` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `organization` varchar(100) default NULL,
  `department` varchar(100) default NULL,
  `title` varchar(100) default NULL,
  `phone_number` varchar(15) default NULL,
  `password` varchar(100) default NULL,
  `email_id` varchar(100) default NULL,
  `start_date` date default NULL,
  `end_date` date default NULL,
  `update_date` date NOT NULL,
  PRIMARY KEY  (`user_id`),
  UNIQUE KEY `uq_login_name` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=59 ;

--
-- Dumping data for table `csm_user`
--

INSERT INTO `csm_user` (`user_id`, `login_name`, `first_name`, `last_name`, `organization`, `department`, `title`, `phone_number`, `password`, `email_id`, `start_date`, `end_date`, `update_date`) VALUES
(8, 'mcneil', 'Scott', 'McNeil', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-7579', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'mcneils@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(9, 'patri', 'Anil', 'Patri', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-5237', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'patri@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(10, 'marina', 'Marina', 'Dobrovolskaia', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-6352', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'marina@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(11, 'stern', 'Steve', 'Stern', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-6198', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'stern@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(12, 'fritts', 'Marty', 'Fritts', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-6938', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'frittsm@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(13, 'fine', 'Lydia', 'Fine', 'SAIC-Frederick', 'NCL', 'Ms.', '301-846-7378', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'lfine@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(14, 'zheng', 'Jiwen', 'Zheng', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-1418', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'zheng@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(15, 'clogston', 'Jeff', 'Clogston', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-1388', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'jclogston@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(16, 'zolnik', 'Banu', 'Zolnik', 'SAIC-Frederick', 'NCL', 'Dr.', '301-846-7161', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'zolnik@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(17, 'neun', 'Barry', 'Neun', 'SAIC-Frederick', 'NCL', 'Mr.', '301-846-5393', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'neunb@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(18, 'potter', 'Tim', 'Potter', 'SAIC-Frederick', 'NCL', 'Mr.', '301-846-6649', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'potter@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(19, 'skoczen', 'Sarah', 'Skoczen', 'SAIC-Frederick', 'NCL', 'Ms.', '301-846-5021', 'QXOhpC+wr65Zpwg23how1EQemNc=', 'sskoczen@ncifcrf.gov', NULL, NULL, '2006-06-16'),
(35, 'admin', 'Admin', 'Doe', NULL, NULL, NULL, NULL, 'dJE/XNX2HsC8/bd1QUwvs9FhtiA=', NULL, NULL, NULL, '2007-05-01'),
(38, 'superadmin', 'Super', 'Doe', NULL, NULL, NULL, NULL, 'dJE/XNX2HsC8/bd1QUwvs9FhtiA=', NULL, NULL, NULL, '2007-05-02'),
(51, 'jennifer', 'Jennifer', 'Hall', NULL, 'NCL', NULL, NULL, 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', NULL, NULL, NULL, '2007-02-16'),
(53, 'mcleland', 'Chris', 'McLeland', 'NCI-Frederick', 'NCL', 'Mr.', '301-846-6974', 'SGRs9BJR7F3FvCcN3HBc9YFccMI=', 'mclelandc@ncifcrf.gov', NULL, NULL, '2007-02-16'),
(54, 'nclguest', 'guest', 'guest', NULL, NULL, NULL, NULL, 'cdcJW0tCXMvAaq0dfKYBz+Lq598=', NULL, NULL, NULL, '2007-10-24'),
(55, 'molnarl', 'Linda', 'Molnar', 'NCI', NULL, NULL, NULL, 'RIWHBqEuEcCeATON64GEW51QY0Y=', NULL, NULL, NULL, '2007-06-06'),
(56, 'sharon', 'Sharon', 'Gaheen', NULL, NULL, NULL, NULL, 'TknYVMm/+wpkJXEklxI0xElSkmw=', NULL, NULL, NULL, '2007-05-18'),
(57, 'piotr', 'Piotr', 'Grodzinski', 'nci', NULL, NULL, NULL, 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'grodzinp@mail.nih.gov', NULL, NULL, '2007-06-05'),
(58, 'scott', 'Scott', 'McNeil', 'NCL', NULL, NULL, NULL, 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'mcneils@mail.nih.gov', NULL, NULL, '2007-06-14');

-- --------------------------------------------------------

--
-- Table structure for table `csm_user_group`
--

CREATE TABLE IF NOT EXISTS `csm_user_group` (
  `user_group_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_group_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=102 ;

--
-- Dumping data for table `csm_user_group`
--

INSERT INTO `csm_user_group` (`user_group_id`, `user_id`, `group_id`) VALUES
(54, 11, 6),
(55, 10, 6),
(56, 9, 6),
(57, 12, 6),
(59, 13, 7),
(60, 8, 7),
(61, 14, 7),
(62, 15, 7),
(63, 16, 7),
(64, 17, 7),
(65, 18, 7),
(66, 19, 7),
(69, 53, 6),
(85, 54, 11),
(86, 51, 9),
(87, 51, 6),
(88, 55, 7),
(92, 56, 11),
(93, 56, 6),
(94, 57, 7),
(97, 58, 11),
(98, 35, 6),
(99, 35, 7),
(100, 35, 11),
(101, 35, 9);

-- --------------------------------------------------------

--
-- Table structure for table `csm_user_group_role_pg`
--

CREATE TABLE IF NOT EXISTS `csm_user_group_role_pg` (
  `user_group_role_pg_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `group_id` bigint(20) default NULL,
  `role_id` bigint(20) NOT NULL,
  `protection_group_id` bigint(20) NOT NULL,
  `update_date` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`user_group_role_pg_id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_protection_group_id` (`protection_group_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1815 ;

--
-- Dumping data for table `csm_user_group_role_pg`
--

INSERT INTO `csm_user_group_role_pg` (`user_group_role_pg_id`, `user_id`, `group_id`, `role_id`, `protection_group_id`, `update_date`) VALUES
(1004, NULL, 6, 10, 51, '2006-12-08'),
(1005, NULL, 7, 10, 51, '2006-12-08'),
(1009, NULL, 6, 10, 53, '2006-12-11'),
(1010, NULL, 7, 10, 53, '2006-12-11'),
(1011, NULL, 6, 10, 54, '2006-12-11'),
(1012, NULL, 7, 10, 54, '2006-12-11'),
(1013, NULL, 6, 10, 55, '2006-12-11'),
(1014, NULL, 7, 10, 55, '2006-12-11'),
(1015, NULL, 6, 10, 56, '2006-12-11'),
(1016, NULL, 7, 10, 56, '2006-12-11'),
(1017, NULL, 6, 10, 57, '2006-12-11'),
(1018, NULL, 7, 10, 57, '2006-12-11'),
(1022, NULL, 6, 10, 59, '2006-12-11'),
(1023, NULL, 7, 10, 59, '2006-12-11'),
(1027, NULL, 6, 10, 61, '2006-12-11'),
(1028, NULL, 7, 10, 61, '2006-12-11'),
(1029, NULL, 6, 10, 62, '2006-12-11'),
(1030, NULL, 7, 10, 62, '2006-12-11'),
(1031, NULL, 6, 10, 63, '2006-12-11'),
(1032, NULL, 7, 10, 63, '2006-12-11'),
(1045, NULL, 6, 10, 68, '2006-12-11'),
(1046, NULL, 7, 10, 68, '2006-12-11'),
(1047, NULL, 6, 10, 69, '2006-12-11'),
(1048, NULL, 7, 10, 69, '2006-12-11'),
(1049, NULL, 6, 10, 70, '2006-12-11'),
(1050, NULL, 7, 10, 70, '2006-12-11'),
(1051, NULL, 6, 10, 71, '2006-12-11'),
(1052, NULL, 7, 10, 71, '2006-12-11'),
(1053, NULL, 6, 10, 72, '2006-12-11'),
(1054, NULL, 7, 10, 72, '2006-12-11'),
(1055, NULL, 6, 10, 73, '2006-12-11'),
(1056, NULL, 7, 10, 73, '2006-12-11'),
(1057, NULL, 6, 10, 74, '2006-12-11'),
(1058, NULL, 7, 10, 74, '2006-12-11'),
(1059, NULL, 6, 10, 75, '2006-12-11'),
(1060, NULL, 7, 10, 75, '2006-12-11'),
(1061, NULL, 6, 10, 76, '2006-12-11'),
(1062, NULL, 7, 10, 76, '2006-12-11'),
(1063, NULL, 6, 10, 77, '2006-12-11'),
(1064, NULL, 7, 10, 77, '2006-12-11'),
(1065, NULL, 6, 10, 78, '2006-12-11'),
(1066, NULL, 7, 10, 78, '2006-12-11'),
(1067, NULL, 6, 10, 79, '2006-12-11'),
(1068, NULL, 7, 10, 79, '2006-12-11'),
(1069, NULL, 6, 10, 80, '2006-12-11'),
(1070, NULL, 7, 10, 80, '2006-12-11'),
(1071, NULL, 6, 10, 81, '2006-12-11'),
(1072, NULL, 7, 10, 81, '2006-12-11'),
(1073, NULL, 6, 10, 82, '2006-12-11'),
(1074, NULL, 7, 10, 82, '2006-12-11'),
(1075, NULL, 6, 10, 83, '2006-12-11'),
(1076, NULL, 7, 10, 83, '2006-12-11'),
(1077, NULL, 6, 10, 84, '2006-12-11'),
(1078, NULL, 7, 10, 84, '2006-12-11'),
(1106, NULL, 6, 10, 86, '2006-12-11'),
(1107, NULL, 7, 10, 86, '2006-12-11'),
(1108, NULL, 6, 10, 87, '2006-12-11'),
(1109, NULL, 7, 10, 87, '2006-12-11'),
(1110, NULL, 6, 10, 88, '2006-12-11'),
(1111, NULL, 7, 10, 88, '2006-12-11'),
(1112, NULL, 6, 10, 89, '2006-12-11'),
(1113, NULL, 7, 10, 89, '2006-12-11'),
(1114, NULL, 6, 10, 90, '2006-12-11'),
(1115, NULL, 7, 10, 90, '2006-12-11'),
(1116, NULL, 6, 10, 91, '2006-12-11'),
(1117, NULL, 7, 10, 91, '2006-12-11'),
(1118, NULL, 6, 10, 92, '2006-12-11'),
(1119, NULL, 7, 10, 92, '2006-12-11'),
(1120, NULL, 6, 10, 93, '2006-12-11'),
(1121, NULL, 7, 10, 93, '2006-12-11'),
(1122, NULL, 6, 10, 94, '2006-12-11'),
(1123, NULL, 7, 10, 94, '2006-12-11'),
(1124, NULL, 6, 10, 95, '2006-12-11'),
(1125, NULL, 7, 10, 95, '2006-12-11'),
(1126, NULL, 6, 10, 96, '2006-12-11'),
(1127, NULL, 7, 10, 96, '2006-12-11'),
(1128, NULL, 6, 10, 97, '2006-12-11'),
(1129, NULL, 7, 10, 97, '2006-12-11'),
(1130, NULL, 6, 10, 98, '2006-12-11'),
(1131, NULL, 7, 10, 98, '2006-12-11'),
(1132, NULL, 6, 10, 99, '2006-12-11'),
(1133, NULL, 7, 10, 99, '2006-12-11'),
(1134, NULL, 6, 10, 100, '2006-12-11'),
(1135, NULL, 7, 10, 100, '2006-12-11'),
(1136, NULL, 6, 10, 101, '2006-12-11'),
(1137, NULL, 7, 10, 101, '2006-12-11'),
(1138, NULL, 6, 10, 102, '2006-12-11'),
(1139, NULL, 7, 10, 102, '2006-12-11'),
(1140, NULL, 6, 10, 103, '2006-12-11'),
(1141, NULL, 7, 10, 103, '2006-12-11'),
(1142, NULL, 6, 10, 104, '2006-12-11'),
(1143, NULL, 7, 10, 104, '2006-12-11'),
(1144, NULL, 6, 10, 105, '2006-12-11'),
(1145, NULL, 7, 10, 105, '2006-12-11'),
(1146, NULL, 6, 10, 106, '2006-12-11'),
(1147, NULL, 7, 10, 106, '2006-12-11'),
(1148, NULL, 6, 10, 107, '2006-12-11'),
(1149, NULL, 7, 10, 107, '2006-12-11'),
(1150, NULL, 6, 10, 108, '2006-12-11'),
(1151, NULL, 7, 10, 108, '2006-12-11'),
(1158, NULL, 6, 10, 112, '2006-12-13'),
(1159, NULL, 7, 10, 112, '2006-12-13'),
(1160, NULL, 6, 10, 113, '2006-12-13'),
(1161, NULL, 7, 10, 113, '2006-12-13'),
(1162, NULL, 6, 10, 114, '2006-12-13'),
(1163, NULL, 7, 10, 114, '2006-12-13'),
(1164, NULL, 6, 10, 115, '2006-12-13'),
(1165, NULL, 7, 10, 115, '2006-12-13'),
(1166, NULL, 6, 10, 116, '2006-12-13'),
(1167, NULL, 7, 10, 116, '2006-12-13'),
(1168, NULL, 6, 10, 117, '2006-12-13'),
(1169, NULL, 7, 10, 117, '2006-12-13'),
(1170, NULL, 6, 10, 118, '2006-12-13'),
(1171, NULL, 7, 10, 118, '2006-12-13'),
(1172, NULL, 6, 10, 119, '2006-12-13'),
(1173, NULL, 7, 10, 119, '2006-12-13'),
(1176, NULL, 6, 10, 121, '2006-12-13'),
(1177, NULL, 7, 10, 121, '2006-12-13'),
(1178, NULL, 6, 10, 122, '2006-12-13'),
(1179, NULL, 7, 10, 122, '2006-12-13'),
(1180, NULL, 6, 10, 123, '2006-12-13'),
(1181, NULL, 7, 10, 123, '2006-12-13'),
(1182, NULL, 6, 10, 124, '2006-12-13'),
(1183, NULL, 7, 10, 124, '2006-12-13'),
(1186, NULL, 8, 10, 126, '2006-12-15'),
(1187, NULL, 13, 10, 126, '2006-12-15'),
(1188, NULL, 11, 10, 127, '2006-12-20'),
(1189, NULL, 6, 10, 127, '2006-12-20'),
(1190, NULL, 7, 10, 127, '2006-12-20'),
(1206, NULL, 6, 10, 133, '2006-12-21'),
(1207, NULL, 7, 10, 133, '2006-12-21'),
(1208, NULL, 6, 10, 134, '2006-12-21'),
(1209, NULL, 7, 10, 134, '2006-12-21'),
(1214, NULL, 6, 10, 64, '2006-12-22'),
(1215, NULL, 7, 10, 64, '2006-12-22'),
(1224, NULL, 6, 10, 135, '2007-04-11'),
(1225, NULL, 7, 10, 135, '2007-04-11'),
(1226, NULL, 6, 10, 136, '2007-04-11'),
(1227, NULL, 7, 10, 136, '2007-04-11'),
(1228, NULL, 6, 10, 137, '2007-04-11'),
(1229, NULL, 7, 10, 137, '2007-04-11'),
(1230, NULL, 6, 10, 138, '2007-04-11'),
(1231, NULL, 7, 10, 138, '2007-04-11'),
(1232, NULL, 6, 10, 139, '2007-04-11'),
(1233, NULL, 7, 10, 139, '2007-04-11'),
(1234, NULL, 6, 10, 140, '2007-04-11'),
(1235, NULL, 7, 10, 140, '2007-04-11'),
(1236, NULL, 6, 10, 141, '2007-04-11'),
(1237, NULL, 7, 10, 141, '2007-04-11'),
(1240, NULL, 6, 10, 142, '2007-04-11'),
(1241, NULL, 7, 10, 142, '2007-04-11'),
(1242, NULL, 6, 10, 143, '2007-04-11'),
(1243, NULL, 7, 10, 143, '2007-04-11'),
(1244, NULL, 6, 10, 144, '2007-04-11'),
(1245, NULL, 7, 10, 144, '2007-04-11'),
(1246, NULL, 6, 10, 145, '2007-04-11'),
(1247, NULL, 7, 10, 145, '2007-04-11'),
(1248, NULL, 6, 10, 146, '2007-04-11'),
(1249, NULL, 7, 10, 146, '2007-04-11'),
(1250, NULL, 6, 10, 147, '2007-04-11'),
(1251, NULL, 7, 10, 147, '2007-04-11'),
(1252, NULL, 6, 10, 148, '2007-04-11'),
(1253, NULL, 7, 10, 148, '2007-04-11'),
(1254, NULL, 6, 10, 149, '2007-04-12'),
(1255, NULL, 7, 10, 149, '2007-04-12'),
(1256, NULL, 6, 10, 150, '2007-04-12'),
(1257, NULL, 7, 10, 150, '2007-04-12'),
(1258, NULL, 6, 10, 151, '2007-04-12'),
(1259, NULL, 7, 10, 151, '2007-04-12'),
(1260, NULL, 6, 10, 152, '2007-04-12'),
(1261, NULL, 7, 10, 152, '2007-04-12'),
(1262, NULL, 6, 10, 153, '2007-04-12'),
(1263, NULL, 7, 10, 153, '2007-04-12'),
(1264, NULL, 6, 10, 154, '2007-04-12'),
(1265, NULL, 7, 10, 154, '2007-04-12'),
(1266, NULL, 6, 10, 155, '2007-04-12'),
(1267, NULL, 7, 10, 155, '2007-04-12'),
(1268, NULL, 6, 10, 156, '2007-04-12'),
(1269, NULL, 7, 10, 156, '2007-04-12'),
(1270, NULL, 6, 10, 157, '2007-04-12'),
(1271, NULL, 7, 10, 157, '2007-04-12'),
(1272, NULL, 6, 10, 158, '2007-04-12'),
(1273, NULL, 7, 10, 158, '2007-04-12'),
(1274, NULL, 6, 10, 159, '2007-04-12'),
(1275, NULL, 7, 10, 159, '2007-04-12'),
(1276, NULL, 6, 10, 160, '2007-04-12'),
(1277, NULL, 7, 10, 160, '2007-04-12'),
(1278, NULL, 6, 10, 161, '2007-04-12'),
(1279, NULL, 7, 10, 161, '2007-04-12'),
(1280, NULL, 6, 10, 162, '2007-04-12'),
(1281, NULL, 7, 10, 162, '2007-04-12'),
(1282, NULL, 6, 10, 109, '2007-04-13'),
(1283, NULL, 7, 10, 109, '2007-04-13'),
(1284, NULL, 6, 10, 163, '2007-04-13'),
(1285, NULL, 7, 10, 163, '2007-04-13'),
(1286, NULL, 6, 10, 164, '2007-04-13'),
(1287, NULL, 7, 10, 164, '2007-04-13'),
(1290, NULL, 6, 10, 110, '2007-04-13'),
(1291, NULL, 7, 10, 110, '2007-04-13'),
(1292, NULL, 6, 10, 165, '2007-04-13'),
(1293, NULL, 7, 10, 165, '2007-04-13'),
(1294, NULL, 6, 10, 166, '2007-04-13'),
(1295, NULL, 7, 10, 166, '2007-04-13'),
(1296, NULL, 6, 10, 111, '2007-04-13'),
(1297, NULL, 7, 10, 111, '2007-04-13'),
(1298, NULL, 6, 10, 167, '2007-04-13'),
(1299, NULL, 7, 10, 167, '2007-04-13'),
(1300, NULL, 6, 10, 168, '2007-04-13'),
(1301, NULL, 7, 10, 168, '2007-04-13'),
(1302, NULL, 6, 10, 169, '2007-04-13'),
(1303, NULL, 7, 10, 169, '2007-04-13'),
(1304, NULL, 6, 10, 170, '2007-04-13'),
(1305, NULL, 7, 10, 170, '2007-04-13'),
(1306, NULL, 6, 10, 171, '2007-04-13'),
(1307, NULL, 7, 10, 171, '2007-04-13'),
(1308, NULL, 6, 10, 172, '2007-04-13'),
(1309, NULL, 7, 10, 172, '2007-04-13'),
(1310, NULL, 6, 10, 173, '2007-04-13'),
(1311, NULL, 7, 10, 173, '2007-04-13'),
(1312, NULL, 6, 10, 174, '2007-04-13'),
(1313, NULL, 7, 10, 174, '2007-04-13'),
(1316, NULL, 6, 10, 175, '2007-04-13'),
(1317, NULL, 7, 10, 175, '2007-04-13'),
(1318, NULL, 6, 10, 176, '2007-04-13'),
(1319, NULL, 7, 10, 176, '2007-04-13'),
(1320, NULL, 6, 10, 177, '2007-04-13'),
(1321, NULL, 7, 10, 177, '2007-04-13'),
(1322, NULL, 6, 10, 178, '2007-04-13'),
(1323, NULL, 7, 10, 178, '2007-04-13'),
(1324, NULL, 6, 10, 179, '2007-04-13'),
(1325, NULL, 7, 10, 179, '2007-04-13'),
(1330, NULL, 6, 10, 120, '2007-04-13'),
(1331, NULL, 7, 10, 120, '2007-04-13'),
(1332, NULL, 6, 10, 180, '2007-04-13'),
(1333, NULL, 7, 10, 180, '2007-04-13'),
(1334, NULL, 6, 10, 181, '2007-04-13'),
(1335, NULL, 7, 10, 181, '2007-04-13'),
(1336, NULL, 6, 10, 182, '2007-04-13'),
(1337, NULL, 7, 10, 182, '2007-04-13'),
(1338, NULL, 6, 10, 183, '2007-04-13'),
(1339, NULL, 7, 10, 183, '2007-04-13'),
(1340, NULL, 6, 10, 184, '2007-04-13'),
(1341, NULL, 7, 10, 184, '2007-04-13'),
(1342, NULL, 6, 10, 185, '2007-04-13'),
(1343, NULL, 7, 10, 185, '2007-04-13'),
(1344, NULL, 6, 10, 186, '2007-04-13'),
(1345, NULL, 7, 10, 186, '2007-04-13'),
(1346, NULL, 6, 10, 187, '2007-04-13'),
(1347, NULL, 7, 10, 187, '2007-04-13'),
(1348, NULL, 6, 10, 188, '2007-04-13'),
(1349, NULL, 7, 10, 188, '2007-04-13'),
(1350, NULL, 6, 10, 189, '2007-04-13'),
(1351, NULL, 7, 10, 189, '2007-04-13'),
(1352, NULL, 6, 10, 190, '2007-04-13'),
(1353, NULL, 7, 10, 190, '2007-04-13'),
(1360, NULL, 11, 10, 52, '2007-05-03'),
(1361, NULL, 6, 10, 52, '2007-05-03'),
(1362, NULL, 7, 10, 52, '2007-05-03'),
(1363, NULL, 11, 10, 58, '2007-05-03'),
(1364, NULL, 6, 10, 58, '2007-05-03'),
(1365, NULL, 7, 10, 58, '2007-05-03'),
(1369, NULL, 11, 10, 67, '2007-05-03'),
(1370, NULL, 6, 10, 67, '2007-05-03'),
(1371, NULL, 7, 10, 67, '2007-05-03'),
(1372, NULL, 11, 10, 85, '2007-05-03'),
(1373, NULL, 6, 10, 85, '2007-05-03'),
(1374, NULL, 7, 10, 85, '2007-05-03'),
(1375, NULL, 11, 10, 66, '2007-05-03'),
(1376, NULL, 6, 10, 66, '2007-05-03'),
(1377, NULL, 7, 10, 66, '2007-05-03'),
(1378, NULL, 11, 10, 65, '2007-05-03'),
(1379, NULL, 6, 10, 65, '2007-05-03'),
(1380, NULL, 7, 10, 65, '2007-05-03'),
(1383, NULL, 6, 10, 193, '2007-05-17'),
(1384, NULL, 7, 10, 193, '2007-05-17'),
(1387, NULL, 6, 10, 195, '2007-05-17'),
(1388, NULL, 7, 10, 195, '2007-05-17'),
(1389, NULL, 6, 10, 196, '2007-05-17'),
(1390, NULL, 7, 10, 196, '2007-05-17'),
(1392, NULL, 11, 10, 51, '2007-05-21'),
(1393, NULL, 11, 10, 53, '2007-05-21'),
(1394, NULL, 11, 10, 54, '2007-05-21'),
(1395, NULL, 11, 10, 55, '2007-05-21'),
(1396, NULL, 11, 10, 56, '2007-05-21'),
(1397, NULL, 11, 10, 59, '2007-05-21'),
(1398, NULL, 11, 10, 61, '2007-05-21'),
(1399, NULL, 11, 10, 62, '2007-05-21'),
(1400, NULL, 11, 10, 63, '2007-05-21'),
(1401, NULL, 11, 10, 68, '2007-05-21'),
(1402, NULL, 11, 10, 69, '2007-05-21'),
(1403, NULL, 11, 10, 70, '2007-05-21'),
(1404, NULL, 11, 10, 71, '2007-05-21'),
(1405, NULL, 11, 10, 72, '2007-05-21'),
(1406, NULL, 11, 10, 73, '2007-05-21'),
(1407, NULL, 11, 10, 75, '2007-05-21'),
(1408, NULL, 11, 10, 76, '2007-05-21'),
(1409, NULL, 11, 10, 77, '2007-05-21'),
(1410, NULL, 11, 10, 80, '2007-05-21'),
(1411, NULL, 11, 10, 81, '2007-05-21'),
(1412, NULL, 11, 10, 82, '2007-05-21'),
(1413, NULL, 11, 10, 83, '2007-05-21'),
(1414, NULL, 11, 10, 84, '2007-05-21'),
(1415, NULL, 11, 10, 86, '2007-05-21'),
(1416, NULL, 11, 10, 87, '2007-05-21'),
(1417, NULL, 11, 10, 88, '2007-05-21'),
(1418, NULL, 11, 10, 89, '2007-05-21'),
(1419, NULL, 11, 10, 90, '2007-05-21'),
(1420, NULL, 11, 10, 91, '2007-05-21'),
(1421, NULL, 11, 10, 93, '2007-05-21'),
(1422, NULL, 11, 10, 94, '2007-05-21'),
(1423, NULL, 11, 10, 95, '2007-05-21'),
(1424, NULL, 11, 10, 96, '2007-05-21'),
(1425, NULL, 11, 10, 97, '2007-05-21'),
(1426, NULL, 11, 10, 98, '2007-05-21'),
(1427, NULL, 11, 10, 100, '2007-05-21'),
(1428, NULL, 11, 10, 101, '2007-05-21'),
(1429, NULL, 11, 10, 102, '2007-05-21'),
(1430, NULL, 11, 10, 103, '2007-05-21'),
(1431, NULL, 11, 10, 104, '2007-05-21'),
(1432, NULL, 11, 10, 105, '2007-05-21'),
(1433, NULL, 11, 10, 106, '2007-05-21'),
(1434, NULL, 11, 10, 107, '2007-05-21'),
(1435, NULL, 11, 10, 108, '2007-05-21'),
(1436, NULL, 11, 10, 109, '2007-05-21'),
(1437, NULL, 11, 10, 110, '2007-05-21'),
(1438, NULL, 11, 10, 111, '2007-05-21'),
(1439, NULL, 11, 10, 112, '2007-05-21'),
(1440, NULL, 11, 10, 113, '2007-05-21'),
(1441, NULL, 11, 10, 114, '2007-05-21'),
(1442, NULL, 11, 10, 115, '2007-05-21'),
(1443, NULL, 11, 10, 116, '2007-05-21'),
(1444, NULL, 11, 10, 117, '2007-05-21'),
(1445, NULL, 11, 10, 118, '2007-05-21'),
(1446, NULL, 11, 10, 119, '2007-05-21'),
(1447, NULL, 11, 10, 120, '2007-05-21'),
(1448, NULL, 11, 10, 121, '2007-05-21'),
(1449, NULL, 11, 10, 122, '2007-05-21'),
(1450, NULL, 11, 10, 123, '2007-05-21'),
(1451, NULL, 11, 10, 124, '2007-05-21'),
(1452, NULL, 11, 10, 135, '2007-05-21'),
(1453, NULL, 11, 10, 136, '2007-05-21'),
(1454, NULL, 11, 10, 137, '2007-05-21'),
(1455, NULL, 11, 10, 138, '2007-05-21'),
(1456, NULL, 11, 10, 139, '2007-05-21'),
(1457, NULL, 11, 10, 140, '2007-05-21'),
(1458, NULL, 11, 10, 141, '2007-05-21'),
(1459, NULL, 11, 10, 142, '2007-05-21'),
(1460, NULL, 11, 10, 143, '2007-05-21'),
(1461, NULL, 11, 10, 144, '2007-05-21'),
(1462, NULL, 11, 10, 145, '2007-05-21'),
(1463, NULL, 11, 10, 146, '2007-05-21'),
(1464, NULL, 11, 10, 147, '2007-05-21'),
(1465, NULL, 11, 10, 148, '2007-05-21'),
(1466, NULL, 11, 10, 149, '2007-05-21'),
(1467, NULL, 11, 10, 150, '2007-05-21'),
(1468, NULL, 11, 10, 151, '2007-05-21'),
(1469, NULL, 11, 10, 152, '2007-05-21'),
(1470, NULL, 11, 10, 153, '2007-05-21'),
(1471, NULL, 11, 10, 154, '2007-05-21'),
(1472, NULL, 11, 10, 155, '2007-05-21'),
(1473, NULL, 11, 10, 156, '2007-05-21'),
(1474, NULL, 11, 10, 157, '2007-05-21'),
(1475, NULL, 11, 10, 158, '2007-05-21'),
(1476, NULL, 11, 10, 159, '2007-05-21'),
(1477, NULL, 11, 10, 160, '2007-05-21'),
(1478, NULL, 11, 10, 161, '2007-05-21'),
(1479, NULL, 11, 10, 162, '2007-05-21'),
(1480, NULL, 11, 10, 163, '2007-05-21'),
(1481, NULL, 11, 10, 164, '2007-05-21'),
(1482, NULL, 11, 10, 165, '2007-05-21'),
(1483, NULL, 11, 10, 166, '2007-05-21'),
(1484, NULL, 11, 10, 167, '2007-05-21'),
(1485, NULL, 11, 10, 168, '2007-05-21'),
(1486, NULL, 11, 10, 169, '2007-05-21'),
(1487, NULL, 11, 10, 170, '2007-05-21'),
(1488, NULL, 11, 10, 171, '2007-05-21'),
(1489, NULL, 11, 10, 172, '2007-05-21'),
(1490, NULL, 11, 10, 173, '2007-05-21'),
(1491, NULL, 11, 10, 174, '2007-05-21'),
(1492, NULL, 11, 10, 175, '2007-05-21'),
(1493, NULL, 11, 10, 176, '2007-05-21'),
(1494, NULL, 11, 10, 177, '2007-05-21'),
(1495, NULL, 11, 10, 178, '2007-05-21'),
(1496, NULL, 11, 10, 179, '2007-05-21'),
(1497, NULL, 11, 10, 180, '2007-05-21'),
(1498, NULL, 11, 10, 181, '2007-05-21'),
(1499, NULL, 11, 10, 182, '2007-05-21'),
(1500, NULL, 11, 10, 183, '2007-05-21'),
(1501, NULL, 11, 10, 184, '2007-05-21'),
(1502, NULL, 11, 10, 185, '2007-05-21'),
(1503, NULL, 11, 10, 186, '2007-05-21'),
(1504, NULL, 11, 10, 187, '2007-05-21'),
(1505, NULL, 11, 10, 188, '2007-05-21'),
(1506, NULL, 11, 10, 189, '2007-05-21'),
(1507, NULL, 11, 10, 190, '2007-05-21'),
(1516, NULL, 6, 10, 197, '2007-05-31'),
(1517, NULL, 7, 10, 197, '2007-05-31'),
(1518, NULL, 6, 10, 198, '2007-05-31'),
(1519, NULL, 7, 10, 198, '2007-05-31'),
(1520, NULL, 6, 10, 199, '2007-05-31'),
(1521, NULL, 7, 10, 199, '2007-05-31'),
(1522, NULL, 6, 10, 200, '2007-05-31'),
(1523, NULL, 7, 10, 200, '2007-05-31'),
(1524, NULL, 6, 10, 201, '2007-05-31'),
(1525, NULL, 7, 10, 201, '2007-05-31'),
(1526, NULL, 6, 10, 202, '2007-05-31'),
(1527, NULL, 7, 10, 202, '2007-05-31'),
(1528, NULL, 6, 10, 203, '2007-05-31'),
(1529, NULL, 7, 10, 203, '2007-05-31'),
(1530, NULL, 6, 10, 204, '2007-05-31'),
(1531, NULL, 7, 10, 204, '2007-05-31'),
(1532, NULL, 6, 10, 205, '2007-05-31'),
(1533, NULL, 7, 10, 205, '2007-05-31'),
(1534, NULL, 6, 10, 206, '2007-05-31'),
(1535, NULL, 7, 10, 206, '2007-05-31'),
(1536, NULL, 6, 10, 207, '2007-05-31'),
(1537, NULL, 7, 10, 207, '2007-05-31'),
(1538, NULL, 6, 10, 208, '2007-05-31'),
(1539, NULL, 7, 10, 208, '2007-05-31'),
(1540, NULL, 6, 10, 209, '2007-05-31'),
(1541, NULL, 7, 10, 209, '2007-05-31'),
(1542, NULL, 6, 10, 210, '2007-05-31'),
(1543, NULL, 7, 10, 210, '2007-05-31'),
(1544, NULL, 6, 10, 211, '2007-05-31'),
(1545, NULL, 7, 10, 211, '2007-05-31'),
(1550, NULL, 6, 10, 214, '2007-05-31'),
(1551, NULL, 7, 10, 214, '2007-05-31'),
(1552, NULL, 6, 10, 215, '2007-05-31'),
(1553, NULL, 7, 10, 215, '2007-05-31'),
(1554, NULL, 6, 10, 216, '2007-06-08'),
(1555, NULL, 7, 10, 216, '2007-06-08'),
(1556, NULL, 6, 10, 217, '2007-06-08'),
(1557, NULL, 7, 10, 217, '2007-06-08'),
(1558, NULL, 6, 10, 218, '2007-06-08'),
(1559, NULL, 7, 10, 218, '2007-06-08'),
(1560, NULL, 6, 10, 219, '2007-06-08'),
(1561, NULL, 7, 10, 219, '2007-06-08'),
(1564, NULL, 6, 10, 221, '2007-06-08'),
(1565, NULL, 7, 10, 221, '2007-06-08'),
(1566, NULL, 6, 10, 222, '2007-06-08'),
(1567, NULL, 7, 10, 222, '2007-06-08'),
(1570, NULL, 6, 10, 213, '2007-06-14'),
(1571, NULL, 7, 10, 213, '2007-06-14'),
(1572, NULL, 6, 10, 192, '2007-06-14'),
(1573, NULL, 7, 10, 192, '2007-06-14'),
(1576, NULL, 6, 10, 194, '2007-06-14'),
(1577, NULL, 7, 10, 194, '2007-06-14'),
(1578, NULL, 6, 10, 212, '2007-06-14'),
(1579, NULL, 7, 10, 212, '2007-06-14'),
(1580, NULL, 6, 10, 223, '2007-06-14'),
(1581, NULL, 7, 10, 223, '2007-06-14'),
(1582, NULL, 6, 10, 224, '2007-06-14'),
(1583, NULL, 7, 10, 224, '2007-06-14'),
(1584, NULL, 6, 10, 225, '2007-06-14'),
(1585, NULL, 7, 10, 225, '2007-06-14'),
(1586, NULL, 6, 10, 226, '2007-06-14'),
(1587, NULL, 7, 10, 226, '2007-06-14'),
(1588, NULL, 6, 10, 227, '2007-06-14'),
(1589, NULL, 7, 10, 227, '2007-06-14'),
(1590, NULL, 6, 10, 228, '2007-06-14'),
(1591, NULL, 7, 10, 228, '2007-06-14'),
(1592, NULL, 6, 10, 229, '2007-06-14'),
(1593, NULL, 7, 10, 229, '2007-06-14'),
(1596, NULL, 6, 10, 231, '2007-07-13'),
(1597, NULL, 7, 10, 231, '2007-07-13'),
(1598, NULL, 6, 10, 232, '2007-07-13'),
(1599, NULL, 7, 10, 232, '2007-07-13'),
(1600, NULL, 6, 10, 233, '2007-07-20'),
(1601, NULL, 7, 10, 233, '2007-07-20'),
(1602, NULL, 6, 10, 234, '2007-07-20'),
(1603, NULL, 7, 10, 234, '2007-07-20'),
(1604, NULL, 6, 10, 235, '2007-07-20'),
(1605, NULL, 7, 10, 235, '2007-07-20'),
(1606, NULL, 6, 10, 236, '2007-07-20'),
(1607, NULL, 7, 10, 236, '2007-07-20'),
(1609, NULL, 6, 10, 239, '2007-08-31'),
(1610, NULL, 6, 10, 240, '2007-08-31'),
(1611, NULL, 6, 10, 241, '2007-08-31'),
(1612, NULL, 6, 10, 242, '2007-08-31'),
(1614, NULL, 6, 10, 244, '2007-08-31'),
(1617, NULL, 6, 10, 247, '2007-08-31'),
(1628, NULL, 7, 10, 239, '2007-08-31'),
(1629, NULL, 7, 10, 240, '2007-08-31'),
(1630, NULL, 7, 10, 241, '2007-08-31'),
(1631, NULL, 7, 10, 242, '2007-08-31'),
(1633, NULL, 7, 10, 244, '2007-08-31'),
(1636, NULL, 7, 10, 247, '2007-08-31'),
(1647, NULL, 6, 10, 257, '2007-09-13'),
(1648, NULL, 7, 10, 257, '2007-09-13'),
(1649, NULL, 6, 10, 258, '2007-09-13'),
(1650, NULL, 7, 10, 258, '2007-09-13'),
(1651, NULL, 6, 10, 259, '2007-09-13'),
(1652, NULL, 7, 10, 259, '2007-09-13'),
(1653, NULL, 6, 10, 260, '2007-09-13'),
(1654, NULL, 7, 10, 260, '2007-09-13'),
(1655, NULL, 6, 10, 261, '2007-09-13'),
(1656, NULL, 7, 10, 261, '2007-09-13'),
(1659, NULL, 11, 10, 249, '2007-11-16'),
(1660, NULL, 6, 10, 249, '2007-11-16'),
(1661, NULL, 7, 10, 249, '2007-11-16'),
(1662, NULL, 11, 10, 245, '2007-11-16'),
(1663, NULL, 6, 10, 245, '2007-11-16'),
(1664, NULL, 7, 10, 245, '2007-11-16'),
(1665, NULL, 11, 10, 250, '2007-11-16'),
(1666, NULL, 6, 10, 250, '2007-11-16'),
(1667, NULL, 7, 10, 250, '2007-11-16'),
(1668, NULL, 11, 10, 251, '2007-11-16'),
(1669, NULL, 6, 10, 251, '2007-11-16'),
(1670, NULL, 7, 10, 251, '2007-11-16'),
(1671, NULL, 11, 10, 252, '2007-11-16'),
(1672, NULL, 6, 10, 252, '2007-11-16'),
(1673, NULL, 7, 10, 252, '2007-11-16'),
(1674, NULL, 11, 10, 253, '2007-11-16'),
(1675, NULL, 6, 10, 253, '2007-11-16'),
(1676, NULL, 7, 10, 253, '2007-11-16'),
(1677, NULL, 11, 10, 254, '2007-11-16'),
(1678, NULL, 6, 10, 254, '2007-11-16'),
(1679, NULL, 7, 10, 254, '2007-11-16'),
(1680, NULL, 11, 10, 255, '2007-11-16'),
(1681, NULL, 6, 10, 255, '2007-11-16'),
(1682, NULL, 7, 10, 255, '2007-11-16'),
(1683, NULL, 11, 10, 256, '2007-11-16'),
(1684, NULL, 6, 10, 256, '2007-11-16'),
(1685, NULL, 7, 10, 256, '2007-11-16'),
(1686, NULL, 11, 10, 246, '2007-11-16'),
(1687, NULL, 6, 10, 246, '2007-11-16'),
(1688, NULL, 7, 10, 246, '2007-11-16'),
(1689, NULL, 11, 10, 248, '2007-11-16'),
(1690, NULL, 6, 10, 248, '2007-11-16'),
(1691, NULL, 7, 10, 248, '2007-11-16'),
(1701, NULL, 11, 10, 263, '2007-11-16'),
(1702, NULL, 6, 10, 263, '2007-11-16'),
(1703, NULL, 7, 10, 263, '2007-11-16'),
(1704, NULL, 11, 10, 264, '2007-11-16'),
(1705, NULL, 6, 10, 264, '2007-11-16'),
(1706, NULL, 7, 10, 264, '2007-11-16'),
(1707, NULL, 11, 10, 265, '2007-11-16'),
(1708, NULL, 6, 10, 265, '2007-11-16'),
(1709, NULL, 7, 10, 265, '2007-11-16'),
(1710, NULL, 11, 10, 266, '2007-11-16'),
(1711, NULL, 6, 10, 266, '2007-11-16'),
(1712, NULL, 7, 10, 266, '2007-11-16'),
(1713, NULL, 11, 10, 243, '2007-11-16'),
(1714, NULL, 6, 10, 243, '2007-11-16'),
(1715, NULL, 7, 10, 243, '2007-11-16'),
(1716, NULL, 11, 10, 238, '2007-11-16'),
(1717, NULL, 6, 10, 238, '2007-11-16'),
(1718, NULL, 7, 10, 238, '2007-11-16'),
(1719, NULL, 11, 10, 262, '2007-11-16'),
(1720, NULL, 6, 10, 262, '2007-11-16'),
(1721, NULL, 7, 10, 262, '2007-11-16'),
(1722, NULL, 11, 10, 267, '2007-11-16'),
(1723, NULL, 6, 10, 267, '2007-11-16'),
(1724, NULL, 7, 10, 267, '2007-11-16'),
(1725, NULL, 11, 10, 268, '2007-11-16'),
(1726, NULL, 6, 10, 268, '2007-11-16'),
(1727, NULL, 7, 10, 268, '2007-11-16'),
(1728, NULL, 6, 12, 269, '2008-01-17'),
(1729, NULL, 6, 12, 270, '2008-01-17'),
(1730, NULL, 6, 12, 272, '2008-01-17'),
(1731, NULL, 6, 12, 271, '2008-01-17'),
(1732, NULL, 7, 12, 269, '2008-01-17'),
(1733, NULL, 6, 10, 273, '2008-01-24'),
(1734, NULL, 7, 10, 273, '2008-01-24'),
(1735, NULL, 6, 10, 220, '2008-01-24'),
(1736, NULL, 7, 10, 220, '2008-01-24'),
(1741, NULL, 6, 10, 274, '2008-01-24'),
(1742, NULL, 7, 10, 274, '2008-01-24'),
(1743, NULL, 6, 10, 230, '2008-01-24'),
(1744, NULL, 7, 10, 230, '2008-01-24'),
(1745, NULL, 6, 10, 275, '2008-01-24'),
(1746, NULL, 7, 10, 275, '2008-01-24'),
(1756, NULL, 11, 10, 276, '2008-01-24'),
(1757, NULL, 6, 10, 276, '2008-01-24'),
(1758, NULL, 7, 10, 276, '2008-01-24'),
(1759, NULL, 11, 10, 277, '2008-01-24'),
(1760, NULL, 6, 10, 277, '2008-01-24'),
(1761, NULL, 7, 10, 277, '2008-01-24'),
(1762, NULL, 11, 10, 192, '2008-01-24'),
(1763, NULL, 11, 10, 212, '2008-01-24'),
(1764, NULL, 11, 10, 214, '2008-01-24'),
(1765, NULL, 11, 10, 216, '2008-01-24'),
(1766, NULL, 11, 10, 218, '2008-01-24'),
(1767, NULL, 11, 10, 193, '2008-01-24'),
(1768, NULL, 11, 10, 194, '2008-01-24'),
(1769, NULL, 11, 10, 195, '2008-01-24'),
(1770, NULL, 11, 10, 196, '2008-01-24'),
(1771, NULL, 11, 10, 199, '2008-01-24'),
(1772, NULL, 11, 10, 200, '2008-01-24'),
(1773, NULL, 11, 10, 201, '2008-01-24'),
(1774, NULL, 11, 10, 202, '2008-01-24'),
(1775, NULL, 11, 10, 203, '2008-01-24'),
(1776, NULL, 11, 10, 204, '2008-01-24'),
(1777, NULL, 11, 10, 205, '2008-01-24'),
(1778, NULL, 11, 10, 206, '2008-01-24'),
(1779, NULL, 11, 10, 211, '2008-01-24'),
(1780, NULL, 11, 10, 213, '2008-01-24'),
(1781, NULL, 11, 10, 215, '2008-01-24'),
(1782, NULL, 11, 10, 217, '2008-01-24'),
(1783, NULL, 11, 10, 219, '2008-01-24'),
(1784, NULL, 11, 10, 223, '2008-01-24'),
(1785, NULL, 11, 10, 224, '2008-01-24'),
(1786, NULL, 11, 10, 225, '2008-01-24'),
(1787, NULL, 11, 10, 226, '2008-01-24'),
(1788, NULL, 11, 10, 227, '2008-01-24'),
(1789, NULL, 11, 10, 228, '2008-01-24'),
(1790, NULL, 11, 10, 229, '2008-01-24'),
(1791, NULL, 11, 10, 257, '2008-01-24'),
(1792, NULL, 11, 10, 258, '2008-01-24'),
(1793, NULL, 11, 10, 259, '2008-01-24'),
(1794, NULL, 11, 10, 260, '2008-01-24'),
(1795, NULL, 11, 10, 261, '2008-01-24'),
(1796, NULL, 11, 10, 273, '2008-01-24'),
(1797, NULL, 11, 10, 221, '2008-01-24'),
(1798, NULL, 11, 10, 278, '2008-01-24'),
(1799, NULL, 11, 10, 279, '2008-01-24'),
(1800, NULL, 11, 10, 280, '2008-01-24'),
(1801, NULL, 11, 10, 220, '2008-01-24'),
(1802, NULL, 11, 10, 222, '2008-01-24'),
(1803, NULL, 11, 10, 230, '2008-01-24'),
(1804, NULL, 11, 10, 231, '2008-01-24'),
(1805, NULL, 11, 10, 232, '2008-01-24'),
(1806, NULL, 11, 10, 233, '2008-01-24'),
(1807, NULL, 11, 10, 234, '2008-01-24'),
(1808, NULL, 11, 10, 235, '2008-01-24'),
(1809, NULL, 11, 10, 236, '2008-01-24'),
(1810, NULL, 11, 10, 274, '2008-01-24'),
(1811, NULL, 11, 10, 275, '2008-01-24'),
(1812, NULL, 11, 10, 60, '2008-04-28'),
(1813, NULL, 6, 10, 60, '2008-04-28'),
(1814, NULL, 7, 10, 60, '2008-04-28');

-- --------------------------------------------------------

--
-- Table structure for table `csm_user_pe`
--

CREATE TABLE IF NOT EXISTS `csm_user_pe` (
  `user_protection_element_id` bigint(20) NOT NULL auto_increment,
  `protection_element_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `update_date` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`user_protection_element_id`),
  UNIQUE KEY `uq_user_protection_element_protection_element_id` (`user_id`,`protection_element_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_protection_element_id` (`protection_element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=52 ;

--
-- Dumping data for table `csm_user_pe`
--

INSERT INTO `csm_user_pe` (`user_protection_element_id`, `protection_element_id`, `user_id`, `update_date`) VALUES
(10, 1, 38, '2006-07-24'),
(50, 2, 51, '2007-05-02'),
(51, 2, 35, '2007-05-02');

-- --------------------------------------------------------

--
-- Table structure for table `cytotoxicity`
--

CREATE TABLE IF NOT EXISTS `cytotoxicity` (
  `cytotoxicity_pk_id` bigint(20) NOT NULL,
  `cell_line` varchar(200) default NULL,
  `cell_death_method` varchar(200) default NULL,
  PRIMARY KEY  (`cytotoxicity_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `cytotoxicity`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_cytotoxicity`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_cytotoxicity` BEFORE DELETE ON `cananolab`.`cytotoxicity`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       cell_line, 
       cell_death_method, 
       deleted_date, 
       table_source)
    values
     ( old.cytotoxicity_pk_id,
       old.cell_line,
       old.cell_death_method,
       sysdate(),
       'CYTOTOXICITY');
end
//
DELIMITER ;

--
-- Dumping data for table `cytotoxicity`
--

INSERT INTO `cytotoxicity` (`cytotoxicity_pk_id`, `cell_line`, `cell_death_method`) VALUES
(1867784, 'LLC-PK1', 'apoptosis'),
(1867785, 'LLC-PK1', 'apoptosis'),
(1867786, 'Hep-G2', 'apoptosis'),
(1867787, 'Hep-G2', 'apoptosis'),
(1867788, 'LLC-PK1', 'apoptosis'),
(1867789, 'LLC-PK1', 'apoptosis'),
(1867790, 'Hep-G2', 'apoptosis'),
(1867791, 'LLC-PK1', 'apoptosis'),
(1867792, 'LLC-PK1', 'apoptosis'),
(1867793, 'Hep-G2', 'apoptosis'),
(1867794, 'Hep-G2', 'apoptosis'),
(2949122, 'Hep-G2', 'necrosis'),
(3768330, 'Hep-G2', 'apoptosis'),
(4227077, 'LLC-PK1', 'apoptosis'),
(4227078, 'HL60', 'apoptosis'),
(4227080, 'LLC-PK1', 'necrosis'),
(4227084, 'LLC-PK1', 'apoptosis'),
(4227085, 'LLC-PK1', 'apoptosis'),
(4227086, 'LLC-PK1', 'apoptosis'),
(4227091, 'LLC-PK1', 'apoptosis'),
(4227098, 'Hep-G2', 'apoptosis'),
(4227099, 'Hep-G2', 'apoptosis'),
(4587520, 'Porcine Renal Tubule', 'apoptosis');

-- --------------------------------------------------------

--
-- Table structure for table `data_status`
--

CREATE TABLE IF NOT EXISTS `data_status` (
  `data_status_pk_id` bigint(20) NOT NULL,
  `status` varchar(20) default NULL,
  `reason` varchar(2000) default NULL,
  PRIMARY KEY  (`data_status_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_status`
--


-- --------------------------------------------------------

--
-- Table structure for table `datum`
--

CREATE TABLE IF NOT EXISTS `datum` (
  `datum_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL,
  `value` decimal(22,3) NOT NULL,
  `value_unit` varchar(200) default NULL,
  `derived_bioassay_data_pk_id` bigint(20) default NULL,
  `control_name` varchar(200) default NULL,
  `control_type` varchar(100) default NULL,
  `list_index` bigint(20) default NULL,
  `statistics_type` varchar(200) default NULL,
  `bioassay_data_category` varchar(2000) default NULL,
  PRIMARY KEY  (`datum_pk_id`),
  KEY `sys_c00246821` (`derived_bioassay_data_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `datum`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_datum`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_datum` BEFORE UPDATE ON `cananolab`.`datum`
 FOR EACH ROW begin
   if new.derived_bioassay_data_pk_id is null then
       insert into history_datum 
        ( datum_pk_id, 
          name, 
          value, 
          value_unit, 
          derived_bioassay_data_pk_id, 
          control_name, 
          control_type, 
          list_index, 
          statistics_type, 
          bioassay_data_category, 
          deleted_date )
       values
        (old.datum_pk_id, 
         old.name, 
         old.value, 
         old.value_unit, 
         old.derived_bioassay_data_pk_id, 
         old.control_name, 
         old.control_type, 
         old.list_index, 
         old.statistics_type, 
         old.bioassay_data_category, 
         sysdate() );
   else 
       delete from history_datum where datum_pk_id=old.datum_pk_id;
   end if;
end
//
DELIMITER ;

--
-- Dumping data for table `datum`
--

INSERT INTO `datum` (`datum_pk_id`, `name`, `value`, `value_unit`, `derived_bioassay_data_pk_id`, `control_name`, `control_type`, `list_index`, `statistics_type`, `bioassay_data_category`) VALUES
(1572865, 'Z-Average', '5.200', 'nm', 1540096, NULL, NULL, 1, NULL, NULL),
(1572866, 'PDI', '0.122', NULL, 1540096, NULL, NULL, 2, NULL, NULL),
(1572868, 'Z-Average', '8.600', 'nm', 1540097, NULL, NULL, 1, NULL, NULL),
(1572869, 'PDI', '0.211', NULL, 1540097, NULL, NULL, 2, NULL, NULL),
(1572870, 'Molecular Weight', '18.150', 'kDa', 1540098, NULL, NULL, 0, NULL, NULL),
(1572871, 'Molecular Weight', '17.680', 'kDa', 1540099, NULL, NULL, 0, NULL, NULL),
(1572872, 'Molecular Weight', '15.100', 'kDa', 1540100, NULL, NULL, 0, NULL, NULL),
(1572873, 'Molecular Weight', '22.310', 'kDa', 1540102, NULL, NULL, 0, NULL, NULL),
(1572874, 'Molecular Weight', '24.390', 'kDa', 1540103, NULL, NULL, 0, NULL, NULL),
(1572875, 'Molecular Weight', '23.900', 'kDa', 1540104, NULL, NULL, 0, NULL, NULL),
(1572877, 'Z-Average', '8.500', 'nm', 1540106, NULL, NULL, 1, NULL, NULL),
(1572878, 'PDI', '0.200', NULL, 1540106, NULL, NULL, 2, NULL, NULL),
(1572879, 'Average', '5.200', 'nm', 1540107, NULL, NULL, 0, NULL, NULL),
(1572880, 'Z-Average', '6.600', 'nm', 1540107, NULL, NULL, 1, NULL, NULL),
(1572881, 'PDI', '0.214', NULL, 1540107, NULL, NULL, 2, NULL, NULL),
(1572883, 'Z-Average', '7.900', 'nm', 1540108, NULL, NULL, 1, NULL, NULL),
(1572884, 'PDI', '0.282', NULL, 1540108, NULL, NULL, 2, NULL, NULL),
(1572891, 'Molecular Weight', '22.000', 'kDa', 1540111, NULL, NULL, 0, NULL, NULL),
(1572892, 'Molecular Weight', '26.280', 'kDa', 1540112, NULL, NULL, 0, NULL, NULL),
(1572893, 'Molecular Weight', '23.670', 'kDa', 1540113, NULL, NULL, 0, NULL, NULL),
(1572894, 'Molecular Weight', '22.720', 'kDa', 1540114, NULL, NULL, 0, NULL, NULL),
(1572895, 'Molecular Weight', '21.630', 'kDa', 1540115, NULL, NULL, 0, NULL, NULL),
(1572897, 'Z-Average', '7.400', 'nm', 1540118, NULL, NULL, 1, NULL, NULL),
(1572898, 'PDI', '0.235', NULL, 1540118, NULL, NULL, 2, NULL, NULL),
(1572900, 'Z-Average', '8.400', 'nm', 1540119, NULL, NULL, 1, NULL, NULL),
(1572901, 'PDI', '0.265', NULL, 1540119, NULL, NULL, 2, NULL, NULL),
(1572903, 'Z-Average', '9.800', 'nm', 1540120, NULL, NULL, 1, NULL, NULL),
(1572904, 'PDI', '0.358', NULL, 1540120, NULL, NULL, 2, NULL, NULL),
(1933312, 'Molecular Weight', '22.000', 'kDa', 1900544, NULL, NULL, 0, NULL, NULL),
(1933313, 'Molecular Weight', '24.170', 'kDa', 1900545, NULL, NULL, 0, NULL, NULL),
(1933314, 'Molecular Weight', '24.620', 'kDa', 1900546, NULL, NULL, 0, NULL, NULL),
(1933315, 'Molecular Weight', '20.740', 'kDa', 1900547, NULL, NULL, 0, NULL, NULL),
(2228224, 'Percent Cell Viability', '99.200', '%', 1900550, NULL, NULL, 0, NULL, NULL),
(2228225, 'Percent Cell Viability', '91.700', '%', 1900550, NULL, NULL, 1, NULL, NULL),
(2228226, 'Percent Cell Viability', '97.000', '%', 1900550, NULL, NULL, 2, NULL, NULL),
(2228227, 'Percent Cell Viability', '78.300', '%', 1900550, NULL, NULL, 3, NULL, NULL),
(2228228, 'Percent Cell Viability', '90.700', '%', 1900550, NULL, NULL, 4, NULL, NULL),
(2228229, 'Percent Cell Viability', '86.800', '%', 1900550, NULL, NULL, 5, NULL, NULL),
(2228230, 'Percent Cell Viability', '85.000', '%', 1900550, NULL, NULL, 6, NULL, NULL),
(2228231, 'Percent Cell Viability', '106.500', '%', 1900550, NULL, NULL, 7, NULL, NULL),
(2228232, 'Percent Cell Viability', '106.300', '%', 1900550, NULL, NULL, 8, NULL, NULL),
(2228233, 'Percent Cell Viability', '86.400', '%', 1900550, NULL, NULL, 9, NULL, NULL),
(2228234, 'Percent Cell Viability', '80.200', '%', 1900550, NULL, NULL, 10, NULL, NULL),
(2228235, 'Percent Cell Viability', '73.200', '%', 1900550, NULL, NULL, 11, NULL, NULL),
(2228236, 'Percent Cell Viability', '104.300', '%', 1900550, NULL, NULL, 12, NULL, NULL),
(2228237, 'Percent Cell Viability', '85.900', '%', 1900550, NULL, NULL, 13, NULL, NULL),
(2228238, 'Percent Cell Viability', '74.600', '%', 1900550, NULL, NULL, 14, NULL, NULL),
(2228239, 'Percent Cell Viability', '88.700', '%', 1900550, NULL, NULL, 15, NULL, NULL),
(2228240, 'Percent Cell Viability', '88.800', '%', 1900550, NULL, NULL, 16, NULL, NULL),
(2228241, 'Percent Cell Viability', '81.700', '%', 1900550, NULL, NULL, 17, NULL, NULL),
(2228242, 'Percent Cell Viability', '70.600', '%', 1900550, NULL, NULL, 18, NULL, NULL),
(2228243, 'Percent Cell Viability', '87.000', '%', 1900550, NULL, NULL, 19, NULL, NULL),
(2228244, 'Percent Cell Viability', '100.300', '%', 1900550, NULL, NULL, 20, NULL, NULL),
(2228245, 'Percent Cell Viability', '74.500', '%', 1900550, NULL, NULL, 21, NULL, NULL),
(2228246, 'Percent Cell Viability', '64.400', '%', 1900550, NULL, NULL, 22, NULL, NULL),
(2228247, 'Percent Cell Viability', '83.400', '%', 1900550, NULL, NULL, 23, NULL, NULL),
(2228248, 'Percent Cell Viability', '101.700', '%', 1900550, NULL, NULL, 24, NULL, NULL),
(2228249, 'Percent Cell Viability', '68.700', '%', 1900550, NULL, NULL, 25, NULL, NULL),
(2228250, 'Percent Cell Viability', '59.800', '%', 1900550, NULL, NULL, 26, NULL, NULL),
(2228251, 'Percent Cell Viability', '72.300', '%', 1900550, NULL, NULL, 27, NULL, NULL),
(2228252, 'Percent Cell Viability', '96.500', '%', 1900550, NULL, NULL, 28, NULL, NULL),
(2228253, 'Percent Cell Viability', '63.000', '%', 1900550, NULL, NULL, 29, NULL, NULL),
(2228254, 'Percent Cell Viability', '58.400', '%', 1900550, NULL, NULL, 30, NULL, NULL),
(2228255, 'Percent Cell Viability', '51.500', '%', 1900550, NULL, NULL, 31, NULL, NULL),
(2228257, 'Percent Cell Viability', '122.400', '%', 1900554, NULL, NULL, 0, NULL, NULL),
(2228258, 'Percent Cell Viability', '99.100', '%', 1900554, NULL, NULL, 1, NULL, NULL),
(2228259, 'Percent Cell Viability', '108.400', '%', 1900554, NULL, NULL, 2, NULL, NULL),
(2228260, 'Percent Cell Viability', '112.800', '%', 1900554, NULL, NULL, 3, NULL, NULL),
(2228261, 'Percent Cell Viability', '96.600', '%', 1900554, NULL, NULL, 4, NULL, NULL),
(2228262, 'Percent Cell Viability', '106.800', '%', 1900554, NULL, NULL, 5, NULL, NULL),
(2228263, 'Percent Cell Viability', '98.100', '%', 1900554, NULL, NULL, 6, NULL, NULL),
(2228264, 'Percent Cell Viability', '166.500', '%', 1900554, NULL, NULL, 7, NULL, NULL),
(2228265, 'Percent Cell Viability', '124.300', '%', 1900554, NULL, NULL, 8, NULL, NULL),
(2228266, 'Percent Cell Viability', '96.000', '%', 1900554, NULL, NULL, 9, NULL, NULL),
(2228267, 'Percent Cell Viability', '112.400', '%', 1900554, NULL, NULL, 10, NULL, NULL),
(2228268, 'Percent Cell Viability', '91.300', '%', 1900554, NULL, NULL, 11, NULL, NULL),
(2228269, 'Percent Cell Viability', '114.600', '%', 1900554, NULL, NULL, 12, NULL, NULL),
(2228270, 'Percent Cell Viability', '109.300', '%', 1900554, NULL, NULL, 13, NULL, NULL),
(2228271, 'Percent Cell Viability', '103.600', '%', 1900554, NULL, NULL, 14, NULL, NULL),
(2228272, 'Percent Cell Viability', '136.300', '%', 1900554, NULL, NULL, 15, NULL, NULL),
(3833856, 'Average', '6.000', 'nm', 3801088, NULL, NULL, 0, NULL, NULL),
(3833857, 'Z-Average', '8.500', 'nm', 3801088, NULL, NULL, 1, NULL, NULL),
(3833858, 'PDI', '0.200', NULL, 3801088, NULL, NULL, 2, NULL, NULL),
(3833859, 'Average', '5.200', 'nm', 3801089, NULL, NULL, 0, NULL, NULL),
(3833860, 'Z-Average', '6.600', 'nm', 3801089, NULL, NULL, 1, NULL, NULL),
(3833861, 'PDI', '0.214', NULL, 3801089, NULL, NULL, 2, NULL, NULL),
(3833862, 'Average', '5.100', 'nm', 3801090, NULL, NULL, 0, NULL, NULL),
(3833863, 'Z-Average', '7.900', 'nm', 3801090, NULL, NULL, 1, NULL, NULL),
(3833864, 'PDI', '0.282', NULL, 3801090, NULL, NULL, 2, NULL, NULL),
(3833865, 'Average', '5.300', 'nm', 3801091, NULL, NULL, 0, NULL, NULL),
(3833866, 'Z-Average', '7.400', 'nm', 3801091, NULL, NULL, 1, NULL, NULL),
(3833867, 'PDI', '0.235', NULL, 3801091, NULL, NULL, 2, NULL, NULL),
(3833868, 'Average', '6.100', 'nm', 3801092, NULL, NULL, 0, NULL, NULL),
(3833869, 'Z-Average', '8.400', 'nm', 3801092, NULL, NULL, 1, NULL, NULL),
(3833870, 'PDI', '0.265', NULL, 3801092, NULL, NULL, 2, NULL, NULL),
(3833871, 'Average', '5.600', 'nm', 3801093, NULL, NULL, 0, NULL, NULL),
(3833872, 'Z-Average', '9.800', 'nm', 3801093, NULL, NULL, 1, NULL, NULL),
(3833873, 'PDI', '0.358', NULL, 3801093, NULL, NULL, 2, NULL, NULL),
(3833874, 'Average', '4.400', 'nm', 3801094, NULL, NULL, 0, NULL, NULL),
(3833875, 'Z-Average', '5.200', 'nm', 3801094, NULL, NULL, 1, NULL, NULL),
(3833876, 'PDI', '0.122', NULL, 3801094, NULL, NULL, 2, NULL, NULL),
(3833877, 'Average', '6.200', 'nm', 3801095, NULL, NULL, 0, NULL, NULL),
(3833878, 'Z-Average', '8.600', 'nm', 3801095, NULL, NULL, 1, NULL, NULL),
(3833879, 'PDI', '0.211', NULL, 3801095, NULL, NULL, 2, NULL, NULL),
(4292608, 'Average', '4.800', 'nm', 4259840, NULL, NULL, 0, NULL, NULL),
(4292609, 'Z-Average', '7.800', 'nm', 4259840, NULL, NULL, 1, NULL, NULL),
(4292610, 'PDI', '0.288', NULL, 4259840, NULL, NULL, 2, NULL, NULL),
(4292612, 'Average', '4.000', 'nm', 4259854, NULL, NULL, 0, NULL, NULL),
(4292613, 'Z-Average', '8.900', 'nm', 4259854, NULL, NULL, 1, NULL, NULL),
(4292614, 'PDI', '0.344', NULL, 4259854, NULL, NULL, 2, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `datum_condition`
--

CREATE TABLE IF NOT EXISTS `datum_condition` (
  `datum_condition_pk_id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `value` decimal(22,3) NOT NULL,
  `value_unit` varchar(200) NOT NULL,
  `datum_pk_id` bigint(20) NOT NULL,
  `list_index` bigint(20) default NULL,
  `statistics_type` varchar(200) default NULL,
  PRIMARY KEY  (`datum_condition_pk_id`),
  KEY `sys_c00246822` (`datum_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `datum_condition`
--

INSERT INTO `datum_condition` (`datum_condition_pk_id`, `name`, `value`, `value_unit`, `datum_pk_id`, `list_index`, `statistics_type`) VALUES
(2260992, 'Particle Concentration', '0.004', 'mg/ml', 2228224, 0, NULL),
(2260993, 'Time', '0.000', 'hours', 2228224, 1, NULL),
(2260994, 'Particle Concentration', '0.004', 'mg/ml', 2228225, 0, NULL),
(2260995, 'Time', '6.000', 'hours', 2228225, 1, NULL),
(2260996, 'Particle Concentration', '0.004', 'mg/ml', 2228226, 0, NULL),
(2260997, 'Time', '24.000', 'hours', 2228226, 1, NULL),
(2260998, 'Particle Concentration', '0.004', 'mg/ml', 2228227, 0, NULL),
(2260999, 'Time', '48.000', 'hours', 2228227, 1, NULL),
(2261000, 'Particle Concentration', '0.008', 'mg/ml', 2228228, 0, NULL),
(2261001, 'Time', '0.000', 'hours', 2228228, 1, NULL),
(2261002, 'Particle Concentration', '0.008', 'mg/ml', 2228229, 0, NULL),
(2261003, 'Time', '6.000', 'hours', 2228229, 1, NULL),
(2261004, 'Particle Concentration', '0.008', 'mg/ml', 2228230, 0, NULL),
(2261005, 'Time', '24.000', 'hours', 2228230, 1, NULL),
(2261006, 'Particle Concentration', '0.008', 'mg/ml', 2228231, 0, NULL),
(2261007, 'Time', '48.000', 'hours', 2228231, 1, NULL),
(2261008, 'Particle Concentration', '0.016', 'mg/ml', 2228232, 0, NULL),
(2261009, 'Time', '0.000', 'hours', 2228232, 1, NULL),
(2261010, 'Particle Concentration', '0.016', 'mg/ml', 2228233, 0, NULL),
(2261011, 'Time', '6.000', 'hours', 2228233, 1, NULL),
(2261012, 'Particle Concentration', '0.016', 'mg/ml', 2228234, 0, NULL),
(2261013, 'Time', '24.000', 'hours', 2228234, 1, NULL),
(2261014, 'Particle Concentration', '0.016', 'mg/ml', 2228235, 0, NULL),
(2261015, 'Time', '48.000', 'hours', 2228235, 1, NULL),
(2261016, 'Particle Concentration', '0.031', 'mg/ml', 2228236, 0, NULL),
(2261017, 'Time', '0.000', 'hours', 2228236, 1, NULL),
(2261018, 'Particle Concentration', '0.016', 'mg/ml', 2228237, 0, NULL),
(2261019, 'Time', '6.000', 'hours', 2228237, 1, NULL),
(2261020, 'Particle Concentration', '0.016', 'mg/ml', 2228238, 0, NULL),
(2261021, 'Time', '24.000', 'hours', 2228238, 1, NULL),
(2261022, 'Particle Concentration', '0.016', 'mg/ml', 2228239, 0, NULL),
(2261023, 'Time', '48.000', 'hours', 2228239, 1, NULL),
(2261024, 'Particle Concentration', '0.063', 'mg/ml', 2228240, 0, NULL),
(2261026, 'Particle Concentration', '0.063', 'mg/ml', 2228241, 0, NULL),
(2261027, 'Time', '6.000', 'hours', 2228241, 1, NULL),
(2261028, 'Particle Concentration', '0.063', 'mg/ml', 2228242, 0, NULL),
(2261029, 'Time', '24.000', 'hours', 2228242, 1, NULL),
(2261030, 'Particle Concentration', '0.063', 'mg/ml', 2228243, 0, NULL),
(2261031, 'Time', '48.000', 'hours', 2228243, 1, NULL),
(2261032, 'Particle Concentration', '0.125', 'mg/ml', 2228244, 0, NULL),
(2261033, 'Time', '0.000', 'hours', 2228244, 1, NULL),
(2261034, 'Particle Concentration', '0.125', 'mg/ml', 2228245, 0, NULL),
(2261035, 'Time', '6.000', 'hours', 2228245, 1, NULL),
(2261036, 'Particle Concentration', '0.125', 'mg/ml', 2228246, 0, NULL),
(2261037, 'Time', '24.000', 'hours', 2228246, 1, NULL),
(2261038, 'Particle Concentration', '0.125', 'mg/ml', 2228247, 0, NULL),
(2261039, 'Time', '48.000', 'hours', 2228247, 1, NULL),
(2261040, 'Particle Concentration', '0.250', 'mg/ml', 2228248, 0, NULL),
(2261041, 'Time', '0.000', 'hours', 2228248, 1, NULL),
(2261042, 'Particle Concentration', '0.250', 'mg/ml', 2228249, 0, NULL),
(2261043, 'Time', '6.000', 'hours', 2228249, 1, NULL),
(2261044, 'Particle Concentration', '0.250', 'mg/ml', 2228250, 0, NULL),
(2261045, 'Time', '24.000', 'hours', 2228250, 1, NULL),
(2261046, 'Particle Concentration', '0.250', 'mg/ml', 2228251, 0, NULL),
(2261047, 'Time', '48.000', 'hours', 2228251, 1, NULL),
(2261048, 'Particle Concentration', '0.500', 'mg/ml', 2228252, 0, NULL),
(2261049, 'Time', '0.000', 'hours', 2228252, 1, NULL),
(2261050, 'Particle Concentration', '0.250', 'mg/ml', 2228253, 0, NULL),
(2261051, 'Time', '6.000', 'hours', 2228253, 1, NULL),
(2261052, 'Particle Concentration', '0.250', 'mg/ml', 2228254, 0, NULL),
(2261053, 'Time', '24.000', 'hours', 2228254, 1, NULL),
(2261054, 'Particle Concentration', '0.250', 'mg/ml', 2228255, 0, NULL),
(2261055, 'Time', '48.000', 'hours', 2228255, 1, NULL),
(2261056, 'Time', '0.000', 'hours', 2228256, 0, NULL),
(2261057, 'Particle Concentration', '0.004', 'mg/ml', 2228257, 0, NULL),
(2261058, 'Time', '0.000', 'hours', 2228257, 1, NULL),
(2261059, 'Particle Concentration', '0.004', 'mg/ml', 2228258, 0, NULL),
(2261060, 'Time', '6.000', 'hours', 2228258, 1, NULL),
(2261061, 'Particle Concentration', '0.004', 'mg/ml', 2228259, 0, NULL),
(2261062, 'Time', '24.000', 'hours', 2228259, 1, NULL),
(2261063, 'Particle Concentration', '0.004', 'mg/ml', 2228260, 0, NULL),
(2261064, 'Time', '48.000', 'hours', 2228260, 1, NULL),
(2261065, 'Particle Concentration', '0.008', 'mg/ml', 2228261, 0, NULL),
(2261066, 'Time', '0.000', 'hours', 2228261, 1, NULL),
(2261067, 'Particle Concentration', '0.008', 'mg/ml', 2228262, 0, NULL),
(2261068, 'Time', '6.000', 'hours', 2228262, 1, NULL),
(2261069, 'Particle Concentration', '0.008', 'mg/ml', 2228263, 0, NULL),
(2261070, 'Time', '24.000', 'hours', 2228263, 1, NULL),
(2261071, 'Particle Concentration', '0.008', 'mg/ml', 2228264, 0, NULL),
(2261072, 'Time', '48.000', 'hours', 2228264, 1, NULL),
(2261073, 'Particle Concentration', '0.016', 'mg/ml', 2228265, 0, NULL),
(2261074, 'Time', '0.000', 'hours', 2228265, 1, NULL),
(2261075, 'Particle Concentration', '0.016', 'mg/ml', 2228266, 0, NULL),
(2261076, 'Time', '6.000', 'hours', 2228266, 1, NULL),
(2261077, 'Particle Concentration', '0.016', 'mg/ml', 2228267, 0, NULL),
(2261078, 'Time', '24.000', 'hours', 2228267, 1, NULL),
(2261079, 'Particle Concentration', '0.016', 'mg/ml', 2228268, 0, NULL),
(2261080, 'Time', '48.000', 'hours', 2228268, 1, NULL),
(2261081, 'Particle Concentration', '0.031', 'mg/ml', 2228269, 0, NULL),
(2261082, 'Time', '0.000', 'hours', 2228269, 1, NULL),
(2261083, 'Particle Concentration', '0.031', 'mg/ml', 2228270, 0, NULL),
(2261084, 'Time', '6.000', 'hours', 2228270, 1, NULL),
(2261085, 'Particle Concentration', '0.031', 'mg/ml', 2228271, 0, NULL),
(2261086, 'Time', '24.000', 'hours', 2228271, 1, NULL),
(2261087, 'Particle Concentration', '0.031', 'mg/ml', 2228272, 0, NULL),
(2261088, 'Time', '48.000', 'hours', 2228272, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `def_activation_method`
--

CREATE TABLE IF NOT EXISTS `def_activation_method` (
  `activation_method_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`activation_method_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_activation_method`
--

INSERT INTO `def_activation_method` (`activation_method_pk_id`, `name`) VALUES
(1, 'MRI'),
(2, 'NMR'),
(3, 'Radiation'),
(4, 'Ultrasound'),
(5, 'Ultraviolet');

-- --------------------------------------------------------

--
-- Table structure for table `def_bioassay_data_category`
--

CREATE TABLE IF NOT EXISTS `def_bioassay_data_category` (
  `category_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL,
  `characterization_name` varchar(2000) NOT NULL,
  PRIMARY KEY  (`category_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_bioassay_data_category`
--

INSERT INTO `def_bioassay_data_category` (`category_pk_id`, `name`, `characterization_name`) VALUES
(1, 'Volume Distribution', 'Size'),
(2, 'Number Distribution ', 'Size'),
(3, 'Intensity Distribution', 'Size'),
(4, 'Mass Distribution', 'Molecular Weight'),
(5, 'Height Distribution', 'Size'),
(6, 'Radius Vs. Time Distribution', 'Size'),
(7, 'Volume Distribution', 'Molecular Weight'),
(8, 'Number Distribution', 'Molecular Weight'),
(9, 'Intensity Vs. m/z Spectrum Distribution', 'Molecular Weight'),
(10, 'Zeta Potential Distribution', 'Surface'),
(11, 'Hemolytic Properties', 'Hemolysis');

-- --------------------------------------------------------

--
-- Table structure for table `def_bond_type`
--

CREATE TABLE IF NOT EXISTS `def_bond_type` (
  `bond_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`bond_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_bond_type`
--

INSERT INTO `def_bond_type` (`bond_type_pk_id`, `name`) VALUES
(1, 'Covalent'),
(2, 'Vander Walls'),
(3, 'Ionic'),
(4, 'Non-specific');

-- --------------------------------------------------------

--
-- Table structure for table `def_cellline_type`
--

CREATE TABLE IF NOT EXISTS `def_cellline_type` (
  `cellline_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`cellline_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_cellline_type`
--

INSERT INTO `def_cellline_type` (`cellline_type_pk_id`, `name`) VALUES
(1, 'Human Hepatocarinma'),
(2, 'Porcine Renal Tubule');

-- --------------------------------------------------------

--
-- Table structure for table `def_characterization_category`
--

CREATE TABLE IF NOT EXISTS `def_characterization_category` (
  `char_category_pk_id` bigint(20) NOT NULL,
  `category` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `has_action` tinyint(4) NOT NULL,
  `category_order` smallint(6) NOT NULL,
  `indent_level` tinyint(4) NOT NULL,
  `name_abbreviation` varchar(200) default NULL,
  PRIMARY KEY  (`char_category_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_characterization_category`
--

INSERT INTO `def_characterization_category` (`char_category_pk_id`, `category`, `name`, `has_action`, `category_order`, `indent_level`, `name_abbreviation`) VALUES
(1, 'Toxicity', 'Oxidative Stress', 1, 2, 1, 'OS'),
(2, 'Toxicity', 'Enzyme Induction', 1, 2, 1, 'EI'),
(3, 'Cytotoxicity', 'Cell Viability', 1, 3, 2, 'CV'),
(4, 'Cytotoxicity', 'Caspase 3 Activation', 1, 3, 2, 'C3'),
(5, 'Blood Contact', 'Platelet Aggregation', 1, 5, 3, 'PA'),
(6, 'Blood Contact', 'Hemolysis', 1, 5, 3, 'HM'),
(7, 'Blood Contact', 'Coagulation', 1, 5, 3, 'CG'),
(8, 'Blood Contact', 'Plasma Protein Binding', 1, 5, 3, 'PB'),
(9, 'Immune Cell Function', 'Complement Activation', 1, 6, 3, 'CA'),
(10, 'Immune Cell Function', 'Phagocytosis', 1, 6, 3, 'PC'),
(11, 'Immune Cell Function', 'Chemotaxis', 1, 6, 3, 'CT'),
(12, 'Immune Cell Function', 'CFU_GM', 1, 6, 3, 'CU'),
(13, 'Immune Cell Function', 'Oxidative Burst', 1, 6, 3, 'OB'),
(14, 'Immune Cell Function', 'Leukocyte Proliferation', 1, 6, 3, 'LP'),
(15, 'Immune Cell Function', 'Cytokine Induction', 1, 6, 3, 'CI'),
(16, 'Immune Cell Function', 'NK Cell Cytotoxic Activity', 1, 6, 3, 'NK'),
(17, 'Physical', 'Size', 1, 0, 0, 'SZ'),
(18, 'Physical', 'Purity', 1, 0, 0, 'PT'),
(19, 'Physical', 'Surface', 1, 0, 0, 'SF'),
(21, 'Physical', 'Solubility', 1, 0, 0, 'SL'),
(22, 'Physical', 'Molecular Weight', 1, 0, 0, 'MW'),
(23, 'Physical', 'Shape', 1, 0, 0, 'SH'),
(24, 'Physical', 'Morphology', 1, 0, 0, 'MP'),
(26, 'In Vitro', 'Toxicity', 0, 1, 0, NULL),
(27, 'Toxicity', 'Cytotoxicity', 1, 2, 1, NULL),
(28, 'Toxicity', 'Immunotoxicity', 1, 2, 1, NULL),
(29, 'Immunotoxicity', 'Blood Contact', 0, 4, 2, NULL),
(30, 'Immunotoxicity', 'Immune Cell Function', 0, 4, 2, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `def_characterization_file_type`
--

CREATE TABLE IF NOT EXISTS `def_characterization_file_type` (
  `file_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL,
  PRIMARY KEY  (`file_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_characterization_file_type`
--

INSERT INTO `def_characterization_file_type` (`file_type_pk_id`, `name`) VALUES
(1, 'Image'),
(2, 'Graph'),
(3, 'SpreadSheet');

-- --------------------------------------------------------

--
-- Table structure for table `def_composing_element_type`
--

CREATE TABLE IF NOT EXISTS `def_composing_element_type` (
  `composing_element_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`composing_element_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_composing_element_type`
--

INSERT INTO `def_composing_element_type` (`composing_element_type_pk_id`, `name`) VALUES
(1, 'core'),
(2, 'shell'),
(3, 'coating'),
(4, 'monomer'),
(5, 'lipid'),
(6, 'modification'),
(7, 'oil'),
(8, 'pfc'),
(9, 'drug'),
(10, 'image contrast agent');

-- --------------------------------------------------------

--
-- Table structure for table `def_datum_name`
--

CREATE TABLE IF NOT EXISTS `def_datum_name` (
  `datum_name_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL,
  `is_datum_parsed` int(2) NOT NULL default '1',
  `characterization_name` varchar(2000) default NULL,
  PRIMARY KEY  (`datum_name_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_datum_name`
--

INSERT INTO `def_datum_name` (`datum_name_pk_id`, `name`, `is_datum_parsed`, `characterization_name`) VALUES
(1, 'PDI', 0, 'Size'),
(2, 'Z-Average', 0, 'Size'),
(3, 'Peak 1', 0, 'Size'),
(4, 'RMS Size', 0, 'Size'),
(5, 'Molecular Weight', 0, 'Molecular Weight'),
(6, 'Zeta Potential', 0, 'Surface'),
(8, 'LC50', 0, 'Cell Viability'),
(9, 'Is Hemolytic', 0, 'Hemolysis'),
(10, 'Is Above Threshold', 0, 'Platelet Aggregation'),
(11, 'Number of CFU-GM Colonies', 0, 'CFU_GM'),
(12, 'Total Number of Bone Marrow Cells', 0, 'CFU_GM');

-- --------------------------------------------------------

--
-- Table structure for table `def_function_agent_target_type`
--

CREATE TABLE IF NOT EXISTS `def_function_agent_target_type` (
  `agent_target_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_function_agent_target_type`
--

INSERT INTO `def_function_agent_target_type` (`agent_target_type_pk_id`, `name`) VALUES
(1, 'Receptor'),
(2, 'Antigen');

-- --------------------------------------------------------

--
-- Table structure for table `def_function_agent_type`
--

CREATE TABLE IF NOT EXISTS `def_function_agent_type` (
  `agent_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_function_agent_type`
--

INSERT INTO `def_function_agent_type` (`agent_type_pk_id`, `name`) VALUES
(1, 'Peptide'),
(2, 'Small Molecule'),
(3, 'Antibody'),
(4, 'DNA'),
(5, 'Image Contrast Agent');

-- --------------------------------------------------------

--
-- Table structure for table `def_function_linkage_type`
--

CREATE TABLE IF NOT EXISTS `def_function_linkage_type` (
  `linkage_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_function_linkage_type`
--

INSERT INTO `def_function_linkage_type` (`linkage_type_pk_id`, `name`) VALUES
(1, 'Attachment'),
(2, 'Encapsulation');

-- --------------------------------------------------------

--
-- Table structure for table `def_function_type`
--

CREATE TABLE IF NOT EXISTS `def_function_type` (
  `function_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`function_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_function_type`
--

INSERT INTO `def_function_type` (`function_type_pk_id`, `name`) VALUES
(1, 'Therapeutic'),
(2, 'Targeting'),
(3, 'Diagnostic Imaging'),
(4, 'Diagnostic Reporting');

-- --------------------------------------------------------

--
-- Table structure for table `def_image_contrast_agent_type`
--

CREATE TABLE IF NOT EXISTS `def_image_contrast_agent_type` (
  `agent_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`agent_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_image_contrast_agent_type`
--

INSERT INTO `def_image_contrast_agent_type` (`agent_type_pk_id`, `name`) VALUES
(1, 'Flurorescent'),
(2, 'Infrared'),
(3, 'MRI'),
(4, 'Neutron Scattering'),
(5, 'PET'),
(6, 'SPECT'),
(7, 'Ultrasound'),
(8, 'X-Ray');

-- --------------------------------------------------------

--
-- Table structure for table `def_measure_type`
--

CREATE TABLE IF NOT EXISTS `def_measure_type` (
  `measure_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`measure_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_measure_type`
--

INSERT INTO `def_measure_type` (`measure_type_pk_id`, `name`) VALUES
(1, 'mean'),
(2, 'median'),
(3, 'observed'),
(4, 'standard deviation'),
(5, 'threshold'),
(6, 'boolean');

-- --------------------------------------------------------

--
-- Table structure for table `def_measure_unit`
--

CREATE TABLE IF NOT EXISTS `def_measure_unit` (
  `measure_unit_pk_id` bigint(20) NOT NULL,
  `unit_name` varchar(50) NOT NULL,
  `description` varchar(1000) default NULL,
  `unit_type` varchar(100) NOT NULL,
  PRIMARY KEY  (`measure_unit_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_measure_unit`
--

INSERT INTO `def_measure_unit` (`measure_unit_pk_id`, `unit_name`, `description`, `unit_type`) VALUES
(1, 'g', NULL, 'Quantity'),
(2, 'mg', NULL, 'Quantity'),
(3, 'ug', NULL, 'Quantity'),
(4, 'g/ml', NULL, 'Concentration'),
(5, 'mg/ml', NULL, 'Concentration'),
(6, 'ug/ml', NULL, 'Concentration'),
(7, 'ug/ul', NULL, 'Concentration'),
(8, 'ml', NULL, 'Volume'),
(9, 'ul', NULL, 'Volume'),
(10, 'nm', NULL, 'Size'),
(12, 'kDa', NULL, 'Molecular Weight'),
(13, 'a.u', NULL, 'Charge'),
(14, 'aC', NULL, 'Charge'),
(15, 'Ah', NULL, 'Charge'),
(16, 'C', NULL, 'Charge'),
(17, 'esu', NULL, 'Charge'),
(18, 'Fr', NULL, 'Charge'),
(19, 'statC', NULL, 'Charge'),
(20, 'nm^2', NULL, 'Area'),
(21, 'mV', NULL, 'Zeta Potential');

-- --------------------------------------------------------

--
-- Table structure for table `def_molecular_formula_type`
--

CREATE TABLE IF NOT EXISTS `def_molecular_formula_type` (
  `molecular_formula_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`molecular_formula_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_molecular_formula_type`
--

INSERT INTO `def_molecular_formula_type` (`molecular_formula_type_pk_id`, `name`) VALUES
(1, 'SMILES'),
(2, 'SMARTS');

-- --------------------------------------------------------

--
-- Table structure for table `def_morphology_type`
--

CREATE TABLE IF NOT EXISTS `def_morphology_type` (
  `morphology_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`morphology_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_morphology_type`
--

INSERT INTO `def_morphology_type` (`morphology_type_pk_id`, `name`) VALUES
(1, 'Powder'),
(2, 'Liquid'),
(3, 'Solid'),
(4, 'Crystalline'),
(5, 'Copolymer'),
(6, 'Fibril'),
(7, 'Colloid'),
(8, 'Oil');

-- --------------------------------------------------------

--
-- Table structure for table `def_protocol_type`
--

CREATE TABLE IF NOT EXISTS `def_protocol_type` (
  `protocol_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_protocol_type`
--

INSERT INTO `def_protocol_type` (`protocol_type_pk_id`, `name`) VALUES
(1, 'Physical assay'),
(2, 'In Vivo assay'),
(3, 'In Vitro assay'),
(4, 'Radio Labeling'),
(5, 'Synthesis'),
(6, 'Sample Preparation'),
(7, 'Safety'),
(4554752, 'Sterility');

-- --------------------------------------------------------

--
-- Table structure for table `def_sample_type`
--

CREATE TABLE IF NOT EXISTS `def_sample_type` (
  `sample_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_sample_type`
--

INSERT INTO `def_sample_type` (`sample_type_pk_id`, `name`) VALUES
(9, 'Complex Particle'),
(8, 'Emulsion'),
(1, 'Dendrimer'),
(2, 'Quantum Dot'),
(3, 'Polymer'),
(4, 'Metal Particle'),
(5, 'Fullerene'),
(6, 'Liposome'),
(7, 'Carbon Nanotube');

-- --------------------------------------------------------

--
-- Table structure for table `def_shape_type`
--

CREATE TABLE IF NOT EXISTS `def_shape_type` (
  `shape_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`shape_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_shape_type`
--

INSERT INTO `def_shape_type` (`shape_type_pk_id`, `name`) VALUES
(1, 'Composite'),
(2, 'Cubic'),
(3, 'Cylinder'),
(4, 'Elliptical'),
(5, 'Ellipsoid'),
(6, 'Hexagonal'),
(7, 'Irregular'),
(8, 'Needle'),
(9, 'Oblate'),
(10, 'Rod'),
(11, 'Spherical'),
(12, 'Tetrahedron'),
(13, 'Tetrapod'),
(14, 'Triangle'),
(15, 'Vesicle');

-- --------------------------------------------------------

--
-- Table structure for table `def_solvent_type`
--

CREATE TABLE IF NOT EXISTS `def_solvent_type` (
  `solvent_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`solvent_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_solvent_type`
--

INSERT INTO `def_solvent_type` (`solvent_type_pk_id`, `name`) VALUES
(1, 'Pooled Blood Serum (PBS)'),
(2, 'Saline '),
(3, 'Water');

-- --------------------------------------------------------

--
-- Table structure for table `def_species_name`
--

CREATE TABLE IF NOT EXISTS `def_species_name` (
  `species_name_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`species_name_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_species_name`
--

INSERT INTO `def_species_name` (`species_name_pk_id`, `name`) VALUES
(1, 'Cat'),
(2, 'Cattle'),
(3, 'Dog'),
(4, 'Goat'),
(5, 'Guinea Pig'),
(6, 'Hamster'),
(7, 'Horse'),
(8, 'Human'),
(9, 'Mouse'),
(10, 'Pig'),
(11, 'Rat'),
(12, 'Sheep'),
(13, 'Yeast'),
(14, 'Zebrafish');

-- --------------------------------------------------------

--
-- Table structure for table `def_storage_type`
--

CREATE TABLE IF NOT EXISTS `def_storage_type` (
  `storage_type_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`storage_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_storage_type`
--

INSERT INTO `def_storage_type` (`storage_type_id`, `name`) VALUES
(1, 'Lab'),
(2, 'Room'),
(3, 'Freezer'),
(4, 'Shelf'),
(5, 'Rack'),
(6, 'Box');

-- --------------------------------------------------------

--
-- Table structure for table `def_surface_group_type`
--

CREATE TABLE IF NOT EXISTS `def_surface_group_type` (
  `surface_group_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`surface_group_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_surface_group_type`
--

INSERT INTO `def_surface_group_type` (`surface_group_type_pk_id`, `name`) VALUES
(1, 'Amine'),
(2, 'Carboxyl'),
(3, 'Hydroxyl');

-- --------------------------------------------------------

--
-- Table structure for table `def_wall_type`
--

CREATE TABLE IF NOT EXISTS `def_wall_type` (
  `wall_type_pk_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`wall_type_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `def_wall_type`
--

INSERT INTO `def_wall_type` (`wall_type_pk_id`, `name`) VALUES
(1, 'SWNT'),
(2, 'DWNT'),
(3, 'MWNT');

-- --------------------------------------------------------

--
-- Table structure for table `dendrimer_composition`
--

CREATE TABLE IF NOT EXISTS `dendrimer_composition` (
  `generation` int(11) default NULL,
  `molecular_formula` varchar(200) default NULL,
  `repeat_unit` varchar(100) default NULL,
  `branch` varchar(200) default NULL,
  `d_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`d_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `dendrimer_composition`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_dendrimer`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_dendrimer` BEFORE DELETE ON `cananolab`.`dendrimer_composition`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       generation, 
       molecular_formula, 
       repeat_unit, 
       branch,
       deleted_date, 
       table_source)
    values
     ( old.d_composition_pk_id,
       old.generation,
       old.molecular_formula,
       old.repeat_unit,
       old.branch,
       sysdate(),
       'DENDRIMER_COMPOSITION');
end
//
DELIMITER ;

--
-- Dumping data for table `dendrimer_composition`
--

INSERT INTO `dendrimer_composition` (`generation`, `molecular_formula`, `repeat_unit`, `branch`, `d_composition_pk_id`) VALUES
(4, NULL, 'PAMAM', '1-2', 1376256),
(4, NULL, NULL, '1-3', 1376263),
(5, NULL, NULL, '1-4', 1376268),
(4, NULL, NULL, NULL, 1376281),
(4, NULL, NULL, '1-3', 1376283),
(5, NULL, NULL, '1-4', 1376285),
(NULL, NULL, NULL, NULL, 1867783);

-- --------------------------------------------------------

--
-- Table structure for table `derived_bioassay_data`
--

CREATE TABLE IF NOT EXISTS `derived_bioassay_data` (
  `derived_bioassay_data_pk_id` bigint(20) NOT NULL,
  `characterization_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  PRIMARY KEY  (`derived_bioassay_data_pk_id`),
  KEY `sys_c00246818` (`characterization_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `derived_bioassay_data`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_derived_biodata`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_derived_biodata` BEFORE UPDATE ON `cananolab`.`derived_bioassay_data`
 FOR EACH ROW begin
   if new.characterization_pk_id is null then
       insert into history_derived_bioassay_data 
        ( derived_bioassay_data_pk_id, 
          characterization_pk_id, 
          list_index, 
          deleted_date )
       values 
        (old.derived_bioassay_data_pk_id, 
         old.characterization_pk_id, 
         old.list_index,
         sysdate());
   else 
       delete from history_derived_bioassay_data where derived_bioassay_data_pk_id=old.derived_bioassay_data_pk_id;
   end if;
end
//
DELIMITER ;

--
-- Dumping data for table `derived_bioassay_data`
--

INSERT INTO `derived_bioassay_data` (`derived_bioassay_data_pk_id`, `characterization_pk_id`, `list_index`) VALUES
(1540096, 1376257, 0),
(1540097, 1376258, 0),
(1540098, 1376259, 0),
(1540099, 1376260, 0),
(1540100, 1376261, 0),
(1540102, 1376264, 0),
(1540103, 1376265, 0),
(1540104, 1376266, 0),
(1540105, 1376267, 0),
(1540106, 1376269, 0),
(1540107, 1376270, 0),
(1540108, 1376271, 0),
(1540109, 1376272, 0),
(1540110, 1376272, 1),
(1540111, 1376273, 0),
(1540112, 1376274, 0),
(1540113, 1376275, 0),
(1540114, 1376275, 1),
(1540115, 1376276, 0),
(1540116, 1376279, 0),
(1540117, 1376280, 0),
(1540118, 1376286, 0),
(1540119, 1376287, 0),
(1540120, 1376288, 0),
(1540121, 1376289, 0),
(1900544, 1867776, 0),
(1900545, 1867777, 0),
(1900546, 1867777, 1),
(1900547, 1867778, 0),
(1900548, 1867781, 0),
(1900549, 1867782, 0),
(1900550, 1867784, 0),
(1900551, 1867785, 0),
(1900552, 1867786, 0),
(1900553, 1867787, 0),
(1900554, 1867788, 0),
(1900555, 1867789, 0),
(1900556, 1867790, 0),
(1900557, 1867791, 0),
(1900558, 1867792, 0),
(1900559, 1867793, 0),
(1900561, 1867795, 0),
(1900562, 1867796, 0),
(1900563, 1867797, 0),
(1900564, 1867798, 0),
(1900565, 1867799, 0),
(1900566, 1867800, 0),
(1900567, 1867801, 0),
(1900568, 1867802, 0),
(1900569, 1867803, 0),
(2457600, 2424832, 0),
(2457601, 2424833, 0),
(2457602, 2424834, 0),
(2457603, 2424835, 0),
(2457604, 2424836, 0),
(2457605, 2424837, 0),
(2457606, 2424838, 0),
(2457607, 2424839, 0),
(2457608, 2424840, 0),
(2457609, 2424841, 0),
(2457610, 2424842, 0),
(2457611, 2424843, 0),
(2457612, 2424844, 0),
(2457613, 2424845, 0),
(2457614, 2424846, 0),
(3801088, 3768320, 0),
(3801089, 3768320, 1),
(3801090, 3768320, 2),
(3801091, 3768321, 0),
(3801092, 3768321, 1),
(3801093, 3768321, 2),
(3801094, 3768322, 0),
(3801095, 3768322, 1),
(3801096, 3768323, 0),
(3801097, 3768324, 0),
(3801098, 3768325, 0),
(3801099, 3768326, 0),
(3801100, 3768327, 0),
(3801101, 3768328, 0),
(3801102, 3768329, 0),
(3801103, 1376262, 0),
(3801104, 1376284, 0),
(3801105, 1376282, 0),
(3801106, 1867794, 0),
(3801107, 3768330, 0),
(3801108, 2424832, 1),
(3801109, 2424832, 2),
(3801110, 2424833, 1),
(3801111, 2424833, 2),
(3801112, 2424834, 1),
(3801113, 2424834, 2),
(3801114, 3768331, 0),
(3801115, 3768332, 0),
(3801116, 3768333, 0),
(3801117, 3768334, 0),
(3801118, 3768335, 0),
(3801119, 3768336, 0),
(3801120, 3768337, 0),
(3801121, 3768338, 0),
(3801122, 3768339, 0),
(3801123, 2424842, 1),
(3801124, NULL, NULL),
(3801125, 2424843, 1),
(3801126, 2424843, 2),
(3801127, 2424841, 1),
(3801128, 2424845, 1),
(3801129, 2424845, 2),
(3801130, 2424844, 1),
(3801131, 2424844, 2),
(3801132, 2424846, 1),
(3801133, 2424846, 2),
(4259840, 4227072, 0),
(4259841, 4227073, 0),
(4259842, 4227074, 0),
(4259843, 4227075, 0),
(4259844, 4227076, 0),
(4259845, 4227076, 1),
(4259846, 4227077, 0),
(4259847, 4227078, 0),
(4259848, 4227079, 0),
(4259849, 4227079, 1),
(4259850, 4227080, 0),
(4259851, 4227080, 1),
(4259852, 4227081, 0),
(4259853, 4227082, 0),
(4259854, 4227083, 0),
(4259855, 4227084, 0),
(4259856, 4227085, 0),
(4259857, 4227086, 0),
(4259858, 4227087, 0),
(4259859, 4227088, 0),
(4259860, 4227089, 0),
(4259861, 4227090, 0),
(4259862, 4227091, 0),
(4259863, 4227092, 0),
(4259864, 4227093, 0),
(4259865, 4227094, 0),
(4259866, 4227095, 0),
(4259867, 4227096, 0),
(4259868, 4227097, 0),
(4259869, 4227098, 0),
(4259870, 4227099, 0),
(4259871, 4227100, 0),
(4456448, 4423680, 0),
(4456449, 4423681, 0),
(4456450, 4423682, 0),
(4456451, 4423683, 0),
(4620288, 4587520, 0),
(4620291, 4587521, 0),
(4620292, 4587522, 0);

-- --------------------------------------------------------

--
-- Table structure for table `derived_sample_container`
--

CREATE TABLE IF NOT EXISTS `derived_sample_container` (
  `parent_container_id` bigint(20) NOT NULL,
  `sample_container_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`parent_container_id`,`sample_container_pk_id`),
  KEY `sys_c00246775` (`sample_container_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `derived_sample_container`
--


-- --------------------------------------------------------

--
-- Table structure for table `emulsion_composition`
--

CREATE TABLE IF NOT EXISTS `emulsion_composition` (
  `emulsion_type` varchar(200) default NULL,
  `molecular_formula` varchar(200) default NULL,
  `polymer_name` varchar(200) default NULL,
  `is_polymerized` tinyint(4) default NULL,
  `e_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`e_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `emulsion_composition`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_emulsion`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_emulsion` BEFORE DELETE ON `cananolab`.`emulsion_composition`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       emulsion_type, 
       molecular_formula, 
       polymer_name, 
       is_polymerized,
       deleted_date, 
       table_source)
    values
     ( old.e_composition_pk_id,
       old.emulsion_type,
       old.molecular_formula,
       old.polymer_name,
       old.is_polymerized,
       sysdate(),
       'EMULSION_COMPOSITION');
end
//
DELIMITER ;

--
-- Dumping data for table `emulsion_composition`
--


-- --------------------------------------------------------

--
-- Table structure for table `fullerene_composition`
--

CREATE TABLE IF NOT EXISTS `fullerene_composition` (
  `number_of_carbon` varchar(200) default NULL,
  `f_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`f_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fullerene_composition`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_fullerene`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_fullerene` BEFORE DELETE ON `cananolab`.`fullerene_composition`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       number_of_carbon,
       deleted_date, 
       table_source)
    values
     ( old.f_composition_pk_id,
       old.number_of_carbon,
       sysdate(),
       'FULLERENE_COMPOSITION');
end
//
DELIMITER ;

--
-- Dumping data for table `fullerene_composition`
--


-- --------------------------------------------------------

--
-- Table structure for table `hibernate_unique_key`
--

CREATE TABLE IF NOT EXISTS `hibernate_unique_key` (
  `next_hi` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hibernate_unique_key`
--

INSERT INTO `hibernate_unique_key` (`next_hi`) VALUES
(144);

-- --------------------------------------------------------

--
-- Table structure for table `history_characterization`
--

CREATE TABLE IF NOT EXISTS `history_characterization` (
  `characterization_pk_id` bigint(20) default NULL,
  `classification` varchar(200) default NULL,
  `source` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `identifier_name` varchar(500) default NULL,
  `name` varchar(100) default NULL,
  `discriminator` varchar(50) default NULL,
  `created_date` datetime default NULL,
  `created_by` varchar(200) default NULL,
  `protocol_file_pk_id` bigint(20) default NULL,
  `instrument_config_pk_id` bigint(20) default NULL,
  `deleted_date` datetime default NULL,
  `chirality` varchar(100) default NULL,
  `growth_diameter` decimal(22,3) default NULL,
  `average_length` decimal(22,3) default NULL,
  `wall_type` varchar(100) default NULL,
  `generation` int(11) default NULL,
  `molecular_formula` varchar(200) default NULL,
  `repeat_unit` varchar(100) default NULL,
  `branch` varchar(200) default NULL,
  `emulsion_type` varchar(200) default NULL,
  `polymer_name` varchar(200) default NULL,
  `is_polymerized` tinyint(4) default NULL,
  `table_source` varchar(500) default NULL,
  `cell_line` varchar(200) default NULL,
  `cell_death_method` varchar(200) default NULL,
  `number_of_carbon` varchar(200) default NULL,
  `type` varchar(100) default NULL,
  `is_cross_link` tinyint(4) default NULL,
  `cross_link_degree` decimal(22,3) default NULL,
  `initiator` varchar(200) default NULL,
  `max_dimension` decimal(22,3) default NULL,
  `min_dimension` decimal(22,3) default NULL,
  `max_dimension_unit` varchar(100) default NULL,
  `min_dimension_unit` varchar(100) default NULL,
  `solvent` varchar(200) default NULL,
  `critical_concentration` decimal(22,3) default NULL,
  `concentration_unit` varchar(100) default NULL,
  `is_soluble` tinyint(4) default NULL,
  `surface_area` decimal(22,3) default NULL,
  `surface_area_unit` varchar(100) default NULL,
  `zeta_potential` decimal(22,3) default NULL,
  `zeta_potential_unit` varchar(100) default NULL,
  `charge` decimal(22,3) default NULL,
  `charge_unit` varchar(100) default NULL,
  `is_hydrophobic` tinyint(4) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_characterization`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_composing_element`
--

CREATE TABLE IF NOT EXISTS `history_composing_element` (
  `composing_element_pk_id` bigint(20) NOT NULL,
  `element_type` varchar(100) default NULL,
  `chemical_name` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  `characterization_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_composing_element`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_datum`
--

CREATE TABLE IF NOT EXISTS `history_datum` (
  `datum_pk_id` bigint(20) NOT NULL,
  `name` varchar(2000) NOT NULL,
  `value` decimal(22,3) NOT NULL,
  `value_unit` varchar(200) default NULL,
  `derived_bioassay_data_pk_id` bigint(20) default NULL,
  `control_name` varchar(200) default NULL,
  `control_type` varchar(100) default NULL,
  `list_index` bigint(20) default NULL,
  `statistics_type` varchar(200) default NULL,
  `bioassay_data_category` varchar(2000) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_datum`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_derived_bioassay_data`
--

CREATE TABLE IF NOT EXISTS `history_derived_bioassay_data` (
  `derived_bioassay_data_pk_id` bigint(20) NOT NULL,
  `characterization_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_derived_bioassay_data`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_lab_file`
--

CREATE TABLE IF NOT EXISTS `history_lab_file` (
  `file_pk_id` bigint(20) NOT NULL,
  `file_name` varchar(500) default NULL,
  `file_uri` varchar(500) default NULL,
  `file_type_extension` varchar(100) default NULL,
  `file_source_type` varchar(100) default NULL,
  `version` varchar(200) default NULL,
  `status` varchar(20) default NULL,
  `reason` varchar(2000) default NULL,
  `created_by` varchar(200) default NULL,
  `created_date` datetime default NULL,
  `sample_sop_pk_id` bigint(20) default NULL,
  `run_pk_id` bigint(20) default NULL,
  `data_status_pk_id` bigint(20) default NULL,
  `title` varchar(500) default NULL,
  `description` varchar(2000) default NULL,
  `comments` varchar(2000) default NULL,
  `type` varchar(200) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_lab_file`
--

INSERT INTO `history_lab_file` (`file_pk_id`, `file_name`, `file_uri`, `file_type_extension`, `file_source_type`, `version`, `status`, `reason`, `created_by`, `created_date`, `sample_sop_pk_id`, `run_pk_id`, `data_status_pk_id`, `title`, `description`, `comments`, `type`, `deleted_date`) VALUES
(107, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2007-08-31 14:31:03');

-- --------------------------------------------------------

--
-- Table structure for table `history_nanoparticle_char`
--

CREATE TABLE IF NOT EXISTS `history_nanoparticle_char` (
  `characterization_pk_id` bigint(20) NOT NULL,
  `nanoparticle_pk_id` bigint(20) NOT NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_nanoparticle_char`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_surface_chemistry`
--

CREATE TABLE IF NOT EXISTS `history_surface_chemistry` (
  `surface_chemistry_pk_id` bigint(20) default NULL,
  `molecule_name` varchar(200) default NULL,
  `surface_pk_id` bigint(20) default NULL,
  `number_molecule` int(11) default NULL,
  `list_index` bigint(20) default NULL,
  `molecular_formula_type` varchar(200) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_surface_chemistry`
--


-- --------------------------------------------------------

--
-- Table structure for table `history_surface_group`
--

CREATE TABLE IF NOT EXISTS `history_surface_group` (
  `surface_group_pk_id` bigint(20) default NULL,
  `name` varchar(100) default NULL,
  `modifier` varchar(100) default NULL,
  `d_composition_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  `deleted_date` datetime default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `history_surface_group`
--


-- --------------------------------------------------------

--
-- Table structure for table `instrument`
--

CREATE TABLE IF NOT EXISTS `instrument` (
  `instrument_pk_id` bigint(20) NOT NULL,
  `type` varchar(200) default NULL,
  `abbreviation` varchar(50) default NULL,
  `manufacturer` varchar(2000) default NULL,
  PRIMARY KEY  (`instrument_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instrument`
--

INSERT INTO `instrument` (`instrument_pk_id`, `type`, `abbreviation`, `manufacturer`) VALUES
(1, 'Asymmetrical Flow Field-Flow Fractionation with Multi-Angle Light Scattering', 'AFFF-MALLS', 'Wyatt'),
(2, 'Atomic Force Microscope', 'AFM', 'Molecular Imaging'),
(3, 'Capillary Electrophoresis', NULL, 'NA'),
(4, 'Clot Detection System', NULL, 'Diagnostica'),
(5, 'Coulter Counter', NULL, 'Beckman/Coulter'),
(6, 'Dynamic Light Scattering', 'DLS', 'Malvern'),
(7, 'Dynamic Light Scattering', 'DLS', 'Wyatt Technologies'),
(8, 'Energy Dispersive Spectroscopy', 'EDS', 'EDAX'),
(9, 'Flow Cytometer', NULL, 'Becton Dickinson'),
(10, 'Hemocytometer', NULL, 'Unknown'),
(11, 'High Performance Liquid Chromatography', 'HPLC', 'Agilent'),
(12, 'High Performance Liquid Chromatography', 'HPLC', 'Shimadzu'),
(13, 'Imaging System', NULL, 'Kodak'),
(14, 'Liquid Chromatography', 'LC', 'Amersham'),
(15, 'Refractometer', NULL, 'Waters'),
(16, 'Scintillation Counter', NULL, 'Beckman/Coulter'),
(17, 'Size Exclusion Chromatography with Multi-Angle Light Scattering', 'SEC-MALLS', 'Wyatt'),
(18, 'Spectrophotometer', NULL, 'Molecular Devices'),
(19, 'Spectrophotometer', NULL, 'Tecan'),
(20, 'Spectrophotometer', NULL, 'Thermo Electron'),
(21, 'Thermal Cycler', NULL, 'Biorad'),
(22, 'Zetasizer', NULL, 'Malvern');

-- --------------------------------------------------------

--
-- Table structure for table `instrument_config`
--

CREATE TABLE IF NOT EXISTS `instrument_config` (
  `instrument_config_pk_id` bigint(20) NOT NULL,
  `description` varchar(4000) default NULL,
  `instrument_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`instrument_config_pk_id`),
  KEY `con_config_instrument` (`instrument_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `instrument_config`
--

INSERT INTO `instrument_config` (`instrument_config_pk_id`, `description`, `instrument_pk_id`) VALUES
(1507328, '', 6),
(1507332, NULL, 1),
(1507344, 'SEC - Agilent\r\nMALLS - Wyatt', 17),
(3571712, NULL, 7),
(4194307, 'Reversed-phase high performance liquid chromatography (RP-HPLC) is a separation technique used for determining the purity of a sample. It is based on the partitioning of the sample molecules with the mobile phase and the stationary hydrophobic phase. The chromatographic system used here consisted of a degasser (Agilent G1379A, Palo Alto, CA), capillary pump (Agilent G1378A), micro well-plate autosampler (Agilent G1377A), Zorbax 300SB-C18 column (1.0 mm ID x 150 mm, 3.5 &#956;m, Agilent), and a diode array detector (Agilent G1315B). The mobile phase consisted of water/acetonitrile (HPLC Grade, 0.14 % (w/v) trifluoroacetic acid) at varying volume percentages with a flow rate of 50 &#956;L/min. The elution profile is illustrated in red and the chromatogram is shown in blue in Figure 5. The injected sample had a concentration of 1mg/mL in a volume of 5 &#956;L (in HPLC water). The eluted sample was detected at 254 nm. Samples were run in triplicate.', 11),
(4653056, '', 6),
(4653057, '', 6);

-- --------------------------------------------------------

--
-- Table structure for table `keyword`
--

CREATE TABLE IF NOT EXISTS `keyword` (
  `keyword_pk_id` bigint(20) NOT NULL,
  `name` varchar(100) default NULL,
  PRIMARY KEY  (`keyword_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keyword`
--

INSERT INTO `keyword` (`keyword_pk_id`, `name`) VALUES
(1048576, 'metal'),
(1048577, 'test'),
(1114112, 'aggregation'),
(3309568, 'demotube'),
(3997696, 'Hydroxy'),
(3997697, 'NCL-20'),
(3997698, 'MRI'),
(3997699, 'PAMAM'),
(3997700, 'Tris'),
(3997701, 'MRI'),
(3997702, 'Pyrrolidinone'),
(3997703, 'NCL-21'),
(3997707, 'NCL-23'),
(3997708, 'MRI'),
(3997709, 'Magnevist'),
(3997710, 'Magnevist'),
(3997711, 'NCL-24'),
(3997712, 'MRI'),
(3997713, 'Hydroxy'),
(3997714, 'Tris'),
(3997715, 'Magnevist'),
(3997716, 'NCL-25'),
(3997717, 'Pyrrolidinone'),
(3997718, 'Magnevist'),
(3997719, 'NCL-26'),
(3997720, 'MRI'),
(4161536, 'DLS'),
(4161537, 'diameter'),
(4161538, 'volume-weighted size'),
(4161542, 'mass spec'),
(4161543, 'dendritic branch'),
(4161544, 'fullerenes'),
(4161545, 'HPLC'),
(4161546, 'absorbance'),
(4161551, 'Zeta Potential'),
(4161552, 'dendrofullerene '),
(4161553, 'DF1'),
(4161554, 'C60-derivative branched '),
(4161558, 'absorbance'),
(4161559, 'UV-Vis'),
(4161560, 'fullerene ring'),
(4161561, 'C60 derivative  DF1-mini'),
(4161562, 'dendrofullerene '),
(4685824, 'MRI'),
(4685825, 'NCL-22'),
(4685826, 'Carboxyl');

-- --------------------------------------------------------

--
-- Table structure for table `keyword_bioassay_data`
--

CREATE TABLE IF NOT EXISTS `keyword_bioassay_data` (
  `keyword_pk_id` bigint(20) NOT NULL,
  `derived_bioassay_data_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`keyword_pk_id`,`derived_bioassay_data_pk_id`),
  KEY `sys_c00246815` (`derived_bioassay_data_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keyword_bioassay_data`
--

INSERT INTO `keyword_bioassay_data` (`keyword_pk_id`, `derived_bioassay_data_pk_id`) VALUES
(4161536, 4259840),
(4161537, 4259840),
(4161538, 4259840),
(4161558, 4259841),
(4161559, 4259841),
(4161560, 4259841),
(4161542, 4259842),
(4161543, 4259842),
(4161544, 4259842),
(4161545, 4259843),
(4161546, 4259843);

-- --------------------------------------------------------

--
-- Table structure for table `keyword_nanoparticle`
--

CREATE TABLE IF NOT EXISTS `keyword_nanoparticle` (
  `keyword_pk_id` bigint(20) NOT NULL,
  `nanoparticle_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`keyword_pk_id`,`nanoparticle_pk_id`),
  KEY `sys_c00246793` (`nanoparticle_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keyword_nanoparticle`
--

INSERT INTO `keyword_nanoparticle` (`keyword_pk_id`, `nanoparticle_pk_id`) VALUES
(1048576, 524288),
(1048577, 524288),
(3997696, 917504),
(3997697, 917504),
(3997698, 917504),
(3997699, 917504),
(3997700, 917504),
(3997701, 917505),
(3997702, 917505),
(3997703, 917505),
(4685824, 917506),
(4685825, 917506),
(4685826, 917506),
(3997707, 917507),
(3997708, 917507),
(3997709, 917507),
(3997710, 917508),
(3997711, 917508),
(3997712, 917509),
(3997713, 917509),
(3997714, 917509),
(3997715, 917509),
(3997716, 917509),
(3997717, 917510),
(3997718, 917510),
(3997719, 917510),
(3997720, 917510),
(4161552, 4063235),
(4161553, 4063235),
(4161554, 4063235),
(4161561, 4063236),
(4161562, 4063236);

-- --------------------------------------------------------

--
-- Table structure for table `lab_file`
--

CREATE TABLE IF NOT EXISTS `lab_file` (
  `file_pk_id` bigint(20) NOT NULL,
  `file_name` varchar(500) default NULL,
  `file_uri` varchar(500) default NULL,
  `file_type_extension` varchar(100) default NULL,
  `file_source_type` varchar(100) default NULL,
  `version` varchar(200) default NULL,
  `status` varchar(20) default NULL,
  `reason` varchar(2000) default NULL,
  `created_by` varchar(200) default NULL,
  `created_date` datetime default NULL,
  `sample_sop_pk_id` bigint(20) default NULL,
  `run_pk_id` bigint(20) default NULL,
  `data_status_pk_id` bigint(20) default NULL,
  `title` varchar(500) default NULL,
  `description` varchar(2000) default NULL,
  `comments` varchar(2000) default NULL,
  `type` varchar(200) default NULL,
  PRIMARY KEY  (`file_pk_id`),
  KEY `sys_c00246777` (`run_pk_id`),
  KEY `sys_c00246778` (`sample_sop_pk_id`),
  KEY `sys_c00246779` (`data_status_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `lab_file`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_lab_file`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_lab_file` BEFORE DELETE ON `cananolab`.`lab_file`
 FOR EACH ROW begin
   
    insert into history_lab_file
     ( file_pk_id,
       file_name,
       file_uri,
       file_type_extension,
       file_source_type,
       version,
       status,
       reason,
       created_by,
       created_date,
       sample_sop_pk_id,
       run_pk_id,
       data_status_pk_id,
       title,
       description,
       comments,
       type,
       deleted_date )   
    values
     ( old.file_pk_id,
       old.file_name,
       old.file_uri,
       old.file_type_extension,
       old.file_source_type,
       old.version,
       old.status,
       old.reason,
       old.created_by,
       old.created_date,
       old.sample_sop_pk_id,
       old.run_pk_id,
       old.data_status_pk_id,
       old.title,
       old.description,
       old.comments,
       old.type,
       sysdate());
end
//
DELIMITER ;

--
-- Dumping data for table `lab_file`
--

INSERT INTO `lab_file` (`file_pk_id`, `file_name`, `file_uri`, `file_type_extension`, `file_source_type`, `version`, `status`, `reason`, `created_by`, `created_date`, `sample_sop_pk_id`, `run_pk_id`, `data_status_pk_id`, `title`, `description`, `comments`, `type`) VALUES
(1, NULL, '/doc/solubilized.doc', 'doc', 'SOP', NULL, NULL, NULL, NULL, NULL, 4, NULL, NULL, NULL, NULL, NULL, NULL),
(2, NULL, '/doc/lyophilized.doc', 'doc', 'SOP', NULL, NULL, NULL, NULL, NULL, 3, NULL, NULL, NULL, NULL, NULL, NULL),
(101, 'NCL_Method_GTA-1.pdf', 'protocols/20071116_13-53-18-596_NCL_Method_GTA-1.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'MTT AND LDH RELEASE (PORCINE RENAL PROXIMAL TUBULE CELL)', NULL, NULL, NULL),
(102, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(103, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(104, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(105, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(106, 'NCL_Method_GTA-7.pdf', 'protocols/20071116_14-04-21-271_NCL_Method_GTA-7.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'ROS', NULL, NULL, NULL),
(108, 'NCL_Method_ITA-1.pdf', 'protocols/20071116_13-02-45-395_NCL_Method_ITA-1.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'HEMOLYSIS', NULL, NULL, NULL),
(109, 'NCL_Method_ITA-10.pdf', 'protocols/20071116_13-51-24-457_NCL_Method_ITA-10.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'CYTOKINE INDUCTION', 'Cytokine Induction (6 assays): TNFa, IL-1b, IL-6, IL-8, IL-10, IL-12\r\n', NULL, NULL),
(110, NULL, NULL, NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(111, 'NCL_Method_ITA-12.pdf', 'protocols/20071116_13-52-11-208_NCL_Method_ITA-12.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'COAGULATION', NULL, NULL, NULL),
(112, 'NCL_Method_ITA-2.pdf', 'protocols/20071116_13-05-54-234_NCL_Method_ITA-2.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'PLATELET AGGREGATION', NULL, NULL, NULL),
(113, 'NCL_Method_ITA-3.pdf', 'protocols/20071116_13-44-35-590_NCL_Method_ITA-3.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'CFU-GM', NULL, NULL, NULL),
(114, 'NCL_Method_ITA-4.pdf', 'protocols/20071116_13-46-11-313_NCL_Method_ITA-4.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'PLASMA PROTEIN BINDING (2D PAGE)', NULL, NULL, NULL),
(115, 'NCL_Method_ITA-5.pdf', 'protocols/20071116_13-46-55-329_NCL_Method_ITA-5.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'COMPLEMENT ACTIVATION', NULL, NULL, NULL),
(116, 'NCL_Method_ITA-6.pdf', 'protocols/20071116_13-47-49-385_NCL_Method_ITA-6.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'LEUKOCYTE PROLIFERATION', NULL, NULL, NULL),
(117, 'NCL_Method_ITA-7.pdf', 'protocols/20071116_13-48-42-707_NCL_Method_ITA-7.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'OXIDATIVE BURST (NO)', NULL, NULL, NULL),
(118, 'NCL_Method_ITA-8.pdf', 'protocols/20071116_13-49-29-255_NCL_Method_ITA-8.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'CHEMOTAXIS', NULL, NULL, NULL),
(119, 'NCL_Method_ITA-9.pdf', 'protocols/20071116_13-50-15-971_NCL_Method_ITA-9.pdf', NULL, NULL, '1.0', NULL, NULL, 'data_migration', '2007-08-31 09:59:21', NULL, NULL, NULL, 'PHAGOCYTOSIS', NULL, NULL, NULL),
(1081344, 'assay-result.txt', '/particles/NCL-52-A009/plateAggregation/20061208_16-28-21-370_assay-result.txt', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'metal aggregation', NULL, NULL, NULL),
(1474574, 'NCL200612A_fig 13.jpg', '/particles/NCL-23-1/size/20061211_15-29-45-831_NCL200612A_fig 13.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 13', 'The effect of hydrodynamic size on NCL23 concentration in PBS.  DLS batch measurements on NCL23 in PBS indicate no dependence on dendrimer concentration for the hydrodynamic diameter (indicated as "peak1" in graph).  The Z-average remains fairly high and subject to large variation across measurements when the sample is prefiltered using a 0.1- or 0.02-um-pore-size alumina membrane.', NULL, NULL),
(1540096, 'NCL200612A_fig 7.jpg', '/particles/NCL-20-1/size/20061211_9-20-14-537_NCL200612A_fig 7.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 7', 'Statistics graph based on size distribution by volume for NCL20 in saline at 25 degrees celsius', NULL, NULL),
(1540097, 'NCL200612A_fig 8.jpg', '/particles/NCL-20-1/size/20061211_9-42-18-806_NCL200612A_fig 8.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 8', 'Statistics graph based on size distribution by volume for NCL20 in PBS at 25 degrees celcius', NULL, NULL),
(1540098, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540099, 'NCL200612A_fig 25.jpg', '/particles/NCL-20-1/molecularWeight/20061211_9-52-21-327_NCL200612A_fig 25.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 25', 'SEC Chromatogram of NCL 20.  The molar mass distribution plot for NCL20 is shown.  The caluclated molar mass for NCL20 was 17.68 kDa, and the polydispersity index was 1.001.  The moleclar weight determined by SEC-MALLS is very close to the theorectical molecular weight of 18.15 kDa.', NULL, NULL),
(1540100, 'NCL200612A_fig 28.jpg', '/particles/NCL-20-1/molecularWeight/20061211_9-57-58-621_NCL200612A_fig 28.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 28', 'Molar Mass versus elution time plot of NCL20 by AFFF-MALLS.  Concentration: 3.38 mg/ml; Conditions: Injection volume: 100 uL; 10 kDa AFFF regenerated cellulose membrane, 350 um channel thickness, 1 mL/min channel flow, 3 mL/min cross-flow. The calulated molar mass for NCL20 was 15.1 kDa, and the polydispersity index was 1.077.', NULL, NULL),
(1540102, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540103, 'Figure 26.pdf', '/particles/NCL-21-1/molecularWeight/20070412_20-13-45-379_Figure 26.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'SEC chromatogram of NCL21', 'The molar mass distribution plot for NCL21 illustrates a main peak with a front shoulder (roughly between 7.5 and 9 mL). The calculated molar mass for the main peak and its front shoulder was 24.39 kDa and 47.02 kDa, respectively, and the polydispersity index was 1.005 and 1.014, respectively. The front shoulder has a molecular weight roughly double that of the main peak, suggesting that the front shoulder is a dimer. The molecular weight determined by SEC-MALLS for the monomer is slightly higher than the theoretical molecular weight of 22.31 kDa.', NULL, NULL),
(1540104, 'Figure 29.pdf', '/particles/NCL-21-1/molecularWeight/20070412_20-18-25-955_Figure 29.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Molar mass versus elution time plot of NCL21 by AFFF-MALLS.', 'Concentration: 2.7 mg/mL; Conditions: injection volume: 100 &#956;L, 10kDa AFFF regenerated cellulose membrane, 350 &#956;m channel thickness, 1 mL/min channel flow, 3 mL/min cross-flow. For molecular weight determinations, the dn/dc value is needed and was assumed to be the same as NCL22 (0.1677 mL/g). The calculated molar mass for the major peak and its shoulder peak was 23.9 kDa and 45.3 kDa, respectively, and the polydispersity index was 1.141 and 1.281, respectively. The shoulder peak has a molecular weight roughly double that of the major peak, which is indicative of a dimer.', NULL, NULL),
(1540105, 'NCL200612A_fig 20.jpg', '/particles/NCL-21-1/purity/20061211_10-30-46-773_NCL200612A_fig 20.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT CL200612A Fig 20', 'HPLC chromatogram for NCL21. The chromatogram and elution profile for NCL21 are shown.  The retention time for the main peak is 49.3 and corresponds to 96% +- 1% of the total area.  The UV spectra (data not shown) for this peak are consistent with that of NCL21 measured with a UV-VIS spectrometer.  The nature of the broad peak at 89.6 min is unknown.', NULL, NULL),
(1540106, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540107, 'Figure 2.pdf', '/particles/NCL-22-1/size/20070411_14-44-17-630_Figure 2.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL22 in PBS at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL22 in PBS at 25 Â°C.', NULL, NULL),
(1540108, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540109, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540110, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540111, 'NCL200612A_fig 14.jpg', '/particles/NCL-22-1/molecularWeight/20061211_11-06-47-966_NCL200612A_fig 14.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mass spectra for (a) NCL22 and (b) NCL23.  The theoretical molecular weight of NCL 22 is 26.28 kDa.  The actual/experimental result based on MS was 22 kDa for both samples.  The experimental details are as follows: DHB matrix, 10 mg/mL. CH CN/H O = 3/7 (v/v). Molecular weight spectra obtained by MALDI-TOF, with a major peak at 22 kDa and minor peaks centered around 43 kDa and 64 kDa, are consistent with the information provided by DNT for G4.5 NaCOO dendrimer samples NCL22 and NCL23. Incorporating Magnevist did not change the spectrum of NCL23.', NULL, NULL),
(1540112, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL),
(1540113, 'Figure23.pdf', '/particles/NCL-22-1/molecularWeight/20070412_20-08-08-385_Figure23.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'SEC chromatograms of NCL22 using two different columns.', 'SEC is a separation technique used for determining the purity of a sample and is based on the molecular size of the sample components. Coupled with a MALLS and RI detectors, the molecular weight and root mean square (rms) radius can be determined for the fractionated sample.Experimental conditions: The chromatographic system consisted of an isocratic pump (Agilent G1310A, Palo Alto, CA); Wyatt Injection System (Wyatt Technology, Santa Barbara, CA); and TosoHaas TSKgel Guard PWH 06762 (7.5 mm ID x 7.5 cm, 12 &#956;m), TSKgel G3000PW 05762 (7.5 mm ID x 30 cm, 10 &#956;m), and TSKgel G4000PW 05763 (7.5 mm ID x 30 cm, 17 &#956;m) columns (TosoHaas, Montgomeryville, PA). The size exclusion column was connected in-line to a light scattering detector (DAWN EOS, 690 nm laser, Wyatt Technology, Santa Barbara, CA) and a refractive index detector (Optilab rEX, Wyatt Technology, Santa Barbara, CA). The isocratic mobile phase was PBS (1x, pH 7.5, Sigma D1408, St. Louis, MO) at a flow rate of 1 mL/min. Sample concentration was typically 2 mg/mL in PBS, and 100 &#956;L was manually injected. Molecular weights were determined using Astra V 5.1.9.1 (Wyatt Technology, Santa Barbara, CA). A dn/dc value of 0.1677 mL/g (measured separately using the Wyatt Optilab rEX for NCL-22) was used for all molecular weight determinations. The molar mass distribution plot for NCL-22 using two different size exclusion columns is shown. NCL-22 elutes faster using the G3000 column. The calculated molar mass for NCL-22 was 23.67 kDa and 22.72 kDa, and the polydispersity index was 1.001 and 1.001 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS is slightly smaller than the theoretical molecular weight of 26.28 kDa.', NULL, NULL),
(1540114, 'Figure23.pdf', '/particles/NCL-22-1/molecularWeight/20070412_20-09-35-806_Figure23.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'SEC chromatograms of NCL22 using two different columns.', 'SEC is a separation technique used for determining the purity of a sample and is based on the molecular size of the sample components. Coupled with a MALLS and RI detectors, the molecular weight and root mean square (rms) radius can be determined for the fractionated sample.Experimental conditions: The chromatographic system consisted of an isocratic pump (Agilent G1310A, Palo Alto, CA); Wyatt Injection System (Wyatt Technology, Santa Barbara, CA); and TosoHaas TSKgel Guard PWH 06762 (7.5 mm ID x 7.5 cm, 12 &#956;m), TSKgel G3000PW 05762 (7.5 mm ID x 30 cm, 10 &#956;m), and TSKgel G4000PW 05763 (7.5 mm ID x 30 cm, 17 &#956;m) columns (TosoHaas, Montgomeryville, PA). The size exclusion column was connected in-line to a light scattering detector (DAWN EOS, 690 nm laser, Wyatt Technology, Santa Barbara, CA) and a refractive index detector (Optilab rEX, Wyatt Technology, Santa Barbara, CA). The isocratic mobile phase was PBS (1x, pH 7.5, Sigma D1408, St. Louis, MO) at a flow rate of 1 mL/min. Sample concentration was typically 2 mg/mL in PBS, and 100 &#956;L was manually injected. Molecular weights were determined using Astra V 5.1.9.1 (Wyatt Technology, Santa Barbara, CA). A dn/dc value of 0.1677 mL/g (measured separately using the Wyatt Optilab rEX for NCL-22) was used for all molecular weight determinations. The molar mass distribution plot for NCL-22 using two different size exclusion columns is shown. NCL-22 elutes faster using the G3000 column. The calculated molar mass for NCL-22 was 23.67 kDa and 22.72 kDa, and the polydispersity index was 1.001 and 1.001 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS is slightly smaller than the theoretical molecular weight of 26.28 kDa.', NULL, NULL),
(1540115, 'NCL200612A_fig 27.jpg', '/particles/NCL-22-1/molecularWeight/20061211_11-29-07-542_NCL200612A_fig 27.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 27', 'Molar mass versus elution time plot of NCL22 and NCL23 by AFFF-MALLS.  Concentration of NCL22: 1 mg/mL in H O; concentration of NCL23: 2 mg/mL in PBS; Conditions: Injection volume: 100 uL; 10kDa regenerated cellulose membrane; 350 um channel thickness; 1 mL/min channel flow; 3 mL/min cross-flow. AFFF is an innovative separation method for an efficient separation and characterization of nanoparticles, polymers, and proteins that is both fast and gentle.  When coupled with a MALLS system, the molar mass and rms radius can be obtained for the fractionated sample.  The molar mass distribution plot shows that NCL22 and NCL23 has similar molar mass by using AFFF as separation method.  The calculated molar mass of NCL22 and NCL23 was 21.63 kDa and 20.74 kDa, and the polydispersity index was 1.046 and 1.078, respectively (the molar mass of both NCL22 and NCL23 was determined by using the dn/dc value of NCL22, which was measured using an RI detector).', NULL, NULL),
(1540116, 'Figure 16.pdf', '/particles/NCL-22-1/purity/20070412_17-07-30-254_Figure 16.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'HPLC Chromatogram for NCL22', 'Reversed-phase high-performance liquid chromatography (RP-HPLC) is a separation technique used for determining the purity of a sample. It is based on the partitioning of the sample molecules with the mobile phase and the stationary, hydrophobic phase. The chromatographic system consisted of a degasser (Agilent G1379A, Palo Alto, CA), capillary pump (Agilent G1378A), micro well-plate autosampler (Agilent G1377A), Zorbax 300SB-C18 column (1.0 mm ID x 150 mm, 3.5 &#956;m, Agilent), and a diode array detector (Agilent G1315B). The mobile phase consisted of water/acetonitrile (HPLC-grade, 0.14 % [w/v] trifluoroacetic acid) at varying volume percentages at a flow rate of 50 &#956;L/min. The elution gradient for each sample is shown in red on the chromatograms. The sample volume injected was 5 &#956;L, typically at a concentration of 1 mg/mL in HPLC water, and the eluted sample was detected at 210 nm. Samples were run in triplicate. The chromatogram and elution profile for NCL22 are shown. The retention time for the main peak is 37.9 min and corresponds to 85% Â± 2% of the total area (based on peaks eluted between 36.7 and 39.7 min). The UV spectra (data not shown) for these peaks are consistent with that of NCL22 measured with a UV-Vis spectrophotometer. The nature of the broad peak at 47.2 min is unknown.', NULL, NULL),
(1540117, 'NCL200612A_fig 22.jpg', '/particles/NCL-22-1/purity/20061211_12-47-57-977_NCL200612A_fig 22.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 22', 'Typical electropherogram of (A) NCL22 and (B) NCL23.  Sample concentration: 0.1 mg/mL in water; capillary:  40cm x 50um I.D; buffer: 20mM sodium phosphate (pH=7.4); separation voltage: -14kV; Injection pressure: 0.5 psi/20s; detector: UV (wavelength 200 nm). CE is a powerful chromatographic technique that separates analytes on the basis of electrophretic mobility differences.  Mobility is determined by the mass-to-charge ratio of the analyte.  CE has high separation efficiencies, high sensitivity, short run time and high automation capability.  CE is extensively used to evaluate the molecular distribution of dendrimers, since the charge distribution and electrophretic mobility often change upon dendrimer surface conjugation.  Figure 22 shows the NCL22 and NCL23 have very similar electorphoretic mobilities (both are in the range of [4.3-5.2] x 10 cm V S), which indicates they have the same charge/mass ratio.', NULL, NULL),
(1540118, 'NCL200612A_fig 4.jpg', '/particles/NCL-23-1/size/20061211_14-49-01-102_NCL200612A_fig 4.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 4', 'Statistics graph based on size distribution by volume for NCL23 in saline at 25 degrees Celsius', NULL, NULL),
(1540119, 'NCL200612A_fig 5.jpg', '/particles/NCL-23-1/size/20061211_14-54-29-189_NCL200612A_fig 5.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 5', 'Statistics graph based on size distribution by volume for NCL23 in PBS at 37 degrees Celsius', NULL, NULL),
(1540120, 'NCL200612A_fig 6.jpg', '/particles/NCL-23-1/size/20061211_15-15-15-306_NCL200612A_fig 6.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 6', 'Statistics graph based on size distribution by volume for NCL23 in PBS at 37 degrees Celsius', NULL, NULL),
(1540121, 'NCL200612A_fig 10.jpg', '/particles/NCL-23-1/size/20061211_15-25-24-13_NCL200612A_fig 10.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 10', 'The intensity-weighted (A) and volume-weighted size distribution (B) plots for NCL22 and NCL23. Multiple DLS measurements of NCL22 and NCL23 at 2000 ppm (2 mg/mL) in 10 mM NaCl were averaged and presented as intensity (Figure 10, A) and volume (Figure 10, B) distributions calculated using a non-negative least squares (NNLS) fit to the inverse Laplace transform.  Limited data suggest a slight increase in size occurs because of the presence of Gd-complex, but more extensive data are needed to confirm this suggestion.  For particles in the sub-100 nm range, the scattered intensity exhibits a d dependence, where d is the diameter.  In other words, a single 100-nm particle will scatter roughly the same amount of light as 1,000,000 particles with a diameter of 1 nm.  That is why the conversion to volume from intensity indicates that the smaller mode is predominant on a volume (or number) basis, and the large mode virtually disappears.  The smaller particle size peak below 10 nm is identified as the "primary" size.', NULL, NULL),
(1802240, 'NCL200612A_fig 14.jpg', '/particles/NCL-23-1/molecularWeight/20061211_15-45-58-672_NCL200612A_fig 14.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 14', 'Mass spectra for (A) NCL 22 and (B) NCL23.  The theoretical molecular weight of NCL22 is 26.28 kDa.  The actual/experimental result based on MS was 22 kDa for both samples.  The experimental details are as follows: DHB matrix, 10 mg/mL. CH CN/H O = 3/7 (v/v).  Molecular weight spectra obtained by MALDI-TOF, waith a major peak at 22 kDa and minor peaks centered around 43 kDa and 64 kDa, are consistent with the information provided by DNT for G4.5 NaCOO dendrimer samples NCL22 and NCL23. Incorporating Magnevist did not change the spectrum of NCL23.', NULL, NULL),
(1802243, 'NCL200612A_fig 24.jpg', '/particles/NCL-23-1/molecularWeight/20061211_15-54-37-428_NCL200612A_fig 24.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 24', 'SEC chromatograms of NCL23 using two different columns. The molar mass distribution plot for NCL23 using 2 different size exclusion columns is shown.  NCL23 elutes faster using the G3000 column.  The calculated molar mass for NCL23 was 24.17 kDa and 24.62 kDa, and the polydispersity index was 1.003 and 1032 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS for NCL23 is very similar to that of NCL22. Since NCL23 is NCL22 with incorporated Magnevist (noncovalent inclusion complex), the results suggest that Magnevist is no longer incorporated with the dendrimer after fractionation.', NULL, NULL),
(1802244, 'NCL200612A_fig 24.jpg', '/particles/NCL-23-1/molecularWeight/20061211_15-55-52-696_NCL200612A_fig 24.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'DNT NCL200612A Fig 24', 'SEC chromatograms of NCL23 using two different columns. The molar mass distribution plot for NCL23 using 2 different size exclusion columns is shown.  NCL23 elutes faster using the G3000 column.  The calculated molar mass for NCL23 was 24.17 kDa and 24.62 kDa, and the polydispersity index was 1.003 and 1032 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS for NCL23 is very similar to that of NCL22. Since NCL23 is NCL22 with incorporated Magnevist (noncovalent inclusion complex), the results suggest that Magnevist is no longer incorporated with the dendrimer after fractionation', NULL, NULL),
(1802260, 'NCL200612A_fig 32.jpg', '/particles/NCL-24-1/cellViability/20061211_17-41-50-651_NCL200612A_fig 32.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 32', 'LDH cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1).  Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(1802261, 'NCL200612A_fig 32.jpg', '/particles/NCL-24-1/cellViability/20061211_17-42-04-305_NCL200612A_fig 32.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 32', 'LDH cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1).  Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(1802263, 'NCL200612A_fig 33.jpg', '/particles/NCL-24-1/cellViability/20061211_17-45-38-382_NCL200612A_fig 33.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 33', 'MTT cytotoxicity assay in Hep-G2 cells. Hep-G2 cells were treated for 6, 24, and 48 h with 0.02-5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 100% control viability.', NULL, NULL),
(1900544, 'NCL200612A_fig 14.jpg', '/particles/NCL-23-1/molecularWeight/20061211_15-47-58-562_NCL200612A_fig 14.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mass spectra for (A) NCL 22 and (B) NCL23.  The theoretical molecular weight of NCL22 is 26.28 kDa.  The actual/experimental result based on MS was 22 kDa for both samples.  The experimental details are as follows: DHB matrix, 10 mg/mL. CH CN/H O = 3/7 (v/v).  Molecular weight spectra obtained by MALDI-TOF, waith a major peak at 22 kDa and minor peaks centered around 43 kDa and 64 kDa, are consistent with the information provided by DNT for G4.5 NaCOO dendrimer samples NCL22 and NCL23. Incorporating Magnevist did not change the spectrum of NCL23.', NULL, NULL),
(1900545, 'NCL200612A_fig 24.jpg', '/particles/NCL-23-1/molecularWeight/20061211_16-03-11-419_NCL200612A_fig 24.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'DNT NCL200612A Fig 24', 'SEC chromatograms of NCL23 using two different columns. The molar mass distribution plot for NCL23 using 2 different size exclusion columns is shown.  NCL23 elutes faster using the G3000 column.  The calculated molar mass for NCL23 was 24.17 kDa and 24.62 kDa, and the polydispersity index was 1.003 and 1032 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS for NCL23 is very similar to that of NCL22. Since NCL23 is NCL22 with incorporated Magnevist (noncovalent inclusion complex), the results suggest that Magnevist is no longer incorporated with the dendrimer after fractionation', NULL, NULL),
(1900546, 'NCL200612A_fig 24.jpg', '/particles/NCL-23-1/molecularWeight/20061211_16-03-31-716_NCL200612A_fig 24.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'DNT NCL200612A Fig 24', 'SEC chromatograms of NCL23 using two different columns. The molar mass distribution plot for NCL23 using 2 different size exclusion columns is shown.  NCL23 elutes faster using the G3000 column.  The calculated molar mass for NCL23 was 24.17 kDa and 24.62 kDa, and the polydispersity index was 1.003 and 1032 using the G3000 and G4000 columns, respectively. The molecular weight determined by SEC-MALLS for NCL23 is very similar to that of NCL22. Since NCL23 is NCL22 with incorporated Magnevist (noncovalent inclusion complex), the results suggest that Magnevist is no longer incorporated with the dendrimer after fractionation', NULL, NULL),
(1900547, 'NCL200612A_fig 27.jpg', '/particles/NCL-23-1/molecularWeight/20061211_16-14-37-972_NCL200612A_fig 27.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 27', 'Molar mass versus elution time plot of NCL22 and NCL23 by AFFF-MALLS. Concentration of NCL22: 1 mg/mL in H O; concentration of NCL23: 2 mg/mL in PBS; Conditions: Injection volume: 100 uL; 10kDa regenerated cellulose membrane; 350 um channel thickness; 1 mL/min channel flow; 3 mL/min cross-flow. AFFF is an innovative separation method for an efficient separation and characterization of nanoparticles, polymers, and proteins that is both fast and gentle.  When coupled with a MALLS system, the molar mass and rms radius can be obtained for the fractionated sample.  The molar mass distribution plot shows that NCL22 and NCL23 have similar molar mass by using AFFF as separation method.  The calculated molar mass of NCL22 and NCL23 was 21.63 kDa and 20.74 kDa, and the polydipersity index was 1.046 and 1.078, respectively (the molar mass of both NCL22 and NCL23 was determined by using the dn/dc value of NCL22, which was measured using an RI detector).', NULL, NULL),
(1900548, 'NCL200612A_fig 17.jpg', '/particles/NCL-23-1/purity/20061211_16-22-25-102_NCL200612A_fig 17.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 17', 'HPLC chromatogram for NCL23. The chromatogram and elution profile for NCL23 are shown.  The retention time for the main peak is 38.0 min and corresponds to 79% +- 1% of the total area (based on peaks eluted between 36.9 and 39.9 min). The UV spectra (data not shown) for these peaks are consistent with that of NCL23 measured with a UV-Vis spectrophotometer. The nature of the broad peak at 47.1 min is unknown.', NULL, NULL),
(1900549, 'NCL200612A_fig 22.jpg', '/particles/NCL-23-1/purity/20061211_16-30-47-448_NCL200612A_fig 22.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 22', 'Typical electropherogram of (A) NCL22 and (B) NCL23. Sample concentration: 0.1 mg/mL in water; capillary: 40 cm x 50 um I.D; buffer: 20 mM sodium phosphate (ph = 7.4); separation voltage: -14kV; injection pressure: 0.5 psi/20s; detector: UV (wavelength 200 nm). CE is a powerful chromatorgraphic technique that separates analytes on the basis of electrophoretic mobility differences.  Mobility is determined by the mass-to-charge ratio of the analyte.  CE has high separation efficiencies, high sensitivity, short run time and high automation capability.  DE is extensively used to evaluate the molecular distribution of dendirmers, since the charge distibution and electrophoretic mobility often change upon dendirmer surface conjugation.  Figure 22 shows that NCL22 and NCL23 have very similar electrophoretic mobilities (both are in the range of [4.3-5.2] x 10 cm V S), which indicates they have the same charge/mass ratio.', NULL, NULL),
(1900550, 'NCL200612A_fig 31.jpg', '/particles/NCL-22-1/cellViability/20061211_16-48-16-119_NCL200612A_fig 31.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 31', 'MTT cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample.  Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA01). Dashed red line indicates 100% control viability.', NULL, NULL),
(1900551, 'NCL200612A_fig 32.jpg', '/particles/NCL-22-1/cellViability/20061211_17-15-21-247_NCL200612A_fig 32.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 32', 'LDH cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1). Dashed red line indicates 0% LDH leakage', NULL, NULL),
(1900552, 'NCL200612A_fig 33.jpg', '/particles/NCL-22-1/cellViability/20061211_17-19-24-529_NCL200612A_fig 33.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 33', 'MTT cytotoxicity assay in Hep-G2 cells. Hep-G2 cells were treated for 6, 24, and 48 h with 0.02-5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 100% control viability.', NULL, NULL),
(1900553, 'Figure 34a.pdf', '/particles/NCL-22-1/cellViability/20070412_20-34-45-581_Figure 34a.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'LDH cytotoxicity assay in Hep-G2 cells NCL22', 'Hep-G2 cells were treated for 6, 24, and 48 h with 0.02Â–5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(1900554, 'NCL200612A_fig 31.jpg', '/particles/NCL-23-1/cellViability/20061211_17-29-25-845_NCL200612A_fig 31.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 31', 'MTT cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1). Dashed red line indicates 100% control viability.', NULL, NULL),
(1900555, 'NCL200612A_fig 32.jpg', '/particles/NCL-23-1/cellViability/20061211_17-31-39-634_NCL200612A_fig 32.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 32', 'LDH cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1).  Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(1900556, 'NCL200612A_fig 33.jpg', '/particles/NCL-23-1/cellViability/20061211_17-34-01-801_NCL200612A_fig 33.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 33', 'MTT cytotoxicity assay in Hep-G2 cells. Hep-G2 cells were treated for 6, 24, and 48 h with 0.02-5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2).  Dashed red line indicates 100% control viability.', NULL, NULL),
(1900557, 'NCL200612A_fig 31.jpg', '/particles/NCL-24-1/cellViability/20061211_17-39-33-509_NCL200612A_fig 31.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 31', 'MTT cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1).  Dashed red line indicates 100% control viability.', NULL, NULL),
(1900558, 'NCL200612A_fig 32.jpg', '/particles/NCL-24-1/cellViability/20061211_17-43-05-301_NCL200612A_fig 32.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 32', 'LDH cytotoxicity assay in LLC-PK1 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 h with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1). Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(1900559, 'NCL200612A_fig 33.jpg', '/particles/NCL-24-1/cellViability/20061211_17-47-22-675_NCL200612A_fig 33.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 33', 'MTT cytotoxicity assay in Hep-G2 cells. Hep-G2 cells were treated for 6, 24, and 48 h with 0.02-5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 100% control viability.', NULL, NULL),
(1900561, 'NCL200612A_fig 39.jpg', '/particles/NCL-22-1/hemolysis/20061211_18-01-08-982_NCL200612A_fig 39.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 39', 'Analysis of nanoparticle hemolytic properties (ITA-1). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the integrity of red blood cells.  Three independent samples were prepared for each nanoparticle concentration and analyzed in duplicate (%CV < 20). Each bar represents the mean of duplicate results.  Triton X-100 was used as a positive control.  PBS was used to reconstitute nanoparticle and represented the negative control.  Neither nanoparticle sample revealed hemolytic properties.', NULL, NULL),
(1900562, 'NCL200612A_fig 39.jpg', '/particles/NCL-23-1/hemolysis/20061211_18-02-55-255_NCL200612A_fig 39.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 39', 'Analysis of nanoparticle hemolytic properties (ITA-1). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the integrity of red blood cells.  Three independent samples were prepared for each nanoparticle concentration and analyzed in duplicate (%CV < 20). Each bar represents the mean of duplicate results.  Triton X-100 was used as a positive control.  PBS was used to reconstitute nanoparticle and represented the negative control.  Neither nanoparticle sample revealed hemolytic properties.', NULL, NULL),
(1900563, 'NCL200612A_fig 39.jpg', '/particles/NCL-24-1/hemolysis/20061211_18-04-58-779_NCL200612A_fig 39.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 39', 'Analysis of nanoparticle hemolytic properties (ITA-1). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the integrity of red blood cells.  Three independent samples were prepared for each nanoparticle concentration and analyzed in duplicate (%CV < 20). Each bar represents the mean of duplicate results.  Triton X-100 was used as a positive control.  PBS was used to reconstitute nanoparticle and represented the negative control.  Neither nanoparticle sample revealed hemolytic properties.', NULL, NULL),
(1900564, 'NCL200612A_fig 40A&B.jpg', '/particles/NCL-24-1/plateAggregation/20061211_18-16-02-45_NCL200612A_fig 40A&B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 40A&B', 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade.  Fore each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that neither nanoparticle sample is capable of inducing platelet aggregation.  Collagen and PBS were used as a positive and negative control, respectively. (B) Analysis of nanoparticle effect on collagen-induced platelet aggregation (ITA-2). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle interference with platelet aggregation caused by a known activation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplication (%CV < 20%). Each bar represents the mean of duplicate results.   The results demonstrate the high doses of NCL23 andNCL24 enhance collagen-induced platelet aggregation, while low concentration of these particles did no disturb the process.  NCL22 at both high and low doses did not interfere with collagen-induce platelet aggregation.', NULL, NULL),
(1900565, 'NCL200612A_fig 40A&B.jpg', '/particles/NCL-22-1/plateAggregation/20061211_18-18-16-967_NCL200612A_fig 40A&B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 40A&B', 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade.  Fore each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that neither nanoparticle sample is capable of inducing platelet aggregation.  Collagen and PBS were used as a positive and negative control, respectively. (B) Analysis of nanoparticle effect on collagen-induced platelet aggregation (ITA-2). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle interference with platelet aggregation caused by a known activation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplication (%CV < 20%). Each bar represents the mean of duplicate results.   The results demonstrate the high doses of NCL23 andNCL24 enhance collagen-induced platelet aggregation, while low concentration of these particles did no disturb the process.  NCL22 at both high and low doses did not interfere with collagen-induce platelet aggregation.', NULL, NULL),
(1900566, 'NCL200612A_fig 40A&B.jpg', '/particles/NCL-23-1/plateAggregation/20061211_18-19-43-689_NCL200612A_fig 40A&B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 40A&B', 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade.  Fore each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that neither nanoparticle sample is capable of inducing platelet aggregation.  Collagen and PBS were used as a positive and negative control, respectively. (B) Analysis of nanoparticle effect on collagen-induced platelet aggregation (ITA-2). NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle interference with platelet aggregation caused by a known activation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplication (%CV < 20%). Each bar represents the mean of duplicate results.   The results demonstrate the high doses of NCL23 andNCL24 enhance collagen-induced platelet aggregation, while low concentration of these particles did no disturb the process.  NCL22 at both high and low doses did not interfere with collagen-induce platelet aggregation.', NULL, NULL),
(1900567, 'NCL200612A_fig 41.jpg', '/particles/NCL-22-1/cfu_gm/20061211_18-25-57-158_NCL200612A_fig 41.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 41', 'Analysis of nanoparticle toxicity to bone marrow cells (ITA-3).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the formation of granulocyte-macrophage colonies from bone marrow precursors.  For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that NCL22 and a low concentration of NCL23 are not myelosuppressive, while a high concentration of NCL23 and NCL24 suppresses CFU-GM formation (p<0.05). Cisplatin and PBS were used as a positive and negative control, respectively.', NULL, NULL),
(1900568, 'NCL200612A_fig 41.jpg', '/particles/NCL-23-1/cfu_gm/20061211_18-27-21-971_NCL200612A_fig 41.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 41', 'Analysis of nanoparticle toxicity to bone marrow cells (ITA-3).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the formation of granulocyte-macrophage colonies from bone marrow precursors.  For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that NCL22 and a low concentration of NCL23 are not myelosuppressive, while a high concentration of NCL23 and NCL24 suppresses CFU-GM formation (p<0.05). Cisplatin and PBS were used as a positive and negative control, respectively.', NULL, NULL),
(1900569, 'NCL200612A_fig 41.jpg', '/particles/NCL-24-1/cfu_gm/20061211_18-28-47-846_NCL200612A_fig 41.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 41', 'Analysis of nanoparticle toxicity to bone marrow cells (ITA-3).  NCL22 and NCL23 at either high (1 mg/mL) or low (0.0156 and 0.25 mg/mL, respectively) concentration, and NCL24 at 1 mg/mL were used to evaluate potential particle effects on the formation of granulocyte-macrophage colonies from bone marrow precursors.  For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 20%). Each bar represents to mean of duplicate results.  The results demonstrate that NCL22 and a low concentration of NCL23 are not myelosuppressive, while a high concentration of NCL23 and NCL24 suppresses CFU-GM formation (p<0.05). Cisplatin and PBS were used as a positive and negative control, respectively.', NULL, NULL),
(2293765, 'NCL200612A_fig 46.jpg', '/particles/NCL-23-1/oxidativeBurst/20061213_17-20-55-699_NCL200612A_fig 46.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 46', 'Analysis of nitric oxide (NO) production by macrophages (ITA-7).  NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) concentrations; NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%).  Each bar represents the mean of duplication results.  NCL22, NCL23, and NCL24 did not induce NO production. At the high concentration NCL22, NCL23, and NCL24 were toxic for the RAW 264.7 macrophage cell line.  PBS and bacterial LPS were used as a negative and positive control, respectively.', NULL, NULL),
(2457600, 'NCL200612A_fig 42C.jpg', '/particles/NCL-22-1/coagulation/20061213_15-49-30-475_NCL200612A_fig 42C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ANALYSIS OF NANOPARTICLE EFFECT ON COAGULATION - DONOR GROUP 3 (ITA&#8209;12).', 'NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) and NCL22, NCL23 and NCL24 at high concentration (1mg/mL) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, two independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, and NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interfered with coagulation.', NULL, NULL),
(2457601, 'NCL200612A_fig 42C.jpg', '/particles/NCL-23-1/coagulation/20061213_15-51-43-280_NCL200612A_fig 42C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ANALYSIS OF NANOPARTICLE EFFECT ON COAGULATION - DONOR GROUP 3 (ITA&#8209;12). ', 'NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) and NCL22, NCL23 and NCL24 at high concentration (1mg/mL) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, two independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, and NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interfered with coagulation.', NULL, NULL),
(2457602, 'NCL200612A_fig 42C.jpg', '/particles/NCL-24-1/coagulation/20061213_15-53-40-80_NCL200612A_fig 42C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ANALYSIS OF NANOPARTICLE EFFECT ON COAGULATION - DONOR GROUP 3 (ITA&#8209;12).', 'Analysis of nanoparticle effect on coagulation - donor group 3 (ITA&#8209;12). NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) and NCL22, NCL23 and NCL24 at high concentration (1mg/mL) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, two independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, and NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interfered with coagulation.', NULL, NULL);
INSERT INTO `lab_file` (`file_pk_id`, `file_name`, `file_uri`, `file_type_extension`, `file_source_type`, `version`, `status`, `reason`, `created_by`, `created_date`, `sample_sop_pk_id`, `run_pk_id`, `data_status_pk_id`, `title`, `description`, `comments`, `type`) VALUES
(2457603, 'NCL200612A_fig 43.jpg', '/particles/NCL-22-1/plasmaProteinBinding/20061213_15-58-58-71_NCL200612A_fig 43.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 43', 'Interaction with plasma proteins (ITA-4). NCL22 was immobilized on a CovaLink ELISA plate in order to achieve separation of particle-bound proteins from bulk plasma.  Proteins isolated from plates coated with NCL22 (A) or acetic acid (B) without any blocking buffers were then analyzed by 2D PAGE. Acetic acid was used as a negative control (C). During method development, several blocking buffers were tested to block unspecific binding sites on the ELISA plate.  The results suggest that NCL22 acts as a blocking agent. NO proteins spots specific for NCL22 were identified using these conditions.', NULL, NULL),
(2457604, 'NCL200612A_fig 46.jpg', '/particles/NCL-22-1/oxidativeBurst/20061213_17-16-12-457_NCL200612A_fig 46.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 46', 'Analysis of nitric oxide (NO) production by macrophages (ITA-7).  NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) concentrations; NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%).  Each bar represents the mean of duplication results.  NCL22, NCL23, and NCL24 did not induce NO production. At the high concentration NCL22, NCL23, and NCL24 were toxic for the RAW 264.7 macrophage cell line.  PBS and bacterial LPS were used as a negative and positive control, respectively.', NULL, NULL),
(2457605, 'NCL200612A_fig 46.jpg', '/particles/NCL-24-1/oxidativeBurst/20061213_17-22-14-3_NCL200612A_fig 46.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 46', 'Analysis of nitric oxide (NO) production by macrophages (ITA-7).  NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) concentrations; NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%).  Each bar represents the mean of duplication results.  NCL22, NCL23, and NCL24 did not induce NO production. At the high concentration NCL22, NCL23, and NCL24 were toxic for the RAW 264.7 macrophage cell line.  PBS and bacterial LPS were used as a negative and positive control, respectively.', NULL, NULL),
(2457606, 'NCL200612A_fig 48.jpg', '/particles/NCL-24-1/phagocytosis/20061213_17-28-31-912_NCL200612A_fig 48.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 48', 'Phagocytosis assay (ITA-9). NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) Concentrations;  NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. Zymosan A was used as positive control. NCL22, NCL23, and NCL24 were not phagocytosed by macrophages. NCL22 did no affect phagocytic uptake of Zymosan A, while NCL23 and NCL24 suppressed phagocytosis of Zymosan A.  All particles were tested at a concentration of 1 mg/mL.', NULL, NULL),
(2457607, 'NCL200612A_fig 48.jpg', '/particles/NCL-23-1/phagocytosis/20061213_17-30-02-81_NCL200612A_fig 48.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 48', 'Phagocytosis assay (ITA-9). NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) Concentrations;  NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. Zymosan A was used as positive control. NCL22, NCL23, and NCL24 were not phagocytosed by macrophages. NCL22 did no affect phagocytic uptake of Zymosan A, while NCL23 and NCL24 suppressed phagocytosis of Zymosan A.  All particles were tested at a concentration of 1 mg/mL.\r\n\r\n', NULL, NULL),
(2457608, 'NCL200612A_fig 48.jpg', '/particles/NCL-22-1/phagocytosis/20061213_17-31-34-469_NCL200612A_fig 48.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 48', 'Phagocytosis assay (ITA-9). NCL22 and NCL23 were analyzed at high (1 mg/mL) and low (0.0156 and 0.25 mg/mL respectively) Concentrations;  NCL24 was analyzed at 1 mg/mL. For each concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. Zymosan A was used as positive control. NCL22, NCL23, and NCL24 were not phagocytosed by macrophages. NCL22 did no affect phagocytic uptake of Zymosan A, while NCL23 and NCL24 suppressed phagocytosis of Zymosan A.  All particles were tested at a concentration of 1 mg/mL.', NULL, NULL),
(2457609, 'NCL200612A_fig 49B.jpg', '/particles/NCL-23-1/cytokineInduction/20061213_17-41-57-151_NCL200612A_fig 49B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 49B', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 48 hrs. PBMCs isolated from healthy donors were either untreated or treated for 48 h with bacterial LPS, LPS +IFNx, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL.  Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results.  Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a CBA inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8.  Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.', NULL, NULL),
(2457610, 'NCL200612A_fig 49B.jpg', '/particles/NCL-24-1/cytokineInduction/20061213_17-43-21-979_NCL200612A_fig 49B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'CYTOKINE SECRETION BY PERIPHERAL BLOOD MONONUCLEAR CELLS (PBMCS) (ITA-10) 48 HRS.', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 48 hrs. PBMCs isolated from healthy donors were either untreated or treated for 48 h with bacterial LPS, LPS +IFNx, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL.  Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results.  Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a CBA inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8.  Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.\r\n\r\n', NULL, NULL),
(2457611, 'NCL200612A_fig 49B.jpg', '/particles/NCL-22-1/cytokineInduction/20061213_17-44-54-411_NCL200612A_fig 49B.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig 49B', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 48 hrs. PBMCs isolated from healthy donors were either untreated or treated for 48 h with bacterial LPS, LPS +IFNx, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL.  Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results.  Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a CBA inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8.  Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.', NULL, NULL),
(2457612, 'NCL200612A_fig 50C.jpg', '/particles/NCL-22-1/nkCellCytotoxicActivity/20061213_17-46-52-244_NCL200612A_fig 50C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig50C', 'Analysis of cytotoxic activity of NK cells by RT-CES.  The NK cell line NK92 (source ATCC) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively.  The effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 untreated or in the presence of NCL22, NCL23, or NCL24 at a concentration of 0.2 mg/mL.  Cell viability was continuously monitored in real time for 48 h. The percentage of cytoxicty was calculated by comparing the AIC values of untreated cells or of cells co-cultured in the presence of nanoparticles with that of the targets growth curve. Results from duplicate samples are shown (%CV < 20%). Each bar represents the mean of duplicate results. NCL22, NCL23, and NCL24 did not interfere with the cytotoxicity of NK cells towards tumor targets.', NULL, NULL),
(2457613, 'NCL200612A_fig 50C.jpg', '/particles/NCL-23-1/nkCellCytotoxicActivity/20061213_17-48-12-137_NCL200612A_fig 50C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig50C', 'Analysis of cytotoxic activity of NK cells by RT-CES.  The NK cell line NK92 (source ATCC) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively.  The effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 untreated or in the presence of NCL22, NCL23, or NCL24 at a concentration of 0.2 mg/mL.  Cell viability was continuously monitored in real time for 48 h. The percentage of cytoxicty was calculated by comparing the AIC values of untreated cells or of cells co-cultured in the presence of nanoparticles with that of the targets growth curve. Results from duplicate samples are shown (%CV < 20%). Each bar represents the mean of duplicate results. NCL22, NCL23, and NCL24 did not interfere with the cytotoxicity of NK cells towards tumor targets.\r\n\r\n', NULL, NULL),
(2457614, 'NCL200612A_fig 50C.jpg', '/particles/NCL-24-1/nkCellCytotoxicActivity/20061213_17-49-44-789_NCL200612A_fig 50C.jpg', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'August 2006 DNT NCL200612A Fig50C', 'Analysis of cytotoxic activity of NK cells by RT-CES.  The NK cell line NK92 (source ATCC) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively.  The effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 untreated or in the presence of NCL22, NCL23, or NCL24 at a concentration of 0.2 mg/mL.  Cell viability was continuously monitored in real time for 48 h. The percentage of cytoxicty was calculated by comparing the AIC values of untreated cells or of cells co-cultured in the presence of nanoparticles with that of the targets growth curve. Results from duplicate samples are shown (%CV < 20%). Each bar represents the mean of duplicate results. NCL22, NCL23, and NCL24 did not interfere with the cytotoxicity of NK cells towards tumor targets.', NULL, NULL),
(3178496, '120406.pdf', '/reports/20061220_8-06-33-125_120406.pdf', NULL, NULL, NULL, NULL, NULL, NULL, '2006-12-20 08:06:33', NULL, NULL, NULL, 'DENDRITIC NANOTECHNOLOGIES 122006', 'Dendrimer-Based MRI Contrast Agents prepared for Dendritic Nanotechnologies, December, 2006', NULL, NULL),
(3702797, 'Figure 15 UV-Vis.pdf', '/particles/NCL-22-1/purity/20070411_20-59-15-921_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', 'UV-Vis spectra were recorded using a Thermo Electron Evolution 300 spectrophotometer (Waltham, MA). Samples were prepared in HPLC-grade water and measured in quartz microcuvettes (b = 10 mm, QS109.004, Hellma, Plainview, NY). The UV-Vis spectra are consistent with the dendrimers in that no absorption above 230 nm was observed.', NULL, NULL),
(3702824, 'Figure 47.pdf', '/particles/NCL-22-1/chemotaxis/20070413_8-57-02-972_Figure 47.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ANALYSIS OF NANOPARTICLE EFFECT ON CHEMOTAXIS (ITA-8)', 'Analysis of nanoparticle effect on chemotaxis (ITA-8). NCL22, NCL23 and NCL24 did not induce chemotaxis of HL-60 macrophage-like cells. PBS and FBS were used as negative and positive controls, respectively.', NULL, NULL),
(3702825, 'Figure 47.pdf', '/particles/NCL-22-1/chemotaxis/20070413_8-59-17-745_Figure 47.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on chemotaxis (ITA-8)', 'Analysis of nanoparticle effect on chemotaxis (ITA-8). NCL22, NCL23 and NCL24 did not induce chemotaxis of HL-60 macrophage-like cells. PBS and FBS were used as negative and positive controls, respectively.', NULL, NULL),
(3801088, 'Figure 1.pdf', '/particles/NCL-22-1/size/20070411_14-50-26-85_Figure 1.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL22 in saline at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL22 in saline at 25 Â°C.', NULL, NULL),
(3801089, 'Figure 2.pdf', '/particles/NCL-22-1/size/20070411_14-52-42-496_Figure 2.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL22 in PBS at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL22 in PBS at 25 Â°C.', NULL, NULL),
(3801090, 'Figure 3.pdf', '/particles/NCL-22-1/size/20070411_14-54-55-912_Figure 3.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL22 in PBS at 37 Â°C.', 'Statistics graph based on size distribution by volume for NCL22 in PBS at 37 Â°C.', NULL, NULL),
(3801091, 'Figure 4.pdf', '/particles/NCL-23-1/size/20070411_15-01-37-203_Figure 4.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL23 in saline at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL23 in saline at 25 Â°C.', NULL, NULL),
(3801092, 'Figure 5.pdf', '/particles/NCL-23-1/size/20070411_15-04-16-56_Figure 5.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL23 in PBS at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL23 in PBS at 25 Â°C.', NULL, NULL),
(3801093, 'Figure 6.pdf', '/particles/NCL-23-1/size/20070411_15-07-03-192_Figure 6.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL23 in PBS at 37 Â°C.', 'Statistics graph based on size distribution by volume for NCL23 in PBS at 37 Â°C.', NULL, NULL),
(3801094, 'Figure 7.pdf', '/particles/NCL-20-1/size/20070411_15-38-37-358_Figure 7.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL20 in saline at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL20 in saline at 25 Â°C.', NULL, NULL),
(3801095, 'Figure 8.pdf', '/particles/NCL-20-1/size/20070411_15-40-20-19_Figure 8.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Statistics graph based on size distribution by volume for NCL20 in PBS at 25 Â°C.', 'Statistics graph based on size distribution by volume for NCL20 in PBS at 25 Â°C.', NULL, NULL),
(3801096, 'Figure 11.pdf', '/particles/NCL-22-1/size/20070411_16-51-25-212_Figure 11.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The effect of prefiltration on hydrodynamic size of NCL22 in 10 mM NaCl.', 'Using a 0.1-&#956;m-pore-size filter during sample preparation consistently results in the appearance of a size mode near 100 nm in the intensity distribution derived from DLS measurements on NCL22 and NCL23 dendrimer samples. Using a 0.02-&#956;m-pore-size filter causes the large-size mode to be removed, indicating that this is a real mode and not an analysis artifact. Additional measurements on DI water and saline solution filtered in the same manner, but not containing the dendrimer sample, did not indicate presence of a large-size mode with similar intensity. The origin of peak has not been resolved, but it may result from large dendrimer agglomerates that either exist in the sample after redispersion, or that are formed during the filtration process itself. The technique cannot resolve the presence of dimers and trimers.', NULL, NULL),
(3801097, 'Figure 15 UV-Vis.pdf', '/particles/NCL-22-1/purity/20070411_20-51-22-348_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', 'UV-Vis spectra were recorded using a Thermo Electron Evolution 300 spectrophotometer (Waltham, MA). Samples were prepared in HPLC-grade water and measured in quartz microcuvettes (b = 10 mm, QS109.004, Hellma, Plainview, NY). The UV-Vis spectra are consistent with the dendrimers in that no absorption above 230 nm was observed.', NULL, NULL),
(3801098, 'Figure 15 UV-Vis.pdf', '/particles/NCL-20-1/purity/20070411_20-53-56-802_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', 'UV-Vis spectra were recorded using a Thermo Electron Evolution 300 spectrophotometer (Waltham, MA). Samples were prepared in HPLC-grade water and measured in quartz microcuvettes (b = 10 mm, QS109.004, Hellma, Plainview, NY). The UV-Vis spectra are consistent with the dendrimers in that no absorption above 230 nm was observed.', NULL, NULL),
(3801099, 'Figure 15 UV-Vis.pdf', '/particles/NCL-21-1/purity/20070411_20-56-27-594_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', 'UV-Vis spectra were recorded using a Thermo Electron Evolution 300 spectrophotometer (Waltham, MA). Samples were prepared in HPLC-grade water and measured in quartz microcuvettes (b = 10 mm, QS109.004, Hellma, Plainview, NY). The UV-Vis spectra are consistent with the dendrimers in that no absorption above 230 nm was observed.', NULL, NULL),
(3801100, 'Figure 15 UV-Vis.pdf', '/particles/NCL-23-1/purity/20070412_16-39-47-741_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', 'UV-Vis spectra were recorded using a Thermo Electron Evolution 300 spectrophotometer (Waltham, MA). Samples were prepared in HPLC-grade water and measured in quartz microcuvettes (b = 10 mm, QS109.004, Hellma, Plainview, NY). The UV-Vis spectra are consistent with the dendrimers in that no absorption above 230 nm was observed.', NULL, NULL),
(3801101, 'Figure 15 UV-Vis.pdf', '/particles/NCL-25-1/purity/20070412_16-42-36-865_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', NULL, NULL, NULL),
(3801102, 'Figure 15 UV-Vis.pdf', '/particles/NCL-26-1/purity/20070412_16-44-13-314_Figure 15 UV-Vis.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for the dendrimers studied.', NULL, NULL, NULL),
(3801103, 'Figure 18.pdf', '/particles/NCL-20-1/purity/20070412_19-51-04-219_Figure 18.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'HPLC Chromatogram for NCL20', 'The chromatogram and elution profile for NCL20 are shown. The retention time for the main peak is 41.9 min. The UV spectra (data not shown) for these peaks (eluted between 37.5 and 44.9 min) are consistent with that of NCL20 measured with a UV-Vis spectrophotometer.', NULL, NULL),
(3801104, 'Figure 19.pdf', '/particles/NCL-25-1/purity/20070412_19-55-36-143_Figure 19.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'HPLC Chromatogram for NCL25', 'The chromatogram and elution profile for NCL25 are shown. The retention time for the main peak is 42.4 min. The UV spectra (data not shown) for these peaks (eluted between 40.6 and 45.3 min) are consistent with that of NCL25 measured with a UV-Vis spectrophotometer.', NULL, NULL),
(3801105, 'Figure 21.pdf', '/particles/NCL-26-1/purity/20070412_19-57-56-207_Figure 21.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'HPLC Chromatogram for NCL26', 'The chromatogram and elution profile for NCL26 are shown. The retention time for the main peak is 50.1 min and corresponds to 88% Â± 1% of the total area. The UV spectra (data not shown) for this peak are consistent with that of NCL26 measured with a UV-Vis spectrophotometer. The nature of the broad peak at 89.6 min is unknown.', NULL, NULL),
(3801106, 'Figure 34c.pdf', '/particles/NCL-24-1/cellViability/20070412_20-30-15-35_Figure 34c.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'LDH cytotoxicity assay in Hep-G2 cells NCL24', 'Hep-G2 cells were treated for 6, 24, and 48 h with 0.02Â–5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(3801107, 'Figure 34c.pdf', '/particles/NCL-22-1/cellViability/20070412_20-41-34-221_Figure 34c.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'LDH cytotoxicity assay in Hep-G2 cells NCL23', 'Hep-G2 cells were treated for 6, 24, and 48 h with 0.02Â–5.0 mg/mL of test sample. Cytotoxicity was determined at each time point as described in the HEP G2 Hepatocarcinoma Cytotoxicity Assay (GTA-2). Dashed red line indicates 0% LDH leakage.', NULL, NULL),
(3801108, 'Figure 42A.pdf', '/particles/NCL-22-1/coagulation/20070413_7-55-34-179_Figure 42A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 1 (ITA&#8209;12).', 'NCL22, NCL23, and NCL24 at high (1mg/mL) concentrations were used to evaluate potential particle effects on the biochemical component of the blood coagulation cascade (prothrombin time [PT]; activated partial thromboplastin time [APTT]; Thrombin time and Reptilase time). For each nanoparticle, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. Normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, or NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that high concentrations of each nanoparticle sample delay coagulation time of plasma derived from donor group 1 above clinically acceptable standard in APTT, thrombin time and reptilase time tests.', NULL, NULL),
(3801109, 'Figure 42B.pdf', '/particles/NCL-22-1/coagulation/20070413_7-56-49-814_Figure 42B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 2 (ITA&#8209;12).', 'NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22 or NCL23. Plasma samples exposed to high concentrations of NCL22, NCL23, and NCL24 were also included in the analysis; one sample of each nanoparticle formulation at high concentration was prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interferes with coagulation.', NULL, NULL),
(3801110, 'Figure 42A.pdf', '/particles/NCL-23-1/coagulation/20070413_8-00-34-236_Figure 42A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 1 (ITA&#8209;12). ', 'NCL22, NCL23, and NCL24 at high (1mg/mL) concentrations were used to evaluate potential particle effects on the biochemical component of the blood coagulation cascade (prothrombin time [PT]; activated partial thromboplastin time [APTT]; Thrombin time and Reptilase time). For each nanoparticle, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. Normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, or NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that high concentrations of each nanoparticle sample delay coagulation time of plasma derived from donor group 1 above clinically acceptable standard in APTT, thrombin time and reptilase time tests.', NULL, NULL),
(3801111, 'Figure 42B.pdf', '/particles/NCL-23-1/coagulation/20070413_8-01-33-212_Figure 42B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 2 (ITA&#8209;12).', 'NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22 or NCL23. Plasma samples exposed to high concentrations of NCL22, NCL23, and NCL24 were also included in the analysis; one sample of each nanoparticle formulation at high concentration was prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interferes with coagulation.', NULL, NULL),
(3801112, 'Figure 42A.pdf', '/particles/NCL-24-1/coagulation/20070413_8-03-59-599_Figure 42A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 1 (ITA&#8209;12).', 'Analysis of nanoparticle effect on coagulation - donor group 1 (ITA&#8209;12). NCL22, NCL23, and NCL24 at high (1mg/mL) concentrations were used to evaluate potential particle effects on the biochemical component of the blood coagulation cascade (prothrombin time [PT]; activated partial thromboplastin time [APTT]; Thrombin time and Reptilase time). For each nanoparticle, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. Normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22, NCL23, or NCL24. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that high concentrations of each nanoparticle sample delay coagulation time of plasma derived from donor group 1 above clinically acceptable standard in APTT, thrombin time and reptilase time tests.', NULL, NULL),
(3801113, 'Figure 42B.pdf', '/particles/NCL-24-1/coagulation/20070413_8-06-36-70_Figure 42B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on coagulation - donor group 2 (ITA&#8209;12).', 'Analysis of nanoparticle effect on coagulation - donor group 2 (ITA&#8209;12). NCL22 and NCL23 at low concentrations (0.0156 and 0.25 mg/mL, respectively) were used to evaluate potential particle effects on blood coagulation. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The normal plasma standard (N) and abnormal plasma standard (ABN) were used for the instrument control. Plasma pooled from at least three donors was either untreated (Unt.) or treated with nanoparticle preparations NCL22 or NCL23. Plasma samples exposed to high concentrations of NCL22, NCL23, and NCL24 were also included in the analysis; one sample of each nanoparticle formulation at high concentration was prepared and analyzed in duplicate (%CV < 5%). Each bar represents the mean of duplicate results. The dotted red line indicates the clinical standard cut-off for normal coagulation time for each of the tests. The results demonstrate that, in this group of donors, neither nanoparticle test sample interferes with coagulation.', NULL, NULL),
(3801114, 'Figure 45.pdf', '/particles/NCL-22-1/leukocyteProliferation/20070413_8-23-11-757_Figure 45.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6).', 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6). NCL22, NCL23, and NCL24 did not induce leukocyte proliferation. Phytohemaglutinin-M (PHM) was used as a positive control for proliferation induction. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. NCL22 did not suppress proliferation induced by PHA-M, while NCL23 at 1mg/mL suppressed PHA-MÂ–induced proliferation. PBS was used as a negative control.', NULL, NULL),
(3801115, 'Figure 45.pdf', '/particles/NCL-23-1/leukocyteProliferation/20070413_8-28-17-114_Figure 45.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6).', 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6). NCL22, NCL23, and NCL24 did not induce leukocyte proliferation. Phytohemaglutinin-M (PHM) was used as a positive control for proliferation induction. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. NCL22 did not suppress proliferation induced by PHA-M, while NCL23 at 1mg/mL suppressed PHA-MÂ–induced proliferation. PBS was used as a negative control.', NULL, NULL),
(3801116, 'Figure 45.pdf', '/particles/NCL-24-1/leukocyteProliferation/20070413_8-30-06-209_Figure 45.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6).', 'Analysis of nanoparticle effect on leuckocyte proliferation (ITA-6). NCL22, NCL23, and NCL24 did not induce leukocyte proliferation. Phytohemaglutinin-M (PHM) was used as a positive control for proliferation induction. For each nanoparticle concentration, three independent samples were prepared and analyzed in duplicate (%CV < 25%). Each bar represents the mean of duplicate results. NCL22 did not suppress proliferation induced by PHA-M, while NCL23 at 1mg/mL suppressed PHA-MÂ–induced proliferation. PBS was used as a negative control.', NULL, NULL),
(3801117, 'Figure 44.pdf', '/particles/NCL-24-1/complementActivation/20070413_8-33-18-829_Figure 44.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of complement activation (ITA-5)', 'Analysis of complement activation (ITA-5). NCL22, NCL23 and NCL24 were tested for their ability to activate a complement. PBS and cobra venom factor (CVF) were used as the negative and positive control, respectively. NCL22 at both concentrations and NCL23 at low concentration did not induce complement activation, evidenced by an intensity of bands A and C that was similar to that of the negative control. NCL23 and NCL24 at 1mg/mL induced complement activation, evidenced by the appearance of split product (bands B and D) that was similar to that of the positive control.', NULL, NULL),
(3801118, 'Figure 44.pdf', '/particles/NCL-23-1/complementActivation/20070413_8-36-08-917_Figure 44.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of complement activation (ITA-5)', 'Analysis of complement activation (ITA-5). NCL22, NCL23 and NCL24 were tested for their ability to activate a complement. PBS and cobra venom factor (CVF) were used as the negative and positive control, respectively. NCL22 at both concentrations and NCL23 at low concentration did not induce complement activation, evidenced by an intensity of bands A and C that was similar to that of the negative control. NCL23 and NCL24 at 1mg/mL induced complement activation, evidenced by the appearance of split product (bands B and D) that was similar to that of the positive control.', NULL, NULL),
(3801119, 'Figure 44.pdf', '/particles/NCL-22-1/complementActivation/20070413_8-37-50-774_Figure 44.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of complement activation (ITA-5)', 'Analysis of complement activation (ITA-5). NCL22, NCL23 and NCL24 were tested for their ability to activate a complement. PBS and cobra venom factor (CVF) were used as the negative and positive control, respectively. NCL22 at both concentrations and NCL23 at low concentration did not induce complement activation, evidenced by an intensity of bands A and C that was similar to that of the negative control. NCL23 and NCL24 at 1mg/mL induced complement activation, evidenced by the appearance of split product (bands B and D) that was similar to that of the positive control.', NULL, NULL),
(3801120, 'Figure 47.pdf', '/particles/NCL-22-1/chemotaxis/20070413_9-09-28-384_Figure 47.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on chemotaxis (ITA-8)', 'Analysis of nanoparticle effect on chemotaxis (ITA-8). NCL22, NCL23 and NCL24 did not induce chemotaxis of HL-60 macrophage-like cells. PBS and FBS were used as negative and positive controls, respectively.', NULL, NULL),
(3801121, 'Figure 47.pdf', '/particles/NCL-23-1/chemotaxis/20070413_9-12-22-857_Figure 47.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on chemotaxis (ITA-8)', 'Analysis of nanoparticle effect on chemotaxis (ITA-8). NCL22, NCL23 and NCL24 did not induce chemotaxis of HL-60 macrophage-like cells. PBS and FBS were used as negative and positive controls, respectively.', NULL, NULL),
(3801122, 'Figure 47.pdf', '/particles/NCL-24-1/chemotaxis/20070413_9-14-42-959_Figure 47.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle effect on chemotaxis (ITA-8)', 'Analysis of nanoparticle effect on chemotaxis (ITA-8). NCL22, NCL23 and NCL24 did not induce chemotaxis of HL-60 macrophage-like cells. PBS and FBS were used as negative and positive controls, respectively.', NULL, NULL),
(3801123, 'Figure 49A.pdf', '/particles/NCL-24-1/cytokineInduction/20070413_9-33-56-604_Figure 49A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 24 hrs.', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA&#8209;10) 24 hrs. PBMCs isolated from healthy donors were either untreated or treated for 24 h with bacterial lipopolysaccharide (LPS), LPS +IFNg, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL. Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results. Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a Cytometric Bead Array (CBA) inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8. Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.', NULL, NULL),
(3801124, 'Figure 49C.pdf', '/particles/NCL-24-1/cytokineInduction/20070413_9-35-21-846_Figure 49C.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of potential effects of NCL22 on LPS-induced cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10).', 'Analysis of potential effects of NCL22 on LPS-induced cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10). PBMCs isolated from healthy donors were either untreated (#1) or treated for 24 h with bacterial LPS (#2), NCL22 at 0.2 mg/mL(#3), LPS and NCL22 at 0.2 mg/mL (#4). Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results. Viability of the cells was evaluated by trypan blue exclusion assay at the end of the 24 hour incubation. Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a CBA inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8. Shown is the mean concentration of cytokine in two independent samples after dilution. CV is less than 25%, except for NCL22 in the IL&#8209;6 and IL-8 plots, where CV is less than 50%. The tested formulation did not suppress LPS&#8209;induced cytokine production by PBMCs.', NULL, NULL),
(3801125, 'Figure 49A.pdf', '/particles/NCL-22-1/cytokineInduction/20070413_9-40-03-362_Figure 49A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 24 hrs.', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA&#8209;10) 24 hrs. PBMCs isolated from healthy donors were either untreated or treated for 24 h with bacterial lipopolysaccharide (LPS), LPS +IFNg, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL. Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results. Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a Cytometric Bead Array (CBA) inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8. Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.', NULL, NULL),
(3801126, 'Figure 49C.pdf', '/particles/NCL-22-1/cytokineInduction/20070413_9-38-55-793_Figure 49C.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of potential effects of NCL22 on LPS-induced cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10).', 'Analysis of potential effects of NCL22 on LPS-induced cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10). PBMCs isolated from healthy donors were either untreated (#1) or treated for 24 h with bacterial LPS (#2), NCL22 at 0.2 mg/mL(#3), LPS and NCL22 at 0.2 mg/mL (#4). Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results. Viability of the cells was evaluated by trypan blue exclusion assay at the end of the 24 hour incubation. Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a CBA inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8. Shown is the mean concentration of cytokine in two independent samples after dilution. CV is less than 25%, except for NCL22 in the IL&#8209;6 and IL-8 plots, where CV is less than 50%. The tested formulation did not suppress LPS&#8209;induced cytokine production by PBMCs.', NULL, NULL),
(3801127, 'Figure 49A.pdf', '/particles/NCL-23-1/cytokineInduction/20070413_9-42-04-302_Figure 49A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA-10) 24 hrs.', 'Cytokine secretion by peripheral blood mononuclear cells (PBMCs) (ITA&#8209;10) 24 hrs. PBMCs isolated from healthy donors were either untreated or treated for 24 h with bacterial lipopolysaccharide (LPS), LPS +IFNg, NCL22 at 0.2 mg/mL, NCL23 at 0.2 mg/mL or NCL24 at 0.2 mg/mL. Two independent samples were analyzed for each concentration (%CV < 25%). Each bar represents the mean of duplicate results. Cell culture supernatants were diluted 1:5 and analyzed by flow cytometry using a Cytometric Bead Array (CBA) inflammation kit for quantitative determination of cytokines IL-10, TNF, IL-1, IL-6 and IL-8. Shown are concentrations measured in individual samples after dilution. None of the nanoparticle formulations resulted in cytokine induction.', NULL, NULL),
(3801128, 'Figure50A.pdf', '/particles/NCL-23-1/nkCellCytotoxicActivity/20070413_9-58-03-982_Figure50A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A).', 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A). The NK cell line NK92 (source Laboratory of Experimental Immunology [LEI]) and tumor cell line K562 (source Developmental Therapeutics Program [DTP]) were used as effectors and targets, respectively. The effector and 51Cr-loaded target cells were co-cultured at different effector-to-target (E:T) ratios without, or in the presence of, NCL22, NCL23, and NCL24. For each concentration, two independent samples were prepared and analyzed in triplicate (%CV < 20%). Each data point represents the mean of triplicate results. Additional samples were included to control for nanoparticle-induced chromium release from target cells. NCL22 at both concentrations and NCL23 at a low concentration did not interfere with the cytotoxicity of NK cells, while high doses of NCL23 and NCL24 slightly inhibited the cytotoxicity of NK cells towards K562 targets.', NULL, NULL),
(3801129, 'Figure 50B.pdf', '/particles/NCL-23-1/nkCellCytotoxicActivity/20070413_9-59-09-349_Figure 50B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by (RT-CES).', 'Analysis of cytotoxic activity of NK cells by (RT-CES). The NK cell line NK92 (source American Type Culture Collection [ATCC]) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively. Effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 without, or in the presence of, NCL22, NCL23, and NCL24 (%CV < 20%). Additional samples were included to control for nanoparticle-associated toxicity to target cells. Cell viability was continuously monitored in real time for 48 h. Data were collected every 30 min during the first 22 h, every 2 min from 22 to 25 h, and every 10 min from 26 to 48 h. Nanoparticles did not interfere with the instrument detection system and were not toxic to tumor targets. NCL22, NCL23, and NCL24 did not interfere with cytotoxicity of NK cells towards tumor targets. NCL22 and NCL24 slightly inhibited the viability of effector NK92 cells.', NULL, NULL),
(3801130, 'Figure 50B.pdf', '/particles/NCL-22-1/nkCellCytotoxicActivity/20070413_10-00-55-585_Figure 50B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by (RT-CES).', 'Analysis of cytotoxic activity of NK cells by (RT-CES). The NK cell line NK92 (source American Type Culture Collection [ATCC]) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively. Effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 without, or in the presence of, NCL22, NCL23, and NCL24 (%CV < 20%). Additional samples were included to control for nanoparticle-associated toxicity to target cells. Cell viability was continuously monitored in real time for 48 h. Data were collected every 30 min during the first 22 h, every 2 min from 22 to 25 h, and every 10 min from 26 to 48 h. Nanoparticles did not interfere with the instrument detection system and were not toxic to tumor targets. NCL22, NCL23, and NCL24 did not interfere with cytotoxicity of NK cells towards tumor targets. NCL22 and NCL24 slightly inhibited the viability of effector NK92 cells.', NULL, NULL),
(3801131, 'Figure50A.pdf', '/particles/NCL-22-1/nkCellCytotoxicActivity/20070413_10-01-54-452_Figure50A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A).', 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A). The NK cell line NK92 (source Laboratory of Experimental Immunology [LEI]) and tumor cell line K562 (source Developmental Therapeutics Program [DTP]) were used as effectors and targets, respectively. The effector and 51Cr-loaded target cells were co-cultured at different effector-to-target (E:T) ratios without, or in the presence of, NCL22, NCL23, and NCL24. For each concentration, two independent samples were prepared and analyzed in triplicate (%CV < 20%). Each data point represents the mean of triplicate results. Additional samples were included to control for nanoparticle-induced chromium release from target cells. NCL22 at both concentrations and NCL23 at a low concentration did not interfere with the cytotoxicity of NK cells, while high doses of NCL23 and NCL24 slightly inhibited the cytotoxicity of NK cells towards K562 targets.', NULL, NULL),
(3801132, 'Figure50A.pdf', '/particles/NCL-24-1/nkCellCytotoxicActivity/20070413_10-04-01-744_Figure50A.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A).', 'Analysis of cytotoxic activity of NK cells by 51Cr-release Assay (ITA-11A). The NK cell line NK92 (source Laboratory of Experimental Immunology [LEI]) and tumor cell line K562 (source Developmental Therapeutics Program [DTP]) were used as effectors and targets, respectively. The effector and 51Cr-loaded target cells were co-cultured at different effector-to-target (E:T) ratios without, or in the presence of, NCL22, NCL23, and NCL24. For each concentration, two independent samples were prepared and analyzed in triplicate (%CV < 20%). Each data point represents the mean of triplicate results. Additional samples were included to control for nanoparticle-induced chromium release from target cells. NCL22 at both concentrations and NCL23 at a low concentration did not interfere with the cytotoxicity of NK cells, while high doses of NCL23 and NCL24 slightly inhibited the cytotoxicity of NK cells towards K562 targets.', NULL, NULL),
(3801133, 'Figure 50B.pdf', '/particles/NCL-24-1/nkCellCytotoxicActivity/20070413_10-05-32-121_Figure 50B.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of cytotoxic activity of NK cells by (RT-CES).', 'Analysis of cytotoxic activity of NK cells by (RT-CES). The NK cell line NK92 (source American Type Culture Collection [ATCC]) and tumor cell line HepG2 (source DTP) were used as effectors and targets, respectively. Effector and target cells were co-cultured at an effector-to-target (E:T) ratio of 1:5 without, or in the presence of, NCL22, NCL23, and NCL24 (%CV < 20%). Additional samples were included to control for nanoparticle-associated toxicity to target cells. Cell viability was continuously monitored in real time for 48 h. Data were collected every 30 min during the first 22 h, every 2 min from 22 to 25 h, and every 10 min from 26 to 48 h. Nanoparticles did not interfere with the instrument detection system and were not toxic to tumor targets. NCL22, NCL23, and NCL24 did not interfere with cytotoxicity of NK cells towards tumor targets. NCL22 and NCL24 slightly inhibited the viability of effector NK92 cells.', NULL, NULL),
(4128772, 'Figure 7.emf', '/particles/NCL-42/surface/20070531_12-20-25-169_Figure 7.emf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'THE ZETA POTENTIAL DISTRIBUTION DERIVED FROM THE ELECTROPHORETIC MOBILITY FOR DF1 IN 10 MM NACL. ', 'The zeta potential distribution derived from the electrophoretic mobility for DF1 in 10 mM NaCl. ', NULL, NULL),
(4128773, 'Figure 8.emf', '/particles///20070531_12-51-20-564_Figure 8.emf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Electropherogram of DF1', 'The chromatogram shows a major peak eluting at ~10 minutes with several minor peaks eluting before. This suggests that DF1 contains impurities and is consistent with the RP-HPLC data.', NULL, NULL);
INSERT INTO `lab_file` (`file_pk_id`, `file_name`, `file_uri`, `file_type_extension`, `file_source_type`, `version`, `status`, `reason`, `created_by`, `created_date`, `sample_sop_pk_id`, `run_pk_id`, `data_status_pk_id`, `title`, `description`, `comments`, `type`) VALUES
(4128783, 'Figure 30.gif', '/particles/NCL-42/plateletAggregation/20070531_13-41-41-925_Figure 30.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2).', 'AF1, AF3, C3 and DF1 at a final concentration of 200 ÂƒÃg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade. Collagen was used as positive control. The effects of the fullerene derivatives on collagen induced aggregation were studied by combining collagen with the nanoparticle treatments. Three independent samples were prepared for each nanoparticle formulation and analyzed in duplicate (%CV<20%). Bars represent the mean of duplicate results. None of the tested formulations induced platelet aggregation. AF1 did not interfere with collagen-induced aggregation, AF3 resulted in approximately a 30% decrease in collagen-induced platelet aggregation, and DF1 and C3 prevented aggregation of platelets in response to collagen. At tested concentrations, none of the C60 derivatives induced platelet aggregation (all tested fullerene derivatives showed % aggregation <10). When platelet aggregation was induced by collagen, AF1 did not interfere with this process. AF3 resulted in approximately a 30% decrease in collagen-induced platelet aggregation. C3 and DF1 prevented platelet aggregation caused by collagen (Figure 30).', NULL, NULL),
(4128784, 'Figure 30.gif', '/particles///20070531_13-43-00-5_Figure 30.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2)', 'AF1, AF3, C3 and DF1 at a final concentration of 200 ÂƒÃg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade. Collagen was used as positive control. The effects of the fullerene derivatives on collagen induced aggregation were studied by combining collagen with the nanoparticle treatments. Three independent samples were prepared for each nanoparticle formulation and analyzed in duplicate (%CV<20%). Bars represent the mean of duplicate results. None of the tested formulations induced platelet aggregation. AF1 did not interfere with collagen-induced aggregation, AF3 resulted in approximately a 30% decrease in collagen-induced platelet aggregation, and DF1 and C3 prevented aggregation of platelets in response to collagen.', NULL, NULL),
(4128785, 'Figure 30.gif', '/particles///20070531_13-46-13-312_Figure 30.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle ability to induce platelet aggregation (ITA-2)', 'AF1, AF3, C3 and DF1 at a final concentration of 200 ÂƒÃg/mL were used to evaluate potential particle effects on the cellular component of the blood coagulation cascade. Collagen was used as positive control. The effects of the fullerene derivatives on collagen induced aggregation were studied by combining collagen with the nanoparticle treatments. Three independent samples were prepared for each nanoparticle formulation and analyzed in duplicate (%CV<20%). Bars represent the mean of duplicate results. None of the tested formulations induced platelet aggregation. AF1 did not interfere with collagen-induced aggregation, AF3 resulted in approximately a 30% decrease in collagen-induced platelet aggregation, and DF1 and C3 prevented aggregation of platelets in response to collagen.', NULL, NULL),
(4259840, 'C60 Figure 2.pdf', '/particles/NCL-42/size/20070517_21-23-14-326_C60 Figure 2.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Volume distribution plot for (A) DF1 and (B) DF1-mini in 10 mM NaCl.', 'The Z-avg size, which is more sensitive to larger size species in the sample, of DF1 is 7.8 nm. The Z-avg size of DF1-mini is 8.9 nm. There is only a slight difference in the volume-weighted size (denoted as Â‘PeakÂ’ in Figure 2) between the two conjugates, with DF1 being slightly larger (4.8 nm) than DF1-mini (4.0 nm) as expected. This data also suggests that under these buffer conditions, there is little or no aggregation in these samples.', NULL, NULL),
(4259841, 'C60 Figure 3.pdf', '/particles/NCL-42/purity/20070517_21-34-30-266_C60 Figure 3.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-VIS SPECTRA FOR DF1 AND DF1-MINI.', 'DF1 and DF1-mini have similar UV-Vis absorption spectra as expected. The peaks at 256 nm and 325 nm are at characteristic wavelengths for absorbance of the fullerene ring and are at approximately the same wavelengths (as expected) for DF1 and DF1-mini. The peaks around 200 nm are attributed to absorbance in the dendrimer branches. There is a small shift in the &#955;max of this peak between the spectra of the DF1 and DF1-mini samples. The &#955;max of the lowest wavelength peak in the DF1 spectrum is at a lower wavelength than that of the analogous peak in the DF1-mini spectrum.', NULL, NULL),
(4259842, 'C60 Figure 4.pdf', '/particles/NCL-42/molecularWeight/20070517_21-46-19-971_C60 Figure 4.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mass spectra for (A) DF1 and (B) DF1-mini.', 'The theoretical molecular weights of DF1 and DF1-mini are 2828 Da and 1452 Da, respectively. The experimental molecular weight determined from the mass spectra were 2827 Da for DF1 and 1476 Da (which corresponds to 1452 Da plus the mass of a sodium ion) for DF1-mini. The spectrum of the DF1 sample (A) contains several peaks in addition to the peak corresponding to DF1, suggesting the sample contains impurities or fragments due to MS conditions. Minor peaks (e.g., the peak at 720 Da which corresponds to a fullerene ring without any dendritic branches) are also present in the spectrum of DF1-mini (B) and suggest that minor impurities are also present in this sample.', NULL, NULL),
(4259843, 'C60 Figure 5.pdf', '/particles/NCL-42/purity/20070517_21-55-45-102_C60 Figure 5.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RP-HPLC chromatogram for DF1.', 'For DF1, the UV spectra (data not shown) for the peaks (see Figure 5) highlighted in cyan in Table 1 are consistent with that of DF1 measured with a UV-Vis spectrophotometer. The UV spectral analysis on the remaining peaks showed similar absorption peaks but were slightly shifted to lower wavelengths (blue-shifted by ~10 nm). The UV absorption at these wavelengths is indicative of the presence of fullerene and not free dendritic branches. This data is consistent with published reports by Gharbi et al. (Anal. Chem. 2003, 75, 4217-4222).', NULL, NULL),
(4259844, 'Figure 8.emf', '/particles/NCL-42/purity/20070531_13-04-09-569_Figure 8.emf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Electropherogram of DF1', 'The chromatogram shows a major peak eluting at ~10 minutes with several minor peaks eluting before. This suggests that DF1 contains impurities and is consistent with the RP-HPLC data.', NULL, NULL),
(4259845, 'Figure 11 gif.gif', '/particles/NCL-42/purity/20070531_13-09-36-982_Figure 11 gif.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Calibration of dendrofullerence (DF1) in serum using capillary zone electrophoresis', 'Capillary: 50 Âµm x 40 cm with dynamic coating; buffer: 40 mM sodium tetraborate (pH 9.2); voltage: -14 kV. Serum samples were diluted 5-fold with SDS solution (20 mM final concentration). The DF1 concentrations refer to the undiluted samples. ', NULL, NULL),
(4259846, 'Figure 15.gif', '/particles/NCL-42/cellViability/20070531_13-13-59-976_Figure 15.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'blue dye-exclusion assay in LLC-PK1 and HL60 cells.', 'LLC-PK1 (A) and HL-60 (B) cells were treated for 24 hours with 0.25 mg/mL of test samples. Cytotoxicity was determined by the trypan blue dye-exclusion assay, as described in methods. The data points are the mean + the standard error, with N=3.', NULL, NULL),
(4259847, 'Figure 15.gif', '/particles/NCL-42/cellViability/20070531_13-17-15-167_Figure 15.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Trypan blue dye-exclusion assay in LLC-PK1 and HL60 cells. ', '.  LLC-PK1 (A) and HL-60 (B) cells were treated for 24 hours with 0.25 mg/mL of test samples. Cytotoxicity was determined by the trypan blue dye-exclusion assay, as described in methods. The data points are the mean + the standard error, with N=3.The test concentration of AF1, AF3, DF1 and C3 utilized in the LLC-PK1 and HL60 cell trypan blue cytotoxicity assays was 0.25 mg/mL. Treatment of LLC-PK1 cells with AF1, AF3 or DF1 for 24 hours, did not produce a significant loss of cell viability, as determined by the trypan blue dye-exclusion assay (Figure 15A). However, treatment of LLC-PK1 cells with C3, at the same test concentration and exposure period, resulted in a significant loss of cell viability compared to media treated control (Figure 15A). Treatment of HL60 cells with either DF1 or C3, resulted in a significant loss of cell viability (Figure 15B), as measured by the trypan blue exclusion assay.', NULL, NULL),
(4259848, 'Figure 17.gif', '/particles/NCL-42/oxidativeStress/20070531_13-26-41-741_Figure 17.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ROS Assay in LLC-PK1 cells. ', '. LLC-PK1 cells were treated for 2 and 4 hours with 0.004-1 mg/mL of AF1 (A) DF1 (B), or C3 (C). ROS was determined as described in described in the Hepatocyte Primary ROS Assay (GTA-7), except that LLC-PK1 cells were used instead of Hep G2 cells. Data represents the mean + the standard error, N=3.', NULL, NULL),
(4259849, 'Figure 18.gif', '/particles/NCL-42/oxidativeStress/20070531_13-27-34-599_Figure 18.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ROS Assay in SD Primary Hepatocytes.', 'SD primary hepatocytes were treated for 0-4 hours with 0.004-1 mg/mL of AF1 (A), DF1 (B), or C3 (C). ROS were measured as described in the Hepatocyte Primary ROS Assay (GTA-7). Data represents the mean + the standard error with N=3.', NULL, NULL),
(4259850, 'Figure 21.gif', '/particles/NCL-42/caspase3Activation/20070531_13-31-51-623_Figure 21.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The effect of DF1 cotreatment on 24 hour cisplatin-induced caspase 3 activity. ', 'Cells were treated for 24 hours with 50 ÂƒÃM cisplatin (Cisp) with or without 0.5 mg/mL DF1, or control media. Data are presented as % control caspase 3 activity, normalized to total cellular protein. Bars correspond to the mean + the standard error of six individual samples. Bars with different letters are significantly different from one another ( P < 0.05), as determined by an ANOVA statistical analysis.', NULL, NULL),
(4259851, 'Figure 20.gif', '/particles/NCL-42/caspase3Activation/20070531_13-33-39-281_Figure 20.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The effect of DF1 cotreatment on 24 hour cisplatin-induced morphological changes', 'Photomicrographs show representative phase contrast images (x225) of cells treated for 24 hours with 50 ÂƒÃM cisplatin with or without 0.5 mg/mL DF1, or control media. ', NULL, NULL),
(4259852, 'Figure 29.gif', '/particles/NCL-42/hemolysis/20070531_13-38-29-753_Figure 29.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', '125 ug/mL samples of AF1, AF3, C3 and DF1 were tested for effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle sample and analyzed in duplicate (%CV<20). Each bar represents the mean of duplicate results. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively. AF1 and AF3 were hemolytic, while C3 and DF1 did not disturb the integrity of red blood cells.\r\n\r\n', NULL, NULL),
(4259853, 'Figure 31.gif', '/particles/NCL-42/cfu_gm/20070531_13-48-48-200_Figure 31.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle toxicity to bone marrow cells (ITA-3', 'Two independent samples were prepared and analyzed in duplicate. Each bar represents the mean of duplicate responses (%CV <25). Results indicate that none of the tested particles was myelosuppressive or capable of protecting granulocyte-macrophage precursors from cisplatin cytotoxicity. NP stands for nanoparticle. All nanoparticle samples had concentrations of 50 ÂƒÃg/mL', NULL, NULL),
(4259854, 'C60 Figure 2.pdf', '/particles/NCL-45/size/20070531_13-57-30-310_C60 Figure 2.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'VOLUME DISTRIBUTION PLOT FOR (A) DF1 AND (B) DF1-MINI IN 10 MM NACL. ', 'The Z-avg size, which is more sensitive to larger size species in the sample, of DF1 is 7.8 nm. The Z-avg size of DF1-mini is 8.9 nm. There is only a slight difference in the volume-weighted size (denoted as Â‘PeakÂ’ in Figure 2) between the two conjugates, with DF1 being slightly larger (4.8 nm) than DF1-mini (4.0 nm) as expected.  This data also suggests that under these buffer conditions, there is little or no aggregation in these samples.', NULL, NULL),
(4259855, 'Figure 15.gif', '/particles/NCL-17/cellViability/20070531_14-01-46-761_Figure 15.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Trypan blue dye-exclusion assay in LLC-PK1 and HL60 cells.', '.  LLC-PK1 (A) and HL-60 (B) cells were treated for 24 hours with 0.25 mg/mL of test samples. Cytotoxicity was determined by the trypan blue dye-exclusion assay, as described in methods. The data points are the mean + the standard error, with N=3.', NULL, NULL),
(4259856, 'Figure 12.gif', '/particles/NCL-16/cellViability/20070608_9-57-43-500_Figure 12.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Cytotoxicity of AF1 assayed in LLC-PK1 cells.', 'Porcine renal proximal tubule cells were treated for 6, 24, and 48 hours with 0.004-1.0 mg/mL of test sample. Cytotoxicity at each time point was determined by the LDH (A) and MTT (B) assays, as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1).  The data points are the mean + the standard error, with N=3.', NULL, NULL),
(4259857, 'Figure 13.gif', '/particles/NCL-19/cellViability/20070608_10-04-31-674_Figure 13.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'C3 cytotoxicity assayed in LLC-PK1 cells.', 'Porcine renal proximal tubule cells were treated for 6, 24, and 48 hours with 0.004-1.0 mg/mL of test sample. Cytotoxicity at each time point was determined by the LDH (A) and MTT (B) assays, as described in the LLC-PK1 Kidney Cytotoxicity Assay (GTA-1). The data points are the mean + the standard error, with N=3.', NULL, NULL),
(4259858, 'C60 Figure 3.pdf', '/particles/NCL-45/purity/20070614_15-38-06-496_C60 Figure 3.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'UV-Vis spectra for DF1 and DF1-mini', 'DF1 and DF1-mini have similar UV-Vis absorption spectra as expected. The peaks at 256 nm and 325 nm are at characteristic wavelengths for absorbance of the fullerene ring and are at approximately the same wavelengths (as expected) for DF1 and DF1-mini. The peaks around 200 nm are attributed to absorbance in the dendrimer branches. There is a small shift in the lambda max of this peak between the spectra of the DF1 and DF1-mini samples. The lambda max of the lowest wavelength peak in the DF1 spectrum is at a lower wavelength than that of the analogous peak in the DF1-mini spectrum.', NULL, NULL),
(4259859, 'C60 Figure 4.pdf', '/particles/NCL-45/molecularWeight/20070614_16-04-12-948_C60 Figure 4.pdf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Mass spectra for (A) DF1 and (B) DF1-mini', 'The theoretical molecular weights of DF1 and DF1-mini are 2828 Da and 1452 Da, respectively.  The experimental molecular weight determined from the mass spectra were 2827 Da for DF1 and 1476 Da (which corresponds to 1452 Da plus the mass of a sodium ion) for DF1-mini. The spectrum of the DF1 sample (A) contains several peaks in addition to the peak corresponding to DF1, suggesting the sample contains impurities or fragments due to MS conditions. Minor peaks (e.g., the peak at 720 Da which corresponds to a fullerene ring without any dendritic branches) are also present in the spectrum of DF1-mini (B) and suggest that minor impurities are also present in this sample.', NULL, NULL),
(4259860, 'Figure 6.gif', '/particles/NCL-45/purity/20070614_16-11-52-706_Figure 6.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'RP-HPLC chromatogram and elution profile for DF1-mini', 'For DF1-Mini, the UV spectra (data not shown) for the peaks (see Figure 6) highlighted in cyan in Table 2 are consistent with that of DF1-mini measured with a UV-Vis spectrophotometer.  The UV spectral analysis on the remaining two peaks showed a major peak at 225 nm and a minor peak at 254 nm suggesting that these impurities contain the fullerene component.  DF1-mini seems to be relatively purer than the next generation derivative (DF1). This is also consistent with mass spectrometry analysis on these two derivatives. Further analysis with LC-MS would provide additional details on the species present as impurities in these two samples.', NULL, NULL),
(4259861, 'Figure 7.emf', '/particles/NCL-42/surface/20070614_16-18-54-921_Figure 7.emf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The zeta potential distribution derived from the electrophoretic mobility for DF1 in 10 mM NaCl. ', 'The measured zeta potential (-53.6 mV) is negative as expected for a fullerene ring with carboxylated dendritic branches. \r\n', NULL, NULL),
(4259862, 'Figure 15.gif', '/particles/NCL-19/cellViability/20070614_16-36-52-46_Figure 15.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Trypan blue dye-exclusion assay in LLC-PK1 and HL60 cells', 'LLC-PK1 (A) and HL-60 (B) cells were treated for 24 hours with 0.25 mg/mL of test samples. Cytotoxicity was determined by the trypan blue dye-exclusion assay, as described in methods. The data points are the mean + the standard error, with N=3.', NULL, NULL),
(4259863, 'Figure 17.gif', '/particles/NCL-19/oxidativeStress/20070614_16-48-37-596_Figure 17.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ROS Assay in LLC-PK1 cells', 'Figure 18. ROS Assay in SD Primary Hepatocytes. SD primary hepatocytes were treated for 0-4 hours with 0.004-1 mg/mL of AF1 (A), DF1 (B), or C3 (C). ROS were measured as described in the Hepatocyte Primary ROS Assay (GTA-7). Data represents the mean + the standard error with N=3.', NULL, NULL),
(4259864, 'Figure 17.gif', '/particles/NCL-16/oxidativeStress/20070614_16-52-29-170_Figure 17.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'ROS Assay in LLC-PK1 cells', 'Figure 17. ROS Assay in LLC-PK1 cells. LLC-PK1 cells were treated for 2 and 4 hours with 0.004-1 mg/mL of AF1 (A) DF1 (B), or C3 (C). ROS was determined as described in described in the Hepatocyte Primary ROS Assay (GTA-7), except that LLC-PK1 cells were used instead of Hep G2 cells. Data represents the mean + the standard error, N=3.', NULL, NULL),
(4259865, 'Figure 1.gif', '/particles/NCL-48/size/20070713_10-28-02-688_Figure 1.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Comparison of particle size of two batches of NCL48 and NCL49', 'The average size by volume distributions (each line is the average of at least seven measurements) for both batches of (A) NCL48 and (B) NCL49 in PBS and saline (154 mM NaCl). Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', NULL, ''),
(4259866, 'Figure 2.gif', '/particles/NCL-48/size/20070713_10-33-20-711_Figure 2.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The effect of solvent the hydrodynamic sizes of NCL48 and NCL49.', 'The size by volume distributions (each line in the above figures is the average of at least seven measurements per sample) for (A) NCL48-1 and NCL49-1 in PBS and saline (154 mM NaCl), (B) NCL48-2, and (C) NCL49-2 in water, 10mM NaCl, PBS, and saline (154 mM NaCl). The diameters of the first batch of liposomes (NCL48-1 and NCL49-1) are largely independent of dilution media in PBS and saline (154 mM NaCl). NCL49-1 is slightly larger (~10 nm) than NCL48-1 in both examined media. \r\n\r\nFor NCL48-2, studied in H20, 10 mM NaCl, and saline (154 mM NaCl), there is a slight increase in size as the ionic strength increases, though as in the first batch no significant difference in size in PBS and saline is observed. For NCL49-2, the size is relatively constant for the lower ionic strength media (H20 and 10 mM NaCl) but increases by ~30 nm for the higher ionic strength media (PBS and saline). NCL49-2 is approximately 10 nm larger than NCL48-2 in all studied media. This size difference between the ceramide liposome and ghost liposome is similar to that observed for the first batch of liposomes.  It is interesting to note that a larger-sized population appears and may be liposomal aggregates. \r\n', NULL, NULL),
(4259867, 'Figure 4.gif', '/particles/NCL-48/size/20070713_10-36-40-998_Figure 4.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The effect of concentration on hydrodynamic size for NCL48-1 and NCL49-1 in PBS, and NCL48-2 and NCL49-2 in saline. ', 'The average size by volume distributions (each line is the average of at least seven measurements) for (A) NCL48-1 and (B) NCL49-1 in PBS, and (C) NCL48-2 and (D) NCL49-2 in saline, all at 3 different concentrations. For NCL48-1, there is a slight decrease in size for the 10 mg/mL sample compared to the 0.1 and 1 mg/mL samples which show no concentration dependence. For NCL49-1, there is a slight increase in size for the 10 mg/mL sample compared to the 0.1 and 1 mg/mL samples, which show no concentration dependence.\r\n\r\nFor NCL48-2 and NCL49-2, there was a slight increase in size for the 25 mg/mL sample compared to the 0.25 and 2.5 mg/mL samples which show no concentration dependence. \r\n\r\nOver certain concentration ranges, concentration can affect specific viscosity. The high concentration samples (10 mg/mL for NCL48-1 and NCL49-1 and 25 mg/mL for NCL48-2 and NCL49-2) may be more viscous than the low concentration samples and this could contribute to higher measured hydrodynamic sizes.\r\n', NULL, NULL),
(4259868, 'Figure 15.gif', '/particles/NCL-48/surface/20070720_10-14-41-593_Figure 15.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'The zeta potential distributions. ', 'The zeta potential distributions derived from the electrophoretic mobility for (A) NCL48-1(average of at least 7 measurements), (B.) NCL49-1(average of at least 7 measurements), (C) NCL48-2, and (D) NCL49-2 in 10 mM NaCl. ', NULL, NULL),
(4259869, 'Figure 23.emf', '/particles/NCL-48/cellViability/20070720_10-39-19-463_Figure 23.emf', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'NCL48-1 cytotoxicity assay in Hep G2 cells. ', 'Porcine renal proximal tubule cells were treated for 6, 24, and 48 hour with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point by the LDH (A) and MTT (B) assays, as described in the Hep G2 Hepatocarcinoma Assay (GTA-2).  Data represents the mean + SE, N=3.\r\n \r\n', NULL, NULL),
(4259870, 'Figure 24.gif', '/particles/NCL-49/cellViability/20070720_10-43-36-156_Figure 24.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'NCL49-1 cytotoxicity assay in Hep G2 cells.', 'NCL49-1 cytotoxicity assay in Hep G2 cells. Porcine renal proximal tubule cells were treated for 6, 24, and 48 hour with 0.004-1.0 mg/mL of test sample. Cytotoxicity was determined at each time point by the LDH (A) and MTT (B) assays, as described in the Hep G2 Hepatocarcinoma Assay (GTA-2). Data represents the mean + SE, N=3.\r\n \r\n', NULL, NULL),
(4259871, 'Figure 28.gif', '/particles/NCL-49/hemolysis/20070720_10-51-51-657_Figure 28.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', 'Analysis of nanoparticle hemolytic properties (ITA-1). NCL48-1 and NCL49-1 at high (1 mg/mL) and low (0.2 and 0.0625 mg/mL, respectively) concentration were used to evaluate potential particle effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle concentration and analyzed in duplicate (%CV<25). Each bar represents the mean of duplicate results. ND indicates there were no detectable results. Triton X-100 was used as a positive control. PBS was used to reconstitute nanoparticles and represented the negative control. At high concentration (1 mg/ml) both NCL48-1 and NCL49-1 disturbed the integrity of red blood cells as manifest by percent hemolysis at or above 5%. Low concentrations of NCL48-1 (0.2mg/mL) and NCL49-1 (0.0625 mg/mL) were not hemolytic.', NULL, NULL),
(4456448, 'Figure 29.gif', '/particles/NCL-16/hemolysis/20070531_13-38-29-753_Figure 29.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', '125 ug/mL samples of AF1, AF3, C3 and DF1 were tested for effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle sample and analyzed in duplicate (%CV<20). Each bar represents the mean of duplicate results. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively. AF1 and AF3 were hemolytic, while C3 and DF1 did not disturb the integrity of red blood cells.\r\n\r\n', NULL, NULL),
(4456449, 'Figure 29.gif', '/particles/NCL-17/hemolysis/20070531_13-38-29-753_Figure 29.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', '125 ug/mL samples of AF1, AF3, C3 and DF1 were tested for effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle sample and analyzed in duplicate (%CV<20). Each bar represents the mean of duplicate results. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively. AF1 and AF3 were hemolytic, while C3 and DF1 did not disturb the integrity of red blood cells.\r\n\r\n', NULL, NULL),
(4456450, 'Figure 29.gif', '/particles/NCL-19/hemolysis/20070531_13-38-29-753_Figure 29.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', '125 ug/mL samples of AF1, AF3, C3 and DF1 were tested for effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle sample and analyzed in duplicate (%CV<20). Each bar represents the mean of duplicate results. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively. AF1 and AF3 were hemolytic, while C3 and DF1 did not disturb the integrity of red blood cells.\r\n\r\n', NULL, NULL),
(4456451, 'Figure 29.gif', '/particles/NCL-45/hemolysis/20070531_13-38-29-753_Figure 29.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Analysis of nanoparticle hemolytic properties (ITA-1).', '125 ug/mL samples of AF1, AF3, C3 and DF1 were tested for effects on the integrity of red blood cells. Three independent samples were prepared for each nanoparticle sample and analyzed in duplicate (%CV<20). Each bar represents the mean of duplicate results. Poly-L-lysine (PLL) and polyethyleneglycol (PEG) were used as positive and negative controls, respectively. AF1 and AF3 were hemolytic, while C3 and DF1 did not disturb the integrity of red blood cells.\r\n\r\n', NULL, NULL),
(4521984, 'NCL_Method_GTA-2.pdf', 'protocols/20071116_13-59-04-665_NCL_Method_GTA-2.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 13:59:04', NULL, NULL, NULL, 'MTT AND LDH RELEASE (HEP G2)', NULL, NULL, NULL),
(4521985, 'NCL_Method_GTA-3.pdf', 'protocols/20071116_14-00-06-875_NCL_Method_GTA-3.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:00:06', NULL, NULL, NULL, 'GSH HOMEOSTASIS (HEP G2)', NULL, NULL, NULL),
(4521986, 'NCL_Method_GTA-4.pdf', 'protocols/20071116_14-00-58-843_NCL_Method_GTA-4.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:00:58', NULL, NULL, NULL, 'LIPID PEROXIDATION (HEP G2)', NULL, NULL, NULL),
(4521987, 'NCL_Method_GTA-5.pdf', 'protocols/20071116_14-02-19-894_NCL_Method_GTA-5.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:02:19', NULL, NULL, NULL, 'CASPASE 3 ACTIVATION (PORCINE RENAL PROXIMAL TUBULE CELL)', NULL, NULL, NULL),
(4521988, 'NCL_Method_GTA-6.pdf', 'protocols/20071116_14-03-21-285_NCL_Method_GTA-6.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:03:21', NULL, NULL, NULL, 'CASPASE 3 ACTIVATION (HEP G2)', NULL, NULL, NULL),
(4521989, 'NCL_Method_STE-1.pdf', 'protocols/20071116_14-06-30-964_NCL_Method_STE-1.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:06:30', NULL, NULL, NULL, 'ENDOTOXIN', NULL, NULL, NULL),
(4521990, 'NCL_Method_STE-2.pdf', 'protocols/20071116_14-07-11-585_NCL_Method_STE-2.pdf', NULL, NULL, '1.0', NULL, NULL, NULL, '2007-11-16 14:07:11', NULL, NULL, NULL, 'BACTERIAL/YEAST/MOLD', NULL, NULL, NULL),
(4620288, 'Figure 14.gif', '/particles/NCL-19/cellViability/20080124_09-35-09-344_Figure 14.gif', NULL, NULL, NULL, NULL, NULL, 'jennifer', '2008-01-24 09:35:09', NULL, NULL, NULL, 'Nonlinear Regression of the Dependence of Percent Cytotoxicty on C3 Concentration.', 'This graph displays the nonlinear fit of the 48 hour C3 MTT data to a sigmoidal Hill equation using the WinNonlin analysis software. The percent cytotoxicity versus the C3 concentration is displayed. The IC50 value determined from the fit is 0.145 (0.100 ? 0.187, 95% CI) mg/mL.', NULL, 'Graph'),
(4620291, 'Figure 1.gif', '/particles/NCL-49/size/20070713_10-28-02-688_Figure 1.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Comparison of particle size of two batches of NCL48 and NCL49', 'The average size by volume distributions (each line is the average of at least seven measurements) for both batches of (A) NCL48 and (B) NCL49 in PBS and saline (154 mM NaCl). Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', NULL, ''),
(4620292, 'Figure 1.gif', '/particles/NCL-49/size/20070713_10-28-02-688_Figure 1.gif', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Comparison of particle size of two batches of NCL48 and NCL49', 'The average size by volume distributions (each line is the average of at least seven measurements) for both batches of (A) NCL48 and (B) NCL49 in PBS and saline (154 mM NaCl). Hydrodynamic size (diameter) of the liposome samples was measured in aqueous solutions using DLS at 25Â° C. This measurement included the intensity-weighted average diameter over all size populations (Z-avg), the polydispersity index (PdI), the volume-weighted average diameter over the major volume peak (Vol-Peak), and its percentage of the total population (Vol-Peak %Vol). An instrument with a back scattering detector was used for these measurements in batch mode (i.e. without fractionation). Samples were diluted in either PBS or saline (154 mM NaCl) to a final concentration of 1 mg/mL. For this purpose, 67 or 40 &#61549;L stock for the first and second batch, respectively, was brought up to a final volume of 1 mL in the appropriate solvent. A minimum of seven measurements were taken for each sample (as is, no filtration) in a disposable low volume (polystyrene) cuvette. ', NULL, ''),
(4620293, 'NCL20071A Functionalized Fullerenes.pdf', '/reports/20080124_10-25-31-219_NCL20071A Functionalized Fullerenes.pdf', NULL, NULL, NULL, NULL, NULL, 'jennifer', '2008-01-24 10:25:31', NULL, NULL, NULL, 'NCL200710A FUNCTIONALIZED FULLERENES FOR CSIXTY INC', 'The objective of the C-Sixty, Inc. - NCL collaboration is to aid C-Sixty, Inc. in identifying the most promising candidate from a series of fullerene based antioxidants, and to investigate potential applications related to cancer therapy. The antioxidant candidates submitted for testing were AF1 (NCL16), AF3 (NCL17), C3 (NCL19), DF1 (NCL42), and DF1-mini (NCL45). ', '', NULL),
(4620294, 'NCL200702A Ceramide Liposomes.pdf', '/reports/20080124_10-27-52-501_NCL200702A Ceramide Liposomes.pdf', NULL, NULL, NULL, NULL, NULL, 'jennifer', '2008-01-24 10:27:52', NULL, NULL, NULL, 'NCL200702A CERAMIDE LIPOSOMES FOR MARK KESTER PSU', 'The objective of the Penn State University-NCL collaboration is to characterize C6-ceramide liposomes as a drug delivery platform. ', '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `linkage`
--

CREATE TABLE IF NOT EXISTS `linkage` (
  `linkage_pk_id` bigint(20) NOT NULL,
  `description` varchar(2000) default NULL,
  `discriminator` varchar(200) default NULL,
  `bond_type` varchar(200) default NULL,
  `localization` varchar(200) default NULL,
  `function_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  PRIMARY KEY  (`linkage_pk_id`),
  KEY `fk_linkage_function` (`function_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `linkage`
--

INSERT INTO `linkage` (`linkage_pk_id`, `description`, `discriminator`, `bond_type`, `localization`, `function_pk_id`, `list_index`) VALUES
(2719744, 'Association', 'Other', NULL, NULL, NULL, NULL),
(2719745, 'Association', 'Other', NULL, NULL, 1703937, 0),
(2719746, 'Association', 'Other', NULL, NULL, 2031616, 0);

-- --------------------------------------------------------

--
-- Table structure for table `liposome_composition`
--

CREATE TABLE IF NOT EXISTS `liposome_composition` (
  `is_polymerized` tinyint(4) default NULL,
  `polymer_name` varchar(200) default NULL,
  `l_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`l_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `liposome_composition`
--


-- --------------------------------------------------------

--
-- Table structure for table `morphology`
--

CREATE TABLE IF NOT EXISTS `morphology` (
  `morphology_pk_id` bigint(20) NOT NULL,
  `type` varchar(100) default NULL,
  PRIMARY KEY  (`morphology_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `morphology`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_morphology`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_morphology` BEFORE DELETE ON `cananolab`.`morphology`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       type,
       deleted_date, 
       table_source)
    values
     ( old.morphology_pk_id,
       old.type,
       sysdate(),
       'MORPHOLOGY');
end
//
DELIMITER ;

--
-- Dumping data for table `morphology`
--

INSERT INTO `morphology` (`morphology_pk_id`, `type`) VALUES
(1376277, 'Powder'),
(1867779, 'Powder');

-- --------------------------------------------------------

--
-- Table structure for table `nanoparticle`
--

CREATE TABLE IF NOT EXISTS `nanoparticle` (
  `nanoparticle_pk_id` bigint(20) NOT NULL,
  `classification` varchar(200) default NULL,
  PRIMARY KEY  (`nanoparticle_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nanoparticle`
--

INSERT INTO `nanoparticle` (`nanoparticle_pk_id`, `classification`) VALUES
(524288, 'inorganic'),
(917504, 'organic:hydrocarbon'),
(917505, 'organic:hydrocarbon'),
(917506, 'organic:hydrocarbon'),
(917507, 'organic:hydrocarbon'),
(917508, 'organic:hydrocarbon'),
(917509, 'organic:hydrocarbon'),
(917510, 'organic:hydrocarbon'),
(917511, 'organic:liposome'),
(917512, 'organic:liposome'),
(917513, 'organic:liposome'),
(917514, 'organic:liposome'),
(917515, 'organic:emulsion'),
(917516, 'organic:emulsion'),
(917517, 'organic:emulsion'),
(917518, 'organic:emulsion'),
(917519, 'organic:emulsion'),
(4063232, 'organic:carbon'),
(4063233, 'organic:carbon'),
(4063234, 'organic:carbon'),
(4063235, 'organic:carbon'),
(4063236, 'organic:carbon'),
(4063237, 'organic'),
(4063238, 'organic');

-- --------------------------------------------------------

--
-- Table structure for table `nanoparticle_char`
--

CREATE TABLE IF NOT EXISTS `nanoparticle_char` (
  `characterization_pk_id` bigint(20) NOT NULL,
  `nanoparticle_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`characterization_pk_id`,`nanoparticle_pk_id`),
  KEY `sys_c00246797` (`nanoparticle_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nanoparticle_char`
--

INSERT INTO `nanoparticle_char` (`characterization_pk_id`, `nanoparticle_pk_id`) VALUES
(1310720, 524288),
(1376256, 917504),
(1376257, 917504),
(1376258, 917504),
(1376259, 917504),
(1376260, 917504),
(1376261, 917504),
(1376262, 917504),
(3768322, 917504),
(3768325, 917504),
(1376263, 917505),
(1376264, 917505),
(1376265, 917505),
(1376266, 917505),
(1376267, 917505),
(3768326, 917505),
(1376268, 917506),
(1376269, 917506),
(1376270, 917506),
(1376271, 917506),
(1376272, 917506),
(1376273, 917506),
(1376274, 917506),
(1376275, 917506),
(1376276, 917506),
(1376277, 917506),
(1376278, 917506),
(1376279, 917506),
(1376280, 917506),
(1867784, 917506),
(1867785, 917506),
(1867786, 917506),
(1867787, 917506),
(1867795, 917506),
(1867799, 917506),
(1867801, 917506),
(2424832, 917506),
(2424835, 917506),
(2424836, 917506),
(2424840, 917506),
(2424843, 917506),
(2424844, 917506),
(3768320, 917506),
(3768323, 917506),
(3768324, 917506),
(3768330, 917506),
(3768331, 917506),
(3768336, 917506),
(3768337, 917506),
(1376285, 917507),
(1376286, 917507),
(1376287, 917507),
(1376288, 917507),
(1376289, 917507),
(1867776, 917507),
(1867777, 917507),
(1867778, 917507),
(1867779, 917507),
(1867780, 917507),
(1867781, 917507),
(1867782, 917507),
(1867788, 917507),
(1867789, 917507),
(1867790, 917507),
(1867796, 917507),
(1867800, 917507),
(1867802, 917507),
(2424833, 917507),
(2424839, 917507),
(2424841, 917507),
(2424845, 917507),
(3768321, 917507),
(3768327, 917507),
(3768332, 917507),
(3768335, 917507),
(3768338, 917507),
(1867783, 917508),
(1867791, 917508),
(1867792, 917508),
(1867793, 917508),
(1867794, 917508),
(1867797, 917508),
(1867798, 917508),
(1867803, 917508),
(2424834, 917508),
(2424837, 917508),
(2424838, 917508),
(2424842, 917508),
(2424846, 917508),
(3768333, 917508),
(3768334, 917508),
(3768339, 917508),
(1376283, 917509),
(1376284, 917509),
(3768328, 917509),
(1376281, 917510),
(1376282, 917510),
(3768329, 917510),
(4227085, 4063232),
(4227093, 4063232),
(4423680, 4063232),
(4227084, 4063233),
(4423681, 4063233),
(4227086, 4063234),
(4227091, 4063234),
(4227092, 4063234),
(4423682, 4063234),
(4587520, 4063234),
(4227072, 4063235),
(4227073, 4063235),
(4227074, 4063235),
(4227075, 4063235),
(4227076, 4063235),
(4227077, 4063235),
(4227078, 4063235),
(4227079, 4063235),
(4227080, 4063235),
(4227081, 4063235),
(4227082, 4063235),
(4227090, 4063235),
(4227083, 4063236),
(4227087, 4063236),
(4227088, 4063236),
(4227089, 4063236),
(4423683, 4063236),
(4227094, 4063237),
(4227095, 4063237),
(4227096, 4063237),
(4227097, 4063237),
(4227098, 4063237),
(4227099, 4063238),
(4227100, 4063238),
(4587521, 4063238),
(4587522, 4063238);

-- --------------------------------------------------------

--
-- Table structure for table `particle_function`
--

CREATE TABLE IF NOT EXISTS `particle_function` (
  `particle_function_pk_id` bigint(20) NOT NULL,
  `type` varchar(100) default NULL,
  `activation_method` varchar(500) default NULL,
  `nanoparticle_pk_id` bigint(20) default NULL,
  `identifier_name` varchar(200) default NULL,
  `description` varchar(2000) default NULL,
  PRIMARY KEY  (`particle_function_pk_id`),
  KEY `sys_c00246798` (`nanoparticle_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `particle_function`
--

INSERT INTO `particle_function` (`particle_function_pk_id`, `type`, `activation_method`, `nanoparticle_pk_id`, `identifier_name`, `description`) VALUES
(1703936, 'Diagnostic Imaging', NULL, 917506, 'DNT 082006', NULL),
(1703937, 'Diagnostic Imaging', NULL, 917507, 'DNT 082006', NULL),
(2031616, 'Diagnostic Imaging', NULL, 917508, 'DNT 082006', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `polymer_composition`
--

CREATE TABLE IF NOT EXISTS `polymer_composition` (
  `is_cross_link` tinyint(4) default NULL,
  `cross_link_degree` decimal(22,3) default NULL,
  `initiator` varchar(200) default NULL,
  `p_composition_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`p_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `polymer_composition`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_polymer`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_polymer` BEFORE DELETE ON `cananolab`.`polymer_composition`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       is_cross_link,
       cross_link_degree,
       initiator,
       deleted_date, 
       table_source)
    values
     ( old.p_composition_pk_id,
       old.is_cross_link,
       old.cross_link_degree,
       old.initiator,
       sysdate(),
       'POLYMER_COMPOSITION');
end
//
DELIMITER ;

--
-- Dumping data for table `polymer_composition`
--


-- --------------------------------------------------------

--
-- Table structure for table `protocol`
--

CREATE TABLE IF NOT EXISTS `protocol` (
  `protocol_pk_id` bigint(20) NOT NULL,
  `protocol_name` varchar(2000) default NULL,
  `protocol_type` varchar(2000) default NULL,
  PRIMARY KEY  (`protocol_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `protocol`
--

INSERT INTO `protocol` (`protocol_pk_id`, `protocol_name`, `protocol_type`) VALUES
(2129920, 'GTA-1 MTT', 'In Vitro assay'),
(2129923, 'GTA-2 LDH', 'In Vitro assay'),
(2129928, 'GTA-1 LDH', 'In Vitro assay'),
(2129929, 'GTA-2 MTT', 'In Vitro assay'),
(2129931, 'ITA-1', 'In Vitro assay'),
(2129934, 'ITA-2', 'In Vitro assay'),
(2129937, 'ITA-3', 'In Vitro assay'),
(2392066, 'ITA-12', 'In Vitro assay'),
(2392067, 'ITA-4', 'In Vitro assay'),
(2392068, 'ITA-7', 'In Vitro assay'),
(2392070, 'ITA-9', 'In Vitro assay'),
(2392074, 'ITA-10', 'In Vitro assay'),
(2392076, 'ITA-11', 'In Vitro assay'),
(3964929, 'ITA-6', 'In Vitro assay'),
(3964932, 'ITA-5', 'In Vitro assay'),
(3964935, 'ITA-8', 'In Vitro assay'),
(4390913, 'GTA-1', 'In Vitro assay'),
(4390915, 'GTA-7', 'In Vitro assay'),
(4489216, 'GTA-2', 'In Vitro assay'),
(4489217, 'GTA-3', 'In Vitro assay'),
(4489218, 'GTA-4', 'In Vitro assay'),
(4489219, 'GTA-5', 'In Vitro assay'),
(4489220, 'GTA-6', 'In Vitro assay'),
(4489221, 'STE-1', 'Sterility'),
(4489222, 'STE-2', 'Sterility');

-- --------------------------------------------------------

--
-- Table structure for table `protocol_file`
--

CREATE TABLE IF NOT EXISTS `protocol_file` (
  `protocol_file_pk_id` bigint(20) NOT NULL,
  `protocol_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`protocol_file_pk_id`),
  KEY `con_prot_prot_file` (`protocol_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `protocol_file`
--

INSERT INTO `protocol_file` (`protocol_file_pk_id`, `protocol_pk_id`) VALUES
(103, 2129920),
(104, 2129923),
(102, 2129928),
(105, 2129929),
(108, 2129931),
(112, 2129934),
(113, 2129937),
(111, 2392066),
(114, 2392067),
(117, 2392068),
(119, 2392070),
(109, 2392074),
(110, 2392076),
(116, 3964929),
(115, 3964932),
(118, 3964935),
(101, 4390913),
(106, 4390915),
(4521984, 4489216),
(4521985, 4489217),
(4521986, 4489218),
(4521987, 4489219),
(4521988, 4489220),
(4521989, 4489221),
(4521990, 4489222);

-- --------------------------------------------------------

--
-- Table structure for table `report`
--

CREATE TABLE IF NOT EXISTS `report` (
  `report_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`report_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `report`
--

INSERT INTO `report` (`report_pk_id`) VALUES
(3178496),
(4620293),
(4620294);

-- --------------------------------------------------------

--
-- Table structure for table `sample`
--

CREATE TABLE IF NOT EXISTS `sample` (
  `sample_pk_id` bigint(20) NOT NULL,
  `sample_sequence_id` bigint(20) default NULL,
  `sample_type` varchar(100) default NULL,
  `description` varchar(4000) default NULL,
  `source_sample_id` varchar(100) default NULL,
  `solubility_description` varchar(4000) default NULL,
  `lot_id` varchar(100) default NULL,
  `lot_description` varchar(4000) default NULL,
  `number_of_containers` int(11) default NULL,
  `general_comments` varchar(4000) default NULL,
  `received_date` datetime default NULL,
  `created_by` varchar(200) default NULL,
  `created_date` datetime default NULL,
  `source_pk_id` bigint(20) default NULL,
  `received_by` varchar(200) default NULL,
  `sample_name` varchar(200) default NULL,
  `sample_sop_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`sample_pk_id`),
  KEY `sys_c00246786` (`sample_sop_pk_id`),
  KEY `sys_c00246787` (`source_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sample`
--

INSERT INTO `sample` (`sample_pk_id`, `sample_sequence_id`, `sample_type`, `description`, `source_sample_id`, `solubility_description`, `lot_id`, `lot_description`, `number_of_containers`, `general_comments`, `received_date`, `created_by`, `created_date`, `source_pk_id`, `received_by`, `sample_name`, `sample_sop_pk_id`) VALUES
(524288, 52, 'Metal Particle', 'Gold nanoparticle linker', NULL, 'In water', 'A009', NULL, NULL, NULL, '2006-06-16 00:00:00', 'clogston', '2006-06-19 08:51:35', 491520, NULL, 'NCL-52-A009', NULL),
(917504, 20, 'Dendrimer', 'G4 DAB core Tris surface PAMAM dendrimer', 'DNT-232', NULL, '1', NULL, NULL, 'DNT Proposal', '2005-10-03 00:00:00', 'skoczen', '2006-07-10 15:13:20', 884736, NULL, 'NCL-20-1', NULL),
(917505, 21, 'Dendrimer', 'G4 DAB core Pyr surface PAMAM dendrimer', 'DNT-280', NULL, '1', NULL, NULL, 'DNT Proposal', '2005-10-03 00:00:00', 'skoczen', '2006-07-10 15:15:29', 884736, NULL, 'NCL-21-1', NULL),
(917506, 22, 'Dendrimer', 'G4.5 DAB core COONa surface PAMAM dendrimer', 'DNT-203', NULL, '1', NULL, NULL, NULL, '2005-10-03 00:00:00', 'skoczen', '2006-07-10 15:17:21', 884736, NULL, 'NCL-22-1', NULL),
(917507, 23, 'Dendrimer', 'G4.5 DAB core COONa Surface Magnevist PAMAM dendrimer', NULL, NULL, '1', NULL, NULL, 'DNT proposal', '2005-10-03 00:00:00', 'skoczen', '2006-07-10 15:18:44', 884736, NULL, 'NCL-23-1', NULL),
(917508, 24, 'Dendrimer', 'Magnevist', NULL, NULL, '1', NULL, NULL, 'DNT proposal', '2005-10-03 00:00:00', 'skoczen', '2006-07-10 15:20:19', 884736, NULL, 'NCL-24-1', NULL),
(917509, 25, 'Dendrimer', 'G4 DAB core Tris surface Magnevist PAMAM dendrimer', 'SG-58-26A', NULL, '1', NULL, NULL, 'DNT proposal', '2005-10-12 00:00:00', 'skoczen', '2006-07-10 15:21:52', 884736, NULL, 'NCL-25-1', NULL),
(917510, 26, 'Dendrimer', 'G4 DAB core Pyr surface Magnevist PAMAM dendrimer', 'SG-58-26B', NULL, '1', NULL, NULL, 'DNT Proposal', '2005-10-13 00:00:00', 'skoczen', '2006-07-10 15:23:31', 884736, NULL, 'NCL-26-1', NULL),
(917511, 48, 'Liposome', 'Ghost Liposomes', NULL, NULL, '4', NULL, NULL, 'Kester Proposal', '2005-05-18 00:00:00', 'skoczen', '2006-07-10 15:26:24', 884737, NULL, 'NCL-48-4', NULL),
(917512, 49, 'Liposome', 'Ceramide Liposomes', NULL, NULL, '2', NULL, NULL, 'Kester Proposal', '2005-05-18 00:00:00', 'skoczen', '2006-07-10 15:28:50', 884737, NULL, 'NCL-49-2', NULL),
(917513, 50, 'Liposome', 'Ghost liposomes with 3H (5.5uCi)', NULL, NULL, '1', NULL, NULL, 'Kester Proposal', '2005-05-18 00:00:00', 'skoczen', '2006-07-10 15:34:11', 884737, NULL, 'NCL-50-1', NULL),
(917514, 51, 'Liposome', 'Ceramide Liposomes with 14C (8.5uCi) & 3H (8.5uCi)', NULL, NULL, '3', NULL, NULL, 'Kester Proposal', '2005-05-18 00:00:00', 'skoczen', '2006-07-10 15:35:53', 884737, NULL, 'NCL-51-3', NULL),
(917515, 55, 'Emulsion', 'NE-Blank', 'NE-Blank', NULL, '1', NULL, NULL, 'Proposal', '2006-06-20 00:00:00', 'skoczen', '2006-07-10 15:40:54', 884738, NULL, 'NCL-55-1', NULL),
(917516, 56, 'Emulsion', 'NE-Gd+3', 'NE-Gd+3', NULL, '2', NULL, NULL, 'proposal\r\n2nd batch', '2006-07-07 00:00:00', 'skoczen', '2006-07-10 15:43:30', 884738, NULL, 'NCL-56-2', NULL),
(917517, 57, 'Emulsion', 'NE-Gd+3-PTX', 'NE-Gd+3-PTX', NULL, '2', NULL, NULL, 'Proposal\r\n2nd batch', '2006-07-07 00:00:00', 'skoczen', '2006-07-10 15:45:11', 884738, NULL, 'NCL-57-2', NULL),
(917518, 58, 'Emulsion', 'NE-Gd+3-Cer', 'NE-Gd+3-Cer', NULL, '2', NULL, NULL, 'Proposal\r\n2nd batch', '2006-07-07 00:00:00', 'skoczen', '2006-07-10 15:46:28', 884738, NULL, 'NCL-58-2', NULL),
(917519, 59, 'Emulsion', 'NE-Gd+3-PTX-Cer', 'NE-Gd+3-PTX-Cer', NULL, '2', NULL, NULL, 'Proposal\r\n2nd batch', '2006-07-07 00:00:00', 'skoczen', '2006-07-10 15:47:59', 884738, NULL, 'NCL-59-2', NULL),
(4063232, 16, 'Fullerene', 'NCL16: An amphiphilic fullerene C60 derivative commonly called AF1.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-05-17 20:54:16', 4030464, NULL, 'NCL-16', NULL),
(4063233, 17, 'Fullerene', 'NCL17: An amphiphilic fullerene C60 derivative commonly called AF3.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-05-17 20:59:50', 4030464, NULL, 'NCL-17', NULL),
(4063234, 19, 'Fullerene', 'NCL19: A tris-malonic acid C60 derivative commonly called C3.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-05-17 21:00:55', 4030464, NULL, 'NCL-19', NULL),
(4063235, 42, 'Fullerene', 'NCL42: A dendrofullerene C60 derivative with branched architecture and 18 carboxylic acid terminal groups, commonly called DF1.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-05-17 21:06:15', 4030464, NULL, 'NCL-42', NULL),
(4063236, 45, 'Fullerene', 'NCL45: A dendrofullerene derivative with 6 carboxylic acid terminal groups, referred to here as DF1-mini.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-05-17 21:07:20', 4030464, NULL, 'NCL-45', NULL),
(4063237, 48, 'Liposome', 'NCL48 is a control empty or Â“ghostÂ” liposome. The liposomes are composed\r\nof 1,2-Distearoyl-sn-Glycero-3-Phosphocholine (DSPC), 1,2-Dioleoyl-sn-Glycero-3-\r\nPhosphoethanolamine (DOPE), PEG(2000)- 1,2-Distearoyl-sn-Glycero-3-\r\nPhosphoethanolamine (DSPE), and PEG(750)-C8.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-06-08 10:13:14', 4030465, NULL, 'NCL-48', NULL),
(4063238, 49, 'Liposome', 'NCL49 is a liposome associated with 30%\r\nC6-ceramide. The liposomes are composed\r\nof 1,2-Distearoyl-sn-Glycero-3-Phosphocholine (DSPC), 1,2-Dioleoyl-sn-Glycero-3-\r\nPhosphoethanolamine (DOPE), PEG(2000)- 1,2-Distearoyl-sn-Glycero-3-\r\nPhosphoethanolamine (DSPE), and PEG(750)-C8.', NULL, NULL, 'N/A', NULL, NULL, NULL, NULL, 'jennifer', '2007-06-08 10:14:27', 4030465, NULL, 'NCL-49', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `sample_associated_file`
--

CREATE TABLE IF NOT EXISTS `sample_associated_file` (
  `sample_pk_id` bigint(20) NOT NULL,
  `associated_file_pk_id` bigint(20) NOT NULL,
  KEY `sys_c00246806` (`sample_pk_id`),
  KEY `sys_c00246808` (`associated_file_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sample_associated_file`
--


-- --------------------------------------------------------

--
-- Table structure for table `sample_container`
--

CREATE TABLE IF NOT EXISTS `sample_container` (
  `sample_container_pk_id` bigint(20) NOT NULL,
  `quantity` decimal(22,3) default NULL,
  `concentration` decimal(22,3) default NULL,
  `volume` decimal(22,0) default NULL,
  `diluents_solvent` varchar(500) default NULL,
  `safety_precautions` varchar(4000) default NULL,
  `storage_conditions` varchar(1000) default NULL,
  `comments` varchar(4000) default NULL,
  `quantity_unit` varchar(100) default NULL,
  `concentration_unit` varchar(100) default NULL,
  `volume_unit` varchar(100) default NULL,
  `barcode` varchar(50) default NULL,
  `container_type` varchar(200) default NULL,
  `sample_pk_id` bigint(20) default NULL,
  `is_derived` varchar(20) default NULL,
  `created_method` varchar(500) default NULL,
  `reason` varchar(2000) default NULL,
  `status` varchar(20) default NULL,
  `storage_pk_id` bigint(20) default NULL,
  `created_date` datetime default NULL,
  `created_by` varchar(200) default NULL,
  `name` varchar(200) default NULL,
  `data_status_pk_id` bigint(20) default NULL,
  PRIMARY KEY  (`sample_container_pk_id`),
  KEY `sys_c00246788` (`storage_pk_id`),
  KEY `sys_c00246789` (`sample_pk_id`),
  KEY `sys_c00246790` (`data_status_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sample_container`
--

INSERT INTO `sample_container` (`sample_container_pk_id`, `quantity`, `concentration`, `volume`, `diluents_solvent`, `safety_precautions`, `storage_conditions`, `comments`, `quantity_unit`, `concentration_unit`, `volume_unit`, `barcode`, `container_type`, `sample_pk_id`, `is_derived`, `created_method`, `reason`, `status`, `storage_pk_id`, `created_date`, `created_by`, `name`, `data_status_pk_id`) VALUES
(557056, NULL, '4.000', '0', 'water', NULL, '4 C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Vial', 524288, 'false', NULL, NULL, NULL, NULL, '2006-06-19 08:51:35', 'clogston', 'NCL-52-A009-1', NULL),
(557057, NULL, '4.000', '0', 'water', NULL, '4 C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Vial', 524288, 'false', NULL, NULL, NULL, NULL, '2006-06-19 08:51:35', 'clogston', 'NCL-52-A009-2', NULL),
(557058, NULL, '4.000', '0', 'water', NULL, '4 C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Vial', 524288, 'false', NULL, NULL, NULL, NULL, '2006-06-19 08:51:35', 'clogston', 'NCL-52-A009-3', NULL),
(557059, NULL, '4.000', '0', 'water', NULL, '4 C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Vial', 524288, 'false', NULL, NULL, NULL, NULL, '2006-06-19 08:51:35', 'clogston', 'NCL-52-A009-4', NULL),
(950272, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917504, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:13:20', 'skoczen', 'NCL-20-1-1', NULL),
(950273, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917505, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:15:29', 'skoczen', 'NCL-21-1-1', NULL),
(950274, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917506, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:17:21', 'skoczen', 'NCL-22-1-1', NULL),
(950275, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917507, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:18:44', 'skoczen', 'NCL-23-1-1', NULL),
(950276, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917508, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:20:19', 'skoczen', 'NCL-24-1-1', NULL),
(950277, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917509, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:21:52', 'skoczen', 'NCL-25-1-1', NULL),
(950278, '1.000', NULL, NULL, NULL, NULL, '-20C', NULL, 'g', NULL, NULL, NULL, 'Vial', 917510, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:23:31', 'skoczen', 'NCL-26-1-1', NULL),
(950279, NULL, '15.000', '11', 'Saline', NULL, '4C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Tube', 917511, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:26:24', 'skoczen', 'NCL-48-4-1', NULL),
(950280, NULL, '15.000', '11', 'Saline', NULL, '4C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Tube', 917512, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:28:50', 'skoczen', 'NCL-49-2-1', NULL),
(950281, NULL, '15.000', '11', 'Saline', 'Radiolabeled', '4C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Tube', 917513, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:34:11', 'skoczen', 'NCL-50-1-1', NULL),
(950282, NULL, '15.000', '17', 'saline', 'radiolabeled', '4C', NULL, NULL, 'mg/ml', 'ml', NULL, 'Tube', 917514, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:35:53', 'skoczen', 'NCL-51-3-1', NULL),
(950283, NULL, NULL, '4', NULL, NULL, '4C', NULL, NULL, NULL, 'ml', NULL, 'Tube', 917515, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:40:54', 'skoczen', 'NCL-55-1-1', NULL),
(950284, NULL, NULL, '5', NULL, NULL, '4C', NULL, NULL, NULL, 'ml', NULL, 'Tube', 917516, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:43:30', 'skoczen', 'NCL-56-2-1', NULL),
(950285, NULL, NULL, '5', NULL, NULL, '4C', NULL, NULL, NULL, 'ml', NULL, 'Tube', 917517, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:45:11', 'skoczen', 'NCL-57-2-1', NULL),
(950286, NULL, NULL, '5', NULL, NULL, '4', NULL, NULL, NULL, 'ml', NULL, 'Tube', 917518, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:46:28', 'skoczen', 'NCL-58-2-1', NULL),
(950287, NULL, NULL, '5', NULL, NULL, '4C', NULL, NULL, NULL, 'ml', NULL, 'Tube', 917519, 'false', NULL, NULL, NULL, NULL, '2006-07-10 15:47:59', 'skoczen', 'NCL-59-2-1', NULL),
(4096000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Tube', 4063232, 'false', NULL, NULL, NULL, NULL, '2007-05-17 20:54:16', 'jennifer', 'NCL-16-0-1', NULL),
(4096001, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063233, 'false', NULL, NULL, NULL, NULL, '2007-05-17 20:59:50', 'jennifer', 'NCL-17-0-1', NULL),
(4096002, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063234, 'false', NULL, NULL, NULL, NULL, '2007-05-17 21:00:55', 'jennifer', 'NCL-19-0-1', NULL),
(4096003, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063235, 'false', NULL, NULL, NULL, NULL, '2007-05-17 21:06:15', 'jennifer', 'NCL-42-0-1', NULL),
(4096004, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063236, 'false', NULL, NULL, NULL, NULL, '2007-05-17 21:07:20', 'jennifer', 'NCL-45-0-1', NULL),
(4096005, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063237, 'false', NULL, NULL, NULL, NULL, '2007-06-08 10:13:14', 'jennifer', 'NCL-48-0-1', NULL),
(4096006, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Vial', 4063238, 'false', NULL, NULL, NULL, NULL, '2007-06-08 10:14:27', 'jennifer', 'NCL-49-0-1', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `sample_report`
--

CREATE TABLE IF NOT EXISTS `sample_report` (
  `sample_pk_id` bigint(20) NOT NULL,
  `file_pk_id` bigint(20) NOT NULL,
  KEY `sys_c00246811` (`file_pk_id`),
  KEY `sys_c00246812` (`sample_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sample_report`
--

INSERT INTO `sample_report` (`sample_pk_id`, `file_pk_id`) VALUES
(917504, 3178496),
(917505, 3178496),
(917506, 3178496),
(917507, 3178496),
(917508, 3178496),
(917509, 3178496),
(917510, 3178496),
(4063232, 4620293),
(4063233, 4620293),
(4063234, 4620293),
(4063235, 4620293),
(4063236, 4620293),
(4063237, 4620294),
(917511, 4620294),
(4063238, 4620294),
(917512, 4620294),
(917513, 4620294),
(917514, 4620294);

-- --------------------------------------------------------

--
-- Table structure for table `sample_sop`
--

CREATE TABLE IF NOT EXISTS `sample_sop` (
  `sample_sop_pk_id` bigint(20) NOT NULL,
  `description` varchar(2000) default NULL,
  `sop_name` varchar(200) default NULL,
  PRIMARY KEY  (`sample_sop_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `sample_sop`
--

INSERT INTO `sample_sop` (`sample_sop_pk_id`, `description`, `sop_name`) VALUES
(1, 'sample creation', 'Original'),
(2, 'sample creation', 'Testing'),
(3, 'aliquot creation', 'Lyophilized'),
(4, 'aliquot creation', 'Solubilized');

-- --------------------------------------------------------

--
-- Table structure for table `shape`
--

CREATE TABLE IF NOT EXISTS `shape` (
  `shape_pk_id` bigint(20) NOT NULL,
  `max_dimension` decimal(22,3) default NULL,
  `min_dimension` decimal(22,3) default NULL,
  `type` varchar(100) default NULL,
  `min_dimension_unit` varchar(100) default NULL,
  `max_dimension_unit` varchar(100) default NULL,
  PRIMARY KEY  (`shape_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `shape`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_shape`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_shape` BEFORE DELETE ON `cananolab`.`shape`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       max_dimension,
       min_dimension,
       max_dimension_unit,
       min_dimension_unit,
       type,
       deleted_date, 
       table_source)
    values
     ( old.shape_pk_id,
       old.max_dimension,
       old.min_dimension,
       old.max_dimension_unit,
       old.min_dimension_unit,
       old.type,
       sysdate(),
       'SHAPE');
end
//
DELIMITER ;

--
-- Dumping data for table `shape`
--


-- --------------------------------------------------------

--
-- Table structure for table `solubility`
--

CREATE TABLE IF NOT EXISTS `solubility` (
  `solubility_pk_id` bigint(20) NOT NULL,
  `solvent` varchar(200) default NULL,
  `critical_concentration` decimal(22,3) default NULL,
  `concentration_unit` varchar(100) default NULL,
  `is_soluble` tinyint(4) default NULL,
  PRIMARY KEY  (`solubility_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `solubility`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_solubility`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_solubility` BEFORE DELETE ON `cananolab`.`solubility`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       solvent,
       critical_concentration,
       concentration_unit,
       is_soluble,
       deleted_date, 
       table_source)
    values
     ( old.solubility_pk_id,
       old.solvent,
       old.critical_concentration,
       old.concentration_unit,
       old.is_soluble,
       sysdate(),
       'SOLUBILITY');
end
//
DELIMITER ;

--
-- Dumping data for table `solubility`
--

INSERT INTO `solubility` (`solubility_pk_id`, `solvent`, `critical_concentration`, `concentration_unit`, `is_soluble`) VALUES
(1376278, 'Water', NULL, 'g/ml', 1),
(1867780, 'water', NULL, 'g/ml', 1);

-- --------------------------------------------------------

--
-- Table structure for table `source`
--

CREATE TABLE IF NOT EXISTS `source` (
  `source_pk_id` bigint(20) NOT NULL,
  `organization_name` varchar(200) default NULL,
  `address` varchar(200) default NULL,
  `city` varchar(100) default NULL,
  `state` varchar(100) default NULL,
  `country` varchar(100) default NULL,
  `postal_code` varchar(10) default NULL,
  PRIMARY KEY  (`source_pk_id`),
  UNIQUE KEY `unique_name` (`organization_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `source`
--

INSERT INTO `source` (`source_pk_id`, `organization_name`, `address`, `city`, `state`, `country`, `postal_code`) VALUES
(491520, 'Joe Barchi', NULL, NULL, NULL, NULL, NULL),
(884736, 'DNT', NULL, NULL, NULL, NULL, NULL),
(884737, 'Mark Kester', NULL, NULL, NULL, NULL, NULL),
(884738, 'Mansoor Amiji', NULL, NULL, NULL, NULL, NULL),
(2555904, 'NCL', NULL, NULL, NULL, NULL, NULL),
(4030464, 'C-Sixty (CNI)', NULL, NULL, NULL, NULL, NULL),
(4030465, 'Mark Kester PSU', NULL, NULL, NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `source_contact`
--

CREATE TABLE IF NOT EXISTS `source_contact` (
  `contact_pk_id` bigint(20) NOT NULL,
  `source_pk_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`contact_pk_id`,`source_pk_id`),
  KEY `sys_c00246791` (`source_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `source_contact`
--


-- --------------------------------------------------------

--
-- Table structure for table `stability`
--

CREATE TABLE IF NOT EXISTS `stability` (
  `stability_pk_id` bigint(20) NOT NULL,
  `long_term_storage` decimal(22,3) default NULL,
  `long_term_storage_unit` varchar(100) default NULL,
  `short_term_storage` decimal(22,3) default NULL,
  `short_term_storage_unit` varchar(100) default NULL,
  `stress_result` varchar(500) default NULL,
  `release_kinetics_description` varchar(4000) default NULL,
  `measurement_type` varchar(100) default NULL,
  `stressor_type` varchar(100) default NULL,
  `stressor_desc` varchar(2000) default NULL,
  `stressor_value` decimal(22,3) default NULL,
  `stressor_value_unit` varchar(100) default NULL,
  PRIMARY KEY  (`stability_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stability`
--


-- --------------------------------------------------------

--
-- Table structure for table `storage`
--

CREATE TABLE IF NOT EXISTS `storage` (
  `storage_pk_id` bigint(20) NOT NULL,
  `storage_location` varchar(500) default NULL,
  `storage_type` varchar(200) default NULL,
  PRIMARY KEY  (`storage_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `storage`
--

INSERT INTO `storage` (`storage_pk_id`, `storage_location`, `storage_type`) VALUES
(1, '250', 'Room'),
(2, '117', 'Room'),
(3, 'F1', 'Freezer'),
(4, 'F2', 'Freezer'),
(5, 'F3', 'Freezer'),
(6, 'F4', 'Freezer'),
(688128, '2', 'Box'),
(688129, '1', 'Shelf'),
(688130, '3', 'Box'),
(688131, '5', 'Box'),
(688132, '2', 'Shelf'),
(688133, '4', 'Shelf'),
(688134, '6', 'Box'),
(2654208, '1', 'Box');

-- --------------------------------------------------------

--
-- Table structure for table `surface`
--

CREATE TABLE IF NOT EXISTS `surface` (
  `surface_area` decimal(22,3) default NULL,
  `surface_area_unit` varchar(100) default NULL,
  `zeta_potential` decimal(22,3) default NULL,
  `charge` decimal(22,3) default NULL,
  `charge_unit` varchar(100) default NULL,
  `is_hydrophobic` tinyint(4) default NULL,
  `surface_pk_id` bigint(20) NOT NULL,
  `zeta_potential_unit` varchar(100) default NULL,
  PRIMARY KEY  (`surface_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `surface`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_surface`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_surface` BEFORE DELETE ON `cananolab`.`surface`
 FOR EACH ROW begin     
    insert into history_characterization
     ( characterization_pk_id,
       surface_area,
       surface_area_unit,
       zeta_potential,
	   zeta_potential_unit,
       charge,
       charge_unit,
       is_hydrophobic,
       deleted_date, 
       table_source)
    values
     ( old.surface_pk_id,
       old.surface_area,
       old.surface_area_unit,
       old.zeta_potential,
	   old.zeta_potential_unit,
       old.charge,
       old.charge_unit,
       old.is_hydrophobic,
       sysdate(),
       'SURFACE');
end
//
DELIMITER ;

--
-- Dumping data for table `surface`
--

INSERT INTO `surface` (`surface_area`, `surface_area_unit`, `zeta_potential`, `charge`, `charge_unit`, `is_hydrophobic`, `surface_pk_id`, `zeta_potential_unit`) VALUES
('1.000', NULL, '-53.600', '1.000', 'a.u', 1, 4227090, NULL),
('0.000', NULL, '-8.000', '0.000', 'a.u', 1, 4227097, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `surface_chemistry`
--

CREATE TABLE IF NOT EXISTS `surface_chemistry` (
  `surface_chemistry_pk_id` bigint(20) NOT NULL,
  `molecule_name` varchar(200) default NULL,
  `surface_pk_id` bigint(20) default NULL,
  `number_molecule` int(11) default NULL,
  `list_index` bigint(20) default NULL,
  `molecular_formula_type` varchar(200) default NULL,
  PRIMARY KEY  (`surface_chemistry_pk_id`),
  KEY `sys_c00246803` (`surface_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `surface_chemistry`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_surface_chemistry`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_surface_chemistry` BEFORE UPDATE ON `cananolab`.`surface_chemistry`
 FOR EACH ROW begin
   if new.surface_pk_id is null then
       insert into history_surface_chemistry
        ( surface_chemistry_pk_id, 
          molecule_name, 
          surface_pk_id, 
          number_molecule, 
          list_index, 
		  molecular_formula_type,
          deleted_date )
       values
        (old.surface_chemistry_pk_id, 
         old.molecule_name, 
         old.surface_pk_id, 
         old.number_molecule, 
         old.list_index, 
		 old.molecular_formula_type,
         sysdate() );
   else
      delete from history_surface_chemistry where surface_chemistry_pk_id=old.surface_chemistry_pk_id;
   end if;      
end
//
DELIMITER ;

--
-- Dumping data for table `surface_chemistry`
--


-- --------------------------------------------------------

--
-- Table structure for table `surface_group`
--

CREATE TABLE IF NOT EXISTS `surface_group` (
  `surface_group_pk_id` bigint(20) NOT NULL,
  `name` varchar(100) default NULL,
  `modifier` varchar(100) default NULL,
  `d_composition_pk_id` bigint(20) default NULL,
  `list_index` bigint(20) default NULL,
  PRIMARY KEY  (`surface_group_pk_id`),
  KEY `sys_c00246804` (`d_composition_pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `surface_group`
--
DROP TRIGGER IF EXISTS `cananolab`.`set_history_surface_group`;
DELIMITER //
CREATE TRIGGER `cananolab`.`set_history_surface_group` BEFORE UPDATE ON `cananolab`.`surface_group`
 FOR EACH ROW begin
   if new.d_composition_pk_id is null then
       insert into history_surface_group
        ( surface_group_pk_id, 
          name, 
          modifier,
          d_composition_pk_id, 
          list_index, 
          deleted_date )
       values
        (old.surface_group_pk_id, 
         old.name, 
         old.modifier, 
         old.d_composition_pk_id, 
         old.list_index, 
         sysdate() );
   else 
      delete from history_surface_group where surface_group_pk_id=old.surface_group_pk_id;
   end if;
end
//
DELIMITER ;

--
-- Dumping data for table `surface_group`
--

INSERT INTO `surface_group` (`surface_group_pk_id`, `name`, `modifier`, `d_composition_pk_id`, `list_index`) VALUES
(1441792, 'Hydroxyl', NULL, 1376256, 0),
(1441793, 'Pyrrolidone', NULL, 1376263, 0),
(1441794, 'Carboxyl', NULL, 1376268, 0),
(1441795, 'Pyrrolidone', NULL, 1376281, 0),
(1441796, 'Magnevist', NULL, 1376281, 1),
(1441797, 'Tris', NULL, 1376283, 0),
(1441798, 'Magnevist', NULL, 1376283, 1),
(1441799, 'Carboxyl', 'Magnevist', 1376285, 0),
(2097152, 'Magnevist', NULL, 1867783, 0);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_sample_sop_file`
--
CREATE TABLE IF NOT EXISTS `view_sample_sop_file` (
`sop_file_pk_id` bigint(20)
,`file_name` varchar(500)
,`file_type_extension` varchar(100)
,`status` varchar(20)
,`reason` varchar(2000)
,`file_uri` varchar(500)
,`version` varchar(200)
,`created_by` varchar(200)
,`created_date` datetime
,`sample_sop_pk_id` bigint(20)
);
-- --------------------------------------------------------

--
-- Structure for view `view_sample_sop_file`
--
DROP TABLE IF EXISTS `view_sample_sop_file`;

CREATE ALGORITHM=UNDEFINED DEFINER=`ca`@`%` SQL SECURITY DEFINER VIEW `cananolab`.`view_sample_sop_file` AS select `cananolab`.`lab_file`.`file_pk_id` AS `sop_file_pk_id`,`cananolab`.`lab_file`.`file_name` AS `file_name`,`cananolab`.`lab_file`.`file_type_extension` AS `file_type_extension`,`cananolab`.`lab_file`.`status` AS `status`,`cananolab`.`lab_file`.`reason` AS `reason`,`cananolab`.`lab_file`.`file_uri` AS `file_uri`,`cananolab`.`lab_file`.`version` AS `version`,`cananolab`.`lab_file`.`created_by` AS `created_by`,`cananolab`.`lab_file`.`created_date` AS `created_date`,`cananolab`.`lab_file`.`sample_sop_pk_id` AS `sample_sop_pk_id` from `cananolab`.`lab_file` where (`cananolab`.`lab_file`.`file_source_type` = _latin1'SOP');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agent_target`
--
ALTER TABLE `agent_target`
  ADD CONSTRAINT `fk_agent_target_agent` FOREIGN KEY (`agent_pk_id`) REFERENCES `agent` (`agent_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `associated_file`
--
ALTER TABLE `associated_file`
  ADD CONSTRAINT `sys_c00246813` FOREIGN KEY (`associated_file_pk_id`) REFERENCES `lab_file` (`file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `bioassay_data_data_category`
--
ALTER TABLE `bioassay_data_data_category`
  ADD CONSTRAINT `fk_data_data_category` FOREIGN KEY (`derived_bioassay_data_pk_id`) REFERENCES `derived_bioassay_data` (`derived_bioassay_data_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `carbon_nanotube_composition`
--
ALTER TABLE `carbon_nanotube_composition`
  ADD CONSTRAINT `sys_c00246826` FOREIGN KEY (`cn_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `characterization`
--
ALTER TABLE `characterization`
  ADD CONSTRAINT `sys_c00246819` FOREIGN KEY (`instrument_config_pk_id`) REFERENCES `instrument_config` (`instrument_config_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246820` FOREIGN KEY (`protocol_file_pk_id`) REFERENCES `protocol_file` (`protocol_file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `composing_element`
--
ALTER TABLE `composing_element`
  ADD CONSTRAINT `sys_c00246831` FOREIGN KEY (`characterization_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `container_storage_location`
--
ALTER TABLE `container_storage_location`
  ADD CONSTRAINT `sys_c00246773` FOREIGN KEY (`sample_container_pk_id`) REFERENCES `sample_container` (`sample_container_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246774` FOREIGN KEY (`storage_pk_id`) REFERENCES `storage` (`storage_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `csm_group`
--
ALTER TABLE `csm_group`
  ADD CONSTRAINT `fk_application_group` FOREIGN KEY (`application_id`) REFERENCES `csm_application` (`application_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_pg_pe`
--
ALTER TABLE `csm_pg_pe`
  ADD CONSTRAINT `fk_protection_element_protection_group` FOREIGN KEY (`protection_element_id`) REFERENCES `csm_protection_element` (`protection_element_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_protection_group_protection_element` FOREIGN KEY (`protection_group_id`) REFERENCES `csm_protection_group` (`protection_group_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_protection_element`
--
ALTER TABLE `csm_protection_element`
  ADD CONSTRAINT `fk_pe_application` FOREIGN KEY (`application_id`) REFERENCES `csm_application` (`application_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_protection_group`
--
ALTER TABLE `csm_protection_group`
  ADD CONSTRAINT `fk_pg_application` FOREIGN KEY (`application_id`) REFERENCES `csm_application` (`application_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_protection_group` FOREIGN KEY (`parent_protection_group_id`) REFERENCES `csm_protection_group` (`protection_group_id`);

--
-- Constraints for table `csm_role`
--
ALTER TABLE `csm_role`
  ADD CONSTRAINT `fk_application_role` FOREIGN KEY (`application_id`) REFERENCES `csm_application` (`application_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_role_privilege`
--
ALTER TABLE `csm_role_privilege`
  ADD CONSTRAINT `fk_privilege_role` FOREIGN KEY (`privilege_id`) REFERENCES `csm_privilege` (`privilege_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `csm_role` (`role_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_user_group`
--
ALTER TABLE `csm_user_group`
  ADD CONSTRAINT `fk_ug_group` FOREIGN KEY (`group_id`) REFERENCES `csm_group` (`group_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_group` FOREIGN KEY (`user_id`) REFERENCES `csm_user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_user_group_role_pg`
--
ALTER TABLE `csm_user_group_role_pg`
  ADD CONSTRAINT `fk_user_group_role_protection_group_groups` FOREIGN KEY (`group_id`) REFERENCES `csm_group` (`group_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_group_role_protection_group_protection_group` FOREIGN KEY (`protection_group_id`) REFERENCES `csm_protection_group` (`protection_group_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_group_role_protection_group_role` FOREIGN KEY (`role_id`) REFERENCES `csm_role` (`role_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_group_role_protection_group_user` FOREIGN KEY (`user_id`) REFERENCES `csm_user` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `csm_user_pe`
--
ALTER TABLE `csm_user_pe`
  ADD CONSTRAINT `fk_pe_user` FOREIGN KEY (`user_id`) REFERENCES `csm_user` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_protection_element_user` FOREIGN KEY (`protection_element_id`) REFERENCES `csm_protection_element` (`protection_element_id`) ON DELETE CASCADE;

--
-- Constraints for table `cytotoxicity`
--
ALTER TABLE `cytotoxicity`
  ADD CONSTRAINT `sys_c00246824` FOREIGN KEY (`cytotoxicity_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `datum`
--
ALTER TABLE `datum`
  ADD CONSTRAINT `sys_c00246821` FOREIGN KEY (`derived_bioassay_data_pk_id`) REFERENCES `derived_bioassay_data` (`derived_bioassay_data_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `datum_condition`
--
ALTER TABLE `datum_condition`
  ADD CONSTRAINT `sys_c00246822` FOREIGN KEY (`datum_pk_id`) REFERENCES `datum` (`datum_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `dendrimer_composition`
--
ALTER TABLE `dendrimer_composition`
  ADD CONSTRAINT `sys_c00246827` FOREIGN KEY (`d_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `derived_bioassay_data`
--
ALTER TABLE `derived_bioassay_data`
  ADD CONSTRAINT `sys_c00246817` FOREIGN KEY (`derived_bioassay_data_pk_id`) REFERENCES `lab_file` (`file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246818` FOREIGN KEY (`characterization_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `derived_sample_container`
--
ALTER TABLE `derived_sample_container`
  ADD CONSTRAINT `sys_c00246775` FOREIGN KEY (`sample_container_pk_id`) REFERENCES `sample_container` (`sample_container_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246776` FOREIGN KEY (`parent_container_id`) REFERENCES `sample_container` (`sample_container_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `emulsion_composition`
--
ALTER TABLE `emulsion_composition`
  ADD CONSTRAINT `sys_c00246828` FOREIGN KEY (`e_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `fullerene_composition`
--
ALTER TABLE `fullerene_composition`
  ADD CONSTRAINT `sys_c00246829` FOREIGN KEY (`f_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `instrument_config`
--
ALTER TABLE `instrument_config`
  ADD CONSTRAINT `con_config_instrument` FOREIGN KEY (`instrument_pk_id`) REFERENCES `instrument` (`instrument_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `keyword_bioassay_data`
--
ALTER TABLE `keyword_bioassay_data`
  ADD CONSTRAINT `sys_c00246815` FOREIGN KEY (`derived_bioassay_data_pk_id`) REFERENCES `derived_bioassay_data` (`derived_bioassay_data_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246816` FOREIGN KEY (`keyword_pk_id`) REFERENCES `keyword` (`keyword_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `keyword_nanoparticle`
--
ALTER TABLE `keyword_nanoparticle`
  ADD CONSTRAINT `sys_c00246793` FOREIGN KEY (`nanoparticle_pk_id`) REFERENCES `nanoparticle` (`nanoparticle_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246794` FOREIGN KEY (`keyword_pk_id`) REFERENCES `keyword` (`keyword_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `lab_file`
--
ALTER TABLE `lab_file`
  ADD CONSTRAINT `sys_c00246777` FOREIGN KEY (`run_pk_id`) REFERENCES `run` (`run_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246778` FOREIGN KEY (`sample_sop_pk_id`) REFERENCES `sample_sop` (`sample_sop_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246779` FOREIGN KEY (`data_status_pk_id`) REFERENCES `data_status` (`data_status_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `linkage`
--
ALTER TABLE `linkage`
  ADD CONSTRAINT `fk_linkage_function` FOREIGN KEY (`function_pk_id`) REFERENCES `particle_function` (`particle_function_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `liposome_composition`
--
ALTER TABLE `liposome_composition`
  ADD CONSTRAINT `sys_c00246830` FOREIGN KEY (`l_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `morphology`
--
ALTER TABLE `morphology`
  ADD CONSTRAINT `sys_c00246795` FOREIGN KEY (`morphology_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `nanoparticle`
--
ALTER TABLE `nanoparticle`
  ADD CONSTRAINT `sys_c00246796` FOREIGN KEY (`nanoparticle_pk_id`) REFERENCES `sample` (`sample_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `nanoparticle_char`
--
ALTER TABLE `nanoparticle_char`
  ADD CONSTRAINT `sys_c00246797` FOREIGN KEY (`nanoparticle_pk_id`) REFERENCES `nanoparticle` (`nanoparticle_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246805` FOREIGN KEY (`characterization_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `particle_function`
--
ALTER TABLE `particle_function`
  ADD CONSTRAINT `sys_c00246798` FOREIGN KEY (`nanoparticle_pk_id`) REFERENCES `nanoparticle` (`nanoparticle_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `polymer_composition`
--
ALTER TABLE `polymer_composition`
  ADD CONSTRAINT `sys_c00246825` FOREIGN KEY (`p_composition_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `protocol_file`
--
ALTER TABLE `protocol_file`
  ADD CONSTRAINT `con_prot_prot_file` FOREIGN KEY (`protocol_pk_id`) REFERENCES `protocol` (`protocol_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246823` FOREIGN KEY (`protocol_file_pk_id`) REFERENCES `lab_file` (`file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `sys_c00246814` FOREIGN KEY (`report_pk_id`) REFERENCES `lab_file` (`file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `sample`
--
ALTER TABLE `sample`
  ADD CONSTRAINT `sys_c00246786` FOREIGN KEY (`sample_sop_pk_id`) REFERENCES `sample_sop` (`sample_sop_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246787` FOREIGN KEY (`source_pk_id`) REFERENCES `source` (`source_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `sample_associated_file`
--
ALTER TABLE `sample_associated_file`
  ADD CONSTRAINT `sys_c00246806` FOREIGN KEY (`sample_pk_id`) REFERENCES `sample` (`sample_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246808` FOREIGN KEY (`associated_file_pk_id`) REFERENCES `associated_file` (`associated_file_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `sample_container`
--
ALTER TABLE `sample_container`
  ADD CONSTRAINT `sys_c00246788` FOREIGN KEY (`storage_pk_id`) REFERENCES `storage` (`storage_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246789` FOREIGN KEY (`sample_pk_id`) REFERENCES `sample` (`sample_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246790` FOREIGN KEY (`data_status_pk_id`) REFERENCES `data_status` (`data_status_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `sample_report`
--
ALTER TABLE `sample_report`
  ADD CONSTRAINT `sys_c00246811` FOREIGN KEY (`file_pk_id`) REFERENCES `report` (`report_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246812` FOREIGN KEY (`sample_pk_id`) REFERENCES `sample` (`sample_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `shape`
--
ALTER TABLE `shape`
  ADD CONSTRAINT `sys_c00246799` FOREIGN KEY (`shape_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `solubility`
--
ALTER TABLE `solubility`
  ADD CONSTRAINT `sys_c00246800` FOREIGN KEY (`solubility_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `source_contact`
--
ALTER TABLE `source_contact`
  ADD CONSTRAINT `sys_c00246791` FOREIGN KEY (`source_pk_id`) REFERENCES `source` (`source_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `sys_c00246792` FOREIGN KEY (`contact_pk_id`) REFERENCES `contact` (`contact_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `stability`
--
ALTER TABLE `stability`
  ADD CONSTRAINT `sys_c00246801` FOREIGN KEY (`stability_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `surface`
--
ALTER TABLE `surface`
  ADD CONSTRAINT `sys_c00246802` FOREIGN KEY (`surface_pk_id`) REFERENCES `characterization` (`characterization_pk_id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `surface_chemistry`
--
ALTER TABLE `surface_chemistry`
  ADD CONSTRAINT `sys_c00246803` FOREIGN KEY (`surface_pk_id`) REFERENCES `surface` (`surface_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `surface_group`
--
ALTER TABLE `surface_group`
  ADD CONSTRAINT `sys_c00246804` FOREIGN KEY (`d_composition_pk_id`) REFERENCES `dendrimer_composition` (`d_composition_pk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
