use canano;

drop table if exists canano.protection_group_tmp;

CREATE TABLE canano.protection_group_tmp (
   protection_group_name VARCHAR(100) NOT NULL
)
;


-- source
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct so.source_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	source so
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.source_pk_id = so.source_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct so.source_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	source so
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.source_pk_id = so.source_pk_id
;

-- composition
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct comp.composition_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct comp.composition_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
;

/*
-- duplicate error
SET FOREIGN_KEY_CHECKS = 0;
-- nanoparticle_entity
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct ne.nanoparticle_entity_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	nanoparticle_entity ne
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
;
SET FOREIGN_KEY_CHECKS = 1;
*/

-- associated_element from composing_element
-- since composing_element_pk_id = associated_element_pk_id
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct ce.composing_element_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	nanoparticle_entity ne,
	composing_element ce
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
;

-- associated_element from functionalizing_entity
-- since functionalizing_entity_pk_id = associated_element_pk_id
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct fe.functionalizing_entity_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct ce.composing_element_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	nanoparticle_entity ne,
	composing_element ce
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct fe.functionalizing_entity_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
;

-- chemical_association
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct che.chemical_association_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	chemical_association che
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND che.composition_pk_id = comp.composition_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct che.chemical_association_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	chemical_association che
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND che.composition_pk_id = comp.composition_pk_id
;

-- nano_function
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct fun.function_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe,
	nano_function fun
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
AND fun.functionalizing_entity_pk_id = fe.functionalizing_entity_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct fun.function_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe,
	nano_function fun
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
AND fun.functionalizing_entity_pk_id = fe.functionalizing_entity_pk_id
;

-- nano_function
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct fun.function_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	nanoparticle_entity ne,
	composing_element ce,
	nano_function fun
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
AND fun.composing_element_pk_id = ce.composing_element_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct fun.function_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	nanoparticle_entity ne,
	composing_element ce,
	nano_function fun
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
AND fun.composing_element_pk_id = ce.composing_element_pk_id
;

-- target
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct t.target_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe,
	nanoparticle_entity ne,
	composing_element ce,
	nano_function fun,
	target t
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
AND fun.functionalizing_entity_pk_id = fe.functionalizing_entity_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
AND fun.composing_element_pk_id = ce.composing_element_pk_id
AND t.targeting_function_pk_id = fun.function_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct t.target_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	composition comp,
	functionalizing_entity fe,
	nanoparticle_entity ne,
	composing_element ce,
	nano_function fun,
	target t
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = comp.particle_sample_pk_id
AND fe.composition_pk_id = comp.composition_pk_id
AND fun.functionalizing_entity_pk_id = fe.functionalizing_entity_pk_id
AND ne.composition_pk_id = comp.composition_pk_id
AND ne.nanoparticle_entity_pk_id = ce.nanoparticle_entity_pk_id
AND fun.composing_element_pk_id = ce.composing_element_pk_id
AND t.targeting_function_pk_id = fun.function_pk_id
;

-- activation_method
-- since activation_method_pk_id = nanofunction_pk_id, do not need to add


-- characterization
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct cha.characterization_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct cha.characterization_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
;

-- instrument_config
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct ic.instrument_config_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	instrument_config ic
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.instrument_config_pk_id = ic.instrument_config_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct ic.instrument_config_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	instrument_config ic
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.instrument_config_pk_id = ic.instrument_config_pk_id
;

-- instrument
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct ins.instrument_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	instrument_config ic,
	instrument ins
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.instrument_config_pk_id = ic.instrument_config_pk_id
AND ins.instrument_pk_id = ic.instrument_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct ins.instrument_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	instrument_config ic,
	instrument ins
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.instrument_config_pk_id = ic.instrument_config_pk_id
AND ins.instrument_pk_id = ic.instrument_pk_id
;

-- derived_bioassay_data
/*
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct dbd.derived_bioassay_data_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct dbd.derived_bioassay_data_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
;
*/

-- derived_datum
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct dd.datum_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd,
	derived_datum dd
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
AND dbd.derived_bioassay_data_pk_id = dd.derived_bioassay_data_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct dd.datum_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd,
	derived_datum dd
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
AND dbd.derived_bioassay_data_pk_id = dd.derived_bioassay_data_pk_id
;

-- lab_file
/*
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct lf.file_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd,
	lab_file lf
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
AND dbd.file_pk_id = lf.file_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct lf.file_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	derived_bioassay_data dbd,
	lab_file lf
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
AND dbd.file_pk_id = lf.file_pk_id
;
*/

-- protocol
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct pro.protocol_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	protocol_file pf,
	protocol pro
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = pf.protocol_file_pk_id
AND pro.protocol_pk_id = pf.protocol_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct pro.protocol_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	protocol_file pf,
	protocol pro
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = pf.protocol_file_pk_id
AND pro.protocol_pk_id = pf.protocol_pk_id
;

-- keyword nanoparticle sample
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct kw.keyword_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	keyword_nanoparticle_sample kns,
	keyword kw
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = kns.particle_sample_pk_id
AND kw.keyword_pk_id = kns.keyword_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct kw.keyword_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	keyword_nanoparticle_sample kns,
	keyword kw
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = kns.particle_sample_pk_id
AND kw.keyword_pk_id = kns.keyword_pk_id
;

-- keyword lab file
-- do not need to run

-- surface_chemistry
INSERT into csm_protection_group (
	protection_group_name,
	application_id,
	large_element_count_flag,
	update_date
)
SELECT
	distinct sc.surface_chemistry_pk_id,
	'2',
	'0',
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	surface_chemistry sc
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = sc.surface_pk_id
;

INSERT into protection_group_tmp (
	protection_group_name
)
SELECT
	distinct sc.surface_chemistry_pk_id
FROM csm_group g,
	csm_protection_group pg,
	csm_user_group_role_pg upg,
	nanoparticle_sample ns,
	characterization cha,
	surface_chemistry sc
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = sc.surface_pk_id
;

INSERT into csm_user_group_role_pg (
	group_id,
	role_id,
	protection_group_id,
	update_date
)
SELECT
	g.group_id,
	cr.role_id,
	pg.protection_group_id,
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	csm_role cr,
	protection_group_tmp tmp
WHERE pg.protection_group_name = tmp.protection_group_name
AND g.group_name = 'Public'
AND cr.role_name = 'R'
;

drop table protection_group_tmp;

