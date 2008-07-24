USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;


CREATE TABLE nanoparticle_sample_publication
(
	particle_sample_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	INDEX particle_sample_pk_id (particle_sample_pk_id ASC),
	INDEX file_pk_id (file_pk_id ASC)
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
	research_area VARCHAR(200) NOT NULL,
	PRIMARY KEY (publication_pk_id)
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


CREATE TABLE author_publication
(
	document_author_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	KEY (document_author_pk_id),
	KEY (publication_pk_id)
) 
;

ALTER TABLE nanoparticle_sample_publication ADD CONSTRAINT FK_nanoparticle_sample_publication_publication 
	FOREIGN KEY (file_pk_id) REFERENCES publication (publication_pk_id)
;


ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_document_author 
	FOREIGN KEY (document_author_pk_id) REFERENCES document_author (document_author_pk_id)
;

ALTER TABLE author_publication ADD CONSTRAINT FK_author_publication_publication 
	FOREIGN KEY (publication_pk_id) REFERENCES publication (publication_pk_id)
;


UPDATE csm_protection_element
SET protection_element_name = 'document' ,
object_id = 'document'
WHERE  protection_element_id = 'report'

DELETE csm_protection_element
WHERE protection_element_id = 'sample'

UPDATE csm_protection_group
SET protection_element_name = 'document' 
WHERE  protection_element_id = 'report'

DELETE csm_protection_group
WHERE protection_element_id = 'sample'


-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script

