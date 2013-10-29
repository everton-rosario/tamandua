ALTER TABLE music ADD idt_track_radio BIGINT(20) NULL COMMENT 'Coluna com o id da musica na rádio uol.' ;

update music set idt_track_radio = null where idt_track_radio = 201458;
commit;
