use canano;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE 
composition_temp(composition_pk_id BIGINT(20), particle_sample_pk_id BIGINT(20));

INSERT INTO composition_temp(particle_sample_pk_id)
SELECT DISTINCT ucase(particle_sample_pk_id) FROM canano.composition;

update composition_temp, canano.composition com
set composition_temp.composition_pk_id = com.composition_pk_id
where lcase(composition_temp.particle_sample_pk_id) = lcase(com.particle_sample_pk_id)
;

DELETE  FROM canano.composition;

INSERT INTO canano.composition (composition_pk_id, particle_sample_pk_id) 
SELECT distinct composition_pk_id, particle_sample_pk_id FROM composition_temp;

--DROP TABLE composition_temp;

SET FOREIGN_KEY_CHECKS = 1;