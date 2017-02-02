
// CANANOLAB-479


describe("editUser spec ", function() {
	
	var $scope = {}, controller, rootscope, http, httpBackend, location, timeout, route_params, upload, navigation_service, group_service, editUserCtrl,mockUserResource;

	var read = {"username":"ttran","firstName":"tin","lastName":"tran","organization":"NCI","department":"CBIIT","title":null,"phoneNumber":null,"password":"$2a$10$EmfHzgyCiGdkNM6oM2jtEeMstTcNhM9pvsskD2fhy9cupEouWl3uq","emailId":"ttran@nih.gov","enabled":true,"roles":["ROLE_ANONYMOUS","ROLE_RESEARCHER"],"groups":["test_group","test group","a new test","Group A","Group B","TEST 1","TEST 2"],"authorities":[{"authority":"test_group"},{"authority":"test group"},{"authority":"a new test"},{"authority":"Group A"},{"authority":"Group B"},{"authority":"TEST 1"},{"authority":"TEST 2"},{"authority":"ROLE_ANONYMOUS"},{"authority":"ROLE_RESEARCHER"}],"public":true,"displayName":"tran, tin","researcher":true,"curator":false,"admin":false,"accountNonLocked":true,"credentialsNonExpired":true,"accountNonExpired":true};
    var apiService,queryDeferred;
	
	beforeEach(module('angularApp'));

	beforeEach(inject(function($rootScope,$http,$location,$timeout,$routeParams,$httpBackend,$controller,$q) {

        apiService = {
            query:  function() {
                queryDeferred = $q.defer();
                queryDeferred.resolve(read);
                return {$promise: queryDeferred.promise};
            }
        };

        rootscope = $rootScope.$new();
        $scope = $rootScope.$new();
        http = $http,
        location = $location,
        timeout = $timeout,
        route_params = $routeParams,
        upload = "",
        group_service = apiService,
        httpBackend = $httpBackend;
        controller = $controller;

		spyOn(apiService, 'query').and.callThrough();
        spyOn(location, 'path').and.returnValue('');
        spyOn(location, 'replace');

        read = {"username":"ttran","firstName":"tin","lastName":"tran","organization":"NCI","department":"CBIIT","title":null,"phoneNumber":null,"password":"$2a$10$EmfHzgyCiGdkNM6oM2jtEeMstTcNhM9pvsskD2fhy9cupEouWl3uq","emailId":"ttran@nih.gov","enabled":true,"roles":["ROLE_ANONYMOUS","ROLE_RESEARCHER"],"groups":["test_group","test group","a new test","Group A","Group B","TEST 1","TEST 2"],"authorities":[{"authority":"test_group"},{"authority":"test group"},{"authority":"a new test"},{"authority":"Group A"},{"authority":"Group B"},{"authority":"TEST 1"},{"authority":"TEST 2"},{"authority":"ROLE_ANONYMOUS"},{"authority":"ROLE_RESEARCHER"}],"public":true,"displayName":"tran, tin","researcher":true,"curator":false,"admin":false,"accountNonLocked":true,"credentialsNonExpired":true,"accountNonExpired":true};
        httpBackend.when('GET', '/caNanoLab/rest/useraccount/read?username=test').respond(read);

        editUserCtrl = $controller('EditUserCtrl', {
        	navigationService:apiService,
        	groupService:group_service,
        	$rootscope:rootscope,
        	$scope:$scope,
        	$http:http,
        	$location:location,
        	$timeout:timeout,
        	$routeParams:route_params,
        	$upload:upload
        });

    }));

    it('should test loadUserData()', function() {
        $scope.userTag = "test";
    	$scope.loadUserData();
    	httpBackend.flush();
        var result = {username: 'ttran', firstName: 'tin', lastName: 'tran', organization: 'NCI', department: 'CBIIT', title: null, phoneNumber: null, emailId: 'ttran@nih.gov', enabled: true, roles: ['ROLE_ANONYMOUS', 'ROLE_RESEARCHER']}
    	expect(JSON.stringify($scope.userForm)).toEqual(JSON.stringify(result));
    });

});


// describe("manageUser spec", function() {
	
// 	var $scope = {}, controller, rootscope, http, httpBackend, location, timeout, route_params, upload, navigation_service, group_service, editUserCtrl;	

// 	beforeEach(inject(function(userService,navigationService,groupService,$rootScope,$filter,ngTableParams,$http,$location,$modal,$httpBackend,$controller) {
//         rootscope = $rootScope.$new();
//         $scope = $rootScope.$new();
//         http = $http,
//         filter = $filter,
//         modal = $modal,
//         location = $location,
//         timeout = $timeout,
//         route_params = $routeParams,
//         table_params = ngTableParams,
//         upload = $upload,
//         httpBackend = $httpBackend;
//         controller = $controller;

//         manageUserCtrl = controller('ManageUserCtrl', {
//         	$scope:$scope,   	
//         	$http:http,
//         	$location:location,
//         	ngTableParams:table_params,
//         	userService:user_service,
//         	navigationService:navigation_service,
//         	groupService:group_service
//         });

//     }));

//     it('', function() {
    	
//     });

// });


// describe("userSearch spec", function() {

// 	beforeEach(inject(function($rootScope,$httpBackend,$controller) {
//         rootscope = $rootScope.$new();
//         $scope = $rootScope.$new();
//         httpBackend = $httpBackend;
//         controller = $controller;

//         userSearchCtrl = controller('UserResultsCtrl', {
//         	$scope:$scope
//         });

//     }));

//     it('', function() {
    	
//     });

// });

