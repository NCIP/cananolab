'use strict';
var app = angular.module('angularApp')

  .controller('AuthCtrl', function ($rootScope,$scope,$http) {
    $scope.auth_cookie="";
    $rootScope.tabs="";

	$scope.getTabs = function() {
     $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/core/getTabs'}).
     success(function(data, status, headers, config) {
        $rootScope.tabs = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
    	   });
    }      

  });
