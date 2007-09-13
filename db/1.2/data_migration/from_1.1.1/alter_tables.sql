-- Add new table foreign key constrain
 
ALTER TABLE PROTOCOL_FILE ADD (
  CONSTRAINT CON_PROT_PROT_FILE FOREIGN KEY (PROTOCOL_PK_ID) 
    REFERENCES PROTOCOL (PROTOCOL_PK_ID));


-- drop table columns and tables

-- alter table CHARACTERIZATION drop (INSTRUMENT_PK_ID);

-- drop table INSTRUMENT_TYPE_MANUFACTURER;

-- alter table INSTRUMENT  drop (MANUFACTURER_PK_ID,INSTRUMENT_TYPE_PK_ID);

-- drop table MANUFACTURER;

-- drop table INSTRUMENT_TYPE;

-- drop table INSTRUMENT;

-- alter table INSTRUMENT_TMP rename to INSTRUMENT;

alter table CHARACTERIZATION drop (CHAR_PROTOCOL_PK_ID);

alter table LAB_FILE drop (PROTOCOL_PK_ID);

alter table PROTOCOL drop (DESCRIPTION);
	 
alter table LINKAGE drop (AGENT_PK_ID);
	 
alter table DERIVED_BIOASSAY_DATA drop (CATEGORY); 
	 
drop table TABLE_DATA_CONDITION;

drop table TABLE_DATA;

drop table CHARACTERIZATION_PROTOCOL;

drop table CHARACTERIZATION_TABLE;

drop table KEYWORD_DERIVED_FILE;

drop table DERIVED_DATA_FILE;

drop table MEASURE_UNIT;

drop table SAMPLE_TYPE;

alter table STORAGE drop (STORAGE_TYPE_ID);

alter table ASSAY drop (ASSAY_TYPE_PK_ID);

drop table STORAGE_TYPE;

drop table ASSAY_TYPE;

drop view VIEW_PROTOCOL_FILE;


ALTER TABLE INSTRUMENT_CONFIG ADD (
  CONSTRAINT PK_INSTRUMENT_CONFIG PRIMARY KEY (INSTRUMENT_CONFIG_PK_ID));

ALTER TABLE INSTRUMENT_CONFIG ADD (
  CONSTRAINT CON_CONFIG_INSTRUMENT FOREIGN KEY (INSTRUMENT_PK_ID) 
    REFERENCES INSTRUMENT (INSTRUMENT_PK_ID));
    
alter view view_sample_sop_file compile;

--used for backward compatibility with caGrid
alter table characterization 
  add  (instrument_pk_id number,
	char_protocol_pk_id number);
