declare

 v_pi_group number;
 v_admin_group number;
 v_researcher_group number;
 v_read_role number;
 v_delete_role number;
 
begin
  select group_id into v_pi_group 
from CSM_GROUP 
where group_name='&appowner'||'_PI';
    
   select group_id into v_admin_group 
from CSM_GROUP 
where group_name='&appowner'||'_Administrator';

   select group_id into v_researcher_group 
from CSM_GROUP 
where group_name='&appowner'||'_Researcher';

  select role_id into v_read_role
from CSM_ROLE 
where role_name='R';

    select role_id into v_delete_role
from CSM_ROLE 
where role_name='D';
 
    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_pi_group, v_read_role, PROTECTION_GROUP_ID, sysdate
from lab_file a, csm_protection_group b
where a.created_by='data_migration'
and to_char(a.FILE_PK_ID)=b.PROTECTION_GROUP_NAME;

    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_researcher_group, v_read_role, PROTECTION_GROUP_ID, sysdate
from lab_file a, csm_protection_group b
where a.created_by='data_migration'
and to_char(a.FILE_PK_ID)=b.PROTECTION_GROUP_NAME;

    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_admin_group, v_delete_role, PROTECTION_GROUP_ID, sysdate
from csm_protection_group b
where b.PROTECTION_GROUP_NAME='characterization';

end; 
  
