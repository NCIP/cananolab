-- change derived_datum to datum, derived_bioassay to finding, add condition
CREATE TABLE datum
(
	datum_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	value DECIMAL(22,3) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	finding_pk_id BIGINT,
	file_pk_id BIGINT,
	PRIMARY KEY (datum_pk_id),
	KEY (file_pk_id),
	KEY (finding_pk_id)
) TYPE=InnoDB
;


CREATE TABLE datum_condition
(
	datum_pk_id BIGINT NOT NULL,
	condition_pk_id BIGINT NOT NULL,
	PRIMARY KEY (datum_pk_id, condition_pk_id),
	KEY (datum_pk_id),
	KEY (condition_pk_id)
) TYPE=InnoDB
;

CREATE TABLE finding
(
	finding_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	UNIQUE (finding_pk_id),
	KEY (characterization_pk_id)
) TYPE=InnoDB
;

CREATE TABLE finding_file
(
	finding_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (finding_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (finding_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_condition
(
	condition_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	property VARCHAR(200),
	value VARCHAR(200) NOT NULL,
	value_unit VARCHAR(200),
	value_type VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (condition_pk_id)
) TYPE=InnoDB
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_datum
	FOREIGN KEY (datum_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_experiment_condition
	FOREIGN KEY (condition_pk_id) REFERENCES experiment_condition (condition_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_finding
	FOREIGN KEY (finding_pk_id) REFERENCES finding (finding_pk_id)
;

ALTER TABLE finding ADD CONSTRAINT FK_finding_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE finding_file ADD CONSTRAINT FK_finding_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE finding_file ADD CONSTRAINT FK_finding_file_finding
	FOREIGN KEY (finding_pk_id) REFERENCES finding (finding_pk_id)
;

INSERT INTO datum(datum_pk_id, name, value, value_type, value_unit,created_by, created_date, finding_pk_id)
	SELECT datum_pk_id, datum_name, value, value_type, value_unit, created_by, created_date, derived_bioassay_data_pk_id
	FROM derived_datum dd
;

INSERT INTO finding(finding_pk_id, characterization_pk_id, created_date, created_by)
	SELECT derived_bioassay_data_pk_id, characterization_pk_id, created_date, created_by
	FROM derived_bioassay_data
;

INSERT INTO finding_file(finding_pk_id, file_pk_id)
SELECT derived_bioassay_data_pk_id, dd.file_pk_id
FROM derived_bioassay_data dd, file
WHERE dd.file_pk_id=file.file_pk_id
 and file.file_name is not null;
