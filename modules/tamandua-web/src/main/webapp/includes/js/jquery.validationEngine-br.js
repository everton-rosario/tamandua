

(function($) {
	$.fn.validationEngineLanguage = function() {};
	$.validationEngineLanguage = {
		newLang: function() {
			$.validationEngineLanguage.allRules = {"required":{    
				"regex":"none",
				"alertText":"* Campo obrigatório",
				"alertTextCheckboxMultiple":"* Selecione uma opção",
				"alertTextCheckboxe":"* É preciso marcar"},
			"length":{
				"regex":"none",
				"alertText":"* Entre ",
				"alertText2":" e ",
				"alertText3": " caracteres"},
			"minLength":{
                "regex":"none",
                "alertText":"* O campo deve ter no mínimo ",
                "alertText2":" caracteres"},
			"maxLength":{
                "regex":"none",
                "alertText":"* O campo deve ter no máximo ",
                "alertText2":" caracteres"},
			"minValue":{
                "regex":"none",
                "alertText":"* O valor do campo deve ser menor do que "},
			"maxValue":{
                "regex":"none",
                "alertText":"* O valor do campo deve ser maior do que "},
			"maxCheckbox":{
				"regex":"none",
				"alertText":"* Muitos campos marcados"},	
			"minCheckbox":{
				"regex":"none",
				"alertText":"* Selecione ",
				"alertText2":" opções"},	
			"confirm":{
				"regex":"none",
				"alertText":"* Confirmação inválida"},		
			"telephone":{
				"regex":"/^[0-9]{10}$/",
				"alertText":"* Número de telefone com código, somente números"},	
			"email":{
				"regex":"/^[a-zA-Z0-9_\.\-]+\@([a-zA-Z0-9\-]+\.)+[a-zA-Z0-9]{2,4}$/",
				"alertText":"* Endereço de email inválido"},	
			"date":{
	            "regex":"/^[0-9]{1,2}\-\[0-9]{1,2}\-\[0-9]{4}$/",
	            "alertText":"* Data inválida, deve ser no formato DD-MM-AAAA"},
			"onlyNumber":{
				"regex":"/^[0-9\ ]+$/",
				"alertText":"* Apenas números"},	
			"noSpecialCaracters":{
				"regex":"/^[0-9a-zA-Z]+$/",
				"alertText":"* Caracteres especiais não permitidos"},	
			"onlyLetter":{
				//"regex":"/^[a-zA-ZáéíóúÁÉÍÓÚãõç' ']+$/",
				"regex":"/^[a-zA-ZáéíóúÁÉÍÓÚãõç' ']+$/",
				"alertText":"* Apenas letras"},
			"cep":{
				"regex":"/^[0-9]{5}\-\[0-9]{3}$/",
				"alertText":"* Entre o CEP no formato 00000-000"},
			"validateCPF":{
				"regex":"none",
				"alertText":"* O CPF deve ter 11 caracteres",
				"alertText2":"* Apenas números",
				"alertText3":"* CPF inválido"},
			"requiredIf":{
				"regex":"none",
				"alertText":"* Campo obrigatório"}
			}
		}
	}
})(jQuery);

$.validationEngineLanguage.newLang();