use cananolab;

INSERT into csm_user_group_role_pg (
	group_id,
	role_id,
	protection_group_id,
	update_date
)
SELECT
	g.group_id,
	'1',
	pg.protection_group_id,
	sysdate()
FROM csm_group g,
	csm_protection_group pg,
	source so,
	sample sa
WHERE
	g.group_name = so.organization_name
AND so.source_pk_id = sa.source_pk_id
AND sa.sample_name = pg.protection_group_name
;
