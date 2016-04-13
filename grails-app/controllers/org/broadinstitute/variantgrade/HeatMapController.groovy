package org.broadinstitute.variantgrade
import org.broadinstitute.variantgrade.result.ProteinResult
import org.broadinstitute.variantgrade.util.GradeException

class HeatMapController {
    // services
    HeatMapService heatMapService

    // instance variables
    private String passcode = "broadspectacle";

    def index() { }

    def login() {
        String username = params.username;
        String password = params.password;
        Boolean loggedIn = false;

        // log
        log.info("got params: " + params);

        // if user, then logged in
        if (session.user) {
            log.info("have user: " + session.user)
            loggedIn = true;
        }

        // if both provided, try to log in
        if ((username) && (password)) {
            String userPass = (username + password)
            log.info("have user passcode: " + userPass)
            if (userPass == this.passcode) {
                loggedIn = true
            }
        }

        // set session user
        if (loggedIn) {
            def user = session["user"]
            session["user"] = "John"
            log.info("adding user: " + session.user)

            // get the protein reference letter list
            List<String> referenceLetterList = this.heatMapService.getProteinReferenceLetterList();

            // render
            render(view: "proteinForm", model: [referenceLetterList: referenceLetterList])

        } else {
            log.info("no user, so send login form")
            render(view: "loginForm")
        }
    }

    def newlogin() {
        // get the protein reference letter list
        List<String> referenceLetterList = this.heatMapService.getProteinReferenceLetterList();

        // render
        render(view: "proteinForm", model: [referenceLetterList: referenceLetterList])
    }


    def chart() {
        log.info("in chart test")
        render(view: "testd3")
    }

    def proteinSearch() {
        // check that logged in
        if (!session.user) {
            redirect(action: 'login')
        }

        // log
        log.info("in protein search, got params: " + params)

        // keep last search parameters
        String lastQuery = params.query?.trim();
        String lastPrevalence = params.prevalence?.trim();

        // if logged in, protein form
        String referenceLetter = params.referenceLetter
        String position = params.position
        String errorMessage;
        ProteinResult proteinResult = null;

        // get the protein reference letter list
        List<String> referenceLetterList = this.heatMapService.getProteinReferenceLetterList();

        // if params came from query search input
        if (params.query && params.prevalence) {
            try {
                // get the result
                proteinResult = this.heatMapService.getHeatMapReadingFromSearchString(lastQuery, lastPrevalence)

                // log
                log.info("for protein position: " + position + " and letter: " + referenceLetter + " got result:" + proteinResult.getHeatAmount())

            } catch (GradeException exception) {
                log.error("in protein search, for params: " + params + " got error: " + exception.getMessage())
                errorMessage = exception.webMessage;
            }

        } else if (!params.query) {
            errorMessage = "The search string input is empty";

        } else if (!params.prevalence) {
            errorMessage = "The prevalence input is empty";

        } else {
            errorMessage = "Incorrect input for search: " + params.query + " and/or prevalence: " + params.prevalence;


            /*
            if (!position) {
                log.info("missing position with params: " + params);
                errorMessage = "Please enter a protein position";

            } else if (!referenceLetter) {
                log.info("missing reference letter with params: " + params);
                errorMessage = "Please enter a protein letter";

            } else {
                try {
                    // get the result
                    proteinResult = this.heatMapService.getHeatMapReadingFromProtein(Integer.parseInt(position), referenceLetter, 0.01, false)

                    // log
                    log.info("for protein position: " + position + " and letter: " + referenceLetter + " got result:" + proteinResult.getHeatAmount())

                } catch (GradeException exception) {
                    log.error("in protein search, for params: " + params + " got error: " + exception.getMessage())
                    errorMessage = exception.webMessage;
                }
            }
            */
        }

        // render
        render(view: 'proteinForm', model: [lastQuery: lastQuery, lastPrevalence: lastPrevalence, errorMessage: errorMessage, proteinResult: proteinResult, referenceLetterList: referenceLetterList])

    }
}
