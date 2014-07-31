'use strict';
var app = angular.module('angularApp')

    .controller('EditPublicationCtrl', function ($rootScope,$scope,$http,$location,$timeout,$routeParams) {
        $scope.publicationForm = {};
        //$scope.sampleData = sampleService.sampleData;
        $rootScope.navTree=false;
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.get();

        $scope.addNewAuthor = false;
        $scope.showAuthorTable = false;
        $scope.showSearchSampleTable = false;

        // Access variables
        $scope.publicationForm.authors = [];
        $scope.dataType = 'Publication';
        $scope.parentFormName = 'PublicationForm';
        $scope.curator = false;
        $scope.groupAccesses = [];
        $scope.userAccesses = [];
        $scope.addAccess = false;
        $scope.showAddAccessButton = true;
        $scope.showCollaborationGroup = true;
        $scope.showAccessuser = false;
        $scope.showAccessSelection = false;

        //$scope.$on('$viewContentLoaded', function(){
            $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/setup'}).
                success(function(data, status, headers, config) {
                    $scope.publicationCategories = data.publicationCategories;
                    $scope.publicationStatuses = data.publicationStatuses;
                    $scope.publicationResearchAreas = data.publicationResearchAreas;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });
        //});

        var publicationId = $routeParams.publicationId;

        if( publicationId != null ) {
            /* $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/edit?publicationId=' + publicationId}).
                success(function(data, status, headers, config) {
                    $scope.publicationForm = data;
                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                });
             */

            $scope.$on('$viewContentLoaded', function(){
                $scope.publicationForm = {"sampleTitle":null,"title":"Multifunctional polymeric nano-systems for RNA interference therapy","category":"peer review article","status":"published","pubMedId":null,"digitalObjectId":null,"journalName":"Biological Drug Products: Development and Strategies","year":null,"volume":null,"startPage":null,"endPage":null,"authors":[{"lastName":"Iyer","initial":"AK","firstName":"P",id:"1000"}],"authorId":null,"keywordsStr":"NCL-23","description":null,"researchAreas":null,"uri":null,"uriExternal":false,"fileId":42533664,"sampleId":null,"associatedSampleNames":"","groupAccesses":[{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[],"displayName":""},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"},{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[],"displayName":""},"groupName":"Public","roleName":"R","roleDisplayName":"read","accessBy":"group"}],"userAccesses":[],"protectedData":null,"isPublic":true,"isOwner":false,"ownerName":null,"createdBy":"canano_curator","userDeletable":false};

                $scope.groupAccesses = $scope.publicationForm.groupAccesses;
                $scope.userAccesses = $scope.publicationForm.userAccesses;

                if($scope.publicationForm.authors != null && $scope.publicationForm.authors.length > 0 ) {
                    $scope.showAuthorTable = true;
                }

            });
        }

        $scope.addAuthor = function() {
            var author = {
                id : null,
                firstName : null,
                lastName : null,
                initial : null
            };
            var newAuthor = false
            author.id = $scope.publicationForm.theAuthor.id;
            if (author.id == null || author.id.length == 0) {
                author.id = -1000 - $scope.publicationForm.authors.length;
                newAuthor = true;
            }
            author.firstName = $scope.publicationForm.theAuthor.firstName;
            author.lastName = $scope.publicationForm.theAuthor.lastName;
            author.initial = $scope.publicationForm.theAuthor.initial;
            if (author.firstName.length > 0 && author.lastName.length > 0 && author.initial.length > 0) {
                if (newAuthor) {
                    $scope.publicationForm.authors.push(author);
                }
                else {
                    var k;
                    for (k = 0; k < $scope.publicationForm.authors.length; ++k)
                    {
                        var authorL = $scope.publicationForm.authors[k];
                        if (author.id == authorL.id ) {
                            $scope.publicationForm.authors[k].firstName = author.firstName;
                            $scope.publicationForm.authors[k].lastName = author.lastName;
                            $scope.publicationForm.authors[k].initial = author.initial;
                        }
                    }
                }
                $scope.addNewAuthor=false;
                $scope.showAuthorTable = true;

                $scope.publicationForm.theAuthor.firstName = '';
                $scope.publicationForm.theAuthor.lastName = '';
                $scope.publicationForm.theAuthor.initial = '';
                $scope.publicationForm.theAuthor.id = '';
            } else {
                alert("Please fill in values");
            }

        }

        $scope.editAuthor = function(id) {
            var k;
            for (k = 0; k < $scope.publicationForm.authors.length; ++k)
            {
                var author = $scope.publicationForm.authors[k];
                if (id == author.id ) {
                    $scope.publicationForm.theAuthor.firstName = author.firstName;
                    $scope.publicationForm.theAuthor.lastName = author.lastName;
                    $scope.publicationForm.theAuthor.initial = author.initial;
                    $scope.publicationForm.theAuthor.id = author.id;
                    $scope.addNewAuthor=true;
                }
            }
        }

        $scope.deleteAuthor = function() {
            var k;
            for (k = 0; k < $scope.publicationForm.authors.length; ++k)
            {
                var author = $scope.publicationForm.authors[k];
                alert($scope.publicationForm.theAuthor.id);
                if ($scope.publicationForm.theAuthor.id == author.id ) {
                    $scope.publicationForm.authors.splice(k,1);
                }
            }
            $scope.addNewAuthor=false;
            if( $scope.publicationForm.authors.length > 0 ) {
                $scope.showAuthorTable = true;
            }
            else {
                $scope.showAuthorTable = false;
            }

            $scope.publicationForm.theAuthor.firstName = '';
            $scope.publicationForm.theAuthor.lastName = '';
            $scope.publicationForm.theAuthor.initial = '';
            $scope.publicationForm.theAuthor.id = '';
        }

        $scope.searchMatchedSamples = function() {
/*            $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/getSamples?searchStr=ncl'}).
                success(function(data, status, headers, config) {
                    $scope.sampleResults = data;
                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                });
*/
            $scope.sampleResults = ["GATECH_UCSF-EDickersonCL2008-01","NCL-16","NCL-17","NCL-19","NCL-20-1","NCL-21-1","NCL-22-1","NCL-23-1","NCL-24-1","NCL-25-1","NCL-26-1","NCL-42","NCL-45","NCL-48","NCL-48-4","NCL-49","NCL-49-2","NCL-50-1","NCL-51-3","NCL-MGelderman-IJN2008-01","NCL-MGelderman-IJN2008-02","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02"];

        };


        $scope.doSubmit = function() {
            $scope.loader = true;

            $http({method: 'POST', url: 'http://localhost:8080/caNanoLab/rest/publication/submitPublication',data: $scope.publicationForm}).
                success(function(data, status, headers, config) {
                    // $rootScope.sampleData = data;
                    //$scope.sampleData.data = data;
                    //$location.path("/sampleResults").replace();
                	
                	alert(data);

                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.message = data;
                });
        };

        $scope.resetForm = function() {
            $scope.publicationForm = {};
            $scope.characterizationsList = [];
        };

        $scope.updateAssociatedSamples = function() {
            var selectedSamples = $scope.publicationForm.matchedSampleSelect;
            var samplesPerLine = selectedSamples.join("\n");
            $scope.publicationForm.sampleNamesStr = samplesPerLine;
        };

    });


