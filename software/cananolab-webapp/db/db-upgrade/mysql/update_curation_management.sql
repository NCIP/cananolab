--remove protection on sample, publication, protocol

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

UPDATE csm_group
   SET group_name = 'Curator'
 WHERE group_name = 'NCICBIIT_DataCurator';