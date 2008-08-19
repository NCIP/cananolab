-- increase the molecular_formula column from VARCHAR(200) to VARCHAR(500)

ALTER TABLE canano.associated_element
 CHANGE molecular_formula molecular_formula VARCHAR(500);
 
ALTER TABLE canano.surface_chemistry
 CHANGE molecular_formula molecular_formula VARCHAR(500);