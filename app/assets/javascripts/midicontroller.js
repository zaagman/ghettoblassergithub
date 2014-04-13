var questionsApp = angular.module('midicontrollerApp', []);

questionsApp.controller ('MidicontrollerController', function ($scope, $http) {

    var ws = new WebSocket(wsRoute);
    ws.onmessage = function (message) {
        $scope.message = angular.fromJson(message.data);

        console.log($scope.message);

        if ($scope.message.sendNote){
            testNote($scope.message.sendNote);
            console.log("note sent: " + $scope.message.sendNote);
        }
        $scope.$apply();
        console.log(message + "\n");
    }

    $scope.test = function() {
        testNote(64);
        console.log("testnote sent...");
    }

    bootMidi();
});

