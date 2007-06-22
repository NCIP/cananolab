-- Add new table foreign key constrain
 
ALTER TABLE PROTOCOL_FILE ADD (
  CONSTRAINT CON_PROT_PROT_FILE FOREIGN KEY (PROTOCOL_PK_ID) 
    REFERENCES PROTOCOL (PROTOCOL_PK_ID));


-- drop table columns and tables

alter table characterization drop column instrument_pk_id;

drop table instrument_type_manufacturer;

alter table instrument drop (manufacturer_pk_id, instrument_type_pk_id);

drop table manufacturer;

drop table instrument_type;

drop table instrument;

alter table instrument_tmp rename to instrument;

ALTER TABLE CHARACTERIZATION
	 DROP (CHAR_PROTOCOL_PK_ID);

ALTER TABLE LAB_FILE
	 DROP (PROTOCOL_PK_ID);

ALTER TABLE PROTOCOL
	 DROP (DESCRIPTION);
	 
drop table table_data_condition;

drop table table_data;

drop table characterization_protocol;

drop table characterization_table;

drop table keyword_derived_file;

drop table derived_data_file;

drop view view_protocol_file;


ALTER TABLE INSTRUMENT_CONFIG ADD (
  CONSTRAINT PK_INSTRUMENT_CONFIG PRIMARY KEY (INSTRUMENT_CONFIG_PK_ID));

ALTER TABLE INSTRUMENT_CONFIG ADD (
  CONSTRAINT CON_CONFIG_INSTRUMENT FOREIGN KEY (INSTRUMENT_PK_ID) 
    REFERENCES INSTRUMENT (INSTRUMENT_PK_ID));