'use strict';
var app = angular.module('angularApp')

  .controller('AuthCtrl', function ($rootScope,$scope,$http) {
    $scope.auth_cookie="";
    $rootScope.tabs="";
    $rootScope.groups = "" ;

    $scope.$on('$viewContentLoaded', function(){
     $http({method: 'GET', url: '/caNanoLab/rest/core/getTabs'}).
     success(function(data, status, headers, config) {
        $rootScope.tabs = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
         });
    });     
     

    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: '/caNanoLab/rest/security/getUserGroups' }).
        success(function(data, status, headers, config) {
          $rootScope.groups = data;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          //alert(data);
          $scope.authErrors=data;
        });
    });    

  });
