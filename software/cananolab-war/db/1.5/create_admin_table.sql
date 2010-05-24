USE canano;

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
values (
	0,
	0,
	CURDATE(),
	'admin',
	CURDATE());
	
commit;