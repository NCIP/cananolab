DELIMITER | 
drop procedure if exists update_keys;
CREATE PROCEDURE update_keys(IN table_name varchar(200), IN key_name varchar(200))
BEGIN 
	set @increment=20000000;
	set @sql=CONCAT('update ', table_name,' set ', key_name,'=',key_name,'+',@increment+';');
	select @sql;
	PREPARE stmt from @sql; 
  EXECUTE stmt;
END; 
| 
DELIMITER ;


