--change derived_datum to datum, derived_bioassay to data_set, add condition
CREATE TABLE datum
(
	datum_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	value VARCHAR(200) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	data_set_pk_id BIGINT,
	data_row_pk_id BIGINT,
	characterization_pk_id BIGINT,
	PRIMARY KEY (datum_pk_id),
	KEY (characterization_pk_id),
	KEY (data_row_pk_id)
) TYPE=InnoDB
;

CREATE TABLE datum_condition
(
	datum_pk_id BIGINT NOT NULL,
	condition_pk_id BIGINT NOT NULL,
	PRIMARY KEY (datum_pk_id, condition_pk_id),
	KEY (condition_pk_id),
	KEY (datum_pk_id)
) TYPE=InnoDB
;


CREATE TABLE data_set
(
	data_set_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT,
	PRIMARY KEY (data_set_pk_id),
	UNIQUE (data_set_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;


CREATE TABLE data_row
(
	data_row_pk_id BIGINT NOT NULL,
	PRIMARY KEY (data_row_pk_id)
) TYPE=InnoDB
;

CREATE TABLE experiment_condition
(
	condition_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	property VARCHAR(200),
	value VARCHAR(200) NOT NULL,
	value_unit VARCHAR(200),
	value_type VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (condition_pk_id)
) TYPE=InnoDB
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_data_row
	FOREIGN KEY (data_row_pk_id) REFERENCES data_row (data_row_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_condition
	FOREIGN KEY (condition_pk_id) REFERENCES experiment_condition (condition_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_data_row
	FOREIGN KEY (datum_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE data_set ADD CONSTRAINT FK_data_set_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

INSERT INTO data_set(data_set_pk_id, file_pk_id)
	SELECT derived_bioassay_data_pk_id, file_pk_id
	FROM derived_bioassay_data
;

INSERT INTO data_row(data_row_pk_id)
SELECT datum_pk_id
FROM derived_datum
;

INSERT INTO datum(datum_pk_id, name, value, value_type, value_unit,
	description, created_by, created_date, data_set_pk_id, data_row_pk_id,
	characterization_pk_id)
	SELECT dd.datum_pk_id, dd.datum_name, dd.value, dd.value_type,
		dd.value_unit, dd.description, dd.created_by, dd.created_date,
		dd.derived_bioassay_data_pk_id, dd.datum_pk_id,
		b.characterization_pk_id
	FROM derived_datum dd, derived_bioassay_data b
	WHERE dd.derived_bioassay_data_pk_id = b.derived_bioassay_data_pk_id
;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into common_lookup(name,attribute, value) values ('Condition', 'name','Solvent Media');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Culture Media');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Sample Concentration');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Short Term Storage');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Long Term Storage');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Ph');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Temperature');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Centrifugation');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Freeze Thaw');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Lyophilization');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Sonication');
insert into common_lookup(name,attribute, value) values ('Condition', 'name','Electromagnetic Radiation');
insert into common_lookup(name,attribute, value) values ('Lyophilization', 'property','time');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','with serum');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','ion concentration');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','ionic strength');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','osmolality');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','molecular formula');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','with serum');
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','with serum');
insert into common_lookup(name,attribute, value) values ('Culture Media', 'property','with serum');
insert into common_lookup(name,attribute, value) values ('Short Term Storage', 'property','time');
insert into common_lookup(name,attribute, value) values ('Long Term Storage', 'property','time');
insert into common_lookup(name,attribute, value) values ('Lyophilization', 'property','time');
insert into common_lookup(name,attribute, value) values ('Centrifugation', 'property','g-Force');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','bandwidth');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','frequency');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','wavelength');
insert into common_lookup(name,attribute, value) values ('Sonication', 'property','time');
insert into common_lookup(name,attribute, value) values ('Sonication', 'property','power');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','mg/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','g/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','mg/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','ug/uL');
insert into common_lookup(name,attribute, value) values ('ion concentration', 'unit','mol/L');
insert into common_lookup(name,attribute, value) values ('ionic strength', 'unit','mol/L');
insert into common_lookup(name,attribute, value) values ('osmolality', 'unit','osmol/kg');
insert into common_lookup(name,attribute, value) values ('time', 'unit','second');
insert into common_lookup(name,attribute, value) values ('time', 'unit','hour');
insert into common_lookup(name,attribute, value) values ('time', 'unit','Minute');
insert into common_lookup(name,attribute, value) values ('time', 'unit','Day');
insert into common_lookup(name,attribute, value) values ('power', 'unit','Watt');
insert into common_lookup(name,attribute, value) values ('power', 'unit','Kilowatt');
insert into common_lookup(name,attribute, value) values ('temperature', 'unit','Celcius');
insert into common_lookup(name,attribute, value) values ('temperature', 'unit','Fahrenheit');
insert into common_lookup(name,attribute, value) values ('Freeze Thaw', 'unit','Number of Cycles');
insert into common_lookup(name,attribute, value) values ('bandwidth', 'unit','Hz');
insert into common_lookup(name,attribute, value) values ('frequency', 'unit','Hz');
insert into common_lookup(name,attribute, value) values ('wavelength', 'unit','nm');
insert into common_lookup(name,attribute,value) values('Purity', 'datumName', '% purity for compound of interest');
insert into common_lookup(name,attribute,value) values('Purity', 'datumName', 'peak1');
insert into common_lookup(name,attribute,value) values('Purity', 'datumName', '% compound plus exipients');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'T1');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'T2');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'R1');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'R2');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'extinction coefficient');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'attenuation coefficient');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'quantum yield');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'quantum efficiency');
insert into common_lookup(name,attribute,value) values('Targeting', 'datumName', 'mean celluar fluorescence');
insert into common_lookup(name,attribute,value) values('Targeting', 'datumName', 'fraction fluorescence (control vs. treated)');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'Z score');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', '% of cells showing signal of caspase activation');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'amount of caspase 3 in control cells');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', '% normalized amount of treated vs untreated');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', '% control caspase');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'mean fluorescence');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'LC50');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'red to green fluorescence ratio');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'fluorescence ratio');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'luminescence signal');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', '% NVA equivalent (normalized to total celluar protein)');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'odds ratio, % of targeted cell population (control vs. treated)');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'datumName', 'fraction (control vs. treated)');
insert into common_lookup(name,attribute,value) values('luminescence signal', 'unit', 'volt');
insert into common_lookup(name,attribute,value) values('EnzymeInduction', 'datumName', 'relative activity');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', 'prothrombin time(PT)');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', 'activated partial thromboplastin time(APTT)');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', ' thrombin time');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', ' reptilase time');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', ' is blocking agent');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', ' is above threshold');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', ' % platelet aggregation');
insert into common_lookup(name,attribute,value) values('BloodContact', 'datumName', '% collagen induced platelet aggregation ');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' number of CFU GM colonies');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', '% chemotaxis');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', 'Is complement activation induced');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', 'IL10 cytokine');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' IL1 Beta cytokine');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' IL8 cytokine');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' IL6 cytokine');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' TNF Alpha cytokine');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' % leukocyte proliferation');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' % cytotoxicity');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' effector to target (E:T) ratio');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' is induced?');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' fold phagocytosis induction');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'datumName', ' is phagocytosis suppressed');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'datumName', '%GSH amount in particle vs control');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'datumName', '% ROS fluorescent dye in particle vs control');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'datumName', ' hyperperoxides in cell');

update common_lookup
set name='Datum'
where name='DerivedDatum';

update common_lookup
set attribute='datumName'
where attribute='derivedDatumName';

delete from common_lookup
where name in ('CFU_GM', 'Hemolysis', 'PlateletAggregation', 'CellViability')
and attribute='datumName';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;

--TODO migrate data from derived_datum to datum
--DROP TABLE derived_datum;
--DROP TABLE derived_bioassay_data;
--set datum drop down for assay endpoints