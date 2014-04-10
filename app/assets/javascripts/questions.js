var questionsApp = angular.module('questionsApp', []);

questionsApp.controller ('QuestionsController', function ($scope, $http) {
    $scope.newQuestion = function () {
        if (!$scope.questions){
            $scope.questions = [];
        }
        $scope.questions.push({});
    };

    $scope.removeQuestion = function (question) {
        $scope.questions.splice($scope.questions.indexOf(question), 1);
    };

    $scope.newAnswer = function (question){
        if (!question.answers){
            console.log("creating answers...");
            question.answers = [];
        }
        question.answers.push({});
    };

    $scope.removeAnswer = function (question, answer) {
        question.answers.splice(question.answers.indexOf(answer), 1);
    };

    $scope.addQuestionlist = function() {
        $http.post("addQuestionlist", $scope.questions);
    };

    $scope.testMidi = function( answer ) {
        toConsole("testing note: " + answer.note);
        testNote(answer.note);
    };

    $scope.bootMidi = function () {
        bootMidi();
    };

    $scope.questions = [{"questiontext":"Testquestion 1","time":10,"duration":60,"answers":[{"answertext":"Antwoord 1","note":1},{"answertext":"Antwoord 2","note":2}]},{"questiontext":"Vraag 2","time":20,"duration":60,"answers":[{"answertext":"Antwoord 1","note":3},{"answertext":"Antwoord 2","note":4}]}];
});