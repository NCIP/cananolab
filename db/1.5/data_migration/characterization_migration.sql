--update description with identifier_name if no description
update characterization
set description=identifier_name
where description is null and identifier_name is not null;

ALTER TABLE characterization change description design_method_description TEXT;
ALTER TABLE characterization DROP COLUMN cytotoxicity_cell_death_method;
ALTER TABLE characterization DROP COLUMN identifier_name;
ALTER TABLE characterization ADD COLUMN analysis_conclusion TEXT;
ALTER TABLE characterization ADD COLUMN enzyme_induction_enzyme VARCHAR(200);
ALTER TABLE characterization ADD COLUMN other_char_assay_category VARCHAR(200);
ALTER TABLE characterization ADD COLUMN other_char_name VARCHAR(200);
ALTER TABLE characterization ADD COLUMN assay_type VARCHAR(200);

delete from common_lookup where attribute='cellDeathMethod';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT AUTO_INCREMENT NOT NULL;

insert into common_lookup(name,attribute,value) values ('PhysicoChemicalCharacterization','displayName','Physico-Chemical Characterization');
insert into common_lookup(name,attribute,value) values ('InvivoCharacterization','displayName','In Vivo Characterization');
insert into common_lookup(name,attribute,value) values ('Pharmacokinetics','displayName','Pharmacokinetics');
insert into common_lookup(name,attribute,value) values ('Toxicology','displayName','Toxicology');
insert into common_lookup(name,attribute,value) values ('Relaxivity','displayName','Relaxivity');
insert into common_lookup(name,attribute,value) values ('Sterility','displayName','Sterility');
insert into common_lookup(name,attribute,value) values ('Targeting','displayName','Targeting');
insert into common_lookup(name,attribute,value) values ('MetabolicStability','displayName','Metabolic Stability');
insert into common_lookup(name,attribute,value) values ('Entrapment','displayName','Entrapment');
insert into common_lookup(name,attribute,value) values('Sterility', 'assayType', 'Endotoxin');
insert into common_lookup(name,attribute,value) values('Sterility', 'assayType', 'Bacterial/Yeast/Mold');
insert into common_lookup(name,attribute,value) values('Sterility', 'assayType', 'Mycoplasma');
insert into common_lookup(name,attribute,value) values('BloodContact', 'assayType', 'Plasma Protein Binding (2D PAGE)');
insert into common_lookup(name,attribute,value) values('BloodContact', 'assayType', 'Hemolysis');
insert into common_lookup(name,attribute,value) values('BloodContact', 'assayType', 'Platelet Aggregation');
insert into common_lookup(name,attribute,value) values('BloodContact', 'assayType', 'Coagulation');
insert into common_lookup(name,attribute,value) values('BloodContact', 'assayType', 'Complement Activation');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'CFU-GM');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Leukocyte Proliferation');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Phagocytosis');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Cytokine Induction');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Chemotaxis');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Oxidative Burst');
insert into common_lookup(name,attribute,value) values('ImmuneCellFunction', 'assayType', 'Cytotoxic Activity of NK Cells');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'assayType', 'GSH Homeostasis');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'assayType', 'Lipid Peroxidation');
insert into common_lookup(name,attribute,value) values('OxidativeStress', 'assayType', 'ROS Generation');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Cell Viability');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Caspase 3 Apoptosis');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Proliferation');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Mitochondrial Membrane Potential');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Mitochondrial Function');
insert into common_lookup(name,attribute,value) values('Cytotoxicity', 'assayType', 'Gene Expression');
insert into common_lookup(name,attribute,value) values('Targeting', 'assayType', 'Cell Binding/Internalization');
insert into common_lookup(name,attribute,value) values('Targeting', 'assayType', 'Gene Expression');

delete from common_lookup
where name in ('CFU_GM', 'Hemolysis', 'PlateletAggregation', 'CellViability', 'Toxicity', 'Caspase3Activation', 'Chemotaxis', 'Coagulation', 'ComplementActivation', 'CytokineInduction', 'Immunotoxicity', 'LeukocyteProliferation', 'NKCellCytotoxicActivity', 'OxidativeBurst', 'LabFile', 'Phagocytosis', 'PlasmaProteinBinding')
and attribute='displayName';

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT  NOT NULL;

CREATE TABLE characterization_file
(
	characterization_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (characterization_pk_id, file_pk_id),
	KEY (characterization_pk_id),
	KEY (file_pk_id)
) TYPE=InnoDB
;

ALTER TABLE characterization_file ADD CONSTRAINT FK_characterization_file_characterization
	FOREIGN KEY (characterization_pk_id) REFERENCES characterization (characterization_pk_id)
;

ALTER TABLE characterization_file ADD CONSTRAINT FK_characterization_file_file
	FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id)
;

--in vitro data migration
update characterization
set discriminator='Cytotoxicity',
assay_type='Cell Viability'
where discriminator ='CellViability';

update characterization
set discriminator='Cytotoxicity',
assay_type='Caspase 3 Apoptosis'
where discriminator ='Caspase3Activation';

update characterization
set discriminator='BloodContact',
assay_type='Plasma Protein Binding (2D PAGE)'
where discriminator ='PlasmaProteinBinding';

update characterization
set discriminator='BloodContact',
assay_type='Hemolysis'
where discriminator ='Hemolysis';

update characterization
set discriminator='BloodContact',
assay_type='Platelet Aggregation'
where discriminator ='PlateletAggregation';

update characterization
set discriminator='BloodContact',
assay_type='Coagulation'
where discriminator ='Coagulation';

update characterization
set discriminator='BloodContact',
assay_type='Complement Activation'
where discriminator ='ComplementActivation';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='CFU-GM'
where discriminator ='CFU_GM';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Leukocyte Proliferation'
where discriminator ='LeukocyteProliferation';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Phagocytosis'
where discriminator ='Phagocytosis';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Cytokine Induction'
where discriminator ='CytokineInduction';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Chemotaxis'
where discriminator ='Chemotaxis';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Oxidative Burst'
where discriminator ='OxidativeBurst';

update characterization
set discriminator='ImmuneCellFunction',
assay_type='Cytotoxic Activity of NK Cells'
where discriminator ='NKCellCytotoxicActivity';
