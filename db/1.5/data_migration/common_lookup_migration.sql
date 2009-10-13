-- common lookup
RENAME TABLE common_lookup to common_lookup_old;

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

--copy the other values and exclude cell lines
INSERT INTO common_lookup (name,attribute,value)
SELECT name, attribute, value
FROM common_lookup_old
where attribute like 'other%' and attribute!='otherCellLine';

--update camel case to lower cases separated by space
update common_lookup c2, common_lookup_old c1
set c2.name=lower(c1.value)
where c1.attribute='displayName' and
c1.name=c2.name and
c2.attribute like 'other%';

--update special cases
update common_lookup
set attribute='otherDatumName'
where attribute='otherDerivedDatumName';

UPDATE common_lookup
set name='caspase 3 apoptosis'
where name='caspase 3 activation'
and attribute like 'other%';

UPDATE common_lookup
set name='imaging function'
where name='imaging'
and attribute like 'other%';

UPDATE common_lookup
set name='cytotoxic activity of nk cells'
where name='nk cell cytotoxic activity'
and attribute like 'other%';

UPDATE common_lookup
set name='CFU-GM'
where name='cfu gm'
and attribute like 'other%';

UPDATE common_lookup
set name='composing element'
where name='ComposingElement'
and attribute like 'other%';

UPDATE common_lookup
set name='publication'
where name='Publication'
and attribute like 'other%';

delete
from common_lookup
where name='LC50'
and attribute='otherUnit'
and value in ('mg/L', 'nmol/L');

UPDATE common_lookup
set value='proceeding'
where value='in proceedings';

DELETE FROM common_lookup
where value in ('other', '[OTHER]')
and attribute like 'other%';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

DROP TABLE common_lookup_old;