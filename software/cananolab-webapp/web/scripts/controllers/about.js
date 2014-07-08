'use strict';
var app = angular.module('angularApp')
  .controller('AboutCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope) {
	$rootScope.tabs = navigationFactory.query({'homePage':'true'});
     $rootScope.groups = groupsFactory.get();
    $scope.myData = [{name: "Moroni", age: 50},
                     {name: "Tiancum", age: 43},
                     {name: "Jacob", age: 27},
                     {name: "Nephi", age: 29},
                     {name: "Enos", age: 34}];  

    $scope.gridOptions = { data: 'myData' }; 
  });
