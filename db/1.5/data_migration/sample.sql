-- change nanoparticle_sample to sample
CREATE TABLE sample
(
	sample_pk_id BIGINT NOT NULL,
	sample_name VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	primary_contact_pk_id BIGINT,
	PRIMARY KEY (sample_pk_id),
	UNIQUE (sample_name),
	UNIQUE (sample_pk_id),
	KEY (primary_contact_pk_id)
) TYPE=InnoDB
;

ALTER TABLE composition DROP FOREIGN KEY FK_Composition_nanoparticle_sample;
ALTER TABLE composition CHANGE particle_sample_pk_id sample_pk_id BIGINT;
ALTER TABLE composition ADD CONSTRAINT FK_composition_sample FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id);

ALTER TABLE characterization DROP FOREIGN KEY FK_characterization_nanoparticle_sample;
ALTER TABLE characterization CHANGE particle_sample_pk_id sample_pk_id BIGINT;
ALTER TABLE characterization ADD CONSTRAINT FK_characterization_sample FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id);

ALTER TABLE nanoparticle_sample_publication RENAME sample_publication;
ALTER TABLE sample_publication
 DROP FOREIGN KEY FK_nanoparticle_sample_publication_nanoparticle_sample,
 DROP FOREIGN KEY FK_nanoparticle_sample_publication_publication,
 CHANGE particle_sample_pk_id sample_pk_id BIGINT NOT NULL,
 CHANGE file_pk_id publication_pk_id BIGINT  NOT NULL;

ALTER TABLE sample_publication
 ADD CONSTRAINT FK_sample_publication_sample FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id),
 ADD CONSTRAINT FK_sample_publication_publication FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id);

ALTER TABLE keyword_nanoparticle_sample RENAME keyword_sample;
ALTER TABLE keyword_sample
 DROP FOREIGN KEY FK_keyword_nanoparticle_sample_keyword,
 DROP FOREIGN KEY FK_keyword_nanoparticle_sample_nanoparticle_sample,
 CHANGE particle_sample_pk_id sample_pk_id BIGINT NOT NULL;
ALTER TABLE keyword_sample
 ADD CONSTRAINT FK_keyword_sample_keyword FOREIGN KEY (keyword_pk_id) REFERENCES keyword (keyword_pk_id),
 ADD CONSTRAINT FK_keyword_sample_sample FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id);

INSERT INTO sample (sample_pk_id, sample_name, created_date, created_by, primary_contact_pk_id)
 SELECT particle_sample_pk_id, particle_sample_name, created_date, created_by, source_pk_id FROM nanoparticle_sample;

--trim trailing and leading spaces in sample_name
UPDATE sample
set sample_name=trim(sample_name);

