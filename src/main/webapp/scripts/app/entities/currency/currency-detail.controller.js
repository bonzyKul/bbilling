'use strict';

angular.module('bbillingApp')
    .controller('CurrencyDetailController', function ($scope, $rootScope, $stateParams, entity, Currency, Country) {
        $scope.currency = entity;
        $scope.load = function (id) {
            Currency.get({id: id}, function(result) {
                $scope.currency = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:currencyUpdate', function(event, result) {
            $scope.currency = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
