var RSAModulus = null;
var RSAExponent = null;
	
DataLoadFunc.push(function() {
    if(userData.signed === "1") {
        location.href = "index.html";
    }
}); 
  
$(document).ready(function() {
    getSessionData();
	getKey();
});
	
$('.submitBtn').on('click', sign_up);

function getKey() {
	$.ajax({
        type:"post",
        url:"/CocktailTagSearch/GetRsaKey",
		dataType:"json",
        success:function(data) {
          RSAModulus = data.RSAModulus;
		  RSAExponent = data.RSAExponent;
        }
    });
}

function checkValue() {
	inputs = [
        {value: $('#id').val(), name: "아이디"},
        {value: $('#name').val(), name: "닉네임"},
        {value: $('#pw').val(), name: "비밀번호"}
    ]
	var bool = true;
    $(inputs).each(function(index, item) {
        if(item.value === "") {
            alert(item.name + "(이)가 비어 있습니다.");
            return bool = false;
        }
    });
	if(!bool) return bool;
	
	if($('#pw').val() != $('#pwCheck').val()) {
		alert("비밀번호 확인과 비밀번호가 동일하지 않습니다.");
		return false;
	}
	
	return true;
}

function sign_up() {
    if(checkValue()) {
		var rsa = new RSAKey();
		rsa.setPublic(RSAModulus,RSAExponent);
		
		var id = $("#id");
		var pw = $("#pw");
		var name = $("#name");
		var encrypt_id = rsa.encrypt(id.val());
		var encrypt_pw = rsa.encrypt(pw.val());
		var encrypt_name = rsa.encrypt(name.val());
		
        $.ajax({
            type:"post",
            url:"/CocktailTagSearch/SignUp",
            data: {
                df8Z368CKkFDNHk7: encrypt_id,
                tFw9C8dV2KGBhbrY: encrypt_name,
                wGKnr4ppPF8rBPss: encrypt_pw
            },
			success:function(data) {
              location.href = "index.html";
            }
        });
    }
}