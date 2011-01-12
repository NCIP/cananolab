USE canano;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','reproductive');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','continuous breeding');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','developmental');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','cancer bioassay');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','designType','parallel group');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','designType','crossover');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;
