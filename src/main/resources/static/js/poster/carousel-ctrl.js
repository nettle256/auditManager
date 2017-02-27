/**
 * Created by Nettle on 2017/2/7.
 */

auditManagerApp
    .config(['$stateProvider', function($stateProvider) {
        $stateProvider
            .state('carousel', {
                url: '/carousel',
                templateUrl: '/html/poster/carousel',
                controller: 'carouselCtrl'
            });
    }])
    .controller('carouselCtrl', function ($scope, $http, Upload) {

        $scope.carousels = [];

        $http
            .get(['api','carousel'].join('/'))
            .then(function (result) {
                $scope.carousels = result.data.sort(function (a, b) {
                    return a.index > b.index;
                });
            }, function () {

            });

        $scope.setDetail = function(carousel) {
            $scope.showImage = carousel.imageUrl;
        };

        $scope.saveOld = function (carousel) {
            carousel.oldText = carousel.text;
            carousel.oldIndex = carousel.index;
            carousel.oldConnect = carousel.connect;
        };

        $scope.save = function (carousel) {
            if (carousel.oldText == carousel.text && carousel.oldIndex == carousel.index && carousel.oldConnect == carousel.connect)
                return ;
            $http
                .put(['api','carousel',carousel.id].join('/'), carousel)
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

        $scope.deleteCarousel = function (carousel) {
            mdui.dialog({
                title: '确定要删除该图片？',
                content: '<img src="' + carousel.imageUrl + '" style="max-width: 100%;" />',
                buttons: [
                    {
                        text: '取消'
                    },
                    {
                        text: '确认',
                        onClick: function(){
                            $http
                                .delete(['api','carousel',carousel.id].join('/'), {})
                                .then(function (result) {
                                    carousel.deleted = true;
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
                    .post(['api','carousel'].join('/'), resp.data)
                    .then(function (result) {
                        console.log(result.data);
                        $scope.carousels.push(result.data);
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
