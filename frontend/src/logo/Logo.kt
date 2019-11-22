package logo

import react.RBuilder
import react.dom.div

fun RBuilder.logo() =
    div("Logo") {
        +"Interchange"
        div("Small") {
            +"Powered by Codestar"
        }
    }
