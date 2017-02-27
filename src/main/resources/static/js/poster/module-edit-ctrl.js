/**
 * Created by Nettle on 2017/2/8.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('module_new', {
                url: '/information/new',
                templateUrl: '/html/poster/moduleEdit',
                controller: 'moduleEditCtrl'
            })
            .state('module_edit', {
                url: '/information/{id:[0-9]+}/edit',
                templateUrl: '/html/poster/moduleEdit',
                controller: 'moduleEditCtrl'
            });
    }])
    .controller('moduleEditCtrl', function ($scope, $http, $stateParams, $state) {

        $scope.modified = false;

        if ($stateParams.id) {
            $scope.id = $stateParams.id;
            $scope.isNew = false;
            $http
                .get(['api','module',$scope.id].join('/'))
                .then(function (result) {
                   $scope.mod = result.data;
                   $scope.modified = false;
                }, function (result) {
                });
        }   else {
            $scope.isNew = true;
            $scope.mod = {
                name: '',
                content: ''
            }
        }

        $scope.save = function() {
            if ($scope.isNew) {
                $http
                    .post(['api','module'].join('/'), $scope.mod)
                    .then(function () {
                       $state.go('information');
                    }, function () {
                        mdui.snackbar({
                            message: '新建模板失败'
                        });
                    });
            }   else {
                $http
                    .put(['api','module',$scope.mod.id].join('/'), $scope.mod)
                    .then(function () {
                        $state.go('information');
                    }, function () {
                        mdui.snackbar({
                            message: '保存模板失败'
                        });
                    });
            }
        };
        
        $scope.cancel = function () {
            mdui.dialog({
                content: '确定要放弃' + ($scope.isNew ? '新建模板' : '修改'),
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $state.go('information');
                        }
                    }
                ]
            })
        };

    });