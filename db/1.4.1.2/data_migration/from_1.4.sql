USE canano;

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

CREATE TABLE publication
(
	publication_pk_id BIGINT NOT NULL,
	category VARCHAR(200) NOT NULL,
	publication_status VARCHAR(50) NOT NULL,
	pubmed_id BIGINT NULL,
	digital_object_id VARCHAR(200) NULL,
	journal_name VARCHAR(200) NULL,
	volume VARCHAR(50) NULL,
	start_page VARCHAR(50) NULL,
	end_page VARCHAR(50) NULL,
	year INTEGER NULL,
	research_area VARCHAR(200),
	PRIMARY KEY (publication_pk_id)
) TYPE=InnoDB
;

DROP TABLE IF EXISTS author;
CREATE TABLE author
(
	author_pk_id BIGINT NOT NULL,
	first_name VARCHAR(200) NOT NULL,
	last_name VARCHAR(200) NOT NULL,
	initial VARCHAR(10) NULL,
	created_date DATETIME NOT NULL,
	created_by varchar(100) NOT NULL,
	PRIMARY KEY (author_pk_id),
	UNIQUE (author_pk_id)
) TYPE=InnoDB
;

UPDATE csm_protection_element
SET protection_element_name = 'publication',
object_id = 'publication'
WHERE  protection_element_name = 'report';

UPDATE csm_protection_group
SET protection_group_name = 'publication'
WHERE  protection_group_name = 'report';

DELETE from csm_protection_element
WHERE protection_element_name = 'sample';

DELETE from csm_protection_group
WHERE protection_group_name = 'sample';


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;


insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','peer review article');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','book chapter');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','review');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','editorial');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','report');

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

update canano.common_lookup
set value = 'fluorescence'
where value = 'flurorescence'
;


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;

ALTER TABLE canano.associated_element
 CHANGE molecular_formula molecular_formula VARCHAR(2000);

ALTER TABLE canano.surface_chemistry
 CHANGE molecular_formula molecular_formula VARCHAR(2000);

update csm_group
set group_name = replace(group_name,'_PI','_DataCurator')
where group_name like '%_PI';

INSERT INTO canano.publication
(
	publication_pk_id,
	category,
	publication_status
)
SELECT
	report.report_pk_id,
	'report',
	'published'
FROM canano.report report;

drop table canano.report;

INSERT INTO canano.nanoparticle_sample_publication
(
	particle_sample_pk_id,
	file_pk_id
)
SELECT
	nsreport.particle_sample_pk_id,
	nsreport.file_pk_id
FROM canano.nanoparticle_sample_report nsreport
order by nsreport.particle_sample_pk_id;

ALTER TABLE nanoparticle_sample_publication ADD CONSTRAINT FK_nanoparticle_sample_publication_publication
	FOREIGN KEY (file_pk_id) REFERENCES publication (publication_pk_id)
;
ALTER TABLE nanoparticle_sample_publication ADD CONSTRAINT FK_nanoparticle_sample_publication_nanoparticle_sample
	FOREIGN KEY (particle_sample_pk_id) REFERENCES nanoparticle_sample (particle_sample_pk_id)
;

drop table canano.nanoparticle_sample_report;

DROP TABLE IF EXISTS author_publication;
CREATE TABLE author_publication
(
	author_pk_id BIGINT NOT NULL,
	publication_pk_id BIGINT NOT NULL,
	KEY (author_pk_id),
	KEY (publication_pk_id)
) TYPE=InnoDB
;

ALTER TABLE canano.author_publication
ADD CONSTRAINT FK_author_publication_author FOREIGN KEY (author_pk_id)
	REFERENCES canano.author (author_pk_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
ADD CONSTRAINT FK_author_publication_publication FOREIGN KEY (publication_pk_id)
	REFERENCES canano.publication (publication_pk_id) ON UPDATE RESTRICT ON DELETE RESTRICT
;

ALTER TABLE canano.publication
 ADD CONSTRAINT FK_publication_lab_file FOREIGN KEY (publication_pk_id)
 REFERENCES canano.lab_file (file_pk_id) ON UPDATE RESTRICT ON DELETE RESTRICT
;

update canano.functionalizing_entity fe, canano.activation_method am
set fe.activation_method_pk_id = null
where am.activation_method_pk_id = fe.activation_method_pk_id
and ( am.type = null or am.type = ' ' )
and ( am.activation_effect = null or am.activation_effect = ' ' )
;

delete from activation_method
where ( type = null or type = ' ' )
and (activation_effect = null or activation_effect = ' ' )
;

ALTER TABLE canano.lab_file
DROP COLUMN file_extension,
DROP COLUMN comments;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
