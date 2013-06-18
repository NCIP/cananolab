/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

use canano;

-- Expand datum.value from decimal(22,3) to decimal(30,10).
alter table canano.datum
change value value decimal(30,10) not null;

-- Expand associated_element.value from decimal(22,3) to decimal(30,10).
alter table canano.associated_element
change value value decimal(30,10);
