<%--
  Created by IntelliJ IDEA.
  User: mduby
  Date: 1/4/16
  Time: 5:24 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <script src="http://d3js.org/d3.v3.js"></script>
    <style>

    body {
        font: 10px verdana;
    }
    .axis path,
    .axis line {
        fill: none;
        stroke: #333;
    }
    .line {
        fill: none;
        stroke: steelblue;
        stroke-width: 1.5px;
    }

    path {
        stroke-width: 3;
        fill: none;
    }
    line {
        stroke: black;
    }
    </style>
</head>

<body>
<script type="text/javascript">
    var margin = {top: 70, right: 20, bottom: 30, left: 50},
            w = 400 - margin.left - margin.right,
            h = 400 - margin.top - margin.bottom;

//    var parseDate = d3.time.format("%d-%b-%y").parse;

//    var x = d3.time.scale().range([0, w]);
    var x = d3.scale.linear().range([0, w]);
    var y = d3.scale.linear().range([h, 0]);

    x.domain([-8, 4]);
    y.domain([0, 0.6]);

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom")
            .ticks(5);

    var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .ticks(5);

    var svg = d3.select("body").append("svg")
            .attr("width", w + margin.left + margin.right)
            .attr("height", h + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + h + ")")
            .call(xAxis);

    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis);


//    d3.csv("${g.resource(file:'data_01.csv')}", function(error, data) {
    d3.csv("${g.resource(file:'deleterious.csv')}", function(error, data) {
        // Here we will put all the SVG elements affected by the data
        // on the file!!!
        data.forEach(function(d) {
            d.score = +d.score;
            d.frequency = +d.frequency;
        });

//        x.domain(d3.extent(data, function(d) { return d.score; }));
//        y.domain(d3.extent(data, function(d) { return d.frequency; }));

        var line = d3.svg.line()
                .x(function(d) { return x(d.score); })
                .y(function(d) { return y(d.frequency); });

        svg.append("g").append("svg:path").attr("d", line(data)).attr("class", "red");

        svg.select('path.red').attr("stroke", "red");
    });

    d3.csv("${g.resource(file:'benign.csv')}", function(error, data) {
        // Here we will put all the SVG elements affected by the data
        // on the file!!!
        data.forEach(function(d) {
            d.score = +d.score;
            d.frequency = +d.frequency;
        });

//        x.domain(d3.extent(data, function(d) { return d.score; }));
//        y.domain(d3.extent(data, function(d) { return d.frequency; }));

        var line = d3.svg.line()
                .x(function(d) { return x(d.score); })
                .y(function(d) { return y(d.frequency); });

        svg.append("g").append("svg:path").attr("d", line(data)).attr("class", "green");

        svg.selectAll('path.green').attr("stroke", "green");
    });

</script>
</body>
</html>