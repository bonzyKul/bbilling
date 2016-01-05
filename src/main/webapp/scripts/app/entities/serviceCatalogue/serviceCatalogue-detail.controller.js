'use strict';

angular.module('bbillingApp')
    .controller('ServiceCatalogueDetailController', function ($scope, $rootScope, $stateParams, entity, ServiceCatalogue, Products, Pricing) {
        $scope.serviceCatalogue = entity;
        $scope.load = function (id) {
            ServiceCatalogue.get({id: id}, function(result) {
                $scope.serviceCatalogue = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:serviceCatalogueUpdate', function(event, result) {
            $scope.serviceCatalogue = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
