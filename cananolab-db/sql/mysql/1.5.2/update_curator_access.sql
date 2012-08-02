/* update to be CURD by curator */

UPDATE csm_user_group_role_pg
   SET role_id = 5
 WHERE role_id = 1 AND group_id = 2;

/* insert public data to be CURD by curator */

INSERT INTO csm_user_group_role_pg(group_id,
                                   role_id,
                                   update_date,
                                   protection_group_id)
   SELECT DISTINCT '2',
                   '5',
                   sysdate(),
                   protection_group_id
     FROM csm_user_group_role_pg
    WHERE role_id = 1;

/* create curation as PG and PE and assign Curator CURD access */
INSERT INTO csm_protection_element(protection_element_name,
                                   protection_element_description,
                                   object_id,
                                   attribute,
                                   protection_element_type,
                                   application_id,
                                   update_date)
VALUES ('curation', '', 'curation', '', NULL, 2, sysdate());

INSERT INTO csm_protection_group(protection_group_name,
                                 protection_group_description,
                                 application_id,
                                 large_element_count_flag,
                                 update_date,
                                 parent_protection_group_id)
VALUES ('curation', '', 2, 0, sysdate(), NULL);

INSERT INTO csm_pg_pe(protection_group_id,
                      protection_element_id,
                      update_date)
   SELECT pg.protection_group_id, pe.protection_element_id, sysdate()
     FROM csm_protection_group pg, csm_protection_element pe
    WHERE pg.protection_group_name = pe.protection_element_name
          AND pg.protection_group_name = 'curation';

INSERT INTO csm_user_group_role_pg(group_id,
                                   role_id,
                                   update_date,
                                   protection_group_id)
   SELECT DISTINCT '2',
                   '5',
                   sysdate(),
                   pg.protection_group_id
     FROM csm_protection_group pg
    WHERE pg.protection_group_name = 'curation';

