USE canano;
# 
# The following entries creates a super admin application incase you decide 
# to use this database to run UPT also. In that case you need to provide
# the project login id and name for the super admin.
# However in incase you are using this database just to host the application
# authorization schema, these enteries are not used and hence they can be left as 
# it is.
#

insert into csm_application(application_name,application_description,declarative_flag,active_flag,update_date)
values ("caNanoLab-upt","CSM UPT Super Admin Application",0,0,sysdate());

insert into csm_user (login_name,first_name,last_name,password,update_date)
values ("superadmin","super_admin_first_name","super_admin_last_name","W6ph5Mm5Pz8GgiULbPgzG37mj9g=",sysdate());
 
insert into csm_protection_element(protection_element_name,protection_element_description,object_id,application_id,update_date)
values("caNanoLab-upt","CSM UPT Super Admin Application Protection Element","caNanoLab-upt",1,sysdate());

insert into csm_user_pe(protection_element_id,user_id,update_date)
values(1,1,sysdate());

# 
# The following entry is for your application. 
# Replace caNanoLab with your application name.
#

insert into csm_application(application_name,application_description,declarative_flag,active_flag,update_date)
values ("caNanoLab","Application Description",0,0,sysdate());

insert into csm_protection_element(protection_element_name,protection_element_description,object_id,application_id,update_date)
values("caNanoLab","caNanoLab Admin Application Protection Element","caNanoLab",1,sysdate());

#
# The following entries are Common Set of Privileges
#

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("CREATE","This privilege grants permission to a user to create an entity. This entity can be an object, a database entry, or a resource such as a network connection", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("ACCESS","This privilege allows a user to access a particular resource.  Examples of resources include a network or database connection, socket, module of the application, or even the application itself", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("READ","This privilege permits the user to read data from a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to read data about a particular entry", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("WRITE","This privilege allows a user to write data to a file, URL, database, an object, etc. This can be used at an entity level signifying that the user is allowed to write data about a particular entity", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("UPDATE","This privilege grants permission at an entity level and signifies that the user is allowed to update data for a particular entity. Entities may include an object, object attribute, database row etc", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("DELETE","This privilege permits a user to delete a logical entity. This entity can be an object, a database entry, a resource such as a network connection, etc", sysdate());

insert into csm_privilege (privilege_name, privilege_description, update_date)
values("EXECUTE","This privilege allows a user to execute a particular resource. The resource can be a method, function, behavior of the application, URL, button etc", sysdate());


COMMIT;
