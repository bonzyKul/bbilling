'use strict';

angular.module('bbillingApp')
    .factory('AccountsSearch', function ($resource) {
        return $resource('api/_search/accountss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
