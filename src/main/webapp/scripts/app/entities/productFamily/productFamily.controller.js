'use strict';

angular.module('bbillingApp')
    .controller('ProductFamilyController', function ($scope, $state, $modal, ProductFamily, ProductFamilySearch, ParseLinks) {
      
        $scope.productFamilys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ProductFamily.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.productFamilys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductFamilySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productFamilys = result;
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
            $scope.productFamily = {
                productFamilyCode: null,
                productFamilyDesc: null,
                id: null
            };
        };
    });
