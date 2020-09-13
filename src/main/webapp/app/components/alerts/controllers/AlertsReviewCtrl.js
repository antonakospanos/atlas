(function () {
	"use strict";
	angular
		.module("AtlasUi")
		.controller("AlertsReviewCtrl", ["$rootScope", "$scope", "$http", "$state", AlertsReviewCtrl]);

	function AlertsReviewCtrl($rootScope, $scope, $http, $state) {
		var ctrl = this;
        var listAlertsUrl;

        if ($rootScope.globals.currentUser) {
            listAlertsUrl = $rootScope.backend_api + "/alerts?accountId=" + $rootScope.globals.currentUser.token;
        }

        // Initialization
        ctrl.init = function () {
            ctrl.listAlerts();
        }

        // Sorting states
        var sortedByDatesDesc = false;

        ctrl.sortDates = function () {
            if (ctrl.sortedByDatesDesc === true) {
                ctrl.sortDatesAsc();
            } else {
                ctrl.sortDatesDesc();
            }
        }

        ctrl.sortDatesDesc = function () {
            ctrl.sortedByDatesDesc = true;
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return 1;
                if (dateB < dateA)
                    return -1;
                return 0;
            });
        }

        ctrl.sortDatesAsc = function () {
            ctrl.sortedByDatesDesc = false;
            $scope.model.data.sort(function (a, b) {
                var dateA = a && a.publicationDate ? a.publicationDate : 0;
                var dateB = b && b.publicationDate ? b.publicationDate : 0;
                if (dateA < dateB)
                    return -1;
                if (dateB < dateA)
                    return 1;
                return 0;
            });
        }

        /**
         *  List Atlas alerts!
         */
        ctrl.listAlerts = function () {
            if ($rootScope.globals.currentUser === undefined) {
                return;
            }

            var headers = {
                "Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
            }

            $http.get(listAlertsUrl, { headers: headers })
                .then(function successCallback(response) {
                    $scope.model = {data: response.data};
                    ctrl.sortDatesDesc();
                }, function errorCallback(response) {
                    $scope.model = {data: response.data};
                });
        }
	}
}());
