/*
Navicat MySQL Data Transfer

Source Server         : 192.168.200.129
Source Server Version : 50735
Source Host           : 192.168.200.129:3306
Source Database       : gmall_order

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2021-10-19 16:58:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cart_info`
-- ----------------------------
DROP TABLE IF EXISTS `cart_info`;
CREATE TABLE `cart_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` varchar(200) DEFAULT NULL COMMENT '用户id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `cart_price` decimal(10,2) DEFAULT NULL COMMENT '放入购物车时价格',
  `sku_num` int(11) DEFAULT NULL COMMENT '数量',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片文件',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称 (冗余)',
  `is_checked` int(1) DEFAULT NULL,
  `is_ordered` bigint(20) DEFAULT NULL COMMENT '是否已经下单',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `source_type` varchar(20) DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint(20) DEFAULT NULL COMMENT '来源编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='购物车表 用户登录系统时更新冗余';

-- ----------------------------
-- Records of cart_info
-- ----------------------------
INSERT INTO `cart_info` VALUES ('3', '2', '1', '5999.00', '1', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAIpgZAAIvrX6L9fo612.jpg', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机', '1', null, null, null, null, '2021-08-13 15:24:39', '2021-08-13 15:24:39', '0');
INSERT INTO `cart_info` VALUES ('4', '2', '2', '6999.00', '1', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机', '1', null, null, null, null, '2021-08-13 15:24:39', '2021-08-13 15:24:39', '0');

-- ----------------------------
-- Table structure for `order_detail`
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'sku_id',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称（冗余)',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片名称（冗余)',
  `order_price` decimal(10,2) DEFAULT NULL COMMENT '购买价格(下单时sku价格）',
  `sku_num` varchar(200) DEFAULT NULL COMMENT '购买个数',
  `source_type` varchar(20) DEFAULT NULL COMMENT '来源类型',
  `source_id` bigint(20) DEFAULT NULL COMMENT '来源编号',
  `split_total_amount` decimal(10,2) DEFAULT NULL,
  `split_activity_amount` decimal(10,2) DEFAULT NULL,
  `split_coupon_amount` decimal(10,2) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8 COMMENT='订单明细表';

-- ----------------------------
-- Records of order_detail
-- ----------------------------
INSERT INTO `order_detail` VALUES ('1', '1', '4', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', '999.00', '1', '2401', '4', '999.00', '0.00', '0.00', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_detail` VALUES ('2', '1', '5', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', '999.00', '4', '2401', '5', '3874.00', '122.00', '0.00', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_detail` VALUES ('3', '2', '2', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg', '6999.00', '1', '2401', '2', '6999.00', '0.00', '0.00', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_detail` VALUES ('4', '3', '5', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rgJqAHPnoAAF9hoDNfsc505.jpg', '999.00', '1', '2401', '5', '999.00', '0.00', '0.00', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_detail` VALUES ('5', '4', '2', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAcbl2AAFopp2WGBQ404.jpg', '6999.00', '1', null, null, '6999.00', '0.00', '0.00', '2021-08-13 15:41:16', '2021-08-13 15:41:16', '0');
INSERT INTO `order_detail` VALUES ('6', '4', '1', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机', 'http://47.93.148.192:8080/group1/M00/00/01/rBHu8l-rfvmAIpgZAAIvrX6L9fo612.jpg', '5999.00', '1', null, null, '5999.00', '0.00', '0.00', '2021-08-13 15:41:16', '2021-08-13 15:41:16', '0');
INSERT INTO `order_detail` VALUES ('7', '5', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 03:51:06', '2021-09-25 03:51:06', '0');
INSERT INTO `order_detail` VALUES ('8', '5', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 03:51:06', '2021-09-25 03:51:06', '0');
INSERT INTO `order_detail` VALUES ('9', '6', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 03:51:55', '2021-09-25 03:51:55', '0');
INSERT INTO `order_detail` VALUES ('10', '6', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 03:51:55', '2021-09-25 03:51:55', '0');
INSERT INTO `order_detail` VALUES ('11', '7', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 03:54:26', '2021-09-25 03:54:26', '0');
INSERT INTO `order_detail` VALUES ('12', '7', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 03:54:26', '2021-09-25 03:54:26', '0');
INSERT INTO `order_detail` VALUES ('13', '8', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 04:10:24', '2021-09-25 04:10:24', '0');
INSERT INTO `order_detail` VALUES ('14', '8', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 04:10:24', '2021-09-25 04:10:24', '0');
INSERT INTO `order_detail` VALUES ('15', '9', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 04:11:41', '2021-09-25 04:11:41', '0');
INSERT INTO `order_detail` VALUES ('16', '9', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 04:11:41', '2021-09-25 04:11:41', '0');
INSERT INTO `order_detail` VALUES ('17', '10', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '2', null, null, null, null, null, '2021-09-25 06:25:14', '2021-09-25 06:25:14', '0');
INSERT INTO `order_detail` VALUES ('18', '10', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '7', null, null, null, null, null, '2021-09-25 06:25:14', '2021-09-25 06:25:14', '0');
INSERT INTO `order_detail` VALUES ('19', '11', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-25 06:43:24', '2021-09-25 06:43:24', '0');
INSERT INTO `order_detail` VALUES ('20', '11', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '1', null, null, null, null, null, '2021-09-25 06:43:24', '2021-09-25 06:43:24', '0');
INSERT INTO `order_detail` VALUES ('21', '12', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-25 06:43:33', '2021-09-25 06:43:33', '0');
INSERT INTO `order_detail` VALUES ('22', '12', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '1', null, null, null, null, null, '2021-09-25 06:43:33', '2021-09-25 06:43:33', '0');
INSERT INTO `order_detail` VALUES ('23', '13', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-25 07:09:54', '2021-09-25 07:09:54', '0');
INSERT INTO `order_detail` VALUES ('24', '13', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3999.00', '1', null, null, null, null, null, '2021-09-25 07:09:54', '2021-09-25 07:09:54', '0');
INSERT INTO `order_detail` VALUES ('25', '14', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-25 08:04:36', '2021-09-25 08:04:36', '0');
INSERT INTO `order_detail` VALUES ('26', '14', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3838.00', '1', null, null, null, null, null, '2021-09-25 08:04:36', '2021-09-25 08:04:36', '0');
INSERT INTO `order_detail` VALUES ('27', '15', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-25 08:07:37', '2021-09-25 08:07:37', '0');
INSERT INTO `order_detail` VALUES ('28', '15', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3888.00', '1', null, null, null, null, null, '2021-09-25 08:07:37', '2021-09-25 08:07:37', '0');
INSERT INTO `order_detail` VALUES ('29', '16', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-28 03:08:06', '2021-09-28 03:08:06', '0');
INSERT INTO `order_detail` VALUES ('30', '16', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-28 03:08:06', '2021-09-28 03:08:06', '0');
INSERT INTO `order_detail` VALUES ('31', '17', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-28 08:45:38', '2021-09-28 08:45:38', '0');
INSERT INTO `order_detail` VALUES ('32', '17', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-28 08:45:38', '2021-09-28 08:45:38', '0');
INSERT INTO `order_detail` VALUES ('33', '18', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-28 08:57:13', '2021-09-28 08:57:13', '0');
INSERT INTO `order_detail` VALUES ('34', '18', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-28 08:57:13', '2021-09-28 08:57:13', '0');
INSERT INTO `order_detail` VALUES ('35', '19', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 03:15:23', '2021-09-29 03:15:23', '0');
INSERT INTO `order_detail` VALUES ('36', '19', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 03:15:23', '2021-09-29 03:15:23', '0');
INSERT INTO `order_detail` VALUES ('37', '20', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 03:21:41', '2021-09-29 03:21:41', '0');
INSERT INTO `order_detail` VALUES ('38', '20', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 03:21:41', '2021-09-29 03:21:41', '0');
INSERT INTO `order_detail` VALUES ('39', '21', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 03:57:53', '2021-09-29 03:57:53', '0');
INSERT INTO `order_detail` VALUES ('40', '21', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 03:57:53', '2021-09-29 03:57:53', '0');
INSERT INTO `order_detail` VALUES ('41', '22', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 06:42:54', '2021-09-29 06:42:54', '0');
INSERT INTO `order_detail` VALUES ('42', '22', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 06:42:54', '2021-09-29 06:42:54', '0');
INSERT INTO `order_detail` VALUES ('43', '23', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 07:56:29', '2021-09-29 07:56:29', '0');
INSERT INTO `order_detail` VALUES ('44', '23', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 07:56:29', '2021-09-29 07:56:29', '0');
INSERT INTO `order_detail` VALUES ('45', '24', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 07:56:29', '2021-09-29 07:56:29', '0');
INSERT INTO `order_detail` VALUES ('46', '25', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 07:56:29', '2021-09-29 07:56:29', '0');
INSERT INTO `order_detail` VALUES ('47', '26', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-29 08:21:15', '2021-09-29 08:21:15', '0');
INSERT INTO `order_detail` VALUES ('48', '26', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-29 08:21:15', '2021-09-29 08:21:15', '0');
INSERT INTO `order_detail` VALUES ('49', '27', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-30 02:34:18', '2021-09-30 02:34:18', '0');
INSERT INTO `order_detail` VALUES ('50', '27', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-30 02:34:18', '2021-09-30 02:34:18', '0');
INSERT INTO `order_detail` VALUES ('51', '28', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-30 02:35:28', '2021-09-30 02:35:28', '0');
INSERT INTO `order_detail` VALUES ('52', '28', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-30 02:35:28', '2021-09-30 02:35:28', '0');
INSERT INTO `order_detail` VALUES ('53', '29', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-30 03:08:27', '2021-09-30 03:08:27', '0');
INSERT INTO `order_detail` VALUES ('54', '29', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-30 03:08:27', '2021-09-30 03:08:27', '0');
INSERT INTO `order_detail` VALUES ('55', '30', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-30 03:10:12', '2021-09-30 03:10:12', '0');
INSERT INTO `order_detail` VALUES ('56', '30', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-30 03:10:12', '2021-09-30 03:10:12', '0');
INSERT INTO `order_detail` VALUES ('57', '31', '20', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+256G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '4499.00', '2', null, null, null, null, null, '2021-09-30 03:16:56', '2021-09-30 03:16:56', '0');
INSERT INTO `order_detail` VALUES ('58', '31', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3333.00', '1', null, null, null, null, null, '2021-09-30 03:16:56', '2021-09-30 03:16:56', '0');
INSERT INTO `order_detail` VALUES ('59', '32', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3.00', '1', null, null, null, null, null, '2021-10-09 02:53:49', '2021-10-09 02:53:49', '0');
INSERT INTO `order_detail` VALUES ('60', '33', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3.00', '1', null, null, null, null, null, '2021-10-09 03:01:06', '2021-10-09 03:01:06', '0');
INSERT INTO `order_detail` VALUES ('61', '34', '19', '荣耀（HONOR） 荣耀V30 V30Pro 5G手机 麒麟990芯片 V30pro 幻夜星河 全网通(8+128G)', 'http://192.168.200.129:9000/gmall/163126155589457a8f47e-3486-402a-8747-60c511357daf', '3.00', '1', null, null, null, null, null, '2021-10-09 03:08:41', '2021-10-09 03:08:41', '0');

-- ----------------------------
-- Table structure for `order_detail_activity`
-- ----------------------------
DROP TABLE IF EXISTS `order_detail_activity`;
CREATE TABLE `order_detail_activity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '订单明细id',
  `activity_id` bigint(20) DEFAULT NULL COMMENT '活动ID',
  `activity_rule` bigint(20) DEFAULT NULL COMMENT '活动规则',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='订单明细购物券表';

-- ----------------------------
-- Records of order_detail_activity
-- ----------------------------
INSERT INTO `order_detail_activity` VALUES ('1', '1', '2', '8', '68', '5', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');

-- ----------------------------
-- Table structure for `order_detail_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `order_detail_coupon`;
CREATE TABLE `order_detail_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_detail_id` bigint(20) DEFAULT NULL COMMENT '订单明细id',
  `coupon_id` bigint(20) DEFAULT NULL COMMENT '购物券ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细购物券表';

-- ----------------------------
-- Records of order_detail_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for `order_info`
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收货人',
  `consignee_tel` varchar(20) DEFAULT NULL COMMENT '收件人电话',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `payment_way` varchar(20) DEFAULT NULL COMMENT '付款方式',
  `delivery_address` varchar(1000) DEFAULT NULL COMMENT '送货地址',
  `order_comment` varchar(200) DEFAULT NULL COMMENT '订单备注',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '订单交易编号（第三方支付用)',
  `trade_body` varchar(200) DEFAULT NULL COMMENT '订单描述(第三方支付用)',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `process_status` varchar(20) DEFAULT NULL COMMENT '进度状态',
  `tracking_no` varchar(1000) DEFAULT NULL COMMENT '物流单编号',
  `parent_order_id` bigint(20) DEFAULT NULL COMMENT '父订单编号',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片路径',
  `province_id` int(20) DEFAULT NULL COMMENT '地区',
  `activity_reduce_amount` decimal(16,2) DEFAULT NULL COMMENT '促销金额',
  `coupon_amount` decimal(10,2) DEFAULT NULL COMMENT '优惠券',
  `original_total_amount` decimal(16,2) DEFAULT NULL COMMENT '原价金额',
  `feight_fee` decimal(16,2) DEFAULT NULL COMMENT '运费',
  `feight_fee_reduce` decimal(10,2) DEFAULT NULL COMMENT '减免运费',
  `refundable_time` datetime DEFAULT NULL COMMENT '可退款日期（签收后30天）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COMMENT='订单表 订单表';

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('1', '张噶', '15034569876', '4873.00', 'PAID', '2', '1101', '昌平区立水桥小区1号楼308室', '', 'ATGUIGU1628561669341764', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米 Redmi 10X 4', '2021-08-10 10:14:29', '2021-08-11 10:14:29', '0901', null, null, null, '1', '122.00', '0.00', '4995.00', '10.00', '10.00', null, '2021-08-13 15:18:40', '2021-08-25 18:34:44', '0');
INSERT INTO `order_info` VALUES ('2', '张噶', '15034569876', '6999.00', 'PAID', '2', '1101', '昌平区立水桥小区1号楼308室', '', 'ATGUIGU1628668001994948', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机 ', '2021-08-11 15:46:42', '2021-08-12 15:46:42', '0901', null, null, null, '1', '0.00', '0.00', '6999.00', '10.00', '10.00', null, '2021-08-13 15:18:40', '2021-08-25 18:34:46', '0');
INSERT INTO `order_info` VALUES ('3', '张噶', '15034569876', '999.00', 'PAID', '2', '1101', '昌平区立水桥小区1号楼308室', '', 'ATGUIGU1628817290407364', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米 ', '2021-08-13 09:14:50', '2021-08-14 09:14:50', 'COMMNET', null, null, null, '1', '0.00', '0.00', '999.00', '10.00', '10.00', null, '2021-08-13 15:18:40', '2021-08-25 18:34:47', '0');
INSERT INTO `order_info` VALUES ('4', 'qigntiqny', '15099999999', '12998.00', 'PAID', '2', 'ONLINE', '订个蛋糕梵蒂冈电饭锅地方', '', 'ATGUIGU1628840474760981', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 8GB+128GB 陶瓷黑 游戏手机', '2021-08-13 15:41:15', '2021-08-14 15:41:15', 'COMMNET', null, null, null, null, '0.00', '0.00', '12998.00', '0.00', null, null, '2021-08-13 15:41:15', '2021-08-25 18:34:48', '0');
INSERT INTO `order_info` VALUES ('5', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632541865864846', '结婚买房买车买手机.', '2021-09-25 11:51:06', '2021-09-26 11:51:06', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 03:51:06', '2021-09-25 03:52:29', '0');
INSERT INTO `order_info` VALUES ('6', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632541915170268', '结婚买房买车买手机.', '2021-09-25 11:51:55', '2021-09-26 11:51:55', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 03:51:55', '2021-09-25 03:51:55', '0');
INSERT INTO `order_info` VALUES ('7', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU163254206676279', '结婚买房买车买手机.', '2021-09-25 11:54:27', '2021-09-26 11:54:27', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 03:54:26', '2021-09-25 03:54:26', '0');
INSERT INTO `order_info` VALUES ('8', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632543024517987', '结婚买房买车买手机.', '2021-09-25 12:10:25', '2021-09-26 12:10:25', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 04:10:24', '2021-09-25 04:10:24', '0');
INSERT INTO `order_info` VALUES ('9', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632543101407806', '结婚买房买车买手机.', '2021-09-25 12:11:41', '2021-09-26 12:11:41', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 04:11:41', '2021-09-25 04:11:41', '0');
INSERT INTO `order_info` VALUES ('10', 'Administrator', '15099999999', '39491.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632551113863209', '结婚买房买车买手机.', '2021-09-25 14:25:14', '2021-09-26 14:25:14', 'UNPAID', null, null, null, null, null, '0.00', '39491.00', null, null, null, '2021-09-25 06:25:14', '2021-09-25 06:25:14', '0');
INSERT INTO `order_info` VALUES ('11', 'Administrator', '15099999999', '12997.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632552204054905', '结婚买房买车买手机.', '2021-09-25 14:43:24', '2021-09-26 14:43:24', 'UNPAID', null, null, null, null, null, '0.00', '12997.00', null, null, null, '2021-09-25 06:43:24', '2021-09-25 06:43:24', '0');
INSERT INTO `order_info` VALUES ('12', 'Administrator', '15099999999', '12997.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632552213471167', '结婚买房买车买手机.', '2021-09-25 14:43:33', '2021-09-26 14:43:33', 'UNPAID', null, null, null, null, null, '0.00', '12997.00', null, null, null, '2021-09-25 06:43:33', '2021-09-25 06:43:33', '0');
INSERT INTO `order_info` VALUES ('13', 'Administrator', '15099999999', '12997.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632553794263308', '结婚买房买车买手机.', '2021-09-25 15:09:54', '2021-09-26 15:09:54', 'UNPAID', null, null, null, null, null, '0.00', '12997.00', null, null, null, '2021-09-25 07:09:54', '2021-09-25 07:09:54', '0');
INSERT INTO `order_info` VALUES ('14', 'Administrator', '15099999999', '12836.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632557076289546', '结婚买房买车买手机.', '2021-09-25 16:04:36', '2021-09-26 16:04:36', 'UNPAID', null, null, null, null, null, '0.00', '12836.00', null, null, null, '2021-09-25 08:04:36', '2021-09-25 08:04:36', '0');
INSERT INTO `order_info` VALUES ('15', 'Administrator', '15099999999', '12886.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632557257411908', '结婚买房买车买手机.', '2021-09-25 16:07:37', '2021-09-26 16:07:37', 'UNPAID', null, null, null, null, null, '0.00', '12886.00', null, null, null, '2021-09-25 08:07:37', '2021-09-25 08:07:37', '0');
INSERT INTO `order_info` VALUES ('16', 'Administrator', '15099999999', '12331.00', 'UNPAID', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632798486136792', '结婚买房买车买手机.', '2021-09-28 11:08:06', '2021-09-29 11:08:06', 'UNPAID', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-28 03:08:06', '2021-09-28 08:22:24', '0');
INSERT INTO `order_info` VALUES ('17', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632818737862798', '结婚买房买车买手机.', '2021-09-28 16:45:38', '2021-09-29 16:45:38', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-28 08:45:38', '2021-09-29 16:45:38', '0');
INSERT INTO `order_info` VALUES ('18', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632819433352855', '结婚买房买车买手机.', '2021-09-28 16:57:13', '2021-09-29 16:57:13', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-28 08:57:13', '2021-09-30 08:56:33', '0');
INSERT INTO `order_info` VALUES ('19', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632885323098257', '结婚买房买车买手机.', '2021-09-29 11:15:23', '2021-09-30 11:15:23', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 03:15:23', '2021-09-30 11:15:56', '0');
INSERT INTO `order_info` VALUES ('20', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632885701645779', '结婚买房买车买手机.', '2021-09-29 11:21:42', '2021-09-30 11:21:42', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 03:21:41', '2021-09-30 11:24:16', '0');
INSERT INTO `order_info` VALUES ('21', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632887873219914', '结婚买房买车买手机.', '2021-09-29 11:57:53', '2021-09-30 11:57:53', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 03:57:53', '2021-09-30 11:57:53', '0');
INSERT INTO `order_info` VALUES ('22', 'Administrator', '15099999999', '12331.00', 'WAITING_DELEVER', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632897773754138', '结婚买房买车买手机.', '2021-09-29 14:42:54', '2021-09-30 14:42:54', 'WAITING_DELEVER', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 06:42:54', '2021-09-29 14:43:22', '0');
INSERT INTO `order_info` VALUES ('23', 'Administrator', '15099999999', '12331.00', 'SPLIT', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632902189026241', '结婚买房买车买手机.', '2021-09-29 15:56:29', '2021-09-30 15:56:29', 'SPLIT', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 07:56:29', '2021-09-29 15:56:52', '0');
INSERT INTO `order_info` VALUES ('24', 'Administrator', '15099999999', '3333.00', 'FINISHED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632902212193176', '结婚买房买车买手机.', '2021-09-29 15:56:52', '2021-09-30 15:56:52', 'COMMNET', null, '23', null, null, null, '0.00', '3333.00', null, null, null, '2021-09-29 07:56:29', '2021-09-29 15:56:52', '0');
INSERT INTO `order_info` VALUES ('25', 'Administrator', '15099999999', '8998.00', 'FINISHED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632902212207733', '结婚买房买车买手机.', '2021-09-29 15:56:52', '2021-09-30 15:56:52', 'COMMNET', null, '23', null, null, null, '0.00', '8998.00', null, null, null, '2021-09-29 07:56:29', '2021-09-29 15:56:52', '0');
INSERT INTO `order_info` VALUES ('26', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632903675426976', '结婚买房买车买手机.', '2021-09-29 16:21:15', '2021-09-30 16:21:15', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-29 08:21:15', '2021-10-08 17:55:29', '0');
INSERT INTO `order_info` VALUES ('27', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632969258280390', '结婚买房买车买手机.', '2021-09-30 10:34:18', '2021-10-01 10:34:18', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-30 02:34:18', '2021-10-08 17:55:29', '0');
INSERT INTO `order_info` VALUES ('28', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632969328469477', '结婚买房买车买手机.', '2021-09-30 10:35:28', '2021-10-01 10:35:28', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-30 02:35:28', '2021-10-08 17:55:29', '0');
INSERT INTO `order_info` VALUES ('29', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632971307585127', '结婚买房买车买手机.', '2021-09-30 11:08:28', '2021-10-01 11:08:28', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-30 03:08:27', '2021-09-30 11:09:03', '0');
INSERT INTO `order_info` VALUES ('30', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632971412713182', '结婚买房买车买手机.', '2021-09-30 11:10:13', '2021-10-01 11:10:13', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-30 03:10:12', '2021-09-30 11:11:06', '0');
INSERT INTO `order_info` VALUES ('31', 'Administrator', '15099999999', '12331.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1632971816072241', '结婚买房买车买手机.', '2021-09-30 11:16:56', '2021-10-01 11:16:56', 'CLOSED', null, null, null, null, null, '0.00', '12331.00', null, null, null, '2021-09-30 03:16:56', '2021-09-30 11:17:09', '0');
INSERT INTO `order_info` VALUES ('32', 'Administrator', '15099999999', '3.00', 'CLOSED', '2', 'ONLINE', '北京市昌平区宏福科技园', '', 'ATGUIGU1633748029097741', '结婚买房买车买手机.', '2021-10-09 10:53:49', '2021-10-10 10:53:49', 'CLOSED', null, null, null, null, null, '0.00', '3.00', null, null, null, '2021-10-09 02:53:49', '2021-10-09 10:53:54', '0');
INSERT INTO `order_info` VALUES ('33', 'admin', '2', '3.00', 'CLOSED', '1', 'ONLINE', '北京市昌平区2', '', 'ATGUIGU1633748466776867', '结婚买房买车买手机.', '2021-10-09 11:01:07', '2021-10-10 11:01:07', 'CLOSED', null, null, null, null, null, '0.00', '3.00', null, null, null, '2021-10-09 03:01:06', '2021-10-09 11:01:10', '0');
INSERT INTO `order_info` VALUES ('34', 'admin', '2', '3.00', 'UNPAID', '1', 'ONLINE', '北京市昌平区2', '', 'ATGUIGU1633748921609819', '结婚买房买车买手机.', '2021-10-09 11:08:42', '2021-10-10 11:08:42', 'UNPAID', null, null, null, null, null, '0.00', '3.00', null, null, null, '2021-10-09 03:08:41', '2021-10-09 03:10:50', '0');

-- ----------------------------
-- Table structure for `order_refund_info`
-- ----------------------------
DROP TABLE IF EXISTS `order_refund_info`;
CREATE TABLE `order_refund_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  `refund_type` varchar(10) DEFAULT NULL COMMENT '退款类型（1：退款 2：退货）',
  `refund_num` bigint(20) DEFAULT NULL COMMENT '退货件数',
  `refund_amount` decimal(16,2) DEFAULT NULL COMMENT '退款金额',
  `refund_reason_type` varchar(10) DEFAULT NULL COMMENT '原因类型',
  `refund_reason_txt` varchar(20) DEFAULT NULL COMMENT '原因内容',
  `refund_status` varchar(20) DEFAULT NULL COMMENT '退款状态（0：待审批 1：已退款）',
  `approve_operator_id` bigint(20) DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_remark` varchar(255) DEFAULT NULL COMMENT '审批备注',
  `tracking_no` varchar(50) DEFAULT NULL COMMENT '退货物流单号',
  `tracking_time` datetime DEFAULT NULL COMMENT '退货单号录入时间',
  `recieve_operator_id` bigint(20) DEFAULT NULL COMMENT '签收人',
  `recieve_time` datetime DEFAULT NULL COMMENT '签收时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退单表';

-- ----------------------------
-- Records of order_refund_info
-- ----------------------------

-- ----------------------------
-- Table structure for `order_status_log`
-- ----------------------------
DROP TABLE IF EXISTS `order_status_log`;
CREATE TABLE `order_status_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(11) DEFAULT NULL,
  `order_status` varchar(11) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_status_log
-- ----------------------------
INSERT INTO `order_status_log` VALUES ('1', '1', '1001', '2021-08-10 10:14:29', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_status_log` VALUES ('2', '2', '1001', '2021-08-11 15:46:42', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_status_log` VALUES ('3', '3', '1001', '2021-08-13 09:14:50', '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `order_status_log` VALUES ('4', '4', 'UNPAID', '2021-08-13 15:41:16', '2021-08-13 15:41:16', '2021-08-13 15:41:16', '0');

-- ----------------------------
-- Table structure for `payment_info`
-- ----------------------------
DROP TABLE IF EXISTS `payment_info`;
CREATE TABLE `payment_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_trade_no` varchar(50) DEFAULT NULL COMMENT '对外业务编号',
  `order_id` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `payment_type` varchar(20) DEFAULT NULL COMMENT '支付类型（微信 支付宝）',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '交易编号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `subject` varchar(200) DEFAULT NULL COMMENT '交易内容',
  `payment_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `callback_content` text COMMENT '回调信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='支付信息表';

-- ----------------------------
-- Records of payment_info
-- ----------------------------
INSERT INTO `payment_info` VALUES ('1', 'ATGUIGU1628561669341764', '1', '4', '1101', null, '4873.00', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 冰雾白 游戏智能手机 小米 红米 Redmi 10X 4', '0801', null, null, '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `payment_info` VALUES ('2', 'ATGUIGU1628668001994948', '2', '4', '1101', null, '6999.00', '小米10 至尊纪念版 双模5G 骁龙865 120HZ高刷新率 120倍长焦镜头 120W快充 12GB+256GB 陶瓷黑 游戏手机 ', '0801', null, null, '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `payment_info` VALUES ('3', 'ATGUIGU1628817290407364', '3', '4', '1101', null, '999.00', 'Redmi 10X 4G Helio G85游戏芯 4800万超清四摄 5020mAh大电量 小孔全面屏 128GB大存储 4GB+128GB 明月灰 游戏智能手机 小米 红米 ', '0801', null, null, '2021-08-13 15:18:40', '2021-08-13 15:18:40', '0');
INSERT INTO `payment_info` VALUES ('4', 'ATGUIGU1632798486136792', '16', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'UNPAID', null, null, '2021-09-28 08:22:27', '2021-09-28 08:22:27', '0');
INSERT INTO `payment_info` VALUES ('5', 'ATGUIGU1632818737862798', '17', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-28 08:45:41', '2021-09-29 08:45:38', '0');
INSERT INTO `payment_info` VALUES ('6', 'ATGUIGU1632819433352855', '18', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-28 08:57:30', '2021-09-30 00:56:39', '0');
INSERT INTO `payment_info` VALUES ('7', 'ATGUIGU1632885323098257', '19', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-29 03:15:39', '2021-09-29 03:21:03', '0');
INSERT INTO `payment_info` VALUES ('8', 'ATGUIGU1632885701645779', '20', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-29 03:21:55', '2021-09-29 03:59:52', '0');
INSERT INTO `payment_info` VALUES ('9', 'ATGUIGU1632887873219914', '21', '2', 'ALIPAY', '2021092922001423431445765363', '12331.00', '结婚买房买车买手机.', 'CLOSED', '2021-09-29 11:58:13', '{gmt_create=2021-09-29 11:58:04, charset=utf-8, gmt_payment=2021-09-29 11:58:12, notify_time=2021-09-29 11:58:12, subject=结婚买房买车买手机., buyer_id=2088022559923434, invoice_amount=0.01, version=1.0, notify_id=2021092900222115812023431445314217, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"PCREDIT\"}], notify_type=trade_status_sync, out_trade_no=ATGUIGU1632887873219914, total_amount=0.01, trade_status=TRADE_SUCCESS, trade_no=2021092922001423431445765363, auth_app_id=2021001163617452, receipt_amount=0.01, point_amount=0.00, buyer_pay_amount=0.01, app_id=2021001163617452, seller_id=2088831489324244}', '2021-09-29 03:58:00', '2021-09-29 03:59:55', '0');
INSERT INTO `payment_info` VALUES ('10', 'ATGUIGU1632897773754138', '22', '2', 'ALIPAY', '2021092922001456601407323670', '12331.00', '结婚买房买车买手机.', 'PAID', '2021-09-29 14:43:21', '{gmt_create=2021-09-29 14:43:12, charset=utf-8, gmt_payment=2021-09-29 14:43:18, notify_time=2021-09-29 14:43:18, subject=结婚买房买车买手机., buyer_id=2088702818256601, invoice_amount=0.01, version=1.0, notify_id=2021092900222144318056601458496431, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"PCREDIT\"}], notify_type=trade_status_sync, out_trade_no=ATGUIGU1632897773754138, total_amount=0.01, trade_status=TRADE_SUCCESS, trade_no=2021092922001456601407323670, auth_app_id=2021001163617452, receipt_amount=0.01, point_amount=0.00, buyer_pay_amount=0.01, app_id=2021001163617452, seller_id=2088831489324244}', '2021-09-29 06:43:07', '2021-09-29 06:43:21', '0');
INSERT INTO `payment_info` VALUES ('11', 'ATGUIGU1632902189026241', '23', '2', 'ALIPAY', '2021092922001439851447743300', '12331.00', '结婚买房买车买手机.', 'PAID', '2021-09-29 15:56:51', '{gmt_create=2021-09-29 15:56:44, charset=utf-8, gmt_payment=2021-09-29 15:56:49, notify_time=2021-09-29 15:56:49, subject=结婚买房买车买手机., buyer_id=2088802051139850, invoice_amount=0.01, version=1.0, notify_id=2021092900222155649039851434384563, fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"PCREDIT\"}], notify_type=trade_status_sync, out_trade_no=ATGUIGU1632902189026241, total_amount=0.01, trade_status=TRADE_SUCCESS, trade_no=2021092922001439851447743300, auth_app_id=2021001163617452, receipt_amount=0.01, point_amount=0.00, buyer_pay_amount=0.01, app_id=2021001163617452, seller_id=2088831489324244}', '2021-09-29 07:56:40', '2021-09-29 07:56:51', '0');
INSERT INTO `payment_info` VALUES ('12', 'ATGUIGU1632903675426976', '26', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'UNPAID', null, null, '2021-09-29 08:22:24', '2021-09-29 08:22:24', '0');
INSERT INTO `payment_info` VALUES ('13', 'ATGUIGU1632969258280390', '27', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'UNPAID', null, null, '2021-09-30 02:34:30', '2021-09-30 02:34:30', '0');
INSERT INTO `payment_info` VALUES ('14', 'ATGUIGU1632969328469477', '28', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'UNPAID', null, null, '2021-09-30 02:35:30', '2021-09-30 02:35:30', '0');
INSERT INTO `payment_info` VALUES ('15', 'ATGUIGU1632971307585127', '29', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-30 03:08:30', '2021-09-30 03:09:03', '0');
INSERT INTO `payment_info` VALUES ('16', 'ATGUIGU1632971412713182', '30', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-30 03:10:15', '2021-09-30 03:11:05', '0');
INSERT INTO `payment_info` VALUES ('17', 'ATGUIGU1632971816072241', '31', '2', 'ALIPAY', null, '12331.00', '结婚买房买车买手机.', 'CLOSED', null, null, '2021-09-30 03:16:58', '2021-09-30 03:17:09', '0');
