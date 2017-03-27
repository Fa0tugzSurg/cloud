CREATE TABLE `oauth_role_authority` (
  `role` varchar(63) NOT NULL,
  `authority` varchar(63) NOT NULL,
  KEY `index_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;