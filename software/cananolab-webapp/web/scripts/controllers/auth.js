'use strict';
var app = angular.module('angularApp')

  .controller('AuthCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope,$http) {
    $scope.auth_cookie="";

    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();     

  });
