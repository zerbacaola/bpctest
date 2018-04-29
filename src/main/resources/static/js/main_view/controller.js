'use strict';

mainModule.controller('PersonController', function ($scope, PersonService) {
    var process = function(person) {
        if (!angular.isUndefined(person)) {
            $scope.data.push(person);
        }
    };

    $scope.data = [];
    $scope.displayedData = [];
    $scope.filter = {
        "name": "Sergey",
        "gender": "MALE",
        "childrens": ["Pauline", "Katya"],
        "mother": "Natasha",
        "father": null,
        "page": 1,
        "rowsPerPage": 3
    };

    $scope.settings = {
        displayedPages : 10,
        itemsByPage : 3
    };

    $scope.searchByFilter = function(filter) {
        PersonService.searchByFilter(filter).then(function(response) {
            if (response.result === 'OK') {
                $scope.data = [];
                angular.forEach(response.data, function(person){
                    process(person);
                });
            } else {
                // TODO : Implement error modal
            }
        });
    };

    $scope.getPersonNameById = function(id) {
        if (!angular.isUndefined(id) && !angular.isUndefined($scope.data)) {
            for (var i in $scope.data) {
                if ($scope.data.hasOwnProperty(i)) {
                    if ($scope.data[i].id === id) {
                        return $scope.data[i].name;
                    }
                }
            }
        }
        return '';
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