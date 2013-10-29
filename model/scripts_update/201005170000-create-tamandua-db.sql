SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

SHOW WARNINGS;
SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `image`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `image` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `image` (
  `id_image` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `uri` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'Path relativo ao domini' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL permanent da image' ,
  `description` VARCHAR(4000) NULL DEFAULT NULL COMMENT '\'Descrição da image' ,
  `file` VARCHAR(1000) NULL DEFAULT NULL COMMENT '\'Local onde o arquivo encontra-se no disc' ,
  `location` VARCHAR(1000) NULL DEFAULT NULL ,
  `width` INT(10) UNSIGNED NULL DEFAULT NULL COMMENT '\'Largura da image' ,
  `height` INT(10) UNSIGNED NULL DEFAULT NULL COMMENT '\'Altura da image' ,
  PRIMARY KEY (`id_image`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '4 - Imagens do sistema.';

SHOW WARNINGS;
CREATE UNIQUE INDEX `uri_UNIQUE` ON `image` (`uri` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `artist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `artist` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `artist` (
  `id_artist` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '\'Identificador do Artist' ,
  `name` VARCHAR(100) NULL DEFAULT NULL COMMENT '\'Nome formal do Artista. Ex: Antonio Carlos Jobi' ,
  `dt_birth` DATE NULL DEFAULT NULL COMMENT '\'Data nascimento do artista/band' ,
  `dt_end` DATE NULL DEFAULT NULL COMMENT '\'Data de termino/morte da banda/artist' ,
  `type` INT NULL DEFAULT NULL COMMENT '\'Descritivo do tipo de classificação do artist' ,
  `total_access` BIGINT(20) NULL DEFAULT NULL COMMENT '\'Historical total acces' ,
  `id_country` BIGINT(20) NULL DEFAULT NULL COMMENT '\'FK para a tabela de paise' ,
  `sort_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '\'Nome utilziado para organização/ordenação do artist' ,
  `letters` VARCHAR(36) NULL DEFAULT NULL COMMENT '\'Colocar todas as letras, sem separação a que um artista pode pertencer. EX: The Beatles (\'\'bt' ,
  `uri` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'URI do artista. URI é o path relativo, independente do dominio' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL completa do permalink do artist' ,
  `id_image` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para a imagem principal do artist' ,
  `id_image_tiny` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para uma imagem bem pequena do artist' ,
  `id_image_huge` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para uma imagem bem grande do artist' ,
  PRIMARY KEY (`id_artist`) ,
  CONSTRAINT `fk_artist_image1`
    FOREIGN KEY (`id_image` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_artist_image2`
    FOREIGN KEY (`id_image_tiny` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_artist_image3`
    FOREIGN KEY (`id_image_huge` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '2 - Artista que pode ser uma banda, pessoa ou participante.';

SHOW WARNINGS;
CREATE INDEX `fk_artist_image1` ON `artist` (`id_image` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_artist_image2` ON `artist` (`id_image_tiny` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_artist_image3` ON `artist` (`id_image_huge` ASC) ;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uri_UNIQUE` ON `artist` (`uri` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `album`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `album` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `album` (
  `id_album` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '\'Id do albu' ,
  `id_artist` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'FK para Artista, representando o artista do albu' ,
  `name` VARCHAR(300) NULL DEFAULT NULL COMMENT '\'Nome do albu' ,
  `year` INT(10) UNSIGNED NULL DEFAULT NULL COMMENT '\'Ano de lançamento do albu' ,
  `id_country` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'FK para o pais de origem do albu' ,
  `uri` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'URI relativo ao dominio para o albu' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL para link permanente do albu' ,
  `id_cover` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para a imagem de capa do albu' ,
  `id_back_cover` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para imagem de fundo do albu' ,
  PRIMARY KEY (`id_album`) ,
  CONSTRAINT `fk_album_image1`
    FOREIGN KEY (`id_cover` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_album_image2`
    FOREIGN KEY (`id_back_cover` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_album_artist1`
    FOREIGN KEY (`id_artist` )
    REFERENCES `artist` (`id_artist` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '1 - Album de faixas de um artista.';

SHOW WARNINGS;
CREATE INDEX `fk_album_image1` ON `album` (`id_cover` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_album_image2` ON `album` (`id_back_cover` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_album_artist1` ON `album` (`id_artist` ASC) ;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uri_UNIQUE` ON `album` (`uri` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `artist_discography`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `artist_discography` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `artist_discography` (
  `id_album` BIGINT(20) UNSIGNED NOT NULL COMMENT 'FK para album' ,
  `id_artist` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para artista' ,
  CONSTRAINT `fk_artist_discography_album1`
    FOREIGN KEY (`id_album` )
    REFERENCES `album` (`id_album` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_artist_discography_artist1`
    FOREIGN KEY (`id_artist` )
    REFERENCES `artist` (`id_artist` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '10 - Relacionamento entre album e artista.';

SHOW WARNINGS;
CREATE INDEX `fk_artist_discography_album1` ON `artist_discography` (`id_album` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_artist_discography_artist1` ON `artist_discography` (`id_artist` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `entity_descriptor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `entity_descriptor` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `entity_descriptor` (
  `id_entity_descriptor` BIGINT(20) UNSIGNED NOT NULL ,
  `entity_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'Nome da entidade no sistem' ,
  `table_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'Nome da tabela da entidade no sistem' ,
  PRIMARY KEY (`id_entity_descriptor`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '12 - Cada entidade do sistema. Tabela de CONFIGURACAO.';

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `music`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `music` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `music` (
  `id_music` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'Titulo da music' ,
  `dt_release` DATE NULL DEFAULT NULL COMMENT '\'Data de lançamento da music' ,
  `id_country` BIGINT(20) NULL DEFAULT NULL COMMENT '\'FK para a tabela de paises ISO 316' ,
  `uri` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'URI relativo da musica, independente do domini' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL do permalink completo da musica com o domini' ,
  `id_artist` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para o artista dono da music' ,
  PRIMARY KEY (`id_music`) ,
  CONSTRAINT `fk_music_artist`
    FOREIGN KEY (`id_artist` )
    REFERENCES `artist` (`id_artist` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '3 - Entidade que representa uma Musica.';

SHOW WARNINGS;
CREATE INDEX `fk_music_artist` ON `music` (`id_artist` ASC) ;

SHOW WARNINGS;
CREATE UNIQUE INDEX `uri_UNIQUE` ON `music` (`uri` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `lyric`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `lyric` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `lyric` (
  `id_music` BIGINT(20) NOT NULL COMMENT '\'PK-FK para a musica' ,
  `laguage` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'Lingua da letra da music' ,
  `text` LONGTEXT NULL DEFAULT NULL COMMENT '\'A letra da musica' ,
  `total_access` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'Total histórico de acessos a esta letr' ,
  `uri` VARCHAR(1000) NULL DEFAULT NULL COMMENT '\'URI da letra. URI é o path relativo, independente do dominio' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL do link permanente para a letra da music' ,
  `lyric_title` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'Nome da musica traduzid' ,
  `lyric_type` ENUM('ORIGINAL','TRADUCTION','ADAPTATION','TABLATURE') NULL DEFAULT 'ORIGINAL' COMMENT '\'Tipo de letra, conteudo da music' ,
  PRIMARY KEY (`id_music`) ,
  CONSTRAINT `fk_lyric_music1`
    FOREIGN KEY (`id_music` )
    REFERENCES `music` (`id_music` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '6 - Letra da Musica.';

SHOW WARNINGS;
CREATE INDEX `fk_lyric_music1` ON `lyric` (`id_music` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `user` (
  `id_user` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'email do usuari' ,
  `id_image` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'Imagem/avatar do usuári' ,
  `password` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'senha encriptada do usuari' ,
  `role` ENUM('INTERNET','EDITOR','POWER_EDITOR','ROOT','TEACHER','TRNASLATOR') NULL DEFAULT NULL COMMENT '\'possiveis valores: \'\'INTERNET\'\',\'\'EDITOR\'\',\'\'POWER_EDITOR\'\',\'\'ROOT\'\',\'\'TEACHER\'\',\'' ,
  `cpf` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'CPF sem formatação, pontos ou numero' ,
  `street` VARCHAR(1000) NULL DEFAULT NULL COMMENT '\'Rua' ,
  `city` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'Cidade' ,
  `street_number` INT UNSIGNED NULL DEFAULT NULL COMMENT '\'Numero' ,
  `street_complement` VARCHAR(300) NULL DEFAULT NULL COMMENT 'Complemento' ,
  `state` VARCHAR(100) NULL DEFAULT NULL COMMENT '\'Estado/Provincia' ,
  `country` VARCHAR(100) NULL DEFAULT NULL COMMENT '\'Pais' ,
  PRIMARY KEY (`id_user`) ,
  CONSTRAINT `fk_user_image`
    FOREIGN KEY (`id_image` )
    REFERENCES `image` (`id_image` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '5 - Tabela dos usuarios do sistema.';

SHOW WARNINGS;
CREATE INDEX `fk_user_image` ON `user` (`id_image` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `multimedia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `multimedia` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `multimedia` (
  `id_multimedia` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '\'Identificador da midi' ,
  `id_entity` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'FK para a entidade, podendo ser artist, music, albu' ,
  `id_entity_descriptor` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'FK para a tabela entity_descriptor que é o dominio das entidades possivei' ,
  `id_user` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'Usuario que submeteu o video/audi' ,
  `media_type` ENUM('ORIGINAL_AUDIO','VIDEO_CLIP','AUDIO_CLASS','VIDEO_CLASS','PROFESSOR_EXPLANATION','USER_AUDIO','USER_VIDEO') NULL DEFAULT NULL COMMENT '\'Valores possiveis: \'\'ORIGINAL_AUDIO\'\',\'\'VIDEO_CLIP\'\',\'\'AUDIO_CLASS\'\',\'\'VIDEO_CLASS\'\',\'\'PROFESSOR_EXPLANATION\'\',\'\'USER_AUDIO\'\'' ,
  `title` VARCHAR(200) NULL DEFAULT NULL COMMENT '\'Titulo do arquiv' ,
  `description` VARCHAR(1000) NULL DEFAULT NULL COMMENT '\'Descrição da medi' ,
  `contact_information` VARCHAR(1000) NULL DEFAULT NULL COMMENT '\'Meios de contato para aula' ,
  `embed_code` VARCHAR(3000) NULL DEFAULT NULL COMMENT '\'Codigo para embedar video do youtube, musica da radio etc' ,
  `uri` VARCHAR(500) NULL DEFAULT NULL COMMENT '\'Path relativo independende do domini' ,
  `url` VARCHAR(1500) NULL DEFAULT NULL COMMENT '\'URL do link permanente para  a medi' ,
  PRIMARY KEY (`id_multimedia`) ,
  CONSTRAINT `fk_multimedia_entity_descriptor1`
    FOREIGN KEY (`id_entity_descriptor` )
    REFERENCES `entity_descriptor` (`id_entity_descriptor` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_multimedia_user1`
    FOREIGN KEY (`id_user` )
    REFERENCES `user` (`id_user` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '7 - Entradas de midia para cada entidade.';

SHOW WARNINGS;
CREATE INDEX `fk_multimedia_entity_descriptor1` ON `multimedia` (`id_entity_descriptor` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_multimedia_user1` ON `multimedia` (`id_user` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `music_artist`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `music_artist` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `music_artist` (
  `id_music` BIGINT(20) NOT NULL COMMENT '\'FK para a music' ,
  `id_artist` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para o artist' ,
  `participation` ENUM('COMPOSER','INTERPRETER') NULL DEFAULT NULL COMMENT '\'Tipo de participacao na music' ,
  CONSTRAINT `fk_music_artist_music1`
    FOREIGN KEY (`id_music` )
    REFERENCES `music` (`id_music` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_music_artist_artist1`
    FOREIGN KEY (`id_artist` )
    REFERENCES `artist` (`id_artist` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '11 - Participacoes existentes nas musica.';

SHOW WARNINGS;
CREATE INDEX `fk_music_artist_music1` ON `music_artist` (`id_music` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_music_artist_artist1` ON `music_artist` (`id_artist` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `annotation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `annotation` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `annotation` (
  `id_annotation` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '\'Identificador da anotaca' ,
  `id_entity` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK fraca para a entidade a que se referencia esta anotaca' ,
  `id_entity_descriptor` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para a entity_descriptor, para designar a que entidade foi dada a anotaca' ,
  `text` VARCHAR(1000) NOT NULL COMMENT '\'Texto da anotaçã' ,
  `music_meter` INT(10) UNSIGNED NULL DEFAULT NULL COMMENT '\'avaliacao da anotaca' ,
  `type` ENUM('TEXT','LINK','IMAGE','VIDEO','OPINION','WIKIPEDIA') NULL DEFAULT NULL COMMENT '\'Universo de pertinencia da anotaçã' ,
  `id_user` BIGINT(20) UNSIGNED NOT NULL COMMENT 'FK para usuario que fez a anotaçao' ,
  PRIMARY KEY (`id_annotation`) ,
  CONSTRAINT `fk_annotation_entity_descriptor1`
    FOREIGN KEY (`id_entity_descriptor` )
    REFERENCES `entity_descriptor` (`id_entity_descriptor` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_annotation_user1`
    FOREIGN KEY (`id_user` )
    REFERENCES `user` (`id_user` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '9 - Tabela de anotações sobre uma determinada entidade.';

SHOW WARNINGS;
CREATE INDEX `fk_annotation_entity_descriptor1` ON `annotation` (`id_entity_descriptor` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_annotation_user1` ON `annotation` (`id_user` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tag` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `tag` (
  `id_tag` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '\'Identificador da ta' ,
  `description` VARCHAR(400) NULL DEFAULT NULL COMMENT '\'Descrição da ta' ,
  `owner` ENUM('USER','EDITOR') NULL DEFAULT NULL COMMENT '\'Quem criou a tag, quem pode utilizar a ta' ,
  `name` VARCHAR(50) NULL DEFAULT NULL COMMENT '\'Valor utilizado e sensivelmente mostrado no sistem' ,
  `id_entity` BIGINT(20) UNSIGNED NULL DEFAULT NULL COMMENT '\'Id da entidade taggeada' ,
  `type` ENUM('STYLE','PHOTO_ALBUM','LABEL','SENTIMENT','FREESTYLE') NULL DEFAULT NULL COMMENT '\'Tipo de tag, aplicado a entidade definida pelas chaves id_entity e id_entity_descripto' ,
  `id_entity_descriptor` BIGINT(20) UNSIGNED NOT NULL COMMENT '\'FK para a tabela entity_descriptor, discriminando qual o tipo da entidade taggeada' ,
  PRIMARY KEY (`id_tag`) ,
  CONSTRAINT `fk_tag_entity_descriptor1`
    FOREIGN KEY (`id_entity_descriptor` )
    REFERENCES `entity_descriptor` (`id_entity_descriptor` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '8 - Tag adicionada para caracterizar uma entidade.';

SHOW WARNINGS;
CREATE INDEX `fk_tag_entity_descriptor1` ON `tag` (`id_entity_descriptor` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `artist_alias`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `artist_alias` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `artist_alias` (
  `id_artist_alias` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Id do Alias' ,
  `id_artist` BIGINT(20) UNSIGNED NOT NULL COMMENT 'FK para o artista' ,
  `artist_alias` VARCHAR(100) NOT NULL COMMENT 'Alias do artista' ,
  PRIMARY KEY (`id_artist_alias`) ,
  CONSTRAINT `fk_artist_alias`
    FOREIGN KEY (`id_artist` )
    REFERENCES `artist` (`id_artist` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = 'Codinomes do artista. ';

SHOW WARNINGS;
CREATE INDEX `fk_artist_alias` ON `artist_alias` (`id_artist` ASC) ;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `moderation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `moderation` ;

SHOW WARNINGS;
CREATE  TABLE IF NOT EXISTS `moderation` (
  `id_moderation` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Identificador da acao de moderação' ,
  `id_user` BIGINT(20) UNSIGNED NULL COMMENT 'Identificador do usuário que realizou uma alteração.' ,
  `moderation_date` DATE NULL COMMENT 'Data em que ocorreu a moderação' ,
  `id_entity_descriptor` BIGINT(20) UNSIGNED NULL COMMENT 'FK para o tipo de entidade a que foi aplicada a moderação.' ,
  `id_entity` BIGINT(20) UNSIGNED NULL COMMENT 'Id da entidade em que foi aplicada a moderacao, de acordo com a id_entity_descriptor' ,
  `status` ENUM('NOK','OK','CREATED','MODIFIED') NULL DEFAULT 'CREATED' COMMENT 'Status da moderação: \'NOK\',\'OK\',\'CREATED\',\'MODIFIED\'' ,
  `old_value` LONGTEXT NULL COMMENT 'Antigo valor antes da moderação' ,
  `new_value` LONGTEXT NULL COMMENT 'Novo valor apos a moderacao' ,
  `affected_field_name` VARCHAR(100) NULL COMMENT 'Nome da coluna afetada, na entidade (id_entity), na tabela apontada pela id_entity_descriptor' ,
  PRIMARY KEY (`id_moderation`) ,
  CONSTRAINT `fk_mod_user`
    FOREIGN KEY (`id_user` )
    REFERENCES `user` (`id_user` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mod_edescriptor`
    FOREIGN KEY (`id_entity_descriptor` )
    REFERENCES `entity_descriptor` (`id_entity_descriptor` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_german1_ci
COMMENT = '10 - Tabela com as ações de moderação.';

SHOW WARNINGS;
CREATE INDEX `fk_mod_user` ON `moderation` (`id_user` ASC) ;

SHOW WARNINGS;
CREATE INDEX `fk_mod_edescriptor` ON `moderation` (`id_entity_descriptor` ASC) ;

SHOW WARNINGS;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `user`
-- -----------------------------------------------------
SET AUTOCOMMIT=0;
USE `tamandua_db`;
insert into `tamandua_db`.`user` (`id_user`, `email`, `id_image`, `password`, `role`, `cpf`, `street`, `city`, `street_number`, `street_complement`, `state`, `country`) values (1, 'erosario@gmail.com', NULL, NULL, 'ROOT', '31031147888', 'Carlos Peteado Stevenson', 'Valinhos', 700, 'casa 91', 'São Paulo', 'Brasil');

COMMIT;
