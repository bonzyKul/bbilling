'use strict';

angular.module('bbillingApp')
    .controller('BranchDetailController', function ($scope, $rootScope, $stateParams, entity, Branch) {
        $scope.branch = entity;
        $scope.load = function (id) {
            Branch.get({id: id}, function(result) {
                $scope.branch = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:branchUpdate', function(event, result) {
            $scope.branch = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
