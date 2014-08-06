'use strict';

var app = angular.module('angularApp');
app.factory('groupService', function($resource){
	return {
		getGroups: {data:$resource('/caNanoLab/rest/security/getUserGroups') },
		isCurator: {data: 'test' }
		}	
});
