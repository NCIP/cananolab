'use strict';

/**
 * @ngdoc overview
 * @name angularApp
 * @description
 * # angularApp
 *
 * Main module of the application.
 */

var app = angular.module('angularApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngTable',
    'ngSanitize',
    'ngTouch','ngGrid','ui.bootstrap']);


app.config(function ($routeProvider, $httpProvider) {
  $httpProvider.defaults.useXDomain = true;  
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
     .when('/register', {
       templateUrl: 'views/register.html',
       controller: 'RegisterCtrl'
     })       
      .when('/home', {
        templateUrl: 'views/loggedIn.html',
        controller: 'AuthCtrl'
      }) 
      .when('/searchSample', {
        templateUrl: 'views/sample/view/sampleSearch.html',
        controller: 'SampleSearchCtrl'
      })   
      .when('/sampleResults', {
        templateUrl: 'views/sample/view/sampleResults.html',
        controller: 'SampleResultsCtrl'
      })                  
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })  
      .when('/logout', {
        templateUrl: 'views/logout.html',
        controller: 'LogoutCtrl'
      })  
      .when('/manageSample', {
       templateUrl: 'views/manageSample.html',
       controller: 'ManageSampleCtrl'
      }) 
      .when('/sample', {
       templateUrl: 'views/sample/view/viewSample.html',
       controller: 'IndSampleCtrl'
      })       
      .when('/editSample', {
       templateUrl: 'views/sample/edit/editSample.html',
       controller: 'editSampleCtrl'
      })       
      .when('/composition', {
       templateUrl: 'views/sample/view/composition.html',
       controller: 'CompositionCtrl'
      }) 
      .when('/characterization', {
       templateUrl: 'views/sample/view/characterization.html',
       controller: 'CharacterizationCtrl'
      }) 
      .when('/publication', {
       templateUrl: 'views/sample/view/publication.html',
       controller: 'PublicationCtrl'
      })   
      .when('/managePublications', {
       templateUrl: 'views/publication/view/managePublications.html',
       controller: 'ManagePublicationCtrl'
      })                                                          

      .when('/searchPublication', {
        templateUrl: 'views/publication/view/publicationSearch.html',
        controller: 'PublicationSearchCtrl'
      })   
      .when('/searchSamplesByPublicationId', {
        templateUrl: 'views/publication/view/publicationSampleSearch.html',
        controller: 'PublicationSampleSearchCtrl'
      })      
      .when('/publicationResults', {
        templateUrl: 'views/publication/view/publicationResults.html',
        controller: 'PublicationResultsCtrl'
      })   
      .when('/publicationSampleInformation', {
        templateUrl: 'views/publication/view/publicationSampleInformation.html',
        controller: 'PublicationSampleInformationCtrl'
      })                  
      .when('/submitPublication', {
            templateUrl: 'views/publication/edit/editPublication.html',
            controller: 'EditPublicationCtrl'
      })
      .when('/editPublication', {
            templateUrl: 'views/publication/edit/editPublication.html',
            controller: 'EditPublicationCtrl'
      })
      .when('/message', {
            templateUrl: 'views/message.html',
            controller: 'MessageCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
