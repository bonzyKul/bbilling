'use strict';

angular.module('bbillingApp')
    .factory('HolidaySearch', function ($resource) {
        return $resource('api/_search/holidays/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
