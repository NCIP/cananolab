'use strict';
var app = angular.module('angularApp')
  .controller('MainCtrl', function ($scope, $http, $cookieStore, $window, $cookies) {

  	$scope.userActions = 1;
  	$scope.loginShow = 0;
    $scope.authErrors = 0;

  	$scope.doUserAction = function() {
  		if ($scope.userActions==2) {
        $scope.loginShow = 1;
        $scope.authErrors = 0;
  		}
  		else {
  			$scope.loginShow = 0;
  		}
  	}

  	$scope.loginDo = function() {
  		// window.location="/caNanoLab/index.html#/auth"
      if (!$scope.password || !$scope.loginId) {
        $scope.authErrors="Username and Password are required";
      }
      else {
        $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/security/login', params: {"username":$scope.loginId,"password":$scope.password} }).
        success(function(data, status, headers, config) {
          // this callback will be called asynchronously
          // when the response is available
          //alert(data);
          $scope.authErrors=data;
          
//          alert('1');
//          alert($cookies.$cookieStore.get('JSESSIONID'));
//          alert('2');
          
          console.log('1');
          console.log(data); 
          console.log('2');
          
          //$cookieStore.put("JSESSIONID",data);

          $window.location.replace("http://localhost:8080/caNanoLab/collaborationGroup.do;jsessionid=" + data + "?dispatch=setupNew&page=0");

        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          //alert(data);
          $scope.authErrors=data;
    });
      }      
  	}  	


  });
