'use strict';

angular.module('bbillingApp')
    .factory('AccountFamilySearch', function ($resource) {
        return $resource('api/_search/accountFamilys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
