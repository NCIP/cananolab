/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

/* upgrade to CSM 4.2 */
CREATE TABLE IF NOT EXISTS csm_mapping (
  mapping_id BIGINT(20) NOT NULL auto_increment,
  application_id BIGINT(20) NOT NULL,
  object_name VARCHAR(100) NOT NULL,
  attribute_name VARCHAR(100) NOT NULL,
  object_package_name VARCHAR(100),
  table_name VARCHAR(100),
  table_name_group VARCHAR(100),
  table_name_user VARCHAR(100),
  view_name_group VARCHAR(100),
  view_name_user VARCHAR(100),
  active_flag TINYINT(1) NOT NULL DEFAULT '0',
  maintained_flag TINYINT(1) NOT NULL DEFAULT '0',	
  update_date DATE DEFAULT '0000-00-00',
  PRIMARY KEY(mapping_id)
)type=innodb
;

ALTER TABLE csm_mapping ADD CONSTRAINT fk_csm_mapping_application 
FOREIGN KEY (application_id) REFERENCES csm_application (application_id) ON DELETE CASCADE
;
ALTER TABLE csm_mapping
ADD CONSTRAINT uq_mp_obj_name_attri_name_app_id UNIQUE (object_name,attribute_name,application_id)
;
ALTER TABLE csm_protection_element ADD INDEX idx_obj_attr_app(object_id, attribute, application_id)
;

ALTER TABLE csm_application ADD COLUMN csm_version VARCHAR(20)
;