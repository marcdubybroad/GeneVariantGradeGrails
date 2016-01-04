<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Missense Interpreter</title>
    <style type="text/css" media="screen">
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
            padding-top: 15px;
            padding-bottom: 15px;
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
                    <div class="formWrapper title">
                        Get the disease risk by protein change or variant
                    </div>
                    <div class="formWrapper">
                        <p class="bold-text">Enter Search</p>
                        <input id="searchbox" name="query" class="form-control input-lg awesomebar searchbox" type="text" placeholder="Search for a protein change or variant"/>
                        <p class="text-muted small-text">
                            Examples - Protein change: <g:link action="proteinSearch" controller="heatMap" params="[query: 'p.Leu345Arg', prevalence: '1.0e-5']">p.Leu345Arg</g:link>,
                        Variant: <g:link action="proteinSearch" controller="heatMap" params="[query: 'chr3-134069-T-G', prevalence: '1.0e-5']">chr3-134069-T-G</g:link>
                        </p>
                        <p class="bold-text">Enter Prevalence</p>
                        <input id="prevalencebox" name="prevalence" class="form-control input-lg awesomebar prevalencebox" type="text" placeholder="Enter prevalence" value="1.0e-5"/>
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
                        The reference gene codon for protein position ${proteinResult.getPosition()} is ${proteinResult.getReferenceCodon()}
                        <p/>

                        <g:if test="${proteinResult.getAlternateCodon()}">
                            The modified gene codon for protein position ${proteinResult.getPosition()} is ${proteinResult.getAlternateCodon()}
                            <p/>

                            <g:if test="${proteinResult.isResultStopCodon()}">
                                The modified gene codon ${proteinResult.getAlternateCodon()} is a stop codon.
                                <p/>
                            </g:if>
                        </g:if>

                        <g:if test="${proteinResult.getVariantDisplay()}">
                            For variant ${proteinResult.getVariantDisplay()} the protein change is: ${proteinResult.getScientificAlleleCode()}
                            <p/>
                        </g:if>
                        For ${proteinResult.getScientificAlleleCode()} the combined functional score is: ${proteinResult.getHeatAmount()}
                        <p/>
                        For ${proteinResult.getScientificAlleleCode()}, the effect is ${proteinResult.getEffect()} with logp value: ${proteinResult.getLogP()}
                        <p/>
                        For ${proteinResult.getScientificAlleleCode()} at prevalence of ${proteinResult.getInputPrevalence()}, the pValue is ${proteinResult.getpValue()}
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
</body>
</html>
