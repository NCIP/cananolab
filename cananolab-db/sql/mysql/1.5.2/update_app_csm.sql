/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

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
UPDATE csm_protection_element pe,
       sample s
   SET pe.protection_element_name = s.sample_pk_id,
       pe.object_id = s.sample_pk_id
 WHERE pe.protection_element_name = s.sample_name;

/* update pg to use sample ID */
update csm_protection_group pg, sample s
set pg.protection_group_name=s.sample_pk_id
where pg.protection_group_name=s.sample_name;

/* delete groups that are not Curator or Public */
DELETE FROM csm_user_group_role_pg
 WHERE group_id IN (SELECT group_id
                      FROM csm_group
                     WHERE group_name NOT IN ('Curator', 'Public'));

DELETE FROM csm_user_group
 WHERE group_id IN (SELECT group_id
                      FROM csm_group
                     WHERE group_name NOT IN ('Curator', 'Public'));

DELETE FROM csm_group
 WHERE group_name NOT IN ('Curator', 'Public');

UPDATE csm_user
   SET first_name = 'superadmin', last_name = 'superadmin'
 WHERE login_name = 'superadmin';