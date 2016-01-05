'use strict';

angular.module('bbillingApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


