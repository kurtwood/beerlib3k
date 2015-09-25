'use strict';

angular.module('beerlib3kApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


