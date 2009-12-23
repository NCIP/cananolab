USE canano;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;

insert into `common_lookup`(`name`,`attribute`,`value`) values ('Hemolysis','derivedDatumName','is hemolytic');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('PlateletAggregation','derivedDatumName','is above threshold');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('CFU_GM','derivedDatumName','number of CFU-GM colonies');
insert into `common_lookup`(`name`,`attribute`,`value`) values ('CFU_GM','derivedDatumName','total number of bone marrow cells');

ALTER TABLE canano.common_lookup
CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;
 