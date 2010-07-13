/* update to be CURD by curator */
update csm_user_group_role_pg
set role_id=5
where role_id=1
and group_id=2;

/* insert public data to be CURD by curator */
ALTER TABLE csm_user_group_role_pg
 CHANGE user_group_role_pg_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into csm_user_group_role_pg
(group_id, role_id, update_date, protection_group_id)
select distinct '2', '5', sysdate(), protection_group_id
from csm_user_group_role_pg
where role_id=1;

ALTER TABLE csm_user_group_role_pg
 CHANGE user_group_role_pg_id BIGINT(20) NOT NULL;