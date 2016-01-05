'use strict';

angular.module('bbillingApp')
	.controller('HolidayDeleteController', function($scope, $modalInstance, entity, Holiday) {

        $scope.holiday = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Holiday.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });