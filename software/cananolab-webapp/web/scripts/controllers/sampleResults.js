'use strict';
var app = angular.module('angularApp')

  .controller('SampleResultsCtrl', function (sampleService,navigationService,groupService,$rootScope,$scope,$http,$location,$modal) {
    $rootScope.tabs = navigationService.query();
    $rootScope.groups = groupService.get();
    $scope.sampleData = sampleService.sampleData;
    $rootScope.navTree=false;
    // $scope.sampleData.data = [{"sampleId":27131907,"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02","pointOfContact":"UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA","composition":["MetalParticle"],"functions":["ImagingFunction"],"characterizations":[],"dataAvailability":"N/A","createdDate":1275683278000},{"sampleId":27131906,"sampleName":"UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","pointOfContact":"UMC_RadiolD<br>Department of Radiology<br>University of Missouri-Columbia<br>Columbia MO 65212 USA","composition":["MetalParticle"],"functions":[],"characterizations":["BloodContact","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1275683278000},{"sampleId":12451840,"sampleName":"GATECH_UCSF-EDickersonCL2008-01","pointOfContact":"GATECH_UCSF<br>Georgia Institute of Technology<br>Atlanta GA 30332","composition":["OtherNanomaterialEntity"],"functions":["ImagingFunction","TherapeuticFunction"],"characterizations":["Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":1240200000000},{"sampleId":11337795,"sampleName":"NCL-MGelderman-IJN2008-02","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","composition":["Fullerene"],"functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1238731200000},{"sampleId":11337794,"sampleName":"NCL-MGelderman-IJN2008-01","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","composition":["Fullerene"],"functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1238731200000},{"sampleId":24063238,"sampleName":"NCL-49","pointOfContact":"Mark Kester PSU","composition":["Liposome"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","createdDate":1181275200000},{"sampleId":24063237,"sampleName":"NCL-48","pointOfContact":"Mark Kester PSU","composition":["Liposome"],"functions":[],"characterizations":["Cytotoxicity","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1181275200000},{"sampleId":24063236,"sampleName":"NCL-45","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 26%; MINChar: 33%","createdDate":1179374400000},{"sampleId":24063235,"sampleName":"NCL-42","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","OxidativeStress","Purity","Size","Surface"],"dataAvailability":"caNanoLab: 40%; MINChar: 44%","createdDate":1179374400000},{"sampleId":24063234,"sampleName":"NCL-19","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000},{"sampleId":24063233,"sampleName":"NCL-17","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity"],"dataAvailability":"caNanoLab: 20%; MINChar: 11%","createdDate":1179374400000},{"sampleId":24063232,"sampleName":"NCL-16","pointOfContact":"C-Sixty (CNI)","composition":["Fullerene"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","createdDate":1179374400000},{"sampleId":20917514,"sampleName":"NCL-51-3","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917513,"sampleName":"NCL-50-1","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917512,"sampleName":"NCL-49-2","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917511,"sampleName":"NCL-48-4","pointOfContact":"Mark Kester","composition":["Liposome"],"functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917510,"sampleName":"NCL-26-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917509,"sampleName":"NCL-25-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917508,"sampleName":"NCL-24-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction"],"dataAvailability":"caNanoLab: 30%; MINChar: 11%","createdDate":1152504000000},{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 50%; MINChar: 33%","createdDate":1152504000000},{"sampleId":20917506,"sampleName":"NCL-22-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 40%; MINChar: 33%","createdDate":1152504000000},{"sampleId":20917505,"sampleName":"NCL-21-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity"],"dataAvailability":"caNanoLab: 20%; MINChar: 22%","createdDate":1152504000000},{"sampleId":20917504,"sampleName":"NCL-20-1","pointOfContact":"DNT","composition":["Dendrimer"],"functions":[],"characterizations":["MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","createdDate":1152504000000}];

    $scope.goBack = function() {
      $location.path("/searchSample").replace();      
    };

    $scope.openDataAvailability = function(sampleId) {

      $http({method: 'GET', url: '/caNanoLab/rest/sample/viewDataAvailability',params: {"sampleId":sampleId}}).
      success(function(data, status, headers, config) {
        var modalInstance = $modal.open({
          templateUrl: 'views/sampleDataAvailability.html',
          controller: 'SampleDataAvailabilityCtrl',
          size: 'sm',
          resolve: {
            sampleId: function () {
              return sampleId;
            },
            sampleData: function() {
              return data;
            }
          }
        });
      }).
      error(function(data, status, headers, config) {
        // called asynchronously if an error occurs
        // or server returns response with an error status.
        $scope.message = data;
      });        
    };

    if ($scope.sampleData.data==null) {
      $scope.sampleData.data = [];
      $location.path("/searchSample").replace();
    }


    $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
    }; 

    $scope.totalServerItems = 0;
    $scope.pagingOptions = {
        pageSizes: [10],
        pageSize: 10,
        currentPage: 1
    };  

    $scope.setPagingData = function(data, page, pageSize){  
      var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
      $scope.myData = pagedData;
      $scope.totalServerItems = data.length;
      if (!$scope.$$phase) {
          $scope.$apply();
      }
    };

    $scope.getPagedDataAsync = function (pageSize, page, searchText) {
      setTimeout(function () {
          var data;
          if (searchText) {
              var ft = searchText.toLowerCase();
              $scope.setPagingData($scope.sampleData.data,page,pageSize);
          } else {
                  $scope.setPagingData($scope.sampleData.data,page,pageSize);
          }
      }, 100);
    };
      
    $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
      
    $scope.$watch('pagingOptions', function (newVal, oldVal) {
      if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
      }  
    }, true);

    $scope.$watch('filterOptions', function (newVal, oldVal) {
      if (newVal !== oldVal) {
        $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
      }
    }, true);
      
    $scope.gridOptions = {
      rowHeight:30,
      data: 'myData',
      enablePaging: true,
      showFooter: true,
      enableRowSelection: false,      
      totalServerItems: 'totalServerItems',
      pagingOptions: $scope.pagingOptions,
      filterOptions: $scope.filterOptions,
      columnDefs:[
          {field: 'sampleId', displayName: '', enableCellEdit: false,width:'38px',cellTemplate:'<a href="#/sample?sampleId={{row.getProperty(col.field)}}">View</a>',sortable:false},
          {field: 'sampleName', displayName: 'Sample Name', enableCellEdit: false, width:'140px'},
          {field: 'pointOfContact', displayName: 'Primary Point of Contact',width:'150px', enableCellEdit: false,resizable:true,cellTemplate:'<div ng-bind-html="row.getProperty(col.field)" />'},
          {field: 'composition', displayName: 'Composition', enableCellEdit: false, width:'107px', cellTemplate:'<div grid-list="composition">{{row.getProperty(col.field)}}</div>'},
          {field: 'functions', displayName: 'Functions', enableCellEdit: false, width:'93px',cellTemplate:'<div grid-list="functions">{{row.getProperty(col.field)}}</div>'},
          {field: 'characterizations', displayName: 'Characterizations', enableCellEdit: false, width:'112px', cellTemplate:'<div grid-list="characterizations">{{row.getProperty(col.field)}}</div>'},
          {field: 'dataAvailability', displayName: 'Date Availability', enableCellEdit: false, width:'177px',cellTemplate:'<a href="" ng-click="openDataAvailability({{row.getProperty('+"'sampleId'"+')}})">{{row.getProperty(col.field)}}</a>'},
          {field: 'createdDate', displayName: 'Created Date', width:'81px', enableCellEdit: false}
          ]     
    };

   
  });
