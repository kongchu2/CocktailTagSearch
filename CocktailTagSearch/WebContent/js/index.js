const hover = document.querySelectorAll(".cocktailItems");
const hoverSize = hover.length;
const search = document.querySelector(".searchText");
const autocomplete = document.querySelector("#autocompleteTagsContents");
var autocompleteTag = document.querySelectorAll(".autocompleteTags");

var autocompleteTagList = [];
var selectTagList = [];

var save_search_sentence = "";

var dataLoaded = false;

var add_continuous_pressing = false;
var remove_continuous_pressing = false;

var cocktailItemLength = 0;

$(document).ready(init);

$(document).ready(function() {
    $("#menuContents").load("menuContents.html", function() {getSessionData();});
});

search.addEventListener('keydown', function() {
	if(search.style.color == "gray") {
    search.value = save_search_sentence;
    search.style.color = "black";
  }
});

search.addEventListener("input", _.debounce(inputCallback, 300));

search.addEventListener("focus", function() {
  autocomplete.style.display = "flex";

  search.parentNode.style["box-shadow"] = "none";
});
search.addEventListener("focusout", function() {
  autocomplete.style.display = "none";

  search.parentNode.style["box-shadow"] = "0px 8px 20px rgb(0 0 0 / 6%)";
});

$('.cancelIcon').on('click', function() { search.value = ""; })

var autocompleteTagsBox = $('.autocompleteTagsBox');
autocompleteTagsBox.on('mouseover', showComplete);
autocompleteTagsBox.on('mouseout', hideComplete);
autocompleteTagsBox.on('mousedown', addTag);
autocompleteTagsBox.on('mousedown', function() { search.value = ""; });

var searchTagsBox = $('.searchTagsBox');
searchTagsBox.on('click', removeTag);

var cocktailItemContents = $('#cocktailItemContents');
cocktailItemContents.on('mouseover', styleAppendOver);
cocktailItemContents.on('mouseout', styleAppendOut);

var favoriteTagsBox = $('.favoriteTagsBox');
favoriteTagsBox.on('mousedown', addTag);

function inputCallback() {
  getAutocompleteTags();
  cocktailItemLength = 0;
  $('.cocktailItems').remove();
  getCocktailItems();
}

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
  input = document.querySelector(".searchTagsBox");

  input.appendChild(tag);

  e.target.remove();

  setTimeout(function() { 
	findMathingTag(value, loadData);	
  }, 0);
  
  add_continuous_pressing = true;
  setTimeout(function() { add_continuous_pressing = false; }, 100);
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
  setTimeout(function() { remove_continuous_pressing = false; }, 100);

}

function findMathingTag(tagName, callback) {
  $.ajax({
		type:"post",
		url:"/CocktailTagSearch/MatchingTag",
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
		url:"/CocktailTagSearch/TagSearch",
		dataType:"json",
		data: {
		  search: $(".searchText").val(),
	      tags: JSON.stringify(selectTagList)
		},
		success:function(data) {
		  if(data === "") 
			  return;	
	      $(".autocompleteTagsBox").html("");
	      autocompleteTagList = [];
	      $.each(data.tags, createTag);
	    },
   		error:function(error) {
	 	  $('.autocompleteTagsBox').empty();
		}
  });
}

function getCocktailItems() {
  dataLoaded = false;
  $.ajax({
    type:"post",
		url:"/CocktailTagSearch/CocktailSearch",
    dataType:"json",
		data: {
		  search: $(".searchText").val(),
	    tags: JSON.stringify(selectTagList),
      length: cocktailItemLength
		},
    success:function(data) {
	    if(data != null || data.cocktails.length != 0) {
        $('.spaceCocktailItems').remove();
        $.each(data.cocktails, createCocktail);
        createManySpaceCocktail();
        dataLoaded = true;
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
	url:"/CocktailTagSearch/FavoriteTagData",
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
		$('.favoriteTagsBox').empty();
		$.each(data.tag, function(index, item) {
			$('.favoriteTagsBox').append($('<div/>', {
		      class: "favoriteTags",
			  desc: item.desc,
		      text: item.name,
			  tag_id: item.id
		    }));
		  });
	    }
	}
  });
}

function init() {
  loadData()
  setInfiniteScrolling();
}

function loadData() {
	getAutocompleteTags();
  cocktailItemLength = 0;
  $('.cocktailItems').remove()
  getFavoriteTags();
  getCocktailItems();
}

function createTag(index, item) {
	$('.autocompleteTagsBox').append($('<div/>', {
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
		var url = "cocktailPost.html?id="+item.id;
		$(location).attr('href', url);
	})

	cocktail.children('.itemTagsBox').css('visibility', 'hidden');
	cocktail.children('.itemTitle').css('visibility', 'hidden');
	
    $('#cocktailItemContents').append(cocktail);
}
function createSpaceCocktail() {
    var cocktail = $('<div/>');
    cocktail.addClass('spaceCocktailItems');
	cocktail.css('visibility', 'hidden');
	
    $('#cocktailItemContents').append(cocktail);
}
function createManySpaceCocktail() {
  createSpaceCocktail();
  createSpaceCocktail();
  createSpaceCocktail();
  createSpaceCocktail();
  createSpaceCocktail();
}
function getMoreCocktail() {
    cocktailItemLength = $('.cocktailItems').length;
    getCocktailItems();
}

function setInfiniteScrolling() {
  $(window).scroll( _.throttle(function()	{
    if (dataLoaded && $(document).height() <= ($(window).scrollTop() + $(window).height()) + 200) {
      getMoreCocktail();
    }
  }, 200));
}

function goRandomCocktail() {
  $.ajax({
    type:"get",
		url:"/CocktailTagSearch/GetRandomCocktail",
    success:function(data) {
	    location.href = "cocktailPost.html?id="+data;
    }
  });
}