(function () {
	"use strict";
	angular
		.module("AtlasUi")
		.controller("ActionsReviewCtrl", ["$rootScope", "$scope", "$http", "$state", ActionsReviewCtrl]);

	function ActionsReviewCtrl($rootScope, $scope, $http, $state) {
		var ctrl = this;
        var listActionsUrl;

        if ($rootScope.globals.currentUser) {
            listActionsUrl = $rootScope.backend_api + "/actions?accountId=" + $rootScope.globals.currentUser.token;
        }

        // Initialization
        ctrl.init = function () {
            ctrl.listActions();
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
         *  List Atlas actions!
         */
        ctrl.listActions = function () {
            if ($rootScope.globals.currentUser === undefined) {
                return;
            }

            var headers = {
                "Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
            }

            $http.get(listActionsUrl, { headers: headers })
                .then(function successCallback(response) {
                    $scope.model = {data: response.data};
                    ctrl.sortDatesDesc();
                }, function errorCallback(response) {
                    $scope.model = {data: response.data};
                });
        }
	}
}());
