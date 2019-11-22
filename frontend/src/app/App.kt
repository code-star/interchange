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
            attrs.width = "100vw"
            attrs.height = "100vh"
            attrs.latitude = 52.132633
            attrs.longitude = 5.291266
            attrs.zoom = 7
        }
    }
}

fun RBuilder.app() = child(App::class) {}
