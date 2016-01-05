'use strict';

angular.module('bbillingApp')
    .controller('BankDetailController', function ($scope, $rootScope, $stateParams, entity, Bank) {
        $scope.bank = entity;
        $scope.load = function (id) {
            Bank.get({id: id}, function(result) {
                $scope.bank = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:bankUpdate', function(event, result) {
            $scope.bank = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
