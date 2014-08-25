'use strict';
var app = angular.module('angularApp')
  .controller('MainCtrl', function (sampleService, publicationService, protocolService, navigationService, groupService,$rootScope, $scope, $location,$http, $route,$cookieStore, $window, $cookies) {

    $scope.userActions = 1;
    $scope.loginShow = 0;
    $scope.authErrors = 0;
    $rootScope.navTree=false;
    $scope.homepage = true;
    $rootScope.tabs = navigationService.get({'homePage':'true'});
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.searchSampleForm = {};
    $scope.sampleData = sampleService.sampleData;
    $scope.publicationData = publicationService.publicationData;    
    $scope.protocolData = protocolService.protocolData;

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
    };

    $scope.doSearch = function(type) {
      $scope.loader = true;
      if (type=='samples'){
          $http({method: 'POST', url: '/caNanoLab/rest/sample/searchSample',data: $scope.searchSampleForm}).
          success(function(data, status, headers, config) {
            $scope.sampleData.data = data;
            $location.path("/sampleResults").replace();

          }).
          error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.message = data;
          }); 
      };

      if (type=='protocols') {

          $http({method: 'POST', url: '/caNanoLab/rest/protocol/searchProtocol',data: $scope.searchSampleForm}).
              success(function(data, status, headers, config) {
                  $scope.protocolData.data = data;
                  $location.path("/protocolResults").replace();
              }).
              error(function(data, status, headers, config) {
                  $scope.loader = false;
                  $scope.message = data;
              });
      };

      if (type=='publications') {
          $http({method: 'POST', url: '/caNanoLab/rest/publication/searchPublication',data: $scope.searchSampleForm}).
          success(function(data, status, headers, config) {
            $scope.publicationData.data = data;
            $location.path("/publicationResults").replace();

          }).
          error(function(data, status, headers, config) {
            $scope.loader = false;
            $scope.message = data;
          }); 
      };

    };   

    $scope.displayCredit = function(banner,state) {
      if (state)
        { $scope[banner] = true; }
      else { $scope[banner] = false;}
    }    


  });
