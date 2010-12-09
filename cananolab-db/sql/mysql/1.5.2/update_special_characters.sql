/* update special characters '%', '#' to english words */

UPDATE common_lookup
   SET name = replace(name, '%', 'percent')
 WHERE name LIKE '%\%%';

UPDATE common_lookup
   SET value = replace(value, '%', 'percent')
 WHERE value LIKE '%\%%';

UPDATE common_lookup
   SET value = replace(name, '#', 'number')
 WHERE value LIKE '%#%';

UPDATE datum
   SET name = replace(name, '%', 'percent')
 WHERE name LIKE '%\%%';

UPDATE datum
   SET name = replace(name, '#', 'number')
 WHERE name LIKE '%#%';

UPDATE datum
   SET value_unit = replace(value_unit, '%', 'percent')
 WHERE value_unit LIKE '%\%%';

UPDATE experiment_condition
   SET value_unit = replace(value_unit, '%', 'percent')
 WHERE value_unit LIKE '%\%%';
