# GeneVariantGradeGrails
App to grade gene variants for disease risk

# Environment
- grails version 2.4.3
- java version 8

# Commands
* to run from the command line
  * add the JAVA_HOME environment variable, pointing to your java 8 installation
  * add the GRAILS_HOME environment variable, point to your grails 2.4.3 installation
  * add the bin directories for the two above directories to your path
    * ie: export PATH=$JAVA_HOME/bin:$GRAILS_HOME/bin:$PATH
  * grails run-app
    * will run the application in its own application server for testing
* to build a war for Tomcat deployment
  * add the JAVA_HOME environment variable, pointing to your java 8 installation
  * add the GRAILS_HOME environment variable, point to your grails 2.4.3 installation
  * add the bin directories for the two above directories to your path
    * ie: export PATH=$JAVA_HOME/bin:$GRAILS_HOME/bin:$PATH
  * grails war
    * will build a war in the target directory to use for Tomcat deployment
  * copy to the tomcat's webapps directory or deploy using the Tomcat manager

# Notes on the code
* most of this code was wrtitten as I was learning the intricacies of genes/introns/exons, so the naming conventions might be a little confusing
* the application loads a set of xls files upon startup and caches the matrix for faster access
  * the xls files need to be located in the grails-app/conf directory for the grails framework to make them accessible as Resources
* the PPARG amino acid sequence is a static element in the MatrixParser.java file

