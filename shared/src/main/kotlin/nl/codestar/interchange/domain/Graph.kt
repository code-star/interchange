package nl.codestar.interchange.domain

import kotlinx.serialization.*
import kotlinx.serialization.internal.SerialClassDescImpl

@Serializable
data class Graph(
        val nodes: List<Node>,
        val edges: List<Edge>
) {
    @Serializer(forClass= Graph::class)
    companion object : KSerializer<Graph> {
        override val descriptor: SerialDescriptor = object : SerialClassDescImpl("Graph") {
            init {
                addElement("nodes")
                addElement("edges")

            }
        }
        override fun deserialize(decoder: Decoder): Graph {
            return Graph(listOf(), listOf())
        }

        override fun serialize(encoder: Encoder, graph: Graph) {
            val compositeOutput = encoder.beginStructure(descriptor)
            compositeOutput.encodeSerializableElement(descriptor, 0, Node.serializer().list, graph.nodes)
            compositeOutput.encodeSerializableElement(descriptor, 1, RefEdge.serializer().list,
                graph.edges.map { e ->
                    RefEdge(
                            a = graph.nodes.indexOf(e.a),
                            b = graph.nodes.indexOf(e.b)
                    )
                })
            compositeOutput.endStructure(descriptor)
        }
    }
}

@Serializable
data class Node(val position: Position)

@Serializable
data class Edge(
        val a: Node,
        val b: Node
    // TODO: we need a data field here
    // it will probably need to contain a list of routes
    // each route will have: an average traffic value, and a list of gps-like directions
)

@Serializable
data class RefEdge(
    val a: Int,
    val b: Int
)

@Serializable
data class Position(
    val latitude: String,
    val longitude: String
)
