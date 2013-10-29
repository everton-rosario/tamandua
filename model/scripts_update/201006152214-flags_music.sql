ALTER TABLE `music` ADD `flag_moderate` VARCHAR( 1 ) NOT NULL DEFAULT 'N',
ADD `flag_publication` VARCHAR( 1 ) NOT NULL DEFAULT 'N';

UPDATE music SET flag_moderate='N', flag_publication='S';