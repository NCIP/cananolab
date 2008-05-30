SET FOREIGN_KEY_CHECKS = 0;

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