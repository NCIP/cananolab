CREATE TABLE study_sample
(
	study_pk_id BIGINT NOT NULL,
	sample_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, sample_pk_id),
	KEY (sample_pk_id),
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

CREATE TABLE study_characterization
(
	study_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NOT NULL,
	PRIMARY KEY (study_pk_id, characterization_pk_id),
	KEY (study_pk_id),
	KEY (characterization_pk_id)
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


CREATE TABLE study
(
	study_pk_id BIGINT NOT NULL,
	study_name VARCHAR(200) NOT NULL,
	study_title VARCHAR(200) NOT NULL,
	description TEXT,
	outcome TEXT,
	design_types TEXT,
	diseases TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	start_date DATETIME,
	end_date DATETIME,
	public_release_date DATETIME,
	submission_date DATETIME,
	study_type VARCHAR(200),
	primary_poc_pk_id BIGINT,
	parent_study_pk_id BIGINT,
	PRIMARY KEY (study_pk_id),
	UNIQUE (study_pk_id),
	KEY (primary_poc_pk_id),
	KEY (parent_study_pk_id)
)
;

ALTER TABLE study_sample ADD CONSTRAINT FK_study_sample_sample
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

ALTER TABLE study_sample ADD CONSTRAINT FK_study_sample_study
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_other_poc ADD CONSTRAINT FK_study_other_poc_point_of_contact
	FOREIGN KEY (poc_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE study_other_poc ADD CONSTRAINT FK_study_other_poc_study
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_characterization ADD CONSTRAINT FK_study_characterization_study
	FOREIGN KEY (study_pk_id) REFERENCES study (study_pk_id)
;

ALTER TABLE study_characterization ADD CONSTRAINT FK_study_characterization_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
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

ALTER TABLE study ADD CONSTRAINT FK_study_point_of_contact
	FOREIGN KEY (primary_poc_pk_id) REFERENCES point_of_contact (poc_pk_id)
;

ALTER TABLE study ADD CONSTRAINT FK_study_study
	FOREIGN KEY (parent_study_pk_id) REFERENCES study (study_pk_id)
;

