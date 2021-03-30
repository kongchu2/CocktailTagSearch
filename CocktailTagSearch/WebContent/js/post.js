DataLoadFunc.push(getLikeData);
DataLoadFunc.push(addAdminBtn);

$('#likeimg').click(likePost);

var postId = getParam("id");

$(document).ready(function() {
    $("#menuContents").load("menuContents.html", function() {
        getSessionData();
    });
    getCocktailData();
});

function getParam(sname) {
    var params = location.search.substr(location.search.indexOf("?") + 1);
    var sval = "";
    params = params.split("&");
    for (var i = 0; i < params.length; i++) {
        temp = params[i].split("=");
        if ([temp[0]] == sname) { 
            sval = temp[1];
        }
    }
    return sval;
}

function getCocktailData() {
    $.ajax({
        type:"GET",
        url:"/CocktailTagSearch/GetCocktail",
        data: {
            id:postId
        },
        success: function(data) {
            setCocktailData(data);
        },
        error : function(data) { console.log(data)}
    });
}

function setCocktailData(data) {
    $("title").text(data.name);
    $(".postTitle").text(data.name);
    $(".post-image-container > img").attr("src", "image/cocktail/"+data.image);
    $(".description").text(data.desc);
    $(".history").text(data.history);
    $(".taste").text(data.taste);
    $(".base").text(data.base);
    $(".build").text(data.build);
    $(".glass").text(data.glass);
    $(data.tags).each(function(index, item) {
        tag = "<a desc='"+item.desc+"' href='javascript:likeTag("+item.id+")'><div tagId='"+item.id+"' class='tagBlock "+item.category+"'>"+item.name+"</div></a>";
        $("#postTagContents").append(tag);
    });
}

function likePost() {
    if(userData.signed == "0") {
        alert("로그인 후 사용 가능한 기능입니다.");
        return;
    }
    $.ajax({
        type:"post",
        url:"/CocktailTagSearch/AddPostLike",
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
        url:"/CocktailTagSearch/GetPostLike",
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
        alert("로그인 후 사용 가능한 기능입니다.");
        return;
    }
    $.ajax({
        type:"post",
        url:"/CocktailTagSearch/AddTagLike",
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

function addAdminBtn() {
    if(userData.user.permission === 1) {
        $('#postAdminBtnContents').append("<div class='postAdminBtn' onclick='editCocktail()'>칵테일 수정하기</div>");
        $('#postAdminBtnContents').append("<div class='postAdminBtn' onclick='deleteCocktail()'>칵테일 삭제하기</div>");
    }
}
function deleteCocktail() {
    if(userData.user.permission === 1 && confirm("정말 삭제하시겠습니까?")) {
        $('#postContents').empty();
        $('#postContents').load("passwordAuthor.html", function() {
            func = function() {
                $.ajax({
                    type:"post",
                    url:"/CocktailTagSearch/DeleteCocktail",
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

function editCocktail() {
    if(userData.user.permission === 1) {
        cocktail = {
            id: postId,
            name: $(".postTitle").text(),
            image: $(".cocktailImages").attr("src"),
            desc: $(".description").text(),
            history: $(".history").text(),
            taste: $(".taste").text(),
            base: $(".base").text(),
            build: $(".build").text(),
            glass: $(".glass").text()
        }
        tagList = []
        $('.tagBlock').each(function(index, item) {
            tag = {
                name: $(item).text(),
                id: $(item).attr("tagId")
            }
            tagList.push(tag);
        });
        $('#postContents').load("passwordAuthor.html", function() {
            func = function() {
                $("#postContents").empty();
                editItemsDiv = $("<div/>");
                editItemsDiv.attr("id", "editItems");
                editItemsDiv.css("width", "100%");
                editItemsDiv.css("margin", "20px");
                for(let key in cocktail) {
                    if(key === "id") {
                        continue;
                    }
                    value = cocktail[key];

                    label = $("<label/>");
                    label.text(key);
                    label.attr("for", key);

                    input = $("<textarea/>");
                    input.css("display", "block");
                    input.css("width", "95%");
                    input.attr("class", key);
                    input.attr("placeholder", key);
                    input.val(value.trim());

                    $(input).on('input', adjustHeight);

                    label.append(input);
                    $(editItemsDiv).append(label);
                }

                taghtml = '<div id="tagEditContent" class="border"><div id="tagEditSearch" class="inline-block"><input type="text" id="tagSearch"><div id="editAutocompleteTagList"></div></div><div id="addedTagList" class="inline-block"></div></div>'

                $(editItemsDiv).append(taghtml);

                $("#postContents").append(editItemsDiv);

                $(tagList).each(function(index, item) {
                    addedTag = $("<div/>");
                    addedTag.attr("class", "addedTag");
                    addedTag.attr("cocktailId", item.id);
                    addedTag.text(item.name);
                    deleteBtn = $("<span/>");
                    deleteBtn.attr("class", "deleteBtn");
                    deleteBtn.text("X");
                    $(deleteBtn).on("click", function() {
                        $(this).parent().remove();
                    });
                    addedTag.append(deleteBtn);
                    $('#addedTagList').append(addedTag);
                });

                $('#tagSearch').on("input", function() {
                    getTagIdList = function() {
                        tagIdList = [];
                        $('.addedTag').each(function(index, item) {
                            tagIdList.push(
                                {
                                    name:$(item).text(),
                                    id:parseInt($(item).attr("cocktailId"))
                                }
                            );
                        });
                        return tagIdList;
                    };
                    $.ajax({
                        type:"post",
                        url:"/CocktailTagSearch/TagSearch",
                        dataType:"json",
                        data: {
                            search: $("#tagSearch").val(),
                            tags: JSON.stringify(getTagIdList())
                        },
                        success:function(data) {
                            $("#editAutocompleteTagList").html("");
                            autocompleteTagList = [];
                            $.each(data.tags, function(index, item) {
                                autoTag = $("<div/>");
                                autoTag.attr("class", "editAutocompleteTag");
                                autoTag.attr("cocktailId", item.id);
                                autoTag.text(item.name);
                                $(autoTag).on("click", function() {
                                    myId = $(this).attr("cocktailId");
                                    myName = $(this).text();

                                    addedTag = $("<div/>");
                                    addedTag.attr("class", "addedTag");
                                    addedTag.attr("cocktailId", myId);
                                    addedTag.text(myName);

                                    deleteBtn = $("<span/>");
                                    deleteBtn.attr("class", "deleteBtn");
                                    deleteBtn.text("X");

                                    $(deleteBtn).on("click", function() {
                                        $(this).parent().remove();
                                    });

                                    addedTag.append(deleteBtn);
                                    $(this).remove();
                                    $("#addedTagList").append(addedTag);
                                });
                                $("#editAutocompleteTagList").append(autoTag);
                            });
                        }
                    });
                });

                submit = $("<div/>");
                submit.attr("class", "submitBtn");
                submit.css("margin", "auto");
                submit.text("제출");
                $(submit).on('click', function() {//change 된 것만 보내기
                    cocktail.name = $('textarea.name').val(),
                    cocktail.image = $('textarea.image').val(),
                    cocktail.desc = $('textarea.desc').val(),
                    cocktail.history = $('textarea.history').val(),
                    cocktail.taste = $('textarea.taste').val(),
                    cocktail.base = $('textarea.base').val(),
                    cocktail.build = $('textarea.build').val(),
                    cocktail.glass = $('textarea.glass').val()
                    addedTagList = [];
                    $('.addedTag').each(function(index, item) {
                        addedTagList.push(
                            {id:parseInt($(item).attr("cocktailId")), 
                            name:$(item).text().replace("X", "")}
                        );
                    });
                    $.ajax({
                        type:"post",
                        url:"/CocktailTagSearch/UpdateCocktail",
                        data: {
                            cocktail: JSON.stringify(cocktail),
                            tag:JSON.stringify(addedTagList)
                        },
                        success:function(data) {
                            if(data.isUpdated == "1") {
                                alert("수정되었습니다.");
                            } else {
                                alert("수정에 실패하였습니다.");
                            }
                            location.reload();
                        }
                    });
                });
                $('#postContents').append(submit);
            }
        });
    }
}
function adjustHeight() {
    var textarea = $(this);
    textarea.css('height', "auto");
    var textEleHeight = textarea.prop('scrollHeight');
    textarea.css('height', textEleHeight);
}