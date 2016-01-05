'use strict';

angular.module('bbillingApp')
    .controller('AccountPreferencesController', function ($scope, $state, $modal, AccountPreferences, AccountPreferencesSearch, ParseLinks) {
      
        $scope.accountPreferencess = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            AccountPreferences.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.accountPreferencess = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AccountPreferencesSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.accountPreferencess = result;
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
            $scope.accountPreferences = {
                accountEStatement: false,
                accountNotification: false,
                accountLvlBilling: false,
                accountPrintStmt: false,
                accountRewardSuppInd: false,
                accountPackCnt: null,
                accountCFInd: false,
                accountPNCS: false,
                accountBillingDefault: false,
                id: null
            };
        };
    });
