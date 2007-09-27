INSERT INTO `cananolab121`.`csm_group`(`group_name`, `group_desc`, `update_date`, `application_id`)
VALUES ('Public', 'caBIG and public', sysdate(), 2);

INSERT INTO `cananolab121`.`csm_protection_element`(`protection_element_name`, `protection_element_description`, `object_id`, `attribute`, `protection_element_type_id`, `application_id`, `update_date`)
VALUES ('workflow', 'caNanoLab workflow related data', 'workflow', NULL, NULL, 2, sysdate()),
  ('inventory', 'caNanoLab inventory related data', 'inventory', NULL, NULL, 2, sysdate()),
  ('search', 'caNanoLab search related data', 'search', NULL, NULL, 2, sysdate()),
  ('submit', 'caNanoLab annotation submission related data', 'submit', NULL, NULL, 2, sysdate()),
  ('administration', 'caNanoLab administration related data', 'administration', NULL, NULL, 2, sysdate()),
  ('remote search', 'caNanoLab remote search related data', 'remote search', NULL, NULL, 2, sysdate());

INSERT INTO `cananolab121`.`csm_protection_group`(`protection_group_name`, `protection_group_description`, `application_id`, `large_element_count_flag`, `update_date`, `parent_protection_group_id`)
VALUES ('workflow', NULL, 2, 0, sysdate(), NULL),
  ('inventory', NULL, 2, 0, sysdate(), NULL),
  ('search', NULL, 2, 0, sysdate(), NULL),
  ('submit', NULL, 2, 0, sysdate(), NULL),
  ('administration', NULL, 2, 0, sysdate(), NULL),
  ('remote search', NULL, 2, 0, sysdate(), NULL);

INSERT INTO `cananolab121`.`csm_pg_pe`(`protection_group_id`, `protection_element_id`, `update_date`)
VALUES (2, 4, sysdate()),
  (1, 3, sysdate()),
  (3, 5, sysdate()),
  (4, 6, sysdate()),
  (5, 7, sysdate()),
  (6, 8, sysdate());

INSERT INTO `cananolab121`.`csm_role`(`role_name`, `role_description`, `application_id`, `active_flag`, `update_date`)
VALUES ('R', 'read only', 2, 1, sysdate()),
  ('E', 'execute only', 2, 1, sysdate());

INSERT INTO `cananolab121`.`csm_role_privilege`(`role_id`, `privilege_id`, `update_date`)
VALUES (2, 7, sysdate()),
  (1, 3, sysdate());

INSERT INTO `cananolab121`.`csm_user_group`(`user_id`, `group_id`)
VALUES (1, 1);

INSERT INTO `cananolab121`.`csm_user_group_role_pg`(`user_id`, `group_id`, `role_id`, `protection_group_id`, `update_date`)
VALUES (NULL, 1, 2, 3, sysdate()),
  (NULL, 1, 2, 6, sysdate());

