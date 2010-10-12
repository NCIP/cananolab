-- ----------------------------------------------------------------------
-- SQL data bulk transfer script generated by the MySQL Migration Toolkit
-- ----------------------------------------------------------------------

-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

use cananolab;

insert into csm_protection_element(protection_element_name, protection_element_description, object_id, attribute, protection_element_type_id, application_id, update_date)
values ('sample', '', 'sample', '', NULL, 2, sysdate()),
  ('protocol', '', 'protocol', '', NULL, 2, sysdate()),
  ('nanoparticle', '', 'nanoparticle', '', NULL, 2, sysdate()),
  ('report', '', 'report', '', NULL, 2, sysdate());

insert into csm_protection_group(protection_group_name, protection_group_description, application_id, large_element_count_flag, update_date, parent_protection_group_id)
values ('sample', '', 2, 0, sysdate(), NULL),
  ('protocol', '', 2, 0, sysdate(), NULL),
  ('nanoparticle', '', 2, 0, sysdate(), NULL),
  ('report', '', 2, 0, sysdate(), NULL);

insert into csm_role(role_name, role_description, application_id, active_flag, update_date)
values ('CUR', 'create, update, read', 2, 1, sysdate()),
  ('CURD', 'create, update, read, delete', 2, 1, sysdate());

insert into csm_pg_pe(protection_group_id, protection_element_id, update_date)
select g.PROTECTION_GROUP_ID, e.PROTECTION_ELEMENT_ID, sysdate()
from csm_protection_group g, csm_protection_element e
where g.PROTECTION_GROUP_NAME=e.PROTECTION_ELEMENT_NAME
and g.PROTECTION_GROUP_NAME in ('sample', 'protocol', 'nanoparticle', 'report');

insert into csm_role_privilege(role_id, privilege_id, update_date)
select r.ROLE_ID, p.PRIVILEGE_ID, sysdate()
from csm_role r, csm_privilege p
where r.ROLE_NAME = 'CUR' and p.PRIVILEGE_NAME in ('CREATE', 'UPDATE', 'READ'); 

insert into csm_role_privilege(role_id, privilege_id, update_date)
select r.ROLE_ID, p.PRIVILEGE_ID, sysdate()
from csm_role r, csm_privilege p
where r.ROLE_NAME = 'CURD' and p.PRIVILEGE_NAME in ('CREATE', 'UPDATE', 'READ', 'DELETE'); 

DELETE from csm_protection_element
where protection_element_name in ('administration', 'search', 'remote search', 'submit', 'workflow', 'inventory', 'characterization');

DELETE from csm_protection_group
where protection_group_name in ('administration', 'search', 'remote search', 'submit', 'workflow', 'inventory', 'characterization');

DELETE from csm_user_group_role_pg 
where protection_group_id not in
(select protection_group_id
from csm_protection_group);

DELETE from csm_pg_pe
where protection_group_id not in
(select protection_group_id
from csm_protection_group);

DELETE from csm_pg_pe
where protection_element_id not in
(select protection_element_id
from csm_protection_element);

commit;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

-- End of script