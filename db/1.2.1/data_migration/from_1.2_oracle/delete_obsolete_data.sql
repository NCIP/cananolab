delete from report
where report_pk_id not in
(select file_pk_id
from lab_file);

delete from associated_file
where associated_file_pk_id not in
(select file_pk_id
from lab_file);