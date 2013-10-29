DROP FUNCTION IF EXISTS FNC_GET_TAGS ;
delimiter $$
CREATE FUNCTION FNC_GET_TAGS(p_id_entity BIGINT(20), p_id_entity_descriptor BIGINT(20), p_separator VARCHAR(10))
  RETURNS VARCHAR(200)
BEGIN
  DECLARE l_tags VARCHAR(200);
  DECLARE l_separator VARCHAR(10);
  DECLARE l_tag VARCHAR(50);
  DECLARE done INT DEFAULT 0;
  DECLARE tag_cursor CURSOR FOR
     select distinct t.name from tag t where t.id_entity = p_id_entity and t.id_entity_descriptor = p_id_entity_descriptor and t.type = 'STYLE';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  IF ISNULL(p_id_entity) or ISNULL(p_id_entity_descriptor) THEN
     RETURN null;
  END IF;
  
  IF ISNULL(p_separator) THEN
	 SET l_separator = ',';
  ELSE
     SET l_separator = p_separator;
  END IF;

   OPEN tag_cursor; 
   SET l_tags = null;
   REPEAT
       FETCH tag_cursor INTO l_tag;
		   IF ISNULL(l_tags) THEN
			   SET l_tags = l_tag;
		   ELSE IF not ISNULL(l_tag) THEN
			   SET l_tags = concat(l_tags, l_separator, l_tag);
		   END IF;
	   END IF;
	   SET l_tag = null;
	   
   UNTIL done END REPEAT;
	  
   CLOSE tag_cursor;

  RETURN(l_tags);
END$$
delimiter ;
