use canano;

update common_lookup
set value='radioactivity quantitation'
where value='radioactivity quantiation';

update common_lookup
set name='radioactivity quantitation'
where name='radioactivity quantiation';

commit;