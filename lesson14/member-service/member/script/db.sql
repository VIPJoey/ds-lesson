create database mmc;

use mmc;

drop table if exists Tbl_MemberInfo;

create table Tbl_MemberInfo
(
  `uid` bigint(11) NOT NULL auto_increment,
  `uname` varchar(20) NOT NULL,
  `usex` tinyint(4) DEFAULT NULL,
  `ubirth` datetime NOT NULL,
  `utel` char(11) DEFAULT NULL,
  `uaddr` varchar(255) DEFAULT NULL,
  `uphoto` longblob,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `state` tinyint(4) DEFAULT NULL,
  `delFlag` tinyint(4) DEFAULT '0' COMMENT '删除状态：0未删除，1已删除',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
