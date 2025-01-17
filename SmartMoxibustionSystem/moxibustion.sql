/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : moxibustion

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 30/12/2024 23:56:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for patient_data
-- ----------------------------
DROP TABLE IF EXISTS `patient_data`;
CREATE TABLE `patient_data`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `date` date NULL DEFAULT NULL,
  `time` time NULL DEFAULT NULL,
  `temperature` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient_data
-- ----------------------------
INSERT INTO `patient_data` VALUES (1, 'aa', 'woman', '2024-12-28', '20:27:10', 25);
INSERT INTO `patient_data` VALUES (2, 'bb', '女', '2024-12-28', '21:08:54', 36.5);
INSERT INTO `patient_data` VALUES (3, 'cc', '男', '2024-12-29', '20:57:51', 25);
INSERT INTO `patient_data` VALUES (4, '小王', '男', '2024-12-29', '21:09:27', 37);

-- ----------------------------
-- Table structure for temperature_data
-- ----------------------------
DROP TABLE IF EXISTS `temperature_data`;
CREATE TABLE `temperature_data`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `temperature` double NOT NULL,
  `recorded_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of temperature_data
-- ----------------------------
INSERT INTO `temperature_data` VALUES (31, '患者A', 39.28396979252081, '2024-12-30 05:04:24');
INSERT INTO `temperature_data` VALUES (32, '患者A', 42.658562460215975, '2024-12-30 05:06:14');
INSERT INTO `temperature_data` VALUES (33, '患者A', 49.896118860376966, '2024-12-30 05:08:17');
INSERT INTO `temperature_data` VALUES (34, '患者A', 48.04876575740374, '2024-12-30 05:15:10');
INSERT INTO `temperature_data` VALUES (35, '患者A', 48.45858107882223, '2024-12-30 14:28:38');
INSERT INTO `temperature_data` VALUES (36, '患者A', 43.13337099563912, '2024-12-30 19:51:19');
INSERT INTO `temperature_data` VALUES (37, '患者A', 42.1858610137787, '2024-12-30 19:58:45');
INSERT INTO `temperature_data` VALUES (38, '患者A', 45.1314164591901, '2024-12-30 20:00:27');
INSERT INTO `temperature_data` VALUES (39, '患者A', 39.14465290793542, '2024-12-30 20:02:54');
INSERT INTO `temperature_data` VALUES (40, '患者A', 41.74004361970081, '2024-12-30 20:04:26');
INSERT INTO `temperature_data` VALUES (41, '患者A', 47.093644011396634, '2024-12-30 20:05:28');
INSERT INTO `temperature_data` VALUES (42, '患者A', 45.615181736026884, '2024-12-30 20:07:39');
INSERT INTO `temperature_data` VALUES (43, '患者A', 42.740053547978185, '2024-12-30 20:11:04');
INSERT INTO `temperature_data` VALUES (44, '患者A', 47.646484841554035, '2024-12-30 20:12:58');
INSERT INTO `temperature_data` VALUES (45, '患者A', 42.57996641274245, '2024-12-30 20:15:37');
INSERT INTO `temperature_data` VALUES (46, '患者A', 48.382030645523656, '2024-12-30 20:19:28');
INSERT INTO `temperature_data` VALUES (47, '患者A', 38.15136896612917, '2024-12-30 20:19:45');
INSERT INTO `temperature_data` VALUES (48, '患者A', 49.00788987611262, '2024-12-30 20:20:17');
INSERT INTO `temperature_data` VALUES (49, '患者A', 42.65515278480252, '2024-12-30 20:20:49');
INSERT INTO `temperature_data` VALUES (50, '患者A', 43.187532058436815, '2024-12-30 21:07:28');
INSERT INTO `temperature_data` VALUES (51, '患者A', 45.82011482622022, '2024-12-30 21:11:11');
INSERT INTO `temperature_data` VALUES (52, '患者A', 41.717884807854595, '2024-12-30 21:32:00');
INSERT INTO `temperature_data` VALUES (53, '患者A', 48.54430860684461, '2024-12-30 21:35:17');
INSERT INTO `temperature_data` VALUES (54, '患者A', 47.52529194821341, '2024-12-30 21:39:37');
INSERT INTO `temperature_data` VALUES (55, '患者A', 37.62336642774984, '2024-12-30 22:00:03');
INSERT INTO `temperature_data` VALUES (56, '患者A', 40.73322221575453, '2024-12-30 22:03:04');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '123', '123', '123', '2024-12-30 20:07:33');
INSERT INTO `users` VALUES (2, 'aaa', 'aa', '123', '2024-12-30 12:15:48');
INSERT INTO `users` VALUES (5, 'asdf', 'asdf', 'asdf', '2024-12-30 13:38:01');

SET FOREIGN_KEY_CHECKS = 1;
