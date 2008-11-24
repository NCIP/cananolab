use canano;

delete from composition
where particle_sample_pk_id in
(select particle_sample_pk_id
from nanoparticle_sample
where created_by='janedoe');

delete from keyword_lab_file
where file_pk_id in
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

delete from nanoparticle_sample
where created_by='janedoe';

delete from source
where organization_name in ('CCNE', 'Demo');

commit;