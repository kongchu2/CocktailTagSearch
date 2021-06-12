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

$('.submitBtn').on('click', login);
	
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

function checkValue() {
    inputs = [
        {value: $('#id').val(), name: "아이디"},
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
    return true;
}

function login() {
    if(checkValue()) {
		var rsa = new RSAKey();
		rsa.setPublic(RSAModulus,RSAExponent);
		
		var id = $("#id");
		var pw = $("#pw");
		var encrypt_id = rsa.encrypt(id.val());
		var encrypt_pw = rsa.encrypt(pw.val());
		
        $.ajax({
            type:"post",
            url:"Login",
            data: {
                evyv6StCkKAvwEDu: encrypt_pw,
                HxxKFRVJZqxcft8V: encrypt_id
            },
            success:function(data) {
                if(data === "wrong") {
                    alert("비밀번호 또는 아이디가 틀렸습니다.");
                } else if(data === "right") {
                    location.href = "index.html";
                }
            }
        });
    }
}