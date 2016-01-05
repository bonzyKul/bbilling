'use strict';

angular.module('bbillingApp')
    .controller('AccountPreferencesDetailController', function ($scope, $rootScope, $stateParams, entity, AccountPreferences, Accounts) {
        $scope.accountPreferences = entity;
        $scope.load = function (id) {
            AccountPreferences.get({id: id}, function(result) {
                $scope.accountPreferences = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:accountPreferencesUpdate', function(event, result) {
            $scope.accountPreferences = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
