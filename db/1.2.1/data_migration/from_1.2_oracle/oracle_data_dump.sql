set	wrap off
set linesize 10000
set	feedback off
set	pagesize 0
set	verify off
set termout off
set trimspool on
spool tmp.sql


prompt  select
select  'replace(decode(' || lower(column_name) || ', NULL, ''\N'', ' || lower(column_name) || '), chr(13)||chr(10), '' '') ||chr(9)||'
from    user_tab_columns
where   table_name = upper('&1') and
    column_id != (select max(column_id) from user_tab_columns where
             table_name = upper('&1'))
order by column_id
/
select  'replace(decode(' || lower(column_name) || ', NULL, ''\N'', ' || lower(column_name) || '), chr(13)||chr(10), '' '')'
from    user_tab_columns
where   table_name = upper('&1') and
    column_id = (select max(column_id) from user_tab_columns where
             table_name = upper('&1'))
			 order by column_id
/
prompt  from    &1
prompt  /

spool off
set linesize 10000
set termout on
alter session set nls_date_format='yyyy-mm-dd';
spool &1
@tmp.sql
spool off
exit
