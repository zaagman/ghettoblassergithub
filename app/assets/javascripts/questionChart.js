var app = angular.module('question-chart-directive', []);

app.directive('questionChart', function () {
    function link ($scope, el, attr){
        var question = $scope.question;
        var width = 480;
        var height = 480;


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
            .attr("width", widthScale(question.answers[0].reactions))
            .attr("fill", colorScale(question.answers[0].reactions));

//        var bars = container.selectAll("rect")
//            .data(question.answers)
//            .enter()
//                .append("rect")
//                .attr("y", function (d, i) {return i * 70;})
//                .attr("height", 60)
////                .attr("width", 0)
////                .attr("fill", function (d) { return colorScale(0)})
////                .transition()
////                .duration(1000)
//                .attr("width", function (d) {return widthScale(d.reactions);})
//                .attr("fill", function (d) { return colorScale(d.reactions);});
//
//
//        var texts = container.selectAll("text")
//            .data(question.answers)
//            .enter()
//                .append("text")
//                .attr("x", 15)
//                .attr("y", function (d, i) {return i * 70 + 37;})
//                .text(function (d) { return d.answertext;})
//                .attr("fill", "white")
//                .attr("font-family", "sans-serif")
//                .attr("font-size", "20px");
////                .on("click", function(d) { $scope.sendReaction(question, d)});

    }
    return {
        link: link,
        restrict: 'E',
        scope: { question: "="}
    }
} );





