'use strict';

mainModule.factory('PersonService', function($http) {
    return {
        searchByFilter: function(filterTO) {
            return $http.post('/search', filterTO).then(function (response) {
                return response.data;
            });
        },

        getPhotoByPersonId: function(id) {
            return $http.get('/getPhoto', {"params": {"id" : id }}).then(function (response) {
                return response.data;
            });
        }
    };
});