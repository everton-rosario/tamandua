update lyric set original_flag = 'N' where id_lyric < 500000;
update lyric set original_flag = 'N' where id_lyric between 500000 and 1000000;
update lyric set original_flag = 'N' where id_lyric between 1000000 and 1500000;
update lyric set original_flag = 'N' where id_lyric between 1500000 and 2000000;
update lyric set original_flag = 'N' where id_lyric between 2000000 and 2500000;
update lyric set original_flag = 'N' where id_lyric between 2500000 and 3000000;
update lyric set original_flag = 'N' where id_lyric between 3000000 and 3500000;
update lyric set original_flag = 'N' where id_lyric between 3500000 and 4000000;
update lyric set original_flag = 'N' where id_lyric between 4500000 and 5000000;
commit;

update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric < 500000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 500000 and 1000000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 1000000 and 1500000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 1500000 and 2000000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 2000000 and 2500000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 2500000 and 3000000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 3000000 and 3500000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 3500000 and 4000000;
update lyric set original_flag = 'S' where text is not null and trim(text) <> '' and id_lyric between 4500000 and 5000000;
commit;


update music set flag_publication = 'S', flag_moderate = 'N';
update music set flag_publication = 'N' where title is null or trim(title) = '';
commit;

update music set flag_publication = 'N' where title like '%cifrada%';
commit;


