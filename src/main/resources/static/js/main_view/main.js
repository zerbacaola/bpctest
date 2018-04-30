'use strict';

var mainModule = angular.module('mainModule', [
   'ngRoute',
   'pascalprecht.translate',
   'smart-table'
]);

mainModule.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
        $locationProvider.hashPrefix('');
        $routeProvider
        .when('/', {
            templateUrl : '/template/main_view.html',
            controller : 'PersonController'
        })
        .otherwise({
            redirectTo: '/'
        });
    }]
);

mainModule.config(['$translateProvider', function ($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: '/i18/main_view/tr_',
            suffix: '.json'
        }).registerAvailableLanguageKeys(['en', 'ru'], {
            'en_US': 'en',
            'en_UK': 'en',
            'ru_RU': 'ru'
        })
        .determinePreferredLanguage()
        .fallbackLanguage('en')
        .useSanitizeValueStrategy('escape');
    }]
);

mainModule.config(['$compileProvider', function ($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|file|ftp|blob):|data:image\//);
    }]
);