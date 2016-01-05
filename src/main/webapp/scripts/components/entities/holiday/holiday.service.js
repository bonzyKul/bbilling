'use strict';

angular.module('bbillingApp')
    .factory('Holiday', function ($resource, DateUtils) {
        return $resource('api/holidays/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.holidayDate = DateUtils.convertLocaleDateFromServer(data.holidayDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.holidayDate = DateUtils.convertLocaleDateToServer(data.holidayDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.holidayDate = DateUtils.convertLocaleDateToServer(data.holidayDate);
                    return angular.toJson(data);
                }
            }
        });
    });
