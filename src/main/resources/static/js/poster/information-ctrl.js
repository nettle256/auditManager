/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('information', {
                url: '/information',
                templateUrl: '/html/poster/information',
                controller: 'informationCtrl'
            });
    }])
    .controller('informationCtrl', function ($scope, $http, $sce) {

        $scope.sce = $sce;

        $http
            .get(['api', 'module'].join('/'))
            .then(function (result) {
                $scope.mods = result.data;
            }, function (result) {
            });

        $scope.deleteMod = function (mod) {
            mdui.dialog({
                content: '<div class="mdui-typo"><p>确定要删除模块 <strong>' + mod.name + '</strong> ？</p></div>',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','module',mod.id].join('/'), {})
                                .then(function (result) {
                                    mod.deleted = true;
                                    mdui.snackbar({
                                        message: '已删除模块' + mod.name
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '删除模块' + mod.name + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };


        $scope.showMod = function (mod) {
            $scope.ready = false;
            $http
                .get(['api','module',mod.id].join('/'))
                .then(function (result) {
                    $scope.mod = result.data;
                    $scope.ready = true;
                }, function (result) {
                });
        }

    });

