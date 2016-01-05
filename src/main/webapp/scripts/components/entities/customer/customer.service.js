'use strict';

angular.module('bbillingApp')
    .factory('Customer', function ($resource, DateUtils) {
        return $resource('api/customers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.customerStartDate = DateUtils.convertLocaleDateFromServer(data.customerStartDate);
                    data.customerEndDate = DateUtils.convertLocaleDateFromServer(data.customerEndDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.customerStartDate = DateUtils.convertLocaleDateToServer(data.customerStartDate);
                    data.customerEndDate = DateUtils.convertLocaleDateToServer(data.customerEndDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.customerStartDate = DateUtils.convertLocaleDateToServer(data.customerStartDate);
                    data.customerEndDate = DateUtils.convertLocaleDateToServer(data.customerEndDate);
                    return angular.toJson(data);
                }
            }
        });
    });
