/**
 * URL Padrão dos Services
 */
var url_prefix = "http://tocaletra.com.br/ws/";


window.onload = function(){
	/**
	 * Envia dados para o service /artists/save
	 */
	$(function(){
		$('#enviar_artista').click(function(){
			/** Campos do formulario **/
			var artist_id = $("#artist_id").val();
			var artist_letter = $("#artist_letters").val();
			var artist_name = $("#artist_name").val();
			/** Data no formato padrão do banco de dados **/
			var artist_dtBirth = parseDate($("#artist_dtBirth").val());
			var artist_dtEnd = parseDate($("#artist_dtEnd").val());
			var artist_uri = $("#artist_uri").val();
			var artist_image = $("#image").val();
			var artist_imageHuge = $("#imageHuge").val();
			var artist_imageTiny = $("#imageTiny").val();
			/** URL do service **/
			var url = url_prefix + "artists/save";
			
			if(artist_id == null ){
				var letters = artist_name.substr(0,1).toLowerCase();
				var objeto = {"artist":{"idArtist":artist_id,"dtBirth":artist_dtBirth,"dtEnd":artist_dtEnd,"name":artist_name,"letters":letters}};
				postData(objeto, url);
			}else{
				var objeto = {"artist":{"idArtist":artist_id,"dtBirth":artist_dtBirth,"dtEnd":artist_dtEnd,"name":artist_name,"letters":artist_letter,"image":{"uri":artist_image},"imageHuge":{"uri":artist_imageHuge},"imageTiny":{"uri":artist_imageTiny},"uri":artist_uri}};
				postData(objeto, url);
			}

			return false;
		})
	})
	
	/**
	 * Envia dados para o service /music/save
	 */
	 $(function(){
		$('#salvar_musica').click(function(){
			/** Campos do formulario **/
			var artist_uri = $("#artist_uri").val();
			var music_id = $("#music_id").val();
			var music_uri = $("#music_uri").val();
			var music_name = $("#music_name").val();
			var music_dtRelease = parseDate($("#music_dtRelease").val());
			var music_lyric_laguage = $("input:radio[name=language]:checked").val();
			var music_lyric_text = $("#lyric_text").val();
			var idLyricOriginal = $("#idLyrics").val();
			var music_lyric_type = $("#lyricType").val();
			
			/** URL do service **/
			var url = url_prefix + "musics/saveDetailed";
			
			/** Objeto com os dados do Formulário **/
			if(music_id == null){
				var objeto = {"music":{"idMusic":music_id,"dtRelease":music_dtRelease,"title":music_name,"uri":music_uri,"lyrics":[{"lyricTitle":music_name,"text":music_lyric_text,"provider":"x","lyricType":music_lyric_type,"flag":"S","laguage":music_lyric_laguage}],"artist":{"uri":artist_uri}}};
				//alert(JSON.stringify(objeto))
				postData(objeto, url);
			}else{
				var objeto = {"music":{"idMusic":music_id,"dtRelease":music_dtRelease,"title":music_name,"uri":music_uri,"idLyricOriginal":idLyricOriginal}};
				postData(objeto, url);
			}

			return false;
		})
	})										
	
	$(function(){
		$('#salvar_nova_letra').click(function(){
			
			/** Campos do formulario **/
			var music_url = $("#music_url_new").val();
			var music_id = $("#music_id_new").val();
			var music_uri = $("#music_uri_new").val();
			var music_title = $("#music_title_new").val();
			var lyric_text = $("#lyric_text_new").val();
			var lyric_provider = $("#lyric_provider").val();
			var lyric_type = $("#lyric_type").val();
			var lyric_laguage = $("input:radio[name=language]:checked").val();
			var lyric_flag_original_new = $("input:checkbox[name=lyric_flag_original_new]:checked").val();
			
			/** Objeto com os dados do Formulário **/
			var objeto = {"music":{"uri":music_uri,"url":music_url,"title":music_title,"lyrics":[{"lyricTitle":music_title,"text":lyric_text,"provider":lyric_provider,"lyricType":lyric_type,"laguage":lyric_laguage,"flag":lyric_flag_original_new}]}};
			/** URL do service **/
			var url = url_prefix + "musics/saveLyric";
			
			//alert(JSON.stringify(objeto));
			
			/** Enviando os dados para o service **/
			postData(objeto, url);

			return false;
		})
	})
	
	$(function(){
		$('#save_user_ainda_nao_usado').click(function(){
			
			var user_email = $("#email").val();
			var user_password = $("#password").val();
			var user_cpf = $("#cpf").val();
			var user_street = $("#street").val();
			var user_city = $("#city").val();
			var user_streetNumber = $("#streetNumber").val();
			var user_streetComplement = $("#streetComplement").val();
			var user_state = $("#state").val();
			var user_country = $("#country").val();
		
		    var objeto = {"user":{"email":user_email,"password":user_password,"cpf":str_cpf,"street":str_street,"city":str_city,"streetNumber":str_streetNumber,"streetComplement":str_streetComplement,"state":str_state,"country":str_country}};
		    
		    alert(JSON.stringify(objeto));
		    
		    return false;
		})
	})
	
	$(function(){
		$('#adicionar_imagem').click(function(){
			$('#uploadLink').click();
		})
	})
	
	$(function(){
		$('#remover_imagem').click(function(){
			$('#removeLink').click();
		})
	})
	
	$(function(){
		$('#corrigir_musica').click(function(){
			var user_id = $("#idUser").val();
			var md_status = $("#status").val();
			var music_idEntity = $("#music_idEntity").val();
			var lyric_text_corretor = $("#lyric_text_corretor").val();
			
			var objeto = {"moderation":{"newValue":lyric_text_corretor,"idEntity":music_idEntity,"status":md_status,"user":{"idUser":user_id}}};
			
			/** URL do service **/
			var url = url_prefix + "moderation/music";
			
			//alert(JSON.stringify(objeto));
			
			/** Enviando os dados para o service **/
			postData(objeto, url);
		})
	})
	/**
	 * Converte data "dd/mm/aaaa" para "aaaa-mm-dd"
	 */
	function parseDate(date){
		if(date == "undefined" || date == ""){
			newDate = "0000-00-00";
		}else{
			var date_split = date.split("/");
			newDate = date_split[2]+"-"+date_split[1]+"-"+date_split[0];
		}
		return newDate;
	}
	
	}
	
	/**
	 * Envia os dados via ajax para o service
	 */
	function postData(data,url){
		$.ajax({
			type:'POST',		
			url: url,
			data:JSON.stringify(data),
			beforeSend: function(xhr) {
				xhr.setRequestHeader("Content-Type","application/json; charset=UTF-8");
				$('#basic-modal-content').modal();
			},
			dataType: "text",
			success : function(){
				location.reload();
			}
		})
	}
	
	/**
	 * Faz a edição da letra
	 */
	function salvaLetra(id){
		var music_uri = $("#music_uri"+id).val();
		var music_name = $("#music_name"+id).val();
		var lyric_id = $("#lyric_id"+id).val();
		var lyric_text = $("#lyric_text"+id).val();
		var lyric_provider = $("#lyric_prov"+id).val();
		var lyric_uri = $("#lyric_uri"+id).val();
		var lyric_title = $("#lyric_title"+id).val();
		var lyric_type = $("#lyric_type"+id).val();
		var lyric_language = $("#lyric_language"+id).val();
		
		/** Objeto com os dados do Formulário **/
		
		var objeto = {"music":{"uri":music_uri,"title":music_name,"lyrics":[{"idLyric":lyric_id,"uri":lyric_uri,"text":lyric_text,"provider":lyric_provider,"lyricTitle":lyric_title,"lyricType":lyric_type,"laguage":lyric_language}]}};
		
		/** URL do service **/
				
		var url = url_prefix + "musics/saveLyric";
		
		//alert(JSON.stringify(objeto));
		
		
		postData(objeto,url);
	}
	
	/**
	 * 
	 */
	function publishModerate(type, action, status){
		var message = "Deseja realmente";
		var url = url_prefix + type+"/"+action;
		if(action == "moderate"){
			url += "?moderate="+status;
			message += status ? " moderar " : " desmoderar ";
		} else if(action == "publish"){
			url += "?publish="+status;
			message += status ? " publicar " : " despublicar ";
		}
		if(type == "artists"){
			url += "&id="+$('#artist_id').val();
			message += "esse artista?";
		} else if(type == "musics"){
			url += "&id="+$('#music_id').val();
			message += "essa música?";
		}

		 if(confirm(message)) {						
			postData({}, url);
		 }else{
			return false;		
		 }
	}

	/**
	 * 
	 */
	function publishModerateAll(type, action, status){
		var message = "Deseja realmente";
		var url = url_prefix + type+"/"+action+"/all";
		if(action == "moderate"){
			url += "?moderate="+status;
			message += status ? " moderar " : " desmoderar ";
		} else if(action == "publish"){
			url += "?publish="+status;
			message += status ? " publicar " : " despublicar ";
		}
		if(type == "artists"){
			url += "&letters="+$('#letter_artist').val();
			message += "todos os artistas dessa letra?";
		} else if(type == "musics"){
			url += "&artistId="+$('#artist_id').val();
			message += "todas as músicas desse artista?";
		}

		 if(confirm(message)) {						
			postData({}, url);
		 }else{
			return false;		
		 }
	}

	/**
	 * 
	 */
	function publishModerateSelected(type, action, status){
		var warningMessage = "Você deve selecionar pelo menos";
		var confirmMessage = "Deseja realmente";
		var url = url_prefix + type+"/"+action+"/selected";
		if(action == "moderate"){
			url += "?moderate="+status;
			confirmMessage += status ? " moderar " : " desmoderar ";
		} else if(action == "publish"){
			url += "?publish="+status;
			confirmMessage += status ? " publicar " : " despublicar ";
		}
		
		var ids;
		if(type == "artists"){
			ids = getSelectedValues("artist_ids");
			confirmMessage += "todos os artistas selecionados?";
			warningMessage += " um artista.";
		} else if(type == "musics"){
			ids = getSelectedValues("music_ids");
			confirmMessage += "todas as músicas selecionadas?";
			warningMessage += " uma música.";
		}
		 
		if(ids.length > 0){
			if(confirm(confirmMessage)) {
				for(var i=0; i<ids.length; i++){
					url += "&ids="+ids[i];
				}					
				postData({}, url);
			} else {
				return false;
			}
		} else {
			alert(warningMessage);
			return false;
		}
	}
	 
	/**
	 * Publica ou despublica a letra
	 */
	function publicLetter(status){
		var action = status ? "publicar" : "despublicar";
		var url = url_prefix + "artists/publish/letter?publish=" + status+"&letters="+$("#letter_artist").val();	

		if(confirm("Deseja realmente "+action+" a letra?")) {						
			postData({}, url);
		}else{
			return false;		
		}	
	}
	
	 /**
	  * Obtém um array com os valores dos checkboxes selecionados.
	  */
	function getSelectedValues(name){
		 var values = new Array();
		 $(document).find("#"+name+":checked").each(function(){
			 values.push(this.value);
		 });
		 return values;
	}
	
	/**
	 * Remove as imagens do artista
	 */
	function removeImages(){
			var artistId = $("#artistId").val();
			var confirmMessage = "Deseja remover todas as imagens selecionadas?";
			var	warningMessage = "Você deve selecionar pelo menos uma imagem";
				
			var url = url_prefix + "artists/remove/image?idArtist=" + artistId;
			var ids;
						
			ids = getSelectedValues("image_ids");
			
			if(ids.length > 0){
				if(confirm(confirmMessage)) {
					for(var i=0; i<ids.length; i++){
						url += "&ids="+ids[i];
					}					
					postData({}, url);
				} else {
					return false;
				}
			} else {
				alert(warningMessage);
				return false;
			}
	}
	
	function checkAll(name, flag){
		$("input[@name=" + name + "][type='checkbox']").attr('checked', flag);
    }