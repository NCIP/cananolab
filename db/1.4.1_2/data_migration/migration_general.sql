
USE canano;

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
ALTER TABLE canano.document_author RENAME canano.author;

ALTER TABLE canano.author
 CHANGE document_author_pk_id author_pk_id BIGINT(20) NOT NULL;
 
 ALTER TABLE canano.author
 ADD created_date DATETIME NOT NULL AFTER middle_initial;
 
 
 ALTER TABLE canano.author
 ADD created_by varchar(100) NOT NULL AFTER created_date;
 
 
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
object_id = 'document'
WHERE  protection_element_name = 'document';

UPDATE csm_protection_group
SET protection_group_name = 'publication' 
WHERE  protection_group_name = 'document';


--migrate and drop report table

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

 
ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
insert into `common_lookup`(`name`,`attribute`,`value`) values ('Publication','category','report');
ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;


-- End of script