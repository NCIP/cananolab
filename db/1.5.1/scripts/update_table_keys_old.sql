DELIMITER |
drop procedure if exists update_keys;

CREATE PROCEDURE update_keys(IN increment INT)
BEGIN 
	DECLARE table_name varchar(200);
	DECLARE key_name varchar(200);
	
  DECLARE cur_keys cursor for
	select c.TABLE_NAME, c.COLUMN_NAME
	from information_schema.COLUMNS c
	where c.TABLE_SCHEMA='ncl'
	and c.column_key in ('PRI', 'MUL')
	and c.TABLE_NAME not like 'csm%';

	DECLARE v_notfound BOOL default FALSE;
	declare continue handler  for not found set v_notfound := TRUE;       
	declare exit handler  for sqlexception close cur_keys; 
	
	OPEN cur_keys;
  cursor_loop: LOOP
    FETCH  cur_keys
    INTO   table_name, key_name
		if v_not_found then           
		leave cursor_loop;            
		end if;

set @sql=CONCAT('update ', table_name,' set ', key_name,'=',key_name,'+',
increment+';');
		
select @sql;
end loop;        
	close cur_keys;  
	
END;|

DELIMITER ;

