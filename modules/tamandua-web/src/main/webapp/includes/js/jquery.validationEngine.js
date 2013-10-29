/*
 * Inline Form Validation Engine 1.6, jQuery plugin
 * 
 * Copyright(c) 2009, Cedric Dugas
 * http://www.position-relative.net
 *	
 * Form validation engine which allow custom regex rules to be added.
 * Thanks to Francois Duquette
 * Licenced under the MIT Licence
 */
 
 
(function($) {
	
	var firstvalid = true;
	$.fn.validationEngine = function(settings) {

	if($.validationEngineLanguage){				// IS THERE A LANGUAGE LOCALISATION ?
		allRules = $.validationEngineLanguage.allRules
	}else{
		allRules = 	{"required":{    			// Add your regex rules here, you can take telephone as an example
						"regex":"none",
						"alertText":"* This field is required",
						"alertTextCheckboxMultiple":"* Please select an option",
						"alertTextCheckboxe":"* This checkbox is required"},
					"length":{
						"regex":"none",
						"alertText":"*Between ",
						"alertText2":" and ",
						"alertText3": " characters allowed"},
					"minLength":{
                        "regex":"none",
                        "alertText":"* The field must be at least ",
                        "alertText2":" characters"},
					"maxLength":{
                        "regex":"none",
                        "alertText":"* The field must be no longer than ",
                        "alertText2":" characters"},
					"minValue":{
                        "regex":"none",
                        "alertText":"* The field value must not be smaller than "},
					"maxValue":{
                        "regex":"none",
                        "alertText":"* The field value must not be larger than "},
					"maxCheckbox":{
						"regex":"none",
						"alertText":"* Checks allowed Exceeded"},	
					"minCheckbox":{
						"regex":"none",
						"alertText":"* Please select ",
						"alertText2":" options"},	
					"confirm":{
						"regex":"none",
						"alertText":"* Your field is not matching"},		
					"telephone":{
						"regex":"/^[0-9\-\(\)\ ]+$/",
						"alertText":"* Invalid phone number"},	
					"email":{
						"regex":"/^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/",
						"alertText":"* Invalid email address"},	
					"date":{
                         "regex":"/^[0-9]{4}\-\[0-9]{1,2}\-\[0-9]{1,2}$/",
                         "alertText":"* Invalid date, must be in YYYY-MM-DD format"},
					"onlyNumber":{
						"regex":"/^[0-9\ ]+$/",
						"alertText":"* Numbers only"},	
					"noSpecialCaracters":{
						"regex":"/^[0-9a-zA-Z]+$/",
						"alertText":"* No special caracters allowed"},		
					"onlyLetter":{
						"regex":"/^[a-zA-Z\ \']+$/",
						"alertText":"* Letters only"},
					"validateCPF":{
						"regex":"none",
						"alertText":"* The CPF must be 11 characters",
						"alertText2":"* Numbers only",
						"alertText3":"* Invalid CPF"},
					"requiredIf":{
						"regex":"none",
						"alertText":"* This field is required"}
					}	
	}
 	settings = jQuery.extend({
		allrules:allRules,
		validationEventTriggers:"blur",					
		inlineValidation: true,	
		returnIsValid:false,
		ajaxSubmit: false,
		promptPosition: "centerRight",	// OPENNING BOX POSITION, IMPLEMENTED: topLeft, topRight, bottomLeft, centerRight, bottomRight
		success : false,
		failure : function() {},
 		submitFunction : function() {}
	}, settings);	

	$.validationEngine.ajaxValidArray = new Array()	// ARRAY FOR AJAX: VALIDATION MEMORY 
	
	if(settings.inlineValidation == true){ 		// Validating Inline ?
		if(firstvalid == true){					// NEEDED FOR THE SETTING returnIsValid
			$(this).find("[class^=validate]").not("[type=checkbox]").bind(settings.validationEventTriggers, function(caller){ _inlinEvent(this)})
			$(this).find("[class^=validate][type=checkbox]").bind("click", function(caller){ _inlinEvent(this) })
			firstvalid = false;
		}
			function _inlinEvent(caller){
				if($.validationEngine.intercept == false || !$.validationEngine.intercept){		// STOP INLINE VALIDATION THIS TIME ONLY
					$.validationEngine.onSubmitValid=false;
					$.validationEngine.loadValidation(caller,settings); 
				}else{
					$.validationEngine.intercept = false;
				}
			}
	} else {
		$(this).find("[class^=validate]").not("[type=checkbox]").bind('blur', function(){ $(this).closePrompt(false) })
	}
	
	if (settings.returnIsValid == true){		// Do validation and return true or false, it bypass everything;
		
		if ($.validationEngine.submitValidation(this,settings)){
			return false;
		}else{
			return true;
		}
	}
	
	$(this).bind("submit", function(caller){   // ON FORM SUBMIT, CONTROL AJAX FUNCTION IF SPECIFIED ON DOCUMENT READY
		$.validationEngine.onSubmitValid = true;
		
		if($.validationEngine.submitValidation(this,settings) == false){
			if($.validationEngine.submitForm(this,settings) == true) {return false;}
		}else{
			settings.failure && settings.failure(); 
			return false;
		}
		return $.validationEngine.settings.submitFunction();
	})
};
$.fn.showPrompt = function(promptText,type,ajaxed,showTriangle,promptPosition){
	var divFormError = document.createElement('div');
	var formErrorContent = document.createElement('div');

	linkTofield = $(this).attr("id") + "formError";
	linkTofield = linkTofield.replace("[",""); linkTofield = linkTofield.replace("]","");

	$(divFormError).addClass("formError")

	if(type == "pass"){ $(divFormError).addClass("greenPopup") }
	if(type == "load"){ $(divFormError).addClass("blackPopup") }
	if(ajaxed){ $(divFormError).addClass("ajaxed") }

	$(divFormError).addClass(linkTofield)
	$(formErrorContent).addClass("formErrorContent")

	$("body").append(divFormError)
	$(divFormError).append(formErrorContent)

	if(showTriangle != false){		// NO TRIANGLE ON MAX CHECKBOX AND RADIO
		var arrow = document.createElement('div')
		$(arrow).addClass("formErrorArrow")
		$(divFormError).append(arrow)
		if(promptPosition == "bottomLeft" || promptPosition == "bottomRight"){
			$(arrow).addClass("formErrorArrowBottom")
			$(arrow).html('<div class="line1"><!-- --></div><div class="line2"><!-- --></div><div class="line3"><!-- --></div><div class="line4"><!-- --></div><div class="line5"><!-- --></div><div class="line6"><!-- --></div><div class="line7"><!-- --></div><div class="line8"><!-- --></div><div class="line9"><!-- --></div><div class="line10"><!-- --></div>');
		}
		if(promptPosition == "topLeft" || promptPosition == "topRight"){
			$(divFormError).append(arrow)
			$(arrow).html('<div class="line10"><!-- --></div><div class="line9"><!-- --></div><div class="line8"><!-- --></div><div class="line7"><!-- --></div><div class="line6"><!-- --></div><div class="line5"><!-- --></div><div class="line4"><!-- --></div><div class="line3"><!-- --></div><div class="line2"><!-- --></div><div class="line1"><!-- --></div>');
		}
	}
	$(formErrorContent).html(promptText)

	callerTopPosition = $(this).offset().top;
	callerleftPosition = $(this).offset().left;
	callerWidth =  $(this).width()
	inputHeight = $(divFormError).height()

	/* POSITIONNING */
	if(promptPosition == "topRight"){callerleftPosition +=  callerWidth -30; callerTopPosition += -inputHeight -10; }
	if(promptPosition == "topLeft"){ callerTopPosition += -inputHeight -10; }
	
	if(promptPosition == "centerRight"){ callerleftPosition +=  callerWidth +13; }
	
	if(promptPosition == "bottomLeft"){
		callerHeight =  $(this).height();
		callerleftPosition = callerleftPosition;
		callerTopPosition = callerTopPosition + callerHeight + 15;
	}
	if(promptPosition == "bottomRight"){
		callerHeight =  $(this).height();
		callerleftPosition +=  callerWidth -30;
		callerTopPosition +=  callerHeight + 15;
	}
	$(divFormError).css({
		top:callerTopPosition,
		left:callerleftPosition,
		opacity:0
	})
	return $(divFormError).animate({"opacity":0.87},function(){return true;});
};
$.fn.updatePromptText = function(promptText,type,ajaxed,promptPosition) {	// UPDATE TEXT ERROR IF AN ERROR IS ALREADY DISPLAYED
	
	linkTofield = $(this).attr("id") + "formError";
	linkTofield = linkTofield.replace("[",""); linkTofield = linkTofield.replace("]","");
	
	var updateThisPrompt =  "."+ linkTofield;
	
	if(type == "pass") { $(updateThisPrompt).addClass("greenPopup") }else{ $(updateThisPrompt).removeClass("greenPopup")};
	if(type == "load") { $(updateThisPrompt).addClass("blackPopup") }else{ $(updateThisPrompt).removeClass("blackPopup")};
	if(ajaxed) { $(updateThisPrompt).addClass("ajaxed") }else{ $(updateThisPrompt).removeClass("ajaxed")};

	$(updateThisPrompt).find(".formErrorContent").html(promptText)
	callerTopPosition  = $(this).offset().top;
	inputHeight = $(updateThisPrompt).height()
	
	if(promptPosition == "bottomLeft" || promptPosition == "bottomRight"){
		callerHeight =  $(this).height()
		callerTopPosition =  callerTopPosition + callerHeight + 15
	}
	if(promptPosition == "centerRight"){  callerleftPosition +=  callerWidth +13;}
	if(promptPosition == "topLeft" || promptPosition == "topRight"){
		callerTopPosition = callerTopPosition  -inputHeight -10
	}
	$(updateThisPrompt).animate({ top:callerTopPosition });
};
$.fn.closePrompt = 	function(outside) {						// CLOSE PROMPT WHEN ERROR CORRECTED
	if(outside){
		$(".formError").fadeTo("fast",0,function(){
			$(".formError").remove();
		});
		return false;
	} else {
		linkTofield = $(this).attr("id") + "formError";
		linkTofield = linkTofield.replace("[",""); linkTofield = linkTofield.replace("]","");
		
		closingPrompt = "."+linkTofield;

		$(closingPrompt).fadeTo("fast",0,function(){
			$(closingPrompt).remove();
		});
	}
}
$.validationEngine = {
	submitForm : function(caller){
		if($.validationEngine.settings.ajaxSubmit){		
			
			$.ajax({
			   	type: "POST",
			   	url: $.validationEngine.settings.ajaxSubmitFile,
			   	async: true,
			   	data: $(caller).serialize(),
			   	error: function(data,transport){ $.validationEngine.debug("error in the ajax: "+data.status+" "+transport) },
			   	success: function(data){
			   		if(data == "true"){			// EVERYTING IS FINE, SHOW SUCCESS MESSAGE
			   			$(caller).css("opacity",1)
			   			$(caller).animate({opacity: 0, height: 0}, function(){
			   				$(caller).css("display","none")
			   				$(caller).before("<div class='ajaxSubmit'>"+$.validationEngine.settings.ajaxSubmitMessage+"</div>")
			   				$(caller).closePrompt(true); 	
			   				$(".ajaxSubmit").show("slow")
			   				if ($.validationEngine.settings.success){	// AJAX SUCCESS, STOP THE LOCATION UPDATE
								$.validationEngine.settings.success && $.validationEngine.settings.success(); 
								return false;
							}
			   			})
		   			}else{						// HOUSTON WE GOT A PROBLEM (SOMETING IS NOT VALIDATING)
			   			data = eval( "("+data+")");	
			   			if(!data.jsonValidateReturn){
			   				 $.validationEngine.debug("you are not going into the success fonction and jsonValidateReturn return nothing")
			   			}
			   			errorNumber = data.jsonValidateReturn.length	
			   			for(index=0; index<errorNumber; index++){	
			   				fieldId = data.jsonValidateReturn[index][0];
			   				promptError = data.jsonValidateReturn[index][1];
			   				type = data.jsonValidateReturn[index][2];
			   				$(fieldId).showPrompt(promptError,type,false,$.validationEngine.showTriangle,$.validationEngine.settings.promptPosition);
		   				}
	   				}
   				}
			})	
			return true;
		}
		if ($.validationEngine.settings.success){	// AJAX SUCCESS, STOP THE LOCATION UPDATE
			$(caller).unbind("submit")
			$.validationEngine.settings.success && $.validationEngine.settings.success(); 
			return true;
		}
		return false;
	},
	loadValidation : function(caller,settings) {		// GET VALIDATIONS TO BE EXECUTED
		
		$.validationEngine.settings = settings
		rulesParsing = $(caller).attr('class');
		rulesRegExp = /\[(.*)\]/;
		getRules = rulesRegExp.exec(rulesParsing);
		str = getRules[1]
		pattern = /\W+/;
		result= str.split(pattern);	
		
		var validateCalll = $.validationEngine.validateCall(caller,result)
		return validateCalll
	},
	validateCall : function(caller,rules) {	// EXECUTE VALIDATION REQUIRED BY THE USER FOR THIS FIELD
		var promptText =""	
		
		if(!$(caller).attr("id")) { $.validationEngine.debug("This field have no ID attribut( name & class displayed): "+$(caller).attr("name")+" "+$(caller).attr("class")) }
		
		var prompt = $(caller).attr("id");
		var caller = caller;
		ajaxValidate = false
		var callerName = $(caller).attr("name")
		$.validationEngine.isError = false;
		$.validationEngine.showTriangle = true
		callerType = $(caller).attr("type");

		for (i=0; i<rules.length;i++){
			switch (rules[i]){
			case "optional": 
				if(!$(caller).val()){
					$(caller).closePrompt(false);
					return $.validationEngine.isError
				}
			break;
			case "required": 
				_required(caller,rules);
			break;
			case "custom": 
				 _customRegex(caller,rules,i);
			break;
			case "ajax": 
				if(!$.validationEngine.onSubmitValid){
					_ajax(caller,rules,i);	
				}
			break;
			case "length": 
				 _length(caller,rules,i);
			break;
		    case "minLength":
                  _minLength(caller,rules,i);
            break;
			case "maxLength":
			      _maxLength(caller,rules,i);
			break;
			case "minValue":
			      _minValue(caller,rules,i);
			break;
			case "maxValue":
			      _maxValue(caller,rules,i);
			break;
			case "maxCheckbox": 
				_maxCheckbox(caller,rules,i);
			 	groupname = $(caller).attr("name");
			 	caller = $("input[name='"+groupname+"']")
			break;
			case "minCheckbox": 
				_minCheckbox(caller,rules,i);
				groupname = $(caller).attr("name");
			 	caller = $("input[name='"+groupname+"']")
			break;
			case "confirm": 
				 _confirm(caller,rules,i);
			break;
			case "validateCPF": 
				 _validateCPF(caller,rules,i);
			break;
			case "requiredIf": 
				_requiredIf(caller,rules,i);
			break;
			default :;
			};
		};
		if ($.validationEngine.isError == true){
			radioHackOpen();
			($("div."+prompt+"formError").size() ==0) ?
					$(caller).showPrompt(promptText,"error",false,$.validationEngine.showTriangle,$.validationEngine.settings.promptPosition) :
					$(caller).updatePromptText(promptText,"",false,$.validationEngine.settings.promptPosition);
		}else{
			radioHackClose();
			$(caller).closePrompt(false);
		}		
		/* UNFORTUNATE RADIO AND CHECKBOX GROUP HACKS */
		/* As my validation is looping input with id's we need a hack for my validation to understand to group these inputs */
		function radioHackOpen(){
	      if($("input[name='"+callerName+"']").size()> 1 && (callerType == "radio" || callerType == "checkbox")) {        // Hack for radio/checkbox group button, the validation go the first radio/checkbox of the group
	          caller = $("input[name='"+callerName+"'][type!=hidden]:first");     
	          $.validationEngine.showTriangle = false;
	           prompt =$(caller).attr("id");
	      }      
	    }
	    function radioHackClose(){
	      if($("input[name='"+callerName+"']").size()> 1 && (callerType == "radio" || callerType == "checkbox")) {       //  Hack for radio/checkbox group button, the validation go the first radio/checkbox of the group
	          caller = $("input[name='"+callerName+"'][type!=hidden]:first");      
	      }      
	    }
		/* VALIDATION FUNCTIONS */
		function _required(caller,rules){   // VALIDATE BLANK FIELD
			callerType = $(caller).attr("type");
			if (callerType == "text" || callerType == "password" || callerType == "textarea"){
								
				if(!$(caller).val()){
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules[rules[i]].alertText+"<br />";
				}	
			}	
			if (callerType == "radio" || callerType == "checkbox" ){
				callerName = $(caller).attr("name");
		
				if($("input[name='"+callerName+"']:checked").size() == 0) {
					$.validationEngine.isError = true;
					if($("input[name='"+callerName+"']").size() ==1) {
						promptText += $.validationEngine.settings.allrules[rules[i]].alertTextCheckboxe+"<br />"; 
					}else{
						 promptText += $.validationEngine.settings.allrules[rules[i]].alertTextCheckboxMultiple+"<br />";
					}	
				}
			}	
			if (callerType == "select-one") { // added by paul@kinetek.net for select boxes, Thank you
				callerName = $(caller).attr("id");
				
				if(!$("#"+callerName).val()) {
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules[rules[i]].alertText+"<br />";
				}
			}
			if (callerType == "select-multiple") { // added by paul@kinetek.net for select boxes, Thank you
				callerName = $(caller).attr("id");
				
				if(!$("#"+callerName).val()) {
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules[rules[i]].alertText+"<br />";
				}
			}
		}
		function _customRegex(caller,rules,position){		 // VALIDATE REGEX RULES
			customRule = rules[position+1];
			pattern = eval($.validationEngine.settings.allrules[customRule].regex);
			
			var value = $(caller).attr('value');
			
			if(value.length > 0 && !pattern.test(value)){
				$.validationEngine.isError = true;
				promptText += $.validationEngine.settings.allrules[customRule].alertText+"<br />";
			}
		}
		function _ajax(caller,rules,position){				 // VALIDATE AJAX RULES
			
			customAjaxRule = rules[position+1];
			postfile = $.validationEngine.settings.allrules[customAjaxRule].file;
			fieldValue = $(caller).val();
			ajaxCaller = caller;
			fieldId = $(caller).attr("id");
			ajaxValidate = true;
			ajaxisError = $.validationEngine.isError;
			
			/* AJAX VALIDATION HAS ITS OWN UPDATE AND BUILD UNLIKE OTHER RULES */	
			if(!ajaxisError){
				$.ajax({
				   	type: "POST",
				   	url: postfile,
				   	async: true,
				   	data: "validateValue="+fieldValue+"&validateId="+fieldId+"&validateError="+customAjaxRule,
				   	beforeSend: function(){		// BUILD A LOADING PROMPT IF LOAD TEXT EXIST		   			
				   		if($.validationEngine.settings.allrules[customAjaxRule].alertTextLoad){
				   		
				   			if(!$("div."+fieldId+"formError")[0]){				   				
	 			 				return $(ajaxCaller).showPrompt($.validationEngine.settings.allrules[customAjaxRule].alertTextLoad,"load",false,$.validationEngine.showTriangle,$.validationEngine.settings.promptPosition);
	 			 			}else{
	 			 				$(ajaxCaller).updatePromptText($.validationEngine.settings.allrules[customAjaxRule].alertTextLoad,"load",false,$.validationEngine.settings.promptPosition);
	 			 			}
			   			}
			  	 	},
			  	 	error: function(data,transport){ $.validationEngine.debug("error in the ajax: "+data.status+" "+transport) },
					success: function(data){					// GET SUCCESS DATA RETURN JSON
						data = eval( "("+data+")");				// GET JSON DATA FROM PHP AND PARSE IT
						ajaxisError = data.jsonValidateReturn[2];
						customAjaxRule = data.jsonValidateReturn[1];
						ajaxCaller = $("#"+data.jsonValidateReturn[0])[0];
						fieldId = ajaxCaller;
						ajaxErrorLength = $.validationEngine.ajaxValidArray.length
						existInarray = false;
						
			 			 if(ajaxisError == "false"){			// DATA FALSE UPDATE PROMPT WITH ERROR;
			 			 	
			 			 	_checkInArray(false)				// Check if ajax validation alreay used on this field
			 			 	
			 			 	if(!existInarray){		 			// Add ajax error to stop submit		 		
				 			 	$.validationEngine.ajaxValidArray[ajaxErrorLength] =  new Array(2)
				 			 	$.validationEngine.ajaxValidArray[ajaxErrorLength][0] = fieldId
				 			 	$.validationEngine.ajaxValidArray[ajaxErrorLength][1] = false
				 			 	existInarray = false;
			 			 	}
				
			 			 	$.validationEngine.ajaxValid = false;
							promptText += $.validationEngine.settings.allrules[customAjaxRule].alertText+"<br />";
							$(ajaxCaller).updatePromptText(promptText,"",true,$.validationEngine.settings.promptPosition);				
						 }else{	 
						 	_checkInArray(true)
					
						 	$.validationEngine.ajaxValid = true; 						   
	 			 			if($.validationEngine.settings.allrules[customAjaxRule].alertTextOk){	// NO OK TEXT MEAN CLOSE PROMPT	 			
	 			 				$(ajaxCaller).updatePromptText($.validationEngine.settings.allrules[customAjaxRule].alertTextOk,"pass",true,$.validationEngine.settings.promptPosition);
 			 				}else{
				 			 	ajaxValidate = false;		 	
				 			 	$(ajaxCaller).closePrompt(false);
 			 				}		
			 			 }
				 			function  _checkInArray(validate){
				 				for(x=0;x<ajaxErrorLength;x++){
				 			 		if($.validationEngine.ajaxValidArray[x][0] == fieldId){
				 			 			$.validationEngine.ajaxValidArray[x][1] = validate
				 			 			existInarray = true;
				 			 		
				 			 		}
				 			 	}
				 			}
			 		}				
				});
			}
		}
		function _confirm(caller,rules,position){		 // VALIDATE FIELD MATCH
			confirmField = rules[position+1];
			value = $(caller).attr('value');
			confirmValue = $("#"+confirmField).attr('value');
			
			if(value.length > 0 || confirmValue.length > 0){
				if(value != confirmValue){
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules["confirm"].alertText+"<br />";
				}
			}
		}
		function _length(caller,rules,position){    	  // VALIDATE LENGTH
		
			startLength = eval(rules[position+1]);
			endLength = eval(rules[position+2]);
			feildLength = $(caller).attr('value').length;

			if(feildLength > 0 && feildLength<startLength || feildLength>endLength){
				$.validationEngine.isError = true;
				promptText += $.validationEngine.settings.allrules["length"].alertText+startLength+$.validationEngine.settings.allrules["length"].alertText2+endLength+$.validationEngine.settings.allrules["length"].alertText3+"<br />"
			}
		}
	    function _minLength(caller,rules,position){    // VALIDATE MIN LENGTH
            var minLength = eval(rules[position+1])
            var fieldLength = $(caller).attr('value').length
			if (fieldLength == 0) { // no value entered
				return;
			}
			if(fieldLength<minLength){
				$.validationEngine.isError = true
			    promptText += $.validationEngine.settings.allrules["minLength"].alertText;
				promptText += minLength;
				promptText += $.validationEngine.settings.allrules["minLength"].alertText2+"<br />";
			}
	    }
		function _maxLength(caller,rules,position){    // VALIDATE MAX LENGTH
			var maxLength = eval(rules[position+1])
			var fieldLength = $(caller).attr('value').length
			if (fieldLength == 0) { // no value entered
				return;
			}
			if(fieldLength>maxLength){
				$.validationEngine.isError = true
			    promptText += $.validationEngine.settings.allrules["maxLength"].alertText;
				promptText += maxLength;
				promptText += $.validationEngine.settings.allrules["maxLength"].alertText2+"<br />";
			}
		}
		function _minValue(caller,rules,position){    // VALIDATE MIN VALUE
			var minValue = eval(rules[position+1]);
			var fieldValue = $(caller).attr('value');
			if (fieldValue.length == 0) { // no value entered
				return;
			}
			if(fieldValue<minValue){
				$.validationEngine.isError = true
			    promptText += $.validationEngine.settings.allrules["minValue"].alertText+minValue+"<br />"
			}
		}
		function _maxValue(caller,rules,position){    // VALIDATE MAX VALUE			
			var maxValue = eval(rules[position+1]);
			var fieldValue= $(caller).attr('value');			
			if (fieldValue.length == 0) { // no value entered
				return;
			}
			if(fieldValue>maxValue){
				$.validationEngine.isError = true
			    promptText += $.validationEngine.settings.allrules["maxValue"].alertText+maxValue+"<br />"
			}
		}
		function _maxCheckbox(caller,rules,position){  	  // VALIDATE CHECKBOX NUMBER
		
			nbCheck = eval(rules[position+1]);
			groupname = $(caller).attr("name");
			groupSize = $("input[name='"+groupname+"']:checked").size();
			if(groupSize > nbCheck){	
				$.validationEngine.showTriangle = false
				$.validationEngine.isError = true;
				promptText += $.validationEngine.settings.allrules["maxCheckbox"].alertText+"<br />";
			}
		}
		function _minCheckbox(caller,rules,position){  	  // VALIDATE CHECKBOX NUMBER
		
			nbCheck = eval(rules[position+1]);
			groupname = $(caller).attr("name");
			groupSize = $("input[name='"+groupname+"']:checked").size();
			if(groupSize < nbCheck){	
			
				$.validationEngine.isError = true;
				$.validationEngine.showTriangle = false;
				promptText += $.validationEngine.settings.allrules["minCheckbox"].alertText+" "+nbCheck+" "+$.validationEngine.settings.allrules["minCheckbox"].alertText2+"<br />";
			}
		}
		function _validateCPF(caller,rules,position){  	  // VALIDATE CPF
			var cpf = $(caller).attr("value");
			if(cpf.length > 0){
				if(cpf.length != 11){
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules["validateCPF"].alertText+"<br />";
				}
				
				var pattern = eval("/^[0-9\ ]+$/");
				if(!pattern.test(cpf)){
					$.validationEngine.isError = true;
					promptText += $.validationEngine.settings.allrules["validateCPF"].alertText2+"<br />";
				}

				if(!$.validationEngine.isError){
					var value1 = 0;
					var value2 = 0;
					var base1 = 10;
					var base2 = 11;
					
					for(var i=0; i<10; i++){
						if(i<9){
							value1 += parseInt(cpf.charAt(i)) * base1;
							base1--;
						}
						value2 += parseInt(cpf.charAt(i)) * base2;
						base2--;
					}
					
					var calc1 = value1 % 11;
					var calc2 = value2 % 11;
					
					var digit1 = (calc1 < 2) ? 0 : (11 - calc1);
					var digit2 = (calc2 < 2) ? 0 : (11 - calc2);
					
					if(cpf=="00000000000" || cpf=="11111111111" || cpf=="22222222222" || cpf=="33333333333" 
						|| cpf=="44444444444" || cpf=="55555555555" || cpf=="66666666666" || cpf=="77777777777"
						|| cpf=="88888888888" || cpf=="99999999999" || cpf.charAt(9) != digit1 || cpf.charAt(10) != digit2){
						$.validationEngine.isError = true;
						promptText += $.validationEngine.settings.allrules["validateCPF"].alertText3+"<br />";
					}
				}

			}
		}
		
		function _requiredIf(caller,rules,position){
			var value = $(caller).attr("value");
			var requiredValue = $("#"+rules[position+1]).attr("value");
			if(requiredValue.length > 0 && value.length == 0){
				$.validationEngine.isError = true;
				$.validationEngine.showTriangle = false;
				var message = $.validationEngine.settings.allrules["requiredIf"].alertText;
				if(promptText.length == 0 || promptText.indexOf(message) == -1){
					promptText += message+"<br />";
				}
			}
		}
		return($.validationEngine.isError) ? $.validationEngine.isError : false;
	},
	debug : function(error) {
		if(!$("#debugMode")[0]){
			$("body").append("<div id='debugMode'><div class='debugError'><strong>This is a debug mode, you got a problem with your form, it will try to help you, refresh when you think you nailed down the problem</strong></div></div>")
		}
		$(".debugError").append("<div class='debugerror'>"+error+"</div>")
	},			
	submitValidation : function(caller,settings) {					// FORM SUBMIT VALIDATION LOOPING INLINE VALIDATION
		var stopForm = false;
		$.validationEngine.settings = settings
		$.validationEngine.ajaxValid = true
		$(caller).find(".formError").remove();
		var toValidateSize = $(caller).find("[class^=validate]").size();
		
		$(caller).find("[class^=validate]").each(function(){
			callerId = $(this).attr("id")
			if(!$("."+callerId+"formError").hasClass("ajaxed")){	// DO NOT UPDATE ALREADY AJAXED FIELDS (only happen if no normal errors, don't worry)
				var validationPass = $.validationEngine.loadValidation(this,settings);
				return(validationPass) ? stopForm = true : "";					
			}
		});
		ajaxErrorLength = $.validationEngine.ajaxValidArray.length		// LOOK IF SOME AJAX IS NOT VALIDATE
		for(x=0;x<ajaxErrorLength;x++){
	 		if($.validationEngine.ajaxValidArray[x][1] == false){
	 			$.validationEngine.ajaxValid = false
	 		}
	 	}
		if(stopForm || !$.validationEngine.ajaxValid){		// GET IF THERE IS AN ERROR OR NOT FROM THIS VALIDATION FUNCTIONS
			destination = $(".formError:not('.greenPopup'):first").offset().top;
			$("html:not(:animated),body:not(:animated)").animate({ scrollTop: destination}, 1100);
			return true;
		}else{
			return false
		}
	}
}	
})(jQuery);