userData = {signed:"0"};

var DataLoadFunc = [];
var notSignedFunc = [];

$('.mainTitle').on('click', function() {
	var url = 'index.html';
	$(location).attr('href',url);
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
		location.href = 'signup.html';
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
	  url:"/CocktailTagSearch/SessionData",
    success: function(data) {
      if(data.signed == "0") {
        createSignIn();
		    createSignUp();
        for(var i=0;i<notSignedFunc.length;i++) {
          notSignedFunc[i]();
        }
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
  fetch('/CocktailTagSearch/Logout');
  location.reload();
}