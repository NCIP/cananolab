/*
Aug. 1, 2008

In WashU 1.3 data, there is one particle function linked to 
different linkages in the linkage table.
The particle that has this duplicated function is WUSTL-121. In the
linkage table, the duplicated function_pk_id is 7078043, which is linked
to both linkage_pk_id 7176363 and 7176364.

When the migration script runs into this data duplication error, it
skips the rest of the records in the linkage table and results in an
incomplete data migration of the target function. 

This script deletes linkage record with linkage_pk_id = 7176364.
*/

use cananolab;

delete from cananolab.linkage
where linkage_pk_id = 7176364
;

commit;