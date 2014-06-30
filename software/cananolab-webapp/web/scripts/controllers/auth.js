'use strict';
var app = angular.module('angularApp')

  .controller('AuthCtrl', function ($rootScope,$scope,$http) {
    $scope.auth_cookie="";
    $rootScope.tabs="";

	$rootScope.getTabs = function() {
     $http({method: 'GET', url: '/caNanoLab/rest/core/getTabs'}).
     success(function(data, status, headers, config) {
        $rootScope.tabs = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
    	   });
    }      

  $scope.getGroups = function() {
    return "Test";
     // $http({method: 'GET', url: '/caNanoLab/rest/core/getGroups'}).
     // success(function(data, status, headers, config) {
     //    $rootScope.tabs = data;
     //    }).
     //    error(function(data, status, headers, config) {
     //      // called asynchronously if an error occurs
     //      // or server returns response with an error status.
     //     });
    }       

  });
