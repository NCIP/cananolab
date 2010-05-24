use canano;

ALTER TABLE canano.point_of_contact
 CHANGE created_date created_date DATETIME NOT NULL;
 
ALTER TABLE canano.organization
 CHANGE created_date created_date DATETIME NOT NULL;