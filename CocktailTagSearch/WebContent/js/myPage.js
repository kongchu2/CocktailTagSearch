$(document).ready(getUserData);

function getUserData() {
  $.ajax({
    type:"post",
	url:"http://localhost:8090/CocktailTagSearch/FavoriteData",
	dataType: "json",
    success: function(data) {
	  if(data != null) {
	    if(data.user != null) {
	      $('.myPageName').text(data.user.name);
	      $('.myPageId').text(data.user.login_id);
	      $.each(data.cocktail, function(index, item) {
	        var cocktail = $('<div/>');
	        cocktail.addClass('myPageCocktails');
		    cocktail.attr('desc', item.name);
		    cocktail.text(item.name);
		    cocktail.on('click', function() {
	          var url = "Cocktail_post.jsp?id="+item.id;
		      $(location).attr('href', url);
		    });
	          $('#myPageCocktailContents').append(cocktail);
          });
          $.each(data.tag, function(index, item) {
	        $('#myPageTagContents').append($('<div/>', {
              class: "myPageTags",
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
	}
	
  });
}

function deleteUser() {
	if(confirm("탈퇴하시겠습니까? 취소할 수 없습니다.")) {
		$.ajax({
			type:"post",
			url:"http://localhost:8090/CocktailTagSearch/deleteUser",
			success: function(data) {
				alert("탈퇴되었습니다.");
				logout();
				location.href = "index.html";
			}
		});
	}
}