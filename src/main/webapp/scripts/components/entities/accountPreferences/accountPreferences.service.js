'use strict';

angular.module('bbillingApp')
    .factory('AccountPreferences', function ($resource, DateUtils) {
        return $resource('api/accountPreferencess/:id', {}, {
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
