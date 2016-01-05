'use strict';

angular.module('bbillingApp')
    .factory('Bank', function ($resource, DateUtils) {
        return $resource('api/banks/:id', {}, {
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
