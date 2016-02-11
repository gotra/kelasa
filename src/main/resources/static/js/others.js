"use strict";

window.fbAsyncInit = function() {
    FB.init({
        appId      : '424234734443230',
        xfbml      : true,
        version    : 'v2.5'
    });
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function onSignIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    var id_token = googleUser.getAuthResponse().id_token;
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail());
    console.log('ID_TOKEN:' + id_token);

    startTokenAuthentication(id_token,'/api/google/tokensignin');

}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
        console.log('User signed out.');
    });
}

function facebookLogin() {

    FB.login(function(response) {
        var gender;
        var events = [];
        if (response.status === 'connected') {
            var userID = response.authResponse.userID;
            var accessToken = response.authResponse.accessToken;

            console.log(userID,accessToken);
            startTokenAuthentication(accessToken,'/api/facebook/tokensignin');


        }
    },{ scope: "public_profile,email" });

}


function startTokenAuthentication(token,url) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send('idtoken=' + token);

}