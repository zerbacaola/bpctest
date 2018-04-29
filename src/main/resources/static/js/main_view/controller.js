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


    $scope.state = {
        isLoading : false,
        //totalPages : 0,
        //start : undefined,
        currentPage : undefined
    };

    $scope.settings = {
        displayedPages : 10,
        itemsByPage : 3
    };

    $scope.searchByFilter = function(filter) {
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
    $scope.$watch('settings.itemsByPage', function(newValue, oldValue) {
        if (!angular.isUndefined(newValue) && newValue !== oldValue) {
            $scope.searchByFilter($scope.filter);
        }
    });

    // Handler of 'page changed' events
    $scope.pageChanged = function(newPage) {
        if (newPage !== $scope.state.currentPage) {
            $scope.state.currentPage = newPage;
            $scope.searchByFilter($scope.filter);
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

    $scope.searchByFilter($scope.filter);
});