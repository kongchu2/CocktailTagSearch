var removeToggle = false;

$(document).ready(getUserData);
$(document).ready(function() {
  $('.myPageSelectRemove').attr('data-content', 'Select remove');
});

DataLoadFunc.push(addAdminBtn);

$(document).ready(function() {
    $("#menuContents").load("menuContents.html");
    getSessionData();
});

$('.myPageCocktailTitle , .myPageTagTitle').on('click', function(e) {
	const target = $(e.currentTarget);
	target.toggleClass('myPageTitleClick');
	var node = target.parent().children("div:not(.myPageFavoriteTitle)");
	
	node.toggleClass("myPageFavoriteClick");
	if(node.hasClass("myPageFavoriteClick")) {
		node.css("display", "block");
	}
	else { 
		setTimeout(function() { 
		  node.css("display", "none");
		}, 400);
	}
});

function getUserData() {
  $.ajax({
    type:"post",
	url:"GetFavoriteData",
	dataType: "json",
    success: function(data) {
	  if(data.favorite != null) {
	    if(data.favorite.user != null) {
	      $('.myPageName').text(data.favorite.user.name);
	      $('.myPageId').text(data.favorite.user.login_id);
		
	      $.each(data.favorite.cocktail, function(index, item) {
	        var cocktail = $('<div/>');
	        cocktail.addClass('myPageCocktails');
		    cocktail.attr('id', item.id);
		    cocktail.text(item.name);
			cocktail.css("animation-delay", (index*100)+700+"ms");
			cocktail.css("display", "none");
		    cocktail.on('click', function() {
	          var url = "cocktailPost.html?id="+item.id;
		      $(location).attr('href', url);
		    });
	          $('#myPageCocktailContents').append(cocktail);
          });

          $.each(data.favorite.tag, function(index, item) {
			var tag = $('<div/>');
	        tag.addClass('myPageCocktails');
		    tag.attr('id', item.id);
		    tag.text(item.name);
			tag.css("animation-delay", (index*100)+700+"ms");
			tag.css("display", "none");
	        $('#myPageTagContents').append(tag);
          }); 
	    } 
	  } else {
	      alert("로그인 시간이 만료되었습니다.");
		  logout();
		  location.href = "index.html";
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

$('.myPageEditProfile').on('click', function() {
	location.href = "updateProfile.html";
});

$('.myPageDeleteProfile').on('click', function() {
	location.href = "deleteUser.html";
});

function selectedFavorite() {
  $('.myPageSelectRemove').attr('data-content', 'Done');
  $('.myPageCocktails').off('click');
  $('#myPageCocktailContents').on('click', selectToggle);
  $('#myPageTagContents').on('click', selectToggle); 
	
}

function cancelSelectedFavorite() {
  $('.myPageSelectRemove').attr('data-content', 'Select remove');
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
	url:"RemoveFavoriteData",
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
		$('.function-inner-container').append("<a href='addCocktail.html'><div class='myPageAdminBtn'>칵테일 추가하기</div></a>");
		$('.function-inner-container').append("<a href='addTag.html'><div class='myPageAdminBtn'>태그 추가하기</div></a>");
	}
}