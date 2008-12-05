USE canano
;



CREATE TABLE target
(
	target_pk_id BIGINT NOT NULL,
	discriminator VARCHAR(200),
	name VARCHAR(200),
	description VARCHAR(2000),
	targeting_function_pk_id BIGINT,
	antigen_species VARCHAR(200),
	other_target_type VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (target_pk_id),
	KEY (targeting_function_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nano_function
(
	function_pk_id BIGINT NOT NULL,
	description TEXT,
	discriminator VARCHAR(200),
	functionalizing_entity_pk_id BIGINT,
	composing_element_pk_id BIGINT,
	imaging_function_modality VARCHAR(200),
	other_function_type VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (function_pk_id),
	KEY (composing_element_pk_id),
	KEY (functionalizing_entity_pk_id)
) TYPE=InnoDB
;


CREATE TABLE small_molecule
(
	small_molecule_pk_id BIGINT NOT NULL,
	alternate_name VARCHAR(200),
	PRIMARY KEY (small_molecule_pk_id),
	KEY (small_molecule_pk_id)
) TYPE=InnoDB
;


CREATE TABLE polymer
(
	polymer_pk_id BIGINT NOT NULL,
	is_cross_linked TINYINT,
	cross_link_degree DECIMAL(22,3),
	initiator VARCHAR(200),
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
) TYPE=InnoDB
;


CREATE TABLE other_functionalizing_entity
(
	other_func_entity_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	PRIMARY KEY (other_func_entity_pk_id),
	KEY (other_func_entity_pk_id)
) TYPE=InnoDB
;


CREATE TABLE liposome
(
	liposome_pk_id BIGINT NOT NULL,
	is_polymerized TINYINT,
	polymer_name VARCHAR(200),
	PRIMARY KEY (liposome_pk_id),
	KEY (liposome_pk_id)
) TYPE=InnoDB
;


CREATE TABLE fullerene
(
	fullerene_pk_id BIGINT NOT NULL,
	number_of_carbon DECIMAL(22),
	average_diameter DECIMAL(22,3),
	average_diameter_unit VARCHAR(200),
	PRIMARY KEY (fullerene_pk_id),
	KEY (fullerene_pk_id)
) TYPE=InnoDB
;


CREATE TABLE emulsion
(
	emulsion_pk_id BIGINT NOT NULL,
	polymer_name VARCHAR(200),
	is_polymerized TINYINT,
	PRIMARY KEY (emulsion_pk_id),
	KEY (emulsion_pk_id)
) TYPE=InnoDB
;


CREATE TABLE dendrimer
(
	dendrimer_pk_id BIGINT NOT NULL,
	generation DECIMAL(22,3),
	branch VARCHAR(200),
	PRIMARY KEY (dendrimer_pk_id),
	KEY (dendrimer_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composing_element
(
	composing_element_pk_id BIGINT NOT NULL,
	element_type VARCHAR(100) NOT NULL,
	nanoparticle_entity_pk_id BIGINT,
	PRIMARY KEY (composing_element_pk_id),
	KEY (composing_element_pk_id),
	KEY (nanoparticle_entity_pk_id)
) TYPE=InnoDB
;


CREATE TABLE carbon_nanotube
(
	carbon_nanotube_pk_id BIGINT NOT NULL,
	chirality VARCHAR(100),
	diameter DECIMAL(22,3),
	diameter_unit VARCHAR(200),
	average_length DECIMAL(22,3),
	average_length_unit VARCHAR(200),
	wall_type VARCHAR(100),
	PRIMARY KEY (carbon_nanotube_pk_id),
	KEY (carbon_nanotube_pk_id)
) TYPE=InnoDB
;


CREATE TABLE biopolymer_p
(
	biopolymer_pk_id BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	sequence TEXT,
	name VARCHAR(200),
	PRIMARY KEY (biopolymer_pk_id),
	KEY (biopolymer_pk_id)
) TYPE=InnoDB
;


CREATE TABLE biopolymer_f
(
	biopolymer_pk_id BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	sequence TEXT,
	PRIMARY KEY (biopolymer_pk_id),
	UNIQUE (biopolymer_pk_id),
	KEY (biopolymer_pk_id)
) TYPE=InnoDB
;


CREATE TABLE antibody
(
	antibody_pk_id BIGINT NOT NULL,
	species VARCHAR(200),
	type VARCHAR(200),
	isotype VARCHAR(200),
	PRIMARY KEY (antibody_pk_id),
	UNIQUE (antibody_pk_id),
	KEY (antibody_pk_id)
) TYPE=InnoDB
;


CREATE TABLE surface_chemistry
(
	surface_chemistry_pk_id BIGINT NOT NULL,
	molecule_name VARCHAR(200),
	number_molecule INTEGER,
	molecular_formula_type VARCHAR(200),
	molecular_formula TEXT,
	surface_pk_id BIGINT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (surface_chemistry_pk_id),
	KEY (surface_pk_id)
) TYPE=InnoDB
;


CREATE TABLE solubility
(
	solubility_pk_id BIGINT NOT NULL,
	solvent VARCHAR(200),
	critical_concentration DECIMAL(22,3),
	critical_concentration_unit VARCHAR(200),
	is_soluble TINYINT,
	PRIMARY KEY (solubility_pk_id),
	KEY (solubility_pk_id)
) TYPE=InnoDB
;


CREATE TABLE shape
(
	shape_pk_id BIGINT NOT NULL,
	max_dimension DECIMAL(22,3),
	min_dimension DECIMAL(22,3),
	type VARCHAR(200) NOT NULL,
	min_dimension_unit VARCHAR(200),
	max_dimension_unit VARCHAR(200),
	aspect_ratio DECIMAL(22,3),
	PRIMARY KEY (shape_pk_id),
	KEY (shape_pk_id)
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
	discriminator VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (nanoparticle_entity_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;


CREATE TABLE functionalizing_entity
(
	functionalizing_entity_pk_id BIGINT NOT NULL,
	activation_method_pk_id BIGINT,
	composition_pk_id BIGINT,
	PRIMARY KEY (functionalizing_entity_pk_id),
	KEY (activation_method_pk_id),
	KEY (functionalizing_entity_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composition_file
(
	composition_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (composition_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample_publication
(
	particle_sample_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	INDEX particle_sample_pk_id (particle_sample_pk_id ASC),
	INDEX publication_pk_id (publication_pk_id ASC)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample_other_organization
(
	particle_sample_pk_id BIGINT NOT NULL,
	organization_pk_id BIGINT NOT NULL,
	PRIMARY KEY (particle_sample_pk_id, organization_pk_id),
	KEY (particle_sample_pk_id),
	KEY (organization_pk_id)
) TYPE=InnoDB
;


CREATE TABLE derived_datum
(
	datum_pk_id BIGINT NOT NULL,
	datum_name VARCHAR(200) NOT NULL,
	value DECIMAL(22,3) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	derived_bioassay_data_pk_id BIGINT,
	PRIMARY KEY (datum_pk_id),
	UNIQUE (datum_pk_id),
	KEY (derived_bioassay_data_pk_id)
) TYPE=InnoDB
;


CREATE TABLE composition
(
	composition_pk_id BIGINT NOT NULL,
	particle_sample_pk_id BIGINT,
	PRIMARY KEY (composition_pk_id),
	UNIQUE (composition_pk_id),
	KEY (particle_sample_pk_id)
) TYPE=InnoDB
;


CREATE TABLE chemical_association_file
(
	chemical_association_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (chemical_association_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (chemical_association_pk_id)
) TYPE=InnoDB
;


CREATE TABLE characterization
(
	characterization_pk_id BIGINT NOT NULL,
	source VARCHAR(200),
	description TEXT,
	identifier_name VARCHAR(500),
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	protocol_file_pk_id BIGINT,
	instrument_config_pk_id BIGINT,
	particle_sample_pk_id BIGINT,
	discriminator VARCHAR(50) NOT NULL,
	cytotoxicity_cell_line VARCHAR(200),
	cytotoxicity_cell_death_method VARCHAR(200),
	surface_is_hydrophobic TINYINT,
	characterization_date DATETIME,
	PRIMARY KEY (characterization_pk_id),
	KEY (instrument_config_pk_id),
	KEY (particle_sample_pk_id),
	KEY (protocol_file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE author_publication
(
	author_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	PRIMARY KEY (author_pk_id, publication_pk_id),
	KEY (author_pk_id),
	KEY (publication_pk_id)
) TYPE=InnoDB
;


CREATE TABLE publication
(
	publication_pk_id BIGINT NOT NULL,
	category VARCHAR(200) NOT NULL,
	publication_status VARCHAR(50) NOT NULL,
	pubmed_id BIGINT,
	digital_object_id VARCHAR(200),
	journal_name VARCHAR(200),
	volume VARCHAR(50),
	start_page VARCHAR(200),
	end_page VARCHAR(200),
	year INTEGER,
	research_area VARCHAR(200),
	abstract TEXT,
	PRIMARY KEY (publication_pk_id),
	UNIQUE (publication_pk_id),
	INDEX publication_pk_id (publication_pk_id ASC)
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


CREATE TABLE point_of_contact
(
	poc_pk_id BIGINT NOT NULL,
	role VARCHAR(200) NOT NULL,
	first_name VARCHAR(200),
	last_name VARCHAR(200),
	middle_initial VARCHAR(50),
	phone VARCHAR(50),
	email VARCHAR(200),
	created_date DATE NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	organization_pk_id BIGINT,
	PRIMARY KEY (poc_pk_id),
	KEY (organization_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_sample
(
	particle_sample_pk_id BIGINT NOT NULL,
	particle_sample_name VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	primary_organization_pk_id BIGINT,
	PRIMARY KEY (particle_sample_pk_id),
	UNIQUE (particle_sample_name),
	UNIQUE (particle_sample_pk_id),
	KEY (primary_organization_pk_id)
) TYPE=InnoDB
;


CREATE TABLE nanoparticle_entity_file
(
	nanoparticle_entity_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (nanoparticle_entity_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (nanoparticle_entity_pk_id)
) TYPE=InnoDB
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


CREATE TABLE keyword_file
(
	keyword_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (keyword_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (keyword_pk_id)
) TYPE=InnoDB
;


CREATE TABLE instrument_config
(
	instrument_config_pk_id BIGINT NOT NULL,
	description TEXT,
	instrument_pk_id BIGINT NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (instrument_config_pk_id),
	KEY (instrument_pk_id)
) TYPE=InnoDB
;


CREATE TABLE functionalizing_entity_file
(
	functionalizing_entity_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (functionalizing_entity_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (functionalizing_entity_pk_id)
) TYPE=InnoDB
;


CREATE TABLE derived_bioassay_data
(
	derived_bioassay_data_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT,
	file_pk_id BIGINT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (derived_bioassay_data_pk_id),
	KEY (file_pk_id),
	KEY (characterization_pk_id)
) TYPE=InnoDB
;


CREATE TABLE chemical_association
(
	chemical_association_pk_id BIGINT NOT NULL,
	composition_pk_id BIGINT,
	associated_element_a_pk_id BIGINT NOT NULL,
	associated_element_b_pk_id BIGINT NOT NULL,
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	discriminator VARCHAR(200),
	attachment_bond_type VARCHAR(200),
	other_chemical_association_type VARCHAR(200),
	PRIMARY KEY (chemical_association_pk_id),
	KEY (associated_element_a_pk_id),
	KEY (associated_element_b_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;


CREATE TABLE associated_file
(
	associated_file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (associated_file_pk_id),
	KEY (associated_file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE protocol
(
	protocol_pk_id BIGINT NOT NULL,
	protocol_name VARCHAR(2000),
	protocol_type VARCHAR(2000),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (protocol_pk_id),
	UNIQUE (protocol_pk_id)
) TYPE=InnoDB
;


CREATE TABLE organization
(
	organization_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	streetAddress1 VARCHAR(200),
	streetAddress2 VARCHAR(200),
	city VARCHAR(100),
	state VARCHAR(100),
	postal_code VARCHAR(10),
	country VARCHAR(100),
	created_date DATE NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (organization_pk_id),
	UNIQUE (name),
	UNIQUE (organization_pk_id)
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
	type VARCHAR(200),
	abbreviation VARCHAR(50),
	manufacturer VARCHAR(2000),
	PRIMARY KEY (instrument_pk_id),
	UNIQUE (instrument_pk_id)
) TYPE=InnoDB
;


CREATE TABLE file
(
	file_pk_id BIGINT NOT NULL,
	file_name VARCHAR(500),
	file_uri VARCHAR(500),
	version VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	title VARCHAR(500),
	description TEXT,
	file_type VARCHAR(200),
	is_uri_external TINYINT NOT NULL,
	PRIMARY KEY (file_pk_id)
) TYPE=InnoDB
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


CREATE TABLE author
(
	author_pk_id BIGINT NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	initial VARCHAR(10),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (author_pk_id),
	UNIQUE (author_pk_id)
) TYPE=InnoDB
;


CREATE TABLE associated_element
(
	associated_element_pk_id BIGINT NOT NULL,
	molecular_formula VARCHAR(200),
	molecular_formula_type VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	name VARCHAR(200),
	value DECIMAL(22,3),
	value_unit VARCHAR(200),
	PRIMARY KEY (associated_element_pk_id)
) TYPE=InnoDB
;


CREATE TABLE activation_method
(
	activation_method_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	activation_effect TEXT,
	PRIMARY KEY (activation_method_pk_id)
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

ALTER TABLE physical_state ADD CONSTRAINT FK_physical_state_characterization 
	FOREIGN KEY (physical_state_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE functionalizing_entity ADD CONSTRAINT FK_functionalizing_entity_activation_method 
	FOREIGN KEY (activation_method_pk_id) REFERENCES activation_method (activation_method_pk_id)
;

ALTER TABLE functionalizing_entity ADD CONSTRAINT FK_functionalizing_entity_associated_element 
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE composition_file ADD CONSTRAINT FK_composition_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE nanoparticle_sample_publication ADD CONSTRAINT FK_nanoparticle_sample_publication_publication 
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;

ALTER TABLE nanoparticle_sample_other_organization ADD CONSTRAINT FK_nanoparticle_sample_other_organization_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE nanoparticle_sample_other_organization ADD CONSTRAINT FK_nanoparticle_sample_other_organization_organization 
	FOREIGN KEY (organization_pk_id) REFERENCES organization (organization_pk_id)
;

ALTER TABLE derived_datum ADD CONSTRAINT FK_derived_datum_derived_bioassay_data 
	FOREIGN KEY (derived_bioassay_data_pk_id) REFERENCES derived_bioassay_data (derived_bioassay_data_pk_id)
;

ALTER TABLE composition ADD CONSTRAINT FK_Composition_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE chemical_association_file ADD CONSTRAINT FK_chemical_association_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE chemical_association_file ADD CONSTRAINT FK_chemical_association_file_chemical_association 
	FOREIGN KEY (chemical_association_pk_id) REFERENCES chemical_association (chemical_association_pk_id)
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

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_author 
	FOREIGN KEY (author_pk_id) REFERENCES author (author_pk_id)
;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_publication 
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;

ALTER TABLE publication ADD CONSTRAINT FK_publication_file 
	FOREIGN KEY (publication_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE protocol_file ADD CONSTRAINT FK_protocol_file_file 
	FOREIGN KEY (protocol_file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE protocol_file ADD CONSTRAINT FK_protocol_file_protocol 
	FOREIGN KEY (protocol_pk_id) REFERENCES protocol (protocol_pk_id)
;

ALTER TABLE point_of_contact ADD CONSTRAINT FK_point_of_contact_organization 
	FOREIGN KEY (organization_pk_id) REFERENCES organization (organization_pk_id)
;

ALTER TABLE nanoparticle_sample ADD CONSTRAINT FK_nanoparticle_sample_organization 
	FOREIGN KEY (primary_organization_pk_id) REFERENCES organization (organization_pk_id)
;

ALTER TABLE nanoparticle_entity_file ADD CONSTRAINT FK_nanoparticle_entity_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE nanoparticle_entity_file ADD CONSTRAINT FK_nanoparticle_entity_file_nanoparticle_entity 
	FOREIGN KEY (nanoparticle_entity_pk_id) REFERENCES nanoparticle_entity (nanoparticle_entity_pk_id)
;

ALTER TABLE keyword_nanoparticle_sample ADD CONSTRAINT FK_keyword_nanoparticle_sample_keyword 
	FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id)
;

ALTER TABLE keyword_nanoparticle_sample ADD CONSTRAINT FK_keyword_nanoparticle_sample_nanoparticle_sample 
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

ALTER TABLE keyword_file ADD CONSTRAINT FK_keyword_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE keyword_file ADD CONSTRAINT FK_keyword_file_keyword 
	FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id)
;

ALTER TABLE instrument_config ADD CONSTRAINT FK_instrument_config_instrument 
	FOREIGN KEY (instrument_pk_id) REFERENCES instrument (instrument_pk_id)
;

ALTER TABLE functionalizing_entity_file ADD CONSTRAINT FK_functionalizing_entity_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE functionalizing_entity_file ADD CONSTRAINT FK_functionalizing_entity_file_functionalizing_entity 
	FOREIGN KEY (functionalizing_entity_pk_id) REFERENCES functionalizing_entity (functionalizing_entity_pk_id)
;

ALTER TABLE derived_bioassay_data ADD CONSTRAINT FK_derived_bioassay_data_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE derived_bioassay_data ADD CONSTRAINT FK_derived_bioassay_data_characterization 
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_associated_element_a 
	FOREIGN KEY (associated_element_a_pk_id) REFERENCES associated_element (associated_element_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_associated_element_b 
	FOREIGN KEY (associated_element_b_pk_id) REFERENCES associated_element (associated_element_pk_id)
;
