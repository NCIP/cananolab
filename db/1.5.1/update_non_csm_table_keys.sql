DELIMITER |

drop procedure if exists update_table_keys;
CREATE PROCEDURE update_table_keys(IN increment INT)
BEGIN
  DECLARE table_name varchar(200);
	DECLARE key_name varchar(200);
	DECLARE done INT DEFAULT 0;

	DECLARE cur_keys cursor for
	select c.TABLE_NAME, c.COLUMN_NAME
	from information_schema.COLUMNS c
	where c.TABLE_SCHEMA='ncl'
	and c.column_key in ('PRI', 'MUL')
	and c.TABLE_NAME not like 'csm%';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  OPEN cur_keys;
  REPEAT
    FETCH cur_keys INTO table_name, key_name;
    IF NOT done THEN
			set @sql=CONCAT('update ', table_name,' set ', key_name,'=',key_name,'+',
			increment+';');
		
			select @sql;
  		PREPARE stmt from @sql; 
  		EXECUTE stmt;
END
IF;
  UNTIL done
END REPEAT;

  CLOSE cur_keys;
END;

|

DELIMITER ;
