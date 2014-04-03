var questionsApp = angular.module('questionsApp', []);

questionsApp.controller ('QuestionsController', function ($scope, $http) {
    $scope.newQuestion = function () {
        console.log("creating new question...");
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

    $scope.addQuestionlist = function () {
        alert($scope.questions)
        $http.post("addQuestionlist", $scope.questions);
    };
});