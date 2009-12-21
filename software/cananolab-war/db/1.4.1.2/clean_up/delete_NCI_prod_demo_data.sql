use canano;

delete from csm_protection_element
where protection_element_name in
(select particle_sample_name
from nanoparticle_sample
where created_by='janedoe');

delete from csm_protection_element
where protection_element_name in
(select file_pk_id
from lab_file
where created_by='janedoe');

delete from csm_protection_group
where protection_group_name in
(select particle_sample_name
from nanoparticle_sample
where created_by='janedoe');

delete from csm_protection_group
where protection_group_name in
(select file_pk_id
from lab_file
where created_by='janedoe');

delete from csm_user_group_role_pg
where protection_group_id not in
(select protection_group_id
from csm_protection_group);

delete from csm_pg_pe
where protection_group_id not in
(select protection_group_id
from csm_protection_group);

delete from csm_group 
where group_name in ('CCNE', 'Demo');

delete from csm_user_group
where group_id not in
(select group_id
from csm_group);

delete from composition
where particle_sample_pk_id in
(select particle_sample_pk_id
from nanoparticle_sample
where created_by='janedoe');

delete from keyword_lab_file
where lab_file_pk_id in
(select file_pk_id 
from lab_file
where created_by='janedoe');

delete from nanoparticle_sample_publication
where particle_sample_pk_id in
(select particle_sample_pk_id
from nanoparticle_sample
where created_by='janedoe');

delete from nanoparticle_sample_publication 
where file_pk_id in
(select file_pk_id
from lab_file
where created_by='janedoe');

delete from author_publication
where publication_pk_id in
(select file_pk_id
from lab_file
where created_by='janedoe');

delete from publication
where publication_pk_id in
(select file_pk_id
from lab_file
where created_by='janedoe');

delete from lab_file
where created_by='janedoe';

delete from author
where created_by='janedoe';

delete from keyword_nanoparticle_sample
where particle_sample_pk_id in
(select particle_sample_pk_id 
from nanoparticle_sample
where created_by='janedoe');

delete from nanoparticle_sample
where created_by='janedoe';

delete from source
where organization_name in ('CCNE', 'Demo');

commit;