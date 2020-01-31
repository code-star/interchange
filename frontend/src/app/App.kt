package app

import Marker
import logo.logo
import react.*
import react.dom.*
import search.search
import ReactMapGL
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlin.browser.window

interface ViewportState: RState {
    var width: String
    var height: String 
    var latitude: Number 
    var longitude: Number 
    var zoom: Number
    var waypointsResult: WaypointsResult
}

typealias Location = Array<Number>

interface Waypoint {
    val name: String
    val location: Location
}

interface Intersection {
    val name: String
    val classifications: Array<String>
    val location: Location
}

interface Route {
    val duration: Number
    val distance: Number
    val intersections: Array<Intersection>
}

interface WaypointsResult {
    val waypoints: Array<Waypoint>
    val routes: Array<Route>
}

class EmptyWaypointsResult : WaypointsResult {
    override val waypoints: Array<Waypoint> = emptyArray()
    override val routes: Array<Route> = emptyArray()
}


class App : RComponent<RProps, ViewportState>() {
    override fun ViewportState.init() {
        width = "100vw"
        height = "100vh"
        latitude = 52.132633
        longitude = 5.291266
        zoom = 11
        waypointsResult = EmptyWaypointsResult()

        val mainScope = MainScope()
        mainScope.launch {
            val result = fetchWaypointsResult()
            setState {
                waypointsResult = result
            }
        }
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
                    console.log(viewport)
                    setState {
                        width = viewport.width
                        height = viewport.height
                        latitude = viewport.latitude
                        longitude = viewport.longitude
                        zoom = viewport.zoom
                    }
                }
            }

            for (route in state.waypointsResult.routes) {
                for (intersection in route.intersections) {
                    Marker {
                        attrs {
                            longitude = intersection.location[0]
                            latitude = intersection.location[1]
                        }

                        div("Marker") {
                            +"🍿"
                        }
                    }
                }
            }
        }
    }

    suspend fun fetchWaypointsResult(): WaypointsResult {
        val responsePromise = window.fetch("https://api.myjson.com/bins/19rdz4")
        val response = responsePromise.await()
        val jsonPromise = response.json()
        val json = jsonPromise.await()
        return json.unsafeCast<WaypointsResult>()
    }
}

fun RBuilder.app() = child(App::class) {}
