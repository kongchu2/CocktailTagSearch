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
            } else {
                userData = data;
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


function likeTag(tagId) {
    if(userData.signed == "0") {
        return;
    }
    $.ajax({
        type:"post",
        url:"http://localhost:8090/CocktailTagSearch/AddTagLike",
        data: {
            tagId: tagId,
            userId:userData.user.id
        },
        success: function(data) {
            if(data.isLiked == "0") {
                alert("좋아하는 태그에서 삭제되었습니다.");
            } else {
                alert("좋아하는 태그에 추가되었습니다.");
            }
        }
    });
}