-- Replace the <<database_name>> with proper database name that is to be created.

USE canano;

-- update to csm 3.2
UPDATE csm_application SET csm_application.DECLARATIVE_FLAG='0';

ALTER TABLE csm_protection_element DROP COLUMN protection_element_type_id;
ALTER TABLE csm_protection_element ADD COLUMN protection_element_type VARCHAR(100);
ALTER TABLE csm_pg_pe MODIFY COLUMN update_date DATE;
ALTER TABLE csm_application MODIFY COLUMN application_name VARCHAR(255) NOT NULL;
ALTER TABLE csm_group MODIFY COLUMN group_name VARCHAR(255) NOT NULL;

ALTER TABLE csm_application ADD COLUMN database_url VARCHAR(100);
ALTER TABLE csm_application ADD COLUMN database_user_name VARCHAR(100);
ALTER TABLE csm_application ADD COLUMN database_password VARCHAR(100);
ALTER TABLE csm_application ADD COLUMN database_dialect VARCHAR(100);
ALTER TABLE csm_application ADD COLUMN database_driver VARCHAR(100);

UPDATE csm_pg_pe SET UPDATE_DATE=NULL;

-- upgrade to csm 4.0
CREATE TABLE csm_filter_clause ( 
	filter_clause_id BIGINT AUTO_INCREMENT  NOT NULL,
	class_name VARCHAR(100) NOT NULL,
	filter_chain VARCHAR(2000) NOT NULL,
	target_class_name VARCHAR (100) NOT NULL,
	target_class_attribute_name VARCHAR (100) NOT NULL,
	target_class_attribute_type VARCHAR (100) NOT NULL,
	target_class_alias VARCHAR (100),
	target_class_attribute_alias VARCHAR (100),
	generated_sql VARCHAR (4000) NOT NULL,
	application_id BIGINT NOT NULL,
	update_date DATE NOT NULL,
	PRIMARY KEY(filter_clause_id)	
)Type=InnoDB 
;
 
ALTER TABLE csm_protection_element ADD ( attribute_value VARCHAR(100) );
 
ALTER TABLE csm_protection_element ADD CONSTRAINT uq_pe_pe_name_attribute_value_app_id unique (object_id, attribute, attribute_value, application_id);

-- upgrade to csm 4.1
ALTER TABLE csm_user MODIFY COLUMN login_name VARCHAR(500);
ALTER TABLE csm_user ADD COLUMN migrated_flag BOOL NOT NULL DEFAULT 0;
ALTER TABLE csm_user ADD COLUMN premgrt_login_name VARCHAR(100) ;


ALTER TABLE csm_pg_pe MODIFY COLUMN update_date DATE DEFAULT '0000-00-00';

ALTER TABLE csm_role_privilege DROP COLUMN update_date;
ALTER TABLE csm_user_pe DROP COLUMN update_date;

ALTER TABLE csm_filter_clause CHANGE generated_sql generated_sql_user VARCHAR (4000) NOT NULL;
ALTER TABLE csm_filter_clause ADD COLUMN generated_sql_group VARCHAR (4000) NOT NULL;

-- update upt context name to be default value
update csm_application set application_name='csmupt' where application_name='caNanoLab-upt';
update csm_protection_element set protection_element_name='csmupt', object_id='csmupt' where protection_element_name='caNanoLab-upt';
