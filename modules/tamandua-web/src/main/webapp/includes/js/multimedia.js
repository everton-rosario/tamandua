var videos = [];
var videoIndex = -1;
var windowImage;
var imageWidth = 290;
var imageHeight = 198;
var imageMinWidth = 80;
var imageMinHeight = 80;
var galleryWidth = 130;
var galleryHeight = 130;
var thumbnailWidth = 54;
var thumbnailHeight = 40;
var imageIndex = 0;
var thumbIsMoving = false;

function loadRadio(idtRadioTrack, artistName, artistNameNormalized){
	var radio = '';
    radio += '<div id="playerRadio" style="width:298px; background:none; overflow:hidden; line-height:10px;">';
    
    radio += '<!--[if !IE]> -->';
    radio += '<object type="application/x-shockwave-flash" name="mediaPlayer" wmode="transparent" id="mediaPlayer" data="http://www.radio.uol.com.br/widgets/swf/mediaPlayer.swf" width="300" height="160">';
    radio += '<!-- <![endif]-->';

    radio += '<!--[if IE]>';
    radio += '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"';
    radio += '	codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"';
    radio += '	wmode="transparent" data="http://www.radio.uol.com.br/widgets/swf/mediaPlayer.swf"';
    radio += '	name="mediaPLayer" id="mediaPlayer" width="298" height="160">';
    radio += '	<param name="movie" value="http://www.radio.uol.com.br/widgets/swf/mediaPlayer.swf" />';
    radio += '	<param name="wmode" value="transparent" />';
    radio += '	<!--><!--dgx-->';
    radio += '	<param name="scale" value="noscale" />';
    radio += '	<param name="salign" value="tl" />';
    radio += '	<param name="allowScriptAccess" value="always" />';
    radio += '	<param name="loop" value="true" />';
    radio += '	<param name="menu" value="true" />';
    radio += '	<param name="wmode" value="transparent" />';
    radio += '	<param name="flashvars" value="track='+idtRadioTrack+'&autoLoad=true&autoplay=true" />';
    radio += '</object>';
    radio += '<!-- <![endif]-->';

    radio += '	<p class="link-artista-radio" style="background:#000;margin:0;">';
    radio += '		<a id="link-radio-uol"';
    radio += '	 	 href="http://www.radio.uol.com.br/artista/'+artistNameNormalized+'"';
    radio += '	 	 onclick="window.open(this.href, \'popup_search_radio_uol\'); return false;"';
    radio += '	 	 style="display:block;background:url(http://radio.imguol.com/seta.gif) no-repeat 5px 10px;text-decoration:none;color:#FFF;font:normal 11px Arial, Verdana;padding:5px 3px 5px 12px;">';
    radio += '			<span id="link-text-radio-uol">Ouça mais '+artistName+' na R&aacute;dio UOL</span>';
    radio += '		</a>';
    radio += '	</p>';

    radio += '	<p class="buscar-musica-radio" style="background:#26292E;padding:6px 3px;margin:0 0 -4px;">';
    radio += '		<input type="text"';
    radio += '	 	 id="search-data-radio-uol"';
    radio += '	 	 value="Buscar mais m&uacute;sicas, artistas ou &aacute;lbuns"';
    radio += '	 	 style="float:left;width:268px;height:13px;padding:2px 0 0 2px;border:solid 1px #AABCC8;font:normal 9px Arial, Verdana, sans-serif;color:#A3A6AB;line-height:1.3em;"'; 
    radio += '	 	 onfocus="if (this.value == \'Buscar mais m&uacute;sicas, artistas ou &aacute;lbuns\') this.value=\'\'"'; 
    radio += '	 	 onblur="if (!this.value) this.value=\'Buscar mais m&uacute;sicas, artistas ou &aacute;lbuns\'">';
    radio += '		</input>';
    radio += '		<button type="submit"'; 
    radio += '	 	 value="buscar"';
    radio += '	 	 style="width:20px;height:16px;text-indent:-15000em;cursor:pointer;background: url(http://radio.imguol.com/buscar.png) no-repeat;border:none;"';
    radio += '	 	 id="search-radio-uol"'; 
    radio += '	 	 onclick="window.open(\'http://www.radio.uol.com.br/busca/\' + document.getElementById(\'search-data-radio-uol\').value, \'popup_search_radio_uol\')" >';
    radio += '		</button>';
    radio += '	</p>';

    radio += '</div>';

    $("#aba-radio").show();
    $("#radio").html(radio);
    showMultimedia();
}

function loadVideos(data){
	var entries = data.feed.entry;
	if (entries) {
		for(var i=0; i<entries.length; i++){
			try{
				var videoURL = entries[i].media$group.media$content[0].url;
				if(videoURL != null && videoURL != ""){
					videos.push(videoURL);
				}
			} catch(e) {
				
			}
		}
	}
	
	if(videos.length > 0){
		firstVideo();
		showMultimedia();
	}
}

function loadNoImage(){
	var imgNoGallery = $("<img></img>").attr("src", "http://tocaletra.s3.amazonaws.com/images/no-image.png");
	$("#panel-gallery").append(imgNoGallery);
}

function loadImages(artistImages){	
	var divEnlarge = $("<div></div>").attr("id", "enlarge");
	divEnlarge.click(function(){
		 openImageByIndex();
	});
	$("#panel-gallery").append(divEnlarge);
	
	for(var i=0; i<artistImages.length; i++){
		var imgGallery = $("<img></img>").attr("id", "img"+i)
										 .attr("src", artistImages[i])
										 .addClass("gallery");
		if(i == 0){
			imgGallery.load(function(){
				resizeImageGallery($(this), galleryWidth, galleryHeight);
				positionImageGallery($(this), galleryWidth);
				showImage(0);
			});
		} else {
			imgGallery.load(function(){
				 resizeImageGallery($(this), galleryWidth, galleryHeight);
				 positionImageGallery($(this), galleryWidth);
			 });
		}

		$("#panel-gallery").append(imgGallery);
	}
}

function loadThumbnailImages(artistImages){
	var thumbSelect = $("<div></div>").addClass("thumbselec");
	
	var thumbSize = thumbnailWidth + 2;
	var thumbdiv = $("<div></div>").addClass("thumbs");
	var thumbul = $("<ul></ul>").attr("id", "thumbnails")
								.css("margin-left", (thumbSize*(-1))+"px")
								.width((artistImages.length+3)*thumbSize);

	thumbul.append(getThumbnailImage("thumb"+(artistImages.length-1)+"-cloned", artistImages[artistImages.length-1]));	
	for(var i=0; i<artistImages.length; i++){
		var thumbli = getThumbnailImage("thumb"+i, artistImages[i]);
		thumbul.append(thumbli);
	}
	thumbul.append(getThumbnailImage("thumb0-cloned", artistImages[0]));
	thumbul.append(getThumbnailImage("thumb1-cloned", artistImages[1]));
	thumbdiv.append(thumbul);
	
	var prevSpan = $("<div></div>").addClass("thumbnav").addClass("prev").click(function(){previousImage(thumbSize, artistImages.length)});
	var nextSpan = $("<div></div>").addClass("thumbnav").addClass("next").click(function(){nextImage(thumbSize, artistImages.length)});

	var thumbnails = $("<div></div>").attr("id", "panel-thumbnail");
	thumbnails.append(thumbSelect);
	thumbnails.append(prevSpan);
	thumbnails.append(thumbdiv);
	thumbnails.append(nextSpan);
	
	$("#fotos-artista").append(thumbnails);
	$("#info-artistas").removeClass("info-no-thumb");
	$("#info-artistas").addClass("info-with-thumb");
}

function getThumbnailImage(id, artistImage){
	var thumbli = $("<li></li>").addClass("thumbs");
	var thumbimg = $("<img></img>").attr("id", id)
								   .attr("src", artistImage)
								   .css("opacity", "0.5");
	
	if(id == "thumb0"){
		thumbimg.load(function(){
			resizeImageGallery($(this), thumbnailWidth, thumbnailHeight);
			selectThumbnail(0);
		});
	} else {
		thumbimg.load(function(){
			resizeImageGallery($(this), thumbnailWidth, thumbnailHeight);
		});
	}
	
	thumbli.append(thumbimg);
	return thumbli;
}

function previousImage(thumbSize, artistImagesLength){
	if(!thumbIsMoving){
		thumbIsMoving = true;

		unselectThumbnail(imageIndex);
		hideImage(imageIndex);
		
		$("#thumbnails").animate({marginLeft: '+='+thumbSize}, function(){
			if($(this).css("margin-left") == "0px"){
				$(this).css("margin-left", artistImagesLength*thumbSize*-1+"px")
				imageIndex = artistImagesLength-1;
			} else {
				imageIndex--;
			}
			showImage(imageIndex);
			selectThumbnail(imageIndex);
			thumbIsMoving = false;
		});
	}
}

function nextImage(thumbSize, artistImagesLength){
	if(!thumbIsMoving){
		thumbIsMoving = true;

		unselectThumbnail(imageIndex);
		hideImage(imageIndex);
		  
		$("#thumbnails").animate({marginLeft: '-='+thumbSize}, function(){
			if($(this).css("margin-left") == (artistImagesLength+1)*thumbSize*-1+"px"){
				$(this).css("margin-left", (thumbSize*(-1))+"px")
				imageIndex = 0;
			} else {
				imageIndex++;
			}
			showImage(imageIndex);
			selectThumbnail(imageIndex);
			thumbIsMoving = false;
		});
	}
}

function unselectThumbnail(thumbIndex){
	var thumb = $("#thumb"+thumbIndex);
	thumb.css("opacity", "0.5");
}

function selectThumbnail(thumbIndex){
	var thumb = $("#thumb"+thumbIndex);
	thumb.css("opacity", "1");
}

function hideImage(imageIndex){
	var image = $("#img"+imageIndex);
	image.fadeOut(500);
}

function showImage(imageIndex){
	var image = $("#img"+imageIndex);
	image.fadeIn(500);
}

function resizeImageGallery(image, width, height){
	var imgWidth = image.width();
	var imgHeight = image.height();
	
	image.attr("realWidth", image.width());
	image.attr("realHeight", image.height());
	
	if(imgWidth < imgHeight){
		image.width(width);
		if(image.height() < height ){
			image.height(height);
		}	
	}else{
		image.height(height);
		if(image.width() < width ){
			image.width(width);
		}	
	}
}

function positionImageGallery(image, width){
	 var imageWidth = image.width();
	 if(imageWidth > width){
		 var marginLeft = -1*(imageWidth - width)/2;
		 image.css("margin-left", marginLeft+"px")
	 }
}

function firstVideo(){
	videoIndex = 0;
	loadVideo(false, true);
}

function nextVideo(){
	videoIndex++;
	var hasNext = true;
	if(videoIndex == videos.length-1){
		hasNext = false;
	}
	loadVideo(true, hasNext);
}

function previousVideo(){
	videoIndex--;
	var hasPrevious = true;
	if(videoIndex == 0){
		hasPrevious = false;
	}
	loadVideo(hasPrevious, true);
}

function loadVideo(hasPrevious, hasNext){
	$("#video").html("");	

	var videoLink = videos[videoIndex];
	var video = '';
	video += '<div id="playerVideo">';
	video += '  <img id="loadingVideo" src="http://tocaletra.s3.amazonaws.com/images/loadingBox.gif"/>';
	video += '	<object width="297" height="233">';
	video += '		<param name="movie" value="'+videoLink+'"></param>';
	video += '		<param name="allowFullScreen" value="true"></param>';
	video += '		<param name="allowscriptaccess" value="always"></param>';
	video += '		<param name="wmode" value="transparent" />';
	video += '		<embed src="'+videoLink+'" type="application/x-shockwave-flash" wmode="transparent"';
	video += '		 allowscriptaccess="always" allowfullscreen="true" width="297" height="233">';
	video += '		</embed>';
	video += '	</object>';
	video += '</div>';
	video += '	<span class="videoControl" id="previousControl" style="visibility:hidden">Move previous</span>';
	video += '	<label class="videonav" id="videoPag"></label>';
	video += '	<span class="videoControl" id="nextControl" style="visibility:hidden">Move next</span>';

	$("#aba-video").show();
	$("#video").html(video);
	
	if(hasPrevious){
		$("#previousControl").css("visibility", "visible");
		$("#previousControl").click(function(){
			previousVideo();
		});
	}
	if(hasNext){
		$("#nextControl").css("visibility", "visible");
		$("#nextControl").click(function(){
			nextVideo();
		});
	}	
	$("#videoPag").html((videoIndex+1)+"/"+videos.length);
}

function loadGaleria(artistImages){
	var galeria = '';
	galeria += '<div id="gallery">';
	galeria += '	<div id="slideshow">';
	galeria += '		<div id="slidesContainer">';
	for(var i=0; i<artistImages.length; i++){
		galeria += '		<div class="slide">';
		galeria += '			<img id="img'+i+'" style="display:none;cursor:pointer" onload="resizeImage($(this), imageWidth, imageHeight)" src="'+artistImages[i]+'" onclick="openImage($(this))"></img>';
		galeria += '		</div>';
	}
	galeria += '	  	</div>';
	galeria += '	</div>';
	galeria += '	<label class="imgnav" id="pagination"></label>';
    galeria += '</div>';

    $("#aba-galeria").show();
	$("#galeria").html(galeria);

	loadSlides();
	showMultimedia();
}

function loadSlides(){
	var currentPosition = 0;
	var slideWidth = 300;
	var slides = $('.slide');
	var numberOfSlides = slides.length;

	// Remove scrollbar in JS
	$('#slidesContainer').css('overflow', 'hidden');

	// Wrap all .slides with #slideInner div
	slides.wrapAll('<div id="slideInner"></div>')
	// Float left to display horizontally, readjust .slides width
	.css({
		'float' : 'left',
    	'width' : slideWidth
	});

	// Set #slideInner width equal to total width of all slides
	$('#slideInner').css('width', slideWidth * numberOfSlides);

	// Insert left and right arrow controls in the DOM
	$('#slideshow')
    	.prepend('<span class="control" id="leftControl">Move left</span>')
    	.append('<span class="control" id="rightControl">Move right</span>');

	// Hide left arrow control on first load
	manageControls(currentPosition, numberOfSlides);

	// Create event listeners for .controls clicks
	$('.control')
    	.bind('click', function(){
    		// Determine new position
    		currentPosition = ($(this).attr('id')=='rightControl') ? currentPosition+1 : currentPosition-1;
    		
    		// Hide / show controls
    		manageControls(currentPosition, numberOfSlides);
    		// Move slideInner using margin-left
    		$('#slideInner').animate({
    			'marginLeft' : slideWidth*(-currentPosition)
    		});
    	});
}

// manageControls: Hides and shows controls depending on currentPosition
function manageControls(position, numberOfSlides){
	// Hide left arrow if position is first slide
	if(position==0){
		$('#leftControl').hide();
	} else {
		$('#leftControl').show();
	}
	// Hide right arrow if position is last slide
	if(position==numberOfSlides-1){
		$('#rightControl').hide();
	} else {
		$('#rightControl').show();
	}
	
	$("#pagination").html((position+1)+"/"+numberOfSlides);
}

function resizeImage(image, width, height){
	var imgWidth = image.width();
	var imgHeight = image.height();
	image.attr("realWidth", imgWidth);
	image.attr("realHeight", imgHeight);

	if(image.height() > height) {
		image.height(height);
	}
	
	if(image.width() > width){
		image.width(width);
	}
	
	image.show()
}

function resizeImages(){
	$('img[id*="img"]').each(function(){
		resizeImage($(this), imageWidth, imageHeight);
	});
}

function loadArtistImage(imageURL){	
	$("#img-art")
		.css("background-image", "url()")
		.html('<img src="'+imageURL+'" style="display:none" onload="resizeArtistImage($(this))"/>');
}

function resizeArtistImage(image){
	resizeImage(image, imageMinWidth, imageMinHeight)
	if(image.height() < imageMinHeight){
		var marginTop = (imageMinHeight - image.height())/2;
		image.css("margin-top", marginTop);
	}
}

function openImageByIndex(){
	var image = $("#img"+imageIndex);
	openImage(image);
}

function openImage(image){
	var url = image.attr("src");	
	var width = image.attr("realWidth");
	var height = image.attr("realHeight");
	
	if(windowImage){
		windowImage.close();
	}
	windowImage = window.open("about:blank","windowImage","height="+height+",width="+width+",menubar=0,resizable=0,scrollbars=0,status=0,titlebar=0,toolbar=0");
	windowImage.document.write("<html>");
	windowImage.document.write("<head>");
	windowImage.document.write("<title>TocaLetra.com.br - O seu site de letras, traduções e cifras</title>");
	windowImage.document.write("</head>");
	windowImage.document.write("<body leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>");
	windowImage.document.write("<img src='"+url+"'/>");
	windowImage.document.write("</body>");
	windowImage.document.write("</html>");
}

function scrollMultimedia(){
	var scrollWindow = $(window).scrollTop();
	var divTop = $("#letra").position().top - 80;
	var marginTop = scrollWindow - divTop;
	var limitTop = $("#letra").height() - $("#multimedia").height();
	if(scrollWindow > divTop && marginTop < limitTop){
		$("#multimedia")
			.stop()
			.animate({"marginTop": (marginTop) + "px"},"fast" );	
	}
}

function showMultimedia(){
	if($("#multimedia").css("display") == "none"){
		if($("#aba-radio").css("display") != "none"){
			$("#radio").show();
			$("#aba-radio").removeClass("no-selec");
			$("#aba-radio").addClass("selecionado");
		} else if($("#aba-video").css("display") != "none"){
			$("#video").show();
			$("#aba-video").removeClass("no-selec");
			$("#aba-video").addClass("selecionado");
		} else if($("#aba-galeria").css("display") != "none"){
			$("#galeria").show();
			$("#aba-galeria").removeClass("no-selec");
			$("#aba-galeria").addClass("selecionado");
		}
	
		$("#nav-aba a").click(function(){
			$(".aba").each(function(){
				$(this).hide();
			});										  
									  
			var div = $(this).attr("href");
			$(div).show();
			if(div == "#galeria"){
				resizeImages();
			}
			
			$("#nav-aba li").each(function(){
				$(this).removeClass("selecionado");
				$(this).addClass("no-selec");
			});
			$("#nav-aba li:contains('"+$(this).html()+"')").removeClass("no-selec");
			$("#nav-aba li:contains('"+$(this).html()+"')").addClass("selecionado");
			
			return false;
		});
		
		$("#multimedia").show();
		$(window).bind("scroll", scrollMultimedia);
	}
}

$("#btnScroll").click(function(){
	if($(this).hasClass("move")){
		$(window).bind("scroll", scrollMultimedia);
		$(this).removeClass("move");
		$(this).addClass("stop");
	} else {
		$(window).unbind("scroll");
		$(this).removeClass("stop");
		$(this).addClass("move");
	}
});
$("#btnMinMax").click(function(){
	if($(this).hasClass("maximizar")){
		$("#multi-content").show();
		$(this).removeClass("maximizar");
		$(this).addClass("minimizar");
		
		if($("#galeria").css("display") != "none"){
			resizeImages();
		}
	} else {
		$("#multi-content").hide();
		$(this).removeClass("minimizar");
		$(this).addClass("maximizar");
	}
});