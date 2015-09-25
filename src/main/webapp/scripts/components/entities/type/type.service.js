'use strict';

angular.module('beerlib3kApp')
    .factory('Type', function ($resource, DateUtils) {
        return $resource('api/types/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
