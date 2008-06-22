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