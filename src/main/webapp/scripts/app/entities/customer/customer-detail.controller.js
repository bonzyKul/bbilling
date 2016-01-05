'use strict';

angular.module('bbillingApp')
    .controller('CustomerDetailController', function ($scope, $rootScope, $stateParams, entity, Customer, CustomerAddress, Country) {
        $scope.customer = entity;
        $scope.load = function (id) {
            Customer.get({id: id}, function(result) {
                $scope.customer = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:customerUpdate', function(event, result) {
            $scope.customer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
