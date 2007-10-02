-- ----------------------------------------------------------------------
-- MySQL Migration Toolkit
-- SQL Create Script
-- ----------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS cananolab
  CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE cananolab;

-- -------------------------------------
-- Tables

DROP TABLE IF EXISTS agent;
CREATE TABLE agent (
  agent_pk_id DECIMAL(22, 0) NOT NULL,
  discriminator VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  name VARCHAR(200) BINARY NULL,
  other VARCHAR(200) BINARY NULL,
  sequence VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (agent_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS agent_target;
CREATE TABLE agent_target (
  agent_target_pk_id DECIMAL(22, 0) NOT NULL,
  discriminator VARCHAR(200) BINARY NULL,
  name VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  list_index DECIMAL(22, 0) NULL,
  agent_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (agent_target_pk_id),
  CONSTRAINT fk_agent_target_agent FOREIGN KEY fk_agent_target_agent (agent_pk_id)
    REFERENCES agent (agent_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS assay;
CREATE TABLE assay (
  assay_pk_id DECIMAL(22, 0) NOT NULL,
  assay_name VARCHAR(200) BINARY NULL,
  description VARCHAR(4000) BINARY NULL,
  assay_type VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  created_by VARCHAR(200) BINARY NULL,
  protocol_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (assay_pk_id),
  CONSTRAINT sys_c00246772 FOREIGN KEY sys_c00246772 (protocol_pk_id)
    REFERENCES protocol (protocol_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS associated_file;
CREATE TABLE associated_file (
  associated_file_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (associated_file_pk_id),
  CONSTRAINT sys_c00246813 FOREIGN KEY sys_c00246813 (associated_file_pk_id)
    REFERENCES lab_file (file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS bioassay_data_data_category;
CREATE TABLE bioassay_data_data_category (
  derived_bioassay_data_pk_id DECIMAL(22, 0) NOT NULL,
  category_index DECIMAL(22, 0) NOT NULL,
  category_name VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (derived_bioassay_data_pk_id, category_index),
  CONSTRAINT fk_data_data_category FOREIGN KEY fk_data_data_category (derived_bioassay_data_pk_id)
    REFERENCES derived_bioassay_data (derived_bioassay_data_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS carbon_nanotube_composition;
CREATE TABLE carbon_nanotube_composition (
  chirality VARCHAR(100) BINARY NULL,
  growth_diameter DECIMAL(22, 0) NULL,
  average_length DECIMAL(22, 0) NULL,
  wall_type VARCHAR(100) BINARY NULL,
  cn_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (cn_composition_pk_id),
  CONSTRAINT sys_c00246826 FOREIGN KEY sys_c00246826 (cn_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS characterization;
CREATE TABLE characterization (
  characterization_pk_id DECIMAL(22, 0) NOT NULL,
  classification VARCHAR(200) BINARY NULL,
  source VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  identifier_name VARCHAR(500) BINARY NULL,
  name VARCHAR(100) BINARY NULL,
  discriminator VARCHAR(50) BINARY NULL,
  created_date DATETIME NULL,
  created_by VARCHAR(200) BINARY NULL,
  protocol_file_pk_id DECIMAL(22, 0) NULL,
  instrument_config_pk_id DECIMAL(22, 0) NULL,
  instrument_pk_id DECIMAL(22, 0) NULL,
  char_protocol_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (characterization_pk_id),
  CONSTRAINT sys_c00246819 FOREIGN KEY sys_c00246819 (instrument_config_pk_id)
    REFERENCES instrument_config (instrument_config_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246820 FOREIGN KEY sys_c00246820 (protocol_file_pk_id)
    REFERENCES protocol_file (protocol_file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS composing_element;
CREATE TABLE composing_element (
  composing_element_pk_id DECIMAL(22, 0) NOT NULL,
  element_type VARCHAR(100) BINARY NULL,
  chemical_name VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  characterization_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  PRIMARY KEY (composing_element_pk_id),
  CONSTRAINT sys_c00246831 FOREIGN KEY sys_c00246831 (characterization_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
  contact_pk_id DECIMAL(38, 0) NOT NULL,
  first_name VARCHAR(100) BINARY NOT NULL,
  last_name VARCHAR(100) BINARY NOT NULL,
  title VARCHAR(100) BINARY NULL,
  phone_number VARCHAR(15) BINARY NULL,
  email VARCHAR(100) BINARY NULL,
  update_date DATETIME NOT NULL,
  middle_name VARCHAR(100) BINARY NULL,
  fax VARCHAR(20) BINARY NULL,
  address VARCHAR(200) BINARY NULL,
  city VARCHAR(100) BINARY NULL,
  state VARCHAR(100) BINARY NULL,
  country VARCHAR(100) BINARY NULL,
  postal_code VARCHAR(10) BINARY NULL,
  pi_name VARCHAR(200) BINARY NULL,
  PRIMARY KEY (contact_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS container_storage_location;
CREATE TABLE container_storage_location (
  sample_container_pk_id DECIMAL(22, 0) NOT NULL,
  storage_pk_id DECIMAL(22, 0) NOT NULL,
  CONSTRAINT sys_c00246773 FOREIGN KEY sys_c00246773 (sample_container_pk_id)
    REFERENCES sample_container (sample_container_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246774 FOREIGN KEY sys_c00246774 (storage_pk_id)
    REFERENCES storage (storage_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS cytotoxicity;
CREATE TABLE cytotoxicity (
  cytotoxicity_pk_id DECIMAL(22, 0) NOT NULL,
  cell_line VARCHAR(200) BINARY NULL,
  cell_death_method VARCHAR(200) BINARY NULL,
  PRIMARY KEY (cytotoxicity_pk_id),
  CONSTRAINT sys_c00246824 FOREIGN KEY sys_c00246824 (cytotoxicity_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS data_status;
CREATE TABLE data_status (
  data_status_pk_id DECIMAL(22, 0) NOT NULL,
  status VARCHAR(20) BINARY NULL,
  reason VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (data_status_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS datum;
CREATE TABLE datum (
  datum_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL,
  value DECIMAL(22, 0) NOT NULL,
  value_unit VARCHAR(200) BINARY NULL,
  derived_bioassay_data_pk_id DECIMAL(22, 0) NULL,
  control_name VARCHAR(200) BINARY NULL,
  control_type VARCHAR(100) BINARY NULL,
  list_index DECIMAL(22, 0) NULL,
  statistics_type VARCHAR(200) BINARY NULL,
  bioassay_data_category VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (datum_pk_id),
  CONSTRAINT sys_c00246821 FOREIGN KEY sys_c00246821 (derived_bioassay_data_pk_id)
    REFERENCES derived_bioassay_data (derived_bioassay_data_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS datum_condition;
CREATE TABLE datum_condition (
  datum_condition_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(100) BINARY NOT NULL,
  value DECIMAL(22, 0) NOT NULL,
  value_unit VARCHAR(200) BINARY NOT NULL,
  datum_pk_id DECIMAL(22, 0) NOT NULL,
  list_index DECIMAL(22, 0) NULL,
  statistics_type VARCHAR(200) BINARY NULL,
  PRIMARY KEY (datum_condition_pk_id),
  CONSTRAINT sys_c00246822 FOREIGN KEY sys_c00246822 (datum_pk_id)
    REFERENCES datum (datum_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_activation_method;
CREATE TABLE def_activation_method (
  activation_method_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (activation_method_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_assay_type;
CREATE TABLE def_assay_type (
  assay_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  description VARCHAR(4000) BINARY NULL,
  execute_order VARCHAR(10) BINARY NULL,
  PRIMARY KEY (assay_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_bioassay_data_category;
CREATE TABLE def_bioassay_data_category (
  category_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL,
  characterization_name VARCHAR(2000) BINARY NOT NULL,
  PRIMARY KEY (category_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_bond_type;
CREATE TABLE def_bond_type (
  bond_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (bond_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_cellline_type;
CREATE TABLE def_cellline_type (
  cellline_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (cellline_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_characterization_category;
CREATE TABLE def_characterization_category (
  char_category_pk_id DECIMAL(22, 0) NOT NULL,
  category VARCHAR(200) BINARY NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  has_action DECIMAL(22, 0) NOT NULL,
  category_order DECIMAL(22, 0) NOT NULL,
  indent_level DECIMAL(22, 0) NOT NULL,
  name_abbreviation VARCHAR(200) BINARY NULL,
  PRIMARY KEY (char_category_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_characterization_file_type;
CREATE TABLE def_characterization_file_type (
  file_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL,
  PRIMARY KEY (file_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_datum_name;
CREATE TABLE def_datum_name (
  datum_name_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL,
  is_datum_parsed INT(2) NOT NULL DEFAULT 1,
  characterization_name VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (datum_name_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_function_agent_target_type;
CREATE TABLE def_function_agent_target_type (
  agent_target_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_function_agent_type;
CREATE TABLE def_function_agent_type (
  agent_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_function_linkage_type;
CREATE TABLE def_function_linkage_type (
  linkage_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_function_type;
CREATE TABLE def_function_type (
  function_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (function_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_image_contrast_agent_type;
CREATE TABLE def_image_contrast_agent_type (
  agent_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (agent_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_measure_type;
CREATE TABLE def_measure_type (
  measure_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (measure_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_measure_unit;
CREATE TABLE def_measure_unit (
  measure_unit_pk_id DECIMAL(22, 0) NOT NULL,
  unit_name VARCHAR(50) BINARY NOT NULL,
  description VARCHAR(1000) BINARY NULL,
  unit_type VARCHAR(100) BINARY NOT NULL,
  PRIMARY KEY (measure_unit_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_molecular_formula_type;
CREATE TABLE def_molecular_formula_type (
  molecular_formula_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (molecular_formula_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_morphology_type;
CREATE TABLE def_morphology_type (
  morphology_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (morphology_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_protocol_type;
CREATE TABLE def_protocol_type (
  protocol_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_sample_type;
CREATE TABLE def_sample_type (
  sample_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_shape_type;
CREATE TABLE def_shape_type (
  shape_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (shape_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_solvent_type;
CREATE TABLE def_solvent_type (
  solvent_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (solvent_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_species_name;
CREATE TABLE def_species_name (
  species_name_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (species_name_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_storage_type;
CREATE TABLE def_storage_type (
  storage_type_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (storage_type_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_surface_group_type;
CREATE TABLE def_surface_group_type (
  surface_group_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (surface_group_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS def_wall_type;
CREATE TABLE def_wall_type (
  wall_type_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(200) BINARY NOT NULL,
  PRIMARY KEY (wall_type_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS dendrimer_composition;
CREATE TABLE dendrimer_composition (
  generation DECIMAL(22, 0) NULL,
  molecular_formula VARCHAR(200) BINARY NULL,
  repeat_unit VARCHAR(100) BINARY NULL,
  branch VARCHAR(200) BINARY NULL,
  d_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (d_composition_pk_id),
  CONSTRAINT sys_c00246827 FOREIGN KEY sys_c00246827 (d_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS derived_bioassay_data;
CREATE TABLE derived_bioassay_data (
  derived_bioassay_data_pk_id DECIMAL(22, 0) NOT NULL,
  characterization_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  PRIMARY KEY (derived_bioassay_data_pk_id),
  CONSTRAINT sys_c00246817 FOREIGN KEY sys_c00246817 (derived_bioassay_data_pk_id)
    REFERENCES lab_file (file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246818 FOREIGN KEY sys_c00246818 (characterization_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS derived_sample_container;
CREATE TABLE derived_sample_container (
  parent_container_id DECIMAL(22, 0) NOT NULL,
  sample_container_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (parent_container_id, sample_container_pk_id),
  CONSTRAINT sys_c00246775 FOREIGN KEY sys_c00246775 (sample_container_pk_id)
    REFERENCES sample_container (sample_container_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246776 FOREIGN KEY sys_c00246776 (parent_container_id)
    REFERENCES sample_container (sample_container_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS emulsion_composition;
CREATE TABLE emulsion_composition (
  emulsion_type VARCHAR(200) BINARY NULL,
  molecular_formula VARCHAR(200) BINARY NULL,
  polymer_name VARCHAR(200) BINARY NULL,
  is_polymerized DECIMAL(22, 0) NULL,
  e_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (e_composition_pk_id),
  CONSTRAINT sys_c00246828 FOREIGN KEY sys_c00246828 (e_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS fullerene_composition;
CREATE TABLE fullerene_composition (
  number_of_carbon VARCHAR(200) BINARY NULL,
  f_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (f_composition_pk_id),
  CONSTRAINT sys_c00246829 FOREIGN KEY sys_c00246829 (f_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS hibernate_unique_key;
CREATE TABLE hibernate_unique_key (
  next_hi DECIMAL(22, 0) NOT NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_characterization;
CREATE TABLE history_characterization (
  characterization_pk_id DECIMAL(22, 0) NULL,
  classification VARCHAR(200) BINARY NULL,
  source VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  identifier_name VARCHAR(500) BINARY NULL,
  name VARCHAR(100) BINARY NULL,
  discriminator VARCHAR(50) BINARY NULL,
  created_date DATETIME NULL,
  created_by VARCHAR(200) BINARY NULL,
  protocol_file_pk_id DECIMAL(22, 0) NULL,
  instrument_config_pk_id DECIMAL(22, 0) NULL,
  deleted_date DATETIME NULL,
  chirality VARCHAR(100) BINARY NULL,
  growth_diameter DECIMAL(22, 0) NULL,
  average_length DECIMAL(22, 0) NULL,
  wall_type VARCHAR(100) BINARY NULL,
  generation DECIMAL(22, 0) NULL,
  molecular_formula VARCHAR(200) BINARY NULL,
  repeat_unit VARCHAR(100) BINARY NULL,
  branch VARCHAR(200) BINARY NULL,
  emulsion_type VARCHAR(200) BINARY NULL,
  polymer_name VARCHAR(200) BINARY NULL,
  is_polymerized DECIMAL(22, 0) NULL,
  table_source VARCHAR(500) BINARY NULL,
  cell_line VARCHAR(200) BINARY NULL,
  cell_death_method VARCHAR(200) BINARY NULL,
  number_of_carbon VARCHAR(200) BINARY NULL,
  type VARCHAR(100) BINARY NULL,
  is_cross_link DECIMAL(22, 0) NULL,
  cross_link_degree DECIMAL(22, 0) NULL,
  initiator VARCHAR(200) BINARY NULL,
  max_dimension DECIMAL(22, 0) NULL,
  min_dimension DECIMAL(22, 0) NULL,
  max_dimension_unit VARCHAR(100) BINARY NULL,
  min_dimension_unit VARCHAR(100) BINARY NULL,
  solvent VARCHAR(200) BINARY NULL,
  critical_concentration DECIMAL(22, 0) NULL,
  concentration_unit VARCHAR(100) BINARY NULL,
  is_soluble DECIMAL(22, 0) NULL,
  surface_area DECIMAL(22, 0) NULL,
  surface_area_unit VARCHAR(100) BINARY NULL,
  zeta_potential DECIMAL(22, 0) NULL,
  zeta_potential_unit VARCHAR(100) BINARY NULL,
  charge DECIMAL(22, 0) NULL,
  charge_unit VARCHAR(100) BINARY NULL,
  is_hydrophobic DECIMAL(22, 0) NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_composing_element;
CREATE TABLE history_composing_element (
  composing_element_pk_id DECIMAL(22, 0) NOT NULL,
  element_type VARCHAR(100) BINARY NULL,
  chemical_name VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  characterization_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_datum;
CREATE TABLE history_datum (
  datum_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(2000) BINARY NOT NULL,
  value DECIMAL(22, 0) NOT NULL,
  value_unit VARCHAR(200) BINARY NULL,
  derived_bioassay_data_pk_id DECIMAL(22, 0) NULL,
  control_name VARCHAR(200) BINARY NULL,
  control_type VARCHAR(100) BINARY NULL,
  list_index DECIMAL(22, 0) NULL,
  statistics_type VARCHAR(200) BINARY NULL,
  bioassay_data_category VARCHAR(2000) BINARY NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_derived_bioassay_data;
CREATE TABLE history_derived_bioassay_data (
  derived_bioassay_data_pk_id DECIMAL(22, 0) NOT NULL,
  characterization_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_lab_file;
CREATE TABLE history_lab_file (
  file_pk_id DECIMAL(22, 0) NOT NULL,
  file_name VARCHAR(500) BINARY NULL,
  file_uri VARCHAR(500) BINARY NULL,
  file_type_extension VARCHAR(100) BINARY NULL,
  file_source_type VARCHAR(100) BINARY NULL,
  version VARCHAR(200) BINARY NULL,
  status VARCHAR(20) BINARY NULL,
  reason VARCHAR(2000) BINARY NULL,
  created_by VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  sample_sop_pk_id DECIMAL(22, 0) NULL,
  run_pk_id DECIMAL(22, 0) NULL,
  data_status_pk_id DECIMAL(22, 0) NULL,
  title VARCHAR(500) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  comments VARCHAR(2000) BINARY NULL,
  type VARCHAR(200) BINARY NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_nanoparticle_char;
CREATE TABLE history_nanoparticle_char (
  characterization_pk_id DECIMAL(22, 0) NOT NULL,
  nanoparticle_pk_id DECIMAL(22, 0) NOT NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_surface_chemistry;
CREATE TABLE history_surface_chemistry (
  surface_chemistry_pk_id DECIMAL(22, 0) NULL,
  molecule_name VARCHAR(200) BINARY NULL,
  surface_pk_id DECIMAL(22, 0) NULL,
  number_molecule DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  molecular_formula_type VARCHAR(200) BINARY NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS history_surface_group;
CREATE TABLE history_surface_group (
  surface_group_pk_id DECIMAL(22, 0) NULL,
  name VARCHAR(100) BINARY NULL,
  modifier VARCHAR(100) BINARY NULL,
  d_composition_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  deleted_date DATETIME NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS instrument;
CREATE TABLE instrument (
  instrument_pk_id DECIMAL(22, 0) NOT NULL,
  type VARCHAR(200) BINARY NULL,
  abbreviation VARCHAR(50) BINARY NULL,
  manufacturer VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (instrument_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS instrument_config;
CREATE TABLE instrument_config (
  instrument_config_pk_id DECIMAL(22, 0) NOT NULL,
  description VARCHAR(4000) BINARY NULL,
  instrument_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (instrument_config_pk_id),
  CONSTRAINT con_config_instrument FOREIGN KEY con_config_instrument (instrument_pk_id)
    REFERENCES instrument (instrument_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS keyword;
CREATE TABLE keyword (
  keyword_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(100) BINARY NULL,
  PRIMARY KEY (keyword_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS keyword_bioassay_data;
CREATE TABLE keyword_bioassay_data (
  keyword_pk_id DECIMAL(22, 0) NOT NULL,
  derived_bioassay_data_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (keyword_pk_id, derived_bioassay_data_pk_id),
  CONSTRAINT sys_c00246815 FOREIGN KEY sys_c00246815 (derived_bioassay_data_pk_id)
    REFERENCES derived_bioassay_data (derived_bioassay_data_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246816 FOREIGN KEY sys_c00246816 (keyword_pk_id)
    REFERENCES keyword (keyword_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS keyword_nanoparticle;
CREATE TABLE keyword_nanoparticle (
  keyword_pk_id DECIMAL(22, 0) NOT NULL,
  nanoparticle_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (keyword_pk_id, nanoparticle_pk_id),
  CONSTRAINT sys_c00246793 FOREIGN KEY sys_c00246793 (nanoparticle_pk_id)
    REFERENCES nanoparticle (nanoparticle_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246794 FOREIGN KEY sys_c00246794 (keyword_pk_id)
    REFERENCES keyword (keyword_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS lab_file;
CREATE TABLE lab_file (
  file_pk_id DECIMAL(22, 0) NOT NULL,
  file_name VARCHAR(500) BINARY NULL,
  file_uri VARCHAR(500) BINARY NULL,
  file_type_extension VARCHAR(100) BINARY NULL,
  file_source_type VARCHAR(100) BINARY NULL,
  version VARCHAR(200) BINARY NULL,
  status VARCHAR(20) BINARY NULL,
  reason VARCHAR(2000) BINARY NULL,
  created_by VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  sample_sop_pk_id DECIMAL(22, 0) NULL,
  run_pk_id DECIMAL(22, 0) NULL,
  data_status_pk_id DECIMAL(22, 0) NULL,
  title VARCHAR(500) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  comments VARCHAR(2000) BINARY NULL,
  type VARCHAR(200) BINARY NULL,
  PRIMARY KEY (file_pk_id),
  CONSTRAINT sys_c00246777 FOREIGN KEY sys_c00246777 (run_pk_id)
    REFERENCES run (run_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246778 FOREIGN KEY sys_c00246778 (sample_sop_pk_id)
    REFERENCES sample_sop (sample_sop_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246779 FOREIGN KEY sys_c00246779 (data_status_pk_id)
    REFERENCES data_status (data_status_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS linkage;
CREATE TABLE linkage (
  linkage_pk_id DECIMAL(22, 0) NOT NULL,
  description VARCHAR(2000) BINARY NULL,
  discriminator VARCHAR(200) BINARY NULL,
  bond_type VARCHAR(200) BINARY NULL,
  localization VARCHAR(200) BINARY NULL,
  function_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  PRIMARY KEY (linkage_pk_id),
  CONSTRAINT fk_linkage_function FOREIGN KEY fk_linkage_function (function_pk_id)
    REFERENCES particle_function (particle_function_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS liposome_composition;
CREATE TABLE liposome_composition (
  is_polymerized DECIMAL(22, 0) NULL,
  polymer_name VARCHAR(200) BINARY NULL,
  l_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (l_composition_pk_id),
  CONSTRAINT sys_c00246830 FOREIGN KEY sys_c00246830 (l_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS morphology;
CREATE TABLE morphology (
  morphology_pk_id DECIMAL(22, 0) NOT NULL,
  type VARCHAR(100) BINARY NULL,
  PRIMARY KEY (morphology_pk_id),
  CONSTRAINT sys_c00246795 FOREIGN KEY sys_c00246795 (morphology_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS nanoparticle;
CREATE TABLE nanoparticle (
  nanoparticle_pk_id DECIMAL(22, 0) NOT NULL,
  classification VARCHAR(200) BINARY NULL,
  PRIMARY KEY (nanoparticle_pk_id),
  CONSTRAINT sys_c00246796 FOREIGN KEY sys_c00246796 (nanoparticle_pk_id)
    REFERENCES sample (sample_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS nanoparticle_char;
CREATE TABLE nanoparticle_char (
  characterization_pk_id DECIMAL(22, 0) NOT NULL,
  nanoparticle_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (characterization_pk_id, nanoparticle_pk_id),
  CONSTRAINT sys_c00246797 FOREIGN KEY sys_c00246797 (nanoparticle_pk_id)
    REFERENCES nanoparticle (nanoparticle_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246805 FOREIGN KEY sys_c00246805 (characterization_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS particle_function;
CREATE TABLE particle_function (
  particle_function_pk_id DECIMAL(22, 0) NOT NULL,
  type VARCHAR(100) BINARY NULL,
  activation_method VARCHAR(500) BINARY NULL,
  nanoparticle_pk_id DECIMAL(22, 0) NULL,
  identifier_name VARCHAR(200) BINARY NULL,
  description VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (particle_function_pk_id),
  CONSTRAINT sys_c00246798 FOREIGN KEY sys_c00246798 (nanoparticle_pk_id)
    REFERENCES nanoparticle (nanoparticle_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS polymer_composition;
CREATE TABLE polymer_composition (
  is_cross_link DECIMAL(22, 0) NULL,
  cross_link_degree DECIMAL(22, 0) NULL,
  initiator VARCHAR(200) BINARY NULL,
  p_composition_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (p_composition_pk_id),
  CONSTRAINT sys_c00246825 FOREIGN KEY sys_c00246825 (p_composition_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS project;
CREATE TABLE project (
  project_pk_id DECIMAL(22, 0) NOT NULL,
  project_name VARCHAR(200) BINARY NULL,
  description VARCHAR(4000) BINARY NULL,
  PRIMARY KEY (project_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS project_sample;
CREATE TABLE project_sample (
  sample_pk_id DECIMAL(22, 0) NOT NULL,
  project_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (sample_pk_id, project_pk_id),
  CONSTRAINT sys_c00246780 FOREIGN KEY sys_c00246780 (project_pk_id)
    REFERENCES project (project_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246781 FOREIGN KEY sys_c00246781 (sample_pk_id)
    REFERENCES sample (sample_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS protocol;
CREATE TABLE protocol (
  protocol_pk_id DECIMAL(22, 0) NOT NULL,
  protocol_name VARCHAR(2000) BINARY NULL,
  protocol_type VARCHAR(2000) BINARY NULL,
  PRIMARY KEY (protocol_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS protocol_file;
CREATE TABLE protocol_file (
  protocol_file_pk_id DECIMAL(22, 0) NOT NULL,
  protocol_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (protocol_file_pk_id),
  CONSTRAINT con_prot_prot_file FOREIGN KEY con_prot_prot_file (protocol_pk_id)
    REFERENCES protocol (protocol_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246823 FOREIGN KEY sys_c00246823 (protocol_file_pk_id)
    REFERENCES lab_file (file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS report;
CREATE TABLE report (
  report_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (report_pk_id),
  CONSTRAINT sys_c00246814 FOREIGN KEY sys_c00246814 (report_pk_id)
    REFERENCES lab_file (file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS run;
CREATE TABLE run (
  run_pk_id DECIMAL(22, 0) NOT NULL,
  run_name VARCHAR(500) BINARY NULL,
  description VARCHAR(4000) BINARY NULL,
  created_by VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  assay_pk_id DECIMAL(22, 0) NULL,
  run_by VARCHAR(200) BINARY NULL,
  run_date DATETIME NULL,
  PRIMARY KEY (run_pk_id),
  CONSTRAINT sys_c00246782 FOREIGN KEY sys_c00246782 (assay_pk_id)
    REFERENCES assay (assay_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS run_input_file;
CREATE TABLE run_input_file (
  input_file_pk_id DECIMAL(22, 0) NULL,
  run_pk_id DECIMAL(22, 0) NULL,
  data_status_pk_id DECIMAL(22, 0) NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS run_output_file;
CREATE TABLE run_output_file (
  output_file_pk_id DECIMAL(22, 0) NULL,
  run_pk_id DECIMAL(22, 0) NULL,
  data_status_pk_id DECIMAL(22, 0) NULL
)
ENGINE = INNODB;

DROP TABLE IF EXISTS run_sample_container;
CREATE TABLE run_sample_container (
  comments VARCHAR(4000) BINARY NULL,
  run_sample_container_pk_id DECIMAL(22, 0) NOT NULL,
  created_by VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  run_pk_id DECIMAL(22, 0) NULL,
  sample_container_pk_id DECIMAL(22, 0) NULL,
  status VARCHAR(20) BINARY NULL,
  reason VARCHAR(2000) BINARY NULL,
  data_status_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (run_sample_container_pk_id),
  CONSTRAINT sys_c00246783 FOREIGN KEY sys_c00246783 (sample_container_pk_id)
    REFERENCES sample_container (sample_container_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246784 FOREIGN KEY sys_c00246784 (run_pk_id)
    REFERENCES run (run_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246785 FOREIGN KEY sys_c00246785 (data_status_pk_id)
    REFERENCES data_status (data_status_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS sample;
CREATE TABLE sample (
  sample_pk_id DECIMAL(22, 0) NOT NULL,
  sample_sequence_id DECIMAL(22, 0) NULL,
  sample_type VARCHAR(100) BINARY NULL,
  description VARCHAR(4000) BINARY NULL,
  source_sample_id VARCHAR(100) BINARY NULL,
  solubility_description VARCHAR(4000) BINARY NULL,
  lot_id VARCHAR(100) BINARY NULL,
  lot_description VARCHAR(4000) BINARY NULL,
  number_of_containers DECIMAL(22, 0) NULL,
  general_comments VARCHAR(4000) BINARY NULL,
  received_date DATETIME NULL,
  created_by VARCHAR(200) BINARY NULL,
  created_date DATETIME NULL,
  source_pk_id DECIMAL(22, 0) NULL,
  received_by VARCHAR(200) BINARY NULL,
  sample_name VARCHAR(200) BINARY NULL,
  sample_sop_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (sample_pk_id),
  CONSTRAINT sys_c00246786 FOREIGN KEY sys_c00246786 (sample_sop_pk_id)
    REFERENCES sample_sop (sample_sop_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246787 FOREIGN KEY sys_c00246787 (source_pk_id)
    REFERENCES source (source_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS sample_associated_file;
CREATE TABLE sample_associated_file (
  sample_pk_id DECIMAL(22, 0) NOT NULL,
  associated_file_pk_id DECIMAL(22, 0) NOT NULL,
  CONSTRAINT sys_c00246806 FOREIGN KEY sys_c00246806 (sample_pk_id)
    REFERENCES sample (sample_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246808 FOREIGN KEY sys_c00246808 (associated_file_pk_id)
    REFERENCES associated_file (associated_file_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS sample_container;
CREATE TABLE sample_container (
  sample_container_pk_id DECIMAL(22, 0) NOT NULL,
  quantity DECIMAL(22, 0) NULL,
  concentration DECIMAL(22, 0) NULL,
  volume DECIMAL(22, 0) NULL,
  diluents_solvent VARCHAR(500) BINARY NULL,
  safety_precautions VARCHAR(4000) BINARY NULL,
  storage_conditions VARCHAR(1000) BINARY NULL,
  comments VARCHAR(4000) BINARY NULL,
  quantity_unit VARCHAR(100) BINARY NULL,
  concentration_unit VARCHAR(100) BINARY NULL,
  volume_unit VARCHAR(100) BINARY NULL,
  barcode VARCHAR(50) BINARY NULL,
  container_type VARCHAR(200) BINARY NULL,
  sample_pk_id DECIMAL(22, 0) NULL,
  is_derived VARCHAR(20) BINARY NULL,
  created_method VARCHAR(500) BINARY NULL,
  reason VARCHAR(2000) BINARY NULL,
  status VARCHAR(20) BINARY NULL,
  storage_pk_id DECIMAL(22, 0) NULL,
  created_date DATETIME NULL,
  created_by VARCHAR(200) BINARY NULL,
  name VARCHAR(200) BINARY NULL,
  data_status_pk_id DECIMAL(22, 0) NULL,
  PRIMARY KEY (sample_container_pk_id),
  CONSTRAINT sys_c00246788 FOREIGN KEY sys_c00246788 (storage_pk_id)
    REFERENCES storage (storage_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246789 FOREIGN KEY sys_c00246789 (sample_pk_id)
    REFERENCES sample (sample_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246790 FOREIGN KEY sys_c00246790 (data_status_pk_id)
    REFERENCES data_status (data_status_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS sample_report;
CREATE TABLE sample_report (
  sample_pk_id DECIMAL(22, 0) NOT NULL,
  file_pk_id DECIMAL(22, 0) NOT NULL,
  CONSTRAINT sys_c00246811 FOREIGN KEY sys_c00246811 (file_pk_id)
    REFERENCES report (report_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246812 FOREIGN KEY sys_c00246812 (sample_pk_id)
    REFERENCES sample (sample_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS sample_sop;
CREATE TABLE sample_sop (
  sample_sop_pk_id DECIMAL(22, 0) NOT NULL,
  description VARCHAR(2000) BINARY NULL,
  sop_name VARCHAR(200) BINARY NULL,
  PRIMARY KEY (sample_sop_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS shape;
CREATE TABLE shape (
  shape_pk_id DECIMAL(22, 0) NOT NULL,
  max_dimension DECIMAL(22, 0) NULL,
  min_dimension DECIMAL(22, 0) NULL,
  type VARCHAR(100) BINARY NULL,
  min_dimension_unit VARCHAR(100) BINARY NULL,
  max_dimension_unit VARCHAR(100) BINARY NULL,
  PRIMARY KEY (shape_pk_id),
  CONSTRAINT sys_c00246799 FOREIGN KEY sys_c00246799 (shape_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS solubility;
CREATE TABLE solubility (
  solubility_pk_id DECIMAL(22, 0) NOT NULL,
  solvent VARCHAR(200) BINARY NULL,
  critical_concentration DECIMAL(22, 0) NULL,
  concentration_unit VARCHAR(100) BINARY NULL,
  is_soluble DECIMAL(22, 0) NULL,
  PRIMARY KEY (solubility_pk_id),
  CONSTRAINT sys_c00246800 FOREIGN KEY sys_c00246800 (solubility_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS source;
CREATE TABLE source (
  source_pk_id DECIMAL(22, 0) NOT NULL,
  organization_name VARCHAR(200) BINARY NULL,
  address VARCHAR(200) BINARY NULL,
  city VARCHAR(100) BINARY NULL,
  state VARCHAR(100) BINARY NULL,
  country VARCHAR(100) BINARY NULL,
  postal_code VARCHAR(10) BINARY NULL,
  PRIMARY KEY (source_pk_id),
  UNIQUE INDEX unique_name (organization_name(200))
)
ENGINE = INNODB;

DROP TABLE IF EXISTS source_contact;
CREATE TABLE source_contact (
  contact_pk_id DECIMAL(38, 0) NOT NULL,
  source_pk_id DECIMAL(22, 0) NOT NULL,
  PRIMARY KEY (contact_pk_id, source_pk_id),
  CONSTRAINT sys_c00246791 FOREIGN KEY sys_c00246791 (source_pk_id)
    REFERENCES source (source_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT sys_c00246792 FOREIGN KEY sys_c00246792 (contact_pk_id)
    REFERENCES contact (contact_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS stability;
CREATE TABLE stability (
  stability_pk_id DECIMAL(22, 0) NOT NULL,
  long_term_storage DECIMAL(22, 0) NULL,
  long_term_storage_unit VARCHAR(100) BINARY NULL,
  short_term_storage DECIMAL(22, 0) NULL,
  short_term_storage_unit VARCHAR(100) BINARY NULL,
  stress_result VARCHAR(500) BINARY NULL,
  release_kinetics_description VARCHAR(4000) BINARY NULL,
  measurement_type VARCHAR(100) BINARY NULL,
  stressor_type VARCHAR(100) BINARY NULL,
  stressor_desc VARCHAR(2000) BINARY NULL,
  stressor_value DECIMAL(22, 0) NULL,
  stressor_value_unit VARCHAR(100) BINARY NULL,
  PRIMARY KEY (stability_pk_id),
  CONSTRAINT sys_c00246801 FOREIGN KEY sys_c00246801 (stability_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS storage;
CREATE TABLE storage (
  storage_pk_id DECIMAL(22, 0) NOT NULL,
  storage_location VARCHAR(500) BINARY NULL,
  storage_type VARCHAR(200) BINARY NULL,
  PRIMARY KEY (storage_pk_id)
)
ENGINE = INNODB;

DROP TABLE IF EXISTS surface;
CREATE TABLE surface (
  surface_area DECIMAL(22, 0) NULL,
  surface_area_unit VARCHAR(100) BINARY NULL,
  zeta_potential DECIMAL(22, 0) NULL,
  charge DECIMAL(22, 0) NULL,
  charge_unit VARCHAR(100) BINARY NULL,
  is_hydrophobic DECIMAL(22, 0) NULL,
  surface_pk_id DECIMAL(22, 0) NOT NULL,
  zeta_potential_unit VARCHAR(100) BINARY NULL,
  PRIMARY KEY (surface_pk_id),
  CONSTRAINT sys_c00246802 FOREIGN KEY sys_c00246802 (surface_pk_id)
    REFERENCES characterization (characterization_pk_id)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS surface_chemistry;
CREATE TABLE surface_chemistry (
  surface_chemistry_pk_id DECIMAL(22, 0) NOT NULL,
  molecule_name VARCHAR(200) BINARY NULL,
  surface_pk_id DECIMAL(22, 0) NULL,
  number_molecule DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  molecular_formula_type VARCHAR(200) BINARY NULL,
  PRIMARY KEY (surface_chemistry_pk_id),
  CONSTRAINT sys_c00246803 FOREIGN KEY sys_c00246803 (surface_pk_id)
    REFERENCES surface (surface_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS surface_group;
CREATE TABLE surface_group (
  surface_group_pk_id DECIMAL(22, 0) NOT NULL,
  name VARCHAR(100) BINARY NULL,
  modifier VARCHAR(100) BINARY NULL,
  d_composition_pk_id DECIMAL(22, 0) NULL,
  list_index DECIMAL(22, 0) NULL,
  PRIMARY KEY (surface_group_pk_id),
  CONSTRAINT sys_c00246804 FOREIGN KEY sys_c00246804 (d_composition_pk_id)
    REFERENCES dendrimer_composition (d_composition_pk_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;



-- -------------------------------------
-- Views

 DROP VIEW IF EXISTS view_sample_sop_file;
 CREATE OR REPLACE VIEW view_sample_sop_file (SOP_FILE_PK_ID, FILE_NAME, FILE_TYPE_EXTENSION, STATUS, REASON, FILE_URI, VERSION, CREATED_BY, CREATED_DATE, SAMPLE_SOP_PK_ID) AS
 select FILE_PK_ID,
 	FILE_NAME,
 	FILE_TYPE_EXTENSION,
 	STATUS,
 	REASON,
 	FILE_URI,
 	VERSION,
 	CREATED_BY,
 	CREATED_DATE,
 	SAMPLE_SOP_pk_ID from lab_file where File_source_type = 'SOP';



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF

