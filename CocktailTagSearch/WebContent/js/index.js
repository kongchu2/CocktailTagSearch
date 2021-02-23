const hover = document.querySelectorAll(".cocktailItems");
const hoverSize = hover.length;
const search = document.querySelector("#searchText");
const autocomplete = document.querySelector("#autocompleteTagsContents");
var autocompleteTag = document.querySelectorAll(".autocompleteTags");

var autocompleteTagList = [];
var selectTagList = [];

var save_search_sentence = "";

var add_continuous_pressing = false;
var remove_continuous_pressing = false;

$(document).ready(loadData);

search.addEventListener("keyup", showNotFound);
search.addEventListener("keydown", hideComplete);
search.addEventListener("input", getAutocompleteTags);
search.addEventListener('input', getCocktailItems);

search.addEventListener("focus", function() {
  autocomplete.style.display = "block";
});

search.addEventListener("focusout", function() {
  autocomplete.style.display = "none";
});

// 모든 칵테일이 표시가 안될 때 Not Found 표시
function showNotFound() {
  if($('.cocktailItems').length < 0) {
    document.querySelector(".cocktailNotFound").style.display = "flex";
  } else {
    document.querySelector(".cocktailNotFound").style.display = "none";
  }
}

// 자동완성 미리보기 감추기
function hideComplete() {
  if(search.style.color == "gray") {
    search.value = save_search_sentence;
    search.style.color = "black";
  }
}

// 자동완성 미리보기 표시하기
function showComplete() {
  save_search_sentence = search.value;
  search.value = this.textContent;
  search.style.color = "gray";
}

// 자동완성 창 숨기기
function hideAutocomplete() {
  autocomplete.style.display = "none";
}

// 칵테일의 제목 및 태그 숨기기
function styleAppendOver() {
    const tag = this.querySelectorAll(".itemTags");
    const title = this.querySelector(".itemTitle");

    for(var j=0; j<tag.length; j++) {
        tag[j].style.visibility = "visible";
    }
    title.style.visibility = "visible";
}

// 칵테일의 제목 및 태그 표시하기
function styleAppendOut() {
    const tag = this.querySelectorAll(".itemTags");
    const title = this.querySelector(".itemTitle");

    for(var j=0; j<tag.length; j++) {
        tag[j].style.visibility = "hidden";
    }
    title.style.visibility = "hidden";
}

function addTag() {
  if(add_continuous_pressing) {
	return;
  }

  var value, input;
  var tag = document.createElement("div");
  tag.setAttribute("class", "searchTags"); 
  tag.addEventListener("click", removeTag);

  value = this.textContent.toUpperCase();
  tag.innerText = value;
  input = document.getElementById("searchTagsContents");

  input.appendChild(tag);

  for(var i=0;i<autocompleteTagList.length;i++) {
    if(value === autocompleteTagList[i].name) {
      selectTagList.push(autocompleteTagList[i]);
      break;
    }
  }

  this.parentNode.removeChild(this);

  setTimeout(function() {  
    search.value = "";
	getFavoriteTags();
	getAutocompleteTags();
	getCocktailItems();
    showNotFound();
  }, 0.001);
  
  add_continuous_pressing = true;
  setTimeout(function() { add_continuous_pressing = false; }, 1000);
}

function removeTag() {
  if(remove_continuous_pressing) {
	return;
  }
  var thisName = this.textContent;
    $.each(selectTagList, function(index, item) {
      if(item.name === thisName) {
        selectTagList.splice(index, 1);
		return false;
      }
    })
    this.parentNode.removeChild(this);
	
	getFavoriteTags();
	getAutocompleteTags();
	getCocktailItems();
    showNotFound();	

  remove_continuous_pressing = true;
  setTimeout(function() { remove_continuous_pressing = false; }, 1000);

}

function getAutocompleteTags() {
  $.ajax({
		type:"post",
		url:"http://localhost:8090/CocktailTagSearch/TagSearch",
		dataType:"json",
		data: {
		  search: $("#searchText").val(),
	      tags: JSON.stringify(selectTagList)
		},
		success:function(data) {
			if(data === "")
			  return;
      $("#autocompleteTagsContents").html("");
      autocompleteTagList = [];
      $.each(data.tags, function(index, item) {
        createTag(index, item);
        autocompleteTagList.push({name:item.name, id:item.id});
      });

	    
	autocompleteTag = document.querySelectorAll(".autocompleteTags");
			$.each(autocompleteTag, function(index, item) {
        item.addEventListener("mouseover", showComplete);
	      item.addEventListener("mouseout", hideComplete);
        item.addEventListener("mousedown", addTag);
      });
	  }   
  });
}

function getAllTags() {
  $.ajax({
		type:"post",
		url:"http://localhost:8090/CocktailTagSearch/TagSearch",
		dataType:"json",
		data: {
		  search: $("#searchText").val(),
	      tags: JSON.stringify(selectTagList)
		},
		success:function(data) {
			if(data === "")
			  return;
      $("#autocompleteTagsContents").html("");
      autocompleteTagList = [];
      $.each(data.tags, function(index, item) {
        createTag(index, item);
        autocompleteTagList.push({name:item.name, id:item.id});
      });

	    
	autocompleteTag = document.querySelectorAll(".autocompleteTags");
			$.each(autocompleteTag, function(index, item) {
        item.addEventListener("mouseover", showComplete);
	      item.addEventListener("mouseout", hideComplete);
        item.addEventListener("mousedown", addTag);
      });
	  }   
  });
}

function getCocktailItems() {
  $.ajax({
    type:"post",
		url:"http://localhost:8090/CocktailTagSearch/CocktailSearch",
    dataType:"json",
		data: {
		  search: $("#searchText").val(),
	      tags: JSON.stringify(selectTagList)
		},
    success:function(data) {
      $('.cocktailItems').remove();
	    if(data != null) {
	    $.each(data.cocktails, createCocktail);
		  $('.cocktailItems').each(function(index, item) {
            item.addEventListener("mouseover", styleAppendOver);
            item.addEventListener("mouseout", styleAppendOut);
          });
	    }
	  }
  });
}

function getFavoriteTags() {
	var favoriteTags = [];
	$.each($('.favoriteTags'), function(index, item) {
		favoriteTags.push(item.textContent);
	});
	
	$.ajax({
    type:"post",
	url:"http://localhost:8090/CocktailTagSearch/FavoriteTagData",
	data: {
		love: JSON.stringify(favoriteTags),
	    tags: JSON.stringify(selectTagList)
	},
	dataType: "json",
	error : function(error) {
        console.log(error);
    },
    success: function(data) {
	  if(data != null && data.tag != null) {
		if(data.remove) {
			$('#favoriteTagContents').empty();
		}
		$('#favoriteTagContents').css('visibility', 'visible');
		$.each(data.tag, function(index, item) {
			$('#favoriteTagContents').prepend($('<div/>', {
		      class: "favoriteTags",
			  desc: item.desc,
		      text: item.name
		    }));
		  });
		  $('.favoriteTags').each(function(index, item) {
            item.addEventListener("mousedown", addTag);
		  });
	    }
	  else {
		$('#favoriteTagContents').css('visibility', 'hidden');
	  }
	}
  });
}

function loadData() {
	getAutocompleteTags();
	getCocktailItems();
	getFavoriteTags();
}

function createTag(index, item) {
	$('#autocompleteTagsContents').append($('<div/>', {
      class: "autocompleteTags",
      text: item.name
    }));
	var limit = 5;
	if(index+1 > limit) {
		$('.autocompleteTags:nth-child('+(index+1)+')').css('display', 'none');
	}
}

function createCocktail(index, item) {
    var cocktail = $('<div/>');
    cocktail.css('display', 'flex');
    cocktail.addClass('cocktailItems');
	cocktail.append($('<img/>', {
      class: "cocktailImages",
	  src: item.image,
      alt: item.name
    }));
	cocktail.append($('<div/>', {
      class: "itemTitle",
      text: item.name,
	  desc: item.name
    }));
	cocktail.append($('<div/>', {
	  class: "itemTagsBox"
	}));
    $.each(item.tags, function(index, tag_item) {
      cocktail.children('.itemTagsBox').append($('<div/>', {
        class: "itemTags",
		desc: tag_item.desc,
        text: tag_item.name
      }));
    });

	cocktail.on('click', function() {
		var url = "Cocktail_post.jsp?id="+item.id;
		$(location).attr('href', url);
	})

	cocktail.children('.itemTagsBox').css('visibility', 'hidden');
	cocktail.children('.itemTitle').css('visibility', 'hidden');
	
	var limit = 10;
	if(index+1 > limit) {
		$('.cocktailItems:nth-child('+(index+1)+')').css('display', 'none');
	}
	
    $('#cocktailContents').append(cocktail);
}
