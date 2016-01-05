'use strict';

angular.module('bbillingApp')
    .factory('AccountAppHLD', function ($resource, DateUtils) {
        return $resource('api/accountAppHLDs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.appHLDStartDate = DateUtils.convertLocaleDateFromServer(data.appHLDStartDate);
                    data.appHLDEndDate = DateUtils.convertLocaleDateFromServer(data.appHLDEndDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.appHLDStartDate = DateUtils.convertLocaleDateToServer(data.appHLDStartDate);
                    data.appHLDEndDate = DateUtils.convertLocaleDateToServer(data.appHLDEndDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.appHLDStartDate = DateUtils.convertLocaleDateToServer(data.appHLDStartDate);
                    data.appHLDEndDate = DateUtils.convertLocaleDateToServer(data.appHLDEndDate);
                    return angular.toJson(data);
                }
            }
        });
    });
