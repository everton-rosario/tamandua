		$(function() {
			$.getFeed({
				// RSS path must be local unless using a proxy
				url: '/ultnot/index.xml',
				success: function(feed) {
					
					var html = '';
					
					// Selects how many RSS items to parse
					for(var i = 0; i < feed.items.length && i <= 15; i++) {
					
						var item = feed.items[i];
						html += '<li>'
						+ '<div class="horarioNoticia"><strong>' + item.updated.replace(/(.+(\d\d):(\d\d):(\d\d).+)/,'$2h$3') + '&nbsp;</strong></div>'
			
						// Gets the RSS link and title
						html += '<div class="noticia"><span><a target="_blank" href="' + item.link + '">' + item.title + '</a></span></div>'
						+ '</li>';
					}
					$('.latestnews').append(html);
				}    
			});
		});