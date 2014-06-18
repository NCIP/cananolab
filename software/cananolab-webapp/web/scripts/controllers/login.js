'use strict';

/**
 * @ngdoc function
 * @name angularApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the angularApp
 */
angular.module('angularApp')
.controller('LoginCtrl', function ($scope, $http, $cookieStore, $window, $cookies) {
	$scope.submitData = function() {
		// Enable CORS
		//$httpProvider.defaults.useXDomain = true;
      //delete $httpProvider.defaults.headers.common['X-Requested-With'];

  	  $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/services/login', params: {"username":$scope.loginId,"password":$scope.password} }).
		    success(function(data, status, headers, config) {
		      // this callback will be called asynchronously
		      // when the response is available
		      //alert(data);
		      $scope.message=data;
		      
//		      alert('1');
//		      alert($cookies.$cookieStore.get('JSESSIONID'));
//		      alert('2');
		      
		      console.log('1');
		      console.log(data); 
		      console.log('2');
		      
		      //$cookieStore.put("JSESSIONID",data);

		      $window.location.replace("http://localhost:8080/caNanoLab/collaborationGroup.do;jsessionid=" + data + "?dispatch=setupNew&page=0");

		    }).
		    error(function(data, status, headers, config) {
		      // called asynchronously if an error occurs
		      // or server returns response with an error status.
		      //alert(data);
		      $scope.message=data;
  	});
	};
});

