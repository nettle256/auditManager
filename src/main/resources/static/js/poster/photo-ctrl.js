/**
 * Created by Nettle on 2017/2/10.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('photo', {
                url: '/photo',
                templateUrl: '/html/poster/photo',
                controller: 'photoCtrl'
            });
    }])
    .controller('photoCtrl', function ($scope, $http, Upload) {

        $scope.photos = [];

        $http
            .get(['api','photo'].join('/'))
            .then(function (result) {
                $scope.photos = result.data.sort(function (a, b) {
                    return a.index > b.index;
                });
            }, function () {

            });

        $scope.setDetail = function(photo) {
            $scope.showImage = photo.imageUrl;
        };

        $scope.saveOld = function (photo) {
            photo.oldIndex = photo.index;
        };

        $scope.save = function (photo) {
            if (photo.oldIndex == photo.index)
                return ;
            $http
                .put(['api','photo',photo.id].join('/'), photo)
                .then(function (result) {
                    mdui.snackbar({
                        message: '成功修改图片信息'
                    });
                }, function (result) {
                    mdui.snackbar({
                        message: '修改图片信息失败'
                    });
                });
        };

        $scope.deletePhoto = function (photo) {
            mdui.dialog({
                title: '确定要删除该图片？',
                content: '<img src="' + photo.imageUrl + '" style="max-width: 100%;" />',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','photo',photo.id].join('/'), {})
                                .then(function (result) {
                                    photo.deleted = true;
                                    mdui.snackbar({
                                        message: '已删除图片'
                                    });
                                }, function (result) {
                                    mdui.snackbar({
                                        message: '删除图片' + user.username + '失败'
                                    });
                                });
                        }
                    }
                ]
            })
        };

        // upload on file select or drop
        $scope.uploading = false;
        $scope.upload = function (file) {
            $scope.uploading = true;
            Upload.upload({
                url: 'api/upload/image',
                data: {file: file, 'username': $scope.username}
            }).then(function (resp) {
                // console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                mdui.snackbar({
                    message: '成功上传图片，正在创建滚动图片',
                    timeout: 1000
                });
                $http
                    .post(['api','photo'].join('/'), resp.data)
                    .then(function (result) {
                        console.log(result.data);
                        $scope.photos.push(result.data);
                        mdui.snackbar({
                            message: '成功创建滚动图片'
                        });
                        $scope.uploading = false;
                    }, function (result) {
                        mdui.snackbar({
                            message: '创建滚动图片失败'
                        });
                        $scope.uploading = false;
                    });

            }, function (resp) {
                // console.log('Error status: ' + resp.status);
                mdui.snackbar({
                    message: '上传图片失败'
                });
                $scope.uploading = false;
            }, function (evt) {
                $scope.progress = parseInt(100.0 * evt.loaded / evt.total);
                // var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                // console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            });
        };
    });
