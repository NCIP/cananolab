USE canano;

CREATE TABLE animal
(
	animal_pk_id BIGINT NOT NULL,
	gender VARCHAR(50) NOT NULL,
	age DECIMAL(10,10) NOT NULL,
	age_unit VARCHAR(50) NOT NULL,
	body_weight DECIMAL(10,3),
	body_weight_unit VARCHAR(50),
	behavior TEXT,
	caelmir_id BIGINT,
	description TEXT,
	disposition VARCHAR(200),
	disposition_date DATETIME,
	reproductive_behavior TEXT,
	other_id VARCHAR(200),
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	animal_model_pk_id BIGINT,
	treatment_group_pk_id BIGINT,
	PRIMARY KEY (animal_pk_id),
	KEY (treatment_group_pk_id)
) 
;

CREATE TABLE animal_model
(
	animal_model_pk_id BIGINT NOT NULL,
	strain_name VARCHAR(200) NOT NULL,
	age DECIMAL(10,10) NOT NULL,
	age_unit VARCHAR(50) NOT NULL,
	camod_id BIGINT,
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	animal_diet_pk_id BIGINT,
	organism_pk_id BIGINT,
	PRIMARY KEY (animal_model_pk_id),
	KEY (animal_diet_pk_id),
	KEY (organism_pk_id)
) 
;

CREATE TABLE organism
(
	organism_pk_id BIGINT NOT NULL,
	common_name VARCHAR(200) NOT NULL,
	ncbi_taxonomy_id VARCHAR(200),
	scientific_name VARCHAR(200),
	taxonomy_rank VARCHAR(200),
	PRIMARY KEY (organism_pk_id)
) 
;

CREATE TABLE animal_diet
(
	animal_diet_pk_id BIGINT NOT NULL,
	feed TEXT,
	feed_lot_number VARCHAR(200),
	water TEXT,
	is_diet_restricted VARCHAR(2),
	dietary_restriction TEXT,
	PRIMARY KEY (animal_diet_pk_id)
) 
;


CREATE TABLE treatment_group
(
	treatment_group_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	gender VARCHAR(50) NOT NULL,
	number_of_animals INTEGER NOT NULL,
	number_of_treatments INTEGER,
	age DECIMAL(10,10),
	age_unit VARCHAR(50),
	body_weight DECIMAL(10,3),
	body_weight_unit VARCHAR(50),
	description TEXT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	treatment_pk_id BIGINT,
	sample_pk_id BIGINT,
	dose_pk_id BIGINT,
	PRIMARY KEY (treatment_group_pk_id),
	KEY (dose_pk_id),
	KEY (sample_pk_id),
	KEY (treatment_pk_id)
) 
;

CREATE TABLE treatment
(
	treatment_pk_id BIGINT NOT NULL,
	administrative_route VARCHAR(200) NOT NULL,
	duration VARCHAR(200),
	sugery_type VARCHAR(200),
	regiment TEXT,
	description TEXT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	animal_model_pk_id BIGINT,
	PRIMARY KEY (treatment_pk_id),
	KEY (animal_model_pk_id)
) 
;

CREATE TABLE dose_component
(
	dose_component_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	description TEXT,
	dose_pk_id BIGINT,
	treatment_group_pk_id BIGINT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (dose_component_pk_id),
	KEY (dose_pk_id)
) 
;

CREATE TABLE dose
(
	dose_pk_id BIGINT NOT NULL,
	value DECIMAL(10,10) NOT NULL,
	value_unit VARCHAR(50) NOT NULL,
	volume DECIMAL(10,3),
	volume_unit VARCHAR(50),
	PRIMARY KEY (dose_pk_id)
) 
;

ALTER TABLE treatment_group ADD CONSTRAINT FK_treatment_group_dose 
	FOREIGN KEY (dose_pk_id) REFERENCES dose (dose_pk_id)
;

ALTER TABLE treatment_group ADD CONSTRAINT FK_treatment_group_sample 
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

ALTER TABLE treatment_group ADD CONSTRAINT FK_treatment_group_treatment 
	FOREIGN KEY (treatment_pk_id) REFERENCES treatment (treatment_pk_id)
;

ALTER TABLE treatment ADD CONSTRAINT FK_treatment_animal_model 
	FOREIGN KEY (animal_model_pk_id) REFERENCES animal_model (animal_model_pk_id)
;

ALTER TABLE dose_component ADD CONSTRAINT FK_dose_component_dose 
	FOREIGN KEY (dose_pk_id) REFERENCES dose (dose_pk_id)
;
ALTER TABLE treatment ADD CONSTRAINT FK_treatment_animal_model 
	FOREIGN KEY (animal_model_pk_id) REFERENCES animal_model (animal_model_pk_id)
;

ALTER TABLE study_animal_model ADD CONSTRAINT FK_study_animal_model_animal_model 
	FOREIGN KEY (animal_model_pk_id) REFERENCES animal_model (animal_model_pk_id)
;

ALTER TABLE study_animal_model ADD CONSTRAINT FK_study_animal_model_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE animal_model ADD CONSTRAINT FK_animal_model_animal_diet 
	FOREIGN KEY (animal_diet_pk_id) REFERENCES animal_diet (animal_diet_pk_id)
;

ALTER TABLE animal_model ADD CONSTRAINT FK_animal_model_organism 
	FOREIGN KEY (organism_pk_id) REFERENCES organism (organism_pk_id)
;
