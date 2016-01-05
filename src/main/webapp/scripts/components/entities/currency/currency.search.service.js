'use strict';

angular.module('bbillingApp')
    .factory('CurrencySearch', function ($resource) {
        return $resource('api/_search/currencys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
