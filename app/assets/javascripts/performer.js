var performerApp = angular.module('performerApp', []);

performerApp.controller('PerformerController', function ($scope, $http) {

    var ws = new WebSocket(performerRoute);
    ws.onmessage = function (message) {
        $scope.message = angular.fromJson(message.data);

        console.log($scope.message);

        if ($scope.message.standing){
            $scope.standing = $scope.message.standing.question;
            $scope.$apply();
        }
    }

    $scope.start = function (){
        ws.send(angular.toJson({"start": 1}));
        console.log("sent start...");
    }

});

