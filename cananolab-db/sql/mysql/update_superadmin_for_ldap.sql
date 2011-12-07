use canano;

UPDATE csm_user
   SET login_name = '@superadmin.login.name@',
       first_name = '@superadmin.first.name@',
       last_name = '@superadmin.last.name@',
       update_date = sysdate()
 WHERE login_name = 'superadmin';