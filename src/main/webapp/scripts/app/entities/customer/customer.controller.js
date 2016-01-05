'use strict';

angular.module('bbillingApp')
    .controller('CustomerController', function ($scope, $state, $modal, Customer, CustomerSearch, ParseLinks) {
      
        $scope.customers = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Customer.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CustomerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.customers = result;
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
            $scope.customer = {
                customerID: null,
                customerFName: null,
                customerMName: null,
                customerLName: null,
                customerStartDate: null,
                customerEndDate: null,
                customerStatus: null,
                customerType: null,
                customerEmail: null,
                customerTelNo: null,
                customerMobileNo: null,
                customerTier: null,
                id: null
            };
        };
    });
