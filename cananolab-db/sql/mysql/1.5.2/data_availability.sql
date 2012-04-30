CREATE TABLE canano.data_availability (
   sample_id BIGINT NOT NULL,
   datasource_name VARCHAR(20),
   available_entity_name VARCHAR(200),
   created_date datetime NOT NULL,
   created_by varchar(200) NOT NULL,
   updated_date datetime,
   updated_by varchar(200)
) Type=InnoDB;
