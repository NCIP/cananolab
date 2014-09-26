'use strict';
var app = angular.module('angularApp')
  .controller('AdvancedSampleResultsCtrl', function (sampleService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location,$modal) {
    $rootScope.tabs = navigationService.get();
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.sampleData = sampleService.sampleData;
    $scope.utilsService = utilsService;
    // $scope.sampleData.data = {"samples":[{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":67928066,"sampleName":"SY-NCL-23-1","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["C-Sixty (CNI)","DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":69500928,"sampleName":"NCL-23-1-SY-CharTest","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["C-Sixty (CNI)","DC<br>23098","CP_UCLA_CalTech"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669696,"sampleName":"DC-GZhangMSEC2010-01","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669697,"sampleName":"DC-GZhangMSEC2010-02","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669698,"sampleName":"DC-GZhangMSEC2010-03","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669699,"sampleName":"DC-GZhangMSEC2010-04","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669700,"sampleName":"DC-GZhangMSEC2010-05","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669701,"sampleName":"DC-GZhangMSEC2010-06","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669702,"sampleName":"DC-GZhangMSEC2010-07","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669703,"sampleName":"DC-GZhangMSEC2010-08","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669704,"sampleName":"DC-GZhangMSEC2010-09","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669705,"sampleName":"DC-GZhangMSEC2010-10","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":32669707,"sampleName":"DC-GZhangMSEC2010-11","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false},{"sampleId":51806210,"sampleName":"copy-AIST-KFujitaTL2009-02","pointOfContact":null,"composition":null,"functions":null,"characterizations":null,"dataAvailability":null,"createdDate":null,"editable":false,"orgNames":["GATECH_EMORY","DC<br>23098","C-Sixty (CNI)","KU_JSTC_JAPAN<br>400 Shady Grove Rd.<br>Rockvillezzxczxc","GATECH_EMORY","DC<br>23098"],"nanomaterialEntityNames":[],"functionalizingEntityName":null,"functionNames":[],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false}],"columnTitles":["Sample Name","Point Of Contact Name"],"showPOC":true,"showNanomaterialEntity":false,"showFunctionalizingEntity":false,"showFunction":false,"errors":[]};
    $scope.data = $scope.sampleData.data;
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

    $scope.tableHtml = function(val) {
      if (val||val==true) {
        return 1
      }
      else {
        return 0;
      };

    };

    $scope.tableHtml2 = function(val) {
      return 0;
    };

    var data = $scope.sampleData.data.samples;
    $scope.sampleData.data  = data;
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
