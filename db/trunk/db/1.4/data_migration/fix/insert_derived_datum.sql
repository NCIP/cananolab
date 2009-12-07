use canano;

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
set datum_name = 'surface area'
where datum_name = 'surface_area';

update  canano.derived_datum
set datum_name = 'Z-average'
where datum_name = 'Z-Average';

update  canano.derived_datum
set datum_name = 'zeta potential'
where datum_name = 'zeta_potential';

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
	derived_datum dd,
	cananolab.datum d13
WHERE g.group_id = upg.group_id
AND g.group_name = 'Public'
AND upg.protection_group_id = pg.protection_group_id
AND pg.protection_group_name = ns.particle_sample_name
AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
AND cha.characterization_pk_id = dbd.characterization_pk_id
AND dbd.derived_bioassay_data_pk_id = dd.derived_bioassay_data_pk_id
AND dd.datum_pk_id = d13.datum_pk_id
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
	(SELECT
		distinct dd.datum_pk_id protection_group_name
		FROM csm_group g,
			csm_protection_group pg,
			csm_user_group_role_pg upg,
			nanoparticle_sample ns,
			characterization cha,
			derived_bioassay_data dbd,
			derived_datum dd,
			cananolab.datum d13
		WHERE g.group_id = upg.group_id
		AND g.group_name = 'Public'
		AND upg.protection_group_id = pg.protection_group_id
		AND pg.protection_group_name = ns.particle_sample_name
		AND ns.particle_sample_pk_id = cha.particle_sample_pk_id
		AND cha.characterization_pk_id = dbd.characterization_pk_id
		AND dbd.derived_bioassay_data_pk_id = dd.derived_bioassay_data_pk_id
		AND dd.datum_pk_id = d13.datum_pk_id
	)tmp
WHERE pg.protection_group_name = tmp.protection_group_name
AND g.group_name = 'Public'
AND cr.role_name = 'R'
;