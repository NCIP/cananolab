'use strict';
var app = angular.module('angularApp')

  .controller('SampleResultsCtrl', function (navigationFactory,groupsFactory,$rootScope,$scope,$http) {
    $rootScope.tabs = navigationFactory.query();
    $rootScope.groups = groupsFactory.get();

    $scope.myData = [{name: "Moroni", age: 50},
                       {name: "Tiancum", age: 43},
                       {name: "Jacob", age: 27},
                       {name: "Nephi", age: 29},
                       {name: "Enos", age: 34}];
    $scope.gridOptions = { data: 'myData' };      
    


          // $scope.testData = [{"sampleId":11337795,"sampleName":"NCL-MGelderman-IJN2008-02","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-MGelderman-IJN2008-02"},{"sampleId":11337794,"sampleName":"NCL-MGelderman-IJN2008-01","pointOfContact":"NCL<br>CBER, FDA, 1401 Rockville Pike<br>HFM 335<br>Rockville MD 20852-1448 USA","functions":[],"characterizations":["Size"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-MGelderman-IJN2008-01"},{"sampleId":24063238,"sampleName":"NCL-49","pointOfContact":"Mark Kester PSU","functions":[],"characterizations":["BloodContact","Cytotoxicity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-49"},{"sampleId":24063237,"sampleName":"NCL-48","pointOfContact":"Mark Kester PSU","functions":[],"characterizations":["Cytotoxicity","Size","Surface"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-48"},{"sampleId":24063236,"sampleName":"NCL-45","pointOfContact":"C-Sixty (CNI)","functions":[],"characterizations":["BloodContact","MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 26%; MINChar: 33%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-45"},{"sampleId":24063235,"sampleName":"NCL-42","pointOfContact":"C-Sixty (CNI)","functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","OxidativeStress","Purity","Size","Surface"],"dataAvailability":"caNanoLab: 40%; MINChar: 44%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-42"},{"sampleId":24063234,"sampleName":"NCL-19","pointOfContact":"C-Sixty (CNI)","functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-19"},{"sampleId":24063233,"sampleName":"NCL-17","pointOfContact":"C-Sixty (CNI)","functions":[],"characterizations":["BloodContact","Cytotoxicity"],"dataAvailability":"caNanoLab: 20%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-17"},{"sampleId":24063232,"sampleName":"NCL-16","pointOfContact":"C-Sixty (CNI)","functions":[],"characterizations":["BloodContact","Cytotoxicity","OxidativeStress"],"dataAvailability":"caNanoLab: 23%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-16"},{"sampleId":20917514,"sampleName":"NCL-51-3","pointOfContact":"Mark Kester","functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-51-3"},{"sampleId":20917513,"sampleName":"NCL-50-1","pointOfContact":"Mark Kester","functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-50-1"},{"sampleId":20917512,"sampleName":"NCL-49-2","pointOfContact":"Mark Kester","functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-49-2"},{"sampleId":20917511,"sampleName":"NCL-48-4","pointOfContact":"Mark Kester","functions":[],"characterizations":[],"dataAvailability":"caNanoLab: 13%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-48-4"},{"sampleId":20917510,"sampleName":"NCL-26-1","pointOfContact":"DNT","functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-26-1"},{"sampleId":20917509,"sampleName":"NCL-25-1","pointOfContact":"DNT","functions":[],"characterizations":["Purity"],"dataAvailability":"caNanoLab: 16%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-25-1"},{"sampleId":20917508,"sampleName":"NCL-24-1","pointOfContact":"DNT","functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction"],"dataAvailability":"caNanoLab: 30%; MINChar: 11%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-24-1"},{"sampleId":20917507,"sampleName":"NCL-23-1","pointOfContact":"DNT","functions":["ImagingFunction"],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 50%; MINChar: 33%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-23-1"},{"sampleId":20917506,"sampleName":"NCL-22-1","pointOfContact":"DNT","functions":[],"characterizations":["BloodContact","Cytotoxicity","ImmuneCellFunction","MolecularWeight","PhysicalState","Purity","Size","Solubility"],"dataAvailability":"caNanoLab: 40%; MINChar: 33%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-22-1"},{"sampleId":20917505,"sampleName":"NCL-21-1","pointOfContact":"DNT","functions":[],"characterizations":["MolecularWeight","Purity"],"dataAvailability":"caNanoLab: 20%; MINChar: 22%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-21-1"},{"sampleId":20917504,"sampleName":"NCL-20-1","pointOfContact":"DNT","functions":[],"characterizations":["MolecularWeight","Purity","Size"],"dataAvailability":"caNanoLab: 23%; MINChar: 33%","pocBeanDomainId":0,"otherPOCBeans":[],"keywords":null,"pointOfContactInfo":null,"createdDate":1404875323037,"composition":"NCL-20-1"}];

          // $scope.filterOptions = {
          //     filterText: "",
          //     useExternalFilter: true
          // }; 
          // $scope.totalServerItems = 0;
          // $scope.pagingOptions = {
          //     pageSizes: [10],
          //     pageSize: 10,
          //     currentPage: 1
          // };  

          // $scope.setPagingData = function(data, page, pageSize){  
          //   var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
          //   $scope.myData = pagedData;
          //   $scope.totalServerItems = data.length;
          //   if (!$scope.$$phase) {
          //       $scope.$apply();
          //   }
          // };

          // $scope.getPagedDataAsync = function (pageSize, page, searchText) {
          //   setTimeout(function () {
          //       var data;
          //       if (searchText) {
          //           var ft = searchText.toLowerCase();
          //           $scope.setPagingData($scope.testData,page,pageSize);
          //       } else {
          //               $scope.setPagingData($scope.testData,page,pageSize);
          //       }
          //   }, 100);
          // };
            
          // $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage);
            
          // $scope.$watch('pagingOptions', function (newVal, oldVal) {
          //   if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
          //     $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
          //   }  
          // }, true);

          // $scope.$watch('filterOptions', function (newVal, oldVal) {
          //   if (newVal !== oldVal) {
          //     $scope.getPagedDataAsync($scope.pagingOptions.pageSize, $scope.pagingOptions.currentPage, $scope.filterOptions.filterText);
          //   }
          // }, true);
            
          // $scope.gridOptions = {
          //   data: 'myData',
          //   enablePaging: true,
          //   showFooter: true,
          //   totalServerItems: 'totalServerItems',
          //   pagingOptions: $scope.pagingOptions,
          //   filterOptions: $scope.filterOptions
          // };

   
  });
