update artist set flag_public = 'N' where id_artist in (156328, 322522);
commit;

update lyric set original_flag = 'N' where lyric_type = 'TABLATURE';
commit;

update music set flag_publication = 'N' where title like '%cifra%';
commit;
