'use strict';

angular.module('bbillingApp')
    .factory('BranchSearch', function ($resource) {
        return $resource('api/_search/branchs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
