USE canano;

CREATE TABLE study_sample
(
	study_pk_id BIGINT NOT NULL,
	sample_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, sample_pk_id),
	KEY (sample_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study_publication
(
	study_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, publication_pk_id),
	KEY (publication_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study_protocol
(
	study_pk_id BIGINT NOT NULL,
	protocol_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, protocol_pk_id),
	KEY (protocol_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study_other_poc
(
	study_pk_id BIGINT NOT NULL,
	poc_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, poc_pk_id),
	KEY (poc_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study_disease
(
	study_pk_id BIGINT NOT NULL,
	disease_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, disease_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study_animal_model
(
	study_pk_id BIGINT NOT NULL,
	animal_model_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, animal_model_pk_id),
	KEY (animal_model_pk_id),
	KEY (study_pk_id)
) 
;

CREATE TABLE study
(
	study_pk_id BIGINT NOT NULL,
	study_name VARCHAR(200) NOT NULL,
	study_type VARCHAR(200),
	design_method_description TEXT,
	analysis_conclusion TEXT,
	start_date DATETIME,
	end_date DATETIME,
	is_animal_study TINYINT NOT NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	primary_contact_pk_id BIGINT,
	PRIMARY KEY (study_pk_id),
	KEY (primary_contact_pk_id)
) 
;

ALTER TABLE study_sample ADD CONSTRAINT FK_study_sample_sample 
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

ALTER TABLE study_sample ADD CONSTRAINT FK_study_sample_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_publication ADD CONSTRAINT FK_study_publication_publication 
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;

ALTER TABLE study_publication ADD CONSTRAINT FK_study_publication_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_protocol ADD CONSTRAINT FK_study_protocol_protocol 
	FOREIGN KEY (protocol_pk_id) REFERENCES protocol (protocol_pk_id)
;

ALTER TABLE study_protocol ADD CONSTRAINT FK_study_protocol_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_other_poc ADD CONSTRAINT FK_study_other_poc_point_of_contact 
	FOREIGN KEY (poc_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE study_other_poc ADD CONSTRAINT FK_study_other_poc_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;
ALTER TABLE study_disease ADD CONSTRAINT FK_study_disease_study 
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

