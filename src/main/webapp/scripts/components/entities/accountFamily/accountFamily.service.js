'use strict';

angular.module('bbillingApp')
    .factory('AccountFamily', function ($resource, DateUtils) {
        return $resource('api/accountFamilys/:id', {}, {
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
