'use strict';

angular.module('bbillingApp')
    .controller('CountryDetailController', function ($scope, $rootScope, $stateParams, entity, Country, Currency) {
        $scope.country = entity;
        $scope.load = function (id) {
            Country.get({id: id}, function(result) {
                $scope.country = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:countryUpdate', function(event, result) {
            $scope.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
