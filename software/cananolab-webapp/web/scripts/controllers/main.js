'use strict';
var app = angular.module('angularApp')
  .controller('MainCtrl', function (navigationService, groupService,$rootScope, $scope, $location,$http, $route,$cookieStore, $window, $cookies) {

    $scope.userActions = 1;
    $scope.loginShow = 0;
    $scope.authErrors = 0;
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.query({'homePage':'true'});
    $rootScope.groups = groupService.get();
    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: '/caNanoLab/rest/core/initSetup' }).
        success(function(data, status, headers, config) {
          $scope.numOfPublicProtocols=data.numOfPublicProtocols;
          $scope.numOfPublicCharacterizations=data.numOfPublicCharacterizations;
          $scope.numOfPublicInvitroCharacterizations=data.numOfPublicInvitroCharacterizations;
          $scope.numOfPublicInvivoCharacterizations=data.numOfPublicInvivoCharacterizations;
          $scope.numOfPublicOtherCharacterizations=data.numOfPublicOtherCharacterizations;
          $scope.numOfPublicPhysicoChemicalCharacterizations=data.numOfPublicPhysicoChemicalCharacterizations;
          $scope.numOfPublicPublications=data.numOfPublicPublications;
          $scope.numOfPublicSamples=data.numOfPublicSamples;
          $scope.numOfPublicSources=data.numOfPublicSources;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          //alert(data);
          $scope.authErrors=data;
        });
    });    
    
    $scope.doUserAction = function() {

      if ($scope.userActions==1) {
        $scope.loginShow = 0;
        window.location.href = "/caNanoLab/#/searchSample";

      }
      else if ($scope.userActions==2) {
        $scope.loginShow = 1;
        $scope.authErrors = 0;
      }
      else if ($scope.userActions==3) {
        $scope.loginShow = 0;
        $location.path("/register").replace();
        $scope.apply();        
      }
      else {
        $scope.loginShow = 0;
        window.open("https://iforgotmypassword.nih.gov/aims/ps/");

      }
    }

    $scope.loginDo = function() {
      if (!$scope.password || !$scope.loginId) {
        $scope.authErrors="Username and Password are required";
      }
      else {
        $http({method: 'GET', url: '/caNanoLab/rest/security/login', params: {"username":$scope.loginId,"password":$scope.password} }).
        success(function(data, status, headers, config) {
          // this callback will be called asynchronously
          // when the response is available
          $location.path("/home").replace();

          //Set tabs here.. Delete on logout. Use variable instead of rest call

        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          $scope.authErrors=data;
    });
      }      
    }   


  });
