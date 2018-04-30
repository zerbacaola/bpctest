'use strict';

mainModule.controller('PersonController', function ($scope, PersonService) {
    var process = function(person) {
        if (!angular.isUndefined(person)) {
            $scope.data.push(person);
        }
    };

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

    $scope.image = 'iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==';

    $scope.data = [];
    $scope.displayedData = [];
    $scope.filter = {
        "name": null,
        "gender": null,
        "child": null,
        "mother": null,
        "father": null
    };

    $scope.state = {
        isLoading : false,
        currentPage : 1
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
            $scope.performSearch();
        }
    };

    // var changeTableState = function() {
    //     if (!angular.isUndefined(tableController)) {
    //         var state = tableController.tableState();
    //         state.pagination.totalItemCount = $scope.data.length;
    //         state.pagination.numberOfPages = $scope.state.totalPages;
    //         state.pagination.start = ($scope.state.currentPage - 1) * $scope.settings.itemsByPage;
    //         tableController.pipe();
    //     }
    // };

    // $scope.$watch('tableController', function(newValue, oldValue) {
    //     if (!angular.isUndefined(newValue) && newValue !== oldValue) {
    //         $scope.tableController.pipe = function() {
    //             $scope.tableController.pipe();
    //         };
    //     }
    // });

    // $scope.pipe = function(tableState, pipeFn) {
    //     if (tableState.pagination.start !== $scope.state.start) {
    //         $scope.state.start = tableState.pagination.start;
    //
    //     }
    //     pipeFn();
    // };

    $scope.performSearch();
});