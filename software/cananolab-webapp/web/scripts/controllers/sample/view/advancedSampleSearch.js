'use strict';
var app = angular.module('angularApp')

  .controller('AdvancedSampleSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.sampleData = sampleService.sampleData;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   

    // setup object where all data is stored //
    $scope.searchSampleForm = {};
    $scope.searchSampleForm.theSampleQuery = [];
    $scope.searchSampleForm.theCompositionQuery = [];
    $scope.searchSampleForm.theCharacterizationQuery = [];
    $scope.isNewSample = true;
    $scope.isNewCharacterization = true;
    $scope.isNewComposition = true;



    // setup initial objects where individual temporary objects will be stored //
    // on add or remove they get pushed or removed from the searchSampleForm //
    $scope.theSampleQuery = {};
    $scope.theCompositionQuery = {};
    $scope.theCharacterizationQuery = {};


    $scope.data = {"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"characterizationTypes":["physico-chemical characterization","in vitro characterization","in vivo characterization","[Canano_char_type]","[SY-New-Char-Type]","[TEST111]","[ex vivo]"],"nanomaterialEntityTypes":["biopolymer","carbon","carbon black","carbon nanotube","dendrimer","emulsion","fullerene","liposome","metal oxide","metal particle","metalloid","nanohorn","nanolipogel","nanorod","nanoshell","polymer","quantum dot","silica"],"functionTypes":["TEST TYPE","endosomolysis","imaging function","other","targeting function","test function","therapeutic function","transfection"]};

    // initial rest call to setup form dropdowns //
    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: '/caNanoLab/rest/sample/setupAdvancedSearch'}).
      success(function(data, status, headers, config) {
        $scope.data = data;
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
      });
    });     

    $scope.updateSampleCriteria = function() {
      if ($scope.isNewSample) {
        $scope.searchSampleForm.theSampleQuery.push($scope.theSampleQuery);
      }
      else {
        angular.copy($scope.theSampleQuery,$scope.currentSample);
      }
      $scope.theSampleQuery = {};
      $scope.isNewSample=true;
    };

    $scope.editSampleCriteria = function(sample) {
      $scope.currentSample = sample;
      $scope.theSampleQuery = angular.copy(sample); 
      $scope.isNewSample=false;
    }; 

    $scope.removeSample = function() {
      $scope.searchSampleForm.theSampleQuery.splice($scope.searchSampleForm.theSampleQuery.indexOf($scope.currentSample),1);
      $scope.theSampleQuery = {};
      $scope.isNewSample = true;
    };        

    $scope.clearSampleQuery = function() {
      $scope.theSampleQuery = {};
      $scope.isNewSample = true;
    };

    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
    };

  });
