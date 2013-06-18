/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

use canano;

UPDATE csm_user
   SET login_name = '@superadmin.login.name@',
       first_name = '@superadmin.first.name@',
       last_name = '@superadmin.last.name@',
       update_date = sysdate()
 WHERE login_name = 'superadmin';