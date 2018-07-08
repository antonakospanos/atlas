(function () {
    'use strict';

    /**
     * Application wide Modal Message Controller
     */
    angular
        .module('AtlasUi')
        .controller('MessageInstanceCtrl', function ($scope, $modalInstance, message, $sce) {

            $scope.message = $sce.trustAsHtml(message);
            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
    });

})();