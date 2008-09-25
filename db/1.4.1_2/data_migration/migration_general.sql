USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
ALTER TABLE canano.document_author RENAME canano.author;

ALTER TABLE canano.author
CHANGE document_author_pk_id author_pk_id BIGINT(20) NOT NULL;
 
ALTER TABLE canano.author
ADD created_date DATETIME AFTER middle_initial; 
 
ALTER TABLE canano.author
ADD created_by varchar(100) AFTER created_date;

ALTER TABLE canano.author
 CHANGE middle_initial initial VARCHAR(10);
 
ALTER TABLE canano.publication
CHANGE start_page start_page VARCHAR(50),
CHANGE end_page end_page VARCHAR(50);

ALTER TABLE canano.author_publication
DROP FOREIGN KEY FK_author_publication_document_author,
DROP FOREIGN KEY FK_author_publication_publication,
CHANGE document_author_pk_id author_pk_id BIGINT(20) NOT NULL;
 
ALTER TABLE canano.author_publication
ADD CONSTRAINT FK_author_publication_author FOREIGN KEY (author_pk_id) REFERENCES canano.author (author_pk_id) ON UPDATE RESTRICT ON DELETE RESTRICT,
ADD CONSTRAINT FK_author_publication_publication FOREIGN KEY (publication_pk_id) REFERENCES canano.publication (publication_pk_id) ON UPDATE RESTRICT ON DELETE RESTRICT;

UPDATE csm_protection_element
SET protection_element_name = 'publication' ,
object_id = 'publication'
WHERE  protection_element_name = 'document';

UPDATE csm_protection_group
SET protection_group_name = 'publication' 

WHERE  protection_group_name = 'document';

--migrate and drop nanoparticle_sample_report table
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

drop table canano.nanoparticle_sample_report;

--migrate and drop report table
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


--add data to author.created_by and author.created_date
--larger author.id should has larger author.created_date
UPDATE canano.author author
SET author.created_by = 'DATA_MIGRATION',
author.created_date = ADDDATE(SYSDATE(), INTERVAL author.author_pk_id SECOND)
order by author.author_pk_id;

-- change to not null
ALTER TABLE canano.author
 CHANGE created_date created_date DATETIME NOT NULL;
 
 
ALTER TABLE canano.author
 CHANGE created_by created_by varchar(100) NOT NULL;
 
-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

 
ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','report');
ALTER TABLE canano.common_lookup
CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;


-- End of script