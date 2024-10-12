-- 2024/10/12
ALTER TABLE `web`.`t_meme_info`
    ADD COLUMN `meme_type` varchar(10) DEFAULT NULL COMMENT '类型（具体看memeTypeEnum）';
ALTER TABLE `web`.`t_meme_info`
    DROP `meme_type`;
ALTER TABLE `web`.`t_meme_info`
    ADD COLUMN `meme_type` varchar(10) DEFAULT '1' COMMENT '类型（具体看memeTypeEnum）';
