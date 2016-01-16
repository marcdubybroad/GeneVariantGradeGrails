class BootStrap {
    def heatMapService

    def init = { servletContext ->
        log.info("starting matrix loading")
        this.heatMapService.getMatrixParser();
        log.info("ended matrix loading")

    }
    def destroy = {
    }
}
