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
VALUES (1, 'Core'),
  (2, 'Shell'),
  (3, 'Coating'),
  (4, 'Monomer'),
  (5, 'Lipid'),
  (6, 'Modification'),
  (7, 'Oil'),
  (8, 'PFC'),
  (9, 'Drug'),
  (10, 'Image Contrast Agent');

commit;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;

