-- migrate data from characteriation_table to derived_bioassay_data 
-- insert_into_derived_bioassay_data.sql

insert into derived_bioassay_data
(derived_bioassay_data_pk_id, characterization_pk_id, category, list_index)
select
char_table_pk_id, characterization_pk_id, type, list_index
from characterization_table;


-- map derived_bioassay_data.type to bioassay_data_data_category
-- update the category name to *** Distribution

update DERIVED_BIOASSAY_DATA  
set category=category || ' Distribution' 
where  CATEGORY in 
(select data.CATEGORY
from DERIVED_BIOASSAY_DATA data, CHARACTERIZATION chara 
where data.CHARACTERIZATION_PK_ID = chara.CHARACTERIZATION_PK_ID 
and data.CATEGORY is not null
and data.CATEGORY != 'Image'
and data.CATEGORY != 'Graph'
and data.CATEGORY not like '% Distribution'); 

--  insert data into bioassay_data_data_category
-- @insert_bioassay_data_data_category.sql;
declare
  cursor c_bioassay_data_category is
select chara.NAME name, data.DERIVED_BIOASSAY_DATA_PK_ID id, data.CATEGORY category
from DERIVED_BIOASSAY_DATA data, CHARACTERIZATION chara 
where data.CHARACTERIZATION_PK_ID = chara.CHARACTERIZATION_PK_ID 
and data.CATEGORY is not null
and data.CATEGORY != 'Image'
and data.CATEGORY != 'Graph'
group by (data.DERIVED_BIOASSAY_DATA_PK_ID, data.CATEGORY, chara.NAME);

 v_data_pk_id number;
 v_category_index number;
 
begin
	 v_category_index := 0;
	 v_data_pk_id := 0;
	 for i in c_bioassay_data_category loop
	    if i.id != v_data_pk_id then
     	    v_data_pk_id := i.id;
			v_category_index := 0;
		end if;
		
	    insert into bioassay_data_data_category 
		(derived_bioassay_data_pk_id, category_index, category_name)
		values (v_data_pk_id, v_category_index, i.category);
		
		v_category_index := v_category_index + 1;
		
	 end loop;
end;
/

-- migrate data from table_data to Datum
-- ? statistics_type, bioassay_data_category

insert into datum
(datum_pk_id, name, value, value_unit, derived_bioassay_data_pk_id, control_name, control_type, list_index)
select 
table_data_pk_id, type, value, value_unit, char_table_pk_id, control_name, control_type, list_index
from table_data
where value is not null and type is not null;


-- migrate data from table_data_condition to datum_condition

insert into datum_condition
(datum_condition_pk_id, name, value, value_unit, datum_pk_id, list_index)
select 
 table_data_cond_pk_id, type, value, value_unit, table_data_pk_id, list_index
 from table_data_condition
where value_unit is not null;



-- update lab_file file_id to derived_bioassay_data id (characterization_table)
-- and insert keyword_derived_file data into keyword_derived_bioassay_data
-- update_lab_file_insert_keyword_bioassay_data.sql


-- @update_lab_file_insert_keyword_bioassay_data.sql

declare
  cursor c_table_ids is
select a.CHAR_TABLE_PK_ID table_id, b.FILE_PK_ID file_id
from characterization_table a, lab_file b
where a.LABFILE_PK_ID=b.file_pk_id;

begin
	 for i in c_table_ids loop
	    update lab_file
		set file_pk_id=i.table_id
		where file_pk_id=i.file_id;
		
		insert into keyword_bioassay_data
		select keyword_pk_id, i.table_id
		from keyword_derived_file
		where derived_file_pk_id=i.file_id;
		
	 end loop;
end;
/


-- insert the derived bioassay data that doesn't not have file uploaded
-- insert_lab_file_no_file.sql

insert into lab_file
(file_pk_id)
select char_table_pk_id
from characterization_table
where labfile_pk_id is null;

-- update the CSM 
-- update_csm_pe_pg.sql

-- @update_csm_pg_pe.sql

declare
  cursor c_file_ids is
select to_char(a.CHAR_TABLE_PK_ID) new_file_id, b.PROTECTION_ELEMENT_NAME
from characterization_table a, csm_protection_element b
where to_char(a.LABFILE_PK_ID)=b.PROTECTION_ELEMENT_NAME
and a.labfile_pk_id is not null;

begin
	 for i in c_file_ids loop
    	 update csm_protection_element pe
         set protection_element_name=i.new_file_id,
		     object_id=i.new_file_id
	     where protection_element_name=i.protection_element_name;  
	 end loop;
end;
/


-- insert "characterization" as PE, PG, and assign 
insert into csm_protection_element 
(protection_element_id, protection_element_name, object_id, application_id, update_date)
values (csm_protectio_protection_e_seq.nextval,'characterization','characterization',2,sysdate);

insert into csm_protection_group 
(protection_group_id, protection_group_name, application_id, large_element_count_flag, update_date)
values (csm_protectio_protection_g_seq.nextval,'characterization',2,0,sysdate);

insert into csm_pg_pe
select csm_pg_pe_pg_pe_id_seq.nextval,pg.PROTECTION_GROUP_ID,pe.PROTECTION_ELEMENT_ID,sysdate
from csm_protection_group pg, csm_protection_element pe
where pg.PROTECTION_GROUP_NAME=pe.PROTECTION_ELEMENT_NAME 
and pe.OBJECT_ID='characterization';


-- @remove_redundant_char_protocol.sql
--delete redundant rows in characterization_protocol and update characterization 
declare
  cursor c_redundants is
select count(distinct a.CHAR_PROTOCOL_PK_ID), a.name, a.version
from characterization_protocol a, characterization_protocol b
where a.CHAR_PROTOCOL_PK_ID!=b.CHAR_PROTOCOL_PK_ID
and a.name=b.name
and a.version=b.version
group by (a.name, a.version);

  v_char_protocol_pk_id number;
  
begin
  for i in c_redundants loop
    --select the first one in the redudnant list
    select char_protocol_pk_id
	into v_char_protocol_pk_id
	from characterization_protocol
	where name=i.name
	and version=i.version
	and rownum=1;
	
	--update characterization
	update characterization chara
	set char_protocol_pk_id=v_char_protocol_pk_id
	where exists
	(select char_protocol_pk_id
	from characterization_protocol p
	where p.name=i.name
	and p.version=i.version
	and chara.char_protocol_pk_id=p.char_protocol_pk_id);
	
	--delete other redundant ones
	delete from characterization_protocol
	where char_protocol_pk_id!=v_char_protocol_pk_id
	and name=i.name
	and version=i.version;
  end loop;
end; 
/


-- insert data into protocol from characterization_protocol
-- insert_protocol.sql

insert into protocol
(protocol_pk_id, protocol_name, protocol_type)
select char_protocol_pk_id, name, 'In Vitro assay'
from characterization_protocol
order by name;



-- insert data into protocol_file
-- insert_protocol_file.sql

insert into protocol_file
select 100+rownum, protocol_pk_id
from protocol;



-- insert protocol file into lab_file
-- insert_lab_file.sql

insert into lab_file
(file_pk_id, version, created_by, created_date)
select
protocol_file_pk_id, '1.0', 'data_migration', sysdate
from protocol_file;


-- update characterization with protocol_file_pk_id
-- update_characterization.sql

update characterization a
set protocol_file_pk_id=
(select distinct c.PROTOCOL_FILE_PK_ID
from characterization_protocol b, protocol_file c, protocol d
where c.PROTOCOL_PK_ID=d.PROTOCOL_PK_ID
and b.NAME=d.PROTOCOL_NAME
and a.char_protocol_pk_id=b.CHAR_PROTOCOL_PK_ID);

--updating existing function type values
update particle_function
set type='Diagnostic Imaging'
where type='Imaging';

update particle_function
set type='Diagnostic Reporting'
where type='Reporting';

-- Update shape table to set min/max_dimension_unit  to mv
update shape set min_dimension_unit='nm', max_dimension_unit='nm';
 
 -- update Linkage_pk_id to agent_pk_id
update Linkage set linkage_pk_id = agent_pk_id where agent_pk_id is not null;

-- remove orphaned agents
delete from agent where agent_pk_id not in (select linkage_pk_id from linkage);

-- clean up keywords
select a.KEYWORD_PK_ID from keyword a, keyword_nanoparticle b
where a.KEYWORD_PK_ID=b.KEYWORD_PK_ID
and a.NAME is null;

delete from keyword_bioassay_data
where keyword_pk_id in
( select a.KEYWORD_PK_ID
  from keyword a, keyword_bioassay_data b
  where a.KEYWORD_PK_ID=b.KEYWORD_PK_ID
  and a.NAME is null);

delete from keyword
where name is null;
 
-- insert function_type data
INSERT INTO DEF_FUNCTION_TYPE ( FUNCTION_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Therapeutic'); 
INSERT INTO DEF_FUNCTION_TYPE ( FUNCTION_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Targeting'); 
INSERT INTO DEF_FUNCTION_TYPE ( FUNCTION_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Diagnostic Imaging'); 
INSERT INTO DEF_FUNCTION_TYPE ( FUNCTION_TYPE_PK_ID, NAME ) VALUES ( 
4, 'Diagnostic Reporting'); 

--insert characterization_category
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
1, 'Toxicity', 'Oxidative Stress', 1, 2, 1, 'OS'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
2, 'Toxicity', 'Enzyme Induction', 1, 2, 1, 'EI'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
3, 'Cytotoxicity', 'Cell Viability', 1, 3, 2, 'CV'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
4, 'Cytotoxicity', 'Caspase 3 Activation', 1, 3, 2, 'C3'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
5, 'Blood Contact', 'Platelet Aggregation', 1, 5, 3, 'PA'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
6, 'Blood Contact', 'Hemolysis', 1, 5, 3, 'HM'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
7, 'Blood Contact', 'Coagulation', 1, 5, 3, 'CG'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
8, 'Blood Contact', 'Plasma Protein Binding', 1, 5, 3, 'PB'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
9, 'Immune Cell Function', 'Complement Activation', 1, 6, 3, 'CA'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
10, 'Immune Cell Function', 'Phagocytosis', 1, 6, 3, 'PC'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
11, 'Immune Cell Function', 'Chemotaxis', 1, 6, 3, 'CT'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
12, 'Immune Cell Function', 'CFU_GM', 1, 6, 3, 'CU'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
13, 'Immune Cell Function', 'Oxidative Burst', 1, 6, 3, 'OB'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
14, 'Immune Cell Function', 'Leukocyte Proliferation', 1, 6, 3, 'LP'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
15, 'Immune Cell Function', 'Cytokine Induction', 1, 6, 3, 'CI'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
16, 'Immune Cell Function', 'NK Cell Cytotoxic Activity', 1, 6, 3, 'NK'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
17, 'Physical', 'Size', 1, 0, 0, 'SZ'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
18, 'Physical', 'Purity', 1, 0, 0, 'PT'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
19, 'Physical', 'Surface', 1, 0, 0, 'SF'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
21, 'Physical', 'Solubility', 1, 0, 0, 'SL'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
22, 'Physical', 'Molecular Weight', 1, 0, 0, 'MW'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
23, 'Physical', 'Shape', 1, 0, 0, 'SH'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
24, 'Physical', 'Morphology', 1, 0, 0, 'MP'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
25, 'Physical', 'Composition', 1, 0, 0, 'CP'); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
26, 'In Vitro', 'Toxicity', 0, 1, 0, NULL); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
27, 'Toxicity', 'Cytotoxicity', 1, 2, 1, NULL); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
28, 'Toxicity', 'Immunotoxicity', 1, 2, 1, NULL); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
29, 'Immunotoxicity', 'Blood Contact', 0, 4, 2, NULL); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL, NAME_ABBREVIATION ) VALUES ( 
30, 'Immunotoxicity', 'Immune Cell Function', 0, 4, 2, NULL); 


--insert bioassay_data_category (to be added later)
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
1, 'Volume Distribution', 'Size'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
2, 'Number Distribution ', 'Size'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
3, 'Intensity Distribution', 'Size'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
4, 'Mass Distribution', 'Molecular Weight'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
5, 'Height Distribution', 'Size'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
6, 'Radius Vs. Time Distribution', 'Size'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
7, 'Volume Distribution', 'Molecular Weight'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
8, 'Number Distribution', 'Molecular Weight'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
9, 'Intensity Vs. m/z Spectrum Distribution', 'Molecular Weight'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
10, 'Zeta Potential Distribution', 'Surface'); 
INSERT INTO DEF_BIOASSAY_DATA_CATEGORY ( CATEGORY_PK_ID, NAME,
CHARACTERIZATION_NAME ) VALUES ( 
11, 'Hemolytic Properties', 'Hemolysis'); 


-- insert datum name table ( to be added later)
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME) 
VALUES ( 1, 'PDI', 0, 'Size'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME) 
VALUES (2, 'Z-Average', 0, 'Size'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (3, 'Peak 1', 0, 'Size'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES ( 4, 'RMS Size', 0, 'Size'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (5, 'Molecular Weight', 0, 'Molecular Weight'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (6, 'Zeta Potential', 0, 'Surface'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (8, 'LC50', 0, 'Cell Viability'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (9, 'Is Hemolytic', 0, 'Hemolysis'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (10, 'Is Above Threshold', 0, 'Platelet Aggregation'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (11, 'Number of CFU-GM Colonies', 0, 'CFU_GM'); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, IS_DATUM_PARSED, CHARACTERIZATION_NAME)
VALUES (12, 'Total Number of Bone Marrow Cells', 0, 'CFU_GM'); 

-- insert def_characterization_file_type
INSERT INTO DEF_CHARACTERIZATION_FILE_TYPE ( FILE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Image'); 
INSERT INTO DEF_CHARACTERIZATION_FILE_TYPE ( FILE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Graph'); 
INSERT INTO DEF_CHARACTERIZATION_FILE_TYPE ( FILE_TYPE_PK_ID, NAME ) VALUES ( 
3, 'SpreadSheet'); 


-- insert measure type data
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
5, 'threshold'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
6, 'boolean'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'mean'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'median'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
3, 'observed'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
4, 'standard deviation'); 


INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
1, 'Powder'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
2, 'Liquid'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
3, 'Solid'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
4, 'Crystalline'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
5, 'Copolymer'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
6, 'Fibril'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
7, 'Colloid'); 
INSERT INTO DEF_MORPHOLOGY_TYPE ( MORPHOLOGY_TYPE_PK_ID,
NAME ) VALUES ( 
8, 'Oil'); 

INSERT INTO DEF_CELLLINE_TYPE ( CELLLINE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Human Hepatocarinma'); 
INSERT INTO DEF_CELLLINE_TYPE ( CELLLINE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Porcine Renal Tubule'); 

INSERT INTO DEF_MOLECULAR_FORMULA_TYPE ( MOLECULAR_FORMULA_TYPE_PK_ID,
NAME ) VALUES ( 
1, 'SMILES'); 
INSERT INTO DEF_MOLECULAR_FORMULA_TYPE ( MOLECULAR_FORMULA_TYPE_PK_ID,
NAME ) VALUES ( 
2, 'SMARTS'); 

INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Composite'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Cubic'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Cylinder'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
4, 'Elliptical'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
5, 'Ellipsoid'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
6, 'Hexagonal'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
7, 'Irregular'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
8, 'Needle'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
9, 'Oblate'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
10, 'Rod'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
11, 'Spherical'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
12, 'Tetrahedron'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
13, 'Tetrapod'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
14, 'Triangle'); 
INSERT INTO DEF_SHAPE_TYPE ( SHAPE_TYPE_PK_ID, NAME ) VALUES ( 
15, 'Vesicle'); 

INSERT INTO DEF_SOLVENT_TYPE ( SOLVENT_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Pooled Blood Serum (PBS)'); 
INSERT INTO DEF_SOLVENT_TYPE ( SOLVENT_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Saline '); 
INSERT INTO DEF_SOLVENT_TYPE ( SOLVENT_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Water'); 

INSERT INTO DEF_SURFACE_GROUP_TYPE ( SURFACE_GROUP_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Amine'); 
INSERT INTO DEF_SURFACE_GROUP_TYPE ( SURFACE_GROUP_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Carboxyl'); 
INSERT INTO DEF_SURFACE_GROUP_TYPE ( SURFACE_GROUP_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Hydroxyl'); 

INSERT INTO DEF_BOND_TYPE ( BOND_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Covalent'); 
INSERT INTO DEF_BOND_TYPE ( BOND_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Vander Walls'); 
INSERT INTO DEF_BOND_TYPE ( BOND_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Ionic'); 
INSERT INTO DEF_BOND_TYPE ( BOND_TYPE_PK_ID, NAME ) VALUES ( 
4, 'Non-specific'); 

INSERT INTO DEF_ACTIVATION_METHOD ( ACTIVATION_METHOD_PK_ID, NAME ) VALUES ( 
1, 'MRI'); 
INSERT INTO DEF_ACTIVATION_METHOD ( ACTIVATION_METHOD_PK_ID, NAME ) VALUES ( 
2, 'NMR'); 
INSERT INTO DEF_ACTIVATION_METHOD ( ACTIVATION_METHOD_PK_ID, NAME ) VALUES ( 
3, 'Radiation'); 
INSERT INTO DEF_ACTIVATION_METHOD ( ACTIVATION_METHOD_PK_ID, NAME ) VALUES ( 
4, 'Ultrasound'); 
INSERT INTO DEF_ACTIVATION_METHOD ( ACTIVATION_METHOD_PK_ID, NAME ) VALUES ( 
5, 'Ultraviolet'); 


INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
1, 'Cat'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
2, 'Cattle'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
3, 'Dog'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
4, 'Goat'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
5, 'Guinea Pig'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
6, 'Hamster'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
7, 'Horse'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
8, 'Human'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
9, 'Mouse'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
10, 'Pig'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
11, 'Rat'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
12, 'Sheep'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
13, 'Yeast'); 
INSERT INTO DEF_SPECIES_NAME ( SPECIES_NAME_PK_ID, NAME ) VALUES ( 
14, 'Zebrafish'); 

INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
1, 'Flurorescent'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
2, 'Infrared'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
3, 'MRI'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
4, 'Neutron Scattering'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
5, 'PET'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
6, 'SPECT'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
7, 'Ultrasound'); 
INSERT INTO DEF_IMAGE_CONTRAST_AGENT_TYPE ( AGENT_TYPE_PK_ID,
NAME ) VALUES ( 
8, 'X-Ray'); 


INSERT INTO DEF_WALL_TYPE ( WALL_TYPE_PK_ID, NAME ) VALUES ( 
1, 'SWNT'); 
INSERT INTO DEF_WALL_TYPE ( WALL_TYPE_PK_ID, NAME ) VALUES ( 
2, 'DWNT'); 
INSERT INTO DEF_WALL_TYPE ( WALL_TYPE_PK_ID, NAME ) VALUES ( 
3, 'MWNT'); 


-- will need to drop the old table MEASURE_UNIT
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
13, 'a.u', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
21, 'mV', NULL, 'Zeta Potential'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
14, 'aC', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
15, 'Ah', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
16, 'C', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
17, 'esu', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
18, 'Fr', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
19, 'statC', NULL, 'Charge'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
20, 'nm^2', NULL, 'Area'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
1, 'g', NULL, 'Quantity'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
2, 'mg', NULL, 'Quantity'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
3, 'ug', NULL, 'Quantity'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
4, 'g/ml', NULL, 'Concentration'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
5, 'mg/ml', NULL, 'Concentration'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
6, 'ug/ml', NULL, 'Concentration'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
7, 'ug/ul', NULL, 'Concentration'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
8, 'ml', NULL, 'Volume'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
9, 'ul', NULL, 'Volume'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
10, 'nm', NULL, 'Size'); 
INSERT INTO DEF_MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 
12, 'kDa', NULL, 'Molecular Weight'); 

INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Physical assay'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
2, 'In Vivo assay'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
3, 'In Vitro assay'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
4, 'Radio Labeling'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
5, 'Synthesis'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
6, 'Sample Preparation'); 
INSERT INTO DEF_PROTOCOL_TYPE ( PROTOCOL_TYPE_PK_ID, NAME ) VALUES ( 
7, 'Safety'); 


INSERT INTO DEF_FUNCTION_AGENT_TARGET_TYPE ( AGENT_TARGET_TYPE_PK_ID,
NAME ) VALUES ( 
1, 'Receptor'); 
INSERT INTO DEF_FUNCTION_AGENT_TARGET_TYPE ( AGENT_TARGET_TYPE_PK_ID,
NAME ) VALUES ( 
2, 'Antigen'); 

INSERT INTO DEF_FUNCTION_AGENT_TYPE ( AGENT_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Peptide'); 
INSERT INTO DEF_FUNCTION_AGENT_TYPE ( AGENT_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Small Molecule'); 
INSERT INTO DEF_FUNCTION_AGENT_TYPE ( AGENT_TYPE_PK_ID, NAME ) VALUES ( 
3, 'Antibody'); 
INSERT INTO DEF_FUNCTION_AGENT_TYPE ( AGENT_TYPE_PK_ID, NAME ) VALUES ( 
4, 'DNA'); 
INSERT INTO DEF_FUNCTION_AGENT_TYPE ( AGENT_TYPE_PK_ID, NAME ) VALUES ( 
5, 'Image Contrast Agent'); 

INSERT INTO DEF_FUNCTION_LINKAGE_TYPE ( LINKAGE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'Attachment'); 
INSERT INTO DEF_FUNCTION_LINKAGE_TYPE ( LINKAGE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'Encapsulation'); 


INSERT INTO DEF_SAMPLE_TYPE 
	SELECT SAMPLE_TYPE_PK_ID, SAMPLE_TYPE_NAME
	FROM SAMPLE_TYPE;

INSERT INTO DEF_STORAGE_TYPE 
	SELECT STORAGE_TYPE_ID, STORAGE_TYPE 
	FROM STORAGE_TYPE;

INSERT INTO DEF_ASSAY_TYPE 
	SELECT ASSAY_TYPE_PK_ID, ASSAY_TYPE_NAME, DESCRIPTION, EXECUTE_ORDER 
	FROM ASSAY_TYPE;

-- insert "D" as new role
-- insert csm_role_privilege

declare
  v_application_id number;
  v_delete_privilege_id number;
  v_delete_role_cnt integer;
begin
   select application_id into v_application_id
   from csm_application
   where application_name='caNanoLab';

   select privilege_id into v_delete_privilege_id
   from csm_privilege
   where privilege_name='DELETE';
  
   select count(*) into v_delete_role_cnt
   from csm_role
   where role_name='D';
 
   if v_delete_role_cnt=0 then	
   insert into csm_role
   (role_id, role_name, role_description, application_id, active_flag, update_date)
   values
   (csm_role_role_id_seq.nextval, 'D', 'delete only', v_application_id, 1, sysdate);
   
   insert into csm_role_privilege
   (role_privilege_id, role_id, privilege_id, update_date)
   values
   (csm_role_priv_role_privile_seq.nextval, csm_role_role_id_seq.currval, v_delete_privilege_id, sysdate);
   end if;
   
exception
   when no_data_found then
      return;	
end;
/

-- insert csm protection element/usergroup_role/protocol_file
-- insert_csm.sql

insert into csm_protection_element
(protection_element_id, protection_element_name, object_id, application_id, update_date) 
select csm_protectio_protection_e_seq.nextval, file_pk_id, file_pk_id, 2, sysdate
from lab_file
where created_by='data_migration';

insert into csm_protection_group
(protection_group_id, protection_group_name, application_id, large_element_count_flag, update_date)
select csm_protectio_protection_g_seq.nextval, file_pk_id, 2, 0, sysdate
from lab_file
where created_by='data_migration';

insert into csm_pg_pe
select csm_pg_pe_pg_pe_id_seq.nextval, d.PROTECTION_GROUP_ID, b.PROTECTION_ELEMENT_ID, sysdate
from lab_file a, csm_protection_element b, lab_file c, csm_protection_group d
where to_char(a.FILE_PK_ID)=b.PROTECTION_ELEMENT_NAME
and a.created_by='data_migration' 
and c.created_by='data_migration'
and to_char(c.FILE_PK_ID)=d.PROTECTION_GROUP_NAME
and b.PROTECTION_ELEMENT_NAME=d.PROTECTION_GROUP_NAME;

-- merge PI, Researcher and Administrator groups in CSM_GROUP
-- update NCICB prefix
declare
 v_pi_group number;
 v_admin_group number;
 v_researcher_group number;

 cursor c_pis is
 select group_id from csm_group where group_name like '%PI'	 order by group_id;

 cursor c_researchers is
 select group_id from csm_group where group_name like '%Researcher'	order by group_id;

 cursor c_admins is
 select group_id from csm_group where group_name like '%Administrator'	order by group_id;
	 
begin
	 select group_id into v_pi_group
	 from 
	 (select group_id from csm_group
	 where group_name like '%PI'
	 order by group_id)
	 where rownum=1;
	 
 	 select group_id into v_researcher_group
	 from 
	 (select group_id from csm_group
	 where group_name like '%Researcher'
	 order by group_id)
	 where rownum=1;

 	 select group_id into v_admin_group
	 from 
	 (select group_id from csm_group
	 where group_name like '%Administrator'
	 order by group_id)
	 where rownum=1;
	 

	 for i in c_pis loop	 
	    if i.group_id!=v_pi_group then		
		    update csm_user_group
			set group_id=v_pi_group
			where group_id=i.group_id;
			
			update csm_user_group_role_pg
			set group_id=v_pi_group
			where group_id=i.group_id;
			
			delete from csm_group
			where group_id=i.group_id;
		end if;
	 end loop;
	 
	 for i in c_researchers loop
	    if i.group_id!=v_researcher_group then
		    update csm_user_group
			set group_id=v_researcher_group
			where group_id=i.group_id;
			
			update csm_user_group_role_pg
			set group_id=v_researcher_group
			where group_id=i.group_id;
			
			delete from csm_group
			where group_id=i.group_id;
		end if;
	 end loop;
	 
	 for i in c_admins loop	 
	    if i.group_id!=v_admin_group then
		    update csm_user_group
			set group_id=v_admin_group
			where group_id=i.group_id;
			
			update csm_user_group_role_pg
			set group_id=v_admin_group
			where group_id=i.group_id;
			
			delete from csm_group
			where group_id=i.group_id;
		end if;
	 end loop; 
	 
 	 update csm_group
 	 set group_name='&&appowner'||'_PI'
 	 where group_id=v_pi_group;
 
	 update csm_group
	 set group_name='&appowner'||'_Researcher'
	 where group_id=v_researcher_group;
	 
	 update csm_group
	 set group_name='&appowner'||'_Administrator'
 	 where group_id=v_admin_group;
 exception
    when no_data_found then
 return; 	
end; 
/

select csm_pg_pe_pg_pe_id_seq.currval from dual;

-- update csm_user_group_role_pg for protocle file and new protected group "characterization"
-- @update_csm_user_group_role_pg
declare

 v_pi_group number;
 v_admin_group number;
 v_researcher_group number;
 v_read_role number;
 v_delete_role number;
 
begin
  select group_id into v_pi_group 
from CSM_GROUP 
where group_name='&appowner'||'_PI';
    
   select group_id into v_admin_group 
from CSM_GROUP 
where group_name='&appowner'||'_Administrator';

   select group_id into v_researcher_group 
from CSM_GROUP 
where group_name='&appowner'||'_Researcher';

  select role_id into v_read_role
from CSM_ROLE 
where role_name='R';

    select role_id into v_delete_role
from CSM_ROLE 
where role_name='D';
 
    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_pi_group, v_read_role, PROTECTION_GROUP_ID, sysdate
from lab_file a, csm_protection_group b
where a.created_by='data_migration'
and to_char(a.FILE_PK_ID)=b.PROTECTION_GROUP_NAME;

    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_researcher_group, v_read_role, PROTECTION_GROUP_ID, sysdate
from lab_file a, csm_protection_group b
where a.created_by='data_migration'
and to_char(a.FILE_PK_ID)=b.PROTECTION_GROUP_NAME;

exception
    when no_data_found then
	return;
end; 
/




