DataLoadFunc.push(function() {
    if(userData.signed === "1") {
        location.href = "index.html";
    }
}); 

function valueCheck() {
    inputs = [
        {value: $('#id').val(), name: "아이디"},
        {value: $('#pw').val(), name: "비밀번호"}
    ]
    $(inputs).each(function(index, item) {
        if(item.value === "") {
            alert(item.name + "가 비어 있습니다.");
            return false;
        }
    });
    return true;
}

function login() {
    if(valueCheck()) {
        $.ajax({
            type:"post",
            url:"http://localhost:8090/CocktailTagSearch/Login",
            data: {
                id: $('#id').val(),
                pw: $('#pw').val()
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