var func;
var RSAModulus = null;
var RSAExponent = null;

$(document).ready(function() {
	getKey();
});

$('.submitBtn').on('click', auth);
	
function getKey() {
	$.ajax({
        type:"post",
        url:"GetRsaKey",
		dataType:"json",
        success:function(data) {
          RSAModulus = data.RSAModulus;
		  RSAExponent = data.RSAExponent;
        }
    });
}

function auth() {
    if($('#pw').val() === "") {
        alert("비밀번호가 비어 있습니다.");
        return;
    }
	var rsa = new RSAKey();
	rsa.setPublic(RSAModulus,RSAExponent);
	
	var pw = $("#pw");
	var encrypt_pw = rsa.encrypt(pw.val());
	 
    $.ajax({
        type:"post",
        url:"PasswordAuth",
        data: {
            fXVA92VbkUVvHGNq: encrypt_pw
        },
        success: function(data) {
            if(data === "1") {
                $('#PasswordAuthContents').remove();
                func();
            } else {
                alert("비밀번호가 틀렸습니다.");
            }
        }
    });
}