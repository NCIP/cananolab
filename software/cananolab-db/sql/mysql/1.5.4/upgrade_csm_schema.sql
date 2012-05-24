/* upgrade to CSM 4.1 */
ALTER TABLE csm_user MODIFY COLUMN login_name VARCHAR(500);
ALTER TABLE csm_user ADD COLUMN migrated_flag BOOL NOT NULL DEFAULT 0;
ALTER TABLE csm_user ADD COLUMN premgrt_login_name VARCHAR(100) ;


ALTER TABLE csm_pg_pe MODIFY COLUMN update_date DATE DEFAULT '0000-00-00';

ALTER TABLE csm_role_privilege DROP COLUMN update_date;
ALTER TABLE csm_user_pe DROP COLUMN update_date;

ALTER TABLE csm_filter_clause CHANGE generated_sql generated_sql_user VARCHAR (4000) NOT NULL;
ALTER TABLE csm_filter_clause ADD COLUMN generated_sql_group VARCHAR (4000) NOT NULL;

/* upgrade to CSM 4.2 */
CREATE TABLE IF NOT EXISTS csm_mapping (
  mapping_id bigint(20) NOT NULL auto_increment,
  application_id bigint(20) NOT NULL,
  object_name varchar(100) NOT NULL,
  attribute_name varchar(100) NOT NULL,
  object_package_name varchar(100),
  table_name varchar(100),
  table_name_group varchar(100),
  table_name_user varchar(100),
  view_name_group varchar(100),
  view_name_user varchar(100),
  active_flag tinyint(1) NOT NULL default '0',
  maintained_flag tinyint(1) NOT NULL default '0',	
  update_date date default '0000-00-00',
  PRIMARY KEY(mapping_id)
)Type=InnoDB
;

ALTER TABLE csm_mapping ADD CONSTRAINT fk_pe_application 
FOREIGN KEY (application_id) REFERENCES csm_application (application_id) ON DELETE CASCADE
;
ALTER TABLE csm_mapping
ADD CONSTRAINT uq_mp_obj_name_attri_name_app_id UNIQUE (object_name,attribute_name,application_id)
;
ALTER TABLE csm_protection_element ADD INDEX idx_obj_attr_app(object_id, attribute, application_id)
;