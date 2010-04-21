use ncl;

call update_table_keys(20000000);

update csm_user_group_role_pg
set role_id=1
where role_id=10;

update csm_user_group_role_pg
set role_id=5
where role_id=12;

update csm_user_group
set group_id=2
where group_id=66;

update csm_user_group_role_pg
set group_id=2
where group_id=66;

update csm_group
set group_id=12
where group_id=67;

update csm_user_group
set group_id=12
where group_id=67;

update csm_user_group_role_pg
set group_id=12
where group_id=67;

update csm_group
set group_id=1
where group_id=71;

update csm_user_group
set group_id=1
where group_id=71;

update csm_user_group_role_pg
set group_id=1
where group_id=71;

delete from csm_protection_element
where protection_element_name in ('sample', 'protocol', 'publication);

delete from csm_protection_group
where protection_group_name in ('sample', 'protocol', 'publication);

delete from csm_pg_pe
where protection_group_id in (30270, 30271, 30272);

update csm_user_group_role_pg
set protection_group_id=27
where protection_group_id=30270;

update csm_user_group_role_pg
set protection_group_id=28
where protection_group_id=30271;

update csm_user_group_role_pg
set protection_group_id=29
where protection_group_id=30272;

call update_csm_keys('csm_protection_element', 'protection_element_id', 30000);
call update_csm_keys('csm_pg_pe', 'protection_group_id', 30000);
call update_csm_keys('csm_pg_pe', 'protection_element_id', 30000);
call update_csm_keys('csm_pg_pe', 'pg_pe_id', 30000);
call update_csm_keys('csm_user_group_role_pg', 'user_group_role_pg_id', 130000);
call update_csm_keys('csm_group', 'group_id', 60);
call update_csm_keys('csm_user_group', 'group_id', 60);
call update_csm_keys('csm_user_group_role_pg', 'group_id', 60);
call update_csm_keys('csm_protection_group', 'protection_group_id', 30000);
call update_csm_keys('csm_user_group_role_pg', 'protection_group_id', 30000);

call update_csm_keys('csm_protection_element', 'protection_element_name', 20000000);
call update_csm_keys('csm_protection_element', 'object_id', 20000000);
call update_csm_keys('csm_protection_group', 'protection_group_name', 20000000);


