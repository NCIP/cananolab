use canano;

SET FOREIGN_KEY_CHECKS = 0;

-- keyword table
CREATE TABLE 
keyword_temp(keyword_pk_id BIGINT(20), name VARCHAR(100));

INSERT INTO keyword_temp(name)
SELECT DISTINCT ucase(name) FROM keyword;

update keyword_temp, keyword
set keyword_temp.keyword_pk_id = keyword.keyword_pk_id
where lcase(keyword_temp.name) = lcase(keyword.name)
;

-- keyword_nanoparticle_sample table
CREATE TABLE keyword_particle_temp AS SELECT * FROM keyword_nanoparticle_sample;

update keyword_particle_temp kpt, keyword_temp kt, keyword k
set kpt.keyword_pk_id = kt.keyword_pk_id
where  lcase(kt.name) = lcase(k.name)
and  kpt.keyword_pk_id = k.keyword_pk_id
;

DELETE FROM keyword_nanoparticle_sample;

INSERT INTO keyword_nanoparticle_sample(keyword_pk_id, particle_sample_pk_id) 
SELECT distinct keyword_pk_id,particle_sample_pk_id FROM keyword_particle_temp;

DROP TABLE keyword_particle_temp;

-- keyword_lab_file table
CREATE TABLE keyword_file_temp AS SELECT * FROM keyword_lab_file;

update keyword_file_temp kpt, keyword_temp kt, keyword k
set kpt.keyword_pk_id = kt.keyword_pk_id
where  lcase(kt.name) = lcase(k.name)
and  kpt.keyword_pk_id = k.keyword_pk_id
;

DELETE FROM keyword_lab_file;

INSERT INTO keyword_lab_file(keyword_pk_id, lab_file_pk_id) 
SELECT distinct keyword_pk_id,lab_file_pk_id FROM keyword_file_temp;

-- keyword table
DELETE FROM keyword;

INSERT INTO keyword(keyword_pk_id, name) 
SELECT distinct keyword_pk_id, name FROM keyword_temp;

DROP TABLE keyword_file_temp;
DROP TABLE keyword_temp;

SET FOREIGN_KEY_CHECKS = 1;
