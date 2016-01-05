'use strict';

angular.module('bbillingApp')
    .factory('CountrySearch', function ($resource) {
        return $resource('api/_search/countrys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
