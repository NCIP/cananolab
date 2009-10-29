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
