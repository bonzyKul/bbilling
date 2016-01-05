'use strict';

angular.module('bbillingApp')
    .factory('ProductFamily', function ($resource, DateUtils) {
        return $resource('api/productFamilys/:id', {}, {
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
