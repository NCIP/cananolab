SET FOREIGN_KEY_CHECKS = 0;

-- begin special case for image contrast agent: magnevist
-- functionalizing entity 
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
AND lcase(a.name) = 'magnevist'
;

insert into canano.associated_element
(
	associated_element_pk_id,
	description,
	created_by,
	created_date,
	name
)
SELECT agent_pk_id,
	description,
	'DATA_MIGRATION',
	SYSDATE(),
	name
FROM cananolab.agent
WHERE lcase(name) = 'magnevist'
;

-- ImagingFunction
ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) AUTO_INCREMENT not NULL;
 
insert into canano.nano_function
(
	description,
	discriminator,
	functionalizing_entity_pk_id,
	composing_element_pk_id,
	created_by,
	created_date
)
SELECT 
	pf.description,
	'ImagingFunction',
	l.linkage_pk_id,
	ce14.composing_element_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	cananolab.agent a,
	canano.composition c14,
	canano.nanoparticle_entity ne14,
	canano.composing_element ce14
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Imaging'
AND pf.nanoparticle_pk_id = c14.particle_sample_pk_id
AND ne14.composition_pk_id = c14.composition_pk_id
AND ne14.nanoparticle_entity_pk_id = ce14.nanoparticle_entity_pk_id
AND a.agent_pk_id = l.linkage_pk_id
AND lcase(a.name) = 'magnevist'
;

ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) NOT NULL;
 
insert into canano.small_molecule
(
	small_molecule_pk_id,
	alternate_name
)
SELECT agent_pk_id,
	lcase(name)
FROM cananolab.agent
WHERE lcase(name) = 'magnevist'
;

-- end special case for image contrast agent: magnevist

-- migrate image contrast agent Luc8 in cananolab composing_element
-- to canano biopolymer_f, nano_function and functionalizing_entity
insert into canano.biopolymer_f
(
	biopolymer_pk_id,
	type
)
SELECT ce.composing_element_pk_id,
	'protein'
FROM cananolab.composing_element ce
WHERE lcase(ce.chemical_name) = 'luc8'
;

insert into canano.functionalizing_entity
(
	functionalizing_entity_pk_id,
	composition_pk_id
)
SELECT ce.composing_element_pk_id,
	ce.characterization_pk_id
FROM cananolab.composing_element ce
WHERE lcase(ce.chemical_name) = 'luc8'
;

insert into canano.nano_function
(
	function_pk_id,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT 
	ce.composing_element_pk_id,
	'ImagingFunction',
	ce.composing_element_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.composing_element ce
WHERE lcase(ce.chemical_name) = 'luc8'
;

SET FOREIGN_KEY_CHECKS = 1;