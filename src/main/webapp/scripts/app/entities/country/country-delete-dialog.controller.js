'use strict';

angular.module('bbillingApp')
	.controller('CountryDeleteController', function($scope, $modalInstance, entity, Country) {

        $scope.country = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Country.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });