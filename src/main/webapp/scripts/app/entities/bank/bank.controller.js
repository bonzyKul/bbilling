'use strict';

angular.module('bbillingApp')
    .controller('BankController', function ($scope, $state, $modal, Bank, BankSearch, ParseLinks) {
      
        $scope.banks = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Bank.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.banks = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            BankSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.banks = result;
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
            $scope.bank = {
                bankCode: null,
                bankName: null,
                id: null
            };
        };
    });
