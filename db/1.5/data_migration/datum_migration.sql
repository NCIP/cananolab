-- change derived_datum to datum, derived_bioassay to finding, add condition
CREATE TABLE datum
(
	datum_pk_id BIGINT NOT NULL,
	name VARCHAR(200) NOT NULL,
	value VARCHAR(200) NOT NULL,
	value_type VARCHAR(200),
	value_unit VARCHAR(200),
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	finding_pk_id BIGINT,
	file_pk_id BIGINT,
	PRIMARY KEY (datum_pk_id),
	KEY (file_pk_id),
	KEY (finding_pk_id)
) TYPE=InnoDB
;


CREATE TABLE datum_condition
(
	datum_pk_id BIGINT NOT NULL,
	condition_pk_id BIGINT NOT NULL,
	PRIMARY KEY (datum_pk_id, condition_pk_id),
	KEY (datum_pk_id),
	KEY (condition_pk_id)
) TYPE=InnoDB
;

CREATE TABLE finding
(
	finding_pk_id BIGINT NOT NULL,
	characterization_pk_id BIGINT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	UNIQUE (finding_pk_id),
	KEY (characterization_pk_id)
) TYPE=InnoDB
;

CREATE TABLE finding_file
(
	finding_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (finding_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (finding_pk_id)
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

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_datum
	FOREIGN KEY (datum_pk_id) REFERENCES datum (datum_pk_id)
;

ALTER TABLE datum_condition ADD CONSTRAINT FK_datum_condition_experiment_condition
	FOREIGN KEY (condition_pk_id) REFERENCES experiment_condition (condition_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE datum ADD CONSTRAINT FK_datum_finding
	FOREIGN KEY (finding_pk_id) REFERENCES finding (finding_pk_id)
;

ALTER TABLE finding ADD CONSTRAINT FK_finding_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE finding_file ADD CONSTRAINT FK_finding_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

ALTER TABLE finding_file ADD CONSTRAINT FK_finding_file_finding
	FOREIGN KEY (finding_pk_id) REFERENCES finding (finding_pk_id)
;

INSERT INTO datum(datum_pk_id, name, value, value_type, value_unit,created_by, created_date, finding_pk_id)
	SELECT datum_pk_id, datum_name, value, value_type, value_unit, created_by, created_date, derived_bioassay_data_pk_id
	FROM derived_datum dd
;

INSERT INTO finding(finding_pk_id, characterization_pk_id, created_date, created_by)
	SELECT derived_bioassay_data_pk_id, characterization_pk_id, created_date, created_by
	FROM derived_bioassay_data
;

INSERT INTO finding_file(finding_pk_id, file_pk_id)
SELECT derived_bioassay_data_pk_id, dd.file_pk_id
FROM derived_bioassay_data dd, file
WHERE dd.file_pk_id=file.file_pk_id
 and file.file_name is not null;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT AUTO_INCREMENT NOT NULL;

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
insert into common_lookup(name,attribute, value) values ('Solvent Media', 'property','serum percentage');
insert into common_lookup(name,attribute, value) values ('Culture Media', 'property','media type');
insert into common_lookup(name,attribute, value) values ('Culture Media', 'property','serum percentage');
insert into common_lookup(name,attribute, value) values ('Short Term Storage', 'property','time');
insert into common_lookup(name,attribute, value) values ('Short Term Storage', 'property','lyophilized');
insert into common_lookup(name,attribute, value) values ('Long Term Storage', 'property','time');
insert into common_lookup(name,attribute, value) values ('Long Term Storage', 'property','lyophilized');
insert into common_lookup(name,attribute, value) values ('Lyophilization', 'property','time');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','bandwidth');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','frequency');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','wavelength');
insert into common_lookup(name,attribute, value) values ('Electromagnetic Radiation', 'property','time');
insert into common_lookup(name,attribute, value) values ('Sonication', 'property','number of pulses');
insert into common_lookup(name,attribute, value) values ('Sonication', 'property','pulse duration');

insert into common_lookup(name,attribute,value) values('Purity', 'datumName', '% purity for sample');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'T1');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'T2');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'R1');
insert into common_lookup(name,attribute,value) values('Relaxivity', 'datumName', 'R2');
insert into common_lookup(name,attribute,value) values('Caspase 3 Apoptosis', 'datumName', '% of control');
insert into common_lookup(name,attribute,value) values('Caspase 3 Apoptosis', 'datumName', 'fluorescence');
insert into common_lookup(name,attribute,value) values('Cell Viability', 'datumName', 'LC50');
insert into common_lookup(name,attribute,value) values('Cell Binding/Internalization', 'datumName', 'cellular fluorescence');
insert into common_lookup(name,attribute,value) values ('CFU-GM','datumName','number of CFU-GM colonies');
insert into common_lookup(name,attribute,value) values ('CFU-GM','datumName','total number of bone marrow cells');
insert into common_lookup(name,attribute,value) values ('Chemotaxis', 'datumName',	'% of control');
insert into common_lookup(name,attribute,value) values ('Coagulation', 'datumName',	'Prothrombin Time(PT)');
insert into common_lookup(name,attribute,value) values ('Coagulation', 'datumName',	'Activated Partial Thromboplastin Time (APTT)');
insert into common_lookup(name,attribute,value) values ('Coagulation', 'datumName',	'Thrombin Time');
insert into common_lookup(name,attribute,value) values ('Coagulation', 'datumName',	'Reptilase Time');
insert into common_lookup(name,attribute,value) values ('Complement Activation', 'datumName', 'is complement activation induced');
insert into common_lookup(name,attribute,value) values ('Cytokine Induction', 'datumName','IL10');
insert into common_lookup(name,attribute,value) values ('Cytokine Induction', 'datumName','IL1 Beta');
insert into common_lookup(name,attribute,value) values ('Cytokine Induction', 'datumName','IL8');
insert into common_lookup(name,attribute,value) values ('Cytokine Induction', 'datumName','IL6');
insert into common_lookup(name,attribute,value) values ('Cytokine Induction', 'datumName','TNF Alpha');
insert into common_lookup(name,attribute,value) values ('EnzymeInduction', 'datumName','% of control');
insert into common_lookup(name,attribute,value) values ('Gene Expression', 'datumName', 'cellular fluorescence');
insert into common_lookup(name,attribute,value) values ('Gene Expression', 'datumName', 'fluorescence ratio');
insert into common_lookup(name,attribute,value) values ('GSH Homeostasis', 'datumName', '% of control');
insert into common_lookup(name,attribute,value) values ('Hemolysis','datumName','is hemolytic');
insert into common_lookup(name,attribute,value) values ('Leukocyte Proliferation','datumName','% of control');
insert into common_lookup(name,attribute,value) values ('Lipid Peroxidation', 'datumName', '% of control');
insert into common_lookup(name,attribute,value) values ('Mitochondrial Function', 'datumName', 'luminescence');
insert into common_lookup(name,attribute,value) values ('Mitochondrial Membrane Potential', 'datumName', 'fluorescence ratio');
insert into common_lookup(name,attribute,value) values ('Mitochondrial Membrane Potential', 'datumName', 'ratio of red to green fluorescence');
insert into common_lookup(name,attribute,value) values ('Oxidative Burst', 'datumName', '% of control');
insert into common_lookup(name,attribute,value) values ('Cytotoxic Activity of NK Cells', 'datumName','% of control');
insert into common_lookup(name,attribute,value) values ('Phagocytosis','datumName','Fold Change vs. control');
insert into common_lookup(name,attribute,value) values ('Platelet Aggregation','datumName','is above threshold');
insert into common_lookup(name,attribute,value) values ('Platelet Aggregation','datumName','% of aggregation vs. control');
insert into common_lookup(name,attribute,value) values ('Platelet Aggregation','datumName','% of collagen induced aggregation vs. control');
insert into common_lookup(name,attribute,value) values ('Proliferation', 'datumName', 'ratio of cell line1 to cell line 2');
insert into common_lookup(name,attribute,value) values ('Proliferation', 'datumName', '% of control');
insert into common_lookup(name,attribute,value) values ('Plasma Protein Binding','datumName','protein bound');
insert into common_lookup(name,attribute,value) values ('Plasma Protein Binding','datumName','peptide bound');
insert into common_lookup(name,attribute,value) values ('ROS Generation', 'datumName', '% of control');

insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','mg/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','g/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','mg/mL');
insert into common_lookup(name,attribute, value) values ('Sample Concentration', 'unit','ug/uL');
insert into common_lookup(name,attribute, value) values ('Centrifugation', 'unit','g-Force');
insert into common_lookup(name,attribute, value) values ('Centrifugation', 'unit','RPM');
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
insert into common_lookup(name,attribute,value) values ('dimension', 'unit', ' nm');
insert into common_lookup(name,attribute,value) values ('luminescence signal', 'unit', 'volt');

insert into common_lookup(name,attribute,value) values ('DatumCondition','valueType','Z-score');

update common_lookup
set name='Datum'
where name='DerivedDatum';

update common_lookup
set attribute='datumName'
where attribute='derivedDatumName';

update common_lookup
set attribute='otherDatumName'
where attribute='otherDerivedDatumName';

update common_lookup
set name='DatumCondition'
where name='Datum'
and attribute in ('valueType', 'otherValueType');

delete from common_lookup
where name in ('CFU_GM', 'Hemolysis', 'PlateletAggregation', 'CellViability')
and attribute='datumName';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

DROP TABLE derived_datum;
DROP TABLE derived_bioassay_data;
-- set datum drop down for assay endpoints