'use strict';

angular.module('bbillingApp')
    .controller('AccountsController', function ($scope, $state, $modal, Accounts, AccountsSearch, ParseLinks) {
      
        $scope.accountss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Accounts.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.accountss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AccountsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.accountss = result;
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
            $scope.accounts = {
                accountNumber: null,
                accountType: null,
                accountOpenedDate: null,
                accountClosedDate: null,
                accountStatus: null,
                accountCRTurnOver: null,
                accountDRTurnOver: null,
                accountAvailBal: null,
                accountLedgerBal: null,
                accountBalance: null,
                accountLastBalType: null,
                accountTier: null,
                accountChargingBal: null,
                id: null
            };
        };
    });
