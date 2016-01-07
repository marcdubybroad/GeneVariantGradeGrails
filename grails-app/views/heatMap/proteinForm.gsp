<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Missense Interpreter</title>
    <style type="text/css" media="screen">
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
    #status {
        background-color: #eee;
        border: .2em solid #fff;
        margin: 2em 2em 1em;
        padding: 1em;
        width: 12em;
        float: left;
        -moz-box-shadow: 0px 0px 1.25em #ccc;
        -webkit-box-shadow: 0px 0px 1.25em #ccc;
        box-shadow: 0px 0px 1.25em #ccc;
        -moz-border-radius: 0.6em;
        -webkit-border-radius: 0.6em;
        border-radius: 0.6em;
    }

    .ie6 #status {
        display: inline; /* float double margin fix http://www.positioniseverything.net/explorer/doubled-margin.html */
    }

    #status ul {
        font-size: 0.9em;
        list-style-type: none;
        margin-bottom: 0.6em;
        padding: 0;
    }

    #status li {
        line-height: 1.3;
    }

    #status h1 {
        text-transform: uppercase;
        font-size: 1.1em;
        margin: 0 0 0.3em;
    }

    #page-body {
        margin: 2em 1em 1.25em 18em;
    }

    h2 {
        margin-top: 1em;
        margin-bottom: 0.3em;
        font-size: 1em;
    }

    p {
        line-height: 1.5;
        margin: 0.25em 0;
    }

    #controller-list ul {
        list-style-position: inside;
    }

    #controller-list li {
        line-height: 1.3;
        list-style-position: inside;
        margin: 0.25em 0;
    }

    @media screen and (max-width: 480px) {
        #status {
            display: none;
        }

        #page-body {
            margin: 0 1em 1em;
        }

        #page-body h1 {
            margin-top: 0;
        }
    }
        div.formWrapper {
            font-size: 16px;
            padding-top: 5px;
            padding-bottom: 5px;
        }
        div.bold {
            font-weight: bold;
        }
        div.title {
            font-size: 24px;
            font-weight: bold;
        }
    </style>
    <g:render template="d3"/>
</head>
<body>
<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div id="status" role="complementary">
</div>
<div id="page-body" role="main" class="formWrapper">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <g:form name="myForm" action="proteinSearch" id="1">
                    <div class="formWrapper">
                        <p class="bold-text">Enter Missense Variant</p>
                        <input id="searchbox" value="${lastQuery}" name="query" class="form-control input-lg awesomebar searchbox" type="text" placeholder="Search for a protein change or variant"/>
                        <p class="text-muted small-text">
                            Examples - Protein change: <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.Pro12Ala', prevalence: '1.0e-5']">p.Pro12Ala</g:link>,
                            Variant: <g:link action="proteinSearch" controller="heatMap" params="[query: 'chr3-12393125-C-G', prevalence: '1.0e-5']">chr3-12393125-C-G</g:link>
                        </p>
                        <p class="bold-text">Enter Prevalence</p>
                        <input id="prevalencebox" name="prevalence" class="form-control input-lg awesomebar prevalencebox" type="text" placeholder="Enter prevalence" value="${lastPrevalence ? lastPrevalence : '1.0e-5'}"/>
                    </div>
                    <div class="formWrapper">
                        <input type="submit" name="submit">
                    </div>
                </g:form>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="formWrapper bold">
                    <g:if test="${proteinResult != null}">
                        <g:if test="${proteinResult.getVariantDisplay()}">
                            For variant ${proteinResult.getVariantDisplay()}
                            <ul>
                                <li>The reference codon is <span class="cap">${proteinResult.getReferenceCodon()}</span></li>
                                <g:if test="${proteinResult.getAlternateCodon()}">
                                    <li>The modified codon is <span class="cap">${proteinResult.getAlternateCodon()}</span></li>
                                </g:if>
                                <g:if test="${proteinResult.isResultStopCodon()}">
                                    <li>The modified codon <span class="cap">${proteinResult.getAlternateCodon()}</span> is a stop codon</li>
                                </g:if>
                                <li>The protein change is ${proteinResult.getScientificAlleleCode()}</li>
                                <li>The integrated functional score is <g:formatNumber number="${proteinResult.getHeatAmount()}" type="number" maxFractionDigits="3" /></li>
                                <li>The clinical prediction is ${proteinResult.getEffect()}</li>
                                <li>At prevalence of ${proteinResult.getInputPrevalence()}</li>
                                <ul>
                                    <li>The pValue is ${proteinResult.getPValueClinicalScientificNotation()}</li>
                                </ul>
                            </ul>

                        </g:if>
                        <g:else>
                            For protein change ${proteinResult.getScientificAlleleCode()}
                            <ul>
                                <li>The reference codon is <span class="cap">${proteinResult.getReferenceCodon()}</span></li>
                                <li>The integrated functional score is <g:formatNumber number="${proteinResult.getHeatAmount()}" type="number" maxFractionDigits="3" /></li>
                                <li>The clinical prediction is ${proteinResult.getEffect()}</li>
                                <li>At prevalence of ${proteinResult.getInputPrevalence()}</li>
                                <ul>
                                    <li>The pValue is ${proteinResult.getPValueClinicalScientificNotation()}</li>
                                </ul>
                            </ul>
                        </g:else>
                        <p/>
                    </g:if>

                    <g:if test="${errorMessage}">
                        ${errorMessage}
                    </g:if>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div id="lineDiv" class="lineDiv"/>
            </div>
        </div>
    </div>
</div>

<g:if test="${proteinResult != null}">

<script type="text/javascript">
    var margin = {top: 0, right: 20, bottom: 30, left: 20},
            w = 600 - margin.left - margin.right,
            h = 300 - margin.top - margin.bottom;

    //    var parseDate = d3.time.format("%d-%b-%y").parse;

    //    var x = d3.time.scale().range([0, w]);
    var x = d3.scale.linear().range([0, w]);
    var y = d3.scale.linear().range([h, 0]);

    x.domain([-8, 5]);
    y.domain([0, 0.6]);

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom")
            .ticks(5);

    var yAxis = d3.svg.axis()
            .scale(y)
            .orient("left")
            .ticks(5);

    var svg = d3.select("div.lineDiv").append("svg")
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

    var scoreData = [{xdata: ${proteinResult.getHeatAmount()}, ydata:0}, {xdata: ${proteinResult.getHeatAmount()}, ydata: 0.5}];
    var scoreLine = d3.svg.line()
            .x(function(d) { return x(d.xdata); })
            .y(function(d) { return y(d.ydata); });

    svg.append("g").append("svg:path").attr("d", scoreLine(scoreData)).attr("class", "black");

    svg.selectAll('path.black').attr("stroke", "black");

    svg.append("g").append("svg:text")
            .attr("x", w * ((${proteinResult.getHeatAmount()} + 8)/12))
            .attr("y", 30)
            .attr("text-anchor", "middle")
            .style("font-size", "16px")
            .style("fill", "black")
            .text('${proteinResult.getScientificAlleleCode()}');

</script>
</g:if>
</body>
</html>
