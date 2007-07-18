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
select char_protocol_pk_id, name, 'In vitro assay'
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

--update manufacturer_pk_id to be null in instrument where manufacturer name is null
update instrument
set manufacturer_pk_id=null
where manufacturer_pk_id in
(select b.manufacturer_pk_id
from manufacturer b
where b.name is null);

--delete manufacturer where name is null
delete from manufacturer
where name is null;

--update instrument_pk_id to be null in characterization where instrument information is empty
update characterization
set instrument_pk_id=null
where instrument_pk_id in
(select instrument_pk_id
from instrument a, instrument_type b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID=c.MANUFACTURER_PK_ID
and b.NAME is null and c.NAME is null);

--delete rows where instrument name and manufacturer name are null
delete from instrument
where instrument_pk_id
in (select instrument_pk_id
from instrument a, instrument_type b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID=c.MANUFACTURER_PK_ID
and b.NAME is null and c.NAME is null);

--update instrument set instrument_type_pk_id to null where instrument_type is empty
update instrument a
set instrument_type_pk_id=null
where exists
(select b.instrument_type_pk_id
from instrument_type b
where b.name is null
and a.instrument_type_pk_id=b.instrument_type_pk_id);

--delete from instrument where name is null
delete from instrument_type
where name is null;

-- @remove_redundant_instrument.sql
--delete redundant rows in instrument and update characterization instrument
declare
  cursor c_redundants is
select count(distinct a.instrument_pk_id), a.instrument_type_pk_id, a.manufacturer_pk_id
from instrument a, instrument b
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.manufacturer_pk_id=b.manufacturer_pk_id
and a.instrument_pk_id!=b.instrument_pk_id
group by (a.instrument_type_pk_id, a.manufacturer_pk_id);

  v_instrument_pk_id number;
  
begin
  for i in c_redundants loop
    --select the first one in the redudnant list
    select instrument_pk_id
	into v_instrument_pk_id
	from instrument
	where instrument_type_pk_id=i.instrument_type_pk_id
	and manufacturer_pk_id=i.manufacturer_pk_id
	and rownum=1;
	
	--update to use the first one
	update characterization chara
	set instrument_pk_id=v_instrument_pk_id
	where exists
	(select instrument_pk_id
	from instrument instr
	where chara.instrument_pk_id=instr.instrument_pk_id
	and instr.instrument_type_pk_id=i.instrument_type_pk_id
	and instr.manufacturer_pk_id=i.manufacturer_pk_id);
	
	--delete the other redundnant ones
	delete from instrument
	where instrument_pk_id!=v_instrument_pk_id
	and instrument_type_pk_id=i.instrument_type_pk_id
	and manufacturer_pk_id=i.manufacturer_pk_id;
	
  end loop;
end;
/


--insert into instrument_tmp
insert into instrument_tmp
select rownum, tp, abbr, mf 
from (
select
a.name tp, a.abbreviation abbr, c.name mf
from instrument_type a, instrument_type_manufacturer b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and b.manufacturer_pk_id=c.manufacturer_pk_id 
union
select y.NAME tp, y.ABBREVIATION abbr, z.NAME mf
from instrument x, instrument_type y, manufacturer z
where x.INSTRUMENT_TYPE_PK_ID=y.INSTRUMENT_TYPE_PK_ID
and x.MANUFACTURER_PK_ID=z.MANUFACTURER_PK_ID);

--insert data in instrument into instrument_config 
insert into instrument_config
select a.instrument_pk_id, a.description, e.instrument_pk_id 
from instrument a, instrument_type b, manufacturer d, instrument_tmp e 
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID = d.MANUFACTURER_PK_ID
and e.type=b.name
and e.manufacturer=d.name
and e.abbreviation=b.abbreviation;


-- update characterization to associate with instrument_config
update characterization  chara
set instrument_config_pk_id=instrument_pk_id
where exists
(select instrument_config_pk_id 
from instrument_config config
where chara.instrument_pk_id = config.instrument_config_pk_id);


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
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
1, 'Toxicity', 'Oxidative Stress', 1, 2, 1); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
2, 'Toxicity', 'Enzyme Induction', 1, 2, 1); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
3, 'Cytotoxicity', 'Cell Viability', 1, 3, 2); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
4, 'Cytotoxicity', 'Caspase 3 Activation', 1, 3, 2); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
5, 'Blood Contact', 'Platelet Aggregation', 1, 5, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
6, 'Blood Contact', 'Hemolysis', 1, 5, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
7, 'Blood Contact', 'Coagulation', 1, 5, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
8, 'Blood Contact', 'Plasma Protein Binding', 1, 5, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
9, 'Immune Cell Function', 'Complement Activation', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
10, 'Immune Cell Function', 'Phagocytosis', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
11, 'Immune Cell Function', 'Chemotaxis', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
12, 'Immune Cell Function', 'CFU_GM', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
13, 'Immune Cell Function', 'Oxidative Burst', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
14, 'Immune Cell Function', 'Leukocyte Proliferation', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
15, 'Immune Cell Function', 'Cytokine Induction', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
16, 'Immune Cell Function', 'NK Cell Cytotoxic Activity', 1, 6, 3); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
17, 'Physical', 'Size', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
18, 'Physical', 'Purity', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
19, 'Physical', 'Surface', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
21, 'Physical', 'Solubility', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
22, 'Physical', 'Molecular Weight', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
23, 'Physical', 'Shape', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
24, 'Physical', 'Morphology', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
25, 'Physical', 'Composition', 1, 0, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
26, 'In Vitro', 'Toxicity', 0, 1, 0); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
27, 'Toxicity', 'Cytotoxicity', 1, 2, 1); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
28, 'Toxicity', 'Immunotoxicity', 1, 2, 1); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
29, 'Immunotoxicity', 'Blood Contact', 0, 4, 2); 
INSERT INTO DEF_CHARACTERIZATION_CATEGORY ( CHAR_CATEGORY_PK_ID, CATEGORY, NAME, HAS_ACTION,
CATEGORY_ORDER, INDENT_LEVEL ) VALUES ( 
30, 'Immunotoxicity', 'Immune Cell Function', 0, 4, 2); 

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

-- insert datum name table ( to be added later)
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, CATEGORY_PK_ID,
IS_DATUM_PARSED ) VALUES ( 
1, 'Average/Mean', 1, 0); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, CATEGORY_PK_ID,
IS_DATUM_PARSED ) VALUES ( 
2, 'PDI', 1, 0); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, CATEGORY_PK_ID,
IS_DATUM_PARSED ) VALUES ( 
3, 'Average/Mean', 2, 0); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, CATEGORY_PK_ID,
IS_DATUM_PARSED ) VALUES ( 
4, 'PDI', 2, 0); 
INSERT INTO DEF_DATUM_NAME ( DATUM_NAME_PK_ID, NAME, CATEGORY_PK_ID,
IS_DATUM_PARSED ) VALUES ( 
5, 'Z-Average', 3, 0); 

-- insert measure type data
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
1, 'mean'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
2, 'median'); 
INSERT INTO DEF_MEASURE_TYPE ( MEASURE_TYPE_PK_ID, NAME ) VALUES ( 
3, 'observed'); 

-- insert into measure_unit first, then in alter_table.sql will rename the table to DEF_Measure_unit
INSERT INTO MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 10, 'nm', NULL, 'Size'); 
INSERT INTO MEASURE_UNIT ( MEASURE_UNIT_PK_ID, UNIT_NAME, DESCRIPTION,
UNIT_TYPE ) VALUES ( 12, 'kDa', NULL, 'Molecular Weight'); 


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

    insert into csm_user_group_role_pg
select csm_user_grou_user_group_r_seq.nextval, null, v_admin_group, v_delete_role, PROTECTION_GROUP_ID, sysdate
from csm_protection_group b
where b.PROTECTION_GROUP_NAME='characterization';

end; 
/




