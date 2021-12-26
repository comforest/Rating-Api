DROP TABLE IF EXISTS group_user;
DROP TABLE IF EXISTS game_player;
DROP TABLE IF EXISTS game_record;
DROP TABLE IF EXISTS game_expansion;

DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS auth;
DROP TABLE IF EXISTS users;


CREATE TABLE `users`
(
    `user_id`           INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `nickname`          VARCHAR(20) NULL DEFAULT NULL,
    `resource`          VARCHAR(10) NULL DEFAULT NULL,
    `third_party_token` VARCHAR(100) NULL DEFAULT NULL,
    `create_time`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `auth`
(
    `user_id`       INT(10) UNSIGNED NOT NULL,
    `refresh_token` VARCHAR(100) NOT NULL,
    `expire_date`   DATETIME     ,
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`, `refresh_token`),
    CONSTRAINT `FK_auth_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE `games`
(
    `game_id`     INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(20) NOT NULL,
    `min_num`     TINYINT(3) NOT NULL DEFAULT '2',
    `max_num`     TINYINT(3) NOT NULL DEFAULT '4',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`game_id`)
);

CREATE TABLE `groups`
(
    `group_id`    INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(30) NULL DEFAULT NULL,
    `affiliation` VARCHAR(30) NULL DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`group_id`)
);

CREATE TABLE `game_expansion`
(
    `game_expansion_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `game_id`           INT(10) UNSIGNED NOT NULL,
    `name`              VARCHAR(20) NOT NULL,
    `create_time`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`game_expansion_id`),
    INDEX               `FK_game_expansion_games` (`game_id`),
    CONSTRAINT `FK_game_expansion_games` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
;

CREATE TABLE `game_player`
(
    `record_id` INT(10) UNSIGNED NOT NULL,
    `user_id`   INT(10) UNSIGNED NOT NULL,
    `score`     INT(10) NOT NULL,
    PRIMARY KEY (`record_id`, `user_id`)
)
;

CREATE TABLE `game_record`
(
    `record_id`   INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `group_id`    INT(10) UNSIGNED NOT NULL,
    `game_id`     INT(10) UNSIGNED NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`record_id`),
    INDEX         `FK_game_record_games` (`game_id`),
    INDEX         `FK_game_record_groups` (`group_id`),
    CONSTRAINT `FK_game_record_games` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT `FK_game_record_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`) ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE TABLE `group_user`
(
    `group_id`    INT(10) UNSIGNED NOT NULL,
    `user_id`     INT(10) UNSIGNED NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX         `FK_user_group_groups` (`group_id`),
    INDEX         `FK_user_group_users` (`user_id`),
    CONSTRAINT `FK_user_group_groups` FOREIGN KEY (`group_id`) REFERENCES `groups` (`group_id`) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT `FK_user_group_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE ON DELETE RESTRICT
);
