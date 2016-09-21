CREATE TABLE `users` (
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `organization` varchar(500) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `phone_number` varchar(100) DEFAULT NULL,
  `email_id` varchar(100) DEFAULT NULL,
  `enabled` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `id_users_UNIQUE` (`username`)
);

CREATE TABLE `authorities` (
  `username` varchar(100) NOT NULL,
  `authority` varchar(100) NOT NULL,
  UNIQUE KEY `ix_auth_username` (`username`,`authority`),
  CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
);

CREATE TABLE `groups` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) NOT NULL,
  `group_description` varchar(200) DEFAULT NULL,
  `created_by` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `group_authorities` (
  `group_id` int(11) NOT NULL,
  `authority` varchar(50) NOT NULL,
  KEY `fk_group_members_group_idx` (`group_id`),
  CONSTRAINT `fk_group_authorities_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `group_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `group_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_group_members_group_idx` (`group_id`),
  KEY `fk_group_members_username_idx` (`username`),
  CONSTRAINT `fk_group_members_group` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_group_members_username` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `acl_class` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `class` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_acl_class` (`class`)
);

CREATE TABLE `acl_sid` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `principal` tinyint(1) NOT NULL,
  `sid` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_acl_sid` (`sid`,`principal`)
);

CREATE TABLE `acl_object_identity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `object_id_class` bigint(20) unsigned NOT NULL,
  `object_id_identity` bigint(20) NOT NULL,
  `parent_object` bigint(20) unsigned DEFAULT NULL,
  `owner_sid` bigint(20) unsigned DEFAULT NULL,
  `entries_inheriting` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_acl_object_identity` (`object_id_class`,`object_id_identity`),
  KEY `fk_acl_object_identity_parent` (`parent_object`),
  KEY `fk_acl_object_identity_owner` (`owner_sid`),
  CONSTRAINT `fk_acl_object_identity_class` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  CONSTRAINT `fk_acl_object_identity_owner` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `fk_acl_object_identity_parent` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`)
);

CREATE TABLE `acl_entry` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `acl_object_identity` bigint(20) unsigned NOT NULL,
  `ace_order` int(11) NOT NULL,
  `sid` bigint(20) unsigned NOT NULL,
  `mask` int(10) unsigned NOT NULL,
  `granting` tinyint(1) NOT NULL,
  `audit_success` tinyint(1) NOT NULL,
  `audit_failure` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_acl_entry` (`acl_object_identity`,`ace_order`),
  KEY `fk_acl_entry_acl` (`sid`),
  CONSTRAINT `fk_acl_entry_acl` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `fk_acl_entry_object` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`)
);

INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.SampleBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.CompositionBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.NanomaterialEntityBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.ComposingElementBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.FunctionalizingEntityBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.FunctionBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.particle.composition.ChemicalAssociationBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.particle.Characterization');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.common.ExperimentConfig');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.common.Finding');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.common.ProtocolBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.common.PublicationBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.common.Author');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.common.FileBean');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.common.Organization');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.domain.common.PointOfContact');
INSERT INTO `acl_class` (`class`) VALUES ('gov.nih.nci.cananolab.dto.common.CollaborationGroupBean');
  