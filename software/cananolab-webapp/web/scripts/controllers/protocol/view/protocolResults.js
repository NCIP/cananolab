'use strict';
var app = angular.module('angularApp')
    .controller('ProtocolResultsCtrl', function (protocolService,navigationService,groupService,utilsService,$scope,$rootScope,$filter,ngTableParams,$http,$location) {
        $rootScope.tabs = navigationService.get();
        $rootScope.groups = groupService.getGroups.data.get();
        $scope.protocolData = protocolService.protocolData;
        $scope.utilsService = utilsService;
        $scope.loggedInUser = groupService.getUserName();
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
        
        $scope.addToFavorites = function(protocolId, protocolName, fileId, editable) {
            $scope.loader = true;
            
            $scope.favoriteBean = {"dataType" : "protocol", "dataName" : protocolName, "dataId" : protocolId, "protocolFileId" : fileId, "editable" : editable, "loginName" : $scope.loggedInUser.name};

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
