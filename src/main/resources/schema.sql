CREATE TABLE IF NOT EXISTS `user_master` (
                                             `user_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_email` varchar(50) NOT NULL,
    `user_pw` varchar(100) NOT NULL,
    `user_name` varchar(20) NOT NULL,
    `user_addr` varchar(100) NOT NULL,
    `user_nickname` varchar(20) NOT NULL,
    `user_fav` varchar(50) DEFAULT NULL,
    `user_birth` date NOT NULL,
    `user_gender` smallint(6) NOT NULL,
    `user_phone` varchar(20) NOT NULL,
    `user_intro` varchar(1000) DEFAULT NULL,
    `user_gb` smallint(6) NOT NULL DEFAULT 1,
    `user_state` smallint(6) NOT NULL DEFAULT 1,
    `user_pic` varchar(100) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`user_seq`),
    UNIQUE KEY `user_email` (`user_email`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `email_token` (
                                             `token_email` varchar(50) NOT NULL,
    `token_data` varchar(100) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `end_dt` datetime NOT NULL,
    PRIMARY KEY (`token_email`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_master` (
                                              `party_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `party_name` varchar(100) NOT NULL,
    `party_genre` int(11) NOT NULL,
    `party_location` varchar(50) NOT NULL DEFAULT '',
    `party_min_age` year(4) NOT NULL,
    `party_max_age` year(4) NOT NULL,
    `party_maximum` int(11) NOT NULL DEFAULT 100,
    `party_gender` smallint(6) NOT NULL,
    `party_join_gb` smallint(6) NOT NULL,
    `party_intro` varchar(1000) NOT NULL,
    `party_join_form` varchar(2000) NOT NULL,
    `party_auth_gb` smallint(6) NOT NULL,
    `party_pic` varchar(100) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`party_seq`),
    UNIQUE KEY `party_name` (`party_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_join` (
                                            `join_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `join_party_seq` bigint(20) NOT NULL,
    `join_user_seq` bigint(20) NOT NULL,
    `join_msg` varchar(2000) NOT NULL,
    `join_gb` smallint(6) NOT NULL DEFAULT 1,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`join_seq`),
    UNIQUE KEY `join_party_seq` (`join_party_seq`,`join_user_seq`),
    KEY `join_user_seq` (`join_user_seq`),
    CONSTRAINT `party_join_ibfk_1` FOREIGN KEY (`join_party_seq`) REFERENCES `party_master` (`party_seq`),
    CONSTRAINT `party_join_ibfk_2` FOREIGN KEY (`join_user_seq`) REFERENCES `user_master` (`user_seq`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_member` (
                                              `member_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `member_user_seq` bigint(20) NOT NULL,
    `member_party_seq` bigint(20) NOT NULL,
    `member_role` varchar(10) DEFAULT NULL,
    `member_gb` smallint(6) NOT NULL DEFAULT 1,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`member_seq`),
    UNIQUE KEY `member_user_seq` (`member_user_seq`,`member_party_seq`),
    KEY `member_party_seq` (`member_party_seq`),
    CONSTRAINT `party_member_ibfk_1` FOREIGN KEY (`member_user_seq`) REFERENCES `user_master` (`user_seq`),
    CONSTRAINT `party_member_ibfk_2` FOREIGN KEY (`member_party_seq`) REFERENCES `party_master` (`party_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `plan_master` (
                                             `plan_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `plan_party_seq` bigint(20) NOT NULL,
    `plan_start_dt` date NOT NULL,
    `plan_start_time` time NOT NULL,
    `plan_completed` smallint(6) NOT NULL DEFAULT 1,
    `plan_title` varchar(50) NOT NULL,
    `plan_contents` varchar(2000) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`plan_seq`),
    KEY `plan_party_seq` (`plan_party_seq`),
    CONSTRAINT `plan_master_ibfk_1` FOREIGN KEY (`plan_party_seq`) REFERENCES `party_master` (`party_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1024 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `plan_member` (
                                             `plmember_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `plmember_plan_seq` bigint(20) NOT NULL,
    `plmember_member_seq` bigint(20) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`plmember_seq`),
    UNIQUE KEY `plmember_plan_seq` (`plmember_plan_seq`,`plmember_member_seq`),
    KEY `plmember_member_seq` (`plmember_member_seq`),
    CONSTRAINT `plan_member_ibfk_1` FOREIGN KEY (`plmember_plan_seq`) REFERENCES `plan_master` (`plan_seq`),
    CONSTRAINT `plan_member_ibfk_2` FOREIGN KEY (`plmember_member_seq`) REFERENCES `party_member` (`member_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_budget` (
                                              `budget_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `budget_party_seq` bigint(20) NOT NULL,
    `budget_member_seq` bigint(20) NOT NULL,
    `budget_gb` smallint(6) NOT NULL,
    `budget_amount` bigint(20) NOT NULL,
    `budget_dt` date NOT NULL,
    `budget_text` varchar(50) DEFAULT NULL,
    `budget_pic` varchar(100) DEFAULT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`budget_seq`),
    KEY `budget_party_seq` (`budget_party_seq`),
    KEY `budget_member_seq` (`budget_member_seq`),
    CONSTRAINT `party_budget_ibfk_1` FOREIGN KEY (`budget_party_seq`) REFERENCES `party_master` (`party_seq`),
    CONSTRAINT `party_budget_ibfk_2` FOREIGN KEY (`budget_member_seq`) REFERENCES `party_member` (`member_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1002 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_board_master` (
                                                    `board_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `board_party_seq` bigint(20) NOT NULL,
    `board_member_seq` bigint(20) NOT NULL,
    `board_title` varchar(50) NOT NULL,
    `board_contents` varchar(2000) NOT NULL,
    `board_hit` bigint(20) NOT NULL DEFAULT 0,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`board_seq`),
    KEY `board_party_seq` (`board_party_seq`),
    KEY `board_member_seq` (`board_member_seq`),
    CONSTRAINT `party_board_master_ibfk_1` FOREIGN KEY (`board_party_seq`) REFERENCES `party_master` (`party_seq`),
    CONSTRAINT `party_board_master_ibfk_2` FOREIGN KEY (`board_member_seq`) REFERENCES `party_member` (`member_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_board_pic` (
                                                 `board_pic_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `board_pic_board_seq` bigint(20) NOT NULL,
    `board_pic_file` varchar(100) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`board_pic_seq`),
    KEY `board_pic_board_seq` (`board_pic_board_seq`),
    CONSTRAINT `party_board_pic_ibfk_1` FOREIGN KEY (`board_pic_board_seq`) REFERENCES `party_board_master` (`board_seq`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_board_comment` (
                                                     `comment_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `comment_board_seq` bigint(20) NOT NULL,
    `comment_member_seq` bigint(20) NOT NULL,
    `comment_contents` varchar(200) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`comment_seq`),
    KEY `comment_board_seq` (`comment_board_seq`),
    KEY `comment_member_seq` (`comment_member_seq`),
    CONSTRAINT `party_board_comment_ibfk_1` FOREIGN KEY (`comment_board_seq`) REFERENCES `party_board_master` (`board_seq`),
    CONSTRAINT `party_board_comment_ibfk_2` FOREIGN KEY (`comment_member_seq`) REFERENCES `party_member` (`member_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_ranking` (
                                               `ranking_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `ranking_party_seq` bigint(20) NOT NULL,
    `ranking_name` varchar(10) NOT NULL,
    `ranking_grade` varchar(3) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`ranking_seq`),
    KEY `ranking_party_seq` (`ranking_party_seq`),
    CONSTRAINT `party_ranking_ibfk_1` FOREIGN KEY (`ranking_party_seq`) REFERENCES `party_master` (`party_seq`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `review_master` (
                                               `review_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `review_plan_seq` bigint(20) NOT NULL,
    `review_plmember_seq` bigint(20) NOT NULL,
    `review_title` varchar(50) NOT NULL,
    `review_contents` varchar(2000) NOT NULL,
    `review_rating` decimal(2,1) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`review_seq`),
    UNIQUE KEY `review_plan_seq` (`review_plan_seq`,`review_plmember_seq`),
    KEY `review_plmember_seq` (`review_plmember_seq`),
    CONSTRAINT `review_master_ibfk_1` FOREIGN KEY (`review_plan_seq`) REFERENCES `plan_master` (`plan_seq`),
    CONSTRAINT `review_master_ibfk_2` FOREIGN KEY (`review_plmember_seq`) REFERENCES `plan_member` (`plmember_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=521 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `review_pic` (
                                            `review_pic_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `review_pic_review_seq` bigint(20) NOT NULL,
    `review_pic_file` varchar(100) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`review_pic_seq`),
    KEY `review_pic_review_seq` (`review_pic_review_seq`),
    CONSTRAINT `review_pic_ibfk_1` FOREIGN KEY (`review_pic_review_seq`) REFERENCES `review_master` (`review_seq`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `review_fav` (
                                            `review_fav_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `review_fav_review_seq` bigint(20) NOT NULL,
    `review_fav_user_seq` bigint(20) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`review_fav_seq`),
    UNIQUE KEY `review_fav_review_seq` (`review_fav_review_seq`,`review_fav_user_seq`),
    KEY `review_fav_user_seq` (`review_fav_user_seq`),
    CONSTRAINT `review_fav_ibfk_1` FOREIGN KEY (`review_fav_review_seq`) REFERENCES `review_master` (`review_seq`),
    CONSTRAINT `review_fav_ibfk_2` FOREIGN KEY (`review_fav_user_seq`) REFERENCES `user_master` (`user_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1005 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `party_wish` (
                                            `wish_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `wish_user_seq` bigint(20) NOT NULL,
    `wish_party_seq` bigint(20) NOT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`wish_seq`),
    UNIQUE KEY `wish_user_seq` (`wish_user_seq`,`wish_party_seq`),
    KEY `wish_party_seq` (`wish_party_seq`),
    CONSTRAINT `party_wish_ibfk_1` FOREIGN KEY (`wish_user_seq`) REFERENCES `user_master` (`user_seq`),
    CONSTRAINT `party_wish_ibfk_2` FOREIGN KEY (`wish_party_seq`) REFERENCES `party_master` (`party_seq`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS `common_cd` (
                                           `cd_seq` bigint(20) NOT NULL AUTO_INCREMENT,
    `cd_main` varchar(2) NOT NULL,
    `cd_sub` varchar(2) NOT NULL,
    `cd` varchar(5) NOT NULL,
    `cd_gb` varchar(2) NOT NULL,
    `cd_gb_nm` varchar(10) DEFAULT NULL,
    `input_dt` datetime NOT NULL DEFAULT current_timestamp(),
    `update_dt` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`cd_seq`)
    ) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
