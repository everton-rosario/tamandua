/*load multimedia*/
if(idtRadioTrack != ""){
	loadRadio(idtRadioTrack, artistName, artistNameNormalized);
}

if($("#panel-artistas").length > 0){
	if(artistImages.length > 0){
		loadImages(artistImages);
		if(artistImages.length > 1){
			loadThumbnailImages(artistImages);
		}
	} else {
		loadNoImage();
	}
} else {
	if(artistImages.length > 0){
		loadArtistImage(artistImages[0]);
		loadGaleria(artistImages);
	}	
}

/* includes html */
$("#artistas-az ul").load("/pages/include/az.inc");
$("#artistCloud").load("/pages/include/cloud-artist.inc");
$("#musicCloud").load("/pages/include/cloud-music.inc");

/* binds do box de busca */
$('#inputSearch').focus(function(){
	var oInput = $(this);
	
    oInput.css({
        color: "#2c3f4e"
    });
	
    if ( oInput.val() == 'Digite o artista ou a música' ) {
		oInput.val('');
	}
}).focusout(function(){
	var oInput = $(this);
	oInput.css({
        color: "#a3a9b5"
    });
	
    if ( oInput.val() == '' ) {
		oInput.val('Digite o artista ou a música');
	}
});

if($.url.param("category") != ""){
	$("#categoryOptions option[value='"+$.url.param("category")+"']").attr('selected', 'selected');
}

$("#categoryOptions").change(function(){
	$("#categorySearch").val($(this).val());
	if($(this).val() == ""){
		$("#categorySearch").attr("disabled", "disabled");
	} else {
		$("#categorySearch").attr("disabled", "");
	}
});

$('#formBusca').submit(function(){
	var oThis = $(this);
	var oInput = $('#inputSearch');
    if ( oInput.val() == 'Digite o artista ou a música' ) {
		return false;
	} else {
		return true;
	}
});

$("#btnBusca").click(function(){
	$('#formBusca').submit();
});


$(document).ready(function () {
	// Seleciona a letra
	if(letter != null) {
		$("#letter_"+letter.toLowerCase()).addClass("selecionado");
	}

//	Descomentar trecho quando incluir o login
  	var value = getCookie("session");
  	if(value == ""){
  		showLogin();
  	} else {
  		showLogged(value);
  	}
	
  	$("#avatarImg").error(function(){
  		$(this).attr("src", "http://tocaletraimg.s3.amazonaws.com/avatar/error_photo.gif");
  	});
});

function getCookie(name){
	 if (document.cookie.length > 0){
		 var start = document.cookie.indexOf(name + "=");
		 if (start != -1){
			 start = start + name.length+1;
			 var end = document.cookie.indexOf(";",start);
			 if (end == -1)
				 end = document.cookie.length;
			 
			 var value = document.cookie.substring(start,end);
			 var decodeValue = decodeURIComponent(value);

			 if(decodeValue.charAt(0) == '"'){
				 decodeValue = decodeValue.substring(1,decodeValue.length-1);
			 }
			 return decodeValue;
		 }
	 }
	 return "";
}

function showLogin(){
	$("#loadingBox").hide();
	$("#loginBox").show();
		
	
	$("#loginForm").submit(function(){
		if($("#emailLogin").val().length == 0 || $("#passLogin").val().length == 0){
			alertLogin("Os campos e-mail e senha são obrigatórios.");
			return false;
		}
		$("#loginBox").hide();		
		$("#loadingBox").removeClass("loggedBox");
		$("#loadingBox").addClass("loginBox");
		$("#loadingBox").show();
		$.ajax({
			url: "/usuario/login",
			type: "post",
			data: $(this).serialize(),
			success: function(response){
				if(response.success){
					var value = getCookie("session");
					if(value != ""){
						showLogged(value);
						
					}	
				} else {
					$("#loadingBox").hide();
					$("#loginBox").show();
					alertLogin(response.error);
				}
			}
		})
		return false;
	});
}

function showLogged(value){
	$("#loadingBox").hide();
	$("#loggedBox").show();
	
	value = replaceAll(value, '\\', '');
	var user = eval('(' + value + ')');

	$("#userName").html(user.name);
	if(user.imageUrl == null || user.imageUrl == ""){
		$("#avatarImg").attr("src", "http://tocaletraimg.s3.amazonaws.com/avatar/no_photo.gif");
	} else {
		$("#avatarImg").attr("src", user.imageUrl);
	}
	
	$("#logoutLink").click(function(){
		$("#loggedBox").hide();		
		$("#loadingBox").removeClass("loginBox");
		$("#loadingBox").addClass("loggedBox");
		$("#loadingBox").show();
		$.ajax({
			url: "/usuario/logout",
			type: "post",
			success: function(response){
				var cookie = getCookie("session");
				if(cookie == ""){
					showLogin();
				}
			}
		});
	});
}

function alertLogin(message){
	$("#loginAlert").show();
	$("#loginMessage").html(message);
}

function replaceAll(string, token, newtoken) {
	while (string.indexOf(token) != -1) {
 		string = string.replace(token, newtoken);
	}
	return string;
}
