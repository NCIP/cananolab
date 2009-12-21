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
set type='asymmetrical flow field-flow fractionation with multi-angle laser light scattering'
where type='asymmetrical flow field-flow fractionation with multi-angle light scattering';

update instrument
set type='size exclusion chromatography with multi-angle laser light scattering'
where type='size exclusion chromatography with multi-angle light scattering';

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

--insert into technique table the techniques entered as instruments in the instrument table
INSERT INTO technique (technique_pk_id, type, created_date,created_by)
select distinct ins.instrument_pk_id, ins.type, sysdate(), 'DATA_MIGRATION' 
from instrument ins, common_lookup cl
where ins.type=cl.name
and cl.attribute='instrument';

--insert into experiment_config configs for the above techniques
ALTER TABLE experiment_config
 CHANGE experiment_config_pk_id experiment_config_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO experiment_config(description,created_date, created_by,
	characterization_pk_id, technique_pk_id)
SELECT distinct ic.description, ic.created_date, ic.created_by, c.characterization_pk_id, t.technique_pk_id 
from instrument_config ic, technique t, characterization c
where ic.instrument_pk_id=t.technique_pk_id
and c.instrument_config_pk_id=ic.instrument_config_pk_id;

--for the above techniques, insert the corresponding instrument(s) into the instrument table
ALTER TABLE instrument
 CHANGE instrument_pk_id instrument_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO instrument (type, created_by, created_date)
select cl.value, 'DATA_MIGRATION', sysdate()
from technique t, common_lookup cl
where t.type=cl.name
and cl.attribute='instrument';

--remove the above techniques from the instrument table
DELETE FROM instrument
where type in (select type from technique);

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

-- move instruments that map to multiple techniques to the review table
-- these need manual curation to insert into the 1.5 technique table
insert into instrument_to_review
(instrument_pk_id, instrument_config_pk_id, characterization_pk_id, instrument_type, description)
select i.instrument_pk_id, ic.instrument_config_pk_id, c.characterization_pk_id,i.type, ic.description
from instrument i, common_lookup cl, instrument_config ic, characterization c
where i.type=cl.value
and cl.attribute='instrument'
and i.instrument_pk_id=ic.instrument_pk_id
and ic.instrument_config_pk_id=c.instrument_config_pk_id
group by i.instrument_pk_id, i.type, ic.instrument_config_pk_id, ic.description, c.characterization_pk_id
having count(distinct cl.name)>1;

--delete the instrument to be review from instrument
DELETE FROM instrument
where instrument_pk_id in (select instrument_pk_id from instrument_to_review);

--insert into technique table the instruments map to one technique
ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT AUTO_INCREMENT NOT NULL;

INSERT INTO technique (type, created_date,created_by)
select distinct cl.name, ins.created_date, ins.created_by
from instrument ins, common_lookup cl
where ins.type=cl.value
and cl.attribute='instrument';

--insert into experiment config table the instruments map to one technique
INSERT INTO experiment_config(description,created_date, created_by,
	characterization_pk_id, technique_pk_id)
SELECT distinct ic.description, ic.created_date, ic.created_by, c.characterization_pk_id, t.technique_pk_id 
from instrument_config ic, instrument ins, technique t, common_lookup cl, characterization c
where ic.instrument_pk_id=ins.instrument_pk_id
and c.instrument_config_pk_id=ic.instrument_config_pk_id
and ins.type=cl.value
and cl.attribute='instrument'
and t.type=cl.name;

--insert into experiment_config_instrument
INSERT INTO experiment_config_instrument(experiment_config_pk_id, instrument_pk_id)
select distinct ec.experiment_config_pk_id, ins.instrument_pk_id
from experiment_config ec, instrument_config ic, instrument ins, technique t, common_lookup cl
where ins.type=cl.value
and t.type=cl.name
and cl.attribute='instrument'
and ec.technique_pk_id=t.technique_pk_id
and ic.instrument_pk_id=ins.instrument_pk_id;

-- update instrument model_name with description in instrument_config
UPDATE instrument a, instrument_config b
set a.model_name=substr(b.description, 1, 200)
where a.instrument_pk_id=b.instrument_pk_id;

-- update manufacturer spelling
update instrument
set manufacturer='Diagnostica Stago'
where manufacturer='Diagnostica';

-- update technique abbreviation
update technique t, common_lookup cl
set t.abbreviation=cl.value
where cl.attribute='abbreviation'
and cl.name=t.type;

ALTER TABLE instrument 
 CHANGE instrument_pk_id instrument_pk_id BIGINT NOT NULL;
 
ALTER TABLE technique
 CHANGE technique_pk_id technique_pk_id BIGINT NOT NULL;

ALTER TABLE experiment_config
 CHANGE experiment_config_pk_id experiment_config_pk_id BIGINT NOT NULL;
 
ALTER TABLE characterization
 DROP FOREIGN KEY FK_characterization_instrument_config,
 DROP instrument_config_pk_id;

DROP TABLE instrument_config;
DROP TABLE instrument_old;
