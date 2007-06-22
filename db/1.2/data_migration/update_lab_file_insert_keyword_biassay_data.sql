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