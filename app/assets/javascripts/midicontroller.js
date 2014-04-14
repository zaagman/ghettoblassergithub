var midicontrollerApp = angular.module('midicontrollerApp', []);

midicontrollerApp.controller ('MidicontrollerController', function ($scope, $http) {

    var ws = new WebSocket(midicontrollerRoute);
    ws.onmessage = function (message) {
        $scope.message = angular.fromJson(message.data);

        console.log($scope.message);

        if ($scope.message.sendNote){
            testNote($scope.message.sendNote);
            console.log("note sent: " + $scope.message.sendNote);
        }
        $scope.$apply();
    }

    $scope.test = function() {
        testNote(64);
        console.log("testnote sent...");
    }

    bootMidi();
});

