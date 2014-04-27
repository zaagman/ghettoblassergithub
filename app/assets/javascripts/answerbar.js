var app = angular.module('answer-bar-directive', []);

app.directive('answerBar', function () {
    function link ($scope, el, attr){
        var answer = $scope.answer;
        var width = 480;
        var height = 70;


//        var maxReactions = function (){
//            var max = 0;
//            for (var answer in question.answers){
//                console.log(answer.answer);
//                if (max < answer.reactions){
//                    max = answer.reactions
//                }
//            }
//            console.log("max: " + max);
//            return max;
//        }

        var widthScale = d3.scale.linear()
            .domain([0,50])
            .range([10,480]);

        var colorScale = d3.scale.linear()
            .domain([0,50])
            .range(["#E65C00", "#FF8533"]);

        var container = d3.select(el[0])
            .append("svg")
            .attr("width", width)
            .attr("height", height);


        var bars = container.append("rect")
            .attr("y", 0)
            .attr("height", 60)
//                .attr("width", 0)
//                .attr("fill", function (d) { return colorScale(0)})
//                .transition()
//                .duration(1000)
            .attr("width", widthScale(answer.reactions))
            .attr("fill", colorScale(answer.reactions));

        var texts = container.append("text")
            .attr("x", 15)
            .attr("y", 37)
            .text(answer.answertext)
            .attr("fill", "white")
            .attr("font-family", "sans-serif")
            .attr("font-size", "20px");

    }
    return {
        link: link,
        restrict: 'E',
        scope: { answer: "="}
    }
} );





