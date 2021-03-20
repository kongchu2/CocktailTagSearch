var removeToggle = false;

$(document).ready(getUserData);
$(document).ready(function() {
  $('.myPageSelectRemove').attr('data-content', '선택 삭제하기');
});
DataLoadFunc.push(addAdminBtn);

$(document).ready(function() {
    $("#menuContents").load("menuContents.html");
    getSessionData();
});

function getUserData() {
  $.ajax({
    type:"post",
	url:"/CocktailTagSearch/GetFavoriteData",
	dataType: "json",
    success: function(data) {
	console.log(1);
	  if(data != null) {
	    if(data.favorite.user != null) {
		console.log(data.favorite.user.name);
	      $('.myPageName').text(data.favorite.user.name);
	      $('.myPageId').text(data.favorite.user.login_id);
		
	      $.each(data.favorite.cocktail, function(index, item) {
	        var cocktail = $('<div/>');
	        cocktail.addClass('myPageCocktails');
		    cocktail.attr('id', item.id);
		    cocktail.attr('desc', item.desc);
		    cocktail.text(item.name);
		    cocktail.on('click', function() {
	          var url = "cocktailPost.html?id="+item.id;
		      $(location).attr('href', url);
		    });
	          $('#myPageCocktailContents').append(cocktail);
          });

          $.each(data.favorite.tag, function(index, item) {
	        $('#myPageTagContents').append($('<div/>', {
              class: "myPageTags",
	          id: item.id,
	          desc: item.desc,
              text: item.name
            }));
          }); 
	    } else {
	      alert("로그인 시간이 만료되었습니다.");
		  logout();
		  location.href = "index.html";
	    }
	  }
	},
	error:function(error) {
		console.log(error);
	}
	
  });
}

$('.myPageSelectRemove').on('click', function() {
	if(removeToggle) {
	  removeSelectedFavorite();	  
	  cancelSelectedFavorite();
	} else {
	  selectedFavorite();
	}
	
	removeToggle = !removeToggle;
});

function selectedFavorite() {
  $('.myPageSelectRemove').attr('data-content', '결정');
  $('.myPageCocktails').off('click');
  $('#myPageCocktailContents').on('click', selectToggle);
  $('#myPageTagContents').on('click', selectToggle); 
	
}

function cancelSelectedFavorite() {
  $('.myPageSelectRemove').attr('data-content', '선택 삭제하기');
  $('#myPageCocktailContents').off('click', selectToggle);
  $('#myPageTagContents').off('click', selectToggle);
  
  $('.myPageTags').removeClass('.selected');
  $('.myPageCocktails').removeClass('.selected');

  $.each($('.myPageCocktails'), function(index, item) {
	$(this).on('click', function() {
	  var url = "Cocktail_post.jsp?id="+$(this).attr('id');
	  $(location).attr('href', url);
	});
  });
}

function removeSelectedFavorite() {
  if($('.selected').length == 0) return;
  
  var removeCocktail = [];
  $.each($('#myPageCocktailContents').find('.selected'), function(index, item) {
    removeCocktail.push({name: item.textContent, id: Number.parseInt($(this).attr('id'))});
  });
  
  var removeTag = [];
  $.each($('#myPageTagContents').find('.selected'), function(index, item) {
    removeTag.push({name: item.textContent, id: Number.parseInt($(this).attr('id'))});
  });

  $.ajax({
    type:"post",
	url:"/CocktailTagSearch/RemoveFavoriteData",
	dataType: "json",
	data: {
		cocktail: JSON.stringify(removeCocktail),
		tag: JSON.stringify(removeTag)
	},
    success: function(data) {
	  if(data != null) {
	    $('#myPageProfileContents').find('.selected').remove();
	  }
	}
  });
}

function selectToggle(e) {
  var target = $(e.target);
  if(e.target === e.currentTarget) return;
  if(target.hasClass('myPageFavoriteTitle')) return;
  target.toggleClass('selected');
}

function addAdminBtn() {
	if(userData.user.permission === 1) {
		$('#myPageContents').append("<a href='addCocktail.html'><div class='myPageAdminBtn'>칵테일 추가하기</div></a>");
		$('#myPageContents').append("<a href='addTag.html'><div class='myPageAdminBtn'>태그 추가하기</div></a>");
	}
}