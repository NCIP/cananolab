'use strict';
var app = angular.module('angularApp');
app.factory("protocolService", function(){
    // Service keeps data search results in memory to pass between controllers //
    return {
        protocolData: {data: null }
    }

});