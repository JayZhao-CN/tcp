/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 80026
Source Host           : localhost:3306
Source Database       : ue_sys

Target Server Type    : MYSQL
Target Server Version : 80026
File Encoding         : 65001

Date: 2022-06-16 11:53:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ue_friend
-- ----------------------------
DROP TABLE IF EXISTS `ue_friend`;
CREATE TABLE `ue_friend` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_account` varchar(255) DEFAULT NULL,
  `friend_account` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of ue_friend
-- ----------------------------

-- ----------------------------
-- Table structure for ue_group
-- ----------------------------
DROP TABLE IF EXISTS `ue_group`;
CREATE TABLE `ue_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) DEFAULT NULL,
  `group_name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of ue_group
-- ----------------------------

-- ----------------------------
-- Table structure for ue_group_user
-- ----------------------------
DROP TABLE IF EXISTS `ue_group_user`;
CREATE TABLE `ue_group_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `group_id` varchar(255) DEFAULT NULL,
  `account` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of ue_group_user
-- ----------------------------

-- ----------------------------
-- Table structure for ue_msg_list
-- ----------------------------
DROP TABLE IF EXISTS `ue_msg_list`;
CREATE TABLE `ue_msg_list` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender` varchar(255) DEFAULT NULL,
  `reciever` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of ue_msg_list
-- ----------------------------

-- ----------------------------
-- Table structure for ue_user
-- ----------------------------
DROP TABLE IF EXISTS `ue_user`;
CREATE TABLE `ue_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `level` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;

-- ----------------------------
-- Records of ue_user
-- ----------------------------
INSERT INTO `ue_user` VALUES ('1', '9527', '123456', '杰哥', null);
INSERT INTO `ue_user` VALUES ('7', '1688', '123456', '乌鸡', null);
INSERT INTO `ue_user` VALUES ('8', '2468', '123456', '乌鸦', null);
INSERT INTO `ue_user` VALUES ('9', '7535', '123456', '乌鱼子', null);
INSERT INTO `ue_user` VALUES ('10', '7878', '123456', '阿伟', null);
INSERT INTO `ue_user` VALUES ('11', '1234', '123456', '贝贝', null);
INSERT INTO `ue_user` VALUES ('12', '2234', '123456', '刘能', null);
