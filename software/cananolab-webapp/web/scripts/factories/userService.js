'use strict';
var app = angular.module('angularApp');
app.factory("userService", function(){
    // Service keeps data search results in memory to pass between controllers //
    return {
        userData: {data: null }
    }

});