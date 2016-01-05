'use strict';

angular.module('bbillingApp')
    .factory('Products', function ($resource, DateUtils) {
        return $resource('api/productss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.productStartDate = DateUtils.convertLocaleDateFromServer(data.productStartDate);
                    data.productEndDate = DateUtils.convertLocaleDateFromServer(data.productEndDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.productStartDate = DateUtils.convertLocaleDateToServer(data.productStartDate);
                    data.productEndDate = DateUtils.convertLocaleDateToServer(data.productEndDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.productStartDate = DateUtils.convertLocaleDateToServer(data.productStartDate);
                    data.productEndDate = DateUtils.convertLocaleDateToServer(data.productEndDate);
                    return angular.toJson(data);
                }
            }
        });
    });
