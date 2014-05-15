var app = angular.module('timer-directive', []);

app.directive('timer', function () {
    function link ($scope, el, attr){
        var duration = $scope.duration;
        var width = 128;
        var height = 128;


        var colorScale = d3.scale.linear()
            .domain([0,50])
            .range(["#E65C00", "#FF8533"]);

        var container = d3.select(el[0])
            .append("svg")
            .attr("width", width)
            .attr("height", height);


        var endAngle = Math.PI * 2;
        alert(endAngle);

        var arc = d3.svg.arc()
        .innerRadius(32)
        .outerRadius(64)
        .startAngle(0)
        .endAngle(endAngle);

        container.append(arc);

    }
    return {
        link: link,
        restrict: 'E',
        scope: { duration: "="}
    }
} );
