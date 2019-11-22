@file:JsModule("react-map-gl")

import react.*

@JsName("default")
external val ReactMapGL: RClass<ReactMapGLProps>

external interface ReactMapGLProps : RProps {
    var width: Number
    var height: Number 
    var latitude: Number 
    var longitude: Number 
    var zoom: Number
}
