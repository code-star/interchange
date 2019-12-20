package nl.codestar.interchange

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.codestar.interchange.here.HereRoute
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list
import nl.codestar.interchange.domain.Edge
import nl.codestar.interchange.domain.Graph
import nl.codestar.interchange.domain.Node
import nl.codestar.interchange.domain.Position
import nl.codestar.interchange.here.HereRoutingAPIResponse
import nl.codestar.interchange.producers.KinesisStringProducer
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region

fun CoroutineScope.numberGenerator() = produce<List<HereRoute>> {
    while (true) {
        for (edge in smallGraph().edges) {
            val routes = HereService.getRoutes(edge.a.position, edge.b.position)
            if(routes != null) send(routes)
        }

        delay(3000) // wait 0.1s
    }
}

val smallGraph = {
    val amsterdam = Node(Position("52.328084", "4.913361"))
    val utrecht = Node(Position("52.118121", "5.032961"))
    val rotterdam = Node(Position("51.931298", "4.438818"))
    val groningen = Node(Position("53.199353", "6.510003"))
    val nijmegen = Node(Position("51.841687", "5.777498"))

    Graph(
        listOf(amsterdam, utrecht, rotterdam, groningen, nijmegen),
        listOf(
            Edge(amsterdam, utrecht),
            Edge(utrecht, amsterdam),
            Edge(amsterdam, rotterdam),
            Edge(rotterdam, amsterdam),
            Edge(rotterdam, utrecht),
            Edge(utrecht, rotterdam),
            Edge(groningen, utrecht),
            Edge(utrecht, groningen),
            Edge(utrecht, nijmegen),
            Edge(nijmegen, utrecht),
            Edge(nijmegen, groningen),
            Edge(groningen, nijmegen)
        )
    )
}

suspend fun main() = runBlocking {
    val json = Json(JsonConfiguration.Stable.copy(strictMode = false))

    val config = ConfigFactory.load()

    val kinesisConfig = config.getConfig("kinesis")
    val streamName = kinesisConfig.getString("streamName")
    val region = kinesisConfig.getString("region")
    val accessKeyId = kinesisConfig.getString("accessKeyId")
    val secretAccessKey = kinesisConfig.getString("secretAccessKey")

    val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)

    val producer = KinesisStringProducer(streamName, "1", Region.of(region), credentials)

    val numberGenerator = numberGenerator()


    for( msg in numberGenerator) {
        producer.publish(json.stringify(HereRoute.serializer().list, msg))
        println(msg.toString().substring(0, 100))
    }
}

object HereService {
    private val config = ConfigFactory.load()

    private val appId = config.extract<String>("here.appId")
    private val appCode = config.extract<String>("here.appCode")

    private val client = HttpClient()
    private val json = Json(JsonConfiguration.Stable.copy(strictMode = false))

    suspend fun getRoutes(start: Position, end: Position): List<HereRoute>? =
        try {
            val hereBytes = client.get<String>(
                "https://route.api.here.com/routing/7.2/calculateroute.json" +
                        "?app_id=$appId" +
                        "&app_code=$appCode" +
                        "&waypoint0=geo!${start.latitude},${start.longitude}&waypoint1=geo!${end.latitude},${end.longitude}" +
                        "&mode=shortest;car;traffic:disabled&alternatives=4&instructionFormat=text&representation=turnByTurn&jsonAttributes=25"
            )

            json.parse(HereRoutingAPIResponse.serializer(), hereBytes).response.routes
        } catch (ex: Exception) {
            println(ex)
            null
        }
}
