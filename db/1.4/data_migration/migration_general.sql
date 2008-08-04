SET FOREIGN_KEY_CHECKS = 0;

-- characterization
INSERT INTO canano.characterization (
	characterization_pk_id,
	source,
	description,
	identifier_name,
	created_date,
	created_by,
	protocol_file_pk_id,
	instrument_config_pk_id,
	discriminator,
	particle_sample_pk_id
	)
SELECT c.characterization_pk_id,
	c.source,
	c.description,
	c.identifier_name,
	c.created_date,
	c.created_by,
	c.protocol_file_pk_id,
	c.instrument_config_pk_id,
	c.discriminator,
	n.nanoparticle_pk_id
FROM cananolab.characterization c,
	cananolab.nanoparticle_char n
where c.characterization_pk_id = n.characterization_pk_id
AND c.name != 'Composition'
;

-- shape
insert into canano.shape (
	shape_pk_id,
	max_dimension,
	min_dimension,
	type,
	min_dimension_unit,
	max_dimension_unit)
SELECT shape_pk_id,
	max_dimension,
	min_dimension,
	type,
	min_dimension_unit,
	max_dimension_unit
FROM cananolab.shape
;

-- solubility --
insert into canano.solubility
(
	solubility_pk_id,
	solvent,
	critical_concentration,
	critical_concentration_unit,
	is_soluble
)
SELECT solubility_pk_id,
	solvent,
	critical_concentration,
	concentration_unit,
	is_soluble
FROM cananolab.solubility
;

INSERT INTO canano.surface_chemistry (
	surface_chemistry_pk_id,
	molecule_name,
	number_molecule,
	molecular_formula_type,
	surface_pk_id,
	created_by,
	created_date)
SELECT surface_chemistry_pk_id,
	molecule_name,
	number_molecule,
	molecular_formula_type,
	surface_pk_id,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL list_index MINUTE)
FROM cananolab.surface_chemistry
ORDER BY surface_pk_id, list_index
;

INSERT INTO canano.protocol_file
(
	protocol_file_pk_id,
	protocol_pk_id
)
SELECT protocol_file_pk_id,
	protocol_pk_id
FROM cananolab.protocol_file
;

-- protocol
INSERT INTO canano.protocol
(
	protocol_pk_id,
	protocol_name,
	protocol_type,
	created_by,
	created_date
)
SELECT p.protocol_pk_id,
	p.protocol_name,
	LOWER(p.protocol_type),
	if(lf.created_by IS NULL, 'DATA_MIGRATION', lf.created_by),
	if(lf.created_date IS NULL,SYSDATE(), lf.created_date)
FROM cananolab.protocol p,
cananolab.protocol_file pf,
cananolab.lab_file lf
WHERE lf.file_pk_id = pf.protocol_file_pk_id
and pf.protocol_pk_id = p.protocol_pk_id
;

INSERT INTO canano.lab_file
(
	file_pk_id,
	file_name,
	file_uri,
	file_extension,
	version,
	created_by,
	created_date,
	title,
	description,
	comments,
	file_type,
	is_uri_external
)
SELECT file_pk_id,
	file_name,
	file_uri,
	file_type_extension,
	version,
	if(created_by IS NULL, 'DATA_MIGRATION', created_by),
	if(created_date IS NULL,SYSDATE(), created_date),
	title,
	description,
	comments,
	type,
	0
FROM cananolab.lab_file
;

INSERT INTO canano.instrument
(
	instrument_pk_id,
	type,
	abbreviation,
	manufacturer
)
SELECT instrument_pk_id,
	type,
	abbreviation,
	manufacturer
FROM cananolab.instrument
;

INSERT INTO canano.instrument_config
(
	instrument_config_pk_id,
	description,
	instrument_pk_id,
	created_by,
	created_date
)
SELECT instrument_config_pk_id,
	description,
	instrument_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.instrument_config
;

INSERT INTO canano.derived_bioassay_data
(
	derived_bioassay_data_pk_id,
	characterization_pk_id,
	file_pk_id,
	created_by,
	created_date
)
SELECT
	derived_bioassay_data_pk_id,
	d.characterization_pk_id,
	derived_bioassay_data_pk_id,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL list_index MINUTE)
FROM cananolab.derived_bioassay_data d,
	cananolab.lab_file f,
	cananolab.characterization c
WHERE d.derived_bioassay_data_pk_id = f.file_pk_id
AND d.characterization_pk_id = c.characterization_pk_id
ORDER BY characterization_pk_id, list_index
;

-- surface table in 1.3 loaded into derived_datum table in 1.4

ALTER TABLE canano.derived_datum
 CHANGE datum_pk_id datum_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

ALTER TABLE canano.derived_datum AUTO_INCREMENT = 901;

INSERT INTO canano.derived_datum
(
	datum_name,
	value,
	value_unit,
	created_by,
	created_date,
	derived_bioassay_data_pk_id
)
SELECT 
	'surface area',
	surface_area,
	surface_area_unit,
	'DATA_MIGRATION',
	SYSDATE(),
	surface_pk_id
FROM cananolab.surface
where surface_area is not null
;

INSERT INTO canano.derived_datum
(
	datum_name,
	value,
	value_unit,
	created_by,
	created_date,
	derived_bioassay_data_pk_id
)
SELECT 
	'charge',
	charge,
	charge_unit,
	'DATA_MIGRATION',
	SYSDATE(),
	surface_pk_id
FROM cananolab.surface
where charge is not null
;

INSERT INTO canano.derived_datum
(
	datum_name,
	value,
	value_unit,
	created_by,
	created_date,
	derived_bioassay_data_pk_id
)
SELECT
	'zeta potential',
	zeta_potential,
	zeta_potential_unit,
	'DATA_MIGRATION',
	SYSDATE(),
	surface_pk_id
FROM cananolab.surface
where zeta_potential is not null
;

ALTER TABLE canano.derived_datum
 CHANGE datum_pk_id datum_pk_id BIGINT(20) NOT NULL;


INSERT INTO canano.derived_bioassay_data
(
	derived_bioassay_data_pk_id,
	characterization_pk_id,
	created_by,
	created_date
)
SELECT distinct derived_bioassay_data_pk_id,
	derived_bioassay_data_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM canano.derived_datum
;

-- migrate general datum data after migration of cananolab.surface table.
INSERT INTO canano.derived_datum
(
	datum_pk_id,
	datum_name,
	value,
	value_unit,
	created_by,
	created_date,
	derived_bioassay_data_pk_id
)
SELECT
	datum_pk_id,
	name,
	value,
	value_unit,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL d.list_index MINUTE),
 	derived_bioassay_data_pk_id
FROM cananolab.datum d
ORDER BY d.datum_pk_id, d.list_index
;

update  canano.derived_datum
set datum_name = 'molecular weight'
where datum_name = 'Molecular Weight';

update  canano.derived_datum
set datum_name = 'Z-average'
where datum_name = 'Z-Average';

-- morphology table in 1.3 loaded into physical_state table in 1.4
INSERT INTO canano.physical_state
(
	physical_state_pk_id,
	type
)
SELECT morphology_pk_id,
	type
FROM cananolab.morphology
;

update canano.characterization
set discriminator = 'PhysicalState'
where discriminator = 'Morphology'
;

-- keyword_bioassay_data table in 1.3 loaded into keyword_lab_file table in 1.4
INSERT INTO canano.keyword_lab_file
(
	keyword_pk_id,
	lab_file_pk_id
)
SELECT keyword_pk_id,
  derived_bioassay_data_pk_id
FROM cananolab.keyword_bioassay_data
;

-- report
insert into canano.report
(
	report_pk_id,
	category
)
SELECT report_pk_id,
	'report file'
FROM cananolab.report;

insert into canano.report
(
	report_pk_id,
	category
)
SELECT associated_file_pk_id,
	'associated file'
FROM cananolab.associated_file;


insert into canano.nanoparticle_sample_report
(
	particle_sample_pk_id,
	file_pk_id
)
SELECT sample_pk_id,
	file_pk_id
FROM cananolab.sample_report
;


insert into canano.nanoparticle_sample_report
(
	particle_sample_pk_id,
	file_pk_id
)
SELECT sample_pk_id,
	associated_file_pk_id
FROM cananolab.sample_associated_file;

-- nanoparticle_sample
insert into canano.nanoparticle_sample
(
	particle_sample_pk_id,
	particle_sample_name,
	created_date,
	created_by,
	source_pk_id
)
SELECT sample_pk_id,
	sample_name,
	created_date,
	created_by,
	source_pk_id
FROM cananolab.sample
;

-- source
insert into canano.source
(
	source_pk_id,
	organization_name,
	address,
	city,
	state,
	country,
	postal_code
)
SELECT source_pk_id,
	organization_name,
	address,
	city,
	state,
	country,
	postal_code
FROM cananolab.source
;

/*
-- sample_management_pk_id = sample_pk_id of 1.3
-- particle_sample_pk_id = sample_pk_id of 1.3
insert into canano.sample_management
(
	sample_management_pk_id,
	description,
	source_sample_id,
	lot_id,
	lot_description,
	general_comments,
	received_date,
	received_by,
	particle_sample_pk_id,
	created_by,
	created_date
)
SELECT sample_pk_id,
	description,
 	source_sample_id,
	lot_id,
	lot_description,
	general_comments,
	received_date,
	received_by,
	sample_pk_id,
	created_by,
	created_date
FROM cananolab.sample
;

insert into canano.sample_container
(
	sample_container_pk_id,
	container_type,
	name,
	barcode,
	quantity,
	quantity_unit,
	concentration,
	concentration_unit,
	volume,
	volume_unit,
	diluents_solvent,
	safety_precautions,
	storage_conditions,
	comments,
	created_date,
	created_by,
	sample_management_pk_id,
	discriminator
)
SELECT sample_container_pk_id,
	container_type,
	name,
	barcode,
	quantity,
	quantity_unit,
	concentration,
	concentration_unit,
	volume,
	volume_unit,
	diluents_solvent,
	safety_precautions,
	storage_conditions,
	comments,
	created_date,
	created_by,
	sample_pk_id,
	'SampleContainer'
FROM cananolab.sample_container
WHERE is_derived = 'false'
;

insert into canano.sample_container
(
	sample_container_pk_id,
	container_type,
	name,
	barcode,
	quantity,
	quantity_unit,
	concentration,
	concentration_unit,
	volume,
	volume_unit,
	diluents_solvent,
	safety_precautions,
	storage_conditions,
	comments,
	created_date,
	created_by,
	sample_management_pk_id,
	parent_sample_container_pk_id,
	discriminator
)
SELECT s.sample_container_pk_id,
	s.container_type,
	s.name,
	s.barcode,
	s.quantity,
	s.quantity_unit,
	s.concentration,
	s.concentration_unit,
	s.volume,
	s.volume_unit,
	s.diluents_solvent,
	s.safety_precautions,
	s.storage_conditions,
	s.comments,
	s.created_date,
	s.created_by,
	s.sample_pk_id,
	d.parent_container_id,
	'Aliquot'
FROM cananolab.sample_container s,
	cananolab.derived_sample_container d
WHERE s.is_derived = 'true'
AND s.sample_container_pk_id = d.sample_container_pk_id
;



insert into canano.storage
(
	storage_pk_id,
	storage_location,
	storage_type
)
SELECT 
 	storage_pk_id,
	storage_location,
	storage_type
FROM cananolab.storage
;

insert into canano.sample_container_storage
(
	sample_container_pk_id,
	storage_pk_id
)
SELECT 
 	sample_container_pk_id,
	storage_pk_id
FROM cananolab.container_storage_location
;

*/

-- table keyword_nanoparticle in 1.3
-- change to keyword_nanoparticle_sample in 1.4
insert into canano.keyword_nanoparticle_sample
(
	keyword_pk_id,
	particle_sample_pk_id
)
SELECT 
  keyword_pk_id,
  nanoparticle_pk_id
FROM cananolab.keyword_nanoparticle
;

-- particle entity
-- carbon nanotube
insert into canano.carbon_nanotube
(
	carbon_nanotube_pk_id,
	chirality,
	diameter,
	diameter_unit,
	average_length,
	average_length_unit,
	wall_type
)
SELECT cn_composition_pk_id,
	chirality,
	growth_diameter,
	'nm',
	average_length,
	'nm',
	wall_type
FROM cananolab.carbon_nanotube_composition
;

insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.cn_composition_pk_id,
	c.cn_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'CarbonNanotube'
FROM cananolab.carbon_nanotube_composition c,
	cananolab.characterization ca
WHERE c.cn_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.carbon_nanotube_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.cn_composition_pk_id = n.characterization_pk_id
;

-- Quantum Dot
insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT ca.characterization_pk_id,
	ca.characterization_pk_id,
	ca.created_by,
	ca.created_date,
	'QuantumDot'
FROM cananolab.characterization ca
WHERE ca.discriminator = 'QuantumDotComp'
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c
WHERE c.characterization_pk_id = n.characterization_pk_id
AND c.discriminator = 'QuantumDotComp'
;


-- metal particle
insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT ca.characterization_pk_id,
	ca.characterization_pk_id,
	ca.created_by,
	ca.created_date,
	'MetalParticle'
FROM cananolab.characterization ca
WHERE ca.discriminator = 'MetalComp'
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c
WHERE c.characterization_pk_id = n.characterization_pk_id
AND c.discriminator = 'MetalComp'
;

-- emulsion
insert into canano.emulsion
(
	emulsion_pk_id,
	polymer_name,
	is_polymerized
)
SELECT e_composition_pk_id,
	polymer_name,
	is_polymerized
FROM cananolab.emulsion_composition
;

insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.e_composition_pk_id,
	c.e_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'Emulsion'
FROM cananolab.emulsion_composition c,
	cananolab.characterization ca
WHERE c.e_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.emulsion_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.e_composition_pk_id = n.characterization_pk_id
;

-- dendrimer
insert into canano.dendrimer
(
	dendrimer_pk_id,
	generation,
	branch
)
SELECT d_composition_pk_id,
	generation,
	branch
FROM cananolab.dendrimer_composition
;

insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.d_composition_pk_id,
	c.d_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'Dendrimer'
FROM cananolab.dendrimer_composition c,
	cananolab.characterization ca
WHERE c.d_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.dendrimer_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.d_composition_pk_id = n.characterization_pk_id
;


-- liposome
insert into canano.liposome
(
	liposome_pk_id,
	is_polymerized,
	polymer_name
)
SELECT l_composition_pk_id,
	is_polymerized,
	polymer_name
FROM cananolab.liposome_composition
;

insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.l_composition_pk_id,
	c.l_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'Liposome'
FROM cananolab.liposome_composition c,
	cananolab.characterization ca
WHERE c.l_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.liposome_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.l_composition_pk_id = n.characterization_pk_id
;

-- polymer
insert into canano.polymer
(
	polymer_pk_id,
	is_cross_linked,
	cross_link_degree,
	initiator
)
SELECT p_composition_pk_id,
	is_cross_link,
	cross_link_degree,
	initiator
FROM cananolab.polymer_composition
;

insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.p_composition_pk_id,
	c.p_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'Polymer'
FROM cananolab.polymer_composition c,
	cananolab.characterization ca
WHERE c.p_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.polymer_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.p_composition_pk_id = n.characterization_pk_id
;

-- fullerene
insert into canano.fullerene
(
	fullerene_pk_id,
	number_of_carbon
)
SELECT f_composition_pk_id,
	number_of_carbon
FROM cananolab.fullerene_composition
;


insert into canano.nanoparticle_entity
(
	nanoparticle_entity_pk_id,
	composition_pk_id,
	created_by,
	created_date,
	discriminator
)
SELECT c.f_composition_pk_id,
	c.f_composition_pk_id,
	ca.created_by,
	ca.created_date,
	'Fullerene'
FROM cananolab.fullerene_composition c,
	cananolab.characterization ca
WHERE c.f_composition_pk_id = ca.characterization_pk_id
;

insert into canano.composition
(
	composition_pk_id,
	particle_sample_pk_id
)
SELECT c.characterization_pk_id,
	n.nanoparticle_pk_id
FROM cananolab.nanoparticle_char n,
	cananolab.characterization c,
	cananolab.fullerene_composition cnc
WHERE c.characterization_pk_id = n.characterization_pk_id
AND cnc.f_composition_pk_id = n.characterization_pk_id
;

-- composing element
insert into canano.composing_element
(
	composing_element_pk_id,
	element_type,
	nanoparticle_entity_pk_id
)
SELECT ce.composing_element_pk_id,
	ce.element_type,
	ce.characterization_pk_id
FROM cananolab.composing_element ce,
	cananolab.characterization c
WHERE ce.characterization_pk_id = c.characterization_pk_id
AND c.discriminator != 'ComplexComp'
AND ce.element_type != 'image contrast agent'
ORDER BY ce.composing_element_pk_id, ce.list_index
;

-- associated element
insert into canano.associated_element
(
	associated_element_pk_id,
	description,
	created_by,
	created_date,
	name
)
SELECT ce.composing_element_pk_id,
	ce.description,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL ce.list_index MINUTE),
	ce.chemical_name
FROM cananolab.composing_element ce,
	cananolab.characterization c
WHERE ce.characterization_pk_id = c.characterization_pk_id
AND c.discriminator != 'ComplexComp'
ORDER BY ce.characterization_pk_id, ce.list_index
;

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
AND a.discriminator != 'Other'
AND a.discriminator != 'ImageContrastAgent'
AND a.agent_pk_id in (select a2.agent_pk_id
		from cananolab.agent a2,
			cananolab.linkage l2,
			cananolab.particle_function pf2,
			canano.composition c142
		WHERE a2.agent_pk_id = l2.linkage_pk_id
		AND l2.function_pk_id = pf2.particle_function_pk_id
		AND pf2.nanoparticle_pk_id = c142.particle_sample_pk_id
		AND a2.discriminator != 'Other'
		AND a2.discriminator != 'ImageContrastAgent'
		group by a2.agent_pk_id
		having count(a2.agent_pk_id) = 1
)
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
	ADDDATE(SYSDATE(), INTERVAL ROUND(RAND() * 100) SECOND),
	a.name
FROM cananolab.agent a,
	cananolab.linkage l,
	cananolab.particle_function pf,
	canano.composition c14
WHERE a.agent_pk_id = l.linkage_pk_id
AND l.function_pk_id = pf.particle_function_pk_id
AND pf.nanoparticle_pk_id = c14.particle_sample_pk_id
AND a.discriminator != 'Other'
AND a.discriminator != 'ImageContrastAgent'
AND a.agent_pk_id in (select a2.agent_pk_id
		from cananolab.agent a2,
			cananolab.linkage l2,
			cananolab.particle_function pf2,
			canano.composition c142
		WHERE a2.agent_pk_id = l2.linkage_pk_id
		AND l2.function_pk_id = pf2.particle_function_pk_id
		AND pf2.nanoparticle_pk_id = c142.particle_sample_pk_id
		AND a2.discriminator != 'Other'
		AND a2.discriminator != 'ImageContrastAgent'
		group by a2.agent_pk_id
		having count(a2.agent_pk_id) = 1
)
;

insert into canano.antibody
(
	antibody_pk_id,
	species
)
SELECT agent_pk_id,
	lcase(other)
FROM cananolab.agent a,
	canano.functionalizing_entity fe14
WHERE discriminator = 'Antibody'
and a.agent_pk_id = fe14.functionalizing_entity_pk_id
;

insert into canano.biopolymer_f
(
	biopolymer_pk_id,
	type,
	sequence
)
SELECT agent_pk_id,
	discriminator,
	sequence
FROM cananolab.agent a,
	canano.functionalizing_entity fe14
WHERE (discriminator = 'DNA'
OR	discriminator = 'RNA')
and a.agent_pk_id = fe14.functionalizing_entity_pk_id
;

insert into canano.biopolymer_f
(
	biopolymer_pk_id,
	type,
	sequence
)
SELECT agent_pk_id,
	lcase(discriminator),
	sequence
FROM cananolab.agent a,
	canano.functionalizing_entity fe14
WHERE (lcase(discriminator) = 'protein'
OR lcase(discriminator) = 'peptide')
and a.agent_pk_id = fe14.functionalizing_entity_pk_id
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
WHERE lcase(discriminator) = 'smallmolecule'
and a.agent_pk_id = fe14.functionalizing_entity_pk_id
;

-- what default value for type?
insert into canano.other_functionalizing_entity
(
	other_func_entity_pk_id,
	type
)
SELECT agent_pk_id,
	other
FROM cananolab.agent a,
	canano.functionalizing_entity fe14
WHERE lcase(discriminator) = 'other'
AND other is not null
and a.agent_pk_id = fe14.functionalizing_entity_pk_id
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
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Reporting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
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
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Imaging'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
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
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Targeting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
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
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Therapeutic'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
;

/*
ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) AUTO_INCREMENT not NULL;
 
ALTER TABLE canano.nano_function AUTO_INCREMENT = 601; 

-- OtherFunction for functionalizing entity
insert into canano.nano_function
(
	description,
	discriminator,
	functionalizing_entity_pk_id,
	other_function_type,
	created_by,
	created_date
)
SELECT 
	pf.description,
	'OtherFunction',
	fe.functionalizing_entity_pk_id,
	lcase(pf.type),
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Reporting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
;

-- ImagingFunction for functionalizing entity
insert into canano.nano_function
(
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT 
	pf.description,
	'ImagingFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Diagnostic Imaging'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
;

-- TargetingFunction for functionalizing entity
insert into canano.nano_function
(
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT 
	pf.description,
	'TargetingFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Targeting'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
;

-- TherapeuticFunction for functionalizing entity
insert into canano.nano_function
(
	description,
	discriminator,
	functionalizing_entity_pk_id,
	created_by,
	created_date
)
SELECT 
	pf.description,
	'TherapeuticFunction',
	l.linkage_pk_id,
	'DATA_MIGRATION',
	SYSDATE()
FROM cananolab.particle_function pf,
	cananolab.linkage l,
	canano.functionalizing_entity fe
Where pf.particle_function_pk_id = l.function_pk_id
and pf.type = 'Therapeutic'
AND fe.functionalizing_entity_pk_id = l.linkage_pk_id
;

ALTER TABLE canano.nano_function
 CHANGE function_pk_id function_pk_id BIGINT(20) NOT NULL;
*/

-- create a row in composing_element table with element_type = 'repeat unit'
-- create corresponding row in associated_element table with
-- associated_element.name = content of the column of the dendrimer_composition.repeat_unit

-- NOTE: --
-- insert dendrimer 'repeat unit' after populate the FUNCTION table
-- otherwise, duplicated particle_function_pk_id maybe created
ALTER TABLE canano.composing_element
  CHANGE composing_element_pk_id composing_element_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

ALTER TABLE canano.composing_element AUTO_INCREMENT = 301;
 
insert into canano.composing_element
(
	element_type,
	nanoparticle_entity_pk_id
)
SELECT
	'repeat unit',
	ce.characterization_pk_id
FROM cananolab.composing_element ce,
	cananolab.dendrimer_composition cnc
WHERE cnc.d_composition_pk_id = ce.characterization_pk_id
and cnc.repeat_unit is not null
and cnc.repeat_unit != ""
;

ALTER TABLE canano.composing_element
 CHANGE composing_element_pk_id composing_element_pk_id BIGINT(20) NOT NULL;

insert into canano.associated_element
(
	associated_element_pk_id,
	created_by,
	created_date,
	name
)
SELECT ce14.composing_element_pk_id,
	'DATA_MIGRATION',
	ADDDATE(SYSDATE(), INTERVAL ROUND(RAND() * 100) SECOND),
	cnc.repeat_unit
FROM cananolab.dendrimer_composition cnc,
	canano.composing_element ce14
WHERE ce14.element_type = 'repeat unit'
AND	cnc.d_composition_pk_id = ce14.nanoparticle_entity_pk_id
;

insert into canano.activation_method
(
	activation_method_pk_id,
	type
)
SELECT distinct  pf.particle_function_pk_id,
	pf.activation_method
FROM cananolab.particle_function pf,
	canano.nano_function nf14
Where pf.activation_method is not null
and pf.activation_method != ""
and nf14.function_pk_id = pf.particle_function_pk_id
;

update canano.functionalizing_entity fe14,
	canano.activation_method am14,
	cananolab.linkage l
set fe14.activation_method_pk_id = am14.activation_method_pk_id
where fe14.functionalizing_entity_pk_id = l.linkage_pk_id
and am14.activation_method_pk_id = l.function_pk_id
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
	canano.nano_function nf14
WHERE at.agent_pk_id = fe14.functionalizing_entity_pk_id
and nf14.functionalizing_entity_pk_id = fe14.functionalizing_entity_pk_id
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
	canano.nano_function nf14
WHERE at.agent_pk_id = fe14.functionalizing_entity_pk_id
and nf14.functionalizing_entity_pk_id = fe14.functionalizing_entity_pk_id
and lcase(at.discriminator) != 'other'
order by at.list_index
;

insert into canano.hibernate_unique_key (
  next_hi
)
SELECT next_hi
FROM cananolab.hibernate_unique_key
;

use canano;

-- remove duplicated keyword name from cananolab.keyword table
-- keyword table
CREATE TABLE 
keyword_temp(keyword_pk_id BIGINT(20), name VARCHAR(100));

INSERT INTO keyword_temp(name)
SELECT DISTINCT ucase(name) FROM cananolab.keyword;

update keyword_temp, cananolab.keyword labk
set keyword_temp.keyword_pk_id = labk.keyword_pk_id
where lcase(keyword_temp.name) = lcase(labk.name)
;

-- keyword_nanoparticle_sample table
CREATE TABLE keyword_particle_temp AS SELECT * FROM keyword_nanoparticle_sample;

update keyword_particle_temp kpt, keyword_temp kt, cananolab.keyword k
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

update keyword_file_temp kpt, keyword_temp kt, cananolab.keyword k
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


-- remove the leading '/' if there is one in the file_uri
update canano.lab_file
set file_uri = substring(file_uri, 2)
where left(file_uri, 1) = '/'
;

-- remove first two records in the canano.lab_file table
delete from canano.lab_file
where file_pk_id in (1, 2)
;


-- change element_type from 'coating' to 'coat' in the composing_element table
update canano.composing_element
set element_type = 'coat'
where element_type = 'coating'
;

-- change element_type from 'modification' to 'modifier' in the composing_element table
update canano.composing_element
set element_type = 'modifier'
where element_type = 'modification'
;

-- common_lookup table for user defined "other" types

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

-- def_activation_method
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'ActivationMethod',
	'otherType',
	def.name
FROM cananolab.def_activation_method def
WHERE activation_method_pk_id > 30
;

-- def_bond_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'Attachment',
	'otherBondType',
	def.name
FROM cananolab.def_bond_type def
WHERE bond_type_pk_id > 30
;

-- def_cellline_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'Cytotoxicity',
	'otherCellLine',
	def.name
FROM cananolab.def_cellline_type def
WHERE cellline_type_pk_id > 30
;

-- def_characterization_file_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'LabFile',
	'otherType',
	def.name
FROM cananolab.def_characterization_file_type def
WHERE file_type_pk_id > 30
;

-- def_composing_element_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'ComposingElement',
	'otherType',
	def.name
FROM cananolab.def_composing_element_type def
WHERE composing_element_type_pk_id > 30
;

-- def_datum_name
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	REPLACE(def.characterization_name, ' ', ''),
	'otherDerivedDatumName',
	def.name
FROM cananolab.def_datum_name def
WHERE datum_name_pk_id > 30
;

-- def_image_contrast_agent_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'ImagingFunction',
	'otherModality',
	def.name
FROM cananolab.def_image_contrast_agent_type def
WHERE agent_type_pk_id > 30
;

-- def_measure_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'DerivedDatum',
	'otherValueType',
	def.name
FROM cananolab.def_measure_type def
WHERE measure_type_pk_id > 30
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'SampleContainer',
	'otherConcentrationUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Concentration'
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'SampleContainer',
	'otherQuantityUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Quantity'
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'SampleContainer',
	'otherVolumeUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Volume'
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'molecular weight',
	'otherUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Molecular Weight'
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'charge',
	'otherUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Charge'
;

-- def_measure_unit
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'surface area',
	'otherUnit',
	def.unit_name
FROM cananolab.def_measure_unit def
WHERE measure_unit_pk_id > 30
AND def.unit_type = 'Area'
;

-- def_molecular_formula_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'SurfaceChemistry',
	'otherMolecularFormulaType',
	def.name
FROM cananolab.def_molecular_formula_type def
WHERE molecular_formula_type_pk_id > 30
;

-- def_morphology_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'PhysicalState',
	'otherType',
	def.name
FROM cananolab.def_morphology_type def
WHERE morphology_type_pk_id > 30
;

-- def_protocol_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'Protocol',
	'otherType',
	def.name
FROM cananolab.def_protocol_type def
WHERE protocol_type_pk_id > 30
;

-- def_shape_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'Shape',
	'otherType',
	def.name
FROM cananolab.def_shape_type def
WHERE shape_type_pk_id > 30
;

-- def_solvent_type
INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
SELECT
	'Solubility',
	'otherSolventType',
	def.name
FROM cananolab.def_solvent_type def
WHERE solvent_type_pk_id > 30
;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) NOT NULL;

SET FOREIGN_KEY_CHECKS = 1;



-- csm tables
INSERT INTO canano.csm_application ( 
	application_id,
	application_name,
	application_description,
	declarative_flag,
	active_flag,
	update_date
)
SELECT
	application_id,
	application_name,
	application_description,
	declarative_flag,
	active_flag,
	update_date
FROM cananolab.csm_application
;

INSERT INTO canano.csm_group ( 
	group_id,
	group_name,
	group_desc,
	update_date,
	application_id
)
SELECT
	group_id,
	group_name,
	group_desc,
	update_date,
	application_id
FROM cananolab.csm_group
;

INSERT INTO canano.csm_privilege ( 
	privilege_id,
	privilege_name,
	privilege_description,
	update_date
)
SELECT
	privilege_id,
	privilege_name,
	privilege_description,
	update_date
FROM cananolab.csm_privilege
;

INSERT INTO canano.csm_protection_element ( 
	protection_element_id,
	protection_element_name,
	protection_element_description,
	object_id,
	attribute,
	protection_element_type_id,
	application_id,
	update_date
)SELECT
	protection_element_id,
	protection_element_name,
	protection_element_description,
	object_id,
	attribute,
	protection_element_type_id,
	application_id,
	update_date
FROM cananolab.csm_protection_element
;

INSERT INTO canano.csm_protection_group ( 
	protection_group_id,
	protection_group_name,
	protection_group_description,
	application_id,
	large_element_count_flag,
	update_date,
	parent_protection_group_id
)SELECT
	protection_group_id,
	protection_group_name,
	protection_group_description,
	application_id,
	large_element_count_flag,
	update_date,
	parent_protection_group_id
FROM cananolab.csm_protection_group
;

INSERT INTO canano.csm_pg_pe ( 
	pg_pe_id,
	protection_group_id,
	protection_element_id,
	update_date
)SELECT
	pg_pe_id,
	protection_group_id,
	protection_element_id,
	update_date
FROM cananolab.csm_pg_pe
;

INSERT INTO canano.csm_role ( 
	role_id,
	role_name,
	role_description,
	application_id,
	active_flag,
	update_date
)SELECT
	role_id,
	role_name,
	role_description,
	application_id,
	active_flag,
	update_date
FROM cananolab.csm_role
;

INSERT INTO canano.csm_role_privilege ( 
	role_privilege_id,
	role_id,
	privilege_id,
	update_date
)SELECT
	role_privilege_id,
	role_id,
	privilege_id,
	update_date
FROM cananolab.csm_role_privilege
;

INSERT INTO canano.csm_user ( 
	user_id,
	login_name,
	first_name,
	last_name,
	organization,
	department,
	title,
	phone_number,
	password,
	email_id,
	start_date,
	end_date,
	update_date
)SELECT
	user_id,
	login_name,
	first_name,
	last_name,
	organization,
	department,
	title,
	phone_number,
	password,
	email_id,
	start_date,
	end_date,
	update_date
FROM cananolab.csm_user
;

INSERT INTO canano.csm_user_group ( 
	user_group_id,
	user_id,
	group_id
)SELECT
	user_group_id,
	user_id,
	group_id
FROM cananolab.csm_user_group
;

INSERT INTO canano.csm_user_group_role_pg ( 
	user_group_role_pg_id,
	user_id,
	group_id,
	role_id,
	protection_group_id,
	update_date
)SELECT
	user_group_role_pg_id,
	user_id,
	group_id,
	role_id,
	protection_group_id,
	update_date
FROM cananolab.csm_user_group_role_pg
;

INSERT INTO canano.csm_user_pe ( 
	user_protection_element_id,
	protection_element_id,
	user_id,
	update_date
)SELECT
	user_protection_element_id,
	protection_element_id,
	user_id,
	update_date
FROM cananolab.csm_user_pe
;
