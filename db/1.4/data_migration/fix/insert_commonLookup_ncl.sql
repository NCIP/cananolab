use canano;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

update derived_datum
set datum_name='average'
where datum_name='Average';

update derived_datum
set datum_name='percent cell viability'
where datum_name='Percent Cell Viability';

INSERT INTO canano.common_lookup (
	name,
	attribute,
	value
)
VALUES('Size','otherDerivedDatumName','average'),
('average','unit','nm'),
('CellViability','otherDerivedDatumName','percent cell viability'),
('percent cell viability','unit','%');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;
