/*
Navicat MySQL Data Transfer

Source Server         : wepull
Source Server Version : 50528
Source Host           : localhost:3308
Source Database       : db1

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-05-29 16:14:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for job_execution_log
-- ----------------------------
DROP TABLE IF EXISTS `job_execution_log`;
CREATE TABLE `job_execution_log` (
  `id` varchar(40) NOT NULL,
  `job_name` varchar(100) NOT NULL,
  `task_id` varchar(255) NOT NULL,
  `hostname` varchar(255) NOT NULL,
  `ip` varchar(50) NOT NULL,
  `sharding_item` int(11) NOT NULL,
  `execution_source` varchar(20) NOT NULL,
  `failure_cause` varchar(4000) DEFAULT NULL,
  `is_success` int(11) NOT NULL,
  `start_time` timestamp NULL DEFAULT NULL,
  `complete_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for job_status_trace_log
-- ----------------------------
DROP TABLE IF EXISTS `job_status_trace_log`;
CREATE TABLE `job_status_trace_log` (
  `id` varchar(40) NOT NULL,
  `job_name` varchar(100) NOT NULL,
  `original_task_id` varchar(255) NOT NULL,
  `task_id` varchar(255) NOT NULL,
  `slave_id` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL,
  `execution_type` varchar(20) NOT NULL,
  `sharding_item` varchar(100) NOT NULL,
  `state` varchar(20) NOT NULL,
  `message` varchar(4000) DEFAULT NULL,
  `creation_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `TASK_ID_STATE_INDEX` (`task_id`,`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- Table structure for solr
-- ----------------------------
DROP TABLE IF EXISTS `solr`;
CREATE TABLE `solr` (
  `id` varchar(255) NOT NULL COMMENT '主键信息',
  `price` int(11) DEFAULT NULL COMMENT '价格',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `name` varchar(255) DEFAULT NULL COMMENT '称呼',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of solr
-- ----------------------------
INSERT INTO `solr` VALUES ('18052811531806746313', '250', 'solr', 'solr是一款强大的企业级全文检索搜索引擎，底层基于lunce实现。', '2018-05-16 00:00:00');
INSERT INTO `solr` VALUES ('18052811534908826683', '110', 'redis', 'redis是一款分布式key-value缓存框架', '2018-05-23 00:00:00');
INSERT INTO `solr` VALUES ('18052811542638782403', '150', 'zookeeper', 'zookeeper是一款强大的协处理服务组件。', '2018-05-15 00:00:00');
INSERT INTO `solr` VALUES ('18052811551223769200', '200', 'dubbo', 'dubbo是阿里开源的一款SOA服务框架，提供远程RPC服务。', '2018-05-16 00:00:00');
INSERT INTO `solr` VALUES ('18052811561112625780', '120', 'mycat', 'mycat是一款完全开源的数据库中间件，可以方便快捷的提供读写分离，数据分片等功能。', '2018-05-01 00:00:00');
INSERT INTO `solr` VALUES ('18052811572925047664', '100', 'atomikos', 'atomikos是基于XA事务的分布式事务提供方，它基于java提供2步事务操作，保证事务一致性。', '2018-05-28 00:00:00');

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `pkid` varchar(255) NOT NULL COMMENT '主键',
  `sname` varchar(255) DEFAULT NULL COMMENT '职员姓名',
  `sex` varchar(1) DEFAULT NULL COMMENT '职员性别(1:男 0：女)',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `CREATE_DATE` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATEBY` varchar(255) DEFAULT NULL COMMENT '创建者',
  `UPDATE_DATE` datetime DEFAULT NULL COMMENT '更新时间',
  `UPDATEBY` varchar(255) DEFAULT NULL COMMENT '更新人',
  `POLICE` blob COMMENT 'police',
  PRIMARY KEY (`pkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(11) NOT NULL,
  `sname` varchar(100) NOT NULL COMMENT '姓名',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('179286849', '王大锤', '1', '27', '2018-05-31 00:00:00');
INSERT INTO `student` VALUES ('553573954', '赵六', '1', '24', '2018-05-08 00:00:00');
INSERT INTO `student` VALUES ('554577356', '王五', '1', '25', '2018-05-16 00:00:00');
INSERT INTO `student` VALUES ('558638821', '李本源', '1', '30', '2018-05-30 00:00:00');
INSERT INTO `student` VALUES ('645493166', '李四', '1', '23', '2018-05-08 00:00:00');
INSERT INTO `student` VALUES ('731301385', '张三', '1', '22', '2018-05-09 00:00:00');
INSERT INTO `student` VALUES ('905511136', '王晓', '0', '25', '2018-05-15 00:00:00');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '日志标题',
  `type` char(1) COLLATE utf8_bin DEFAULT '1' COMMENT '日志类型 1:正常 2：异常',
  `user_id` varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '执行操作用户',
  `request_uri` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '请求URI',
  `class_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '执行类名',
  `method_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '执行方法名称',
  `function_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '功能模块名称',
  `user_agent` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '用户代理',
  `remote_ip` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作IP地址',
  `request_method` varchar(5) COLLATE utf8_bin DEFAULT NULL COMMENT '操作方式',
  `request_params` text COLLATE utf8_bin COMMENT '请求参数',
  `request_mac` varchar(60) COLLATE utf8_bin DEFAULT NULL COMMENT '设备MAC',
  `exception` text COLLATE utf8_bin COMMENT '异常信息',
  `action_thread` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '执行线程',
  `action_start_time` datetime DEFAULT NULL COMMENT '开始执行时刻',
  `action_end_time` datetime DEFAULT NULL COMMENT '结束执行时刻',
  `action_time` bigint(20) DEFAULT NULL COMMENT '执行耗时 单位(毫秒)',
  `create_date` datetime DEFAULT NULL COMMENT '创建日志时间',
  `dbName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '数据库实例',
  PRIMARY KEY (`id`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_date`)
) ENGINE=InnoDB AUTO_INCREMENT=576 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
