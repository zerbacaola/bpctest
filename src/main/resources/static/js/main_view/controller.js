'use strict';

mainModule.controller('PersonController', function ($scope, $translate, PersonService) {
    var genderOptions = [
        { k : null,      v : '' },
        { k : 'MALE',    v : $translate.instant('MALE') },
        { k : 'FEMALE',  v : $translate.instant('FEMALE') }
    ];

    var stub = {};

    var isDefined = function(v) {
        return angular.isDefined(v) && v != null;
    };

    var process = function(person) {
        if (isDefined(person)) {
            $scope.data.push(person);
        }
    };

    var normalizeFilter = function(fetchCount) {
        var filter = angular.copy($scope.filter);
        if (isDefined(fetchCount)) {
            filter['offset'] = $scope.data.length;
            filter['limit'] = fetchCount;
        } else {
            filter['offset'] = $scope.settings.itemsPerPage * ($scope.state.currentPage - 1);
            filter['limit'] = $scope.settings.itemsPerPage;
        }
        filter['gender'] = $scope.filter['gender']['k'];
        return filter;
    };

    //TODO : implement common response handler
    var searchByFilter = function(filter, erasePreviousData) {
        $scope.state.isLoading = true;
        PersonService.searchByFilter(filter).then(function(response) {
            if (response.result === 'OK') {
                if (erasePreviousData) {
                    $scope.data.splice(0, $scope.data.length);
                }
                angular.forEach(response.data, function(person) {
                    process(person);
                });
                if (filter.limit > response.data.length) {
                    $scope.state.dataFetched = true;
                } else {
                    // This is for displaying the next page button
                    process(stub);
                }
            } else {
                // TODO : Implement error modal
            }
        }).finally(function() {
            $scope.state.isLoading = false;
        });
    };

    var loadData = function(performCheckerFn) {
        // Remove stub
        if (!isDefined($scope.data[$scope.data.length - 1]['id'])) {
            $scope.data.splice($scope.data.length - 1, 1);
        }

        if (!$scope.state.dataFetched) {
            if (performCheckerFn()) {
                var fetchCount = $scope.state.currentPage * $scope.settings.itemsPerPage - $scope.data.length;
                searchByFilter(normalizeFilter(fetchCount), false);
            } else {
                process(stub);
            }
        }
    };

    var fetchPersonPhoto = function(id) {
        $scope.state.isLoading = true;
        PersonService.getPhotoByPersonId(id).then(function(response) {
            if (response.result === 'OK') {
                $scope.photos[id] = response.data;
            } else {
                // TODO : Implement error modal
            }
        }).finally(function() {
            $scope.state.isLoading = false;
        });
    };

    $scope.data = [];
    $scope.displayedData = [];
    $scope.photos = {};
    $scope.filter = {
        "name": null,
        "gender": genderOptions[0],
        "child": null,
        "mother": null,
        "father": null
    };

    $scope.state = {
        tableState : undefined,
        isLoading : false,
        currentPage : 1,
        selectedPersonId : undefined,
        // For current filter
        dataFetched : false
    };

    $scope.settings = {
        displayedPages : 10,
        itemsPerPage : 1,
        itemsPerPageOptions : [1, 2, 3, 10, 20, 30, 40, 50, 100],
        genderOptions : genderOptions
    };

    $scope.isPhotoExist = function() {
        var id = $scope.state.selectedPersonId;
        return isDefined(id) && isDefined($scope.photos[id]);
    };

    $scope.performPhotoSelected = function() {
        var id = $scope.state.selectedPersonId;
        if (!$scope.isPhotoExist() && isDefined(id)) {
            fetchPersonPhoto(id);
        }
    };

    $scope.getPhotoData = function() {
        return $scope.photos[$scope.state.selectedPersonId];
    };

    $scope.getPersonNameById = function(id) {
        if (angular.isDefined(id) && angular.isDefined($scope.data)) {
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
        if (angular.isDefined(ids) && angular.isArray(ids)) {
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

    $scope.$watch('state.selectedPersonId', function(newValue, oldValue) {
        if (angular.isDefined(newValue) && newValue !== oldValue) {
            $scope.performPhotoSelected();
        }
    });

    $scope.onTableStateRequested = function(commonFn, stController) {
        delete arguments[--arguments.length];
        delete arguments[--arguments.length];
        var tableState = commonFn.apply(stController, arguments);
        //console.log('totalItemCount: ' + tableState.pagination.totalItemCount + ' start: ' + tableState.pagination.start + ' number: ' + tableState.pagination.number + ' STUB: ' + $scope.state.stubIsPresent);

        if (!isDefined($scope.state.tableState)) {
            $scope.state.tableState = tableState;
        }
        return tableState;
    };

    /** ========================================== EVENT HANDLERS =================================================== */

    // Handler of 'reset button was clicked' events
    $scope.performReset = function() {
        for (var i in $scope.filter) {
            if ($scope.filter.hasOwnProperty(i)) {
                $scope.filter[i] = (i === 'gender') ? genderOptions[0] : null;
            }
        }
    };

    // Handler of 'search button was clicked' events
    $scope.performSearch = function() {
        $scope.state.dataFetched = false;
        searchByFilter(normalizeFilter(undefined), true);
    };

    // Handler of 'items per page change' events
    $scope.$watch('settings.itemsPerPage', function(newValue, oldValue) {
        if (!angular.isUndefined(newValue) && newValue !== oldValue) {
            loadData(function() {
                return $scope.settings.itemsPerPage > $scope.data.length;
            });
        }
    });

    // Handler of 'page changed' events
    $scope.pageChanged = function(newPage) {
        if (newPage !== $scope.state.currentPage) {
            var previousPage = $scope.state.currentPage;
            $scope.state.selectedPersonId = undefined;
            $scope.state.currentPage = newPage;
            if (previousPage < newPage) {
                loadData(function() {
                    return $scope.data.length < $scope.settings.itemsPerPage * $scope.state.currentPage;
                });
            }
        }
    };

    // Handler of 'row selected' events
    $scope.onRowSelected = function(rowObj, mode, commonFn, stController) {
        delete arguments[--arguments.length];
        delete arguments[--arguments.length];
        commonFn.apply(stController, arguments);
        if (angular.isDefined(rowObj) && rowObj['isSelected'] === true && mode === 'single') {
            $scope.state.selectedPersonId = rowObj.id;
        }
    };

    // Initial data load
    $scope.performSearch();
});