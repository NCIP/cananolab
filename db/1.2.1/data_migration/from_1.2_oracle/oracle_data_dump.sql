set	wrap off
set linesize 1000
set	feedback off
set	pagesize 0
set	verify off
set termout off
set trimspool on

spool tmp.sql


prompt  select
select  lower(column_name)||'||chr(9)||'
from    user_tab_columns
where   table_name = upper('&1') and
    column_id != (select max(column_id) from user_tab_columns where
             table_name = upper('&1'))
order by column_id
/
select  lower(column_name)
from    user_tab_columns
where   table_name = upper('&1') and
    column_id = (select max(column_id) from user_tab_columns where
             table_name = upper('&1'))
			 order by column_id
/
prompt  from    &1
prompt  /

spool off
set termout on
spool &1
@tmp.sql
spool off
exit
