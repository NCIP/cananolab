/* update common_lookup */

UPDATE common_lookup
   SET value = 'sterility'
 WHERE value = 'Sterility' AND name = 'protocol' AND attribute = 'otherType';

/* delete obsoleted csm records */
DELETE FROM csm_user_group_role_pg
 WHERE protection_group_id IN
          (SELECT protection_group_id
             FROM csm_protection_group
            WHERE protection_group_name REGEXP '[:alpha:]'
                  AND protection_group_name NOT IN
                         ('csmupt', 'caNanoLab', 'curation')
                  AND protection_group_name NOT LIKE
                         'CollaborationGroup%');

DELETE FROM csm_pg_pe
 WHERE protection_group_id IN
          (SELECT protection_group_id
             FROM csm_protection_group
            WHERE protection_group_name REGEXP '[:alpha:]'
                  AND protection_group_name NOT IN
                         ('csmupt', 'caNanoLab', 'curation')
                  AND protection_group_name NOT LIKE 'CollaborationGroup%');

DELETE FROM csm_protection_group
 WHERE protection_group_name REGEXP '[:alpha:]'
       AND protection_group_name NOT IN
              ('csmupt', 'caNanoLab', 'curation')
       AND protection_group_name NOT LIKE 'CollaborationGroup%';

DELETE FROM csm_protection_element
 WHERE protection_element_name REGEXP '[:alpha:]'
       AND protection_element_name NOT IN
              ('csmupt', 'caNanoLab', 'curation')
       AND protection_element_name NOT LIKE 'CollaborationGroup%';