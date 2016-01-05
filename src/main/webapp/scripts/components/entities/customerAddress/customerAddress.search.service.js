'use strict';

angular.module('bbillingApp')
    .factory('CustomerAddressSearch', function ($resource) {
        return $resource('api/_search/customerAddresss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
