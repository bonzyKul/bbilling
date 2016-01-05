'use strict';

angular.module('bbillingApp')
    .controller('AccountFamilyDetailController', function ($scope, $rootScope, $stateParams, entity, AccountFamily, Accounts) {
        $scope.accountFamily = entity;
        $scope.load = function (id) {
            AccountFamily.get({id: id}, function(result) {
                $scope.accountFamily = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:accountFamilyUpdate', function(event, result) {
            $scope.accountFamily = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
