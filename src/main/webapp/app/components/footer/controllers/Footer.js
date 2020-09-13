/* global angular, _, configData,moment,$,waitingDialog */
(function () {
    "use strict";
    angular
        .module("AtlasUi")
        .controller("FooterCtrl", ["$rootScope", "$scope", "$http", "$mdToast", "$cookies", FooterCtrl]);

    function FooterCtrl($rootScope, $scope, $http, $mdToast, $cookies) {

        // Auto refresh footer every 5 seconds to re-calculate backend changes
        // $scope.intervalTimer = setInterval(function () {
        //     $scope.refreshDevices()
        // }, 5000);

        $scope.refreshDevices = function() {
            refreshDevices();
        }

        $scope.initFooter = function initFooter() {
            setConfigCheck('none')
            setConfigAlert('initial')
            setConfigPrompt("Log in or register to the IoT platform ..");
            setProgressBar('none')
            refreshDevices();
        }

        function refreshDevices() {
            var accountsUrl = $rootScope.backend_api + "/accounts";

            if ($cookies.getObject('globals')) {
                // Lookup for /devices
                var token = $cookies.getObject('globals').currentUser.token
                $http.get(accountsUrl + "/" + token + "/devices" )
                    .then(function successCallback(response) {
                        $rootScope.devices = response.data.length;
                        setConfigCheck('initial')
                        setConfigAlert('none')
                        setConfigPrompt($rootScope.devices + " devices registered in the IoT platform");
                    }, function errorCallback(response) {
                        $rootScope.devices = response.data.length;
                        setConfigCheck('none')
                        setConfigAlert('initial')
                        if ($rootScope.devices) {
                            setConfigPrompt($rootScope.devices + " devices registered in the IoT platform");
                        } else {
                            setConfigPrompt("0 devices registered in the IoT platform");
                        }
                    });
            } else {
                setConfigCheck('none')
                setConfigAlert('initial')
                setConfigPrompt("Log in or register to the IoT platform ..");
            }
        }

        function setConfigCheck(display) {
            var check = document.getElementById("configCheck");
            check.style.display = display;
        }

        function setConfigAlert(display) {
            var alert = document.getElementById("configAlert");
            alert.style.display = display;
        }

        function setConfigPrompt(msg) {
            var divElement = document.getElementById("configPrompt");
            for (var i = 0; i < divElement.childNodes.length; ++i) {
                divElement.removeChild(divElement.childNodes[i]);
            }
            var textNode = document.createTextNode(msg);
            divElement.appendChild(textNode);
        }

        function setProgressBar(display) {
            var actions = document.getElementById("progressBar");
            actions.style.display = display;
        }

        $scope.createToast = function(message) {
            $mdToast.show(
                $mdToast.simple()
                    .textContent(message)
                    .parent(document.querySelectorAll('#toaster'))
                    .hideDelay(5000)
                    .action('x')
                    .capsule(true)
            );
        };
    }
}());
