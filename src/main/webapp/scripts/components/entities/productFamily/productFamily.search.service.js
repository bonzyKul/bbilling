'use strict';

angular.module('bbillingApp')
    .factory('ProductFamilySearch', function ($resource) {
        return $resource('api/_search/productFamilys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
