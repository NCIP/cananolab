USE canano;

CREATE TABLE target
(
	target_pk_id BIGINT NOT NULL,
	discriminator VARCHAR(200) NULL,
	name VARCHAR(200) NULL,
	description VARCHAR(2000) NULL,
	targeting_function_pk_id BIGINT NULL,
	antigen_species VARCHAR(200) NULL,
	other_target_type VARCHAR(200) NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (target_pk_id),
	KEY (targeting_function_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nano_function
(
	function_pk_id BIGINT NOT NULL,
	description TEXT NULL,
	discriminator VARCHAR(200) NULL,
	functionalizing_entity_pk_id BIGINT NULL,
	composing_element_pk_id BIGINT NULL,
	imaging_function_modality VARCHAR(200) NULL,
	other_function_type VARCHAR(200) NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (function_pk_id),
	KEY (composing_element_pk_id),
	KEY (functionalizing_entity_pk_id)
) 
;


CREATE TABLE small_molecule
(
	small_molecule_pk_id BIGINT NOT NULL,
	alternate_name VARCHAR(200) NULL,
	PRIMARY KEY (small_molecule_pk_id),
	KEY (small_molecule_pk_id)
) 
;


CREATE TABLE sample_container_storage
(
	sample_container_pk_id BIGINT NOT NULL,
	storage_pk_id BIGINT NOT NULL,
	PRIMARY KEY (sample_container_pk_id, storage_pk_id),
	KEY (sample_container_pk_id),
	KEY (storage_pk_id)
) 
;


CREATE TABLE polymer
(
	polymer_pk_id BIGINT NOT NULL,
	is_cross_linked TINYINT NULL,
	cross_link_degree DECIMAL(22,3) NULL,
	initiator VARCHAR(200) NULL,
	PRIMARY KEY (polymer_pk_id),
	KEY (polymer_pk_id)
) TYPE=InnoDB
;


CREATE TABLE other_nanoparticle_entity
(
	other_nanoparticle_entity_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	PRIMARY KEY (other_nanoparticle_entity_pk_id),
	KEY (other_nanoparticle_entity_pk_id)
) 
;


CREATE TABLE other_functionalizing_entity
(
	other_func_entity_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	PRIMARY KEY (other_func_entity_pk_id),
	KEY (other_func_entity_pk_id)
) 
;


CREATE TABLE liposome
(
	liposome_pk_id BIGINT NOT NULL,
	is_polymerized TINYINT NULL,
	polymer_name VARCHAR(200) NULL,
	PRIMARY KEY (liposome_pk_id),
	KEY (liposome_pk_id)
) TYPE=InnoDB
;


CREATE TABLE fullerene
(
	fullerene_pk_id BIGINT NOT NULL,
	number_of_carbon DECIMAL(22) NULL,
	average_diameter DECIMAL(22,3) NULL,
	average_diameter_unit VARCHAR(200) NULL,
	PRIMARY KEY (fullerene_pk_id),
	KEY (fullerene_pk_id)
) TYPE=InnoDB
;


CREATE TABLE emulsion
(
	emulsion_pk_id BIGINT NOT NULL,
	polymer_name VARCHAR(200) NULL,
	is_polymerized TINYINT NULL,
	PRIMARY KEY (emulsion_pk_id),
	KEY (emulsion_pk_id)
) TYPE=InnoDB
;


CREATE TABLE dendrimer
(
	dendrimer_pk_id BIGINT NOT NULL,
	generation DECIMAL(22,3) NULL,
	branch VARCHAR(200) NULL,
	PRIMARY KEY (dendrimer_pk_id),
	KEY (dendrimer_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composing_element
(
	composing_element_pk_id BIGINT NOT NULL,
	element_type VARCHAR(100) NOT NULL,
	nanoparticle_entity_pk_id BIGINT NULL,
	PRIMARY KEY (composing_element_pk_id),
	KEY (composing_element_pk_id),
	KEY (nanoparticle_entity_pk_id)
) 
;


CREATE TABLE carbon_nanotube
(
	carbon_nanotube_pk_id BIGINT NOT NULL,
	chirality VARCHAR(100) NULL,
	diameter DECIMAL(22,3) NULL,
	diameter_unit VARCHAR(200) NULL,
	average_length DECIMAL(22,3) NULL,
	average_length_unit VARCHAR(200) NULL,
	wall_type VARCHAR(100) NULL,
	PRIMARY KEY (carbon_nanotube_pk_id),
	KEY (carbon_nanotube_pk_id)
) TYPE=InnoDB
;


CREATE TABLE biopolymer_p
(
	biopolymer_pk_id BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	sequence TEXT NULL,
	name VARCHAR(200) NULL,
	PRIMARY KEY (biopolymer_pk_id),
	KEY (biopolymer_pk_id)
) 
;


CREATE TABLE biopolymer_f
(
	biopolymer_pk_id BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	sequence TEXT NULL,
	PRIMARY KEY (biopolymer_pk_id),
	UNIQUE (biopolymer_pk_id),
	KEY (biopolymer_pk_id)
) 
;


CREATE TABLE antibody
(
	antibody_pk_id BIGINT NOT NULL,
	species VARCHAR(200) NULL,
	type VARCHAR(200) NULL,
	isotype VARCHAR(200) NULL,
	PRIMARY KEY (antibody_pk_id),
	UNIQUE (antibody_pk_id),
	KEY (antibody_pk_id)
) 
;


CREATE TABLE surface_chemistry
(
	surface_chemistry_pk_id BIGINT NOT NULL,
	molecule_name VARCHAR(200) NULL,
	number_molecule INTEGER NULL,
	molecular_formula_type VARCHAR(200) NULL,
	molecular_formula VARCHAR(2000)  NULL,
	surface_pk_id BIGINT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (surface_chemistry_pk_id),
	KEY (surface_pk_id)
) TYPE=InnoDB
;


CREATE TABLE solubility
(
	solubility_pk_id BIGINT NOT NULL,
	solvent VARCHAR(200) NULL,
	critical_concentration DECIMAL(22,3) NULL,
	critical_concentration_unit VARCHAR(200) NULL,
	is_soluble TINYINT NULL,
	PRIMARY KEY (solubility_pk_id),
	KEY (solubility_pk_id)
) TYPE=InnoDB
;


CREATE TABLE shape
(
	shape_pk_id BIGINT NOT NULL,
	max_dimension DECIMAL(22,3) NULL,
	min_dimension DECIMAL(22,3) NULL,
	type VARCHAR(200) NOT NULL,
	min_dimension_unit VARCHAR(200) NULL,
	max_dimension_unit VARCHAR(200) NULL,
	aspect_ratio DECIMAL(22,3) NULL,
	PRIMARY KEY (shape_pk_id),
	KEY (shape_pk_id)
) TYPE=InnoDB
;


CREATE TABLE sample_container
(
	sample_container_pk_id BIGINT NOT NULL,
	container_type VARCHAR(200) NULL,
	name VARCHAR(200) NULL,
	barcode VARCHAR(50) NULL,
	quantity DECIMAL(22,3) NULL,
	quantity_unit VARCHAR(200) NULL,
	concentration DECIMAL(22,3) NULL,
	concentration_unit VARCHAR(200) NULL,
	volume DECIMAL(22,3) NULL,
	volume_unit VARCHAR(200) NULL,
	diluents_solvent TEXT NULL,
	safety_precautions TEXT NULL,
	storage_conditions TEXT NULL,
	comments TEXT NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	sample_management_pk_id BIGINT NOT NULL,
	parent_sample_container_pk_id BIGINT NULL,
	discriminator VARCHAR(200) NULL,
	PRIMARY KEY (sample_container_pk_id),
	UNIQUE (sample_container_pk_id),
	KEY (parent_sample_container_pk_id),
	KEY (sample_management_pk_id)
) TYPE=InnoDB
;


CREATE TABLE physical_state
(
	physical_state_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	PRIMARY KEY (physical_state_pk_id),
	KEY (physical_state_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_entity
(
	nanoparticle_entity_pk_id BIGINT NOT NULL,
	composition_pk_id BIGINT NOT NULL,
	discriminator VARCHAR(200) NULL,
	description TEXT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (nanoparticle_entity_pk_id),
	KEY (composition_pk_id)
) 
;


CREATE TABLE functionalizing_entity
(
	functionalizing_entity_pk_id BIGINT NOT NULL,
	activation_method_pk_id BIGINT NULL,
	composition_pk_id BIGINT NULL,
	PRIMARY KEY (functionalizing_entity_pk_id),
	KEY (activation_method_pk_id),
	KEY (functionalizing_entity_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composition_lab_file
(
	composition_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (composition_pk_id, file_pk_id),
	KEY (composition_pk_id),
	KEY (file_pk_id)
) 
;


CREATE TABLE sample_management
(
	sample_management_pk_id BIGINT NOT NULL,
	description VARCHAR(4000) NULL,
	source_sample_id VARCHAR(100) NULL,
	lot_id VARCHAR(100) NULL,
	lot_description TEXT NULL,
	general_comments TEXT NULL,
	received_date DATETIME NULL,
	received_by VARCHAR(200) NULL,
	particle_sample_pk_id BIGINT NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (sample_management_pk_id),
	UNIQUE (sample_management_pk_id),
	KEY (particle_sample_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample_report
(
	particle_sample_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	KEY (particle_sample_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample_publication
(
	particle_sample_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	INDEX particle_sample_pk_id (particle_sample_pk_id ASC),
	INDEX file_pk_id (file_pk_id ASC)
) TYPE=InnoDB
;


CREATE TABLE derived_datum
(
	datum_pk_id BIGINT NOT NULL,
	datum_name VARCHAR(200) NOT NULL,
	value DECIMAL(22,3) NOT NULL,
	value_type VARCHAR(200) NULL,
	value_unit VARCHAR(200) NULL,
	description TEXT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	derived_bioassay_data_pk_id BIGINT NULL,
	PRIMARY KEY (datum_pk_id),
	UNIQUE (datum_pk_id),
	KEY (derived_bioassay_data_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composition
(
	composition_pk_id BIGINT NOT NULL,
	particle_sample_pk_id BIGINT NULL,
	PRIMARY KEY (composition_pk_id),
	UNIQUE (composition_pk_id),
	KEY (particle_sample_pk_id)
) 
;


CREATE TABLE chemical_association_lab_file
(
	chemical_association_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (chemical_association_pk_id, file_pk_id),
	KEY (chemical_association_pk_id),
	KEY (file_pk_id)
) 
;


CREATE TABLE characterization
(
	characterization_pk_id BIGINT NOT NULL,
	source VARCHAR(200) NULL,
	description TEXT NULL,
	identifier_name VARCHAR(500) NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	protocol_file_pk_id BIGINT NULL,
	instrument_config_pk_id BIGINT NULL,
	particle_sample_pk_id BIGINT NULL,
	discriminator VARCHAR(50) NOT NULL,
	cytotoxicity_cell_line VARCHAR(200) NULL,
	cytotoxicity_cell_death_method VARCHAR(200) NULL,
	surface_is_hydrophobic TINYINT NULL,
	characterization_date DATETIME NULL,
	PRIMARY KEY (characterization_pk_id),
	KEY (instrument_config_pk_id),
	KEY (particle_sample_pk_id),
	KEY (protocol_file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE report
(
	report_pk_id BIGINT NOT NULL,
	category VARCHAR(200) NOT NULL,
	PRIMARY KEY (report_pk_id),
	KEY (report_pk_id)
) TYPE=InnoDB
;


CREATE TABLE publication
(
	publication_pk_id BIGINT NOT NULL,
	category VARCHAR(200) NOT NULL,
	publication_status VARCHAR(50) NOT NULL,
	pubmed_id BIGINT NULL,
	digital_object_id VARCHAR(200) NULL,
	journal_name VARCHAR(200) NULL,
	volume VARCHAR(50) NULL,
	start_page BIGINT NULL,
	end_page BIGINT NULL,
	year INTEGER NULL,
	research_area VARCHAR(200) NULL,
	PRIMARY KEY (publication_pk_id)
) TYPE=InnoDB
;


CREATE TABLE protocol_file
(
	protocol_file_pk_id BIGINT NOT NULL,
	protocol_pk_id BIGINT NOT NULL,
	PRIMARY KEY (protocol_file_pk_id),
	UNIQUE (protocol_file_pk_id),
	KEY (protocol_file_pk_id),
	KEY (protocol_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample
(
	particle_sample_pk_id BIGINT NOT NULL,
	particle_sample_name VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	source_pk_id BIGINT NOT NULL,
	PRIMARY KEY (particle_sample_pk_id),
	UNIQUE (particle_sample_name),
	UNIQUE (particle_sample_pk_id),
	KEY (source_pk_id)
) 
;


CREATE TABLE nanoparticle_entity_lab_file
(
	nanoparticle_entity_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (nanoparticle_entity_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (nanoparticle_entity_pk_id)
) 
;


CREATE TABLE keyword_nanoparticle_sample
(
	keyword_pk_id BIGINT NOT NULL,
	particle_sample_pk_id BIGINT NOT NULL,
	PRIMARY KEY (keyword_pk_id, particle_sample_pk_id),
	KEY (keyword_pk_id),
	KEY (particle_sample_pk_id)
) TYPE=InnoDB
;


CREATE TABLE keyword_lab_file
(
	keyword_pk_id BIGINT NOT NULL,
	lab_file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (keyword_pk_id, lab_file_pk_id),
	KEY (keyword_pk_id),
	KEY (lab_file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE instrument_config
(
	instrument_config_pk_id BIGINT NOT NULL,
	description TEXT NULL,
	instrument_pk_id BIGINT NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (instrument_config_pk_id),
	KEY (instrument_pk_id)
) TYPE=InnoDB
;


CREATE TABLE functionalizing_entity_lab_file
(
	functionalizing_entity_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (functionalizing_entity_pk_id, file_pk_id),
	KEY (functionalizing_entity_pk_id),
	KEY (file_pk_id)
) 
;


CREATE TABLE derived_bioassay_data
(
	derived_bioassay_data_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NULL,
	file_pk_id BIGINT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (derived_bioassay_data_pk_id),
	KEY (characterization_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE chemical_association
(
	chemical_association_pk_id BIGINT NOT NULL,
	composition_pk_id BIGINT NULL,
	associated_element_a_pk_id BIGINT NOT NULL,
	associated_element_b_pk_id BIGINT NOT NULL,
	description TEXT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	discriminator VARCHAR(200) NULL,
	attachment_bond_type VARCHAR(200) NULL,
	other_chemical_association_type VARCHAR(200) NULL,
	PRIMARY KEY (chemical_association_pk_id),
	KEY (associated_element_a_pk_id),
	KEY (associated_element_b_pk_id),
	KEY (composition_pk_id)
) 
;


CREATE TABLE author_publication
(
	document_author_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	KEY (document_author_pk_id),
	KEY (publication_pk_id)
) 
;


CREATE TABLE associated_file
(
	associated_file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (associated_file_pk_id),
	KEY (associated_file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE storage
(
	storage_pk_id BIGINT NOT NULL,
	storage_location TEXT NULL,
	storage_type VARCHAR(200) NOT NULL,
	PRIMARY KEY (storage_pk_id),
	UNIQUE (storage_pk_id)
) TYPE=InnoDB
;


CREATE TABLE source
(
	source_pk_id BIGINT NOT NULL,
	organization_name VARCHAR(200) NOT NULL,
	address VARCHAR(200) NULL,
	city VARCHAR(100) NULL,
	state VARCHAR(100) NULL,
	country VARCHAR(100) NULL,
	postal_code VARCHAR(10) NULL,
	PRIMARY KEY (source_pk_id),
	UNIQUE (source_pk_id)
) TYPE=InnoDB
;


CREATE TABLE protocol
(
	protocol_pk_id BIGINT NOT NULL,
	protocol_name VARCHAR(2000) NULL,
	protocol_type VARCHAR(2000) NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (protocol_pk_id),
	UNIQUE (protocol_pk_id)
) TYPE=InnoDB
;


CREATE TABLE lab_file
(
	file_pk_id BIGINT NOT NULL,
	file_name VARCHAR(500) NULL,
	file_uri VARCHAR(500) NULL,
	version VARCHAR(200) NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	title VARCHAR(500) NULL,
	description TEXT NULL,
	file_type VARCHAR(200) NULL,
	is_uri_external TINYINT NOT NULL,
	PRIMARY KEY (file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE keyword
(
	keyword_pk_id BIGINT NOT NULL,
	name VARCHAR(100) NOT NULL,
	PRIMARY KEY (keyword_pk_id)
) TYPE=InnoDB
;


CREATE TABLE instrument
(
	instrument_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NULL,
	abbreviation VARCHAR(50) NULL,
	manufacturer VARCHAR(2000) NULL,
	PRIMARY KEY (instrument_pk_id),
	UNIQUE (instrument_pk_id)
) TYPE=InnoDB
;


CREATE TABLE document_author
(
	document_author_pk_id BIGINT NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	middle_initial VARCHAR(10) NULL,
	PRIMARY KEY (document_author_pk_id),
	UNIQUE (document_author_pk_id)
) 
;


CREATE TABLE common_lookup
(
	common_lookup_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	attribute VARCHAR(200) NOT NULL,
	value VARCHAR(200) NOT NULL,
	PRIMARY KEY (common_lookup_pk_id)
) 
;


CREATE TABLE associated_element
(
	associated_element_pk_id BIGINT NOT NULL,
	molecular_formula VARCHAR(2000) NULL,
	molecular_formula_type VARCHAR(200) NULL,
	description TEXT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	name VARCHAR(200) NULL,
	value DECIMAL(22,3) NULL,
	value_unit VARCHAR(200) NULL,
	PRIMARY KEY (associated_element_pk_id)
) 
;


CREATE TABLE activation_method
(
	activation_method_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	activation_effect TEXT NULL,
	PRIMARY KEY (activation_method_pk_id)
) 
;

CREATE TABLE hibernate_unique_key (
  next_hi BIGINT NOT NULL
) TYPE=InnoDB
;


ALTER TABLE target ADD CONSTRAINT FK_target_function 
	FOREIGN KEY (targeting_function_pk_id) REFERENCES nano_function (function_pk_id)
;

ALTER TABLE nano_function ADD CONSTRAINT FK_function_composing_element 
	FOREIGN KEY (composing_element_pk_id) REFERENCES composing_element (composing_element_pk_id)
;

ALTER TABLE nano_function ADD CONSTRAINT FK_function_functionalizing_entity 
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE small_molecule ADD CONSTRAINT FK_small_molecule_functionalizing_entity 
	FOREIGN KEY (small_molecule_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE sample_container_storage ADD CONSTRAINT FK_sample_container_storage_sample_container 
	FOREIGN KEY (sample_container_pk_id) REFERENCES sample_container (sample_container_pk_id)
;

ALTER TABLE sample_container_storage ADD CONSTRAINT FK_sample_container_storage_storage 
	FOREIGN KEY (storage_pk_id) REFERENCES storage (storage_pk_id)
;

ALTER TABLE polymer ADD CONSTRAINT FK_polymer_nanoparticle_entity 
	FOREIGN KEY (polymer_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE other_nanoparticle_entity ADD CONSTRAINT FK_other_nanoparticle_entity_nanoparticle_entity 
	FOREIGN KEY (other_nanoparticle_entity_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE other_functionalizing_entity ADD CONSTRAINT FK_other_functionalizing_entity_functionalizing_entity 
	FOREIGN KEY (other_func_entity_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE liposome ADD CONSTRAINT FK_liposome_nanoparticle_entity 
	FOREIGN KEY (liposome_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE fullerene ADD CONSTRAINT FK_fullerene_nanoparticle_entity 
	FOREIGN KEY (fullerene_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE emulsion ADD CONSTRAINT FK_emulsion_nanoparticle_entity 
	FOREIGN KEY (emulsion_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE dendrimer ADD CONSTRAINT FK_dendrimer_nanoparticle_entity 
	FOREIGN KEY (dendrimer_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE composing_element ADD CONSTRAINT FK_composing_element_associated_element 
	FOREIGN KEY (composing_element_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE composing_element ADD CONSTRAINT FK_composing_element_nanoparticle_entity 
	FOREIGN KEY (nanoparticle_entity_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE carbon_nanotube ADD CONSTRAINT FK_carbon_nanotube_nanoparticle_entity 
	FOREIGN KEY (carbon_nanotube_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE biopolymer_p ADD CONSTRAINT FK_biopolymer_p_nanoparticle_entity 
	FOREIGN KEY (biopolymer_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE biopolymer_f ADD CONSTRAINT FK_biopolymer_f_functionalizing_entity 
	FOREIGN KEY (biopolymer_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE antibody ADD CONSTRAINT FK_antibody_functionalizing_entity 
	FOREIGN KEY (antibody_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE surface_chemistry ADD CONSTRAINT FK_surface_chemistry_characterization 
	FOREIGN KEY (surface_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE solubility ADD CONSTRAINT FK_solubility_characterization 
	FOREIGN KEY (solubility_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE shape ADD CONSTRAINT FK_shape_characterization 
	FOREIGN KEY (shape_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE sample_container ADD CONSTRAINT FK_sample_container_sample_container 
	FOREIGN KEY (parent_sample_container_pk_id) REFERENCES sample_container (sample_container_pk_id)
;

ALTER TABLE sample_container ADD CONSTRAINT FK_sample_container_sample_management 
	FOREIGN KEY (sample_management_pk_id) REFERENCES sample_management (sample_management_pk_id)
	ON DELETE CASCADE
;

ALTER TABLE physical_state ADD CONSTRAINT FK_physical_state_characterization 
	FOREIGN KEY (physical_state_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE functionalizing_entity ADD CONSTRAINT FK_functionalizing_entity_activation_method 
	FOREIGN KEY (activation_method_pk_id) REFERENCES activation_method (activation_method_pk_id)
;

ALTER TABLE functionalizing_entity ADD CONSTRAINT FK_functionalizing_entity_associated_element 
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE composition_lab_file ADD CONSTRAINT FK_composition_lab_file_lab_file 
	FOREIGN KEY (file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE sample_management ADD CONSTRAINT FK_sample_management_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE nanoparticle_sample_report ADD CONSTRAINT FK_nanoparticle_sample_report_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE nanoparticle_sample_report ADD CONSTRAINT FK_nanoparticle_sample_report_report 
	FOREIGN KEY (file_pk_id) REFERENCES report (report_pk_id)
;

ALTER TABLE nanoparticle_sample_publication ADD CONSTRAINT FK_nanoparticle_sample_publication_publication 
	FOREIGN KEY (file_pk_id) REFERENCES publication (publication_pk_id)
;

ALTER TABLE derived_datum ADD CONSTRAINT FK_derived_datum_derived_bioassay_data 
	FOREIGN KEY (derived_bioassay_data_pk_id) REFERENCES derived_bioassay_data (derived_bioassay_data_pk_id)
;

ALTER TABLE composition ADD CONSTRAINT FK_Composition_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE chemical_association_lab_file ADD CONSTRAINT FK_chemical_association_lab_file_chemical_association 
	FOREIGN KEY (chemical_association_pk_id) REFERENCES chemical_association (chemical_association_pk_id)
;

ALTER TABLE chemical_association_lab_file ADD CONSTRAINT FK_chemical_association_lab_file_lab_file 
	FOREIGN KEY (file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE characterization ADD CONSTRAINT FK_characterization_instrument_config 
	FOREIGN KEY (instrument_config_pk_id) REFERENCES instrument_config (instrument_config_pk_id)
;

ALTER TABLE characterization ADD CONSTRAINT FK_characterization_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE characterization ADD CONSTRAINT FK_characterization_protocol_file 
	FOREIGN KEY (protocol_file_pk_id) REFERENCES protocol_file (protocol_file_pk_id)
;

ALTER TABLE report ADD CONSTRAINT FK_report_lab_file 
	FOREIGN KEY (report_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE protocol_file ADD CONSTRAINT FK_protocol_file_lab_file 
	FOREIGN KEY (protocol_file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE protocol_file ADD CONSTRAINT FK_protocol_file_protocol 
	FOREIGN KEY (protocol_pk_id) REFERENCES protocol (protocol_pk_id)
;

ALTER TABLE nanoparticle_sample ADD CONSTRAINT FK_nanoparticle_sample_source 
	FOREIGN KEY (source_pk_id) REFERENCES source (source_pk_id)
;

ALTER TABLE nanoparticle_entity_lab_file ADD CONSTRAINT FK_nanoparticle_entity_lab_file_lab_file 
	FOREIGN KEY (file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE nanoparticle_entity_lab_file ADD CONSTRAINT FK_nanoparticle_entity_lab_file_nanoparticle_entity 
	FOREIGN KEY (nanoparticle_entity_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE keyword_nanoparticle_sample ADD CONSTRAINT FK_keyword_nanoparticle_sample_keyword 
	FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id)
;

ALTER TABLE keyword_nanoparticle_sample ADD CONSTRAINT FK_keyword_nanoparticle_sample_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE keyword_lab_file ADD CONSTRAINT FK_keyword_lab_file_keyword 
	FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id)
;

ALTER TABLE keyword_lab_file ADD CONSTRAINT FK_keyword_lab_file_lab_file 
	FOREIGN KEY (lab_file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE instrument_config ADD CONSTRAINT FK_instrument_config_instrument 
	FOREIGN KEY (instrument_pk_id) REFERENCES instrument (instrument_pk_id)
;

ALTER TABLE functionalizing_entity_lab_file ADD CONSTRAINT FK_functionalizing_entity_lab_file_functionalizing_entity 
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE functionalizing_entity_lab_file ADD CONSTRAINT FK_functionalizing_entity_lab_file_lab_file 
	FOREIGN KEY (file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE derived_bioassay_data ADD CONSTRAINT FK_derived_bioassay_data_characterization 
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE derived_bioassay_data ADD CONSTRAINT FK_derived_bioassay_data_lab_file 
	FOREIGN KEY (file_pk_id) REFERENCES lab_file (file_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_associated_element_a 
	FOREIGN KEY (associated_element_a_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_associated_element_b 
	FOREIGN KEY (associated_element_b_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_document_author 
	FOREIGN KEY (document_author_pk_id) REFERENCES document_author (document_author_pk_id)
;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_publication 
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;

ALTER TABLE associated_file ADD CONSTRAINT FK_associated_file_lab_file 
	FOREIGN KEY (associated_file_pk_id) REFERENCES lab_file (file_pk_id)
;
