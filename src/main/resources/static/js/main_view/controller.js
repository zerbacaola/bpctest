'use strict';

mainModule.controller('PersonController', function ($scope, PersonService) {
    var process = function(person) {
        if (!angular.isUndefined(person)) {
            $scope.data.push(person);
        }
    };

    //TODO : implement basic response handler
    var searchByFilter = function(filter) {
        $scope.state.isLoading = true;
        PersonService.searchByFilter(filter).then(function(response) {
            if (response.result === 'OK') {
                $scope.data.splice(0, $scope.data.length);

                angular.forEach(response.data, function(person) {
                    process(person);
                });
                $scope.state.isLoading = false;
                //$scope.state.totalPages = Math.ceil($scope.data.length / $scope.settings.itemsByPage);
            } else {
                // TODO : Implement error modal
            }
        });
    };

    var fetchPersonPhoto = function(id) {
        $scope.state.isLoading = true;
        PersonService.getPhotoByPersonId(id).then(function(response) {
            if (response.result === 'OK') {
                $scope.photos[id] = response.data;
                $scope.state.isLoading = false;
            } else {
                // TODO : Implement error modal
            }
        });
    };

    $scope.data = [];
    $scope.displayedData = [];
    $scope.photos = {};
    $scope.filter = {
        "name": null,
        "gender": null,
        "child": null,
        "mother": null,
        "father": null
    };

    $scope.state = {
        isLoading : false,
        currentPage : 1,
        selectedPersonId : undefined
    };

    $scope.settings = {
        displayedPages : 10,
        itemsPerPage : 2,
        itemsPerPageOptions: [3, 4, 5, 10, 20, 30, 40, 50, 100]
    };

    $scope.performSearch = function() {
        var filter = angular.copy($scope.filter);
        filter['page'] = $scope.state.currentPage;
        filter['rowsPerPage'] = $scope.settings.itemsPerPage;
        searchByFilter(filter);
    };

    $scope.isPhotoExist = function() {
        var id = $scope.state.selectedPersonId;
        return angular.isDefined(id) && angular.isDefined($scope.photos[id]);
    };

    $scope.performPhotoSelected = function() {
        var id = $scope.state.selectedPersonId;
        if (!$scope.isPhotoExist() && angular.isDefined(id)) {
            fetchPersonPhoto(id);
        }
    };

    $scope.getPhotoData = function() {
        return $scope.photos[$scope.state.selectedPersonId];
    };

    $scope.performReset = function() {
        for (var i in $scope.filter) {
            if ($scope.filter.hasOwnProperty(i)) {
                $scope.filter[i] = null;
            }
        }
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

    $scope.$watch('stController', function(newValue, oldValue) {
        if (!angular.isUndefined(newValue) && newValue !== oldValue) {
            var stSelect = $scope.stController.select;
            $scope.stController.select = function(rowObj, mode) {
                console.log('Selected: ' + rowObj.isSelected + ', id: ' + rowObj.id);
                stSelect(rowObj, mode);
            };
        }
    });

    $scope.$watch('state.selectedPersonId', function(newValue, oldValue) {
        if (angular.isDefined(newValue) && newValue !== oldValue) {
            $scope.performPhotoSelected();
        }
    });

    /** ========================================== EVENT HANDLERS =================================================== */

    // Handler of 'items per page change' events
    $scope.$watch('settings.itemsPerPage', function(newValue, oldValue) {
        if (!angular.isUndefined(newValue) && newValue !== oldValue) {
            if ($scope.settings.itemsPerPage > $scope.data.length) {
                // TODO : if there are no records found then decrease itemsPerPage value
                $scope.performSearch();
            }
        }
    });

    // Handler of 'page changed' events
    $scope.pageChanged = function(newPage) {
        if (newPage !== $scope.state.currentPage) {
            $scope.state.currentPage = newPage;
            $scope.state.selectedPersonId = undefined;
            $scope.performSearch();
        }
    };

    // Handler of 'row selected' events
    $scope.onRowSelected = function(rowObj, mode) {
        if (angular.isDefined(rowObj) && rowObj['isSelected'] === true && mode === 'single') {
            $scope.state.selectedPersonId = rowObj.id;
        }
    };

    // Initial data load
    $scope.performSearch();
});