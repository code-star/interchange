package nl.codestar.interchange

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import java.time.*

import kotlinx.serialization.json.*
import nl.codestar.interchange.here.HereRoute
import nl.codestar.interchange.here.HereRoutingAPIResponse


data class City(val name: String, val lattitude: String, val longitude: String)
data class Edge(val start: City, val end: City)

suspend fun main() {
    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1 // start from 1
        while (true) {
            send(x++) // produce next
            delay(1000) // wait 0.1s
        }
    }



    val cities = mapOf(
        "Amsterdam" to City("Amsterdam", "52.328084", "4.913361"),
        "Utrecht" to City("Utrecht", "52.118121", "5.032961"),
        "Rotterdam" to City("Rotterdam", "51.931298", "4.438818"),
        "Groningen" to City("Groningen", "53.199353", "6.510003"),
        "Nijmegen" to City("Nijmegen", "51.841687", "5.777498")
    )

    val graph = listOf(
        Edge(cities.get("Amsterdam"), cities.get("Utrecht"))
    )

    for (edge in graph) {
        println(HereService.getRoutes(edge.start, edge.end))
    }


}


object HereService {
    val config = ConfigFactory.load()

    val appId = config.extract<String>("here.appId")
    val appCode = config.extract<String>("here.appCode")

    private val client = HttpClient()
    private val json = Json(JsonConfiguration.Stable.copy(strictMode = false))

    suspend fun getRoutes(start: City, end: City): List<HereRoute>? =
        try {
            val hereBytes = client.get<String>(
                "https://route.api.here.com/routing/7.2/calculateroute.json" +
                        "?app_id=$appId" +
                        "&app_code=$appCode" +
                        "&waypoint0=geo!${start.lattitude},${start.longitude}&waypoint1=geo!${end.lattitude},${end.longitude}" +
                        "&mode=shortest;car;traffic:disabled&alternatives=4&instructionFormat=text&representation=turnByTurn&jsonAttributes=25"
            )

            json.parse(HereRoutingAPIResponse.serializer(), hereBytes).response.routes
        } catch (_: Exception) {
            null
        }

}