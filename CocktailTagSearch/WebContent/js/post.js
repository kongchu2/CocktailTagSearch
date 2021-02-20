var userData = {signed: "0"};

$(document).ready(getSessionData);

$('#likeimg').click(likePost);

function likePost() {
    if(userData.signed == "0") {
        return;
    }
    $.ajax({
        type:"post",
        url:"http://localhost:8090/CocktailTagSearch/AddPostLike",
        data: {
            cocktailId: postId,
            userId: userData.user.id
        },
        success: function(data) {
            setLikeIcon(data.isLiked == "1");
        }
      });
}

function getSessionData() {
    $.ajax({
        type:"post",
        url:"http://localhost:8090/CocktailTagSearch/SessionData",
        success: function(data) {
            if(data.signed == "0") {
                userData = data;
            var html = "<a href='login.html'>로그인</a>";
            $('#sign').append(html);
            } else {
                userData = data;
                var html = "<p>" + data.user.name + "님 안녕하세요.</p>\n<a href='javascript:logout()'>로그아웃</a>";
                $('#sign').append(html);
                getLikeData();
            }
        }
    });
}

function getLikeData() {
    if(userData.signed == "0") {
        return;
    }
    $.ajax({
        type:"post",
        url:"http://localhost:8090/CocktailTagSearch/GetPostLike",
        data: {
            cocktailId: postId,
            userId:userData.user.id
        },
        success: function(data) {
            setLikeIcon(data.isLiked == "1");
        }
    });
}

function setLikeIcon(isLiked) {
    var path = "";
    if(isLiked) {
        path = "image/filledHeart.png";
    } else {
        path = "image/notFilledHeart.png";
    }
    $('#likeimg').attr('src', path);
}