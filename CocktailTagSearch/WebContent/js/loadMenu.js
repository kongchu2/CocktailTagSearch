userData = {signed:"0"};

var DataLoadFunc = [];

$(document).ready(function() {
    $("#menuContents").load("menuContents.html");
    getSessionData();
});

function createSignName(name) {
	$('#sign').append($('<div/>', {
      class: "sign_name",
      text: name
    }));
}
function createSignUp() {
	$('#sign').append($('<div/>', {
      class: "sign_up",
	  text: "Sign up"
    }));
	$('.sign_up').on("click", function() {
		location.href = 'signup.jsp';
	});
}
function createSignIn() {
	$('#sign').append($('<div/>', {
      class: "sign_in",
      text: "Sign in"
    }));
	$('.sign_in').on("click", function() {
		location.href = 'login.html';
	});
	  
}
function createSignOut() {
	$('#sign').append($('<div/>', {
      class: "sign_out",
	  text: "Sign out"
    }));
	$('.sign_out').on("click", function() {
		location.href = 'javascript:logout()';
	});
}
function createMyPageLink() {
	$('#sign').append($('<div/>', {
      class: "myPageLink",
	  text: "MyPage"
    }));
	$('.myPageLink').on("click", function() {
		location.href = 'myPage.html';
	});
}

function getSessionData() {
  $.ajax({
    type:"post",
	  url:"http://localhost:8090/CocktailTagSearch/SessionData",
    success: function(data) {
      if(data.signed == "0") {
        createSignIn();
		    createSignUp();
      } else {
        userData = data;
		    createSignOut();
		    createMyPageLink();
        createSignName(data.user.name);
        for(var i=0;i<DataLoadFunc.length;i++) {
          DataLoadFunc[i]();
        }
      }
    }
  });
}

function logout() {
  fetch('http://localhost:8090/CocktailTagSearch/Logout');
  location.reload();
}