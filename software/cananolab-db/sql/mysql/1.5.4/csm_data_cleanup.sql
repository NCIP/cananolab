/* delete non public access for POC */
delete from csm_user_group_role_pg
where group_id=2 
and role_id=5 
and protection_group_id in
(select pg.protection_group_id
from csm_protection_group pg, point_of_contact poc
where pg.protection_group_name=poc.poc_pk_id);

/* delete non public access for organization */
delete from csm_user_group_role_pg
where group_id=2 
and role_id=5 
and protection_group_id in
(select pg.protection_group_id
from csm_protection_group pg, organization org
where pg.protection_group_name=org.organization_pk_id);
