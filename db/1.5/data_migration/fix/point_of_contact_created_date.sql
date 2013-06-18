/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

use canano;

ALTER TABLE canano.point_of_contact
 CHANGE created_date created_date DATETIME NOT NULL;
 
ALTER TABLE canano.organization
 CHANGE created_date created_date DATETIME NOT NULL;