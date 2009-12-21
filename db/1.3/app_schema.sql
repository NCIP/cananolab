-- ----------------------------------------------------------------------
-- mysql migration toolkit
-- sql create script
-- ----------------------------------------------------------------------

set foreign_key_checks = 0;

create database if not exists cananolab
  character set latin1 collate latin1_swedish_ci;
use cananolab;

-- -------------------------------------
-- tables

drop table if exists agent;
create table agent (
  agent_pk_id bigint not null,
  discriminator varchar(200) null,
  description varchar(2000) null,
  name varchar(200) null,
  other varchar(200) null,
  sequence varchar(2000) null,
  primary key (agent_pk_id)
)
engine = innodb;

drop table if exists agent_target;
create table agent_target (
  agent_target_pk_id bigint not null,
  discriminator varchar(200) null,
  name varchar(200) null,
  description varchar(2000) null,
  list_index bigint null,
  agent_pk_id bigint null,
  primary key (agent_target_pk_id),
  constraint fk_agent_target_agent foreign key fk_agent_target_agent (agent_pk_id)
    references agent (agent_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists associated_file;
create table associated_file (
  associated_file_pk_id bigint not null,
  primary key (associated_file_pk_id),
  constraint sys_c00246813 foreign key sys_c00246813 (associated_file_pk_id)
    references lab_file (file_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists bioassay_data_data_category;
create table bioassay_data_data_category (
  derived_bioassay_data_pk_id bigint not null,
  category_index int not null,
  category_name varchar(2000) null,
  primary key (derived_bioassay_data_pk_id, category_index),
  constraint fk_data_data_category foreign key fk_data_data_category (derived_bioassay_data_pk_id)
    references derived_bioassay_data (derived_bioassay_data_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists carbon_nanotube_composition;
create table carbon_nanotube_composition (
  chirality varchar(100) null,
  growth_diameter decimal(22, 3) null,
  average_length decimal(22, 3) null,
  wall_type varchar(100) null,
  cn_composition_pk_id bigint not null,
  primary key (cn_composition_pk_id),
  constraint sys_c00246826 foreign key sys_c00246826 (cn_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists characterization;
create table characterization (
  characterization_pk_id bigint not null,
  classification varchar(200) null,
  source varchar(200) null,
  description varchar(2000) null,
  identifier_name varchar(500) null,
  name varchar(100) null,
  discriminator varchar(50) null,
  created_date datetime null,
  created_by varchar(200) null,
  protocol_file_pk_id bigint null,
  instrument_config_pk_id bigint null,
  instrument_pk_id bigint null,
  char_protocol_pk_id bigint null,
  primary key (characterization_pk_id),
  constraint sys_c00246819 foreign key sys_c00246819 (instrument_config_pk_id)
    references instrument_config (instrument_config_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246820 foreign key sys_c00246820 (protocol_file_pk_id)
    references protocol_file (protocol_file_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists composing_element;
create table composing_element (
  composing_element_pk_id bigint not null,
  element_type varchar(100) null,
  chemical_name varchar(200) null,
  description varchar(2000) null,
  characterization_pk_id bigint null,
  list_index bigint null,
  primary key (composing_element_pk_id),
  constraint sys_c00246831 foreign key sys_c00246831 (characterization_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists contact;
create table contact (
  contact_pk_id bigint not null,
  first_name varchar(100) not null,
  last_name varchar(100) not null,
  title varchar(100) null,
  phone_number varchar(15) null,
  email varchar(100) null,
  update_date datetime not null,
  middle_name varchar(100) null,
  fax varchar(20) null,
  address varchar(200) null,
  city varchar(100) null,
  state varchar(100) null,
  country varchar(100) null,
  postal_code varchar(10) null,
  pi_name varchar(200) null,
  primary key (contact_pk_id)
)
engine = innodb;

drop table if exists container_storage_location;
create table container_storage_location (
  sample_container_pk_id bigint not null,
  storage_pk_id bigint not null,
  constraint sys_c00246773 foreign key sys_c00246773 (sample_container_pk_id)
    references sample_container (sample_container_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246774 foreign key sys_c00246774 (storage_pk_id)
    references storage (storage_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists cytotoxicity;
create table cytotoxicity (
  cytotoxicity_pk_id bigint not null,
  cell_line varchar(200) null,
  cell_death_method varchar(200) null,
  primary key (cytotoxicity_pk_id),
  constraint sys_c00246824 foreign key sys_c00246824 (cytotoxicity_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists data_status;
create table data_status (
  data_status_pk_id bigint not null,
  status varchar(20) null,
  reason varchar(2000) null,
  primary key (data_status_pk_id)
)
engine = innodb;

drop table if exists datum;
create table datum (
  datum_pk_id bigint not null,
  name varchar(2000) not null,
  value decimal(22, 3) not null,
  value_unit varchar(200) null,
  derived_bioassay_data_pk_id bigint null,
  control_name varchar(200) null,
  control_type varchar(100) null,
  list_index bigint null,
  statistics_type varchar(200) null,
  bioassay_data_category varchar(2000) null,
  primary key (datum_pk_id),
  constraint sys_c00246821 foreign key sys_c00246821 (derived_bioassay_data_pk_id)
    references derived_bioassay_data (derived_bioassay_data_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists datum_condition;
create table datum_condition (
  datum_condition_pk_id bigint not null,
  name varchar(100) not null,
  value decimal(22, 3) not null,
  value_unit varchar(200) not null,
  datum_pk_id bigint not null,
  list_index bigint null,
  statistics_type varchar(200) null,
  primary key (datum_condition_pk_id),
  constraint sys_c00246822 foreign key sys_c00246822 (datum_pk_id)
    references datum (datum_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists def_activation_method;
create table def_activation_method (
  activation_method_pk_id bigint not null,
  name varchar(200) not null,
  primary key (activation_method_pk_id)
)
engine = innodb;

drop table if exists def_bioassay_data_category;
create table def_bioassay_data_category (
  category_pk_id bigint not null,
  name varchar(2000) not null,
  characterization_name varchar(2000) not null,
  primary key (category_pk_id)
)
engine = innodb;

drop table if exists def_bond_type;
create table def_bond_type (
  bond_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (bond_type_pk_id)
)
engine = innodb;

drop table if exists def_cellline_type;
create table def_cellline_type (
  cellline_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (cellline_type_pk_id)
)
engine = innodb;

drop table if exists def_characterization_category;
create table def_characterization_category (
  char_category_pk_id bigint not null,
  category varchar(200) not null,
  name varchar(200) not null,
  has_action tinyint not null,
  category_order smallint not null,
  indent_level tinyint not null,
  name_abbreviation varchar(200) null,
  primary key (char_category_pk_id)
)
engine = innodb;

drop table if exists def_characterization_file_type;
create table def_characterization_file_type (
  file_type_pk_id bigint not null,
  name varchar(2000) not null,
  primary key (file_type_pk_id)
)
engine = innodb;

drop table if exists def_datum_name;
create table def_datum_name (
  datum_name_pk_id bigint not null,
  name varchar(2000) not null,
  is_datum_parsed int(2) not null default 1,
  characterization_name varchar(2000) null,
  primary key (datum_name_pk_id)
)
engine = innodb;

drop table if exists def_function_agent_target_type;
create table def_function_agent_target_type (
  agent_target_type_pk_id bigint not null,
  name varchar(2000) not null
)
engine = innodb;

drop table if exists def_function_agent_type;
create table def_function_agent_type (
  agent_type_pk_id bigint not null,
  name varchar(2000) not null
)
engine = innodb;

drop table if exists def_function_linkage_type;
create table def_function_linkage_type (
  linkage_type_pk_id bigint not null,
  name varchar(2000) not null
)
engine = innodb;

drop table if exists def_function_type;
create table def_function_type (
  function_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (function_type_pk_id)
)
engine = innodb;

drop table if exists def_image_contrast_agent_type;
create table def_image_contrast_agent_type (
  agent_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (agent_type_pk_id)
)
engine = innodb;

drop table if exists def_measure_type;
create table def_measure_type (
  measure_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (measure_type_pk_id)
)
engine = innodb;

drop table if exists def_measure_unit;
create table def_measure_unit (
  measure_unit_pk_id bigint not null,
  unit_name varchar(50) not null,
  description varchar(1000) null,
  unit_type varchar(100) not null,
  primary key (measure_unit_pk_id)
)
engine = innodb;

drop table if exists def_molecular_formula_type;
create table def_molecular_formula_type (
  molecular_formula_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (molecular_formula_type_pk_id)
)
engine = innodb;

drop table if exists def_morphology_type;
create table def_morphology_type (
  morphology_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (morphology_type_pk_id)
)
engine = innodb;

drop table if exists def_protocol_type;
create table def_protocol_type (
  protocol_type_pk_id bigint not null,
  name varchar(2000) not null
)
engine = innodb;

drop table if exists def_sample_type;
create table def_sample_type (
  sample_type_pk_id bigint not null,
  name varchar(200) not null
)
engine = innodb;

drop table if exists def_shape_type;
create table def_shape_type (
  shape_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (shape_type_pk_id)
)
engine = innodb;

drop table if exists def_solvent_type;
create table def_solvent_type (
  solvent_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (solvent_type_pk_id)
)
engine = innodb;

drop table if exists def_species_name;
create table def_species_name (
  species_name_pk_id bigint not null,
  name varchar(200) not null,
  primary key (species_name_pk_id)
)
engine = innodb;

drop table if exists def_storage_type;
create table def_storage_type (
  storage_type_id bigint not null,
  name varchar(200) not null,
  primary key (storage_type_id)
)
engine = innodb;

drop table if exists def_surface_group_type;
create table def_surface_group_type (
  surface_group_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (surface_group_type_pk_id)
)
engine = innodb;

drop table if exists def_wall_type;
create table def_wall_type (
  wall_type_pk_id bigint not null,
  name varchar(200) not null,
  primary key (wall_type_pk_id)
)
engine = innodb;

drop table if exists dendrimer_composition;
create table dendrimer_composition (
  generation int null,
  molecular_formula varchar(200) null,
  repeat_unit varchar(100) null,
  branch varchar(200) null,
  d_composition_pk_id bigint not null,
  primary key (d_composition_pk_id),
  constraint sys_c00246827 foreign key sys_c00246827 (d_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists derived_bioassay_data;
create table derived_bioassay_data (
  derived_bioassay_data_pk_id bigint not null,
  characterization_pk_id bigint null,
  list_index bigint null,
  primary key (derived_bioassay_data_pk_id),
  constraint sys_c00246817 foreign key sys_c00246817 (derived_bioassay_data_pk_id)
    references lab_file (file_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246818 foreign key sys_c00246818 (characterization_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists derived_sample_container;
create table derived_sample_container (
  parent_container_id bigint not null,
  sample_container_pk_id bigint not null,
  primary key (parent_container_id, sample_container_pk_id),
  constraint sys_c00246775 foreign key sys_c00246775 (sample_container_pk_id)
    references sample_container (sample_container_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246776 foreign key sys_c00246776 (parent_container_id)
    references sample_container (sample_container_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists emulsion_composition;
create table emulsion_composition (
  emulsion_type varchar(200) null,
  molecular_formula varchar(200) null,
  polymer_name varchar(200) null,
  is_polymerized tinyint null,
  e_composition_pk_id bigint not null,
  primary key (e_composition_pk_id),
  constraint sys_c00246828 foreign key sys_c00246828 (e_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists fullerene_composition;
create table fullerene_composition (
  number_of_carbon varchar(200) null,
  f_composition_pk_id bigint not null,
  primary key (f_composition_pk_id),
  constraint sys_c00246829 foreign key sys_c00246829 (f_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists hibernate_unique_key;
create table hibernate_unique_key (
  next_hi bigint not null
)
engine = innodb;

drop table if exists history_characterization;
create table history_characterization (
  characterization_pk_id bigint null,
  classification varchar(200) null,
  source varchar(200) null,
  description varchar(2000) null,
  identifier_name varchar(500) null,
  name varchar(100) null,
  discriminator varchar(50) null,
  created_date datetime null,
  created_by varchar(200) null,
  protocol_file_pk_id bigint null,
  instrument_config_pk_id bigint null,
  deleted_date datetime null,
  chirality varchar(100) null,
  growth_diameter decimal(22, 3) null,
  average_length decimal(22, 3) null,
  wall_type varchar(100) null,
  generation int null,
  molecular_formula varchar(200) null,
  repeat_unit varchar(100) null,
  branch varchar(200) null,
  emulsion_type varchar(200) null,
  polymer_name varchar(200) null,
  is_polymerized tinyint null,
  table_source varchar(500) null,
  cell_line varchar(200) null,
  cell_death_method varchar(200) null,
  number_of_carbon varchar(200) null,
  type varchar(100) null,
  is_cross_link tinyint null,
  cross_link_degree decimal(22, 3) null,
  initiator varchar(200) null,
  max_dimension decimal(22, 3) null,
  min_dimension decimal(22, 3) null,
  max_dimension_unit varchar(100) null,
  min_dimension_unit varchar(100) null,
  solvent varchar(200) null,
  critical_concentration decimal(22, 3) null,
  concentration_unit varchar(100) null,
  is_soluble tinyint null,
  surface_area decimal(22, 3) null,
  surface_area_unit varchar(100) null,
  zeta_potential decimal(22, 3) null,
  zeta_potential_unit varchar(100) null,
  charge decimal(22, 3) null,
  charge_unit varchar(100) null,
  is_hydrophobic tinyint null
)
engine = innodb;

drop table if exists history_composing_element;
create table history_composing_element (
  composing_element_pk_id bigint not null,
  element_type varchar(100) null,
  chemical_name varchar(200) null,
  description varchar(2000) null,
  characterization_pk_id bigint null,
  list_index bigint null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_datum;
create table history_datum (
  datum_pk_id bigint not null,
  name varchar(2000) not null,
  value decimal(22, 3) not null,
  value_unit varchar(200) null,
  derived_bioassay_data_pk_id bigint null,
  control_name varchar(200) null,
  control_type varchar(100) null,
  list_index bigint null,
  statistics_type varchar(200) null,
  bioassay_data_category varchar(2000) null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_derived_bioassay_data;
create table history_derived_bioassay_data (
  derived_bioassay_data_pk_id bigint not null,
  characterization_pk_id bigint null,
  list_index bigint null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_lab_file;
create table history_lab_file (
  file_pk_id bigint not null,
  file_name varchar(500) null,
  file_uri varchar(500) null,
  file_type_extension varchar(100) null,
  file_source_type varchar(100) null,
  version varchar(200) null,
  status varchar(20) null,
  reason varchar(2000) null,
  created_by varchar(200) null,
  created_date datetime null,
  sample_sop_pk_id bigint null,
  run_pk_id bigint null,
  data_status_pk_id bigint null,
  title varchar(500) null,
  description varchar(2000) null,
  comments varchar(2000) null,
  type varchar(200) null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_nanoparticle_char;
create table history_nanoparticle_char (
  characterization_pk_id bigint not null,
  nanoparticle_pk_id bigint not null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_surface_chemistry;
create table history_surface_chemistry (
  surface_chemistry_pk_id bigint null,
  molecule_name varchar(200) null,
  surface_pk_id bigint null,
  number_molecule int null,
  list_index bigint null,
  molecular_formula_type varchar(200) null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists history_surface_group;
create table history_surface_group (
  surface_group_pk_id bigint null,
  name varchar(100) null,
  modifier varchar(100) null,
  d_composition_pk_id bigint null,
  list_index bigint null,
  deleted_date datetime null
)
engine = innodb;

drop table if exists instrument;
create table instrument (
  instrument_pk_id bigint not null,
  type varchar(200) null,
  abbreviation varchar(50) null,
  manufacturer varchar(2000) null,
  primary key (instrument_pk_id)
)
engine = innodb;

drop table if exists instrument_config;
create table instrument_config (
  instrument_config_pk_id bigint not null,
  description varchar(4000) null,
  instrument_pk_id bigint not null,
  primary key (instrument_config_pk_id),
  constraint con_config_instrument foreign key con_config_instrument (instrument_pk_id)
    references instrument (instrument_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists keyword;
create table keyword (
  keyword_pk_id bigint not null,
  name varchar(100) null,
  primary key (keyword_pk_id)
)
engine = innodb;

drop table if exists keyword_bioassay_data;
create table keyword_bioassay_data (
  keyword_pk_id bigint not null,
  derived_bioassay_data_pk_id bigint not null,
  primary key (keyword_pk_id, derived_bioassay_data_pk_id),
  constraint sys_c00246815 foreign key sys_c00246815 (derived_bioassay_data_pk_id)
    references derived_bioassay_data (derived_bioassay_data_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246816 foreign key sys_c00246816 (keyword_pk_id)
    references keyword (keyword_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists keyword_nanoparticle;
create table keyword_nanoparticle (
  keyword_pk_id bigint not null,
  nanoparticle_pk_id bigint not null,
  primary key (keyword_pk_id, nanoparticle_pk_id),
  constraint sys_c00246793 foreign key sys_c00246793 (nanoparticle_pk_id)
    references nanoparticle (nanoparticle_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246794 foreign key sys_c00246794 (keyword_pk_id)
    references keyword (keyword_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists lab_file;
create table lab_file (
  file_pk_id bigint not null,
  file_name varchar(500) null,
  file_uri varchar(500) null,
  file_type_extension varchar(100) null,
  file_source_type varchar(100) null,
  version varchar(200) null,
  status varchar(20) null,
  reason varchar(2000) null,
  created_by varchar(200) null,
  created_date datetime null,
  sample_sop_pk_id bigint null,
  run_pk_id bigint null,
  data_status_pk_id bigint null,
  title varchar(500) null,
  description varchar(2000) null,
  comments varchar(2000) null,
  type varchar(200) null,
  primary key (file_pk_id),
  constraint sys_c00246777 foreign key sys_c00246777 (run_pk_id)
    references run (run_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246778 foreign key sys_c00246778 (sample_sop_pk_id)
    references sample_sop (sample_sop_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246779 foreign key sys_c00246779 (data_status_pk_id)
    references data_status (data_status_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists linkage;
create table linkage (
  linkage_pk_id bigint not null,
  description varchar(2000) null,
  discriminator varchar(200) null,
  bond_type varchar(200) null,
  localization varchar(200) null,
  function_pk_id bigint null,
  list_index bigint null,
  primary key (linkage_pk_id),
  constraint fk_linkage_function foreign key fk_linkage_function (function_pk_id)
    references particle_function (particle_function_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists liposome_composition;
create table liposome_composition (
  is_polymerized tinyint null,
  polymer_name varchar(200) null,
  l_composition_pk_id bigint not null,
  primary key (l_composition_pk_id),
  constraint sys_c00246830 foreign key sys_c00246830 (l_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists morphology;
create table morphology (
  morphology_pk_id bigint not null,
  type varchar(100) null,
  primary key (morphology_pk_id),
  constraint sys_c00246795 foreign key sys_c00246795 (morphology_pk_id)
    references characterization (characterization_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists nanoparticle;
create table nanoparticle (
  nanoparticle_pk_id bigint not null,
  classification varchar(200) null,
  primary key (nanoparticle_pk_id),
  constraint sys_c00246796 foreign key sys_c00246796 (nanoparticle_pk_id)
    references sample (sample_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists nanoparticle_char;
create table nanoparticle_char (
  characterization_pk_id bigint not null,
  nanoparticle_pk_id bigint not null,
  primary key (characterization_pk_id, nanoparticle_pk_id),
  constraint sys_c00246797 foreign key sys_c00246797 (nanoparticle_pk_id)
    references nanoparticle (nanoparticle_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246805 foreign key sys_c00246805 (characterization_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists particle_function;
create table particle_function (
  particle_function_pk_id bigint not null,
  type varchar(100) null,
  activation_method varchar(500) null,
  nanoparticle_pk_id bigint null,
  identifier_name varchar(200) null,
  description varchar(2000) null,
  primary key (particle_function_pk_id),
  constraint sys_c00246798 foreign key sys_c00246798 (nanoparticle_pk_id)
    references nanoparticle (nanoparticle_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists polymer_composition;
create table polymer_composition (
  is_cross_link tinyint null,
  cross_link_degree decimal(22, 3) null,
  initiator varchar(200) null,
  p_composition_pk_id bigint not null,
  primary key (p_composition_pk_id),
  constraint sys_c00246825 foreign key sys_c00246825 (p_composition_pk_id)
    references characterization (characterization_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists protocol;
create table protocol (
  protocol_pk_id bigint not null,
  protocol_name varchar(2000) null,
  protocol_type varchar(2000) null,
  primary key (protocol_pk_id)
)
engine = innodb;

drop table if exists protocol_file;
create table protocol_file (
  protocol_file_pk_id bigint not null,
  protocol_pk_id bigint not null,
  primary key (protocol_file_pk_id),
  constraint con_prot_prot_file foreign key con_prot_prot_file (protocol_pk_id)
    references protocol (protocol_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246823 foreign key sys_c00246823 (protocol_file_pk_id)
    references lab_file (file_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists report;
create table report (
  report_pk_id bigint not null,
  primary key (report_pk_id),
  constraint sys_c00246814 foreign key sys_c00246814 (report_pk_id)
    references lab_file (file_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists sample;
create table sample (
  sample_pk_id bigint not null,
  sample_sequence_id bigint null,
  sample_type varchar(100) null,
  description varchar(4000) null,
  source_sample_id varchar(100) null,
  solubility_description varchar(4000) null,
  lot_id varchar(100) null,
  lot_description varchar(4000) null,
  number_of_containers int null,
  general_comments varchar(4000) null,
  received_date datetime null,
  created_by varchar(200) null,
  created_date datetime null,
  source_pk_id bigint null,
  received_by varchar(200) null,
  sample_name varchar(200) null,
  sample_sop_pk_id bigint null,
  primary key (sample_pk_id),
  constraint sys_c00246786 foreign key sys_c00246786 (sample_sop_pk_id)
    references sample_sop (sample_sop_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246787 foreign key sys_c00246787 (source_pk_id)
    references source (source_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists sample_associated_file;
create table sample_associated_file (
  sample_pk_id bigint not null,
  associated_file_pk_id bigint not null,
  constraint sys_c00246806 foreign key sys_c00246806 (sample_pk_id)
    references sample (sample_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246808 foreign key sys_c00246808 (associated_file_pk_id)
    references associated_file (associated_file_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists sample_container;
create table sample_container (
  sample_container_pk_id bigint not null,
  quantity decimal(22, 3) null,
  concentration decimal(22, 3) null,
  volume decimal(22, 0) null,
  diluents_solvent varchar(500) null,
  safety_precautions varchar(4000) null,
  storage_conditions varchar(1000) null,
  comments varchar(4000) null,
  quantity_unit varchar(100) null,
  concentration_unit varchar(100) null,
  volume_unit varchar(100) null,
  barcode varchar(50) null,
  container_type varchar(200) null,
  sample_pk_id bigint null,
  is_derived varchar(20) null,
  created_method varchar(500) null,
  reason varchar(2000) null,
  status varchar(20) null,
  storage_pk_id bigint null,
  created_date datetime null,
  created_by varchar(200) null,
  name varchar(200) null,
  data_status_pk_id bigint null,
  primary key (sample_container_pk_id),
  constraint sys_c00246788 foreign key sys_c00246788 (storage_pk_id)
    references storage (storage_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246789 foreign key sys_c00246789 (sample_pk_id)
    references sample (sample_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246790 foreign key sys_c00246790 (data_status_pk_id)
    references data_status (data_status_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists sample_report;
create table sample_report (
  sample_pk_id bigint not null,
  file_pk_id bigint not null,
  constraint sys_c00246811 foreign key sys_c00246811 (file_pk_id)
    references report (report_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246812 foreign key sys_c00246812 (sample_pk_id)
    references sample (sample_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists sample_sop;
create table sample_sop (
  sample_sop_pk_id bigint not null,
  description varchar(2000) null,
  sop_name varchar(200) null,
  primary key (sample_sop_pk_id)
)
engine = innodb;

drop table if exists shape;
create table shape (
  shape_pk_id bigint not null,
  max_dimension decimal(22, 3) null,
  min_dimension decimal(22, 3) null,
  type varchar(100) null,
  min_dimension_unit varchar(100) null,
  max_dimension_unit varchar(100) null,
  primary key (shape_pk_id),
  constraint sys_c00246799 foreign key sys_c00246799 (shape_pk_id)
    references characterization (characterization_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists solubility;
create table solubility (
  solubility_pk_id bigint not null,
  solvent varchar(200) null,
  critical_concentration decimal(22, 3) null,
  concentration_unit varchar(100) null,
  is_soluble tinyint null,
  primary key (solubility_pk_id),
  constraint sys_c00246800 foreign key sys_c00246800 (solubility_pk_id)
    references characterization (characterization_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists source;
create table source (
  source_pk_id bigint not null,
  organization_name varchar(200) null,
  address varchar(200) null,
  city varchar(100) null,
  state varchar(100) null,
  country varchar(100) null,
  postal_code varchar(10) null,
  primary key (source_pk_id),
  unique index unique_name (organization_name(200))
)
engine = innodb;

drop table if exists source_contact;
create table source_contact (
  contact_pk_id bigint not null,
  source_pk_id bigint not null,
  primary key (contact_pk_id, source_pk_id),
  constraint sys_c00246791 foreign key sys_c00246791 (source_pk_id)
    references source (source_pk_id)
    on delete no action
    on update no action,
  constraint sys_c00246792 foreign key sys_c00246792 (contact_pk_id)
    references contact (contact_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists stability;
create table stability (
  stability_pk_id bigint not null,
  long_term_storage decimal(22, 3) null,
  long_term_storage_unit varchar(100) null,
  short_term_storage decimal(22, 3) null,
  short_term_storage_unit varchar(100) null,
  stress_result varchar(500) null,
  release_kinetics_description varchar(4000) null,
  measurement_type varchar(100) null,
  stressor_type varchar(100) null,
  stressor_desc varchar(2000) null,
  stressor_value decimal(22, 3) null,
  stressor_value_unit varchar(100) null,
  primary key (stability_pk_id),
  constraint sys_c00246801 foreign key sys_c00246801 (stability_pk_id)
    references characterization (characterization_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists storage;
create table storage (
  storage_pk_id bigint not null,
  storage_location varchar(500) null,
  storage_type varchar(200) null,
  primary key (storage_pk_id)
)
engine = innodb;

drop table if exists surface;
create table surface (
  surface_area decimal(22, 3) null,
  surface_area_unit varchar(100) null,
  zeta_potential decimal(22, 3) null,
  charge decimal(22, 3) null,
  charge_unit varchar(100) null,
  is_hydrophobic tinyint null,
  surface_pk_id bigint not null,
  zeta_potential_unit varchar(100) null,
  primary key (surface_pk_id),
  constraint sys_c00246802 foreign key sys_c00246802 (surface_pk_id)
    references characterization (characterization_pk_id)
    on delete cascade
    on update no action
)
engine = innodb;

drop table if exists surface_chemistry;
create table surface_chemistry (
  surface_chemistry_pk_id bigint not null,
  molecule_name varchar(200) null,
  surface_pk_id bigint null,
  number_molecule int null,
  list_index bigint null,
  molecular_formula_type varchar(200) null,
  primary key (surface_chemistry_pk_id),
  constraint sys_c00246803 foreign key sys_c00246803 (surface_pk_id)
    references surface (surface_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists surface_group;
create table surface_group (
  surface_group_pk_id bigint not null,
  name varchar(100) null,
  modifier varchar(100) null,
  d_composition_pk_id bigint null,
  list_index bigint null,
  primary key (surface_group_pk_id),
  constraint sys_c00246804 foreign key sys_c00246804 (d_composition_pk_id)
    references dendrimer_composition (d_composition_pk_id)
    on delete no action
    on update no action
)
engine = innodb;

drop table if exists def_composing_element_type;
create table def_composing_element_type (
   composing_element_type_pk_id bigint not null,
   name varchar (200) not null,
   primary key (composing_element_type_pk_id)
)
engine=innodb;




-- -------------------------------------
-- views

 drop view if exists view_sample_sop_file;
 create or replace view view_sample_sop_file (sop_file_pk_id, file_name, file_type_extension, status, reason, file_uri, version, created_by, created_date, sample_sop_pk_id) as
 select file_pk_id,
 	file_name,
 	file_type_extension,
 	status,
 	reason,
 	file_uri,
 	version,
 	created_by,
 	created_date,
 	sample_sop_pk_id from lab_file where file_source_type = 'SOP';



set foreign_key_checks = 1;

-- ----------------------------------------------------------------------
-- eof

