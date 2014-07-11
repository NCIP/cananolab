'use strict';
var app = angular.module('angularApp')
  .controller('CompositionCtrl', function (navigationFactory, groupsFactory, $rootScope,$scope,$http,$filter,$routeParams) {
    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();   

    // Displays left hand nav for samples section. navTree shows nav and navDetail is page index //
    $rootScope.navTree = true;
    $rootScope.navDetail = 1;
    

  });
