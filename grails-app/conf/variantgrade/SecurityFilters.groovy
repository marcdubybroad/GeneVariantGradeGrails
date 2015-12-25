package variantgrade

class SecurityFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                if (!session.user && !actionName.equals('login')) {
                    log.info("redirecting to login for user: " + session.user)
                    redirect(controller: "heatMap", action: "login")
                    return false
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
