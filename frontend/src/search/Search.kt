package search

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.button
import react.dom.div
import react.dom.input
import react.dom.label
import kotlin.browser.window

interface SearchProps: RProps {}

interface SearchState: RState {
    var from: String
    var to: String
}

class Search(props: SearchProps): RComponent<SearchProps, SearchState>(props) {
    init {
        state.from = ""
        state.to = ""
    }

    override fun RBuilder.render() {
        div("Search") {
            div("From") {
                label {
                    +"From:"
                }
                input(InputType.text) {
                    attrs {
                        placeholder = "From"
                        value = state.from
                        onChangeFunction = {
                            val target = it.target as HTMLInputElement
                            setState {
                                from = target.value
                            }
                        }
                    }
                }
            }
            div("To") {
                label {
                    +"To:"
                }
                input(InputType.text) {
                    attrs {
                        placeholder = "To"
                        value = state.to
                        onChangeFunction = {
                            val target = it.target as HTMLInputElement
                            setState {
                                to = target.value
                            }
                        }
                    }
                }
            }
            div("Submit") {
                button {
                    attrs {
                        onClickFunction = {
                            window.alert("From: ${state.from} to: ${state.to}")
                        }
                    }
                    +"Search"
                }
            }
        }
    }
}

fun RBuilder.search() = child(Search::class) {}
