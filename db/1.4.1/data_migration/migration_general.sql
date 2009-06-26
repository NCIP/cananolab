USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS nanoparticle_sample_publication;
CREATE TABLE nanoparticle_sample_publication
(
	particle_sample_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	INDEX particle_sample_pk_id (particle_sample_pk_id ASC),
	INDEX file_pk_id (file_pk_id ASC)
) TYPE=InnoDB
;

DROP TABLE IF EXISTS publication;
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

DROP TABLE IF EXISTS document_author;
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

DROP TABLE IF EXISTS author_publication;
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
WHERE  protection_element_name = 'report';

DELETE from csm_protection_element
WHERE protection_element_name = 'sample';

UPDATE csm_protection_group
SET protection_group_name = 'document'
WHERE  protection_group_name = 'report';

DELETE from csm_protection_group
WHERE protection_group_name = 'sample';


-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;



ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;


insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','peer review article');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','review article');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','book chapter');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','review');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','editorial');

insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','status','published');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','status','in press');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','status','submitted');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','status','in preparation');

insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','synthesis');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','characterization');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','cell line');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','animal');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','in vitro');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','in vivo');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','researchArea','clinical trials');


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;

-- increase the molecular_formula from VARCHAR(200) to VARCHAR(500)
ALTER TABLE canano.associated_element
 CHANGE molecular_formula molecular_formula VARCHAR(2000);

ALTER TABLE canano.surface_chemistry
 CHANGE molecular_formula molecular_formula VARCHAR(2000);

update csm_group
set group_name = replace(group_name,'_PI','_DataCurator')
where group_name like '%_PI';

ALTER TABLE canano.lab_file
DROP COLUMN file_extension,
DROP COLUMN comments;

-- End of script

