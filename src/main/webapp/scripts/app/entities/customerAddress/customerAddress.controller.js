'use strict';

angular.module('bbillingApp')
    .controller('CustomerAddressController', function ($scope, $state, $modal, CustomerAddress, CustomerAddressSearch, ParseLinks) {
      
        $scope.customerAddresss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            CustomerAddress.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.customerAddresss = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CustomerAddressSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.customerAddresss = result;
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
            $scope.customerAddress = {
                customerAddType: null,
                customerAddLineOne: null,
                customerAddLineTwo: null,
                customerCity: null,
                customerState: null,
                customerAddZip: null,
                id: null
            };
        };
    });
