'use strict';

angular.module('bbillingApp')
    .controller('ProductFamilyDetailController', function ($scope, $rootScope, $stateParams, entity, ProductFamily, Products) {
        $scope.productFamily = entity;
        $scope.load = function (id) {
            ProductFamily.get({id: id}, function(result) {
                $scope.productFamily = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:productFamilyUpdate', function(event, result) {
            $scope.productFamily = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
