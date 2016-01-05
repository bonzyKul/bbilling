'use strict';

angular.module('bbillingApp')
    .controller('HolidayController', function ($scope, $state, $modal, Holiday, HolidaySearch, ParseLinks) {
      
        $scope.holidays = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Holiday.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.holidays = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            HolidaySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.holidays = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.holiday = {
                holidayDate: null,
                holidayDesc: null,
                id: null
            };
        };
    });
