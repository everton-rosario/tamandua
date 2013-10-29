update artist set sort_name = replace(uri, '/', '') where uri is not null;

commit;
