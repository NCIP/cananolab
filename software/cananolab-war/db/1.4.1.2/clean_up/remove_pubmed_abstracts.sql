use canano;

update file a, publication b
set a.description=null
where a.file_pk_id=b.publication_pk_id
and a.description!='' and a.description!=null
and b.pubmed_id!=null;