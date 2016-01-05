'use strict';

angular.module('bbillingApp')
    .factory('AccountPreferencesSearch', function ($resource) {
        return $resource('api/_search/accountPreferencess/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
