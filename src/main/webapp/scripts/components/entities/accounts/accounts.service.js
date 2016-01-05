'use strict';

angular.module('bbillingApp')
    .factory('Accounts', function ($resource, DateUtils) {
        return $resource('api/accountss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.accountOpenedDate = DateUtils.convertLocaleDateFromServer(data.accountOpenedDate);
                    data.accountClosedDate = DateUtils.convertLocaleDateFromServer(data.accountClosedDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.accountOpenedDate = DateUtils.convertLocaleDateToServer(data.accountOpenedDate);
                    data.accountClosedDate = DateUtils.convertLocaleDateToServer(data.accountClosedDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.accountOpenedDate = DateUtils.convertLocaleDateToServer(data.accountOpenedDate);
                    data.accountClosedDate = DateUtils.convertLocaleDateToServer(data.accountClosedDate);
                    return angular.toJson(data);
                }
            }
        });
    });
