-- disable foreign key checks
set @old_foreign_key_checks=@@foreign_key_checks, foreign_key_checks=0;

use cananolab;

drop table assay;
drop table def_assay_type;
drop table project;
drop table project_sample;
drop table run;
drop table run_input_file;
drop table run_output_file;
drop table run_sample_container;

drop table if exists def_composing_element_type;
create table def_composing_element_type (
   composing_element_type_pk_id bigint not null,
   name varchar (200) not null,
   primary key (composing_element_type_pk_id)
)
engine=innodb;

insert into def_composing_element_type(composing_element_type_pk_id, name)
values (1, 'core'),
  (2, 'shell'),
  (3, 'coating'),
  (4, 'monomer'),
  (5, 'lipid'),
  (6, 'modification'),
  (7, 'oil'),
  (8, 'pfc'),
  (9, 'drug'),
  (10, 'image contrast agent');

commit;

delete from def_characterization_category
where category='Physical'
and name='Composition';

update def_protocol_type
set name = 'Physical Assay'
where name like 'Physical assay%'
;

update def_protocol_type
set name = 'In Vivo Assay'
where name like 'In Vivo assay%'
;

update def_protocol_type
set name = 'In Vitro Assay'
where name like 'In Vitro assay%'
;

update protocol
set protocol_type = 'In Vitro Assay'
where protocol_type like 'In Vitro assay%'
;

update protocol
set protocol_type = 'In Vivo Assay'
where protocol_type like 'In Vivo assay%'
;

update protocol
set protocol_type = 'Physical Assay'
where protocol_type like 'Physical assay%'
;

commit;

-- re-enable foreign key checks
set foreign_key_checks=@old_foreign_key_checks;

