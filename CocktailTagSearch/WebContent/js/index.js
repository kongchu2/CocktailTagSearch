const hover = document.querySelectorAll(".cocktailItems");
const hoverSize = hover.length;
const search = document.querySelector("#searchText");
const autocomplete = document.querySelector("#autocompleteTagsContents");
var autocompleteTag = document.querySelectorAll(".autocompleteTags");

var save_search_sentence = "";

search.addEventListener("keyup", cocktailFilter);
search.addEventListener("keyup", tagFilter);
search.addEventListener("keydown", hideComplete);
search.addEventListener("keyup", showNotFound);

search.addEventListener("focus", function() {
  autocomplete.style.display = "block";
});

search.addEventListener("focusout", function() {
  autocomplete.style.display = "none";
});
 
for(var i=0; i<autocompleteTag.length; i++) {
  autocompleteTag[i].addEventListener("mouseover", showComplete);
  autocompleteTag[i].addEventListener("mouseout", hideComplete);
  autocompleteTag[i].addEventListener("mousedown", addTag);
}

function tagSearch() {
  const autoTag = document.querySelectorAll(".searchTags");
  const item = document.getElementsByClassName("cocktailItems");

  hideInFullCocktails();

  for(var i=0; i<item.length; i++) {
    var flag = false;
    var flagCount = 0;

    itemTag = item[i].getElementsByClassName("itemTags");
    
    for(var j=0; j<autoTag.length; j++) {
      for(var k=0; k<itemTag.length; k++) {
        if(autoTag[j].textContent == itemTag[k].textContent) {
          flagCount ++;
        }
      }
    }
    if(flagCount == autoTag.length) {
      flag = true;
    }
    if(flag == true) {
      item[i].style.display = "flex";
    } else {
      item[i].setAttribute("mark", "x");
    }
  }
}

function hideInFullCocktails() {
  const item = document.getElementsByClassName("cocktailItems");

  for(var i=0; i<item.length; i++) {
    item[i].style.display = "none";
  }
}

function showInFullCocktails() {
  const item = document.getElementsByClassName("cocktailItems");

  for(var i=0; i<item.length; i++) {
    item[i].style.display = "flex";
  }
}

// 모든 칵테일이 표시가 안될 때 Not Found 표시
function showNotFound() {
  const item = hover;
  var flag = true;
  for(var i=0; i<item.length; i++) {
    if(item[i].style.display == "flex") {
      flag = false;
      break;
    }
  }
  if(flag == true) {
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

function cocktailFilter(){

    var value, item;

    value = search.value.toUpperCase().trim();

    item = document.getElementsByClassName("cocktailItems");

    for(var i=0;i<item.length;i++){
      var name = item[i].getElementsByClassName("itemTitle");
      var flag = item[i].getAttribute("mark") != "x";
      if(name[0].innerHTML.toUpperCase().indexOf(value) > -1 && flag){
        item[i].style.display = "flex";
      }else{
        item[i].style.display = "none";
      }
    }
  }

  function tagFilter(){
    var value, item;

    value = search.value.toUpperCase().trim();

    var tag = document.querySelectorAll(".searchTags");

    item = document.getElementsByClassName("autocompleteTags");

    for(var i=0;i<item.length;i++){
      var name = item[i];
      if(name.textContent.toUpperCase().indexOf(value) > -1){
        item[i].style.display = "block";
      }else{
        item[i].style.display = "none";
      }
      for(var j=0; j<tag.length; j++) {
        if(tag[j].textContent.toUpperCase() == item[i].textContent.toUpperCase()) {
          item[i].style.display = "none";
        }
      }
    }

  }

function addTag() {
  var value, input;
  var tag = document.createElement("div");
  tag.setAttribute("class", "searchTags"); 
  tag.addEventListener("click", removeTag);

  value = this.textContent.toUpperCase();
  tag.innerText = value;
  input = document.getElementById("searchTagsContents");

  input.appendChild(tag);
  
  setTimeout(function() {  
    search.value = "";
    tagFilter();
    tagSearch();
    cocktailFilter();
    showNotFound();
  }, 0.001);
}

function removeTag() {
    this.parentNode.removeChild(this);
    
    const item = document.getElementsByClassName("cocktailItems");
    for(var i=0; i<item.length; i++) {
      const itemTag = item[i].getElementsByClassName("itemTags");
      for(var j=0; j<itemTag.length; j++) {
        if(itemTag[j].textContent != this.textContent) {
          item[i].removeAttribute("mark", "x");
          break;
        }
      }
    }
    
    tagFilter();
    tagSearch();
    showNotFound();
    cocktailFilter();

}

for(var i=0; i<hoverSize; i++) {
  hover[i].addEventListener("mouseover", styleAppendOver);
  hover[i].addEventListener("mouseout", styleAppendOut);
}

document.getElementById("searchText").addEventListener("input", getAutocompleteTags);

function getAutocompleteTags() {
  console.log($("#searchText").val());
  $.ajax({
		type:"post",
		url:"http://localhost:8090/CocktailTagSearch/search",
		data: {
		  search: $("#searchText").val()
		},
		success:function(data) {
			if(data === "")
			  return;
      $("#autocompleteTagsContents").html("");
      $.each(data.tag, function(index, item) {
        $("#autocompleteTagsContents").append("<div class='autocompleteTags'>" + item.name + "</div>");
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