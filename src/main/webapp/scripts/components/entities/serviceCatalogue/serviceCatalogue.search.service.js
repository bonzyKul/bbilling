'use strict';

angular.module('bbillingApp')
    .factory('ServiceCatalogueSearch', function ($resource) {
        return $resource('api/_search/serviceCatalogues/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
