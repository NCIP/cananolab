use canano;

SET FOREIGN_KEY_CHECKS = 0;

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
	source so,
	nanoparticle_sample sa
WHERE
	g.group_name = so.organization_name
AND so.source_pk_id = sa.source_pk_id
AND sa.particle_sample_name = pg.protection_group_name
AND cr.role_name = 'R'
;

SET FOREIGN_KEY_CHECKS = 1;