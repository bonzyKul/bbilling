'use strict';

angular.module('bbillingApp')
    .controller('ProductsDetailController', function ($scope, $rootScope, $stateParams, entity, Products, ProductFamily, ServiceCatalogue) {
        $scope.products = entity;
        $scope.load = function (id) {
            Products.get({id: id}, function(result) {
                $scope.products = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:productsUpdate', function(event, result) {
            $scope.products = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
