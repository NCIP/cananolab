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

/* update 0 data to null */
UPDATE associated_element
   SET value = NULL
 WHERE value = 0;

UPDATE fullerene
   SET average_diameter = NULL
 WHERE average_diameter = 0;

UPDATE dendrimer
   SET generation = NULL
 WHERE generation = 0;

UPDATE carbon_nanotube
   SET average_length = NULL
 WHERE average_length = 0;

UPDATE carbon_nanotube
   SET diameter = NULL
 WHERE diameter = 0;

UPDATE fullerene
   SET number_of_carbon = NULL
 WHERE number_of_carbon = 0;

UPDATE polymer
   SET cross_link_degree = NULL
 WHERE cross_link_degree = 0;

UPDATE shape
   SET aspect_ratio = NULL
 WHERE aspect_ratio = 0;

UPDATE solubility
   SET critical_concentration = NULL
 WHERE critical_concentration = 0;