'use strict';

angular.module('angularApp')
  .controller('MainCtrl', function ($scope) {

  	$scope.userActions = 1;
  	$scope.loginShow = 0;
  	$scope.doUserAction = function() {
  		if ($scope.userActions==2) {
  			$scope.loginShow = 1;
  		}
  		else {
  			$scope.loginShow = 0;
  		}
  	}

  	$scope.loginDo = function() {
  		window.location="/caNanoLab/index.html#/auth"
  	}  	


  });
