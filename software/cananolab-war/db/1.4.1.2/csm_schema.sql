use canano;

drop table if exists csm_application
;

drop table if exists csm_group
;

drop table if exists csm_privilege
;

drop table if exists csm_protection_element
;

drop table if exists csm_protection_group
;

drop table if exists csm_pg_pe
;

drop table if exists csm_role
;

drop table if exists csm_role_privilege
;

drop table if exists csm_user
;

drop table if exists csm_user_group
;

drop table if exists csm_user_group_role_pg
;

drop table if exists csm_user_pe
;


create table csm_application ( 
	application_id bigint auto_increment  not null,
	application_name varchar(100) not null,
	application_description varchar(200) not null,
	declarative_flag bool,
	active_flag bool not null,
	update_date date not null,
	primary key(application_id)
)engine=innodb
;

create table csm_group ( 
	group_id bigint auto_increment  not null,
	group_name varchar(100) not null,
	group_desc varchar(200),
	update_date date not null,
	application_id bigint not null,
	primary key(group_id)
)engine=innodb
;

create table csm_privilege ( 
	privilege_id bigint auto_increment  not null,
	privilege_name varchar(100) not null,
	privilege_description varchar(200),
	update_date date not null,
	primary key(privilege_id)
)engine=innodb
;

create table csm_protection_element ( 
	protection_element_id bigint auto_increment  not null,
	protection_element_name varchar(100) not null,
	protection_element_description varchar(200),
	object_id varchar(100) not null,
	attribute varchar(100) ,
	protection_element_type_id numeric(10),
	application_id bigint not null,
	update_date date not null,
	primary key(protection_element_id)
)engine=innodb
;

create table csm_protection_group ( 
	protection_group_id bigint auto_increment  not null,
	protection_group_name varchar(100) not null,
	protection_group_description varchar(200),
	application_id bigint not null,
	large_element_count_flag bool not null,
	update_date date not null,
	parent_protection_group_id bigint,
	primary key(protection_group_id)
)engine=innodb
;

create table csm_pg_pe ( 
	pg_pe_id bigint auto_increment  not null,
	protection_group_id bigint not null,
	protection_element_id bigint not null,
	update_date date not null default 0,
	primary key(pg_pe_id)
)engine=innodb
;

create table csm_role ( 
	role_id bigint auto_increment  not null,
	role_name varchar(100) not null,
	role_description varchar(200),
	application_id bigint not null,
	active_flag bool not null,
	update_date date not null,
	primary key(role_id)
)engine=innodb
;

create table csm_role_privilege ( 
	role_privilege_id bigint auto_increment  not null,
	role_id bigint not null,
	privilege_id bigint not null,
	update_date date not null default 0,
	primary key(role_privilege_id)
)engine=innodb
;

create table csm_user ( 
	user_id bigint auto_increment  not null,
	login_name varchar(100) not null,
	first_name varchar(100) not null,
	last_name varchar(100) not null,
	organization varchar(100),
	department varchar(100),
	title varchar(100),
	phone_number varchar(15),
	password varchar(100),
	email_id varchar(100),
	start_date date,
	end_date date,
	update_date date not null,
	primary key(user_id)
)engine=innodb
;

create table csm_user_group ( 
	user_group_id bigint auto_increment  not null,
	user_id bigint not null,
	group_id bigint not null,
	primary key(user_group_id)
)engine=innodb
;

create table csm_user_group_role_pg ( 
	user_group_role_pg_id bigint auto_increment  not null,
	user_id bigint,
	group_id bigint,
	role_id bigint not null,
	protection_group_id bigint not null,
	update_date date not null default 0,
	primary key(user_group_role_pg_id)
)engine=innodb
;

create table csm_user_pe ( 
	user_protection_element_id bigint auto_increment  not null,
	protection_element_id bigint not null,
	user_id bigint not null,
	update_date date not null default 0,
	primary key(user_protection_element_id)
)engine=innodb
;



alter table csm_application
add constraint uq_application_name unique (application_name)
;
create index idx_application_id on csm_group(application_id)
;
alter table csm_group
add constraint uq_group_group_name unique (application_id, group_name)
;
alter table csm_privilege
add constraint uq_privilege_name unique (privilege_name)
;
create index idx_application_id on csm_protection_element(application_id)
;
alter table csm_protection_element
add constraint uq_pe_pe_name_attribute_app_id unique (object_id, attribute, application_id)
;
create index idx_application_id on csm_protection_group(application_id)
;
alter table csm_protection_group
add constraint uq_protection_group_protection_group_name unique (application_id, protection_group_name)
;
create index idx_parent_protection_group_id on csm_protection_group(parent_protection_group_id)
;
create index idx_protection_element_id on csm_pg_pe(protection_element_id)
;
create index idx_protection_group_id on csm_pg_pe(protection_group_id)
;
alter table csm_pg_pe
add constraint uq_protection_group_protection_element_protection_group_id unique (protection_element_id, protection_group_id)
;
create index idx_application_id on csm_role(application_id)
;
alter table csm_role
add constraint uq_role_role_name unique (application_id, role_name)
;
create index idx_privilege_id on csm_role_privilege(privilege_id)
;
alter table csm_role_privilege
add constraint uq_role_privilege_role_id unique (privilege_id, role_id)
;
create index idx_role_id on csm_role_privilege(role_id)
;
alter table csm_user
add constraint uq_login_name unique (login_name)
;
create index idx_user_id on csm_user_group(user_id)
;
create index idx_group_id on csm_user_group(group_id)
;
create index idx_group_id on csm_user_group_role_pg(group_id)
;
create index idx_role_id on csm_user_group_role_pg(role_id)
;
create index idx_protection_group_id on csm_user_group_role_pg(protection_group_id)
;
create index idx_user_id on csm_user_group_role_pg(user_id)
;
create index idx_user_id on csm_user_pe(user_id)
;
create index idx_protection_element_id on csm_user_pe(protection_element_id)
;
alter table csm_user_pe
add constraint uq_user_protection_element_protection_element_id unique (user_id, protection_element_id)
;


alter table csm_group add constraint fk_application_group 
foreign key (application_id) references csm_application (application_id)
on delete cascade
;

alter table csm_protection_element add constraint fk_pe_application 
foreign key (application_id) references csm_application (application_id)
on delete cascade
;

alter table csm_protection_group add constraint fk_pg_application 
foreign key (application_id) references csm_application (application_id)
on delete cascade
;

alter table csm_protection_group add constraint fk_protection_group 
foreign key (parent_protection_group_id) references csm_protection_group (protection_group_id)
;

alter table csm_pg_pe add constraint fk_protection_element_protection_group 
foreign key (protection_element_id) references csm_protection_element (protection_element_id)
on delete cascade
;

alter table csm_pg_pe add constraint fk_protection_group_protection_element 
foreign key (protection_group_id) references csm_protection_group (protection_group_id)
on delete cascade
;

alter table csm_role add constraint fk_application_role 
foreign key (application_id) references csm_application (application_id)
on delete cascade
;

alter table csm_role_privilege add constraint fk_privilege_role 
foreign key (privilege_id) references csm_privilege (privilege_id)
on delete cascade
;

alter table csm_role_privilege add constraint fk_role 
foreign key (role_id) references csm_role (role_id)
on delete cascade
;

alter table csm_user_group add constraint fk_user_group 
foreign key (user_id) references csm_user (user_id)
on delete cascade
;

alter table csm_user_group add constraint fk_ug_group 
foreign key (group_id) references csm_group (group_id)
on delete cascade
;

alter table csm_user_group_role_pg add constraint fk_user_group_role_protection_group_groups 
foreign key (group_id) references csm_group (group_id)
on delete cascade
;

alter table csm_user_group_role_pg add constraint fk_user_group_role_protection_group_role 
foreign key (role_id) references csm_role (role_id)
on delete cascade
;

alter table csm_user_group_role_pg add constraint fk_user_group_role_protection_group_protection_group 
foreign key (protection_group_id) references csm_protection_group (protection_group_id)
on delete cascade
;

alter table csm_user_group_role_pg add constraint fk_user_group_role_protection_group_user 
foreign key (user_id) references csm_user (user_id)
on delete cascade
;

alter table csm_user_pe add constraint fk_pe_user 
foreign key (user_id) references csm_user (user_id)
on delete cascade
;

alter table csm_user_pe add constraint fk_protection_element_user 
foreign key (protection_element_id) references csm_protection_element (protection_element_id)
on delete cascade
;

commit;
