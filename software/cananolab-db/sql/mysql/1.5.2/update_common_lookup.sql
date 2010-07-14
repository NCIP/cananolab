USE canano;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','agglomeration and/or aggregation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','crystal structure/crystallinity');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','purity');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','shape');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','surface area');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','surface charge');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','surface chemistry');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','particle size/size distribution');
INSERT INTO common_lookup (name,attribute,value) VALUES ('MINChar','entity','chemical composition');

INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','purity','purity');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','shape', 'shape');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','surface area ', 'surface area');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','surface charge', 'surface charge');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','zeta potential', 'surface charge');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','attachment','surface chemistry');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','size', 'particle size/size distribution');
INSERT INTO common_lookup (name,attribute,value) VALUES ('caNano2MINChar','sample composition','chemical composition');


ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;
