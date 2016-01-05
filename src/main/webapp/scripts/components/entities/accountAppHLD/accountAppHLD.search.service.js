'use strict';

angular.module('bbillingApp')
    .factory('AccountAppHLDSearch', function ($resource) {
        return $resource('api/_search/accountAppHLDs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
