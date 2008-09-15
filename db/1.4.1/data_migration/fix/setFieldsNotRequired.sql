
ALTER TABLE canano.publication
 CHANGE research_area research_area VARCHAR(200); 
 
ALTER TABLE canano.nanoparticle_sample_publication
 CHANGE particle_sample_pk_id particle_sample_pk_id BIGINT;