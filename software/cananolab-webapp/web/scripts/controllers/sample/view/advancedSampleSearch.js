'use strict';
var app = angular.module('angularApp')

  .controller('AdvancedSampleSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.sampleData = sampleService.sampleData;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   

    // setup object where all data is stored //
    $scope.searchSampleForm = {};

    // define logical operators for each section and overall query //
    $scope.searchSampleForm.sampleLogicalOperator = "and";
    $scope.searchSampleForm.compositionLogicalOperator = "and";
    $scope.searchSampleForm.characterizationLogicalOperator = "and";
    $scope.searchSampleForm.logicalOperator = "and";

    //define array placeholders for samples, compositions and characterizations //
    $scope.searchSampleForm.sampleQueries = [];
    $scope.searchSampleForm.compositionQueries = [];
    $scope.searchSampleForm.characterizationQueries = [];

    // set default values to determine if new sample, etc. //
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

//// Sample Criteria ////

    // called when adding a new sample or updating an existing one //
    $scope.updateSampleCriteria = function() {
      if ($scope.isNewSample) {
        $scope.searchSampleForm.sampleQueries.push($scope.theSampleQuery);
      }
      else {
        angular.copy($scope.theSampleQuery,$scope.currentSample);
      }
      $scope.theSampleQuery = {};
      $scope.isNewSample=true;
    };

    // called when clicking edit on existing sample. Copies sample //
    $scope.editSampleCriteria = function(sample) {
      $scope.currentSample = sample;
      $scope.theSampleQuery = angular.copy(sample); 
      $scope.isNewSample=false;
    }; 

    // removes sample from searchSampleForm //
    $scope.removeSample = function() {
      $scope.searchSampleForm.sampleQueries.splice($scope.searchSampleForm.sampleQueries.indexOf($scope.currentSample),1);
      $scope.theSampleQuery = {};
      $scope.isNewSample = true;
    };        

    // resets sample criteria //
    $scope.clearSampleQuery = function() {
      $scope.theSampleQuery = {};
      $scope.isNewSample = true;
    };

//// Composition Criteria ////

    // called when selecting composition type. populates composition entity dropdown //
    $scope.setCompositionEntityOptions = function(editType) {
      if (!editType) {
        $scope.theCompositionQuery.entityType='';
      }
      if ($scope.theCompositionQuery.compositionType=='nanomaterial entity') { $scope.entityTypes=$scope.data.nanomaterialEntityTypes };
      if ($scope.theCompositionQuery.compositionType=='functionalizing entity') { $scope.entityTypes=$scope.data.functionalizingEntityTypes };
      if ($scope.theCompositionQuery.compositionType=='function') { $scope.entityTypes=$scope.data.functionTypes };      
    }

    // called when adding a new composition or updating an existing one //
    $scope.updateCompositionCriteria = function() {
      if ($scope.isNewComposition) {
        $scope.searchSampleForm.compositionQueries.push($scope.theCompositionQuery);
      }
      else {
        angular.copy($scope.theCompositionQuery,$scope.currentComposition);
      }
      $scope.theCompositionQuery = {};
      $scope.entityTypes = [];
      $scope.isNewComposition=true;
    };

    // called when clicking edit on existing composition. Copies composition //
    $scope.editCompositionCriteria = function(composition) {
      $scope.currentComposition = composition;
      $scope.theCompositionQuery = angular.copy(composition); 
      $scope.setCompositionEntityOptions('update');
      $scope.isNewComposition=false;
    }; 

    // removes composition from searchSampleForm //
    $scope.removeComposition = function() {
      $scope.searchSampleForm.compositionQueries.splice($scope.searchSampleForm.compositionQueries.indexOf($scope.currentComposition),1);
      $scope.theCompositionQuery = {};
      $scope.isNewComposition = true;
    };        

    // resets composition criteria //
    $scope.clearCompositionQuery = function() {
      $scope.theCompositionQuery = {};
      $scope.isNewComposition = true;
    };    

    // resets overall form //
    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
    };

//// Characterization Criteria ////

    // called when selecting composition type. populates composition entity dropdown //
    $scope.setCompositionEntityOptions = function(editType) {
      if (!editType) {
        $scope.theCompositionQuery.entityType='';
      }
      if ($scope.theCompositionQuery.compositionType=='nanomaterial entity') { $scope.entityTypes=$scope.data.nanomaterialEntityTypes };
      if ($scope.theCompositionQuery.compositionType=='functionalizing entity') { $scope.entityTypes=$scope.data.functionalizingEntityTypes };
      if ($scope.theCompositionQuery.compositionType=='function') { $scope.entityTypes=$scope.data.functionTypes };      
    }

    // called when adding a new composition or updating an existing one //
    $scope.updateCompositionCriteria = function() {
      if ($scope.isNewComposition) {
        $scope.searchSampleForm.compositionQueries.push($scope.theCompositionQuery);
      }
      else {
        angular.copy($scope.theCompositionQuery,$scope.currentComposition);
      }
      $scope.theCompositionQuery = {};
      $scope.entityTypes = [];
      $scope.isNewComposition=true;
    };

    // called when clicking edit on existing composition. Copies composition //
    $scope.editCompositionCriteria = function(composition) {
      $scope.currentComposition = composition;
      $scope.theCompositionQuery = angular.copy(composition); 
      $scope.setCompositionEntityOptions('update');
      $scope.isNewComposition=false;
    }; 

    // removes composition from searchSampleForm //
    $scope.removeComposition = function() {
      $scope.searchSampleForm.compositionQueries.splice($scope.searchSampleForm.compositionQueries.indexOf($scope.currentComposition),1);
      $scope.theCompositionQuery = {};
      $scope.isNewComposition = true;
    };        

    // resets composition criteria //
    $scope.clearCompositionQuery = function() {
      $scope.theCompositionQuery = {};
      $scope.isNewComposition = true;
    };    

    // resets overall form //
    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
    };

  });
