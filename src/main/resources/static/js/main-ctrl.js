/**
 * Created by Nettle on 2017/2/6.
 */

auditManagerApp
    .controller('mainCtrl', function ($scope, $http) {
        $http
            .get(['api', 'user'].join('/'))
            .then(function (result) {
                $scope.currentUser = result.data;
            }, function (result) {
            });

        $scope.logout = function () {
            mdui.dialog({
                content: '确认登出',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(inst){
                            $http
                                .get(['api', 'logout'].join('/'))
                                .then(function (result) {
                                    window.location.reload();
                                }, function (result) {
                                });
                        }
                    }
                ]
            })
        };
    });