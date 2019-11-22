package nl.codestar.interchange

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*
import org.slf4j.event.*
import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.serialization.Serializable
import java.time.*

import kotlinx.serialization.json.*
import nl.codestar.interchange.here.HereRoutingAPIResponse


data class City(val name: String, val lattitude: String, val longitude: String)
data class Edge(val start: City, val end: City)

suspend fun main(args: Array<String>) {
    val appId = "knlxkDhhXNJfoE26rgIP"
    val appCode = "j2VGkmwQ63x-BzKP_LOvyA"

    val json = Json(JsonConfiguration.Stable.copy(strictMode = false))
    val client = HttpClient()
    val graph = listOf(
        Edge(
            City("Amsterdam", "52.328084", "4.913361"),
            City("Utrecht", "52.118121", "5.032961")
        )
    )

    for (edge in graph) {
        val hereBytes = client.get<String>("https://route.api.here.com/routing/7.2/calculateroute.json" +
                "?app_id=$appId" +
                "&app_code=$appCode" +
                "&waypoint0=geo!52.065465,5.0676533&waypoint1=geo!52.072752,4.902093&mode=shortest;car;traffic:disabled&alternatives=4&instructionFormat=text&representation=turnByTurn&jsonAttributes=25")

        val hereJson = json.parse(HereRoutingAPIResponse.serializer(), hereBytes)

        println(hereJson)
    }






}


