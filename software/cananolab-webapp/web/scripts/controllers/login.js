'use strict';

var app = angular.module('angularApp')
.controller('LoginCtrl', function (navigationService,groupService,$rootScope,$scope, $http, $location, $routeParams) {
  	$scope.userActions = 1;
  	$scope.loginShow = 0;
    $scope.authErrors = 0;

    $rootScope.tabs = navigationService.get();
    //TODO:Comment to remove CSM
    //$rootScope.groups = groupService.getGroups.data.get();   
    $scope.came_from = $routeParams.came_from;
  	$scope.loginDo = function() {
      if (!$scope.password || !$scope.loginId) {
        $scope.authErrors="Username and Password are required";
      }
      else {      	  
    	  $scope.bean = {"username" : $scope.loginId, "password" : $scope.password};   	  
          $http({method: 'POST', url: 'login', headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                 transformRequest: function(obj) {
                  var str = [];
                  for(var p in obj)
                  str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                  return str.join("&");
                 }, data: $scope.bean}).
        success(function(data, status, headers, config) {
          // this callback will be called asynchronously
          // when the response is available
      	  $rootScope.loggedInUser = data;
      	  $scope.loginShow = 0; 
      	  $location.path("/").replace();
      	  $route.reload();

          //Set tabs here.. Delete on logout. Use variable instead of rest call

        }).
        error(function(data, status, headers, config) {
          $scope.password = ''; 
          $scope.authErrors = data;
        });
      }      
  	}  	
});

