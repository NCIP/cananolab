/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

CREATE TABLE canano.data_review_status (
   data_id BIGINT(20) NOT NULL,
   data_type VARCHAR(200) NOT NULL,
   data_name VARCHAR(200) NOT NULL,
   status VARCHAR(200) NOT NULL,
   submitted_date DATETIME NOT NULL,
   submitted_by VARCHAR(200) NOT NULL,
   PRIMARY KEY (data_id)
) Type = InnoDB;
