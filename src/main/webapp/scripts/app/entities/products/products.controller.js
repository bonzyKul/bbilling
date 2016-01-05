'use strict';

angular.module('bbillingApp')
    .controller('ProductsController', function ($scope, $state, $modal, Products, ProductsSearch, ParseLinks) {
      
        $scope.productss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Products.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.productss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ProductsSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.productss = result;
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
            $scope.products = {
                productCode: null,
                productShortName: null,
                productName: null,
                productStartDate: null,
                productEndDate: null,
                productStatus: null,
                id: null
            };
        };
    });
