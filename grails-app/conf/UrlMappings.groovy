class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

//        "/"(view:"/index")
        "/" (controller: "heatMap", action: "proteinSearch")
        "500"(view:'/error')
	}
}
