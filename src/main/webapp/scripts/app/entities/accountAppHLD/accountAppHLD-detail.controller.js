'use strict';

angular.module('bbillingApp')
    .controller('AccountAppHLDDetailController', function ($scope, $rootScope, $stateParams, entity, AccountAppHLD, Accounts, Products) {
        $scope.accountAppHLD = entity;
        $scope.load = function (id) {
            AccountAppHLD.get({id: id}, function(result) {
                $scope.accountAppHLD = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:accountAppHLDUpdate', function(event, result) {
            $scope.accountAppHLD = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
