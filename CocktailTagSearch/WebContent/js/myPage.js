var userData = {signed: "0"};

$(document).ready(getSessionData);

function getSessionData() {
  $.ajax({
    type:"post",
	url:"http://localhost:8090/CocktailTagSearch/FavoriteData",
	dataType: "json",
	error : function(error) {
        console.log(error);
    },
    success: function(data) {
	
	if(data != null) {
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
	    }
	}
	
  });
}