--  insert data into bioassay_data_data_category
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
		(derived_bioassay_data_pk_id, category_index, category_name, update_date)
		values (v_data_pk_id, v_category_index, i.category, sysdate);
		
		v_category_index := v_category_index + 1;
		
	 end loop;
end;


alter table DERIVED_BIOASSAY_DATA drop (CATEGORY); 

commit;