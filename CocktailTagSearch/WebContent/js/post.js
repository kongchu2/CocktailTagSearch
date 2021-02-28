DataLoadFunc.push(getLikeData);
DataLoadFunc.push(addDeleteBtn);

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
        path = "image/icon/filledHeart.png";
    } else {
        path = "image/icon/notFilledHeart.png";
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

function addDeleteBtn() {
    if(userData.user.permission === 1) {
        $('#postTitleContents').append("<div id='deleteBtn' onclick='deleteCocktail()'>칵테일 삭제하기</div>");
    }
}
function deleteCocktail() {
    if(userData.user.permission === 1 && confirm("정말 삭제하시겠습니까?")) {
        $('#postContents').empty();
        $('#postContents').load("PasswordAuth.html", function() {
            func = function() {
                $.ajax({
                    type:"post",
                    url:"http://localhost:8090/CocktailTagSearch/DeleteCocktail",
                    data: {
                        cocktailId: postId
                    },
                    success:function(data) {
                        if(data.isDeleted === "1") {
                            alert("삭제되었습니다.");
                            location.href = "index.html";
                        } else {
                            alert("삭제에 실패했습니다.");
                            location.reload();
                        }
                    }
                });
            } 
        });
    }
}