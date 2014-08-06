'use strict';
var app = angular.module('angularApp')

  .controller('TemplateCtrl', function ($scope,$rootScope,navigationService, groupService, $location) {
    $scope.test="Put a and groups in here. ALSO GET RID OF navTree left nav and put in here";
    // if ($location.path()!='/') {
    	alert("hello");
    // $scope.tabs = navigationService.query();	


    // }

    	// window.rs = $rootScope;
	// if ($route.current.controller=='MainCtrl') {
	//     $scope.tabs = navigationService.query({'homePage':'true'});	
	// }    
	// else {
	//     $scope.tabs = navigationService.query();			
	// }
    $scope.groups = groupService.getGroups.data.get();    
  });
