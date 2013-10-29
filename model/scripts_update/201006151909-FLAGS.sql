ALTER TABLE `tamandua_db`.`lyric` ADD COLUMN `original_flag` VARCHAR(1);

ALTER TABLE `tamandua_db`.`artist` ADD COLUMN `flag_moderate` VARCHAR(1) NOT NULL DEFAULT 'N';
update artist set flag_moderate='N';

ALTER TABLE `tamandua_db`.`artist` ADD COLUMN `flag_public` VARCHAR(1) NOT NULL DEFAULT 'S';
update artist set flag_public='S';