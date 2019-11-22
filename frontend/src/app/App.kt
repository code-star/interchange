package app

import logo.logo
import react.*
import react.dom.*
import search.search
import ReactMapGL

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div {
            search()
            logo()
           
        }

        ReactMapGL  {
            attrs.width = 1000
            attrs.height = 1000
            attrs.latitude = 37.7577
            attrs.longitude = -122.4376
            attrs.zoom = 8
        }
    }
}

fun RBuilder.app() = child(App::class) {}
