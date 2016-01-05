'use strict';

angular.module('bbillingApp')
    .factory('PricingTierSearch', function ($resource) {
        return $resource('api/_search/pricingTiers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
