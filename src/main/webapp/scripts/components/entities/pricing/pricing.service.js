'use strict';

angular.module('bbillingApp')
    .factory('Pricing', function ($resource, DateUtils) {
        return $resource('api/pricings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.pricingStartDate = DateUtils.convertLocaleDateFromServer(data.pricingStartDate);
                    data.pricingEndDate = DateUtils.convertLocaleDateFromServer(data.pricingEndDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.pricingStartDate = DateUtils.convertLocaleDateToServer(data.pricingStartDate);
                    data.pricingEndDate = DateUtils.convertLocaleDateToServer(data.pricingEndDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.pricingStartDate = DateUtils.convertLocaleDateToServer(data.pricingStartDate);
                    data.pricingEndDate = DateUtils.convertLocaleDateToServer(data.pricingEndDate);
                    return angular.toJson(data);
                }
            }
        });
    });
