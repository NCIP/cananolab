-- update description with identifier_name if no description
update characterization
set description=identifier_name
where description is null and identifier_name is not null;

ALTER TABLE characterization change description design_method_description TEXT;
ALTER TABLE characterization DROP COLUMN cytotoxicity_cell_death_method;
ALTER TABLE characterization DROP COLUMN identifier_name;
ALTER TABLE characterization MODIFY COLUMN cytotoxicity_cell_line TEXT;
ALTER TABLE characterization ADD COLUMN transfection_cell_line TEXT;
ALTER TABLE characterization ADD COLUMN analysis_conclusion TEXT;
ALTER TABLE characterization ADD COLUMN enzyme_induction_enzyme VARCHAR(200);
ALTER TABLE characterization ADD COLUMN other_char_assay_category VARCHAR(200);
ALTER TABLE characterization ADD COLUMN other_char_name VARCHAR(200);
ALTER TABLE characterization ADD COLUMN assay_type VARCHAR(200);

-- in vitro data migration
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

DROP TABLE IF EXISTS surface_chemistry;