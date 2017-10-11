<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Cancer Missense Interpreter</title>
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
/*        margin: 2em 1em 1.25em 18em; */
        margin: 1em 10em 1em 10em;
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
    <g:render template="googleAnalytics"/>
    <g:render template="d3"/>
</head>
<body>
<a href="#page-body" class="skip"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div id="page-body" role="main" class="formWrapper">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <g:form name="myForm" action="proteinSearch" id="1">
                    <div class="formWrapper">
                        <div class="description">The CMITER classifier provides functional classification and supports quantitative predictions of pathogenicity for any missense variant using comprehensive experimental data. It currently supports functional classification for missense variants in the gene <span class="miterItalic">p53</span> and quantitative predictions for familial partial lipodystrophy 3 (FPLD3) and Type 2 Diabetes.
                        <br/>See <a href="http://dx.doi.org/10.1038/ng.3700" target="newWindow">Majithia AR, et al. Prospective functional classification of all possible missense variants in p53. Nature Genetics. 2016 doi:10.1038/ng.3700</a> for details.</div>
                        <div class="apptitle-black description">p53</div>
                        <p class="bold-text">Enter Missense Variant</p>
                        <input id="searchbox" value="${lastQuery}" name="query" class="form-control input-lg awesomebar searchbox" type="text" placeholder="Search for a protein change or variant"/>
                        <p class="text-muted small-text">
                            Examples - Protein change <a href="#" title='Numbering with respect to PPAR&#611; isoform 2 (to convert isoform 1 to 2 add 28)'><g:img dir="images" file="question2.png" width="17" height="17"/></a>:
                            <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.P12A', prevalence: '1.0e-5']">p.P12A</g:link> or
                            <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.Pro12Ala', prevalence: '1.0e-5']">p.Pro12Ala</g:link>,
                            Genomic coordinates  <a href="#" title='chr3-<base position hg18>-<reference base>-<identified base change>'><g:img dir="images" file="question2.png" width="17" height="17"/></a>:
                        <g:link action="proteinSearch" controller="heatMap" params="[query: 'chr3-12393125-C-G', prevalence: '1.0e-5']">chr3-12393125-C-G</g:link>
                        </p>
                        <p class="bold-text">Enter Disease Prevalence  <a href="#" title="Disease prevalence of familial partial lipodystrophy 3 (FPLD3) in the general population is 1:100,000 (default) to 1:1,000,000. In specialist clinics this can be as high as 1:5 (0.20)."><g:img dir="images" file="question2.png" width="17" height="17"/></a></p>
                        <input id="prevalencebox" name="prevalence" class="form-control input-lg awesomebar prevalencebox" type="text" placeholder="Enter disease prevalence" value="${lastPrevalence ? lastPrevalence : '1.0e-5'}"/>
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
                            <div class="row reduced-width">
                                <div class="col-md-4">
                                    <table class="table">
                                        <thead>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>Variant</td>
                                            <td>${proteinResult.getVariantDisplay()}</td>
                                        </tr>
                                        <tr>
                                            <td>Reference codon  <a href="#" class="tool" title="The three nucleotide sequence specifying the reference amino acid."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><span class="cap">${proteinResult.getReferenceCodon()}</span></td>
                                        </tr>
                                        <g:if test="${proteinResult.getAlternateCodon()}">
                                            <tr>
                                                <td>Modified Codon</td>
                                                <td><span class="cap">${proteinResult.getAlternateCodon()}</span></td>
                                            </tr>
                                        </g:if>
                                        <g:if test="${proteinResult.isResultStopCodon()}">
                                            <tr>
                                                <td>Modified codon</td>
                                                <td>is a stop codon</td>
                                            </tr>
                                        </g:if>
                                        <tr>
                                            <td>Reference amino acid</td>
                                            <td>${proteinResult.getAminoAcidReference()}</td>
                                        </tr>
                                        <tr>
                                            <td>Protein change</td>
                                            <td>${proteinResult.getScientificAlleleCode()}</td>
                                        </tr>
                                        <tr>
                                            <td>Experimental function score  <a href="#" title="Also referred to as integrated functional score (IFS) in Majithia et. al. 2016, quantitative measure of the ability of PPARG containing the variant to stimulate CD36 across multiple agonist conditions and doses."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><g:formatNumber number="${proteinResult.getHeatAmount()}" type="number" maxFractionDigits="3" /></td>
                                        </tr>
                                        <tr>
                                            <td>At disease prevalence</td>
                                            <td>${proteinResult.getInputPrevalence()}</td>
                                        </tr>
                                        <tr>
                                            <td>Probability of causing FPLD3  <a href="#" title="The likelihood that the variant queried causes FPLD3. Calculated by combining experimental function score with disease prevalence inputted."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getOddsRatioOfDiseaseString()}</td>
                                        </tr>
                                        <tr>
                                            <td>Clinical prediction for FPLD3  <a href="#" title='An assessment to guide clinical decisions regarding whether the variant is likely to cause FPLD3 (“pathogenic”) or unlikely (“not pathogenic”).'><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getEffect()}</td>
                                        </tr>
                                        <tr>
                                            <td>Clinical prediction for type 2 diabetes <a href="#" title='The risk for type 2 diabetes conferred by the variant as estimated from PPARG variant carriers identified in 21,000 case/controls.'><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getDiabetesRiskString()}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-md-8">

                                </div>
                            </div>

                        </g:if>
                        <g:else>
                            <div class="row reduced-width">
                                <div class="col-md-4">
                                    <table class="table">
                                        <thead>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>Reference amino acid</td>
                                            <td>${proteinResult.getAminoAcidReference()}</td>
                                        </tr>
                                        <tr>
                                            <td>Reference codon  <a href="#" title="The three nucleotide sequence specifying the reference amino acid."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><span class="cap">${proteinResult.getReferenceCodon()}</span></td>
                                        </tr>
                                        <tr>
                                            <td>Protein change</td>
                                            <td>${proteinResult.getScientificAlleleCode()}</td>
                                        </tr>
                                        <tr>
                                            <td>Experimental function score  <a href="#" title="Also referred to as integrated functional score (IFS) in Majithia et. al. 2016, quantitative measure of the ability of PPARG containing the variant to stimulate CD36 across multiple agonist conditions and doses."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><g:formatNumber number="${proteinResult.getHeatAmount()}" type="number" maxFractionDigits="3" /></td>
                                        </tr>
                                        <tr>
                                            <td>At disease prevalence</td>
                                            <td>${proteinResult.getInputPrevalence()}</td>
                                        </tr>
                                        <tr>
                                            <td>Probability of causing FPLD3  <a href="#" title="The likelihood that the variant queried causes FPLD3. Calculated by combining experimental function score with disease prevalence inputted."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getOddsRatioOfDiseaseString()}</td>
                                        </tr>
                                        <tr>
                                            <td>Clinical prediction for FPLD3  <a href="#" title='An assessment to guide clinical decisions regarding whether the variant is likely to cause FPLD3 (“pathogenic”) or unlikely (“not pathogenic”).'><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getEffect()}</td>
                                        </tr>
                                        <tr>
                                            <td>Clinical prediction for type 2 diabetes <a href="#" title='The risk for type 2 diabetes conferred by the variant as estimated from PPARG variant carriers identified in 21,000 case/controls.'><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getDiabetesRiskString()}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-md-8">

                                </div>
                            </div>
                        </g:else>
                    </g:if>

                    <g:if test="${errorMessage}">
                        ${errorMessage}
                    </g:if>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div id="lineDiv" class="lineDiv"></div>
            </div>
        </div>
    </div>
</div>

<g:if test="${proteinResult != null}">

<script type="text/javascript">
    var margin = {top: 0, right: 20, bottom: 60, left: 60},
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

    var labels = svg.append("g")
            .attr("class", "labels");

    labels.append("text")
            .attr("transform", "translate(0," + (h + 10) + ")")
            .attr("x", (w/3))
            .attr("style","font-size:20px;")
            .attr("dx", "-1.0em")
            .attr("dy", "2.0em")
            .text("experimental function score");

    labels.append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", -60)
            .attr("x", -30)
            .attr("style","font-size:20px;")
            .attr("dy", ".71em")
            .style("text-anchor", "end")
            .text("density known variants");

    labels.append("text")
            .attr("transform", "translate(0," + (h - 60) + ")")
            .attr("x", 80)
            .attr("style","font-size:12px;")
            .attr("dx", "-1.0em")
            .attr("dy", "2.0em")
            .style("stroke", "red")
            .text("causal for lipodystrophy");

    labels.append("text")
            .attr("transform", "translate(0," + (h - 60) + ")")
            .attr("x", 280)
            .attr("style","font-size:12px;")
            .attr("dx", "-1.0em")
            .attr("dy", "2.0em")
            .style("stroke", "green")
            .text("not causal for lipodystrophy");

</script>
</g:if>

<script>
    $(function() {
        $( document ).tooltip({
            position: { my: "left+15 center", at: "right center" },
            tooltipClass: "info-tooltip"
        });

    });
</script>
</body>
</html>
