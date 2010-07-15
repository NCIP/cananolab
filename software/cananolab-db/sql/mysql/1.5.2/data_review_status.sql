CREATE TABLE canano.data_review_status (
   data_id BIGINT(20) NOT NULL,
   data_type VARCHAR(200) NOT NULL,
   data_name VARCHAR(200) NOT NULL,
   status VARCHAR(200) NOT NULL,
   submitted_date DATETIME NOT NULL,
   submitted_by VARCHAR(200) NOT NULL,
   PRIMARY KEY (data_id)
) Type = InnoDB;
