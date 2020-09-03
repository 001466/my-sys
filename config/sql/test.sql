/*
Navicat MySQL Data Transfer

Source Server         : 192.168.9.122
Source Server Version : 50730
Source Host           : 192.168.9.122:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-09-03 17:52:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for client
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `client_id` varchar(48) NOT NULL COMMENT '客户端id',
  `client_secret` varchar(256) NOT NULL COMMENT '客户端密钥',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '资源集合',
  `scope` varchar(256) NOT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) NOT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) DEFAULT NULL COMMENT '权限',
  `access_token_validity` bigint(11) NOT NULL COMMENT '令牌过期秒数',
  `refresh_token_validity` bigint(11) NOT NULL COMMENT '刷新令牌过期秒数',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '附加说明',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '自动授权',
  `create_user` bigint(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(11) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` int(2) NOT NULL COMMENT '状态',
  `is_deleted` int(2) NOT NULL COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of client
-- ----------------------------
INSERT INTO `client` VALUES ('21', 'gateway_fa', '{noop}gateway_fa', '', 'approve', 'refresh_token,password,authorization_code', 'http://192.168.9.54:8888/login/oauth2/code/okta', '', '1296000000', '1296000000', '', '', '1', '2019-11-11 10:11:46', '1', '2020-09-03 17:45:01', '1', '0');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT '0' COMMENT '父主键',
  `code` varchar(255) DEFAULT NULL COMMENT '字典码',
  `dict_key` varchar(255) DEFAULT NULL COMMENT '字典值',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '字典名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '字典备注',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of dict
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` int(11) DEFAULT '0' COMMENT '父级菜单',
  `code` varchar(255) DEFAULT NULL COMMENT '菜单编号',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `alias` varchar(255) DEFAULT NULL COMMENT '菜单别名',
  `path` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `source` varchar(255) DEFAULT NULL COMMENT '菜单资源',
  `sort` int(2) DEFAULT NULL COMMENT '排序',
  `category` int(2) DEFAULT NULL COMMENT '菜单类型',
  `action` int(2) DEFAULT '0' COMMENT '操作按钮类型',
  `is_open` int(2) DEFAULT '1' COMMENT '是否打开新页面',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_deleted` int(2) DEFAULT '0' COMMENT '是否已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of menu
-- ----------------------------

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

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '{blade}90b9aa7e25f80cf4f64e990b78a9fc5ebd6cecad', '管理员', '管理员', 'admin@bladex.vip', '22233322', '2018-08-08 00:00:00', '1', '/atom-file/resource/仓井空/adc24597-9a93-4a11-82b5-64699a03a73b.png', '1', '1', '1', '0', '0', '0', '', '000000', '1', '2018-08-08 00:00:00', '-1', '2020-07-08 09:54:05');
