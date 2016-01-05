'use strict';

angular.module('bbillingApp')
    .factory('BankSearch', function ($resource) {
        return $resource('api/_search/banks/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
