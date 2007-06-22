--delete redundant rows in characterization_protocol and update characterization 
declare
  cursor c_redundants is
select count(distinct a.CHAR_PROTOCOL_PK_ID), a.name, a.version
from characterization_protocol a, characterization_protocol b
where a.CHAR_PROTOCOL_PK_ID!=b.CHAR_PROTOCOL_PK_ID
and a.name=b.name
and a.version=b.version
group by (a.name, a.version);

  v_char_protocol_pk_id number;
  
begin
  for i in c_redundants loop
    --select the first one in the redudnant list
    select char_protocol_pk_id
	into v_char_protocol_pk_id
	from characterization_protocol
	where name=i.name
	and version=i.version
	and rownum=1;
	
	--update characterization
	update characterization chara
	set char_protocol_pk_id=v_char_protocol_pk_id
	where exists
	(select char_protocol_pk_id
	from characterization_protocol p
	where p.name=i.name
	and p.version=i.version
	and chara.char_protocol_pk_id=p.char_protocol_pk_id);
	
	--delete other redundant ones
	delete from characterization_protocol
	where char_protocol_pk_id!=v_char_protocol_pk_id
	and name=i.name
	and version=i.version;
  end loop;
end; 