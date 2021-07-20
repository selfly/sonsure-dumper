/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

drop table if exists ss_user_message_item;
drop table if exists ss_message_text;
drop table if exists ss_user_message;

CREATE TABLE `ss_user_message` (
  `user_message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `owner_user_id` bigint(20) NOT NULL COMMENT '所有者用户id',
  `other_user_id` bigint(20) NOT NULL COMMENT '对方用户id',
  `is_receive_new` tinyint(4) NOT NULL COMMENT '是否有新消息',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `last_message_text_id` bigint(20) NOT NULL COMMENT '最后一条消息内容id',
  PRIMARY KEY (`user_message_id`)
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
  PRIMARY KEY (`user_message_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息记录';

