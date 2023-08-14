/*
Navicat MySQL Data Transfer

Source Server         : 192.168.200.129
Source Server Version : 50735
Source Host           : 192.168.200.129:3306
Source Database       : gmall_user

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2021-10-19 16:59:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `user_address`
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `province_id` bigint(20) DEFAULT NULL COMMENT '省份id',
  `user_address` varchar(500) DEFAULT NULL COMMENT '用户地址',
  `consignee` varchar(40) DEFAULT NULL COMMENT '收件人',
  `phone_num` varchar(40) DEFAULT NULL COMMENT '联系方式',
  `is_default` varchar(1) DEFAULT NULL COMMENT '是否是默认',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='用户地址表';

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES ('1', '1', '1', '北京市海淀区1', 'atguigu', '1', '0', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('2', '1', '1', '北京市昌平区2', 'admin', '2', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('4', '1', '2', '发打发第三方第三方', '晴天', '15099999999', '0', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('5', '2', '8', '北京市昌平区宏福科技园', 'Administrator', '15099999999', '1', '2021-08-13 15:18:42', '2021-09-25 02:49:32', '0');
INSERT INTO `user_address` VALUES ('6', '2', '3', '北京市昌平区TDB', '5号病毒', '15888888877', '0', '2021-08-13 15:18:42', '2021-09-25 02:49:32', '0');
INSERT INTO `user_address` VALUES ('8', '5', '2', '西城区北三路', '张三', '15099999999', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('10', '6', '1', '天通苑1', '晴天-11', '15666666666', '0', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('11', '6', '1', '天通苑1', '晴天-2', '15666666666', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_address` VALUES ('12', '7', '1', '天通苑1号', '晴天-00', '15677777777', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `login_name` varchar(200) DEFAULT NULL COMMENT '用户名称',
  `nick_name` varchar(200) DEFAULT NULL COMMENT '用户昵称',
  `passwd` varchar(200) DEFAULT NULL COMMENT '用户密码',
  `name` varchar(200) DEFAULT NULL COMMENT '用户姓名',
  `phone_num` varchar(200) DEFAULT NULL COMMENT '手机号',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `head_img` varchar(200) DEFAULT NULL COMMENT '头像',
  `user_level` varchar(200) DEFAULT NULL COMMENT '用户级别',
  `status` tinyint(3) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'atguigu', 'Atguigu', '96e79218965eb72c92a549dd5a330112', '尚硅谷', '11111', 'atguigu.com', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-17 16:01:47', '0');
INSERT INTO `user_info` VALUES ('2', '13700000000', 'Administrator', '96e79218965eb72c92a549dd5a330112', 'Admin', '2222', 'upd@qq.com', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '2', '1', '2021-08-13 15:18:42', '2021-08-17 16:01:46', '0');
INSERT INTO `user_info` VALUES ('3', 'admin', 'admin', '96e79218965eb72c92a549dd5a330112', 'haha', '123', 'atguigu.com', 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '3', '1', '2021-08-13 15:18:42', '2021-08-17 16:01:46', '0');
INSERT INTO `user_info` VALUES ('4', '15099999999', '15099999999', '96e79218965eb72c92a549dd5a330112', '15099999999', '15099999999', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_info` VALUES ('5', '15666666666', '晴天-奔', '96e79218965eb72c92a549dd5a330112', '晴天-奔', '15666666666', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_info` VALUES ('6', '15000000000', '11', '96e79218965eb72c92a549dd5a330112', '11', '15000000000', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_info` VALUES ('7', '18900000000', '晴天-0', '96e79218965eb72c92a549dd5a330112', '晴天-0', '18900000000', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_info` VALUES ('8', '15777777777', '111', 'bbb8aae57c104cda40c93843ad5e6db8', '111', '15777777777', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `user_info` VALUES ('21', '1509999999', '11', '698d51a19d8a121ce581499d7b701668', '11', '1509999999', null, 'http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132', '1', '1', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
