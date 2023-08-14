/*
Navicat MySQL Data Transfer

Source Server         : 192.168.200.129
Source Server Version : 50735
Source Host           : 192.168.200.129:3306
Source Database       : gmall_ware

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2021-10-19 16:59:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ware_info`
-- ----------------------------
DROP TABLE IF EXISTS `ware_info`;
CREATE TABLE `ware_info` (
  `id` bigint(20) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `areacode` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ware_info
-- ----------------------------
INSERT INTO `ware_info` VALUES ('1', '北京大兴仓库', '北京大兴', '110000', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_info` VALUES ('2', '北京昌平区仓库', '北京昌平', '110100', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');

-- ----------------------------
-- Table structure for `ware_order_task`
-- ----------------------------
DROP TABLE IF EXISTS `ware_order_task`;
CREATE TABLE `ware_order_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `delivery_address` varchar(1000) DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `payment_way` varchar(2) DEFAULT NULL COMMENT '付款方式 1:在线付款 2:货到付款',
  `task_status` varchar(20) DEFAULT NULL COMMENT '工作单状态',
  `order_body` varchar(200) DEFAULT NULL COMMENT '订单描述',
  `tracking_no` varchar(200) DEFAULT NULL COMMENT '物流单号',
  `ware_id` bigint(20) DEFAULT NULL COMMENT '仓库编号',
  `task_comment` varchar(500) DEFAULT NULL COMMENT '工作单备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='库存工作单表 库存工作单表';

-- ----------------------------
-- Records of ware_order_task
-- ----------------------------
INSERT INTO `ware_order_task` VALUES ('1', '1', 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'SPLIT', '红米3 小米77一代 小米MIX2S  ', null, null, null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task` VALUES ('2', '2', 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 小米MIX2S  ', null, '1', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task` VALUES ('3', '3', 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '红米3 ', null, '2', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task` VALUES ('4', '4', 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 ', null, null, null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task` VALUES ('5', '5', 'qigntiqny', '15099999999', '订个蛋糕梵蒂冈电饭锅地方', '', '2', 'DEDUCTED', '小米77一代 ', null, '1', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task` VALUES ('6', '22', 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', null, null, null, '2021-09-29 14:43:21', '2021-09-29 06:43:21', '0');
INSERT INTO `ware_order_task` VALUES ('7', '23', 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'SPLIT', '结婚买房买车买手机.', null, null, null, '2021-09-29 15:56:51', '2021-09-29 07:56:52', '0');
INSERT INTO `ware_order_task` VALUES ('8', '24', 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', null, '1', null, '2021-09-29 15:56:52', '2021-09-29 07:56:52', '0');
INSERT INTO `ware_order_task` VALUES ('9', '25', 'Administrator', '15099999999', '北京市昌平区宏福科技园', '', '2', 'DEDUCTED', '结婚买房买车买手机.', null, '2', null, '2021-09-29 15:56:52', '2021-09-29 07:56:52', '0');

-- ----------------------------
-- Table structure for `ware_order_task_detail`
-- ----------------------------
DROP TABLE IF EXISTS `ware_order_task_detail`;
CREATE TABLE `ware_order_task_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
  `sku_num` int(11) DEFAULT NULL COMMENT '购买个数',
  `task_id` bigint(20) DEFAULT NULL COMMENT '工作单编号',
  `refund_status` varchar(20) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='库存工作单明细表 库存工作单明细表';

-- ----------------------------
-- Records of ware_order_task_detail
-- ----------------------------
INSERT INTO `ware_order_task_detail` VALUES ('1', '42', '红米3', '1', '1', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('2', '27', '小米77一代', '1', '1', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('3', '25', '小米MIX2S ', '1', '1', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('4', '27', '小米77一代', '1', '2', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('5', '25', '小米MIX2S ', '1', '2', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('6', '42', '红米3', '1', '3', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('7', '27', '小米77一代', '1', '4', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('8', '27', '小米77一代', '1', '5', null, '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_order_task_detail` VALUES ('9', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', '1', '6', null, '2021-09-29 06:43:21', '2021-09-29 06:43:21', '0');
INSERT INTO `ware_order_task_detail` VALUES ('10', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', '2', '6', null, '2021-09-29 06:43:21', '2021-09-29 06:43:21', '0');
INSERT INTO `ware_order_task_detail` VALUES ('11', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', '1', '7', null, '2021-09-29 07:56:51', '2021-09-29 07:56:51', '0');
INSERT INTO `ware_order_task_detail` VALUES ('12', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', '2', '7', null, '2021-09-29 07:56:51', '2021-09-29 07:56:51', '0');
INSERT INTO `ware_order_task_detail` VALUES ('13', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', '1', '8', null, '2021-09-29 07:56:52', '2021-09-29 07:56:52', '0');
INSERT INTO `ware_order_task_detail` VALUES ('14', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', '2', '9', null, '2021-09-29 07:56:52', '2021-09-29 07:56:52', '0');

-- ----------------------------
-- Table structure for `ware_sku`
-- ----------------------------
DROP TABLE IF EXISTS `ware_sku`;
CREATE TABLE `ware_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库id',
  `stock` int(11) DEFAULT NULL COMMENT '库存数',
  `stock_name` varchar(200) DEFAULT NULL COMMENT '存货名称',
  `stock_locked` int(11) DEFAULT NULL COMMENT '锁定库存数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='sku与仓库关联表';

-- ----------------------------
-- Records of ware_sku
-- ----------------------------
INSERT INTO `ware_sku` VALUES ('1', '1', '1', '10000', '小米红米5', '60', '2021-08-13 15:18:42', '2021-08-13 15:40:41', '0');
INSERT INTO `ware_sku` VALUES ('2', '2', '2', '9980', '小米红米5另一库', '100', '2021-08-13 15:18:42', '2021-08-13 15:40:42', '0');
INSERT INTO `ware_sku` VALUES ('3', '3', '1', '96', '笔记本', '8', '2021-08-13 15:18:42', '2021-08-13 15:40:43', '0');
INSERT INTO `ware_sku` VALUES ('4', '4', '1', '1000', '笔记本拯救者', '16', '2021-08-13 15:18:42', '2021-08-13 15:40:43', '0');
INSERT INTO `ware_sku` VALUES ('5', '5', '1', '92', '苹果手机', '9', '2021-08-13 15:18:42', '2021-08-13 15:40:45', '0');
INSERT INTO `ware_sku` VALUES ('6', '6', '2', '100', '笔记本电脑', '16', '2021-08-13 15:18:42', '2021-08-13 15:40:46', '0');
INSERT INTO `ware_sku` VALUES ('7', '7', '1', '100', '小米', '42', '2021-08-13 15:18:42', '2021-08-13 15:40:47', '0');
INSERT INTO `ware_sku` VALUES ('8', '8', '1', '1000', '小米一代', '16', '2021-08-13 15:18:42', '2021-08-13 15:40:48', '0');
INSERT INTO `ware_sku` VALUES ('9', '9', '1', '1000', '小米二代', '12', '2021-08-13 15:18:42', '2021-08-13 15:40:49', '0');
INSERT INTO `ware_sku` VALUES ('10', '10', '2', '100', '小米88', '9', '2021-08-13 15:18:42', '2021-08-13 15:40:51', '0');
INSERT INTO `ware_sku` VALUES ('11', '11', '1', '100', '小米三代', '4', '2021-08-13 15:18:42', '2021-08-13 15:40:52', '0');
INSERT INTO `ware_sku` VALUES ('12', '12', '1', '100', 'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 4GB+64GB 黑色 游戏智能手机 小米 红米', '15', '2021-08-13 15:18:42', '2021-08-13 15:40:53', '0');
INSERT INTO `ware_sku` VALUES ('13', '13', '2', '100', 'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 3GB+32GB 金色 游戏智能手机 小米 红米	', '15', '2021-08-13 15:18:42', '2021-08-13 15:40:56', '0');
INSERT INTO `ware_sku` VALUES ('14', '14', '2', '500', 'Redmi Note8Pro 16400万全场景四摄 18W快充 红外遥控 8GB+128GB 金色 游戏智能手机 小米 红米', '30', '2021-08-13 15:18:42', '2021-08-13 15:40:57', '0');
INSERT INTO `ware_sku` VALUES ('15', '15', '2', '100', '小米CC 9 手机 PLUS', '9', '2021-08-13 15:18:42', '2021-08-13 15:40:59', '0');
INSERT INTO `ware_sku` VALUES ('16', '16', '2', '100', '小米CC 9 手机 一代', '24', '2021-08-13 15:18:42', '2021-08-13 15:41:01', '0');
INSERT INTO `ware_sku` VALUES ('17', '38', '1', '600', '小米CC 9 手机 二代', '48', '2021-08-13 15:18:42', '2021-08-13 15:18:42', '0');
INSERT INTO `ware_sku` VALUES ('18', '19', '1', '1000', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', '2', '2021-09-25 07:03:45', '2021-09-29 07:56:52', '0');
INSERT INTO `ware_sku` VALUES ('19', '20', '1', '10000', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', '4', '2021-09-25 07:03:45', '2021-09-30 02:30:40', '0');
