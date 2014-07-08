'use strict';
var app = angular.module('angularApp')
  .controller('AboutCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope) {
	$rootScope.tabs = navigationFactory.query({'homePage':'true'});
     $rootScope.groups = groupsFactory.get();

  });
