(function () {
	"use strict";
	angular
		.module("AtlasUi")
		.controller("DevicesAddCtrl", ["$rootScope", "$scope", "$http", "$state", "$cookies", "Utils", DevicesAddCtrl]);

	function DevicesAddCtrl($rootScope, $scope, $http, $state, $cookies, Utils) {
        var ctrl = this;
        ctrl.accountsUrl = $rootScope.backend_api + "/accounts";

        ctrl.init = function() {
            $scope.device = {};
            $scope.initialModel = angular.copy($scope.device);
        }

        ctrl.reset = function() {
            var message = "This will reset the form. Proceed anyway?";
            $scope.modalWarning(message, "RESET")
                .then(function (response) {
                    if (response === true) {
                        $scope.device = angular.copy($scope.initialModel);
                        // location.reload();
                        $scope.scrollTop();
                    }
                });
        }

        ctrl.cancel = function() {
            var message = "Your work will be lost. Proceed anyway?";
            $scope.modalWarning(message, "PROCEED")
                .then(function (response) {
                    if (response === true) {
                        $state.go("devices_review");
                        $scope.scrollTop();
                    }
                });
        }

        ctrl.getFormCtrl = function() {
            var retval = $scope.$$childHead
            if (retval) {
                retval = retval.formCtrl;
            }
            return retval;
        }

        ctrl.isValid = function() {
            var formCtrl = ctrl.getFormCtrl();
            if (formCtrl && formCtrl.$valid) {
                return true;
            }

            return false;
        }

        ctrl.add = function() {
            var message = "This will register device with id '"+$scope.device.id +"' to your account. Proceed?";
            var config = {
                headers : {
                    "Authorization": $http.defaults.headers.common.Authorization
                },
            }

            $scope.modalWarning(message, "ADD")
                .then(function (response) {
                    if (response === true) {

                        // var token = Utils.decode($cookies.get('globals')).currentUser.token;
                        var token = $cookies.getObject('globals').currentUser.token
                        var addDeviceUrl = ctrl.accountsUrl + "/" + token + "/devices/" + $scope.device.id;
                        $http.post(addDeviceUrl, null, config)
                            .then(function successCallback(response) {
                                $scope.refreshDevices();
                                $state.go("devices_review");
                                // Reload footer's img to switch from device to check-mark!
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                if ($rootScope.devices === 0) {
                                  location.reload();
                                } else {
                                  $scope.scrollTop();
                                }
                            }, function errorCallback(response) {
                                $scope.createToast(response.data.result + "! " + response.data.description)
                                // var message = response.data.result + "<br/>" + response.data.description;
                                // $scope.modalError(message, "100");
                            });
                    }
                });
        }
    }
}());
