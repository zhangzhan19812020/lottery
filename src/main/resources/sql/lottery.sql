/*
 Navicat MySQL Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : lottery

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 06/08/2021 13:48:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lottery
-- ----------------------------
DROP TABLE IF EXISTS `lottery`;
CREATE TABLE `lottery`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `topic` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `state` int NOT NULL DEFAULT 1 COMMENT '活动状态，1-上线，2-下线',
  `link` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `images` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lottery
-- ----------------------------
INSERT INTO `lottery` VALUES (1, '幸运大抽奖', 1, 'localhost:8080/lottery', NULL, '2021-08-01 22:06:23', '2021-08-31 22:06:28', '2021-08-01 22:06:32');

-- ----------------------------
-- Table structure for lottery_item
-- ----------------------------
DROP TABLE IF EXISTS `lottery_item`;
CREATE TABLE `lottery_item`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `lottery_id` int NOT NULL COMMENT '活动id',
  `item_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖项名称',
  `level` int NOT NULL COMMENT '奖项等级',
  `percent` decimal(2, 2) NOT NULL COMMENT '奖项概率',
  `prize_id` int NOT NULL COMMENT '奖品id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `default_item` int NOT NULL DEFAULT 0 COMMENT '是否是默认的奖项, 0-不是 ， 1-是',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lottery_item
-- ----------------------------
INSERT INTO `lottery_item` VALUES (1, 1, '一等奖', 1, 0.02, 1, '2021-07-01 22:10:00', 0);
INSERT INTO `lottery_item` VALUES (2, 1, '二等奖', 2, 0.09, 2, '2021-07-01 22:11:10', 0);
INSERT INTO `lottery_item` VALUES (3, 1, '三等奖', 3, 0.20, 3, '2021-07-01 22:11:37', 0);
INSERT INTO `lottery_item` VALUES (4, 1, '四等奖', 4, 0.30, 4, '2021-07-01 22:12:25', 0);
INSERT INTO `lottery_item` VALUES (5, 1, '五等奖', 5, 0.40, 5, '2021-07-01 22:12:48', 0);
INSERT INTO `lottery_item` VALUES (6, 1, '六等奖', 6, 0.80, 6, '2021-07-01 22:13:04', 1);

-- ----------------------------
-- Table structure for lottery_prize
-- ----------------------------
DROP TABLE IF EXISTS `lottery_prize`;
CREATE TABLE `lottery_prize`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `lottery_id` int NOT NULL COMMENT '活动ID',
  `prize_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖品名称',
  `prize_type` int NOT NULL COMMENT '奖品类型， -1-谢谢参与、1-普通奖品、2-唯一性奖品',
  `total_stock` int NOT NULL DEFAULT 0 COMMENT '总库存',
  `valid_stock` int NOT NULL DEFAULT 0 COMMENT '可用库存',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lottery_prize
-- ----------------------------
INSERT INTO `lottery_prize` VALUES (1, 1, '55寸小米电视', 1, 1, 1, NULL);
INSERT INTO `lottery_prize` VALUES (2, 1, 'AirPods', 1, 5, 3, NULL);
INSERT INTO `lottery_prize` VALUES (3, 1, '摄影背包', 1, 10, 6, NULL);
INSERT INTO `lottery_prize` VALUES (4, 1, '三脚架套餐', 1, 15, 8, NULL);
INSERT INTO `lottery_prize` VALUES (5, 1, '移动电源', 1, 40, 32, NULL);
INSERT INTO `lottery_prize` VALUES (6, 1, '记事本', -1, 1000, 996, NULL);

-- ----------------------------
-- Table structure for lottery_record
-- ----------------------------
DROP TABLE IF EXISTS `lottery_record`;
CREATE TABLE `lottery_record`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_ip` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `item_id` int NOT NULL,
  `prize_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `account_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1421 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lottery_record
-- ----------------------------
INSERT INTO `lottery_record` VALUES (1434, 'yd', 6, '记事本', '2021-08-06 13:25:29', '阿三');
INSERT INTO `lottery_record` VALUES (1435, 'yd', 2, 'AirPods', '2021-08-06 13:25:41', '阿三');
INSERT INTO `lottery_record` VALUES (1436, 'yd', 6, '记事本', '2021-08-06 13:25:54', '阿三');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '工号',
  `true_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `draw_times` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (41, 'yd', '阿三', 3);
INSERT INTO `sys_user` VALUES (42, 'mg', '赤佬', 2);

SET FOREIGN_KEY_CHECKS = 1;
