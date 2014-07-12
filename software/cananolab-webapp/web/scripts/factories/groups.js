'use strict';

var app = angular.module('angularApp');
app.factory('groupService', function($resource){
	return $resource('/caNanoLab/rest/security/getUserGroups');
});
