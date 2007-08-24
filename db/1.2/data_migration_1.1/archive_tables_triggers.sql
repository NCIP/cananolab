CREATE TABLE HISTORY_CHARACTERIZATION
(
  CHARACTERIZATION_PK_ID   NUMBER,
  CLASSIFICATION           VARCHAR2(200),
  SOURCE                   VARCHAR2(200),
  DESCRIPTION              VARCHAR2(2000),
  IDENTIFIER_NAME          VARCHAR2(500),
  NAME                     VARCHAR2(100),
  DISCRIMINATOR            VARCHAR2(50),
  CREATED_DATE             DATE,
  CREATED_BY               VARCHAR2(200),
  PROTOCOL_FILE_PK_ID      NUMBER,
  INSTRUMENT_CONFIG_PK_ID  NUMBER,
  DELETED_DATE             DATE,
  CHIRALITY                VARCHAR2(100),
  GROWTH_DIAMETER          NUMBER,
  AVERAGE_LENGTH           NUMBER,
  WALL_TYPE                VARCHAR2(100),
  GENERATION               NUMBER,
  MOLECULAR_FORMULA        VARCHAR2(200),
  REPEAT_UNIT              VARCHAR2(100),
  BRANCH                   VARCHAR2(200),
  EMULSION_TYPE            VARCHAR2(200),
  POLYMER_NAME             VARCHAR2(200),
  IS_POLYMERIZED           NUMBER,
  TABLE_SOURCE             VARCHAR2(500),
  CELL_LINE                VARCHAR2(200),
  CELL_DEATH_METHOD        VARCHAR2(200),
  NUMBER_OF_CARBON         VARCHAR2(200),
  TYPE                     VARCHAR2(100),
  IS_CROSS_LINK            NUMBER,
  CROSS_LINK_DEGREE        NUMBER,
  INITIATOR                VARCHAR2(200),
  MAX_DIMENSION            NUMBER,
  MIN_DIMENSION            NUMBER,
  MAX_DIMENSION_UNIT	   VARCHAR2(100),
  MIN_DIMENSION_UNIT	   VARCHAR2(100),
  SOLVENT                  VARCHAR2(200),
  CRITICAL_CONCENTRATION   NUMBER,
  CONCENTRATION_UNIT       VARCHAR2(100),
  IS_SOLUBLE               NUMBER,
  SURFACE_AREA             NUMBER,
  SURFACE_AREA_UNIT        VARCHAR2(100),
  ZETA_POTENTIAL           NUMBER,
  ZETA_POTENTIAL_UNIT      VARCHAR2(100),
  CHARGE                   NUMBER,
  CHARGE_UNIT              VARCHAR2(100),
  IS_HYDROPHOBIC           NUMBER
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_COMPOSING_ELEMENT
(
  COMPOSING_ELEMENT_PK_ID  NUMBER               NOT NULL,
  ELEMENT_TYPE             VARCHAR2(100),
  CHEMICAL_NAME            VARCHAR2(200),
  DESCRIPTION              VARCHAR2(2000),
  CHARACTERIZATION_PK_ID   NUMBER,
  LIST_INDEX               NUMBER,
  DELETED_DATE             DATE
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_DATUM
(
  DATUM_PK_ID                  NUMBER           NOT NULL,
  NAME                         VARCHAR2(2000)   NOT NULL,
  VALUE                        NUMBER           NOT NULL,
  VALUE_UNIT                   VARCHAR2(200),
  DERIVED_BIOASSAY_DATA_PK_ID  NUMBER,
  CONTROL_NAME                 VARCHAR2(200),
  CONTROL_TYPE                 VARCHAR2(100),
  LIST_INDEX                   NUMBER,
  STATISTICS_TYPE              VARCHAR2(200),
  BIOASSAY_DATA_CATEGORY       VARCHAR2(2000),
  DELETED_DATE                 DATE
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_DERIVED_BIOASSAY_DATA
(
  DERIVED_BIOASSAY_DATA_PK_ID  NUMBER           NOT NULL,
  CHARACTERIZATION_PK_ID       NUMBER,
  LIST_INDEX                   NUMBER,
  DELETED_DATE                 DATE
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE HISTORY_LAB_FILE
(
  FILE_PK_ID           NUMBER                   NOT NULL,
  FILE_NAME            VARCHAR2(500),
  FILE_URI             VARCHAR2(500),
  FILE_TYPE_EXTENSION  VARCHAR2(100),
  FILE_SOURCE_TYPE     VARCHAR2(100),
  VERSION              VARCHAR2(200),
  STATUS               VARCHAR2(20),
  REASON               VARCHAR2(2000),
  CREATED_BY           VARCHAR2(200),
  CREATED_DATE         DATE,
  SAMPLE_SOP_PK_ID     NUMBER,
  RUN_PK_ID            NUMBER,
  DATA_STATUS_PK_ID    NUMBER,
  TITLE                VARCHAR2(500),
  DESCRIPTION          VARCHAR2(2000),
  COMMENTS             VARCHAR2(2000),
  TYPE                 VARCHAR2(200),
  DELETED_DATE         DATE
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_NANOPARTICLE_CHAR
(
  CHARACTERIZATION_PK_ID  NUMBER                NOT NULL,
  NANOPARTICLE_PK_ID      NUMBER                NOT NULL,
  DELETED_DATE            DATE
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_SURFACE_CHEMISTRY
(
  SURFACE_CHEMISTRY_PK_ID  NUMBER,
  MOLECULE_NAME            VARCHAR2(200),
  SURFACE_PK_ID            NUMBER,
  NUMBER_MOLECULE          NUMBER,
  LIST_INDEX               NUMBER,
  MOLECULAR_FORMULA_TYPE   VARCHAR2(200),
  DELETED_DATE             DATE
)
LOGGING 
NOCACHE
NOPARALLEL;

CREATE TABLE HISTORY_SURFACE_GROUP
(
  SURFACE_GROUP_PK_ID  NUMBER,
  NAME                 VARCHAR2(100),
  MODIFIER             VARCHAR2(100),
  D_COMPOSITION_PK_ID  NUMBER,
  LIST_INDEX           NUMBER,
  DELETED_DATE         DATE
)
LOGGING 
NOCACHE
NOPARALLEL;


--Create triggers

CREATE OR REPLACE TRIGGER SET_HISTORY_NANOPARTICLE_CHAR
BEFORE DELETE
    ON NANOPARTICLE_CHAR
    FOR EACH ROW
BEGIN
   -- Insert record into audit table
    INSERT INTO HISTORY_NANOPARTICLE_CHAR
     ( CHARACTERIZATION_PK_ID, NANOPARTICLE_PK_ID, DELETED_DATE )
    VALUES
     ( :old.CHARACTERIZATION_PK_ID, :old.NANOPARTICLE_PK_ID, sysdate);

END;
/

CREATE OR REPLACE TRIGGER SET_history_Derived_Biodata
BEFORE update
    ON derived_bioassay_data
    FOR EACH ROW
BEGIN
   IF :new.CHARACTERIZATION_PK_ID is null THEN
       insert into history_derived_bioassay_data 
        ( DERIVED_BIOASSAY_DATA_PK_ID, 
          CHARACTERIZATION_PK_ID, 
          LIST_INDEX, 
          DELETED_DATE )
       values 
        (:old.derived_bioassay_data_pk_id, 
         :old.characterization_pk_id, 
         :old.list_index,
         sysdate);
   ELSE 
       delete from history_derived_bioassay_data where DERIVED_BIOASSAY_DATA_PK_ID=:old.DERIVED_BIOASSAY_DATA_PK_ID;
   END IF;
END;
/


CREATE OR REPLACE TRIGGER SET_HISTORY_datum
BEFORE update
    ON datum
    FOR EACH ROW
BEGIN
   IF :new.derived_bioassay_data_pk_id is null THEN
       insert into history_datum 
        ( datum_pk_id, 
          name, 
          value, 
          value_unit, 
          derived_bioassay_data_pk_id, 
          control_name, 
          control_type, 
          list_index, 
          statistics_type, 
          bioassay_data_category, 
          deleted_date )
       values
        (:old.datum_pk_id, 
         :old.name, 
         :old.value, 
         :old.value_unit, 
         :old.derived_bioassay_data_pk_id, 
         :old.control_name, 
         :old.control_type, 
         :old.list_index, 
         :old.statistics_type, 
         :old.bioassay_data_category, 
         sysdate );
   ELSE 
       delete from history_datum where datum_pk_id=:old.datum_pk_id;
   END IF;
END;
/


CREATE OR REPLACE TRIGGER SET_HISTORY_CHARACTERIZATION
BEFORE DELETE
    ON CHARACTERIZATION
    FOR EACH ROW
BEGIN
   -- Insert record into audit table
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       CLASSIFICATION,
       SOURCE,DESCRIPTION,
       IDENTIFIER_NAME,
       NAME,
       DISCRIMINATOR,
       CREATED_DATE,
       CREATED_BY,
       PROTOCOL_FILE_PK_ID,
       INSTRUMENT_CONFIG_PK_ID, 
       DELETED_DATE,
       TABLE_SOURCE)
    VALUES
     ( :old.CHARACTERIZATION_PK_ID, 
       :old.CLASSIFICATION, 
       :old.SOURCE, 
       :old.DESCRIPTION, 
       :old.IDENTIFIER_NAME, 
       :old.NAME, 
       :old.DISCRIMINATOR, 
       :old.CREATED_DATE, 
       :old.CREATED_BY, 
       :old.PROTOCOL_FILE_PK_ID, 
       :old.INSTRUMENT_CONFIG_PK_ID, 
       sysdate,
       'CHARACTERIZATION');

END;
/


CREATE OR REPLACE TRIGGER SET_HIST_COMPOSING_ELEMENT
BEFORE update
    ON  composing_element
    FOR EACH ROW
BEGIN
   IF :new.characterization_pk_id is null then
       insert into history_composing_element
        ( composing_element_pk_id, 
          element_type, 
          chemical_name, 
          description, 
          characterization_pk_id, 
          list_index, 
          deleted_date )
       values
        (:old.composing_element_pk_id, 
         :old.element_type, 
         :old.chemical_name, 
         :old.description, 
         :old.characterization_pk_id, 
         :old.list_index, 
         sysdate );
   ELSE 
      delete from history_composing_element where composing_element_pk_id=:old.composing_element_pk_id;
   END IF;
END;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_SURFACE_CHEMISTRY
BEFORE update
    ON  surface_chemistry
    FOR EACH ROW
BEGIN
   IF :new.surface_pk_id is null THEN
       insert into history_surface_chemistry
        ( surface_chemistry_pk_id, 
          molecule_name, 
          surface_pk_id, 
          number_molecule, 
          list_index, 
		  molecular_formula_type,
          deleted_date )
       values
        (:old.surface_chemistry_pk_id, 
         :old.molecule_name, 
         :old.surface_pk_id, 
         :old.number_molecule, 
         :old.list_index, 
		 :old.molecular_formula_type,
         sysdate );
   ELSE
      delete from history_surface_chemistry where surface_chemistry_pk_id=:old.surface_chemistry_pk_id;
   END IF;      
END;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_SURFACE_GROUP
BEFORE update
    ON  surface_group
    FOR EACH ROW
BEGIN
   IF :new.d_composition_pk_id is null THEN
       insert into history_surface_group
        ( surface_group_pk_id, 
          name, 
          modifier,
          d_composition_pk_id, 
          list_index, 
          deleted_date )
       values
        (:old.surface_group_pk_id, 
         :old.name, 
         :old.modifier, 
         :old.d_composition_pk_id, 
         :old.list_index, 
         sysdate );
   ELSE 
      delete from history_surface_group where surface_group_pk_id=:old.surface_group_pk_id;
   END IF;
END;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_LAB_FILE
BEFORE DELETE
    ON LAB_FILE
    FOR EACH ROW
BEGIN
   -- Insert record into audit table
    INSERT INTO HISTORY_LAB_FILE
     ( FILE_PK_ID,
       FILE_NAME,
       FILE_URI,
       FILE_TYPE_EXTENSION,
       FILE_SOURCE_TYPE,
       VERSION,
       STATUS,
       REASON,
       CREATED_BY,
       CREATED_DATE,
       SAMPLE_SOP_PK_ID,
       RUN_PK_ID,
       DATA_STATUS_PK_ID,
       TITLE,
       DESCRIPTION,
       COMMENTS,
       TYPE,
       DELETED_DATE )   
    VALUES
     ( :old.FILE_PK_ID,
       :old.FILE_NAME,
       :old.FILE_URI,
       :old.FILE_TYPE_EXTENSION,
       :old.FILE_SOURCE_TYPE,
       :old.VERSION,
       :old.STATUS,
       :old.REASON,
       :old.CREATED_BY,
       :old.CREATED_DATE,
       :old.SAMPLE_SOP_PK_ID,
       :old.RUN_PK_ID,
       :old.DATA_STATUS_PK_ID,
       :old.TITLE,
       :old.DESCRIPTION,
       :old.COMMENTS,
       :old.TYPE,
       sysdate);
END;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_NANOTUBE
before delete
    on carbon_nanotube_composition
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID, 
       CHIRALITY, 
       GROWTH_DIAMETER, 
       AVERAGE_LENGTH, 
       WALL_TYPE, 
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.cn_composition_pk_id, 
       :old.chirality, 
       :old.growth_diameter, 
       :old.average_length, 
       :old.wall_type, 
       sysdate, 
       'CARBON_NANOTUBE_COMPOSITION');
      
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_DENDRIMER
before delete
    on dendrimer_composition
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       GENERATION, 
       MOLECULAR_FORMULA, 
       REPEAT_UNIT, 
       BRANCH,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.d_composition_pk_id,
       :old.generation,
       :old.molecular_formula,
       :old.repeat_unit,
       :old.branch,
       sysdate,
       'DENDRIMER_COMPOSITION');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_EMULSION
before delete
    on emulsion_composition
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       EMULSION_TYPE, 
       MOLECULAR_FORMULA, 
       POLYMER_NAME, 
       IS_POLYMERIZED,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.e_composition_pk_id,
       :old.emulsion_type,
       :old.molecular_formula,
       :old.polymer_name,
       :old.is_polymerized,
       sysdate,
       'EMULSION_COMPOSITION');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_CYTOTOXICITY
before delete
    on cytotoxicity
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       CELL_LINE, 
       CELL_DEATH_METHOD, 
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.cytotoxicity_pk_id,
       :old.cell_line,
       :old.cell_death_method,
       sysdate,
       'CYTOTOXICITY');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_FULLERENE
before delete
    on fullerene_composition
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       NUMBER_OF_CARBON,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.f_composition_pk_id,
       :old.number_of_carbon,
       sysdate,
       'FULLERENE_COMPOSITION');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_MORPHOLOGY
before delete
    on morphology
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       TYPE,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.morphology_pk_id,
       :old.type,
       sysdate,
       'MORPHOLOGY');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_POLYMER
before delete
    on polymer_composition
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       IS_CROSS_LINK,
       CROSS_LINK_DEGREE,
       INITIATOR,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.p_composition_pk_id,
       :old.is_cross_link,
       :old.cross_link_degree,
       :old.initiator,
       sysdate,
       'POLYMER_COMPOSITION');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_SHAPE
before delete
    on shape
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       MAX_DIMENSION,
       MIN_DIMENSION,
       MAX_DIMENSION_UNIT,
       MIN_DIMENSION_UNIT,
       TYPE,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.shape_pk_id,
       :old.max_dimension,
       :old.min_dimension,
       :old.max_dimension_unit,
       :old.min_dimension_unit,
       :old.type,
       sysdate,
       'SHAPE');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_SOLUBILITY
before delete
    on solubility
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       SOLVENT,
       CRITICAL_CONCENTRATION,
       CONCENTRATION_UNIT,
       IS_SOLUBLE,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.solubility_pk_id,
       :old.solvent,
       :old.critical_concentration,
       :old.concentration_unit,
       :old.is_soluble,
       sysdate,
       'SOLUBILITY');
end;
/

CREATE OR REPLACE TRIGGER SET_HISTORY_SURFACE
before delete
    on surface
    for each row
begin     
    INSERT INTO HISTORY_CHARACTERIZATION
     ( CHARACTERIZATION_PK_ID,
       SURFACE_AREA,
       SURFACE_AREA_UNIT,
       ZETA_POTENTIAL,
	   ZETA_POTENTIAL_UNIT,
       CHARGE,
       CHARGE_UNIT,
       IS_HYDROPHOBIC,
       DELETED_DATE, 
       TABLE_SOURCE)
    VALUES
     ( :old.surface_pk_id,
       :old.surface_area,
       :old.surface_area_unit,
       :old.zeta_potential,
	   :old.zeta_potential_unit,
       :old.charge,
       :old.charge_unit,
       :old.is_hydrophobic,
       sysdate,
       'SURFACE');
end;
/