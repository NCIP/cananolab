'use strict';
var app = angular.module('angularApp')

  .controller('AdvancedSampleSearchCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location) {
    $scope.sampleData = sampleService.sampleData;
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();   

    // setup object where all data is stored //
    $scope.searchSampleForm = {};

    // define logical operators for each section and overall query //
    $scope.defineSampleForm = function() {
      $scope.searchSampleForm.sampleLogicalOperator = "and";
      $scope.searchSampleForm.compositionLogicalOperator = "and";
      $scope.searchSampleForm.characterizationLogicalOperator = "and";
      $scope.searchSampleForm.logicalOperator = "and";

      //define array placeholders for samples, compositions and characterizations //
      $scope.searchSampleForm.sampleQueries = [];
      $scope.searchSampleForm.compositionQueries = [];
      $scope.searchSampleForm.characterizationQueries = [];
    };
    $scope.defineSampleForm();

    // set default values to determine if new sample, etc. //
    $scope.isNewSample = true;
    $scope.isNewCharacterization = true;
    $scope.isNewComposition = true;

    // setup initial objects where individual temporary objects will be stored //
    // on add or remove they get pushed or removed from the searchSampleForm //
    $scope.theSampleQuery = {};
    $scope.theCompositionQuery = {};
    $scope.theCharacterizationQuery = {};


    $scope.data = {"functionalizingEntityTypes":["Magnetic Particle","Monomer","Polymer","Quantum Dot","antibody","biopolymer","radioisotope","small molecule"],"numberOperands":["=",">",">=","<="],"characterizationTypes":[{"label":"physico-chemical characterization","value":"physico-chemical characterization"},{"label":"in vitro characterization","value":"in vitro characterization"},{"label":"in vivo characterization","value":"in vivo characterization"},{"label":"[Canano_char_type]","value":"Canano_char_type"},{"label":"[SY-New-Char-Type]","value":"SY-New-Char-Type"},{"label":"[TEST111]","value":"TEST111"},{"label":"[ex vivo]","value":"ex vivo"}],"nanomaterialEntityTypes":["biopolymer","carbon","carbon black","carbon nanotube","dendrimer","emulsion","fullerene","liposome","metal oxide","metal particle","metalloid","nanohorn","nanolipogel","nanorod","nanoshell","polymer","quantum dot","silica"],"functionTypes":["TEST TYPE","endosomolysis","imaging function","other","targeting function","test function","therapeutic function","transfection"]};
    // $scope.characterizationNameList = [{"label":"pharmacokinetics","value":"pharmacokinetics"},{"label":" --[pharmacokinetics]","value":"pharmacokinetics:pharmacokinetics"},{"label":"toxicology","value":"toxicology"},{"label":" --[toxicity]","value":"toxicology:toxicity"},{"label":"[imaging]","value":"imaging"},{"label":" --[imaging]","value":"imaging:imaging"},{"label":" --[intra-operative photoacoustic imaging]","value":"imaging:intra-operative photoacoustic imaging"},{"label":" --[multimodality imaging]","value":"imaging:multimodality imaging"},{"label":" --[multimodality imaging sensitivity]","value":"imaging:multimodality imaging sensitivity"},{"label":" --[multimodality kinetics]","value":"imaging:multimodality kinetics"},{"label":" --[tumor resection]","value":"imaging:tumor resection"},{"label":"[other_vv]","value":"other_vv"},{"label":" --[activation of innate and adaptive immunity]","value":"other_vv:activation of innate and adaptive immunity"},{"label":" --[adaptive immunity]","value":"other_vv:adaptive immunity"},{"label":" --[biodistribution]","value":"other_vv:biodistribution"},{"label":" --[drug accumulation]","value":"other_vv:drug accumulation"},{"label":" --[dye accumulation]","value":"other_vv:dye accumulation"},{"label":" --[health status]","value":"other_vv:health status"},{"label":" --[NK depletion]","value":"other_vv:NK depletion"},{"label":" --[photoacoustic imaging detection sensitivity]","value":"other_vv:photoacoustic imaging detection sensitivity"},{"label":" --[photothermal response]","value":"other_vv:photothermal response"},{"label":" --[reproducibility of photoacoustic imaging system]","value":"other_vv:reproducibility of photoacoustic imaging system"},{"label":" --[SERS detection sensitivity]","value":"other_vv:SERS detection sensitivity"},{"label":" --[stability]","value":"other_vv:stability"},{"label":" --[therapeutic efficacy]","value":"other_vv:therapeutic efficacy"},{"label":" --[toxicity]","value":"other_vv:toxicity"}];
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
