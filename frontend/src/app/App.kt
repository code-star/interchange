package app

import logo.logo
import react.*
import react.dom.*
import search.search

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div {
            search()
            logo()
        }
    }
}

fun RBuilder.app() = child(App::class) {}
