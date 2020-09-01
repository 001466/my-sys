/*
Navicat MySQL Data Transfer

Source Server         : 192.168.9.122
Source Server Version : 50730
Source Host           : 192.168.9.122:3306
Source Database       : bladex_sword

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-09-01 10:52:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `name` varchar(128) DEFAULT NULL COMMENT '昵称',
  `nick_name` varchar(128) DEFAULT NULL COMMENT '真名',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(45) DEFAULT NULL COMMENT '手机',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` smallint(6) DEFAULT NULL COMMENT '性别',
  `avatar` varchar(512) DEFAULT NULL,
  `role_id` varchar(255) DEFAULT NULL COMMENT '角色id',
  `dept_id` varchar(255) DEFAULT NULL COMMENT '部门id',
  `enabled` int(2) DEFAULT '0' COMMENT '是否已删除',
  `locked` int(2) DEFAULT '0',
  `expired` int(2) DEFAULT '0',
  `type` int(2) NOT NULL COMMENT '0为用户；1为员工',
  `path` varchar(1024) DEFAULT NULL,
  `tenant_code` varchar(12) DEFAULT '000000' COMMENT '租户编号',
  `create_user` bigint(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(11) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
