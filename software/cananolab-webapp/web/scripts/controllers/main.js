'use strict';
var app = angular.module('angularApp')
  .controller('MainCtrl', function ($rootScope, $scope, $location,$http, $cookieStore, $window, $cookies) {

  	$scope.userActions = 1;
  	$scope.loginShow = 0;
    $scope.authErrors = 0;
    $rootScope.tabs = ["HELP","GLOSSARY"];
    
  	$scope.doUserAction = function() {
  		if ($scope.userActions==2) {
        $scope.loginShow = 1;
        $scope.authErrors = 0;
  		}
  		else if ($scope.userActions==3) {
  			$scope.loginShow = 0;
        $location.path("/register").replace();
        $scope.apply();        
  		}
      else {
        $scope.loginShow = 0;

      }
  	}

  	$scope.loginDo = function() {
      if (!$scope.password || !$scope.loginId) {
        $scope.authErrors="Username and Password are required";
      }
      else {
        $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/security/login', params: {"username":$scope.loginId,"password":$scope.password} }).
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
