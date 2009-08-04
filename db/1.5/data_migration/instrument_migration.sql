-- instrument and technique
RENAME TABLE instrument to instrument_old;

CREATE TABLE instrument
(
	instrument_pk_id BIGINT NOT NULL,
	type VARCHAR(200),
	manufacturer VARCHAR(2000),
	model_name VARCHAR(200),
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (instrument_pk_id)
) TYPE=InnoDB
;

CREATE TABLE technique
(
	technique_pk_id BIGINT NOT NULL,
	type VARCHAR(200) NOT NULL,
	abbreviation VARCHAR(50),
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	PRIMARY KEY (technique_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_config_instrument
(
	experiment_config_pk_id BIGINT NOT NULL,
	instrument_pk_id BIGINT NOT NULL,
	PRIMARY KEY (experiment_config_pk_id, instrument_pk_id),
	KEY (experiment_config_pk_id),
	KEY (instrument_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_config
(
	experiment_config_pk_id BIGINT NOT NULL,
	description TEXT,
	created_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	characterization_pk_id BIGINT,
	technique_pk_id BIGINT,
	PRIMARY KEY (experiment_config_pk_id),
	KEY (characterization_pk_id),
	KEY (technique_pk_id)
) TYPE=InnoDB
;


ALTER TABLE experiment_config_instrument ADD CONSTRAINT FK_experiment_config_instrument_experiment_config
	FOREIGN KEY (experiment_config_pk_id) REFERENCES experiment_config (experiment_config_pk_id)
;

ALTER TABLE experiment_config_instrument ADD CONSTRAINT FK_experiment_config_instrument_instrument
	FOREIGN KEY (instrument_pk_id) REFERENCES instrument (instrument_pk_id)
;

ALTER TABLE experiment_config ADD CONSTRAINT FK_experiment_config_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE experiment_config ADD CONSTRAINT FK_experiment_config_technique
	FOREIGN KEY (technique_pk_id) REFERENCES technique (technique_pk_id)
;

--copy from old table and convert type to lower cases
insert into instrument (instrument_pk_id, type, manufacturer, created_by, created_date)
select a.instrument_pk_id, lcase(type), manufacturer, 'DATA_MIGRATION', sysdate()
from instrument_old a;

--delete the ones that are not used by characterization
DELETE FROM instrument
WHERE NOT EXISTS
  ( SELECT b.instrument_pk_id
     FROM instrument_config b, characterization c
     WHERE instrument.instrument_pk_id=b.instrument_pk_id
     and b.instrument_config_pk_id=c.instrument_config_pk_id)
;

-- update to make sure no spelling differences
update instrument
set type='size exclusion chromatography with multi-angle laser light scattering'
where type='size-exclusion chromatography and multiangle laser light scattering';

update instrument
set type='dynamic light scattering'
where type='zetasizer';

update instrument
set type='zeta potential analyzer'
where type='phase analysis light scattering';

update instrument
set type='gas sorption detector'
where type='surface area and pore size analyzer';

-- update model_name with description in instrument_config
UPDATE instrument a, instrument_config b
set a.model_name=substr(b.description, 1, 200)
where a.instrument_pk_id=b.instrument_pk_id;

-- update manufacturer spelling
update instrument
set manufacturer='Diagnostica Stago'
where manufacturer='Diagnostica';

-- create a temp table to hold instruments that can't be migrated
CREATE TABLE instrument_to_review
(
	instrument_pk_id BIGINT NOT NULL,
	instrument_config_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NOT NULL,
	instrument_type VARCHAR(200),
	description TEXT
) TYPE=InnoDB
;

-- create a temp table to hold techniques that can't be migrated
CREATE TABLE technique_to_review
(
	instrument_pk_id BIGINT NOT NULL,
	instrument_config_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT NOT NULL,
	instrument_type VARCHAR(200),
	description TEXT
) TYPE=InnoDB
;

-- move instruments that map to mulitple techniques to the review table
-- these need manual curation to insert into the 1.5 technique table
insert into instrument_to_review
(instrument_pk_id, instrument_config_pk_id, characterization_pk_id, instrument_type, description)
select i.instrument_pk_id,  ic.instrument_config_pk_id, c.characterization_pk_id,i.type, ic.description
from instrument i, common_lookup cl, instrument_config ic, characterization c
where i.type=cl.value
and i.instrument_pk_id=ic.instrument_pk_id
and ic.instrument_config_pk_id=c.instrument_config_pk_id
and cl.attribute='instrument'
group by i.instrument_pk_id, i.type, ic.instrument_config_pk_id, ic.description, c.characterization_pk_id
having count(distinct cl.name)>1;

-- move techniques that have more than one instruments to the review table
-- these need manual curation to insert to the 1.5 instrument table
insert into technique_to_review
(instrument_pk_id, instrument_config_pk_id, characterization_pk_id, instrument_type, description)
select i.instrument_pk_id,  ic.instrument_config_pk_id, c.characterization_pk_id,i.type, ic.description
from instrument i, common_lookup cl, instrument_config ic, characterization c
where cl.attribute='instrument'
and i.type=cl.name
and i.instrument_pk_id=ic.instrument_pk_id
and ic.instrument_config_pk_id=c.instrument_config_pk_id
group by i.instrument_pk_id, i.type, ic.instrument_config_pk_id, ic.description, c.characterization_pk_id
having count(distinct cl.value)>1;

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT AUTO_INCREMENT NOT NULL;

-- insert into techniques based on technique to instrument mapping
INSERT into technique (type,created_date,created_by)
select distinct c.name, sysdate(), 'DATA_MIGRATION'
from
(select a.name
from common_lookup a, instrument b, instrument_to_review c
where a.attribute='instrument'
 and a.value=b.type
 and b.instrument_pk_id!=c.instrument_pk_id
 union
 select a.name
 from common_lookup a, instrument b
 where a.attribute='instrument'
 and a.name=b.type) c
 ;

-- update technique abbreviation
update technique t, common_lookup cl
set t.abbreviation=cl.value
where cl.attribute='abbreviation'
and cl.name=t.type;

ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

-- insert into experiment_config using matching from technique to instrument
INSERT INTO experiment_config(experiment_config_pk_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT distinct ic.instrument_config_pk_id, ic.description, ic.created_date, ic.created_by,
	c.characterization_pk_id,t.technique_pk_id
FROM instrument_config ic, characterization c, instrument i, common_lookup cl, technique t, instrument_to_review ir
WHERE c.instrument_config_pk_id = ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
and cl.attribute='instrument'
and cl.value=i.type
and cl.name=t.type
and i.instrument_pk_id !=ir.instrument_pk_id
;

-- insert into experiment_config using matching of instrument type to technique type
INSERT INTO experiment_config(experiment_config_pk_id,description,created_date, created_by,
	characterization_pk_id,technique_pk_id)
SELECT ic.instrument_config_pk_id, ic.description, ic.created_date, ic.created_by,
	c.characterization_pk_id,t.technique_pk_id
FROM instrument_config ic, characterization c, instrument i,  technique t
WHERE c.instrument_config_pk_id = ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
and i.type=t.type
;

-- update instrument type with mapping from technique where there is a one to one mapping between technique and instrument
update instrument i, common_lookup cl, technique_to_review tr
set i.type=cl.value
where cl.attribute='instrument'
and i.type=cl.name
and i.type!=tr.instrument_type;

-- delete the technique that needs to be reviewed
DELETE FROM instrument
where instrument_pk_id in
(select instrument_pk_id
from technique_to_review);

ALTER TABLE characterization
 DROP FOREIGN KEY FK_characterization_instrument_config
;
 
DELETE FROM instrument_config
where instrument_pk_id in
(select instrument_pk_id
from technique_to_review);

INSERT INTO experiment_config_instrument(experiment_config_pk_id, instrument_pk_id)
SELECT ic.instrument_config_pk_id, ic.instrument_pk_id
FROM instrument_config ic, characterization c, instrument i
where c.instrument_config_pk_id=ic.instrument_config_pk_id
and ic.instrument_pk_id=i.instrument_pk_id
;

ALTER TABLE characterization
 DROP instrument_config_pk_id;

DROP TABLE instrument_config;
DROP TABLE instrument_old;
