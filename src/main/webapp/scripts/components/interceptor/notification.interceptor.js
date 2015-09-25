 'use strict';

angular.module('beerlib3kApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-beerlib3kApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-beerlib3kApp-params')});
                }
                return response;
            },
        };
    });