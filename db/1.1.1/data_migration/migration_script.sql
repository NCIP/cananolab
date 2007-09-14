-- create INSTRUMENT_CONFIG 

CREATE TABLE INSTRUMENT_CONFIG
(
  INSTRUMENT_CONFIG_PK_ID  NUMBER               NOT NULL,
  DESCRIPTION              VARCHAR2(4000),
  INSTRUMENT_PK_ID         NUMBER               NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE UNIQUE INDEX PK_INSTRUMENT_CONFIG ON INSTRUMENT_CONFIG
(INSTRUMENT_CONFIG_PK_ID)
LOGGING
NOPARALLEL;


CREATE TABLE INSTRUMENT_TMP
(
  INSTRUMENT_PK_ID  NUMBER                 NOT NULL,
  TYPE                   VARCHAR2(200),
  ABBREVIATION           VARCHAR2(50),
  MANUFACTURER           VARCHAR2(2000)
)
LOGGING 
NOCACHE
NOPARALLEL;


ALTER TABLE INSTRUMENT_TMP ADD (
  PRIMARY KEY (INSTRUMENT_PK_ID));
  
  
ALTER TABLE CHARACTERIZATION
  ADD Instrument_config_pk_id	NUMBER;


--update manufacturer_pk_id to be null in instrument where manufacturer name is null
update instrument
set manufacturer_pk_id=null
where manufacturer_pk_id in
(select b.manufacturer_pk_id
from manufacturer b
where b.name is null);

--delete manufacturer where name is null
delete from manufacturer
where name is null;

--update instrument_pk_id to be null in characterization where instrument information is empty
update characterization
set instrument_pk_id=null
where instrument_pk_id in
(select instrument_pk_id
from instrument a, instrument_type b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID=c.MANUFACTURER_PK_ID
and b.NAME is null and c.NAME is null);

--delete rows where instrument name and manufacturer name are null
delete from instrument
where instrument_pk_id
in (select instrument_pk_id
from instrument a, instrument_type b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID=c.MANUFACTURER_PK_ID
and b.NAME is null and c.NAME is null);

--update instrument set instrument_type_pk_id to null where instrument_type is empty
update instrument a
set instrument_type_pk_id=null
where exists
(select b.instrument_type_pk_id
from instrument_type b
where b.name is null
and a.instrument_type_pk_id=b.instrument_type_pk_id);

--delete from instrument where name is null
delete from instrument_type
where name is null;

--delete redundant rows in instrument and update characterization instrument
declare
  cursor c_redundants is
select count(distinct a.instrument_pk_id), a.instrument_type_pk_id, a.manufacturer_pk_id
from instrument a, instrument b
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.manufacturer_pk_id=b.manufacturer_pk_id
and a.instrument_pk_id!=b.instrument_pk_id
group by (a.instrument_type_pk_id, a.manufacturer_pk_id);

  v_instrument_pk_id number;
  
begin
  for i in c_redundants loop
    --select the first one in the redudnant list
    select instrument_pk_id
	into v_instrument_pk_id
	from instrument
	where instrument_type_pk_id=i.instrument_type_pk_id
	and manufacturer_pk_id=i.manufacturer_pk_id
	and rownum=1;
	
	--update to use the first one
	update characterization chara
	set instrument_pk_id=v_instrument_pk_id
	where exists
	(select instrument_pk_id
	from instrument instr
	where chara.instrument_pk_id=instr.instrument_pk_id
	and instr.instrument_type_pk_id=i.instrument_type_pk_id
	and instr.manufacturer_pk_id=i.manufacturer_pk_id);
	
	--delete the other redundnant ones
	delete from instrument
	where instrument_pk_id!=v_instrument_pk_id
	and instrument_type_pk_id=i.instrument_type_pk_id
	and manufacturer_pk_id=i.manufacturer_pk_id;
	
  end loop;
end;

/

--insert into instrument_tmp
insert into instrument_tmp
select rownum, tp, abbr, mf 
from (
select
a.name tp, a.abbreviation abbr, c.name mf
from instrument_type a, instrument_type_manufacturer b, manufacturer c
where a.instrument_type_pk_id=b.instrument_type_pk_id
and b.manufacturer_pk_id=c.manufacturer_pk_id 
union
select y.NAME tp, y.ABBREVIATION abbr, z.NAME mf
from instrument x, instrument_type y, manufacturer z
where x.INSTRUMENT_TYPE_PK_ID=y.INSTRUMENT_TYPE_PK_ID
and x.MANUFACTURER_PK_ID=z.MANUFACTURER_PK_ID);

--insert data in instrument into instrument_config 
insert into instrument_config
select a.instrument_pk_id, a.description, e.instrument_pk_id 
from instrument a, instrument_type b, manufacturer d, instrument_tmp e 
where a.instrument_type_pk_id=b.instrument_type_pk_id
and a.MANUFACTURER_PK_ID = d.MANUFACTURER_PK_ID
and e.type=b.name
and e.manufacturer=d.name
and e.abbreviation=b.abbreviation;


-- update characterization to associate with instrument_config
update characterization  chara
set instrument_config_pk_id=instrument_pk_id
where exists
(select instrument_config_pk_id 
from instrument_config config
where chara.instrument_pk_id = config.instrument_config_pk_id);


alter table characterization drop column instrument_pk_id;

drop table instrument_type_manufacturer;

alter table instrument drop (manufacturer_pk_id, instrument_type_pk_id);

drop table manufacturer;

drop table instrument_type;

drop table instrument;

alter table instrument_tmp rename to instrument;

commit;

exit;

