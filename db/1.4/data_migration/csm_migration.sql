use canano;


INSERT INTO canano.csm_application ( 
	application_id,
	application_name,
	application_description,
	declarative_flag,
	active_flag,
	update_date
)
SELECT
	application_id,
	application_name,
	application_description,
	declarative_flag,
	active_flag,
	update_date
FROM cananolab.csm_application
;

INSERT INTO canano.csm_group ( 
	group_id,
	group_name,
	group_desc,
	update_date,
	application_id
)
SELECT
	group_id,
	group_name,
	group_desc,
	update_date,
	application_id
FROM cananolab.csm_group
;

INSERT INTO canano.csm_privilege ( 
	privilege_id,
	privilege_name,
	privilege_description,
	update_date
)
SELECT
	privilege_id,
	privilege_name,
	privilege_description,
	update_date
FROM cananolab.csm_privilege
;

INSERT INTO canano.csm_protection_element ( 
	protection_element_id,
	protection_element_name,
	protection_element_description,
	object_id,
	attribute,
	protection_element_type_id,
	application_id,
	update_date
)SELECT
	protection_element_id,
	protection_element_name,
	protection_element_description,
	object_id,
	attribute,
	protection_element_type_id,
	application_id,
	update_date
FROM cananolab.csm_protection_element
;

INSERT INTO canano.csm_protection_group ( 
	protection_group_id,
	protection_group_name,
	protection_group_description,
	application_id,
	large_element_count_flag,
	update_date,
	parent_protection_group_id
)SELECT
	protection_group_id,
	protection_group_name,
	protection_group_description,
	application_id,
	large_element_count_flag,
	update_date,
	parent_protection_group_id
FROM cananolab.csm_protection_group
;

INSERT INTO canano.csm_pg_pe ( 
	pg_pe_id,
	protection_group_id,
	protection_element_id,
	update_date
)SELECT
	pg_pe_id,
	protection_group_id,
	protection_element_id,
	update_date
FROM cananolab.csm_pg_pe
;

INSERT INTO canano.csm_role ( 
	role_id,
	role_name,
	role_description,
	application_id,
	active_flag,
	update_date
)SELECT
	role_id,
	role_name,
	role_description,
	application_id,
	active_flag,
	update_date
FROM cananolab.csm_role
;

INSERT INTO canano.csm_role_privilege ( 
	role_privilege_id,
	role_id,
	privilege_id,
	update_date
)SELECT
	role_privilege_id,
	role_id,
	privilege_id,
	update_date
FROM cananolab.csm_role_privilege
;

INSERT INTO canano.csm_user ( 
	user_id,
	login_name,
	first_name,
	last_name,
	organization,
	department,
	title,
	phone_number,
	password,
	email_id,
	start_date,
	end_date,
	update_date
)SELECT
	user_id,
	login_name,
	first_name,
	last_name,
	organization,
	department,
	title,
	phone_number,
	password,
	email_id,
	start_date,
	end_date,
	update_date
FROM cananolab.csm_user
;

INSERT INTO canano.csm_user_group ( 
	user_group_id,
	user_id,
	group_id
)SELECT
	user_group_id,
	user_id,
	group_id
FROM cananolab.csm_user_group
;

INSERT INTO canano.csm_user_group_role_pg ( 
	user_group_role_pg_id,
	user_id,
	group_id,
	role_id,
	protection_group_id,
	update_date
)SELECT
	user_group_role_pg_id,
	user_id,
	group_id,
	role_id,
	protection_group_id,
	update_date
FROM cananolab.csm_user_group_role_pg
;

INSERT INTO canano.csm_user_pe ( 
	user_protection_element_id,
	protection_element_id,
	user_id,
	update_date
)SELECT
	user_protection_element_id,
	protection_element_id,
	user_id,
	update_date
FROM cananolab.csm_user_pe
;

