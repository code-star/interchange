package nl.codestar.interchange.here

data class HereRoutingAPIResponse(val response: HereResponse)

data class HereResponse(val routes: List<HereRoute>)

data class HereRoute(
    val legs: List<HereLeg>,
    val labels: List<String>
)


data class HereLeg(
    val start: HereWaypoint,
    val end: HereWaypoint,
    val maneuvers: List<HereManeuver>,
    val links: List<HereLink>,

    val length: Int,
    val baseTime: Int,
    val travelTime: Int,
    val trafficTime: Int,
    val shape: List<String>
)

data class HereManeuver(
    val id: String,
    val position: HerePosition,
    val toLink: String,
    val roadName: String,
    val nextRoadName: String,
    val roadNumber: String,
    val nextRoadNumber: String,

    val length: Int,
    val baseTime: Int,
    val travelTime: Int,
    val trafficTime: Int,
    val shape: List<String>
)

data class HereLink(
    val linkId: String,
    val remainDistance: Int,
    val remainTime: Int,
    val maneuverId: String,
    val speedLimit: Double,
    val dynamicSpeedInfo: HereTrafficInformation,
    val roadName: String,
    val roadNumber: String,

    val length: Int,
    val shape: List<String>
)

data class HereTrafficInformation(
    val baseSpeed: Double,
    val trafficSpeed: Double,
    val baseTime: Int,
    val trafficTime: Int,
    val jamFactor: Double
)

data class HereWaypoint(
    val linkId: String,
    val mappedPosition: HerePosition,
    val originalPosition: HerePosition
)

data class HerePosition(
    val latitude: String,
    val longitude: String
)
