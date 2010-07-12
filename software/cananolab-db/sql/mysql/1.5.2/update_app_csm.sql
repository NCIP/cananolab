/* remove protection on sample, publication, protocol */
DELETE FROM csm_user_group_role_pg
 WHERE protection_group_id IN
          (SELECT protection_group_id
             FROM csm_protection_group
            WHERE csm_protection_group.protection_group_name IN
                     ('sample', 'publication', 'protocol'));

DELETE FROM csm_pg_pe
 WHERE protection_group_id IN
          (SELECT protection_group_id
             FROM csm_protection_group
            WHERE csm_protection_group.protection_group_name IN
                     ('sample', 'publication', 'protocol'));

DELETE FROM csm_protection_group
 WHERE protection_group_name IN ('sample', 'publication', 'protocol');

DELETE FROM csm_protection_element
 WHERE protection_element_name IN ('sample', 'publication', 'protocol');

/* remove application owner from Curator group */
UPDATE csm_group
   SET group_name = 'Curator'
 WHERE group_name = 'NCICBIIT_DataCurator';

/* update pe to use sample ID */
update csm_protection_element pe, sample s
set pe.protection_element_name=s.sample_pk_id, pe.object_id=s.sample_pk_id
where pe.protection_element_name=s.sample_name;

/* update pg to use sample ID */
update csm_protection_group pg, sample s
set pg.protection_group_name=s.sample_pk_id
where pg.protection_group_name=s.sample_name;

/* update data to be CURD by curator */
update csm_user_group_role_pg
set role_id=5
where role_id=1
and group_id=2;