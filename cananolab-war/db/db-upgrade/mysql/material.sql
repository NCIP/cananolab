USE canano;

--alter chemical_association
CREATE TABLE chemical_association
(
	chemical_association_pk_id BIGINT NOT NULL,
	material_a_pk_id BIGINT NOT NULL,
	material_b_pk_id BIGINT NOT NULL,
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	discriminator VARCHAR(200),
	attachment_bond_type VARCHAR(200),
	other_chemical_association_type VARCHAR(200),
	PRIMARY KEY (chemical_association_pk_id),
	KEY (material_a_pk_id),
	KEY (material_b_pk_id)
) TYPE=InnoDB
;

--alter biopolymer
CREATE TABLE biopolymer
(
	biopolymer_pk_id BIGINT NOT NULL,
	type VARCHAR(50) NOT NULL,
	sequence TEXT,
	PRIMARY KEY (biopolymer_pk_id),
	UNIQUE (biopolymer_pk_id),
	KEY (biopolymer_pk_id)
) TYPE=InnoDB
;

--alter composition_file
CREATE TABLE material_file
(
	material_pk_id BIGINT NOT NULL,
	file_pk_id BIGINT NOT NULL,
	PRIMARY KEY (material_pk_id, file_pk_id),
	KEY (file_pk_id),
	KEY (material_pk_id)
) TYPE=InnoDB
;

--alter sample_composition
CREATE TABLE material
(
	material_pk_id BIGINT NOT NULL,
	molecular_formula VARCHAR(200),
	molecular_formula_type VARCHAR(200),
	is_nano_sized TINYINT,
	description TEXT,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	name VARCHAR(200),
	value DECIMAL(22,10),
	value_unit VARCHAR(200),
	pub_chem_datasource_name VARCHAR(200),
	pub_chem_id BIGINT,
	activation_method_pk_id BIGINT,
	discriminator VARCHAR(200) NOT NULL,
	other_material_type VARCHAR(200),
	parent_material_pk_id BIGINT,
	child_material_type BIGINT,
	PRIMARY KEY (material_pk_id)
) TYPE=InnoDB
;

ALTER TABLE small_molecule ADD CONSTRAINT FK_small_molecule_material 
	FOREIGN KEY (small_molecule_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE sample_material ADD CONSTRAINT FK_sample_material_material 
	FOREIGN KEY (material_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE sample_material ADD CONSTRAINT FK_sample_material_sample 
	FOREIGN KEY (sample_pk_id) REFERENCES sample (sample_pk_id)
;

ALTER TABLE polymer ADD CONSTRAINT FK_polymer_material 
	FOREIGN KEY (polymer_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE nano_function ADD CONSTRAINT FK_function_material 
	FOREIGN KEY (material_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE liposome ADD CONSTRAINT FK_liposome_material 
	FOREIGN KEY (liposome_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE fullerene ADD CONSTRAINT FK_fullerene_material 
	FOREIGN KEY (fullerene_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE emulsion ADD CONSTRAINT FK_emulsion_material 
	FOREIGN KEY (emulsion_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE dendrimer ADD CONSTRAINT FK_dendrimer_material 
	FOREIGN KEY (dendrimer_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_material_a 
	FOREIGN KEY (material_a_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE chemical_association ADD CONSTRAINT FK_chemical_association_material_b 
	FOREIGN KEY (material_b_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE carbon_nanotube ADD CONSTRAINT FK_carbon_nanotube_material 
	FOREIGN KEY (carbon_nanotube_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE biopolymer ADD CONSTRAINT FK_biopolymer_material 
	FOREIGN KEY (biopolymer_pk_id) REFERENCES material (material_pk_id)
;

ALTER TABLE antibody ADD CONSTRAINT FK_antibody_material 
	FOREIGN KEY (antibody_pk_id) REFERENCES material (material_pk_id)
;
ALTER TABLE material_file ADD CONSTRAINT FK_material_file_material 
	FOREIGN KEY (material_pk_id) REFERENCES material (material_pk_id)
;

