CREATE TABLE characterization_sample
(
	characterization_pk_id BIGINT NOT NULL,
	sample_pk_id BIGINT NOT NULL,
	PRIMARY KEY (characterization_pk_id, sample_pk_id),
	KEY (characterization_pk_id),
	KEY (sample_pk_id)
)
;

/* add new columns and drop constraint on sample_pk_id*/
ALTER TABLE canano.characterization
 DROP FOREIGN KEY FK_characterization_sample,
 ADD assay_name VARCHAR(200) AFTER assay_type,
 ADD start_date DATETIME,
 ADD end_date DATETIME,
 ADD other_char_category VARCHAR(200);

/* update data for the new columns */
UPDATE characterization
   SET assay_name = assay_type,
       start_date = characterization_date,
       other_char_category = other_char_assay_category;


ALTER TABLE characterization_sample ADD CONSTRAINT FK_characterization_sample_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE characterization_sample ADD CONSTRAINT FK_characterization_sample_sample
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;
