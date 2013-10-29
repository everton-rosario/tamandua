DROP PROCEDURE IF EXISTS `PRC_FILL_ARTIST_IMAGE` ;
CREATE PROCEDURE `PRC_FILL_ARTIST_IMAGE`() 
BEGIN
   DECLARE done INT DEFAULT 0;   
   DECLARE id_imagem BIGINT(20);
   DECLARE id_artista BIGINT(20);
   DECLARE uri_imagem VARCHAR(100);
	DECLARE image_cursor CURSOR FOR 
	    select id_image, concat(substring_index(replace(uri, 'http://static4.vagalume.uol.com.br', ''), '/', 2), '/') as uri from image where id_image > 1;
   DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
      
   OPEN image_cursor; 
	
   REPEAT   
       FETCH image_cursor INTO id_imagem, uri_imagem;              
       SET id_artista = -1;
       select a.id_artist into id_artista from artist a where a.uri = uri_imagem;       
       if id_artista <> -1 then
           insert into artist_image (id_image, id_artist) values (id_imagem, id_artista);           
       else        
           SET done = 0;
       end if;
   UNTIL done END REPEAT;
	  
   CLOSE image_cursor;
END;

CALL PRC_FILL_ARTIST_IMAGE();
commit;