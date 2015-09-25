'use strict';

angular.module('beerlib3kApp')
    .factory('Beer', function ($resource, DateUtils) {
        return $resource('api/beers/:id', {}, {
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
