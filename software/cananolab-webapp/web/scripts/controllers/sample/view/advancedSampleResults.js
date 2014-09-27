'use strict';
var app = angular.module('angularApp')
  .controller('AdvancedSampleResultsCtrl', function (sampleService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.utilsService = utilsService;
    $scope.data = $scope.sampleData.data;

    // $scope.sampleData.data = {"samples":[{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1","20917507"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":22424833,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":23768335,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":21867796,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":21867800,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":21867776,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":67928066,"sampleName":"SY-NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["SY-NCL-23-1","67928066"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":68288512,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288537,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288532,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288534,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":68288518,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":68288522,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":68288519,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":69500928,"sampleName":"NCL-23-1-SY-CharTest","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1-SY-CharTest","69500928"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":69599232,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599257,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599252,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599254,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":69599238,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":69599242,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":69599239,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":70778880,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":73367552,"sampleName":"NCL-23-1-SY-Test-CharDate","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1-SY-Test-CharDate","73367552"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":73498624,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498649,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498644,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498646,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":73498634,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":73498631,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":57868288,"sampleName":"SY-Test-08-15","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["SY-Test-08-15","57868288"]},{"type":"POC","val":["DC<br>23098"]},{"type":"Characterization","val":[]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":98238464,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false}],"columnTitles":["Sample Name","Point Of Contact Name","in vitro characterization","physico-chemical characterization"],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false,"errors":[]};
    // $scope.test = {"samples":[{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1","20917507"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":22424833,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":23768335,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":21867796,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":21867800,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":21867776,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":67928066,"sampleName":"SY-NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["SY-NCL-23-1","67928066"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":68288512,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288537,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288532,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":68288534,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":68288518,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":68288522,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":68288519,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":69500928,"sampleName":"NCL-23-1-SY-CharTest","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1-SY-CharTest","69500928"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":69599232,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599257,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599252,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":69599254,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":69599238,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":69599242,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":69599239,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":70778880,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":73367552,"sampleName":"NCL-23-1-SY-Test-CharDate","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["NCL-23-1-SY-Test-CharDate","73367552"]},{"type":"POC","val":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":73498624,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498649,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498644,"displayName":"in vitro characterization:blood contact"},{"parentSampleId":0,"charId":73498646,"displayName":"in vitro characterization:blood contact"}]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":73498634,"displayName":"physico-chemical characterization:molecular weight"},{"parentSampleId":0,"charId":73498631,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":57868288,"sampleName":"SY-Test-08-15","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"columns":[{"type":"Sample","val":["SY-Test-08-15","57868288"]},{"type":"POC","val":["DC<br>23098"]},{"type":"Characterization","val":[]},{"type":"Characterization","val":[{"parentSampleId":0,"charId":98238464,"displayName":"physico-chemical characterization:molecular weight"}]}],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false}],"columnTitles":["Sample Name","Point Of Contact Name","in vitro characterization","physico-chemical characterization"],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false,"errors":[]};
    // $scope.pocDetails ={"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":1407951881000,"keywords":"MAGNEVIST<br>MRI<br>NCL-23<br>QA<br>TEST","pointOfContactMap":{"organizationDisplayName":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"],"primaryContact":["true","false","false"],"role":["","",""],"contactPerson":["John Doe","",""]},"pocBeanDomainId":52985856,"availableEntityNames":null,"caNanoLabScore":null,"mincharScore":null,"chemicalAssocs":null,"physicoChars":null,"invitroChars":null,"invivoChars":null,"caNano2MINChar":null,"errors":[],"caNanoMINChar":null};
    // go back to search form //
    $scope.goBack = function() {
      $location.path("/advancedSampleSearch").replace();      
    }; 


    // pops up modal window and passes in data. parameters = data and templateUrl and controller //
    $scope.popupModal = function(data,templateUrl,controller) {
        var modalInstance = $modal.open({
          templateUrl: templateUrl,
          controller: controller,
          windowClass: 'sample-modal-window',
          resolve: {
            data: function () {
              return data;
            }   
          }
        });
    };

    // call rest call to get poc details when details link is clicked //
    $scope.showPocDetails = function(sampleId) {
      $scope.loader = true;
      $http({method: 'GET', url: '/caNanoLab/rest/sample/view?sampleId='+sampleId}).
      success(function(data, status, headers, config) {
        $scope.pocDetails = data;
        $scope.loader = false;
        $scope.popupModal($scope.pocDetails,'views/sample/view/modal/pocDetails.html','PocDetailsCtrl')
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
        $scope.loader = false;
      });       
    };

    // gets sample being clicked on and determines if it is edit or view. //
    // redirects to updateSample or sample page //
    $scope.displaySample = function(sampleId) {
      $http({method: 'GET', url: '/caNanoLab/rest/sample/isSampleEditable?sampleId='+sampleId}).
      success(function(data, status, headers, config) {
        var isEditable = data;
        if (isEditable=='true') {
            $location.path("/editSample").search({'sampleId':sampleId}).replace();      
        }
        else {
            $location.path("/sample").search({'sampleId':sampleId}).replace();      
        };
      }).
      error(function(data, status, headers, config) {
        $scope.message = data;
      });
    };


    var data = $scope.sampleData.data.samples;
    // $scope.sampleData.data  = data;
    if (data==null) {
      $scope.sampleData.data = [];
      data = [];
      $location.path("/advancedSampleSearch").search({}).replace();
    }  

    $scope.tableParams = new ngTableParams(
        {
            page: 1,            // show first page
            count: 20           // count per page     
        },
        {
            counts: [], // hide page counts control

            total: data.length, // length of data
            getData: function($defer, params) {
            // use build-in angular filter
            var orderedData = params.sorting() ? 
            $filter('orderBy')(data, params.orderBy()) : data;
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            } 
        }); 

  
  });
