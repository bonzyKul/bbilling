'use strict';

angular.module('bbillingApp')
    .factory('ServiceCatalogue', function ($resource, DateUtils) {
        return $resource('api/serviceCatalogues/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.serviceStartDate = DateUtils.convertLocaleDateFromServer(data.serviceStartDate);
                    data.serviceEndDate = DateUtils.convertLocaleDateFromServer(data.serviceEndDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.serviceStartDate = DateUtils.convertLocaleDateToServer(data.serviceStartDate);
                    data.serviceEndDate = DateUtils.convertLocaleDateToServer(data.serviceEndDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.serviceStartDate = DateUtils.convertLocaleDateToServer(data.serviceStartDate);
                    data.serviceEndDate = DateUtils.convertLocaleDateToServer(data.serviceEndDate);
                    return angular.toJson(data);
                }
            }
        });
    });
