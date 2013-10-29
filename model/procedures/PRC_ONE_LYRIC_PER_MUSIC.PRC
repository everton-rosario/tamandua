DROP PROCEDURE IF EXISTS `PRC_ONE_LYRIC_PER_MUSICE` ;
CREATE PROCEDURE `PRC_ONE_LYRIC_PER_MUSIC`() 
BEGIN
   DECLARE done INT DEFAULT 0;
   DECLARE music_id BIGINT(20);
   DECLARE num_lyrics BIGINT(20);   
   DECLARE updated_rows INT(20);
	DECLARE music_cursor CURSOR FOR
	    select id_music, count(id_music) from lyric where discard_flag = 'N' and original_flag = 'S' group by id_music having count(id_music) > 1;
   DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

   OPEN music_cursor; 
	
   REPEAT   
       FETCH music_cursor INTO music_id, num_lyrics;
       if num_lyrics = 3 then
           update lyric set discard_flag = 'S', original_flag = 'N' where id_music = music_id and provider in ('vagalume', 'letras');           
       end if;
       if num_lyrics = 2 then
           update lyric set discard_flag = 'S', original_flag = 'N' where id_music = music_id and provider in ('vagalume');
           set updated_rows = ROW_COUNT();           
           if updated_rows = 0 then                      
                update lyric set discard_flag = 'S', original_flag = 'N' where id_music = music_id and provider in ('terra');
           end if;           
       end if;
		 if num_lyrics > 3 then
           update lyric set discard_flag = 'S', original_flag = 'N' where id_music = music_id;
       end if;

   UNTIL done END REPEAT;

   CLOSE music_cursor;
END

CALL PRC_ONE_LYRIC_PER_MUSIC();
commit;