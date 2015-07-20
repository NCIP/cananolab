'use strict';
var app = angular.module('angularApp')
  .controller('MainCtrl', function (sampleService, publicationService, protocolService, navigationService, groupService,$rootScope, $scope, $location,$http, $route,$cookieStore, $window, $cookies) {

    $scope.userActions = 1;
    $scope.loginShow = 0;
    $scope.authErrors = 0;
    $scope.homepage = true;
    $scope.errorMessages = [];
    $rootScope.tabs = navigationService.get({'homePage':'true'});
    $rootScope.groups = groupService.getGroups.data.get();
    $scope.searchSampleForm = {};
    $scope.sampleData = sampleService.sampleData;
    $scope.publicationData = publicationService.publicationData;    
    $scope.protocolData = protocolService.protocolData;

    $scope.$on('$viewContentLoaded', function(){
      $http({method: 'GET', url: '/caNanoLab/rest/core/initSetup' }).
        success(function(data, status, headers, config) {
          $scope.numOfPublicProtocols=data.numOfPublicProtocols;
          $scope.numOfPublicCharacterizations=data.numOfPublicCharacterizations;
          $scope.numOfPublicInvitroCharacterizations=data.numOfPublicInvitroCharacterizations;
          $scope.numOfPublicInvivoCharacterizations=data.numOfPublicInvivoCharacterizations;
          $scope.numOfPublicOtherCharacterizations=data.numOfPublicOtherCharacterizations;
          $scope.numOfPublicPhysicoChemicalCharacterizations=data.numOfPublicPhysicoChemicalCharacterizations;
          $scope.numOfPublicPublications=data.numOfPublicPublications;
          $scope.numOfPublicSamples=data.numOfPublicSamples;
          $scope.numOfPublicSources=data.numOfPublicSources;
        }).
        error(function(data, status, headers, config) {
          // called asynchronously if an error occurs
          // or server returns response with an error status.
          //alert(data);
          $scope.authErrors=data;
        });
    });    
    
    $scope.doUserAction = function() {
        $scope.errorMessages = [];
        if ($scope.userActions==1) {
          $scope.loginShow = 0;
          window.location.href = "/caNanoLab/#/searchSample";

        }
        else if ($scope.userActions==2) {
          $scope.loginShow = 1;
          $scope.loginId = '';
          $scope.password = '';
          $scope.resetPasswordShow = 0;        
          $scope.authErrors = 0;
        }
        else if ($scope.userActions==3) {
          $scope.loginShow = 0;
          $scope.authErrors = 0;
          $scope.resetPasswordShow = 0;        
          $location.path("/register").replace();
        }
        else {
          $scope.authErrors = 0;
          $scope.loginShow = 0;        
          $scope.resetPasswordShow = 1;
          $scope.reset_loginId = "";
          $scope.old_password = "";
          $scope.new_password = "";
          $scope.confirm_new_password = "";
          $scope.errorMessage = "";
        }
    }



    $scope.resetDo = function()  {
        $scope.errorMessages = [];
        
        var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,}$/; 
        var str = $scope.new_password;


        if (!$scope.reset_loginId) {
          $scope.errorMessages.push("LOGIN ID is required")
        }
        if (!$scope.old_password) {
          $scope.errorMessages.push("OLD PASSWORD is required")
        }      
        if (!$scope.new_password) {
          $scope.errorMessages.push("NEW PASSWORD is required")
        }   
        else {
          if (!re.exec(str)) {
            $scope.errorMessages.push("Password must contain 1 upper case letter, 1 lower case letter, 1 symbol and be 8 or more characters in length")
          }
          else {
            if ($scope.new_password!=$scope.confirm_new_password) {
              $scope.errorMessages.push("NEW PASSWORD and CONFIRM PASSWORD do not match")
            }
          }
        }   
        if (!$scope.confirm_new_password) {
          $scope.errorMessages.push("CONFIRM NEW PASSWORD is required")
        }     
        if ($scope.new_password==$scope.old_password) {
          $scope.errorMessages.push("NEW PASSWORD and OLD PASSWORD must not be the same")
        }              

        if (!$scope.errorMessages.length) {
          $scope.resettingPassword = 1;
          var bean = {'username':$scope.reset_loginId,'oldPassword':$scope.old_password,'newPassword':$scope.new_password,'confirmPassword':$scope.confirm_new_password}
          $http({method: 'POST', url: '/caNanoLab/rest/security/resetPassword',data: bean}).
          success(function(data, status, headers, config) {
            $scope.loginShow = 1;
            $scope.resetPasswordShow = 0;
            $scope.authErrors = "Password successfully changed"; // piggybacking authErrors as they show up nicely below login form
            $scope.resettingPassword = 0;
          }).
          error(function(data, status, headers, config) {
            $scope.errorMessages = [];
            $scope.errorMessages.push(data);
            $scope.resettingPassword = 0;
          }); 
        }
    }

    $scope.loginDo = function() {
        if (!$scope.password || !$scope.loginId) {
          $scope.authErrors="Username and Password are required";
        }
        else {
        	$scope.bean = {"userName" : $scope.loginId, "password" : $scope.password};   	  
            $http({method: 'POST', url: '/caNanoLab/rest/security/login', data: $scope.bean}).
          success(function(data, status, headers, config) {
            // this callback will be called asynchronously
            // when the response is available
            $location.path("/").replace();
            $route.reload();

            //Set tabs here.. Delete on logout. Use variable instead of rest call

          }).
          error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            var re = /changed/; 
            var str = data;
            $scope.password = '';            
            if (re.exec(str)) {
              $scope.loginShow = 0
              $scope.resetPasswordShow = 1;
              $scope.loginId = '';
            }
            $scope.authErrors=data;
          });
        }      
    };

    $scope.doSearch = function(type) {
        $scope.loader = true;
        if (type=='samples'){
            $http({method: 'POST', url: '/caNanoLab/rest/sample/searchSample',data: $scope.searchSampleForm}).
            success(function(data, status, headers, config) {
              $scope.sampleData.data = data;
              $location.path("/sampleResults").replace();

            }).
            error(function(data, status, headers, config) {
              $scope.loader = false;
              $scope.message = data;
            }); 
        };

        if (type=='protocols') {

            $http({method: 'POST', url: '/caNanoLab/rest/protocol/searchProtocol',data: $scope.searchSampleForm}).
                success(function(data, status, headers, config) {
                    $scope.protocolData.data = data;
                    $location.path("/protocolResults").replace();
                }).
                error(function(data, status, headers, config) {
                    $scope.loader = false;
                    $scope.message = data;
                });
        };

        if (type=='publications') {
            $http({method: 'POST', url: '/caNanoLab/rest/publication/searchPublication',data: $scope.searchSampleForm}).
            success(function(data, status, headers, config) {
              $scope.publicationData.data = data;
              $location.path("/publicationResults").replace();

            }).
            error(function(data, status, headers, config) {
              $scope.loader = false;
              $scope.message = data;
            }); 
        };

    };   

    $scope.displayCredit = function(banner,state) {
        if (state)
          { $scope[banner] = true; }
        else { $scope[banner] = false;}
    }    


  });
