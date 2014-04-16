var performerApp = angular.module('performerApp', ['ngCookies']);

performerApp.controller ('PerformerController', function ($scope, $http, $cookieStore) {

    var performerID = $cookieStore.get('performerID');
    var ws = new WebSocket(performerRoute + performerID);
    console.log(performerRoute + performerID);
    ws.onmessage = function (message) {
        $scope.message = angular.fromJson(message.data);

        if ($scope.message.performerID) {
            $cookieStore.put('performerID', $scope.message.performerID);
            console.log("putting in performerID");
        }



        console.log($scope.message);
        $scope.$apply();
    };

    $scope.start = function () {
        ws.send(angular.toJson({
            "start": 1
        }));
        console.log("sent start...");
    };

});

