-- change nanoparticle_entity to nanomaterial_entity

CREATE TABLE nanomaterial_entity
(
	nanomaterial_entity_pk_id BIGINT NOT NULL,
	composition_pk_id BIGINT NOT NULL,
	discriminator VARCHAR(200),
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	PRIMARY KEY (nanomaterial_entity_pk_id),
	KEY (composition_pk_id)
) TYPE=InnoDB
;

ALTER TABLE other_nanoparticle_entity RENAME other_nanomaterial_entity;
ALTER TABLE other_nanomaterial_entity
 DROP FOREIGN KEY FK_other_nanoparticle_entity_nanoparticle_entity,
 CHANGE other_nanoparticle_entity_pk_id other_nanomaterial_entity_pk_id BIGINT NOT NULL;
ALTER TABLE other_nanomaterial_entity  ADD CONSTRAINT
    FK_other_nanomaterial_entity_nanomaterial_entity FOREIGN KEY (other_nanomaterial_entity_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE associated_element
	ADD COLUMN pub_chem_datasource_name VARCHAR(200);

ALTER TABLE associated_element
	ADD COLUMN pub_chem_id BIGINT;

ALTER TABLE composing_element
 DROP FOREIGN KEY FK_composing_element_associated_element,
 DROP FOREIGN KEY FK_composing_element_nanoparticle_entity,
 CHANGE nanoparticle_entity_pk_id nanomaterial_entity_pk_id BIGINT;
ALTER TABLE composing_element
 ADD CONSTRAINT FK_composing_element_associated_element FOREIGN KEY (composing_element_pk_id) REFERENCES associated_element (associated_element_pk_id),
 ADD CONSTRAINT FK_composing_element_nanoparticle_entity FOREIGN KEY (nanomaterial_entity_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE polymer
 DROP FOREIGN KEY FK_polymer_nanoparticle_entity;
ALTER TABLE polymer ADD CONSTRAINT FK_polymer_nanomaterial_entity FOREIGN KEY (polymer_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE fullerene
 DROP FOREIGN KEY FK_fullerene_nanoparticle_entity;
ALTER TABLE fullerene ADD CONSTRAINT FK_fullerene_nanomaterial_entity FOREIGN KEY (fullerene_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE carbon_nanotube
 DROP FOREIGN KEY FK_carbon_nanotube_nanoparticle_entity;
ALTER TABLE carbon_nanotube ADD CONSTRAINT FK_carbon_nanotube_nanomaterial_entity FOREIGN KEY (carbon_nanotube_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE dendrimer
 DROP FOREIGN KEY FK_dendrimer_nanoparticle_entity;
ALTER TABLE dendrimer ADD CONSTRAINT FK_dendrimer_nanomaterial_entity FOREIGN KEY (dendrimer_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE liposome
 DROP FOREIGN KEY FK_liposome_nanoparticle_entity;
ALTER TABLE liposome ADD CONSTRAINT FK_liposome_nanomaterial_entity FOREIGN KEY (liposome_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE biopolymer_p
 DROP FOREIGN KEY FK_biopolymer_p_nanoparticle_entity;
ALTER TABLE biopolymer_p ADD CONSTRAINT FK_biopolymer_p_nanomaterial_entity FOREIGN KEY (biopolymer_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE emulsion
 DROP FOREIGN KEY FK_emulsion_nanoparticle_entity;
ALTER TABLE emulsion ADD CONSTRAINT FK_emulsion_nanomaterial_entity FOREIGN KEY (emulsion_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id);

ALTER TABLE nanoparticle_entity_lab_file RENAME nanomaterial_entity_file;
ALTER TABLE nanomaterial_entity_file
 DROP FOREIGN KEY FK_nanoparticle_entity_lab_file_nanoparticle_entity,
 DROP FOREIGN KEY FK_nanoparticle_entity_lab_file_lab_file,
 CHANGE nanoparticle_entity_pk_id nanomaterial_entity_pk_id BIGINT NOT NULL;
ALTER TABLE nanomaterial_entity_file
 ADD CONSTRAINT FK_nanomaterial_entity_file_nanomaterial_entity FOREIGN KEY (nanomaterial_entity_pk_id) REFERENCES nanomaterial_entity (nanomaterial_entity_pk_id),
 ADD CONSTRAINT FK_nanomaterial_entity_file_file FOREIGN KEY (file_pk_id) REFERENCES file (file_pk_id) ;

INSERT INTO  nanomaterial_entity (nanomaterial_entity_pk_id, composition_pk_id, discriminator,	description,	created_by,	created_date)
SELECT nanoparticle_entity_pk_id, composition_pk_id, discriminator,	description, created_by,	created_date FROM nanoparticle_entity;

UPDATE nanomaterial_entity
set discriminator='OtherNanomaterialEntity'
where discriminator='OtherNanoparticleEntity';

DROP TABLE nanoparticle_entity;

-- Reset data in [emulsion] for validation.
update emulsion
   set is_polymerized = null, is_polymerized = null
 where length(polymer_name) = 0
   and is_polymerized = 0;

-- Reset data in [liposome] for validation.
update liposome
   set is_polymerized = null, is_polymerized = null
 where length(polymer_name) = 0
   and is_polymerized = 0;