@file:JsModule("react-map-gl")

import react.*

@JsName("default")
external val ReactMapGL: RClass<ReactMapGLProps>

external interface ReactMapGLProps : RProps {
    var width: String
    var height: String 
    var latitude: Number 
    var longitude: Number 
    var zoom: Number
}
