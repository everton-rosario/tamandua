ALTER TABLE `lyric` ADD `discard_flag` CHAR(1) DEFAULT 'N' NULL COMMENT 'Indica se a letra deve ou não ser descartada' AFTER `original_flag` ;

