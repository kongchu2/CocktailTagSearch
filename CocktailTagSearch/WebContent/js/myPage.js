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
			$('#myPageCocktailContents').append($('<div/>', {
		      class: "myPageCocktails",
		      text: item.name
		    }));
		  });
		  $.each(data.tag, function(index, item) {
			$('#myPageTagContents').append($('<div/>', {
		      class: "myPageTags",
		      text: item.name
		    }));
		  });
	    }
	}
	
  });
}