var questionsApp = angular.module('participantApp', []);

questionsApp.controller ('ParticipantController', function ($scope, $http) {

//    $scope.test = function () {
//    //    var vs = new WebSocket("@routes.ParticipantController.ws().webSocketURL(request)");
//        var ws = new WebSocket(wsRoute);
//        ws.onmessage = function (message) {
//            $scope.message = message;
//            console.log(message + "\n");
//        };
//    };

    var ws = new WebSocket(wsRoute);
    ws.onmessage = function (message) {
        $scope.message = message;

        if (message.data.question){
            $scope.question = message.data.question;
        }



        $scope.$apply();
        console.log(message + "\n");
    };

    $scope.message = "testmessage";
});

