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