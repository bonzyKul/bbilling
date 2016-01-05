'use strict';

angular.module('bbillingApp')
    .controller('AccountAppHLDController', function ($scope, $state, $modal, AccountAppHLD, AccountAppHLDSearch, ParseLinks) {
      
        $scope.accountAppHLDs = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            AccountAppHLD.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.accountAppHLDs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AccountAppHLDSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.accountAppHLDs = result;
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
            $scope.accountAppHLD = {
                appHLDStartDate: null,
                appHLDEndDate: null,
                id: null
            };
        };
    });
