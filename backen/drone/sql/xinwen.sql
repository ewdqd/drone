/*
 Navicat Premium Dump SQL

 Source Server         : xinwen
 Source Server Type    : MySQL
 Source Server Version : 80041 (8.0.41)
 Source Host           : localhost:3306
 Source Schema         : xinwen

 Target Server Type    : MySQL
 Target Server Version : 80041 (8.0.41)
 File Encoding         : 65001

 Date: 02/12/2025 19:48:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID，外键，关联的是用户表，标识哪个用户产生的行为记录',
  `news_id` int NULL DEFAULT NULL COMMENT '新闻ID，外键，关联的是新闻表，标识哪篇新闻产生的行为操作',
  `type` int NULL DEFAULT NULL COMMENT '行为类型（1：点赞行为；2：阅读行为；3：收藏行为；4：停留行为）',
  `times` bigint NULL DEFAULT NULL COMMENT '停留时间（只有当行为操作是停留时才需要设置）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '记录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of action
-- ----------------------------
INSERT INTO `action` VALUES (1, NULL, 8, 2, NULL, '2025-11-27 09:46:43');
INSERT INTO `action` VALUES (2, NULL, 8, 2, NULL, '2025-11-27 09:46:43');
INSERT INTO `action` VALUES (3, NULL, 8, 2, NULL, '2025-11-27 10:08:22');
INSERT INTO `action` VALUES (4, NULL, 8, 2, NULL, '2025-11-27 10:08:22');
INSERT INTO `action` VALUES (5, NULL, 8, 2, NULL, '2025-11-27 10:09:14');
INSERT INTO `action` VALUES (6, NULL, 8, 2, NULL, '2025-11-27 10:09:14');
INSERT INTO `action` VALUES (7, NULL, 8, 2, NULL, '2025-11-27 10:09:18');
INSERT INTO `action` VALUES (8, NULL, 8, 2, NULL, '2025-11-27 10:09:18');
INSERT INTO `action` VALUES (9, NULL, 8, 4, 3185, '2025-11-27 10:09:21');
INSERT INTO `action` VALUES (10, NULL, 8, 4, 8628, '2025-11-27 10:09:23');
INSERT INTO `action` VALUES (11, NULL, 8, 2, NULL, '2025-11-27 10:09:59');
INSERT INTO `action` VALUES (12, NULL, 8, 2, NULL, '2025-11-27 10:09:59');
INSERT INTO `action` VALUES (13, NULL, 8, 4, 15013, '2025-11-27 10:10:14');
INSERT INTO `action` VALUES (14, NULL, 8, 2, NULL, '2025-11-27 10:10:15');
INSERT INTO `action` VALUES (15, NULL, 8, 2, NULL, '2025-11-27 10:10:15');
INSERT INTO `action` VALUES (16, NULL, 8, 2, NULL, '2025-11-27 10:10:51');
INSERT INTO `action` VALUES (17, NULL, 8, 2, NULL, '2025-11-27 10:10:51');
INSERT INTO `action` VALUES (18, NULL, 8, 4, 4975, '2025-11-27 10:10:56');
INSERT INTO `action` VALUES (19, NULL, 8, 2, NULL, '2025-11-27 10:10:57');
INSERT INTO `action` VALUES (20, NULL, 8, 2, NULL, '2025-11-27 10:10:57');
INSERT INTO `action` VALUES (21, NULL, 8, 4, 18386, '2025-11-27 10:11:15');
INSERT INTO `action` VALUES (22, NULL, 8, 2, NULL, '2025-11-27 10:11:18');
INSERT INTO `action` VALUES (23, NULL, 8, 2, NULL, '2025-11-27 10:11:18');
INSERT INTO `action` VALUES (24, NULL, 8, 4, 2830, '2025-11-27 10:11:20');
INSERT INTO `action` VALUES (25, NULL, 7, 2, NULL, '2025-11-27 10:11:22');
INSERT INTO `action` VALUES (26, NULL, 7, 2, NULL, '2025-11-27 10:11:22');
INSERT INTO `action` VALUES (27, NULL, 7, 4, 3934, '2025-11-27 10:11:26');
INSERT INTO `action` VALUES (28, NULL, 3, 2, NULL, '2025-11-27 10:11:29');
INSERT INTO `action` VALUES (29, NULL, 3, 2, NULL, '2025-11-27 10:11:29');
INSERT INTO `action` VALUES (30, NULL, 3, 4, 2117, '2025-11-27 10:11:32');
INSERT INTO `action` VALUES (31, NULL, 3, 2, NULL, '2025-11-27 10:11:32');
INSERT INTO `action` VALUES (32, NULL, 3, 2, NULL, '2025-11-27 10:11:32');
INSERT INTO `action` VALUES (33, NULL, 3, 4, 2917, '2025-11-27 10:11:35');
INSERT INTO `action` VALUES (34, NULL, 8, 2, NULL, '2025-11-27 10:14:20');
INSERT INTO `action` VALUES (35, NULL, 8, 2, NULL, '2025-11-27 10:14:20');
INSERT INTO `action` VALUES (36, NULL, 8, 4, 113001, '2025-11-27 10:16:13');
INSERT INTO `action` VALUES (37, NULL, 8, 2, NULL, '2025-11-27 10:16:13');
INSERT INTO `action` VALUES (38, NULL, 8, 2, NULL, '2025-11-27 10:16:13');
INSERT INTO `action` VALUES (39, NULL, 8, 4, 3739, '2025-11-27 10:16:17');
INSERT INTO `action` VALUES (40, NULL, 7, 2, NULL, '2025-11-27 10:16:19');
INSERT INTO `action` VALUES (41, NULL, 7, 2, NULL, '2025-11-27 10:16:19');
INSERT INTO `action` VALUES (42, NULL, 7, 4, 3979, '2025-11-27 10:16:23');
INSERT INTO `action` VALUES (43, NULL, 8, 2, NULL, '2025-11-27 10:17:03');
INSERT INTO `action` VALUES (44, NULL, 8, 2, NULL, '2025-11-27 10:17:03');
INSERT INTO `action` VALUES (45, NULL, 8, 4, 8487, '2025-11-27 10:17:11');
INSERT INTO `action` VALUES (46, NULL, 8, 2, NULL, '2025-11-27 10:17:12');
INSERT INTO `action` VALUES (47, NULL, 8, 2, NULL, '2025-11-27 10:17:12');
INSERT INTO `action` VALUES (48, NULL, 8, 2, NULL, '2025-11-27 10:25:57');
INSERT INTO `action` VALUES (49, NULL, 8, 2, NULL, '2025-11-27 10:25:57');
INSERT INTO `action` VALUES (50, NULL, 8, 4, 20684, '2025-11-27 10:26:17');
INSERT INTO `action` VALUES (51, NULL, 8, 2, NULL, '2025-11-27 10:26:18');
INSERT INTO `action` VALUES (52, NULL, 8, 2, NULL, '2025-11-27 10:26:18');
INSERT INTO `action` VALUES (53, NULL, 8, 4, 132592, '2025-11-27 10:28:30');
INSERT INTO `action` VALUES (54, NULL, 8, 2, NULL, '2025-11-27 10:28:31');
INSERT INTO `action` VALUES (55, NULL, 8, 2, NULL, '2025-11-27 10:28:31');
INSERT INTO `action` VALUES (56, NULL, 8, 2, NULL, '2025-11-27 10:28:35');
INSERT INTO `action` VALUES (57, NULL, 8, 2, NULL, '2025-11-27 10:28:35');
INSERT INTO `action` VALUES (58, NULL, 8, 2, NULL, '2025-11-27 10:28:40');
INSERT INTO `action` VALUES (59, NULL, 8, 2, NULL, '2025-11-27 10:28:40');
INSERT INTO `action` VALUES (60, NULL, 8, 2, NULL, '2025-11-27 10:30:35');
INSERT INTO `action` VALUES (61, NULL, 8, 2, NULL, '2025-11-27 10:30:35');
INSERT INTO `action` VALUES (62, NULL, 8, 2, NULL, '2025-11-27 10:44:45');
INSERT INTO `action` VALUES (63, NULL, 8, 2, NULL, '2025-11-27 10:44:45');
INSERT INTO `action` VALUES (64, NULL, 8, 2, NULL, '2025-11-27 10:45:59');
INSERT INTO `action` VALUES (65, NULL, 8, 2, NULL, '2025-11-27 10:45:59');
INSERT INTO `action` VALUES (66, NULL, 8, 2, NULL, '2025-11-27 10:46:11');
INSERT INTO `action` VALUES (67, NULL, 8, 2, NULL, '2025-11-27 10:46:11');
INSERT INTO `action` VALUES (68, NULL, 8, 2, NULL, '2025-11-27 10:58:41');
INSERT INTO `action` VALUES (69, NULL, 8, 2, NULL, '2025-11-27 10:58:41');
INSERT INTO `action` VALUES (70, NULL, 2, 2, NULL, '2025-11-27 11:10:20');
INSERT INTO `action` VALUES (71, NULL, 2, 2, NULL, '2025-11-27 11:10:20');
INSERT INTO `action` VALUES (72, NULL, 3, 2, NULL, '2025-11-27 14:17:34');
INSERT INTO `action` VALUES (73, NULL, 3, 2, NULL, '2025-11-27 14:17:34');
INSERT INTO `action` VALUES (74, NULL, 8, 2, NULL, '2025-11-27 14:17:47');
INSERT INTO `action` VALUES (75, NULL, 8, 2, NULL, '2025-11-27 14:17:47');
INSERT INTO `action` VALUES (76, NULL, 3, 2, NULL, '2025-11-27 14:17:49');
INSERT INTO `action` VALUES (77, NULL, 3, 2, NULL, '2025-11-27 14:17:49');
INSERT INTO `action` VALUES (78, NULL, 8, 2, NULL, '2025-11-27 20:29:14');
INSERT INTO `action` VALUES (79, NULL, 8, 2, NULL, '2025-11-27 20:29:14');
INSERT INTO `action` VALUES (80, NULL, 8, 2, NULL, '2025-11-27 20:55:39');
INSERT INTO `action` VALUES (81, NULL, 8, 2, NULL, '2025-11-27 20:55:39');
INSERT INTO `action` VALUES (82, NULL, 2, 2, NULL, '2025-11-27 20:55:44');
INSERT INTO `action` VALUES (83, NULL, 2, 2, NULL, '2025-11-27 20:55:44');
INSERT INTO `action` VALUES (84, NULL, 3, 2, NULL, '2025-11-27 20:55:47');
INSERT INTO `action` VALUES (85, NULL, 3, 2, NULL, '2025-11-27 20:55:47');
INSERT INTO `action` VALUES (86, NULL, 3, 2, NULL, '2025-11-27 21:28:58');
INSERT INTO `action` VALUES (87, NULL, 3, 2, NULL, '2025-11-27 21:28:58');
INSERT INTO `action` VALUES (88, NULL, 3, 4, 4028, '2025-11-27 21:29:02');
INSERT INTO `action` VALUES (89, NULL, 8, 2, NULL, '2025-11-27 21:30:35');
INSERT INTO `action` VALUES (90, NULL, 8, 2, NULL, '2025-11-27 21:30:35');
INSERT INTO `action` VALUES (91, NULL, 8, 2, NULL, '2025-11-27 21:42:09');
INSERT INTO `action` VALUES (92, NULL, 2, 2, NULL, '2025-11-27 21:42:20');
INSERT INTO `action` VALUES (93, NULL, 3, 2, NULL, '2025-11-28 10:18:25');
INSERT INTO `action` VALUES (94, NULL, 3, 2, NULL, '2025-11-28 10:19:03');
INSERT INTO `action` VALUES (95, NULL, 3, 2, NULL, '2025-11-28 10:42:08');
INSERT INTO `action` VALUES (96, NULL, 3, 2, NULL, '2025-11-28 10:42:08');
INSERT INTO `action` VALUES (97, NULL, 3, 2, NULL, '2025-11-28 10:42:10');
INSERT INTO `action` VALUES (98, NULL, 4, 2, NULL, '2025-11-28 10:42:19');
INSERT INTO `action` VALUES (99, NULL, 4, 2, NULL, '2025-11-28 10:43:43');
INSERT INTO `action` VALUES (100, NULL, 4, 2, NULL, '2025-11-28 10:43:54');
INSERT INTO `action` VALUES (101, NULL, 4, 2, NULL, '2025-11-28 10:43:57');
INSERT INTO `action` VALUES (102, NULL, 4, 2, NULL, '2025-11-28 10:44:45');
INSERT INTO `action` VALUES (103, NULL, 3, 2, NULL, '2025-11-28 10:45:32');
INSERT INTO `action` VALUES (104, NULL, 3, 2, NULL, '2025-11-28 10:47:12');
INSERT INTO `action` VALUES (105, NULL, 3, 2, NULL, '2025-11-28 10:47:18');
INSERT INTO `action` VALUES (106, NULL, 3, 2, NULL, '2025-11-28 10:48:32');
INSERT INTO `action` VALUES (107, NULL, 3, 2, NULL, '2025-11-28 14:37:22');
INSERT INTO `action` VALUES (108, NULL, 3, 2, NULL, '2025-11-28 14:53:21');
INSERT INTO `action` VALUES (109, NULL, 3, 2, NULL, '2025-11-28 14:54:59');
INSERT INTO `action` VALUES (110, NULL, 3, 2, NULL, '2025-11-28 14:58:18');
INSERT INTO `action` VALUES (111, NULL, 3, 2, NULL, '2025-11-28 14:59:13');
INSERT INTO `action` VALUES (112, NULL, 2, 2, NULL, '2025-11-28 15:54:29');
INSERT INTO `action` VALUES (113, NULL, 3, 2, NULL, '2025-11-28 15:54:33');
INSERT INTO `action` VALUES (114, NULL, 2, 2, NULL, '2025-11-28 15:54:49');
INSERT INTO `action` VALUES (115, NULL, 2, 2, NULL, '2025-11-28 15:55:12');
INSERT INTO `action` VALUES (116, NULL, 3, 2, NULL, '2025-11-28 15:55:21');
INSERT INTO `action` VALUES (117, NULL, 4, 2, NULL, '2025-11-28 15:55:24');
INSERT INTO `action` VALUES (118, NULL, 6, 2, NULL, '2025-11-28 15:55:27');
INSERT INTO `action` VALUES (119, NULL, 7, 2, NULL, '2025-11-28 15:55:34');
INSERT INTO `action` VALUES (120, NULL, 7, 2, NULL, '2025-11-28 15:55:38');
INSERT INTO `action` VALUES (121, NULL, 8, 2, NULL, '2025-11-28 15:55:41');
INSERT INTO `action` VALUES (122, NULL, 4, 2, NULL, '2025-11-28 16:19:43');
INSERT INTO `action` VALUES (123, NULL, 6, 2, NULL, '2025-11-28 16:19:46');
INSERT INTO `action` VALUES (124, NULL, 7, 2, NULL, '2025-11-28 17:01:15');
INSERT INTO `action` VALUES (125, NULL, 2, 2, NULL, '2025-11-28 19:47:26');
INSERT INTO `action` VALUES (126, NULL, 2, 2, NULL, '2025-11-28 19:54:12');
INSERT INTO `action` VALUES (127, NULL, 2, 2, NULL, '2025-11-28 20:00:10');
INSERT INTO `action` VALUES (128, NULL, 3, 2, NULL, '2025-11-28 20:00:23');
INSERT INTO `action` VALUES (129, NULL, 3, 2, NULL, '2025-11-28 20:00:34');
INSERT INTO `action` VALUES (130, NULL, 3, 2, NULL, '2025-11-28 20:32:35');
INSERT INTO `action` VALUES (131, NULL, 3, 2, NULL, '2025-11-28 20:32:35');
INSERT INTO `action` VALUES (132, NULL, 3, 2, NULL, '2025-11-28 20:33:49');
INSERT INTO `action` VALUES (133, NULL, 3, 2, NULL, '2025-11-28 20:33:54');
INSERT INTO `action` VALUES (134, NULL, 3, 2, NULL, '2025-11-28 20:34:04');
INSERT INTO `action` VALUES (135, NULL, 2, 2, NULL, '2025-11-28 20:36:45');
INSERT INTO `action` VALUES (136, NULL, 2, 2, NULL, '2025-11-30 20:07:10');
INSERT INTO `action` VALUES (137, NULL, 2, 2, NULL, '2025-12-01 15:05:13');
INSERT INTO `action` VALUES (138, NULL, 2, 2, NULL, '2025-12-01 15:05:37');
INSERT INTO `action` VALUES (139, NULL, 2, 2, NULL, '2025-12-01 15:07:26');
INSERT INTO `action` VALUES (140, NULL, 2, 2, NULL, '2025-12-01 15:31:40');
INSERT INTO `action` VALUES (141, NULL, 2, 2, NULL, '2025-12-01 15:32:49');
INSERT INTO `action` VALUES (142, NULL, 2, 2, NULL, '2025-12-01 15:39:40');
INSERT INTO `action` VALUES (143, NULL, 2, 2, NULL, '2025-12-01 15:42:10');
INSERT INTO `action` VALUES (144, NULL, 2, 2, NULL, '2025-12-01 15:42:52');
INSERT INTO `action` VALUES (145, NULL, 4, 2, NULL, '2025-12-02 09:25:08');
INSERT INTO `action` VALUES (146, NULL, 6, 2, NULL, '2025-12-02 09:25:30');
INSERT INTO `action` VALUES (147, NULL, 3, 2, NULL, '2025-12-02 09:25:59');
INSERT INTO `action` VALUES (148, NULL, 4, 2, NULL, '2025-12-02 09:29:54');
INSERT INTO `action` VALUES (149, NULL, 4, 2, NULL, '2025-12-02 09:31:54');
INSERT INTO `action` VALUES (150, NULL, 7, 2, NULL, '2025-12-02 14:28:25');
INSERT INTO `action` VALUES (151, NULL, 7, 2, NULL, '2025-12-02 16:20:13');
INSERT INTO `action` VALUES (152, NULL, 9, 2, NULL, '2025-12-02 19:39:19');
INSERT INTO `action` VALUES (153, NULL, 9, 2, NULL, '2025-12-02 19:39:38');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID,自增',
  `news_type_id` int NULL DEFAULT NULL COMMENT '新闻类别ID，外键，在后端枚举类里面定义',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '摘要',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '封面',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `is_show` tinyint(1) NULL DEFAULT NULL COMMENT '是否展示（1：展示；0：不展示）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (2, NULL, '副校长韩江卫带队参加低空经济研究专业委员会揭牌仪式', '我校副校长出席2024信息通信业高层论坛，参与低空经济专委会揭牌并走访相关企业', '', '<p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">近日，由中国通信企业协会主办的 2024 中国信息通信业发展高层论坛在北京举行。我校副校长韩江卫出席会议并参加低空经济专业委员会揭牌仪式。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">本次论坛深入围绕 “创新数据要素 发展新质生产力” 进行交流研讨、实践分享。论坛期间，中国通信企业协会成立了低空经济专业委员会，我校作为副主任委员单位，韩江卫作为副主任委员参加揭牌仪式。低空经济专业委员会主要负责推进信息通信行业有关低空经济的相关政策研究与发展规划制定；参与相关法规和标准的制修订，促进行业规范化、标准化发展；推进相关专家智库建设与人才培养，推动产教融合；组织行业交流，搭建合作平台，推动技术与应用创新；开展市场调研，组织完成软课题研究，提供战略咨询；组织开展信息通信行业低空经济创新产品、方案的应用推广；发挥自律职能，维护公平竞争的市场环境。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">会议期间，韩江卫带队走访了中国铁塔股份有限公司，与中国铁塔副总经理刘国锋及相关部门负责人就联合建立联合创新研究中心进行了交流，共同探讨了下一步推进计划。走访了北京吉威空间信息股份有限公司、中国铁路通信信号股份有限公司、国家邮政局发展研究中心、湘邮科技股份有限公司等行业及校友企业，围绕人才培养、科技创新、产学研合作等方面进行了深入交流。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">我校低空经济研究院、通信与信息工程学院、计算机学院、现代邮政学院相关团队负责人及校友代表高河福、杨玺艳、王文彪等参加走访交流。</span></p>', 1, '2025-11-17 21:02:17');
INSERT INTO `news` VALUES (3, NULL, '我校与陕西省公安厅、陕西山利科技发展有限责任公司举行“无人机应用与反制联合实验室”揭牌签约仪式', '我校与陕西省公安厅、山利科技共建无人机应用与反制联合实验室', '', '<p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">7月5日上午，我校与陕西省公安厅、陕西山利科技发展有限责任公司三方“无人机应用与反制联合实验室”揭牌签约仪式在长安校区东区低空经济研究院举行。陕西省公安厅党委委员、副厅长、一级巡视员杜清江，我校校长卢光跃、副校长巩红，陕西山利科技发展有限责任公司董事长王文、总经理张勇敢出席活动。仪式由陕西省公安厅治安管理局局长张维护主持。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">揭牌仪式上，张维护介绍了“无人机应用与反制联合实验室”的基本情况，杜清江、卢光跃、王文共同为“无人机应用与反制联合实验室”揭牌。随后，杜清江、卢光跃、巩红、王文、张勇敢等一行参观了“无人机应用与反制联合实验室”。我校低空经济研究院刘伯阳副教授介绍了联合实验室建设及关键技术有关情况。陕西山利科技发展有限责任公司副总经理杨慧霞介绍了无人机安全与反制相关技术的主要应用场景。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">签约仪式上，杜清江、卢光跃、王文分别代表政、校、企三方共同签署“联合共建实验室”框架协议。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">杜清江表示，随着社会发展和技术成果演进，新的生产活动和社会治安问题需要借助无人机相关技术手段去应对，无人机在警务领域发挥着越来越重要的作用。西安邮电大学是一所具有鲜明行业特色的高校，通过推动建立政、校、企联合创新机制，能更好地支撑低空经济背景下的公安事业新需求。省公安厅对成立“无人机应用与反制联合实验室”高度重视，希望各方一起加强技术创新投入、集聚智慧和产业力量，助力实现公安事业在低空经济领域的新跨越。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">王文代表企业方成果转化团队，从积极响应国家低空经济发展战略、加大投入成果研制与市场转化、加强建设低空智能感知与安全态势管控平台等三个方面介绍了企业发展情况。希望以此次联合共建实验室为契机，进一步凝聚合作共识、实现优势互补，携手推动共赢新发展。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">刘伯阳从积极承担无人机飞行、反制培训任务、推动无人机应用、反制关键技术研发创新和建设科技创新平台等方面汇报了我校低空经济研究院相关工作。</span></p><p style=\"text-indent: 2em;\"><span style=\"font-size: 16px; font-family: 宋体;\">陕西省公安厅治安管理局、西安市公安局特警支队、雁塔分局、长安分局、高新分局相关负责人以及长安分局巡特警代表，陕西山利科技发展有限责任公司相关负责人，我校科研处、通信与信息工程学院、经济与管理学院、低空经济研究院负责人参加活动。</span></p>', 1, '2025-11-18 17:09:12');
INSERT INTO `news` VALUES (4, NULL, '学校召开低空经济校企合作交流座谈会', '我校与中兴通讯召开低空经济座谈会，围绕技术、标准等深化产学研合作', '', '<p style=\"text-indent: 2em; text-align: left;\"><span style=\"font-size: 16px; font-family: 宋体;\">3月13日上午，学校在长安校区西区办公楼第一会议室召开低空经济合作交流座谈会。中兴通讯无线产品线副总经理胡凯伟、中兴通讯（中国区）5G-A市场总监艾亚波等，我校副校长韩江卫、通信与信息工程学院院长姜静、经济与管理学院院长苏锦旗、低空经济研究院相关负责人等参加座谈会。会议由科研处副处长陈静主持。<br></span></p><p style=\"text-indent: 2em; text-align: left;\"><span style=\"font-size: 16px; font-family: 宋体;\">韩江卫对胡凯伟一行表示欢迎，介绍了学校的基本情况以及在低空经济领域的研究进展。他表示，近年来学校在低空经济领域取得了显著成果，特别是在飞行设施网、低空智联网和低空服务网等核心技术方面创新发展成果丰硕。希望进一步加强与企业的交流合作，推动科技成果转化，助力地方经济发展。低空经济研究院刘伯阳副教授介绍了学校在低空经济领域的研究现状，期待与中兴通讯在相关领域展开深入合作。</span></p><p style=\"text-indent: 2em; text-align: left;\"><span style=\"font-size: 16px; font-family: 宋体;\">胡凯伟介绍了中兴通讯在低空经济领域的研究进展。他表示，中兴通讯在5G-A、无人机导航、低空通信网络等方面拥有丰富的技术积累，希望与西邮在低空经济的标准化制定、技术研发和人才培养等方面展开深度合作，携手推动低空经济加速发展。<br></span></p><p style=\"text-indent: 2em; text-align: left;\"><span style=\"font-size: 16px; font-family: 宋体;\">会上，双方围绕低空经济的标准化制定、技术创新、人才培养等内容进行了深入讨论。未来，双方将继续深化合作，整合资源优势，推动产学研深度融合，为低空经济的发展注入新动能。</span></p>', 1, '2025-11-18 17:11:33');
INSERT INTO `news` VALUES (6, NULL, '中国电信陕西分公司来校调研洽谈', '陕西电信西咸事业部来校调研交流，双方拟在低空经济领域建深度合作机制\n', '', '<p style=\"text-indent: 2em;\">4 月 17 日上午，陕西电信西咸事业部副总经理魏晓兵、商务总监仝佳诚来校调研交流。我校科研处副处长（主持工作）董军、低空经济研究院执行院长刘信阳参加座谈会。</p><p style=\"text-indent: 2em;\">董军对魏晓兵一行来访表示热烈欢迎，介绍了西安邮电大学低空经济研究院在低空经济领域的工作开展情况。他表示，希望双方以此次交流为契机，依托电信强大的移动通信网络和云计算、大数据、物联网能力与西安邮电大学在数传链路、人工智能、低空物流等方面的科研优势结合，积极布局低空经济领域，在推动低空基础设施建设方面做出更大贡献。</p><p style=\"text-indent: 2em;\">魏晓兵对我校的热情接待表示感谢，高度评价了我校在低空经济领域的工作，希望双方能够校企联合，在低空特色场景落地、科研合作等方面建立深度长效合作机制，推动双方在低空领域尽快产生标志性成果。</p><p style=\"text-indent: 2em;\">座谈会上，双方围绕校企联合、低空特色专班、合作发展规划等方面进行了深入交流。</p>', 1, '2025-11-19 16:19:31');
INSERT INTO `news` VALUES (7, NULL, '通信与信息工程学院举办西安邮电大学高质量发展学术论坛（低空经济专场）', '我校举办建校 75 周年低空经济学术论坛，共探技术创新与产学研融合', '', '<p style=\"text-indent: 2em;\">为庆祝建校 75 周年，深入贯彻落实国家低空经济发展战略，5 月 8 日上午，西安邮电大学高质量发展学术论坛（低空经济专场）在通信与信息工程学院举行。通信与信息工程学院院长姜静、党委书记田卫蒙，科研处副处长（主持工作）董军、副处长陈静、张应辉，行业专家、优秀校友代表、学院师生代表齐聚一堂，围绕低空经济前沿技术与产业发展研讨交流，共筑产学研深度融合新平台。论坛由通信与信息工程学院副院长江帆主持。</p><p style=\"text-indent: 2em;\">姜静在致辞中表示，低空经济作为新质生产力的重要领域，对推动经济高质量发展具有战略意义。近年来，通信与信息工程学院依托学科优势，围绕低空通信网络优化、空天地智能感知、无人机管控及低空安全等核心技术开展攻关，着力构建 “通感一体，智能管导” 的低空智联网技术体系，力争在低空通信覆盖、多源数据融合、网络安全防护等方向实现突破，为我国低空经济信息基础设施建设提供技术支撑。希望本次论坛成为学术交流、成果共享、合作共赢的平台，共同推动低空经济领域创新发展。</p><p style=\"text-indent: 2em;\">田卫蒙向参会校友和嘉宾表示热烈欢迎和衷心感谢，简要介绍了学院近年来在学科建设、人才培养、科技创新、校园文化建设等方面取得的新成绩，表示学院坚持以党建引领学术，立足国家战略需求，将低空经济作为服务地方经济发展的重要方向，期待持续举办此类论坛能为契机，深化校友与母校的交流合作，推动产学研用协同创新。希望望院学子以优秀校友为榜样，传承西邮 “爱国、求是、奋进” 校训精神，发扬通院人 “团结务实、创新卓越” 的优良品质，为服务国家战略和地方经济社会发展贡献力量，携手奋进开创西邮人更加广阔的天地。</p><p style=\"text-indent: 2em;\">在主旨报告环节，行业专家和老师带来了精彩分享。中国移动通信集团陕西有限公司技术专家王伟以《中国移动低空智联系统关键技术研究与实践》为题，系统介绍了低空通信网络架构、5G-A 技术应用及行业落地案例；通信工程专业 03 级校友、陕西直升机股份有限公司市场营销总监蓝王力结合行业实践，深入分析通信技术与低空经济的融合路径；学院智能感知与信息处理团队负责人王宏刚从物联网产业视角，解读了空地一体低空物联网新架构与关键技术难题；学院低空经济研究团队负责人刘信阳展示了团队研发的低空态势感知系统、智能管控平台等科研成果，展现了学院在该领域的创新实践。</p><p style=\"text-indent: 2em;\">会上，与会人员围绕低空经济领域的技术问题、产学研合作模式、人才培养等方面进行了深入交流。未来，学院将持续深化校企合作，推进学科交叉创新，以更扎实的科研成果和人才培养成效，为服务国家战略、推动区域经济发展贡献力量，书写高质量发展新篇章。</p>', 1, '2025-11-20 20:40:56');
INSERT INTO `news` VALUES (9, NULL, '西安邮电大学协办低空经济交流会', '我校参与在陕西省咸阳市举办的低空经济交流会，现场分享科研成果，推动校政企在低空领域的协同发展。', '', '<p style=\"text-indent: 2em;\"><span style=\"color: rgba(0, 0, 0, 0.85); background-color: rgb(255, 255, 255); font-size: 16px; font-family: 宋体;\">为深入贯彻国家低空经济战略部署，探索产业协同发展新方向，10 月 17 日上午，“低空经济产业发展交流会” 在陕西省咸阳市成功举办。本次交流会作为 “2025 陕西交通创新创优大会” 的核心环节，由西安公路研究院有限公司、陕西省公路学会交通工程分会联合主办，我校低空经济研究院与交通运输部科学研究院南湖航空协同立体交通产业研究院等单位共同协办，陕西交控低空经济产业发展有限公司承办。近百位行业专家、企业代表及高校学者齐聚一堂，围绕 “场景创新驱动安全健康发展” 主题共话低空经济新机遇。</span></p><p style=\"text-indent: 2em;\"><span style=\"color: rgba(0, 0, 0, 0.85); background-color: rgb(255, 255, 255); font-size: 16px; font-family: 宋体;\">作为协办单位之一，我校低空经济研究院深度参与本次交流会的筹备与组织工作，积极推动高校科研资源与产业实践需求的精准对接。交流会现场，我校低空经济研究院刘伯阳副教授与陕西交控低空经济产业发展有限公司、西安公路研究院等单位负责人就低空经济技术研发、场景落地及人才培养等议题展开深入交流，为后续校地校企合作奠定坚实基础。西安公路研究院有限公司低空产业研究院院长刘毅明在会上发布并介绍 “交控低空” 无人机及智慧巡检平台，该平台整合了多领域技术成果，可广泛应用于交通巡检、应急救援等场景，引发与会者高度关注。</span></p><p style=\"text-indent: 2em;\"><span style=\"color: rgba(0, 0, 0, 0.85); background-color: rgb(255, 255, 255); font-size: 16px; font-family: 宋体;\">在主题分享环节，刘伯阳以 “低空物流与信息通信技术融合应用实践” 为题，结合学校在信息通信领域的学科优势，系统阐述了信息通信、人工智能技术在低空物流场景中的创新应用路径。他提出，“信息通信技术是低空物流高效运转的核心支撑，需通过技术融合破解空域管理、数据传输等痛点问题”，并分享了我校在低空通信网络优化、物流路径智能规划等方面的研究成果，为行业发展提供 “西邮方案”。</span></p><p style=\"text-indent: 2em;\"><span style=\"color: rgba(0, 0, 0, 0.85); background-color: rgb(255, 255, 255); font-size: 16px; font-family: 宋体;\">本次交流会的成功举办，不仅为我校搭建了展示低空经济领域科研成果的平台，更推动了学校与政府、企业、科研院所的深度协同。</span></p>', 1, '2025-12-02 19:39:06');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `loginid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `sex` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '性别',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  `userstatus` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学生，教师，管理员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (2, '胡拓拓', '8937984', '1', '801222', '15565369115', '1');
INSERT INTO `users` VALUES (3, '李雅睿', '3473453', '2', '80122', '15565369115', '1');
INSERT INTO `users` VALUES (4, '刘心如', '45349583', '2', '80122', '15565369115', '1');
INSERT INTO `users` VALUES (5, '贾申岳', '93453', '1', '801', '15565369115', '1');
INSERT INTO `users` VALUES (6, '李璐', '3497435', '2', '8012', '15565369115', '1');
INSERT INTO `users` VALUES (7, '张文琦', '383858', '1', '8012', '15565369115', '0');
INSERT INTO `users` VALUES (8, '宋夫文', '8435737', '1', '8012', '15565369115', '1');
INSERT INTO `users` VALUES (9, '陈凡', '34857745875', '2', '801', '8397854784575', '1');
INSERT INTO `users` VALUES (41, '000', '111', '1', '111', '15565369115', '1');
INSERT INTO `users` VALUES (42, '111', '111', '2', '111', '15565369115', '1');
INSERT INTO `users` VALUES (43, '111', '111', '2', '111', '15565369115', '0');
INSERT INTO `users` VALUES (44, '111', '111', '2', '3333', '15565369115', '0');
INSERT INTO `users` VALUES (45, '111', '111', '2', '1111', '15565369115', '1');
INSERT INTO `users` VALUES (46, '111', '111', '1', '1111', '15565369115', '1');
INSERT INTO `users` VALUES (47, '111', '1111', '1', '1111', '15565369115', '1');
INSERT INTO `users` VALUES (48, '222', '222', '1', '1111', '15565369115', '0');

SET FOREIGN_KEY_CHECKS = 1;
