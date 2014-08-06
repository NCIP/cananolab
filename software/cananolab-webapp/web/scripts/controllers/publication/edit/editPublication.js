'use strict';
var app = angular.module('angularApp')

    .controller('EditPublicationCtrl', function (groupService,$rootScope,$scope,$http,$location,$timeout,$routeParams) {
        $scope.publicationForm = {};
        //$scope.sampleData = sampleService.sampleData;
        $rootScope.navTree=false;
        //$rootScope.tabs = navigationService.query();
        //$rootScope.groups = groupService.get();

        $scope.addNewAuthor = false;
        $scope.showAuthorTable = false;
        $scope.showSearchSampleTable = false;
        $scope.theAuthor = {};
        $scope.publicationForm.authors = [];
        $scope.localForm = {};

        // Access variables
        $scope.publicationForm.theAccess = {};
        $scope.accessForm = {};
        $scope.dataType = 'Publication';
        $scope.parentFormName = 'publicationForm';
        $scope.accessForm.theAcccess = {};
        $scope.accessForm.theAcccess.userBean = {};
        $scope.curator = false;
        $scope.groupAccesses = [];
        $scope.userAccesses = [];
        $scope.addAccess = false;
        $scope.showAddAccessButton = true;
        $scope.showCollaborationGroup = true;
        $scope.showAccessuser = false;
        $scope.showAccessSelection = false;
        $scope.accessForm.theAccess = {};
        $scope.accessForm.theAccess.groupName = '';
        $scope.accessForm.theAccess.userBean = {};
        $scope.accessForm.theAccess.userBean.loginName = '';
        $scope.access = {};
        $scope.access.groupName = '';
        $scope.access.loginName = '';
        $scope.publicationForm.isPublic = false;
        $scope.accessForm.theAccess.accessBy = 'group';        
        $rootScope.groups = groupService.get();
        console.log($rootScope.groups);
        for (var logUser in $rootScope.groups) break;
        console.log(logUser);

//        $scope.curator = $rootScope.groups.indexOf("Curator") != -1;


        //$scope.$on('$viewContentLoaded', function(){
        $http({method: 'GET', url: '/caNanoLab/rest/publication/setup'}).
            success(function(data, status, headers, config) {
                $scope.publicationCategories = data.publicationCategories;
                $scope.publicationStatuses = data.publicationStatuses;
                $scope.publicationResearchAreas = data.publicationResearchAreas;
                $scope.csmRoleNames = data.csmRoleNames;
            }).
            error(function(data, status, headers, config) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
                $scope.message = data;
            });
        //});

        var publicationId = $routeParams.publicationId;

        if( publicationId != null ) {
            $http({method: 'GET', url: '/caNanoLab/rest/publication/edit?publicationId=' + publicationId}).
                success(function(data, status, headers, config) {
                    $scope.publicationForm = data;
                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                });


            $scope.$on('$viewContentLoaded', function(){
                //$scope.publicationForm = {"sampleTitle":null,"title":"Multifunctional polymeric nano-systems for RNA interference therapy","category":"peer review article","status":"published","pubMedId":null,"digitalObjectId":null,"journalName":"Biological Drug Products: Development and Strategies","year":null,"volume":null,"startPage":null,"endPage":null,"authors":[{"lastName":"Iyer","initial":"AK","firstName":"P",id:"1000"}],"authorId":null,"keywordsStr":"NCL-23","description":null,"researchAreas":null,"uri":null,"uriExternal":false,"fileId":42533664,"sampleId":null,"associatedSampleNames":"","groupAccesses":[{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":true,"domain":null,"groupNames":[],"displayName":""},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"},{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":null,"phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[],"displayName":""},"groupName":"Public","roleName":"R","roleDisplayName":"read","accessBy":"group"}],"userAccesses":[],"protectedData":null,"isPublic":true,"isOwner":false,"ownerName":null,"createdBy":"canano_curator","userDeletable":false};

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
            author.id = $scope.theAuthor.id;
            if (author.id == null || author.id.length == 0) {
                author.id = -1000 - $scope.publicationForm.authors.length;
                newAuthor = true;
            }
            author.firstName = $scope.theAuthor.firstName;
            author.lastName = $scope.theAuthor.lastName;
            author.initial = $scope.theAuthor.initial;
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

                $scope.theAuthor.firstName = '';
                $scope.theAuthor.lastName = '';
                $scope.theAuthor.initial = '';
                $scope.theAuthor.id = '';
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
                    $scope.theAuthor.firstName = author.firstName;
                    $scope.theAuthor.lastName = author.lastName;
                    $scope.theAuthor.initial = author.initial;
                    $scope.theAuthor.id = author.id;
                    $scope.addNewAuthor=true;
                }
            }
        }

        $scope.deleteAuthor = function() {
            var k;
            for (k = 0; k < $scope.publicationForm.authors.length; ++k)
            {
                var author = $scope.publicationForm.authors[k];
                if ($scope.theAuthor.id == author.id ) {
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

            $scope.theAuthor.firstName = '';
            $scope.theAuthor.lastName = '';
            $scope.theAuthor.initial = '';
            $scope.theAuthor.id = '';
        }

        $scope.searchMatchedSamples = function() {
            $http({method: 'GET', url: 'http://localhost:8080/caNanoLab/rest/publication/getSamples?searchStr=ncl'}).
                success(function(data, status, headers, config) {
                    $scope.sampleResults = data;
                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                }); 

            //$scope.sampleResults = ["GATECH_UCSF-EDickersonCL2008-01","NCL-16","NCL-17","NCL-19","NCL-20-1","NCL-21-1","NCL-22-1","NCL-23-1","NCL-24-1","NCL-25-1","NCL-26-1","NCL-42","NCL-45","NCL-48","NCL-48-4","NCL-49","NCL-49-2","NCL-50-1","NCL-51-3","NCL-MGelderman-IJN2008-01","NCL-MGelderman-IJN2008-02","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-01","UMC_HSTVAMC_NCL_NB-NChandaNNBM2010-02"];

        };


        $scope.doSubmit = function() {
            $scope.loader = true;

            $http({method: 'POST', url: '/caNanoLab/rest/publication/submitPublication',data: $scope.publicationForm}).
                success(function(data, status, headers, config) {
                    // $rootScope.sampleData = data;
                    //$scope.sampleData.data = data;
                    //$location.path("/sampleResults").replace();

                    alert(data);
                    
                    $location.search('message','Publication successfully saved with title "' + $scope.publicationForm.title + '"').path('/message').replace();

                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    // $rootScope.sampleData = data;
                    $scope.loader = false;
                    $scope.message = data;
                    alert(data);
                });
            
        };

        $scope.resetForm = function() {
            $scope.publicationForm = {};
            $scope.characterizationsList = [];
        };

        $scope.updateAssociatedSamples = function() {
            var selectedSamples = $scope.localForm.matchedSampleSelect;
            var samplesPerLine = selectedSamples.join("\n");
            $scope.publicationForm.sampleNamesStr = samplesPerLine;
        };

        $scope.fillPubMedInfo = function() {
            $http({method: 'GET', url: '/caNanoLab/rest/publication/retrievePubMedInfo?pubmedId=' + $scope.publicationForm.pubMedId}).
                success(function(data, status, headers, config) {
                    $scope.pubInfo = data;

                    $scope.publicationForm.title = $scope.pubInfo.domainFile.title;
                    $scope.publicationForm.description = $scope.pubInfo.description;
                    $scope.publicationForm.digitalObjectId = $scope.pubInfo.domainFile.digitalObjectId;
                    $scope.publicationForm.journalName = $scope.pubInfo.domainFile.journalName
                    $scope.publicationForm.startPage = $scope.pubInfo.domainFile.startPage
                    $scope.publicationForm.endPage = $scope.pubInfo.domainFile.endPage
                    $scope.publicationForm.volume = $scope.pubInfo.domainFile.volume
                    $scope.publicationForm.year = $scope.pubInfo.domainFile.year
                    $scope.publicationForm.authors = $scope.pubInfo.authors;
                    $scope.publicationForm.keywordsStr = $scope.pubInfo.keywordsStr;

                    if($scope.publicationForm.authors != null && $scope.publicationForm.authors.length > 0 ) {
                        $scope.showAuthorTable = true;
                    }

                }).
                error(function(data, status, headers, config) {
                    $scope.message = data;
                });


            //$scope.pubInfo = {"userAccesses":[],"groupAccesses":[],"theAccess":{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":"asdasdasdsadasdsadasd","phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[],"displayName":""},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"allAccesses":[],"publicStatus":false,"user":null,"userUpdatable":false,"userDeletable":false,"userIsOwner":false,"domainFile":{"createdBy":null,"createdDate":null,"description":null,"id":null,"name":null,"title":"A new granulation method for compressed tablets [proceedings].","type":null,"uri":null,"uriExternal":null,"findingCollection":null,"datumCollection":null,"keywordCollection":[{"id":null,"name":"Tablets","fileCollection":null},{"id":null,"name":"Phenylbutazone/administration & dosage","fileCollection":null},{"id":null,"name":"Hardness","fileCollection":null},{"id":null,"name":"Chemistry, Pharmaceutical/methods","fileCollection":null}],"category":null,"digitalObjectId":"","endPage":null,"journalName":"The Journal of pharmacy and pharmacology","pubMedId":12345,"researchArea":null,"startPage":"67P","status":null,"volume":"28 Suppl","year":1976,"authorCollection":null},"image":false,"keywordsStr":"Chemistry, Pharmaceutical/methods\r\nHardness\r\nPhenylbutazone/administration & dosage\r\nTablets","externalUrl":null,"uploadedFile":null,"newFileData":null,"createdDateStr":"","sampleNames":[],"researchAreas":null,"authors":[{"createdBy":null,"createdDate":null,"firstName":"M","id":null,"initial":"MH","lastName":"Rubinstein","publicationCollection":null}],"theAuthor":{"createdBy":null,"createdDate":null,"firstName":null,"id":null,"initial":null,"lastName":null,"publicationCollection":null},"sampleNamesStr":null,"fromSamplePage":false,"displayName":"Rubinstein, MH. A new granulation method for compressed tablets [proceedings]. The Journal of pharmacy and pharmacology. 1976; 28 Suppl. <a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/12345>PMID: 12345</a>.","pubMedDisplayName":"<a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/12345>PMID: 12345</a>","description":"","keywordsDisplayName":"Chemistry, Pharmaceutical/methods<br>Hardness<br>Phenylbutazone/administration &amp; dosage<br>Tablets","urlTarget":"_self"};
            //$scope.pubInfo = {"userAccesses":[],"groupAccesses":[],"theAccess":{"userBean":{"userId":null,"loginName":"","firstName":null,"lastName":null,"fullName":null,"organization":null,"department":null,"title":"asdffdsfdsfds","phoneNumber":null,"password":null,"emailId":null,"admin":false,"curator":false,"domain":null,"groupNames":[],"displayName":""},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"allAccesses":[],"publicStatus":false,"user":null,"userUpdatable":false,"userDeletable":false,"userIsOwner":false,"domainFile":{"createdBy":null,"createdDate":null,"description":null,"id":null,"name":null,"title":"Formulation studies on slow-release phosphate tablets for high-dosage administration in renal transplant patients [proceedings].","type":null,"uri":null,"uriExternal":null,"findingCollection":null,"datumCollection":null,"keywordCollection":[{"id":null,"name":"Tablets","fileCollection":null},{"id":null,"name":"Phosphates/administration & dosage","fileCollection":null},{"id":null,"name":"Delayed-Action Preparations","fileCollection":null},{"id":null,"name":"Chemistry, Pharmaceutical","fileCollection":null}],"category":null,"digitalObjectId":"","endPage":null,"journalName":"The Journal of pharmacy and pharmacology","pubMedId":12346,"researchArea":null,"startPage":"68P","status":null,"volume":"28 Suppl","year":1976,"authorCollection":null},"image":false,"keywordsStr":"Chemistry, Pharmaceutical\r\nDelayed-Action Preparations\r\nPhosphates/administration & dosage\r\nTablets","externalUrl":null,"uploadedFile":null,"newFileData":null,"createdDateStr":"","sampleNames":[],"researchAreas":null,"authors":[{"createdBy":null,"createdDate":null,"firstName":"R","id":null,"initial":"R","lastName":"Woodroffe","publicationCollection":null}],"theAuthor":{"createdBy":null,"createdDate":null,"firstName":null,"id":null,"initial":null,"lastName":null,"publicationCollection":null},"sampleNamesStr":null,"fromSamplePage":false,"displayName":"Woodroffe, R. Formulation studies on slow-release phosphate tablets for high-dosage administration in renal transplant patients [proceedings]. The Journal of pharmacy and pharmacology. 1976; 28 Suppl. <a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/12346>PMID: 12346</a>.","pubMedDisplayName":"<a target='_abstract' href=http://www.ncbi.nlm.nih.gov/pubmed/12346>PMID: 12346</a>","description":"","keywordsDisplayName":"Chemistry, Pharmaceutical<br>Delayed-Action Preparations<br>Phosphates/administration &amp; dosage<br>Tablets","urlTarget":"_self"};



        };

        $scope.getCollabGroups = function() {
            if ($scope.accessForm.theAccess.groupName === undefined || $scope.accessForm.theAccess.groupName === null) {
                $scope.accessForm.theAccess.groupName = '';
            }

            $http({method: 'GET', url: '/caNanoLab/rest/core/getCollaborationGroup?searchStr=' + $scope.accessForm.theAccess.groupName}).
                success(function(data, status, headers, config) {
                    $scope.collabGroups = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });

            //$scope.collabGroups = ["curator group", "NCI", "NCIP"];
            $scope.showAccessSelection=true;

        };

        $scope.getAccessUsers = function() {
            if ($scope.accessForm.theAccess.userBean.loginName === undefined || $scope.accessForm.theAccess.userBean.loginName === null) {
                $scope.accessForm.theAccess.userBean.loginName = '';
            }

            $http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr=' + $scope.accessForm.theAccess.userBean.loginName}).
                success(function(data, status, headers, config) {
                    $scope.accessUsers = data;
                }).
                error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $scope.message = data;
                });

            //$scope.accessUsers = {"lethai":"Le Thai","Omelchen":"Omelchenko Marina","burnskd":"Burns Kevin","canano_guest":"Guest Guest","grodzinp":"Grodzinski Piotr","swand":"Swan Don","skoczens":"Skoczen Sarah","sternstephan":"Stern Stephan","zolnik":"Zolnik Banu","hunseckerk":"Hunsecker Kelly","lipkeyfg":"Lipkey Foster","marina":"Dobrovolskaia Marina","pottert":"Potter Tim","uckunf":"Uckun Fatih","michal":"Lijowski Michal","mcneils":"Mcneil Scott","neunb":"Neun Barry","cristr":"Crist Rachael","zhengji":"Zheng Jiwen","frittsmj":"Fritts Martin","SchaeferH":"Schaefer Henry","benhamm":"Benham Mick","masoods":"Masood Sana","mclelandc":"McLeland Chris","torresdh":"Torres David","KlemmJ":"Klemm Juli","patria":"Patri Anil","hughesbr":"Hughes Brian","clogstonj":"Clogston Jeff","hinkalgw":"Hinkal George","MorrisS2":"Morris Stephanie","sharon":"Gaheen Sharon"};
            $scope.showAccessSelection=true;

        };

        $scope.hideAccessSection = function() {
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;
        }
        
        $scope.saveAccessSection = function() {
            $scope.loader = true;
            $scope.publicationForm.theAccess = $scope.accessForm.theAccess;
            $scope.addAccess=false;
            $scope.showAddAccessButton=true;
            
            if( $scope.accessForm.theAccess.accessBy == 'public') {
                $scope.publicationForm.isPublic = true;
            }

            $http({method: 'POST', url: '/caNanoLab/rest/publication/saveAccess',data: $scope.publicationForm}).
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
                    alert(data);
                });
        }; 
        
        $scope.uploadFile = function(element){
            //var file = $scope.localForm.uploadedFile;
            var file = element.files[0];
            console.log('file is ' + JSON.stringify(file));
            var uploadUrl = "/fileUpload";
            $scope.uploadFileToUrl(file, uploadUrl);
        };

        $scope.uploadFileToUrl = function(file, uploadUrl){
            var fd = new FormData();
            fd.append('file', file);
            $http.post(uploadUrl, fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
                .success(function(){
                })
                .error(function(){
                });
        };
        

    });


