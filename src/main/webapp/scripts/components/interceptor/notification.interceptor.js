 'use strict';

angular.module('bbillingApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-bbillingApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-bbillingApp-params')});
                }
                return response;
            }
        };
    });
