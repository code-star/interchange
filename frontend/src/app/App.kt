package app

import logo.logo
import react.*
import react.dom.*
import search.search
import ReactMapGL

interface ViewportState: RState {
    var width: String
    var height: String 
    var latitude: Number 
    var longitude: Number 
    var zoom: Number
}


class App : RComponent<RProps, ViewportState>() {
    init {
        state.width = "100vw"
        state.height = "100vh"
        state.latitude = 52.132633
        state.longitude = 5.291266
        state.zoom = 7
    }


    override fun RBuilder.render() {
        div {
            search()
            logo()
        }

        ReactMapGL  {
            attrs {
                width = state.width
                height = state.height
                latitude = state.latitude
                longitude = state.longitude
                zoom = state.zoom
                onViewportChange = { viewport ->
                    setState {
                        width = viewport.width
                        height = viewport.height
                        latitude = viewport.latitude
                        longitude = viewport.longitude
                        zoom = viewport.zoom
                    }
                }
            }
        }
    }

}

fun RBuilder.app() = child(App::class) {}
