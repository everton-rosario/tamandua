ALTER TABLE `lyric` ADD `discard_flag` CHAR(1) DEFAULT 'N' NULL COMMENT 'Indica se a letra deve ou n�o ser descartada' AFTER `original_flag` ;

