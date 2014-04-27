var app = angular.module('app', ['ngCookies']);

app.controller ('QuestionsController', function ($scope, $http, $cookieStore) {
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
        var valid = true;
        if (angular.isUndefined($scope.questions)){
            valid = false;
        }


//        else {
//            for(var question in $scope.questions){
//                if(angular.isUndefined(question.questiontext)) valid = false;
//                if(angular.isUndefined(question.time)) valid = false;
//                if(angular.isUndefined(question.duration)) valid = false;
//                if(angular.isUndefined(question.end)) valid = false;
//                if(angular.isUndefined(question.answers)) {
//                 valid = false;
//                 } else {
//                    for (var answer in question.answers){
//                        if (angular.isUndefined(answer.answertext)) valid = false;
//                        if (angular.isUndefined(answer.note)) valid = false;
//                    }
//                }
//            }
//        }

        if (valid){
        $http.post("addQuestionlist", $scope.questions);
        $cookieStore.put("questionlist", $scope.questions);
        }
        else {
            alert("Please fill out all fields and have atleast one answer per question")
        }
    };

    $scope.testMidi = function( answer ) {
        toConsole("testing note: " + answer.note);
        testNote(answer.note);
    };

    $scope.bootMidi = function () {
        bootMidi();
    };

    $scope.questions = $cookieStore.get('questionlist');
    //$scope.questions = [{"questiontext":"Vraag 2","time":10,"duration":60, "end":20,"answers":[{"answertext":"Antwoord 1","note":1},{"answertext":"Antwoord 2","note":2}]},{"questiontext":"Vraag 3","time":20,"duration":60, "end":20,"answers":[{"answertext":"Antwoord 1","note":3},{"answertext":"Antwoord 2","note":4}]},{"questiontext":"Vraag 1","time":0,"duration":60,  "end":20,"answers":[{"answertext":"Antwoord 1","note":5},{"answertext":"Antwoord 2","note":6}]}];
});