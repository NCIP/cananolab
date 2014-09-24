'use strict';
var app = angular.module('angularApp')

  .controller('AdvancedSampleSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.sampleData = sampleService.sampleData;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   

    // define logical operators for each section and overall query //
    $scope.defineSampleForm = function() {
      // setup object where all data is stored //
      $scope.searchSampleForm = {};      

      // define logical operators for overall form and 3 sections //
      $scope.searchSampleForm.sampleLogicalOperator = "and";
      $scope.searchSampleForm.compositionLogicalOperator = "and";
      $scope.searchSampleForm.characterizationLogicalOperator = "and";
      $scope.searchSampleForm.logicalOperator = "and";

      //define array placeholders for samples, compositions and characterizations //
      $scope.searchSampleForm.sampleQueries = [];
      $scope.searchSampleForm.compositionQueries = [];
      $scope.searchSampleForm.characterizationQueries = [];

      // setup initial objects where individual temporary objects will be stored //
      // on add or remove they get pushed or removed from the searchSampleForm //
      $scope.theSampleQuery = {};
      $scope.theCompositionQuery = {};
      $scope.theCharacterizationQuery = {};

      // define placeholder lists for characterization dropdowns //
      $scope.datumUnitOptionsList = [];
      $scope.datumNameList = [];
      $scope.characterizationNameList = [];  

      // define list for composition entity type dropdowns //
      $scope.entityTypes = [];

      // set default values to determine if new sample, etc. //
      $scope.isNewSample = true;
      $scope.isNewCharacterization = true;
      $scope.isNewComposition = true;

    };
    $scope.defineSampleForm();

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

    // called when selecting characterization type. populates characterization name dropdown //
    $scope.setCharacterizationOptions = function(editType) {
      var charType = $scope.theCharacterizationQuery.characterizationType;
      $scope.loader = true;
      $http({method: 'GET', url: '/caNanoLab/rest/sample/getDecoratedCharacterizationOptions?charType='+$scope.theCharacterizationQuery.characterizationType}).
      success(function(data, status, headers, config) {
        $scope.characterizationNameList = data;
        $scope.loader = false;        
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
        $scope.loader = false;      
      });
      if (!editType) {
        $scope.theCharacterizationQuery = {}; 
        $scope.theCharacterizationQuery.characterizationType = charType;        
      }
    };

    // called when selecting characterization name. populates datum name dropdown //
    $scope.setDatumNameOptionsByCharName = function(editType) {
      $scope.loader = true;
      $http({method: 'GET', url: '/caNanoLab/rest/sample/getDecoratedDatumOptions?charType='+$scope.theCharacterizationQuery.characterizationType+'&charName='+$scope.theCharacterizationQuery.characterizationName}).
      success(function(data, status, headers, config) {
        $scope.datumNameList = data;
        $scope.loader = false;
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
        $scope.loader = false;
      });  
      if (!editType) {
        $scope.theCharacterizationQuery.datumName = '';
        $scope.theCharacterizationQuery.operand = '';
        $scope.theCharacterizationQuery.datumValueUnit = '';        
      }    
    };


    // called when selecting datum name. populates datum value unit options dropdown //
    $scope.setDatumValueUnitOptions = function(editType) {
      if (!$scope.theCharacterizationQuery.datumName) {
        $scope.theCharacterizationQuery.operand = '';
      };
      $http({method: 'GET', url: '/caNanoLab/rest/sample/getDatumUnitOptions?datumName='+$scope.theCharacterizationQuery.datumName}).
      success(function(data, status, headers, config) {
        $scope.datumUnitOptionsList = data;
        $scope.loader = false;
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
        $scope.loader = false;
      });   
      if (!editType) {
        $scope.theCharacterizationQuery.datumValueUnit = '';         
        $scope.theCharacterizationQuery.operand = '';         
      }
    };

    // called when adding a new characterization or updating an existing one //
    $scope.updateCharacterizationCriteria = function() {
      if ($scope.isNewCharacterization) {
        $scope.searchSampleForm.characterizationQueries.push($scope.theCharacterizationQuery);
      }
      else {
        angular.copy($scope.theCharacterizationQuery,$scope.currentCharacterization);
      }
      $scope.theCharacterizationQuery = {};
      $scope.datumUnitOptionsList = [];
      $scope.datumNameList = [];
      $scope.characterizationNameList = [];
      $scope.isNewCharacterization=true;
    };

    // called when clicking edit on existing characterization. Copies characterization //
    $scope.editCharacterizationCriteria = function(characterization) {
      $scope.currentCharacterization = characterization;
      $scope.theCharacterizationQuery = angular.copy(characterization); 
      $scope.setCharacterizationOptions('update');
      $scope.setDatumNameOptionsByCharName('update');
      $scope.setDatumValueUnitOptions('update');
      $scope.isNewCharacterization=false;
    }; 

    // removes characterization from searchSampleForm //
    $scope.removeCharacterization = function() {
      $scope.searchSampleForm.characterizationQueries.splice($scope.searchSampleForm.characterizationQueries.indexOf($scope.currentCharacterization),1);
      $scope.theCharacterizationQuery = {};
      $scope.isNewCharacterization = true;
    };        

    // resets characterization criteria //
    $scope.clearCharacterizationQuery = function() {
      $scope.theCharacterizationQuery = {};
      $scope.datumUnitOptionsList = [];
      $scope.datumNameList = [];
      $scope.characterizationNameList = [];      
      $scope.isNewCharacterization = true;
    };    

    // gets results from advanced search //
    $scope.search = function() {
      $scope.loader = true;
      $http({method: 'POST', url: '/caNanoLab/rest/sample/searchSampleAdvanced',data: $scope.searchSampleForm}).
      success(function(data, status, headers, config) {
        $scope.sampleData.data = data;
        $scope.loader = false;
        // $location.path("/sampleResults").replace();

      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        // $rootScope.sampleData = data;
        $scope.loader = false;
        $scope.message = data;
      }); 
    };

    // resets overall form //
    $scope.resetForm = function() {
      $scope.searchSampleForm = {};
      $scope.defineSampleForm();    
    };

  });
