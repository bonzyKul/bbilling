'use strict';

angular.module('bbillingApp')
    .controller('AccountsDetailController', function ($scope, $rootScope, $stateParams, entity, Accounts, Products, Branch, Bank, Country, Currency, Customer, AccountFamily) {
        $scope.accounts = entity;
        $scope.load = function (id) {
            Accounts.get({id: id}, function(result) {
                $scope.accounts = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:accountsUpdate', function(event, result) {
            $scope.accounts = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
