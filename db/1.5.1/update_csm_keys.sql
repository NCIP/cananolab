DELIMITER |

DROP PROCEDURE IF EXISTS update_csm_keys;

CREATE PROCEDURE update_csm_keys (
   IN table_name varchar (200), IN key_name varchar (200), IN increment INT
)

   BEGIN
      SET @sql =
             CONCAT('update ',
                    table_name,
                    ' set ',
                    key_name,
                    '=',
                    key_name,
                    '+',
                    increment,
                    ' where ',
                    key_name,
                    '=concat('''', 0+',
                    key_name,
                    ');')
                ;

      SELECT @sql;

      PREPARE stmt
      FROM @sql;
      EXECUTE stmt;
   END;


|

DELIMITER ;