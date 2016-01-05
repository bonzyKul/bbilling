'use strict';

angular.module('bbillingApp')
    .controller('HolidayDetailController', function ($scope, $rootScope, $stateParams, entity, Holiday, Country) {
        $scope.holiday = entity;
        $scope.load = function (id) {
            Holiday.get({id: id}, function(result) {
                $scope.holiday = result;
            });
        };
        var unsubscribe = $rootScope.$on('bbillingApp:holidayUpdate', function(event, result) {
            $scope.holiday = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
