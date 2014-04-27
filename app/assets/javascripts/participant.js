var app = angular.module('app', ['ngCookies', 'answer-bar-directive']);

app.controller ('ParticipantController', function ($scope, $http, $cookieStore) {

    var participantID = $cookieStore.get('participantID');

    var ws = new WebSocket(participantRoute + participantID);
    console.log(participantRoute + participantID);
    ws.onmessage = function (message) {
        console.log(angular.fromJson(message.data));

        $scope.message = angular.fromJson(message.data);


        if ($scope.message.participantID){
            $cookieStore.put('participantID', $scope.message.participantID);
        }

        $scope.$apply();
        console.log($scope.message + "\n");
    };

    $scope.sendReaction = function (question, answer){
        ws.send(angular.toJson(answer));
        console.log ("reaction sent: " + answer);
    }

    $scope.message = "testmessage";
});

