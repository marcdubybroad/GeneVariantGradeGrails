# GeneVariantGradeGrails
App to grade gene variants for disease risk

# Environment
- grails version 2.4.3
- java version 8

# Commands
* grails run-app
  * will run the application in its own application server for testing
* grails war
  * will build a war for Tomcat deployment

# Notes on the code
* most of this code was wrtitten as I was learning the intricacies of genes/introns/exons, so the naming conventions might be a little confusing
* the application loads a set of xls files upon startup and caches the matrix for faster access
  * the xls files need to be located in the grails-app/conf directory for the grails framework to make them accessible as Resources
* the PPARG amino acid sequence is a static element in the MatrixParser.java file

