drop table if exists ss_user_extend_prop;
drop table if exists ss_user_relationship;
drop table if exists ss_user_statement;
drop table if exists ss_user_message_item;
drop table if exists ss_message_text;
drop table if exists ss_behavior_verify;
drop table if exists ss_user_message;
drop table if exists ss_user;
CREATE TABLE `ss_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(60) NOT NULL COMMENT '密码',
  `user_type` varchar(32) NOT NULL COMMENT '用户类型',
  `status` varchar(2) NOT NULL COMMENT '状态',
  `sex` varchar(1) NOT NULL COMMENT '性別 1男 0女',
  `real_name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(128) DEFAULT NULL,
  `signature` varchar(256) DEFAULT NULL COMMENT '个性签名',
  `pin_password` varchar(60) DEFAULT NULL COMMENT '安全密码',
  `last_login_ip` varchar(32) DEFAULT NULL,
  `gmt_last_login` datetime DEFAULT NULL COMMENT '最后登录时间',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `creator` varchar(32) NOT NULL COMMENT '创建人',
  `token` varchar(60) DEFAULT NULL COMMENT '令牌',
  `gmt_modify` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_name_unique` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户';

INSERT INTO ss_user
(username, password, user_type, status, sex, real_name, email, mobile, avatar, signature, pin_password, last_login_ip, gmt_last_login, gmt_create, creator, token, gmt_modify)
VALUES('admin', '$2a$10$llu.PkWNrDS2twmpUvx5cuf7.221bfI5lXZbdvu/fI7IQ5GJBqTKa', '1', '1', '1', '超级管理员', 'admin@sonsure.com', NULL, 'assets/images/avatar.jpg', '欢迎使用Sonsure平台', NULL, NULL, NULL, now(), 'init', NULL, NULL);

CREATE TABLE `ss_user_extend_prop` (
  `user_extend_prop_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `prop_name` varchar(64) NOT NULL COMMENT '属性名称',
  `prop_val` varchar(64) NOT NULL COMMENT '属性值',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_extend_prop_id`),
  UNIQUE KEY `ss_user_extend_prop_UN` (`user_id`,`prop_name`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展属性';

CREATE TABLE `ss_user_relationship` (
  `user_relationship_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `master_username` varchar(32) NOT NULL COMMENT '主用户名',
  `slave_user_id` bigint(20) NOT NULL COMMENT '从用户id',
  `slave_username` varchar(32) NOT NULL COMMENT '从用户名',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `master_user_id` bigint(20) NOT NULL COMMENT '主用户id',
  PRIMARY KEY (`user_relationship_id`),
  KEY `idx_ime_user_relationship` (`master_user_id`),
  KEY `idx_ime_user_relationship_0` (`slave_user_id`),
  CONSTRAINT `fk_ime_user_relationship` FOREIGN KEY (`master_user_id`) REFERENCES `ss_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ime_user_relationship_0` FOREIGN KEY (`slave_user_id`) REFERENCES `ss_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户关系';

CREATE TABLE `ss_user_statement` (
  `user_statement_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `statement_type` varchar(32) NOT NULL COMMENT '流水类型',
  `biz_id` bigint(20) DEFAULT NULL COMMENT '业务对象id',
  `occur_val` varchar(64) NOT NULL COMMENT '当前发生值',
  `before_val` varchar(64) DEFAULT NULL COMMENT '发生前值',
  `after_val` varchar(64) NOT NULL COMMENT '发生后值',
  `description` varchar(512) DEFAULT NULL COMMENT '说明',
  `gmt_create` datetime NOT NULL COMMENT '发生时间',
  PRIMARY KEY (`user_statement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户流水';

CREATE TABLE `ss_user_message` (
  `user_message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `owner_user_id` bigint(20) NOT NULL COMMENT '所有者用户id',
  `other_user_id` bigint(20) NOT NULL COMMENT '对方用户id',
  `is_receive_new` tinyint(4) NOT NULL COMMENT '是否有新消息',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `last_message_text_id` bigint(20) NOT NULL COMMENT '最后一条消息内容id',
  PRIMARY KEY (`user_message_id`),
  KEY `idx_ime_user_message` (`owner_user_id`),
  KEY `idx_ime_user_message_0` (`other_user_id`),
  CONSTRAINT `fk_ime_user_message` FOREIGN KEY (`owner_user_id`) REFERENCES `ss_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ime_user_message_0` FOREIGN KEY (`other_user_id`) REFERENCES `ss_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息';

CREATE TABLE `ss_message_text` (
  `message_text_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `content` varchar(1024) NOT NULL COMMENT '消息内容',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`message_text_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='消息内容';

CREATE TABLE `ss_user_message_item` (
  `user_message_item_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_text_id` bigint(20) NOT NULL COMMENT '消息内容id',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `user_message_id` bigint(20) NOT NULL COMMENT '用户消息id',
  `author_user_id` bigint(20) NOT NULL COMMENT '消息作者id',
  PRIMARY KEY (`user_message_item_id`),
  KEY `idx_ime_user_message_record` (`user_message_id`),
  KEY `idx_ime_user_message_record_0` (`message_text_id`),
  CONSTRAINT `fk_ime_user_message_record` FOREIGN KEY (`user_message_id`) REFERENCES `ss_user_message` (`user_message_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_ime_user_message_record_0` FOREIGN KEY (`message_text_id`) REFERENCES `ss_message_text` (`message_text_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息记录';

CREATE TABLE `ss_behavior_verify` (
  `behavior_verify_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `verify_code` varchar(32) NOT NULL COMMENT '校验码',
  `verify_type` varchar(12) NOT NULL COMMENT '验证类型',
  `is_used` tinyint(4) NOT NULL COMMENT '是否已使用',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modify` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`behavior_verify_id`),
  KEY `idx_ime_user_verify` (`user_id`),
  CONSTRAINT `fk_ime_user_verify` FOREIGN KEY (`user_id`) REFERENCES `ss_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户行为校验码';