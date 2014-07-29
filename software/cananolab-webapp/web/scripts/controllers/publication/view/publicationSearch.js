'use strict';
var app = angular.module('angularApp')

  .controller('PublicationSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.searchPublicationForm = {};
    $rootScope.navTree=false;
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();   
    
    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/setup'}).
      success(function(data, status, headers, config) {
        $scope.data = data;
        $scope.researchArea = {};
        $scope.searchPublicationForm.researchArea = [];
        for (var x=0;x<data.publicationResearchAreas.length;x++) {
          $scope.researchArea[$scope.data.publicationResearchAreas[x]]=false;
        }        
      }).
      error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
      $scope.message = data;
      });
    });     

       
    $scope.displayPubMed = function() {
      if ($scope.searchPublicationForm.category=='book chapter'||$scope.searchPublicationForm.category=='report') {
        $scope.pubMed = true;
      }
      else {
        $scope.pubMed = false;
      }
    };

    $scope.doSearch = function() {
      // $scope.loader = true;
      for (var key in $scope.researchArea) {
        if ($scope.researchArea[key]) {
          $scope.searchPublicationForm.researchArea.push(key)          
        }

      }
      $http({method: 'POST', url: 'http://localhost:8080/caNanoLab/rest/publication/searchPublication',data: $scope.searchPublicationForm}).
      success(function(data, status, headers, config) {
        alert("success");
        // $rootScope.sampleData = data;
        $scope.publicationData.data = data;
        // $location.path("/sampleResults").replace();

      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        // $rootScope.sampleData = data;
        alert("test");
        $scope.loader = false;
        $scope.message = data;
      }); 
    };

    $scope.resetForm = function() {
      $scope.searchPublicationForm = {};
    };

  });
