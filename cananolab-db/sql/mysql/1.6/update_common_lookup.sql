USE canano;

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20) AUTO_INCREMENT NOT NULL;
 
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','reproductive');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','continuous breeding');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','developmental');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','type','cancer bioassay');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','designType','parallel group');
INSERT INTO common_lookup (name,attribute,value) VALUES ('study','designType','crossover');

INSERT INTO common_lookup (name,attribute,value) VALUES ('blood contact','assayName','coagulation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('blood contact','assayName','complement activation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('blood contact','assayName','hemolysis');
INSERT INTO common_lookup (name,attribute,value) VALUES ('blood contact','assayName','plasma protein binding');
INSERT INTO common_lookup (name,attribute,value) VALUES ('blood contact','assayName','platelet aggregation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','caspase 3 apoptosis');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','cell viability');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','gene expression');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','mitochondrial function');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','mitochondrial membrane potential');
INSERT INTO common_lookup (name,attribute,value) VALUES ('cytotoxicity','assayName','proliferation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','CFU-GM');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','chemotaxis');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','cytokine induction');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','cytotoxic activity of nk cells');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','leukocyte proliferation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','oxidative burst');
INSERT INTO common_lookup (name,attribute,value) VALUES ('immune cell function','assayName','phagocytosis');
INSERT INTO common_lookup (name,attribute,value) VALUES ('oxidative stress','assayName','gsh homeostasis');
INSERT INTO common_lookup (name,attribute,value) VALUES ('oxidative stress','assayName','lipid peroxidation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('oxidative stress','assayName','ros generation');
INSERT INTO common_lookup (name,attribute,value) VALUES ('sterility','assayName','bacterial/yeast/mold');
INSERT INTO common_lookup (name,attribute,value) VALUES ('sterility','assayName','endotoxin');
INSERT INTO common_lookup (name,attribute,value) VALUES ('sterility','assayName','mycoplasma');
INSERT INTO common_lookup (name,attribute,value) VALUES ('targeting','assayName','cell binding/internalization');
INSERT INTO common_lookup (name,attribute,value) VALUES ('targeting','assayName','gene expression');

ALTER TABLE canano.common_lookup
 CHANGE common_lookup_pk_id common_lookup_pk_id BIGINT(20)  NOT NULL;
