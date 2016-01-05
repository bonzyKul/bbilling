'use strict';

angular.module('bbillingApp')
    .controller('BranchController', function ($scope, $state, $modal, Branch, BranchSearch, ParseLinks) {
      
        $scope.branchs = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Branch.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.branchs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            BranchSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.branchs = result;
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
            $scope.branch = {
                branchCode: null,
                branchName: null,
                id: null
            };
        };
    });
