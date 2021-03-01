var func;

function auth() {
    if($('#pw').val() === "") {
        alert("비밀번호가 비어 있습니다.");
        return;
    }
    $.ajax({
        type:"post",
        url:"http://localhost:8090/CocktailTagSearch/PasswordAuth",
        data: {
            pw: $('#pw').val()
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