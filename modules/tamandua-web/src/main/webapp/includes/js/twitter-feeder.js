jQuery.ajax({
  url: '/pages/twitter.js',
  dataType: 'json',
  success: function(items) { alert('oi');}
});

jQuery.ajax({
  url: '/pages/twitter.js',
  dataType: 'json',
  success: function(items) { 
alert('items.length=' + items.length)
/*jQuery.each(items, function(key, val) {
    alert('key="' + key + '", val="' + val.createdAt + '"');
  });*/
}
});