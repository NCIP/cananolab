'use strict';

var app = angular.module('angularApp');
app.factory('groupsFactory', function($resource){
	return $resource('/caNanoLab/rest/security/getUserGroups');
});
