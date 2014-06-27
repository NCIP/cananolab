'use strict';
var app = angular.module('angularApp')
  .controller('LogoutCtrl', function ($rootScope, $scope, $location,$http, $cookieStore, $window, $cookies) {

    $rootScope.tabs = ["tabs after logout"];
   
  });
