'use strict';
var app = angular.module('angularApp')
.controller('CollaborationGroupCtrl', function (navigationService,groupService,$scope,$rootScope,$http,$location,$sce,$modal) {
	$rootScope.tabs = navigationService.get();
	$rootScope.groups = groupService.getGroups.data.get();

	$scope.isCurator = false;
	$http({method: 'GET', url: '/caNanoLab/rest/security/getUserGroups' }).
		success(function(data, status, headers, config) {

		var x = data;
		for (var key in x) {
		  for (var y=0;y<x[key].length;y++) {
		    if (x[key][y] == 'Curator') {
		    	$scope.isCurator = true;
		    };
		  };
		}
		}).
		error(function(data, status, headers, config) {
			
		});

	$scope.trustAsHtml = $sce.trustAsHtml;

	// $scope.data = [{"userAccesses":[{"userBean":{"userId":"56","displayName":"Gaheen, Sharon","loginName":"sharon","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"Carbon Tube Group","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"}],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"149","name":"Carbon Tube Group","description":"","ownerName":"gaheens","descriptionDisplayName":""},{"userAccesses":[],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"145","name":"Demo University","description":"","ownerName":"gaheens","descriptionDisplayName":""},{"userAccesses":[{"userBean":{"userId":"88","displayName":"Burns, Kevin","loginName":"burnskd","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"Demo-Collaboration","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"},{"userBean":{"userId":"62","displayName":"Guest, Guest","loginName":"canano_guest","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"Demo-Collaboration","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"},{"userBean":{"userId":"79","displayName":"Crist, Rachael","loginName":"cristr","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"Demo-Collaboration","roleName":"R","roleDisplayName":"read","accessBy":"user"}],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"146","name":"Demo-Collaboration","description":"Demonstration Collaboration Group","ownerName":"canano_guest","descriptionDisplayName":"Demonstration Collaboration Group"},{"userAccesses":[{"userBean":{"userId":"121","displayName":"Klemm, Juli","loginName":"KlemmJ","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"NCI group","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"},{"userBean":{"userId":"56","displayName":"Gaheen, Sharon","loginName":"sharon","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"NCI group","roleName":"R","roleDisplayName":"read","accessBy":"user"}],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"150","name":"NCI group","description":"","ownerName":"KlemmJ","descriptionDisplayName":""},{"userAccesses":[{"userBean":{"userId":"73","displayName":"Mcneil, Scott","loginName":"mcneils","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"NCNHIR","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"},{"userBean":{"userId":"79","displayName":"Crist, Rachael","loginName":"cristr","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"NCNHIR","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"}],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"148","name":"NCNHIR","description":"NIEHS Centers for Nanotechnology Health Implicaitons Research","ownerName":"cristr","descriptionDisplayName":"NIEHS Centers for Nanotechnology Health Implicaitons Research"},{"userAccesses":[{"userBean":{"userId":"126","displayName":"Researcher, CaNano","loginName":"canano_res","title":"","admin":false,"curator":false,"groupNames":[]},"groupName":"Test Collaboration","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"user"}],"groupAccesses":[{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"Curator","roleName":"CURD","roleDisplayName":"read update delete","accessBy":"group"}],"theAccess":{"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":"","roleName":"","roleDisplayName":"","accessBy":"group"},"publicStatus":false,"userUpdatable":true,"userDeletable":true,"userIsOwner":false,"id":"151","name":"Test Collaboration","description":"Test","ownerName":"canano_res","descriptionDisplayName":"Test"}];
	// $scope.userList = {"lethai":"Le Thai","burnskd":"Burns Kevin","canano_guest":"Guest Guest","grodzinp":"Grodzinski Piotr","swand":"Swan Don","skoczens":"Skoczen Sarah","sternstephan":"Stern Stephan","zolnik":"Zolnik Banu","canano_res":"Researcher CaNano","daijz":"Dai Jie","hunseckerk":"Hunsecker Kelly","lipkeyfg":"Lipkey Foster","marina":"Dobrovolskaia Marina","pottert":"Potter Tim","uckunf":"Uckun Fatih","michal":"Lijowski Michal","mcneils":"Mcneil Scott","neunb":"Neun Barry","cristr":"Crist Rachael","zhengji":"Zheng Jiwen","frittsmj":"Fritts Martin","SchaeferH":"Schaefer Henry","benhamm":"Benham Mick","masoods":"Masood Sana","mclelandc":"McLeland Chris","torresdh":"Torres David","KlemmJ":"Klemm Juli","patria":"Patri Anil","hughesbr":"Hughes Brian","clogstonj":"Clogston Jeff","hinkalgw":"Hinkal George","sharon":"Gaheen Sharon"}
	$scope.$on('$viewContentLoaded', function() {
		$scope.loaderText = "Loading";
		$scope.loader = true;
		$http({method: 'GET', url: '/caNanoLab/rest/community/getCollaborationGroups'}).
			success(function(data, status, headers, config) {
			$scope.data = data;
			$scope.loader = false;
		}).
			error(function(data, status, headers, config) {
			$scope.message = data;
			$scope.loader = false;
		}); 			
      
	});

	$scope.addGroupCollaboration = function() {		
		$scope.loader = true;
		$scope.loaderText = "Loading";
		$http({method: 'GET', url: '/caNanoLab/rest/community/setupNew'}).
			success(function(data, status, headers, config) {
			$scope.hideRemoveGroupButton = true;
			$scope.editCollaborationGroup = true;
			$scope.collaborationGroup = {};
			$scope.loader = false;

		}).
			error(function(data, status, headers, config) {
			$scope.loader = false;
		});

			window.scope = $scope;
		// $scope.groupBean = {"userAccesses":[]};
		// $scope.groupBeanCopy = angular.copy($scope.groupBean);		
	};

	$scope.editGroupCollaboration = function(item) {
		$scope.loader = true;
		$scope.loaderText = "Loading";
		$http({method: 'GET', url: '/caNanoLab/rest/community/editCollaborationGroup?groupId='+item.id}).
			success(function(data, status, headers, config) {
			$scope.collaborationGroup = data;
			$scope.editCollaborationGroup = true;
			$scope.loader = false;
		}).
			error(function(data, status, headers, config) {
			$scope.loader = false;
		}); 			
		$scope.hideRemoveGroupButton = false;
		$scope.groupBean = item;
		$scope.groupBeanCopy = angular.copy($scope.groupBean);
	};

	$scope.cancelGroupCollaboration = function(item) {
		angular.copy($scope.groupBeanCopy,$scope.groupBean);
		$scope.editCollaborationGroup = false;		
	};

	$scope.removeGroupCollaboration = function(item) {
		$scope.loader = true;
		$scope.loaderText = "Deleting Collaboration Group";
		$http({method: 'POST', url: '/caNanoLab/rest/community/deleteCollaborationGroups',data: $scope.collaborationGroup}).
		success(function(data, status, headers, config) {    
		  $scope.loader = false;
		  $scope.data = data;
		  $scope.editCollaborationGroup = false;		  
		}).
		error(function(data, status, headers, config) {
		  $scope.loader = false;
		});			
		
	};	

	$scope.openUserInfo = function(access) {
		$scope.eMessage = '';
		$scope.userInfo = true;
		$scope.showUsers = false;
		$scope.theAccess = access;
		if (access) {
			$scope.isUserInfoAdd = false;			
			$scope.userInfoBean = access;			
		}
		else {
			$scope.isUserInfoAdd = true;
			//$scope.userInfoBean = {"userBean":{"userId":null,"displayName":"","loginName":"","title":null,"admin":false,"curator":false,"groupNames":[]},"groupName":$scope.collaborationGroup.name,"roleName":"R","roleDisplayName":"read","accessType":"user"};
			//replaced AccessibilityBean by AccessControlInfo bean
			$scope.userInfoBean = {"recipient":"","recipientDisplayName":"","roleName":"R","roleDisplayName":"READ","accessType":"user"};
			$scope.theAccess = $scope.userInfoBean;

			// $scope.theAccess.userBean = {};
			// $scope.theAccess.userBean.loginName = '';
		};
		$scope.userInfoBeanCopy = angular.copy($scope.userInfoBean);
	};

	$scope.saveUserInfo = function(access) {
		$scope.loader = true;
		$scope.tempCollaborationGroup = {};
     	$scope.tempCollaborationGroup.name = $scope.collaborationGroup.name;
		$scope.tempCollaborationGroup.description = $scope.collaborationGroup.description;

		$scope.loaderText = "Editing User Info";
		$http({method: 'POST', url: '/caNanoLab/rest/community/addUserAccess',data: $scope.theAccess}).
		success(function(data, status, headers, config) {    
		  $scope.loader = false;
		  $scope.userInfo = false;
		  $scope.collaborationGroup = data;
		  $scope.collaborationGroup.name = $scope.tempCollaborationGroup.name;
		  $scope.collaborationGroup.description = $scope.tempCollaborationGroup.description;
		}).
		error(function(data, status, headers, config) {
		  $scope.loader = false;
		  $scope.eMessage = data;
		});		
		// $scope.userInfo = false;
		// if ($scope.isUserInfoAdd) {
		// 	$scope.groupBean.userAccesses.push($scope.userInfoBean);			
		// }
		// else {
		// 	alert("edit");
		// };
	};

	$scope.cancelUserInfo = function(access) {
		$scope.userInfo = false;
		if ($scope.isUserInfoAdd) {
			$scope.groupBean.userAccesses.splice($scope.groupBean.userAccesses.indexOf($scope.userInfoBean),1);		
		};
		angular.copy($scope.userInfoBeanCopy,$scope.userInfoBean);		
	};

	$scope.removeUserInfo = function(access) {
		$scope.loader = true;
		$scope.loaderText = "Deleting User Info";
		$http({method: 'POST', url: '/caNanoLab/rest/community/deleteUserAccess',data: $scope.theAccess}).
		success(function(data, status, headers, config) {    
		  $scope.loader = false;
  		$scope.userInfo = false;
  		$scope.collaborationGroup = data;

		}).
		error(function(data, status, headers, config) {
		  $scope.loader = false;
		});		
	};	

	$scope.submit = function(item) {
		// check if add (hideRemoveGroupButton is true if adding) //
		// if add push before submitting //
		if ($scope.hideRemoveGroupButton) {
			$scope.data.push($scope.groupBean);
		};
		$scope.editCollaborationGroup = false;
		$scope.loader = true;
		$scope.loaderText = "Saving";
		// for (key in $scope.collaborationGroup.userAccesses) {
		// 	$scope.collaborationGroup.userAccesses[key].groupName = $scope.collaborationGroup.name;
		// 	console.log($scope.collaborationGroup.userAccesses[key]);
		// }
		
		$http({method: 'POST', url: '/caNanoLab/rest/community/addCollaborationGroups',data: $scope.collaborationGroup}).
		success(function(data, status, headers, config) {    
			$scope.data = data;
			$scope.someData = data;
			// leData.dirty = false;			
		  $scope.loader = false;
		  $scope.loader = false;
		}).
		error(function(data, status, headers, config) {
		  $scope.loader = false;
		});		
	};
	$scope.changeRole = function() {
		console.log($scope.userInfoBean);
		if ($scope.userInfoBean.roleName=='R') {
			$scope.userInfoBean.roleDisplayName = 'READ'
		}
		else {
			$scope.userInfoBean.roleDisplayName = 'READ WRITE DELETE'
		}
	};

	$scope.searchLoginName = function() {
		$http({method: 'GET', url: '/caNanoLab/rest/core/getUsers?searchStr='+$scope.userInfoBean.recipient+ '&dataOwner='}).
			success(function(data, status, headers, config) {
			$scope.userList = data;
		}).
			error(function(data, status, headers, config) {
		}); 		
		$scope.showUsers = true;
	};

	$scope.sampledata = [];

	$scope.showSamples = function(i,ndx) {

		$scope.loader = true;
	
		var groupId = '';
		groupId = $scope.data[ndx].id;

        $http({method: 'GET', url: '/caNanoLab/rest/community/getsamples?groupId=' + groupId}).
            success(function(data, status, headers, config) {
            	$scope.sampledata[i] = data;
            	$scope.loader = false;
            }).
            error(function(data, status, headers, config) {
            	$scope.loader = false;
            });

	};

	$scope.openDataAvailability = function(sampleId) {

		$scope.loader = true;

          $http({method: 'GET', url: '/caNanoLab/rest/sample/viewDataAvailability',params: {"sampleId":sampleId}}).
          success(function(data, status, headers, config) {
            var modalInstance = $modal.open({
              templateUrl: 'views/sample/view/sampleDataAvailability.html',
              controller: 'SampleDataAvailabilityCtrl',
              size: 'sm',
              resolve: {
                sampleId: function () {
                  return sampleId;
                },
                availabilityData: function() {
                  return data;
                },                
                sampleData: function() {
                  return data;
                },
                edit: function() {
                  return 0;
                }
              }
            });
            $scope.loader = false;
          }).
          error(function(data, status, headers, config) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
            $scope.message = data;
            $scope.loader = false;
          });        
    };

    $scope.redirectSample = function(id) {
    	$rootScope.fromCollab = true;
    	$location.path("/editSample").search({'sampleId':id}).replace();
    };

});
