'use strict';

angular.module('bbillingApp')
    .controller('ServiceCatalogueController', function ($scope, $state, $modal, ServiceCatalogue, ServiceCatalogueSearch, ParseLinks) {
      
        $scope.serviceCatalogues = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ServiceCatalogue.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.serviceCatalogues = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ServiceCatalogueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.serviceCatalogues = result;
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
            $scope.serviceCatalogue = {
                serviceCode: null,
                serviceDescription: null,
                serviceStartDate: null,
                serviceEndDate: null,
                id: null
            };
        };
    });
