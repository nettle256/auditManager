/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('articles', {
                url: '/articles',
                templateUrl: '/html/article/articles',
                controller: 'articlesCtrl'
            });
    }])
    .controller('articlesCtrl', function ($scope, $http) {
    });

