var app = angular.module('teamfinder', ['ngCookies', 'directives']);

app.controller('PostcodeController', function($http) {
  var controller = this;
  this.postcode;
  this.team;
  this.img;
  this.teamFound;

  this.request = function() {
    $http.get('QPL/team/' + this.postcode).then(onSuccess, onError);
  }

  function onSuccess(response){
	controller.teamFound = response.status==200;
    controller.team = response.data;
	controller.img = controller.team.replace(/ /g, "_").toLowerCase() + ".png";
  }

  function onError(error){
    controller.team = "Error connecting to server";
  }
  
  this.location = function() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position){
	    $http.get('QPL/team/' + position.coords.latitude + '/' + position.coords.longitude).then(onSuccess, onError);
	  });
    }
  }

});
