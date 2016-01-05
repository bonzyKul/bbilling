'use strict';

angular.module('bbillingApp')
    .factory('PricingTier', function ($resource, DateUtils) {
        return $resource('api/pricingTiers/:id', {}, {
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
