<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Phenotypic Annotation of TP53 Mutations</title>
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
                        <div class="description">The PHANTM classifier provides functional classification for any missense and nonsense variant using comprehensive experimental data. It currently supports functional classification for missense and nonsense variants in the gene <span class="miterItalic">TP53</span>.
                        <br/>See <a href="https://www.nature.com/ng/" target="newWindow">paper link</a> for details.</div>
                        <div class="apptitle-black description">TP53</div>
                        <p class="bold-text">Enter Variant</p>
                        <input id="searchbox" value="${lastQuery}" name="query" class="form-control input-lg awesomebar searchbox" type="text" placeholder="Search for a protein change or variant"/>
                        <p class="text-muted small-text">
                            Examples - Protein change <a href="#" title='Numbering with respect to p53 isoform a'><g:img dir="images" file="question2.png" width="17" height="17"/></a>:
                            <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.R273H', prevalence: '1.0e-5']">p.R273H</g:link> or
                            <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.Arg273His', prevalence: '1.0e-5']">p.Arg273His</g:link>,

                        </p>

                    <g:if test="${false}">
                        <p class="bold-text">Enter Disease Prevalence  <a href="#" title="Disease prevalence of familial partial lipodystrophy 3 (FPLD3) in the general population is 1:100,000 (default) to 1:1,000,000. In specialist clinics this can be as high as 1:5 (0.20)."><g:img dir="images" file="question2.png" width="17" height="17"/></a></p>
                        <input id="prevalencebox" name="prevalence" class="form-control input-lg awesomebar prevalencebox" type="text" placeholder="Enter disease prevalence" value="${lastPrevalence ? lastPrevalence : '1.0e-5'}"/>
                    </g:if>
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
                                            <td># of IARC somatic mutations (human tumors)  <a href="#" title="Add # IARC somatic mutations text here"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getSomaticIarcMutationCount()}</td>
                                        </tr>

                            <g:if test="${false}">
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
                            </g:if>

                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-md-8">

                                </div>
                            </div>

                        </g:if>
                        <g:else>
<!--                            <div class="row reduced-width"> -->
                            <div class="row some-reduced-width">
                                <div class="col-md-11">
                                    <table class="table">
                                        <thead>
                                        </thead>
                                        <tbody>
                                        <tr class="tightrow">
                                            <td>Reference amino acid</td>
                                            <td>${proteinResult.getAminoAcidReference()}</td>
                                        </tr>

                                <g:if test="${false}">
                                        <tr>
                                            <td>Reference codon  <a href="#" title="The three nucleotide sequence specifying the reference amino acid."><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><span class="cap">${proteinResult.getReferenceCodon()}</span></td>
                                        </tr>
                                </g:if>

                                        <tr class="tightrow">
                                            <td>Protein change <a href="#" title="Numbering with respect to p53 isoform a"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getScientificAlleleCode()}</td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td>Experimental function score  <a href="#" title="Also referred to as phenotype score in Giacomelli et al. 2017"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td><g:formatNumber number="${proteinResult.getHeatAmount()}" type="number" maxFractionDigits="3" /></td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td># of IARC somatic mutations (human tumors)  <a href="#" title="Petitjean et al. Human Mutation. 2007

Bouaoun et al. Human Mutation. 2016

IARC database R18 April 2016"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getSomaticIarcMutationCount()}</td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td># of IARC germline mutations (LFL/LFS individuals)  <a href="#" title="Petitjean et al. Human Mutation. 2007

Bouaoun et al. Human Mutation. 2016

IARC database R18 April 2016"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getGermlineIarcMutationCount()}</td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td># of ExAC germline mutations (unselected individuals)  <a href="#" title="Lek et al. Nature. 2016.&nbsp;&nbsp;

Data release 1.0 February 2017"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getGermlineExacMutationCount()}</td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td>Transcriptional activity in yeast (% of wild-type)  <a href="#" title="Kato et al. PNAS. 2003"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getTranscriptionalActivityYeastPercent()}</td>
                                        </tr>

                                        <tr class="tightrow">
                                            <td>Mutation probability (COSMIC Signature 1 percentile)  <a href="#" title="Alexandrov et al. Nature. 2013

Alexandrov et al. Nature Genetics. 2015"><g:img dir="images" file="question2.png" width="17" height="17"/></a></td>
                                            <td>${proteinResult.getMutationProbability()}</td>
                                        </tr>


                                <g:if test="${false}">
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
                                </g:if>

                                        </tbody>
                                    </table>
                                </div>
                                <div class="col-md-1">

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

    x.domain([-6, 7]);
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
    d3.csv("${g.resource(file:'nonsenseCancer.csv')}", function(error, data) {
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

    d3.csv("${g.resource(file:'silentCancer.csv')}", function(error, data) {
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
            .attr("x", w * ((${proteinResult.getHeatAmount()} + 6)/12))
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
            .attr("transform", "translate(0," + (h - 40) + ")")
            .attr("x", 330)
            .attr("style","font-size:12px;")
            .attr("dx", "-1.0em")
            .attr("dy", "2.0em")
            .style("stroke", "red")
            .text("LOF (nonsense)");

    labels.append("text")
            .attr("transform", "translate(0," + (h - 40) + ")")
            .attr("x", 205)
            .attr("style","font-size:12px;")
            .attr("dx", "-1.0em")
            .attr("dy", "2.0em")
            .style("stroke", "green")
            .text("WT (silent)");

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
