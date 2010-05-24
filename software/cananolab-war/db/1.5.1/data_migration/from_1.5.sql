use canano;

-- fix created_date column type
ALTER TABLE canano.point_of_contact
 CHANGE created_date created_date DATETIME NOT NULL;

ALTER TABLE canano.organization
 CHANGE created_date created_date DATETIME NOT NULL;


-- Expand datum.value from decimal(22,3) to decimal(30,10).
alter table canano.datum
change value value decimal(30,10) not null;

-- Expand associated_element.value from decimal(22,3) to decimal(30,10).
alter table canano.associated_element
change value value decimal(30,10);

-- Correct spelling error in common_lookup
update common_lookup
set value='radioactivity quantitation'
where value='radioactivity quantiation';

update common_lookup
set name='radioactivity quantitation'
where name='radioactivity quantiation';

-- create and initialize admin table
CREATE TABLE administration
(
	administration_id BIGINT NOT NULL,
	site_name VARCHAR(200) NULL,
	site_logo VARCHAR(200) NULL,
	visitor_count BIGINT NULL,
	counter_start_date DATETIME NOT NULL,
	created_by VARCHAR(200) NOT NULL,
	created_date DATETIME NOT NULL,
	updated_by VARCHAR(200) NULL,
	updated_date DATETIME NULL,
	PRIMARY KEY (administration_id)
) TYPE=InnoDB
;

insert into administration(
	administration_id,
	visitor_count,
	counter_start_date,
	created_by,
	created_date)
values (0,0,CURDATE(),'admin',CURDATE());

-- drop unused columns
ALTER TABLE canano.file
 DROP file_extension,
 DROP comments;

-- update other characterization
update
characterization
set other_char_assay_category='physico-chemical characterization'
where other_char_assay_category='physico chemical characterization';

update
characterization
set other_char_assay_category='in vitro characterization'
where other_char_assay_category='invitro characterization';

update
characterization
set other_char_assay_category='in vivo characterization'
where other_char_assay_category='invivo characterization';


