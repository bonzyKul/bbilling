'use strict';

angular.module('bbillingApp')
    .factory('PricingSearch', function ($resource) {
        return $resource('api/_search/pricings/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
