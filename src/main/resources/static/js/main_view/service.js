'use strict';

mainModule.factory('PersonService', function($http) {
    return {
        searchByFilter: function(filterTO) {
            return $http.post('/search', filterTO).then(function (response) {
                return response.data;
            });
        }
    };
});