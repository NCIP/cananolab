'use strict';
var app = angular.module('angularApp')
    .controller('ProtocolResultsCtrl', function (protocolService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();
        $scope.protocolData = protocolService.protocolData;
        $scope.utilsService = utilsService;
        $scope.loggedInUser = groupService.getUserName();
        // $scope.publicationData = {"data":[{"displayName":"NCL200702A CERAMIDE LIPOSOMES FOR MARK KESTER PSU. <a href=rest/publication/download?fileId=24620294 target='_self'>View</a>.","publicationType":"report","researchAreas":null,"sampleNames":["NCL-48-4","NCL-49-2","NCL-50-1","NCL-51-3"],"descriptionDetail":"The objective of the Penn State University-NCL collaboration is to characterize C6-ceramide liposomes as a drug delivery platform. ","status":"published","createdDate":1201188472000},{"displayName":"NCL200710A FUNCTIONALIZED FULLERENES FOR CSIXTY INC. <a href=rest/publication/download?fileId=24620293 target='_self'>View</a>.","publicationType":"report","researchAreas":null,"sampleNames":["NCL-16","NCL-17","NCL-19","NCL-42","NCL-45"],"descriptionDetail":"The objective of the C-Sixty, Inc. - NCL collaboration is to aid C-Sixty, Inc. in identifying the most promising candidate from a series of fullerene based antioxidants, and to investigate potential applications related to cancer therapy. The antioxidant candidates submitted for testing were AF1 (NCL16), AF3 (NCL17), C3 (NCL19), DF1 (NCL42), and DF1-mini (NCL45). ","status":"published","createdDate":1201188331000},{"displayName":"DENDRITIC NANOTECHNOLOGIES 122006. <a href=rest/publication/download?fileId=23178496 target='_self'>View</a>.","publicationType":"report","researchAreas":null,"sampleNames":["NCL-20-1","NCL-21-1","NCL-22-1","NCL-23-1","NCL-24-1","NCL-25-1","NCL-26-1"],"descriptionDetail":"Dendrimer-Based MRI Contrast Agents prepared for Dendritic Nanotechnologies, December, 2006","status":"published","createdDate":1166619993000}]}
        $scope.goBack = function() {
            $location.path("/searchProtocol").replace();
        };


        var data = $scope.protocolData.data;
        
        if (data==null) {
            $scope.protocolData.data = [];
            $location.path("/searchProtocol").replace();
        }

        $scope.tableParams = new ngTableParams(
            {
                page: 1,            // show first page
                count: 10           // count per page
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
        
        $scope.addToFavorites = function(protocolId, protocolName, fileId) {
            $scope.loader = true;
            
            $scope.favoriteBean = {"dataType" : "protocol", "dataName" : protocolName, "dataId" : protocolId, "protocolFileId" : fileId, "loginName" : $scope.loggedInUser.name};

            $http({method: 'POST', url: '/caNanoLab/rest/core/addFavorite',data: $scope.favoriteBean}).
                success(function(data, status, headers, config) {
                     var hrefEl = document.getElementById('href' + protocolId);
                     var msgEl = document.getElementById('msg' + protocolId);
                     msgEl.innerHTML = data;
                     hrefEl.style.visibility="hidden";
                     $scope.loader = false;

                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.messages = data;
                });
        };        


    });
