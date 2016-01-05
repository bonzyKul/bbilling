'use strict';

angular.module('bbillingApp')
    .controller('CustomerAddressDetailController', function ($scope, $rootScope, $stateParams, entity, CustomerAddress, Country, Customer) {
        $scope.customerAddress = entity;
        $scope.load = function (id) {
            CustomerAddress.get({id: id}, function(result) {
                $scope.customerAddress = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:customerAddressUpdate', function(event, result) {
            $scope.customerAddress = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
