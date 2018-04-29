'use strict';

mainModule.controller('PersonController', function ($scope, PersonService) {
    var process = function(person) {
        if (!angular.isUndefined(person)) {
            delete $scope.data[person.id];
            $scope.data[person.id] = person;
        }
    };

    $scope.data = {};
    $scope.filter = {
        "name": "Sergey",
        "gender": "MALE",
        "childrens": ["Pauline", "Katya"],
        "mother": "Natasha",
        "father": null,
        "page": 1,
        "rowsPerPage": 3
    };

    $scope.searchByFilter = function(filter) {
        PersonService.searchByFilter(filter).then(function(response) {
            if (response.result === 'OK') {
                angular.forEach(response.data, function(person){
                    process(person);
                });
            } else {
                // TODO : Implement error modal
            }
        });
    };

    $scope.getPersonNameById = function(id) {
        if (!angular.isUndefined(id) && !angular.isUndefined($scope.data[id])) {
            return $scope.data[id].name;
        } else {
            return '';
        }
    };

    $scope.getPersonNamesByIds = function(ids) {
        if (!angular.isUndefined(ids) && angular.isArray(ids)) {
            var names = [];
            for (var index in ids) {
                if (ids.hasOwnProperty(index)) {
                    var name = $scope.getPersonNameById(ids[index]);
                    if (name.length !== 0) {
                        names.push(name);
                    }
                }
            }
            return names.join(', ');
        } else {
            return '';
        }
    };

    $scope.searchByFilter($scope.filter);
});