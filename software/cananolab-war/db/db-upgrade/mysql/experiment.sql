USE canano;

CREATE TABLE experiment_datum
(
	experiment_pk_id BIGINT NOT NULL,
	datum_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_pk_id, datum_pk_id),
	KEY (datum_pk_id),
	KEY (experiment_pk_id)
) 
;

CREATE TABLE sample_experiment
(
	sample_pk_id BIGINT NOT NULL,
	experiment_pk_id BIGINT NOT NULL,
	PRIMARY KEY (sample_pk_id, experiment_pk_id),
	KEY (experiment_pk_id),
	KEY (sample_pk_id)
) 
;

--alter
CREATE TABLE datum
(
	datum_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	value DECIMAL(22,3) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	file_pk_id BIGINT,
	sample_pk_id BIGINT,
	discriminator VARCHAR(200) NOT NULL,
	PRIMARY KEY (datum_pk_id),
	KEY (file_pk_id),
	KEY (sample_pk_id)
) TYPE=InnoDB
;

CREATE TABLE pharmacokinetics_parameter_material
(
	pk_parameter_pk_id BIGINT NOT NULL,
	material_pk_id BIGINT NOT NULL,
	PRIMARY KEY (pk_parameter_pk_id, material_pk_id),
	KEY (pk_parameter_pk_id),
	KEY (material_pk_id)
) 
;

--migrate from characterization
CREATE TABLE experiment
(
	experiment_pk_id BIGINT NOT NULL,
	design_method_description TEXT,
	analysis_conclusion TEXT,
	start_date DATETIME,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	discriminator VARCHAR(50) NOT NULL,
	surface_is_hydrophobic TINYINT,
	purity_type VARCHAR(200),
	invitro_cell_line TEXT,
	invitro_cell_type TEXT,
	invitro_assay_type VARCHAR(200),
	enzyme_induction_enzyme VARCHAR(200),
	other_char_assay_category VARCHAR(200),
	other_char_name VARCHAR(200),
	invivo_procedure_code VARCHAR(200),
	poc_pk_id BIGINT,
	protocol_pk_id BIGINT,
	PRIMARY KEY (experiment_pk_id),
	KEY (poc_pk_id),
	KEY (protocol_pk_id)
) TYPE=InnoDB
;

CREATE TABLE invivo_parameter_anatomic_site
(
	invivo_parameter_pk_id BIGINT NOT NULL,
	anatomic_site_pk_id BIGINT NOT NULL,
	PRIMARY KEY (invivo_parameter_pk_id, anatomic_site_pk_id),
	KEY (anatomic_site_pk_id),
	KEY (invivo_parameter_pk_id)
) 
;

--alter characterization_file
CREATE TABLE experiment_file
(
	experiment_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_pk_id, file_pk_id),
	KEY (experiment_pk_id),
	KEY (file_pk_id)
) 
;

--alter file
CREATE TABLE file
(
	file_pk_id BIGINT NOT NULL,
	file_name VARCHAR(500),
	file_uri VARCHAR(500),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	title VARCHAR(500),
	description TEXT,
	file_type VARCHAR(200),
	is_uri_external TINYINT NOT NULL,
	discriminator VARCHAR(200) NOT NULL,
	PRIMARY KEY (file_pk_id)
) TYPE=InnoDB
;

CREATE TABLE anatomic_site
(
	anatomic_site_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	concept_code VARCHAR(200),
	PRIMARY KEY (anatomic_site_pk_id)
) 
;

ALTER TABLE experiment_datum ADD CONSTRAINT FK_experiment_datum_datum 
	FOREIGN KEY (datum_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE experiment_datum ADD CONSTRAINT FK_experiment_datum_experiment 
	FOREIGN KEY (experiment_pk_id) REFERENCES experiment (experiment_pk_id)
;

ALTER TABLE sample_experiment ADD CONSTRAINT FK_sample_experiment_experiment 
	FOREIGN KEY (experiment_pk_id) REFERENCES experiment (experiment_pk_id)
;

ALTER TABLE sample_experiment ADD CONSTRAINT FK_sample_experiment_sample 
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_sample 
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

;

ALTER TABLE pharmacokinetics_parameter_material ADD CONSTRAINT FK_pharmacokinetics_parameter_material_datum 
	FOREIGN KEY (pk_parameter_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE pharmacokinetics_parameter_material ADD CONSTRAINT FK_pharmacokinetics_parameter_material_material 
	FOREIGN KEY (material_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE invivo_parameter_anatomic_site ADD CONSTRAINT FK_invivo_parameter_anatomic_site_anatomic_site 
	FOREIGN KEY (anatomic_site_pk_id) REFERENCES anatomic_site (anatomic_site_pk_id)
;

ALTER TABLE invivo_parameter_anatomic_site ADD CONSTRAINT FK_invivo_parameter_anatomic_site_datum 
	FOREIGN KEY (invivo_parameter_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE experiment_file ADD CONSTRAINT FK_experiment_file_experiment 
	FOREIGN KEY (experiment_pk_id) REFERENCES experiment (experiment_pk_id)
;

ALTER TABLE experiment_file ADD CONSTRAINT FK_experiment_file_file 
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

