/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

use canano;

update common_lookup
set value='radioactivity quantitation'
where value='radioactivity quantiation';

update common_lookup
set name='radioactivity quantitation'
where name='radioactivity quantiation';

commit;