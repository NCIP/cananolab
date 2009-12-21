SET FOREIGN_KEY_CHECKS = 0;

-- migrate image contrast agent Luc8 in cananolab composing_element
-- to canano biopolymer_f, nano_function and functionalizing_entity

drop table if exists canano.luc_tmp;

CREATE TABLE canano.luc_tmp (
   composing_element_pk_id BIGINT(20) NOT NULL,
   functionalizing_entity_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL,
   PRIMARY KEY (functionalizing_entity_pk_id)
)
;

ALTER TABLE canano.luc_tmp AUTO_INCREMENT = 1101; 

insert into canano.luc_tmp
(
	composing_element_pk_id
)
SELECT
	ce.composing_element_pk_id
FROM cananolab.composing_element ce
WHERE lcase(ce.chemical_name) = 'luc8'
;

insert into canano.functionalizing_entity
(
	functionalizing_entity_pk_id,
	composition_pk_id
)
SELECT
	tmp.functionalizing_entity_pk_id,
	ce.characterization_pk_id
FROM cananolab.composing_element ce,
	canano.luc_tmp tmp
WHERE lcase(ce.chemical_name) = 'luc8'
AND tmp.composing_element_pk_id = ce.composing_element_pk_id
;


-- associated element
-- use same conditions as the functionalizing entity
insert into canano.associated_element
(
	associated_element_pk_id,
	description,
	created_by,
	created_date,
	name
)
SELECT tmp.functionalizing_entity_pk_id,
	ce.description,
	'DATA_MIGRATION',
	SYSDATE(),
	ce.chemical_name
FROM cananolab.composing_element ce,
	canano.luc_tmp tmp
WHERE lcase(ce.chemical_name) = 'luc8'
AND tmp.composing_element_pk_id = ce.composing_element_pk_id
;

insert into canano.biopolymer_f
(
	biopolymer_pk_id,
	type
)
SELECT tmp.functionalizing_entity_pk_id,
	'protein'
FROM cananolab.composing_element ce,
	canano.luc_tmp tmp
WHERE lcase(ce.chemical_name) = 'luc8'
AND tmp.composing_element_pk_id = ce.composing_element_pk_id
;

 
ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) AUTO_INCREMENT not NULL;
 
ALTER TABLE canano.nano_function AUTO_INCREMENT = 601; 

insert into canano.nano_function
(
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT
	'ImagingFunction',
	tmp.functionalizing_entity_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.composing_element ce,
	canano.luc_tmp tmp
WHERE lcase(ce.chemical_name) = 'luc8'
AND tmp.composing_element_pk_id = ce.composing_element_pk_id
;


ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) NOT NULL;

drop table luc_tmp;


-- functionalizing entity for other image contrast agent
insert into canano.functionalizing_entity
(
	functionalizing_entity_pk_id,
	composition_pk_id
)
SELECT a.agent_pk_id,
	c14.composition_pk_id
FROM cananolab.agent a,
	cananolab.linkage l,
	cananolab.particle_function pf,
	canano.composition c14
WHERE a.agent_pk_id = l.linkage_pk_id
AND l.function_pk_id = pf.particle_function_pk_id
AND pf.nanoparticle_pk_id = c14.particle_sample_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

-- associated element
-- use same conditions as the functionalizing entity
insert into canano.associated_element
(
	associated_element_pk_id,
	description,
	created_by,
	created_date,
	name
)
SELECT a.agent_pk_id,
	a.description,
	'DATA_MIGRATION',
	SYSDATE(),
	a.name
FROM cananolab.agent a,
	cananolab.linkage l,
	cananolab.particle_function pf,
	canano.composition c14
WHERE a.agent_pk_id = l.linkage_pk_id
AND l.function_pk_id = pf.particle_function_pk_id
AND pf.nanoparticle_pk_id = c14.particle_sample_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

insert into canano.small_molecule
(
	small_molecule_pk_id,
	alternate_name
)
SELECT agent_pk_id,
	name
FROM cananolab.agent a,
	canano.functionalizing_entity fe14
WHERE a.agent_pk_id = fe14.functionalizing_entity_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

-- nano_function using cananolab.linkage.function_pk_id as function_pk_id

-- OtherFunction for functionalizing entity
insert into canano.nano_function
(
	function_pk_id,
	description,
	discriminator,
	functionalizing_entity_pk_id,
	other_function_type,
	created_by,
	created_date
)
SELECT
	l.function_pk_id,
	pf.description,
	'OtherFunction',
	fe.functionalizing_entity_pk_id,
	lcase(pf.type),
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	cananolab.agent a,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Reporting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

-- ImagingFunction for functionalizing entity
insert into canano.nano_function
(
	function_pk_id,
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT
	l.function_pk_id,
	pf.description,
	'ImagingFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	cananolab.agent a,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Imaging'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

-- TargetingFunction for functionalizing entity
insert into canano.nano_function
(
	function_pk_id,
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT
	l.function_pk_id,
	pf.description,
	'TargetingFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	cananolab.agent a,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Targeting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

-- TherapeuticFunction for functionalizing entity
insert into canano.nano_function
(
	function_pk_id,
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT
	l.function_pk_id,
	pf.description,
	'TherapeuticFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	cananolab.agent a,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Therapeutic'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

insert into canano.activation_method
(
	activation_method_pk_id,
	type
)
SELECT distinct  pf.particle_function_pk_id,
	pf.activation_method
FROM cananolab.particle_function pf,
	canano.nano_function nf14,
	cananolab.agent a,
	cananolab.linkage l
Where pf.activation_method is not null
and pf.activation_method != ""
and nf14.function_pk_id = pf.particle_function_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

update canano.functionalizing_entity fe14,
	canano.activation_method am14,
	cananolab.agent a,
	cananolab.linkage l
set fe14.activation_method_pk_id = am14.activation_method_pk_id
where fe14.functionalizing_entity_pk_id = l.linkage_pk_id
and am14.activation_method_pk_id = l.function_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
;

insert into canano.target
(
	target_pk_id,
	discriminator,
	name,
	description,
	targeting_function_pk_id,
	other_target_type,
	created_by,
	created_date
)
SELECT
	at.agent_target_pk_id,
	'OtherTarget',
	at.name,
	at.description,
	nf14.function_pk_id,
	'other',
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL at.list_index MINUTE)
FROM cananolab.agent_target at,
	canano.functionalizing_entity fe14,
	canano.nano_function nf14,
	cananolab.agent a,
	cananolab.linkage l
WHERE at.agent_pk_id = fe14.functionalizing_entity_pk_id
and nf14.functionalizing_entity_pk_id = fe14.functionalizing_entity_pk_id
and at.agent_pk_id = a.agent_pk_id
and a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
and lcase(at.discriminator) = 'other'
order by at.list_index
;

insert into canano.target
(
	target_pk_id,
	discriminator,
	name,
	description,
	targeting_function_pk_id,
	created_by,
	created_date
)
SELECT
	at.agent_target_pk_id,
	at.discriminator,
	at.name,
	at.description,
	nf14.function_pk_id,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL at.list_index MINUTE)
FROM cananolab.agent_target at,
	canano.functionalizing_entity fe14,
	canano.nano_function nf14,
	cananolab.agent a,
	cananolab.linkage l
WHERE at.agent_pk_id = fe14.functionalizing_entity_pk_id
and nf14.functionalizing_entity_pk_id = fe14.functionalizing_entity_pk_id
and lcase(at.discriminator) != 'other'
and at.agent_pk_id = a.agent_pk_id
and a.agent_pk_id = l.linkage_pk_id
AND a.discriminator = 'ImageContrastAgent'
order by at.list_index
;

SET FOREIGN_KEY_CHECKS = 1;