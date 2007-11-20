-- Disable foreign key checks
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

use cananolab;

drop table assay;
drop table def_assay_type;
drop table project;
drop table project_sample;
drop table run;
drop table run_input_file;
drop table run_output_file;
drop table run_sample_container;

DROP TABLE IF EXISTS def_composing_element_type;
CREATE TABLE def_composing_element_type (
   composing_element_type_pk_id decimal (20, 0) NOT NULL,
   name varchar (200) NOT NULL,
   PRIMARY KEY (composing_element_type_pk_id)
)
ENGINE=INNODB;

INSERT INTO def_composing_element_type(composing_element_type_pk_id, name)
VALUES (1, 'core'),
  (2, 'shell'),
  (3, 'coating'),
  (4, 'monomer'),
  (5, 'lipid'),
  (6, 'modification'),
  (7, 'oil'),
  (8, 'PFC'),
  (9, 'drug'),
  (10, 'image contrast agent');

commit;

delete from def_characterization_category
where category='Physical'
and name='Composition';

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

