-- common lookup
RENAME TABLE common_lookup to common_lookup_old;

update common_lookup_old
set attribute='otherDatumName'
where attribute='otherDerivedDatumName';

CREATE TABLE common_lookup
(
	common_lookup_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	attribute VARCHAR(200) NOT NULL,
	value VARCHAR(200) NOT NULL,
	PRIMARY KEY (common_lookup_pk_id)
) TYPE=InnoDB
;

source ../insert_common_lookup.sql;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO common_lookup (name,attribute,value) 
SELECT lcase(name), attribute, value 
FROM common_lookup_old
where attribute like 'other%' and attribute!='otherCellLine';

UPDATE common_lookup
set name='imaging function'
where name='imagingfunction';

UPDATE common_lookup
set name='composing element'
where name='composingelement';

UPDATE common_lookup
set name='functionalizing entity'
where name='functionalizingentity';

UPDATE common_lookup
set name='molecular weight'
where name='molecularweight';

UPDATE common_lookup
set name='cell viability'
where name='cellviability';

UPDATE common_lookup
set name='IC50'
where name='ic50';

UPDATE common_lookup
set name='LC50'
where name='lc50';

UPDATE common_lookup
set attribute='unit'
where attribute='otherUnit'
and name in ('LC50', 'IC50');

UPDATE common_lookup
set name='Feret''s Diameter'
where name='feret''s Diameter';

UPDATE common_lookup
set value='proceeding'
where value='in proceedings';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

DROP TABLE common_lookup_old;