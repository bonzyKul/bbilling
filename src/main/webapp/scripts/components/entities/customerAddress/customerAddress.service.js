'use strict';

angular.module('bbillingApp')
    .factory('CustomerAddress', function ($resource, DateUtils) {
        return $resource('api/customerAddresss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
