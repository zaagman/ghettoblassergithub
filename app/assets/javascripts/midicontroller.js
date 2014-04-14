var midicontrollerApp = angular.module('midicontrollerApp', ['ngCookies']);

midicontrollerApp.controller ('MidicontrollerController', function ($scope, $http, $cookieStore) {

    var midicontrollerID = $cookieStore.get('midicontrollerID')

    var ws = new WebSocket(midicontrollerRoute + midicontrollerID);
    ws.onmessage = function (message) {
        $scope.message = angular.fromJson(message.data);

        console.log($scope.message);

        if ($scope.message.sendNote){
            testNote($scope.message.sendNote);
            console.log("note sent: " + $scope.message.sendNote);
        }

        if ($scope.message.midicontrollerID){
                $cookieStore.put('midicontrollerID', $scope.message.midicontrollerID);
        }

        $scope.$apply();
    }

    $scope.test = function() {
        testNote(64);
        console.log("testnote sent...");
    }

    bootMidi();
});

