-- migration to csm 4.1
source migration_to_csm_4.1.sql

-- remove adminstrator group, not needed anymore
delete from csm_user_group
where group_id in
(select group_id
from csm_group g
where g.group_name like '%_Administrator');

delete from csm_user_group_role_pg
where group_id in
(select group_id
from csm_group g
where g.group_name like '%_Administrator');

delete from csm_group
where group_name like '%_Administrator';

--update protection element and group 'nanoparticle' to be 'sample'
update csm_protection_element
set protection_element_name='sample'
where protection_element_name='nanoparticle';

update csm_protection_element
set object_id='sample'
where object_id='nanoparticle';

delete from csm_user_group_role_pg
where protection_group_id in
(select protection_group_id
from csm_protection_group
where protection_group_name='sample');

delete from csm_protection_group
where protection_group_name='sample';

update csm_protection_group
set protection_group_name='sample'
where protection_group_name='nanoparticle';

-- fix, some records exist in csm_protection_group not in csm_protection_element
INSERT INTO csm_protection_element (
	protection_element_name,
	object_id,
	application_id,
	update_date
)SELECT
	protection_group_name,
	protection_group_name,
	application_id,
	sysdate()
FROM csm_protection_group
where protection_group_name not in (
select protection_element_name from csm_protection_element
)
;
INSERT INTO csm_pg_pe (
	protection_group_id,
	protection_element_id,
	update_date
)SELECT
	g.protection_group_id,
	e.protection_element_id,
	sysdate()
FROM csm_protection_group g, csm_protection_element e
where  e.update_date = CURRENT_DATE()
and g.protection_group_name = e.protection_element_name
;

--trim trailing and leading spaces in protection_element_name and protection_group_name and object_id
UPDATE csm_protection_element
set protection_element_name=trim(protection_element_name);

UPDATE csm_protection_group
set protection_group_name=trim(protection_group_name);

UPDATE csm_protection_element
set object_id=trim(object_id);
