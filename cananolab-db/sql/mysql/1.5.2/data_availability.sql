/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

CREATE TABLE canano.data_availability (
   sample_id BIGINT NOT NULL,
   datasource_name VARCHAR(20),
   available_entity_name VARCHAR(200),
   created_date datetime NOT NULL,
   created_by varchar(200) NOT NULL,
   updated_date datetime,
   updated_by varchar(200)
) Type=InnoDB;
