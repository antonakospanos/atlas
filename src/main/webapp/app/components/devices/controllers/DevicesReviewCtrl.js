(function () {
	"use strict";
	angular
		.module("AtlasUi")
		.controller("DevicesReviewCtrl", ["$rootScope", "$scope", "$http", "$state", DevicesReviewCtrl]);

	function DevicesReviewCtrl($rootScope, $scope, $http, $state) {
		var ctrl = this;
        var listDevicesUrl;

        if ($rootScope.globals.currentUser) {
            listDevicesUrl = $rootScope.backend_protocol + "://" +
                             $rootScope.backend_ip + ":" + $rootScope.backend_port + "/" + $rootScope.backend_context_path +
                             "/accounts/" +
                             $rootScope.globals.currentUser.token +
                             "/devices";
        }

        // Initialization
        ctrl.init = function () {
            ctrl.listDevices();
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
         *  List Atlas devices!
         */
        ctrl.listDevices = function () {
            if ($rootScope.globals.currentUser === undefined) {
                return;
            }

            var headers = {
                "Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
            }

            $http.get(listDevicesUrl, { headers: headers })
                .then(function successCallback(response) {
                    $scope.model = {data: response.data};
                    ctrl.sortDatesDesc();
                }, function errorCallback(response) {
                    $scope.model = {data: response.data};
                });
        }

        // /**
        //  *  Vote for a device!
        //  *
        //  * @param item
        //  */
        // ctrl.like = function (item) {
        //     $scope.modalWarning("Are you sure you like '" + item.title + "' ?", "LIKE")
        //         .then(function (response) {
        //             if (response === true) {
        //
        //             	var headers = {
        //                     "Content-Type": "application/json",
			// 				"Authorization": $http.defaults.headers.common.Authorization // $cookies.global.accessToken
			// 			}
        //             	var body = {
        //                     "like": true,
        //                     "device": item.id
			// 			}
        //                 $http.put(voteDevicesUrl, body, { headers: headers })
        //                     .then(function successCallback(response) {
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                         ctrl.listDevices()
        //                     }, function errorCallback(response) {
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                     });
        //             }
        //         });
        // }
        //
        // /**
        //  *  Vote against a device!
        //  *
        //  * @param item
        //  */
        // ctrl.hate = function (item) {
        //     $scope.modalAlert("Are you sure you hate '" + item.title + "' ?", "HATE")
        //         .then(function (response) {
        //             if (response === true) {
        //
        //                 var headers = {
        //                     "Content-Type": "application/json",
        //                     "Authorization": $http.defaults.headers.common.Authorization
        //                 }
        //                 var body = {
        //                     "like": false,
        //                     "device": item.id
        //                 }
        //                 $http.put(voteDevicesUrl, body, { headers: headers })
        //                     .then(function successCallback(response) {
        //                         console.log("INFO:" + response.data);
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                         ctrl.listDevices()
        //                     }, function errorCallback(response) {
        //                         console.log("ERROR: " + response.data);
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                     });
        //             }
        //         });
        // }
        //
        //
        // /**
        //  *  Retract vote!
        //  *
        //  * @param item
        //  */
        // ctrl.retract = function (item) {
        //     item.vote = item.like ? "positive vote" : "negative vote";
        //     $scope.modalAlert("Do you want to retract the " +item.vote+ " to '" + item.title + "' ?", "RETRACT")
        //         .then(function (response) {
        //             if (response === true) {
        //
        //                 var headers = {
        //                     "Authorization": $http.defaults.headers.common.Authorization
        //                 }
        //                 $http.delete(voteDevicesUrl + "?device=" + item.id, null, { headers: headers })
        //                     .then(function successCallback(response) {
        //                         console.log("INFO:" + response.data);
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                         ctrl.listDevices()
        //                         $scope.scrollTop();
        //                     }, function errorCallback(response) {
        //                         console.log("ERROR: " + response.data);
        //                         $scope.createToast(response.data.result + "! " + response.data.description)
        //                         $scope.scrollTop();
        //                     });
        //             }
        //         });
        // }
        //
        // /**
        //  *  Hide voting buttons to un-subscribed users!
        //  *
        //  * @param item
        //  */
        // ctrl.hideVotingButtons= function (item) {
        //     return !$http.defaults.headers.common.Authorization
        // }
        //
        // /**
        //  *  Retrieves the user's vote to the Device!
        //  *
        //  * @param item
        //  */
        // ctrl.findVote= function (item) {
        //     $http({
        //         url: voteDevicesUrl + "?device=" + item.id
        //     }).then(function successCallback(response) {
        //         item.like = response.data.like;
        //     });
        // }
        //
        // /**
        //  *  Hide like button to fans!
        //  *
        //  * @param item
        //  */
        // ctrl.hideLikeButton= function (item) {
        //     return item.like !== undefined && item.like === true
        // }
        //
        // /**
        //  *  Hide hate button haters!
        //  *
        //  * @param item
        //  */
        // ctrl.hideHateButton= function (item) {
        //     return item.like !== undefined && item.like === false
        // }
        //
        // /**
        //  *  Hide retract like button to users that have not hated yet!
        //  *
        //  * @param item
        //  */
        // ctrl.hideRetractLikeButton = function (item) {
        //     return item.like === undefined || item.like === false
        // }
        //
        // /**
        //  *  Hide retract hate button to users that have not hated yet!
        //  *
        //  * @param item
        //  */
        // ctrl.hideRetractHateButton = function (item) {
        //     return item.like === undefined || item.like === true
        // }
	}
}());