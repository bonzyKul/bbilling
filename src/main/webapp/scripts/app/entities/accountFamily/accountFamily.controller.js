'use strict';

angular.module('bbillingApp')
    .controller('AccountFamilyController', function ($scope, $state, $modal, AccountFamily, AccountFamilySearch, ParseLinks) {
      
        $scope.accountFamilys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            AccountFamily.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.accountFamilys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AccountFamilySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.accountFamilys = result;
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
            $scope.accountFamily = {
                accountFamilyCode: null,
                accountFamilyDesc: null,
                id: null
            };
        };
    });
