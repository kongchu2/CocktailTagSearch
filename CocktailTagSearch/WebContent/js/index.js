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

search.addEventListener("keydown", hideComplete);
search.addEventListener("input", _.debounce(getAutocompleteTags, 300));
search.addEventListener('input',  _.debounce(getCocktailItems, 300));

search.addEventListener("focus", function() {
  autocomplete.style.display = "block";
});

search.addEventListener("focusout", function() {
  autocomplete.style.display = "none";
});

var autocompleteTagsContents = $('#autocompleteTagsContents');
autocompleteTagsContents.on('mouseover', showComplete);
autocompleteTagsContents.on('mouseout', hideComplete);
autocompleteTagsContents.on('mousedown', addTag);
autocompleteTagsContents.on('mousedown', function() { search.value = ""; });

var searchTagsContents = $('#searchTagsContents');
searchTagsContents.on('click', removeTag);

var cocktailItemContents = $('#cocktailItemContents');
cocktailItemContents.on('mouseover', styleAppendOver);
cocktailItemContents.on('mouseout', styleAppendOut);

var favoriteTagContents = $('#favoriteTagContents');
favoriteTagContents.on('mousedown', addTag);

// 모든 칵테일이 표시가 안될 때 Not Found 표시
function showNotFound() {
  if($('.cocktailItems').length == 0) {
    $('.cocktailNotFound').css('display', 'flex');
  } else {
    $('.cocktailNotFound').css('display', 'none');
  }
}

// 자동완성 미리보기 감추기
function hideComplete(e) {
  if(e.target === e.currentTarget) return;
  if(search.style.color == "gray") {
    search.value = save_search_sentence;
    search.style.color = "black";
  }
}

// 자동완성 미리보기 표시하기
function showComplete(e) {
  if(e.target === e.currentTarget) return;
  save_search_sentence = search.value;
  search.value = e.target.textContent;
  search.style.color = "gray";
}

// 칵테일의 제목 및 태그 숨기기
function styleAppendOver(e) {
  if(e.target === e.currentTarget) return;
  var target;
  if(e.target.className !== "cocktailItems"){
	target = e.target.parentNode;
	if(target.className === "itemTagsBox")
		target = target.parentNode;
  }
  else
	target = e.target;	

    const tag = target.querySelectorAll(".itemTags");
    const title = target.querySelector(".itemTitle");

    for(var j=0; j<tag.length; j++) {
        tag[j].style.visibility = "visible";
    }
    title.style.visibility = "visible";
}

// 칵테일의 제목 및 태그 표시하기
function styleAppendOut(e) {
  if(e.target === e.currentTarget) return;
  var target;
  if(e.target.className !== "cocktailItems"){
	target = e.target.parentNode;
	if(target.className === "itemTagsBox")
		target = target.parentNode;
  }
  else
	target = e.target;	

    const tag = target.querySelectorAll(".itemTags");
    const title = target.querySelector(".itemTitle");

    for(var j=0; j<tag.length; j++) {
        tag[j].style.visibility = "hidden";
    }
    title.style.visibility = "hidden";
}

function addTag(e) {
  if(e.target === e.currentTarget) return;
  if(add_continuous_pressing) return;

  var value, input;
  var tag = document.createElement("div");
  tag.setAttribute("class", "searchTags"); 

  value = e.target.textContent.toUpperCase();
  tag.innerText = value;
  input = document.getElementById("searchTagsContents");

  input.appendChild(tag);

  e.target.remove();

  setTimeout(function() { 
	findMathingTag(value, loadData);	
  }, 0);
  
  add_continuous_pressing = true;
  setTimeout(function() { add_continuous_pressing = false; }, 200);
}

function removeTag(e) {
  if(e.target === e.currentTarget) return;
  if(remove_continuous_pressing) return;

  var thisName = e.target.textContent;
    $.each(selectTagList, function(index, item) {
      if(item.name === thisName) {
        selectTagList.splice(index, 1);
		return false;
      }
    })
    e.target.remove();
	
	loadData();

  remove_continuous_pressing = true;
  setTimeout(function() { remove_continuous_pressing = false; }, 200);

}

function findMathingTag(tagName, callback) {
  $.ajax({
		type:"post",
		url:"http://localhost:8090/CocktailTagSearch/MatchingTag",
		dataType:"json",
		data: {
		  name: tagName
		},
		success:function(data) {
		  if(data === "")
			return;
		  selectTagList.push({name: data.tags.name, id: data.tags.id});
		  if(callback)
			callback();
		  
		}
  });
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
	      $.each(data.tags, createTag);
	    },
   		error:function(error) {
	 	  $('#autocompleteTagsContents').empty();
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
	    }
	  }
  });
}

function getFavoriteTags() {
	var favoriteTags = [];
	$.each($('.favoriteTags'), function(index, item) {
		favoriteTags.push({id: parseInt($(this).attr('tag_id'))});
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
		$('#favoriteTagContents').empty();
		$('#favoriteTagContents').css('visibility', 'visible');
		$.each(data.tag, function(index, item) {
			$('#favoriteTagContents').append($('<div/>', {
		      class: "favoriteTags",
			  desc: item.desc,
		      text: item.name,
			  tag_id: item.id
		    }));
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
	
    $('#cocktailItemContents').append(cocktail);
}