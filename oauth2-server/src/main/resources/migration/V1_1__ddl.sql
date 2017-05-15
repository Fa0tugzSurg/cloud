
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_role_authority`;
CREATE TABLE `oauth_role_authority` (
  `role` varchar(63) NOT NULL,
  `authority` varchar(63) NOT NULL,
  KEY `index_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `oauth_client_details` VALUES ('hanyi', null, '$2a$10$Z9mdaAKWRRp4Bd9XjmbaLOLA/ugtmQBZ54J2UiIB.47.4wIjLn39m', 'read', 'client_credentials', null, 'ROLE_TRUSTED_CLIENT', '28800', null, null, null), ('OAuth2_root', ' ', '$2a$10$LsxuUXj95OybofiyUhmf7.uUm/3c8/t7TuzrRXQuB7GMoUGuF3oKS', 'read', 'client_credentials', ' ', ' ', '28800', null, null, null), ('test_client', '', '$2a$10$bYCZpDgAuvPnEUf5TUjv1O1mRBfwHvPe2V77MZvopJEuMSFlKJ3vy', 'read', 'client_credentials', '', 'ROLE_TRUSTED_CLIENT', '28800', null, '{}', '');

INSERT INTO `oauth_role_authority` VALUES ('ROLE_TRUSTED_CLIENT', 'a'), ('ROLE_TRUSTED_CLIENT', 'c'), ('ROLE_TRUSTED_CLIENT', 'b'), ('role_b', 'a'), ('ROLE_TRUSTED_CLIENT', 'e');

insert into `common_db`.`t_sys_property_resource` ( `property_value`, `property_name`) values ( 'O_vXUOds4wkkoFevWD3jwj0PamwhawbEjVXups37XMwnEwdg6X5s1HxTpLxr2esku-n94KVbk8BAI4Tr-BiumA', 'insurance.cloud.auth.key');
