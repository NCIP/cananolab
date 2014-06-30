'use strict';

var app = angular.module('angularApp')
.controller('LoginCtrl', function ($scope, $http, $location) {
  	$scope.userActions = 1;
  	$scope.loginShow = 0;
     $scope.authErrors = 0;

  	$scope.loginDo = function() {
      if (!$scope.password || !$scope.loginId) {
        $scope.authErrors="Username and Password are required";
      }
      else {
        $http({method: 'GET', url: '/caNanoLab/rest/security/login', params: {"username":$scope.loginId,"password":$scope.password} }).
        success(function(data, status, headers, config) {
          // this callback will be called asynchronously
          // when the response is available
          $location.path("/home").replace();

          //Set tabs here.. Delete on logout. Use variable instead of rest call

        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          $scope.authErrors=data;
    });
      }      
  	}  	
});

