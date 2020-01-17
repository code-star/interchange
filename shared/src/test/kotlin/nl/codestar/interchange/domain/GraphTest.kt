package nl.codestar.interchange.domain

import io.kotlintest.specs.StringSpec
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.jupiter.api.Assertions.assertEquals

class GraphTest : StringSpec({
    val nodes = listOf(
        Node(Position("50", "100")),
        Node(Position("150", "200")),
        Node(Position("250", "300"))
    )

    val edges = listOf(
        Edge(nodes[0], nodes[1]),
        Edge(nodes[0], nodes[2])
    )

    val graph = Graph(nodes, edges)

    val json = Json(JsonConfiguration.Stable.copy(strictMode = false))

    "serialization should work correctly" {
        val expected = """
            {
                "nodes": [
                    {"position": {"latitude": "50", "longitude": "100"}},
                    {"position": {"latitude": "150", "longitude": "200"}},
                    {"position": {"latitude": "250", "longitude": "300"}}
                ],
                "edges": [
                    {"a": 0, "b": 1},
                    {"a": 0, "b": 2}
                ]
            }
        """.trimIndent()

        val serialized = json.toJson(Graph.serializer(), graph)

        assertEquals(
            json.parseJson(expected),
            serialized
        )
    }
})
