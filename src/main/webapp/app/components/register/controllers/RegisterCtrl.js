(function () {
    'use strict';

    angular
        .module('AtlasUi')
        .controller('RegisterCtrl', RegisterCtrl);

    RegisterCtrl.$inject = ['UserService', 'AuthenticationService', '$location', '$scope', "$state"];
    function RegisterCtrl(UserService, AuthenticationService, $location, $scope, $state) {
        var ctrl = this;
        ctrl.register = register;

        function register() {
            ctrl.user.timestamp = Date.now();
            UserService.Create(ctrl.user)
                .then(function (response) {
                    console.log(response)
                    $scope.createToast(response.data.result + "! " + response.data.description)
                    if (response.data.result === 'SUCCESS') {
                        AuthenticationService.Authorize(ctrl.username, response.data.data.id);
                        $scope.loggedIn();
                        $state.go("devices_review");
                        // $location.path("/");
                    }
                });
        }
    }
})();