/*L
   Copyright SAIC
   Copyright SAIC-Frederick

   Distributed under the OSI-approved BSD 3-Clause License.
   See http://ncip.github.com/cananolab/LICENSE.txt for details.
L*/

use canano;

update nano_function nf
set discriminator='TargetingFunction'
where exists
(SELECT t.targeting_function_pk_id
  FROM target t
 WHERE nf.function_pk_id = t.targeting_function_pk_id
and nf.discriminator!='TargetingFunction');