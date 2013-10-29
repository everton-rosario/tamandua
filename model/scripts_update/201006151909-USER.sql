ALTER TABLE `tamandua_db`.`user` ADD COLUMN `name` VARCHAR(255) AFTER `country`,
 ADD COLUMN `rg` VARCHAR(50) AFTER `name`,
 ADD COLUMN `status` VARCHAR(1) AFTER `rg`;
 
 ALTER TABLE `tamandua_db`.`user` MODIFY COLUMN `password` BLOB DEFAULT NULL COMMENT '\'senha encriptada do usuario',
 ADD COLUMN `activation_code` BLOB AFTER `status`;