package nl.codestar.interchange

import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

data class Node(val name: String)

data class Edge(
  val a: Node,
  val b: Node,
  val w: Double
) {
  fun swap(): Edge {
    return Edge(b, a, w)
  }
}

val rotterdam = Node("Rotterdam")
val amsterdam = Node("Amsterdam")
val groningen = Node("Groningen")
val utrecht = Node("Utrecht")
val nijmegen = Node("Nijmegen")

val nodes = listOf(rotterdam, amsterdam, groningen, utrecht, nijmegen)

val unidirecitonalEdges = listOf(
  Edge(rotterdam, amsterdam, 1.0),
  Edge(amsterdam, utrecht, 1.0),
  Edge(utrecht, rotterdam, 3.0),
  Edge(utrecht, groningen, 2.0),
  Edge(utrecht, nijmegen, 2.0)
)

val edges = unidirecitonalEdges.union(unidirecitonalEdges.map { it.swap() })

fun getSmallestDistance(distances: Map<Node, Double>, alreadyVisited: Set<Node>): Pair<Node, Double>? {
  return distances.entries.minBy { p ->
    if (alreadyVisited.contains(p.key)) {
      Double.MAX_VALUE
    } else {
      p.value
    }
  }?.toPair()
}

fun getNeighbours(node: Node): List<Pair<Node, Double>> {
  return edges.filter{e -> e.a == node}.map{e -> Pair(e.b, e.w) }
}

fun doIt(): List<Node>? {

  val from = rotterdam
  val to = groningen
  val distances = HashMap<Node, Double>()
  val routes = HashMap<Node, List<Node>>()
  nodes.forEach { node ->
    if (node.name == from.name) {
      distances[node] = 0.0
      routes[node] = listOf(node)
    } else {
      distances[node] = Double.MAX_VALUE
    }
  }

  val q = nodes.toMutableList()
  val alreadyVisited = HashSet<Node>()
  while (q.isNotEmpty()) {
    val minDistance = getSmallestDistance(distances, alreadyVisited) ?: throw RuntimeException()
    val previousRoute = routes.getOrDefault(minDistance.first, emptyList())
    q.remove(minDistance.first)
    alreadyVisited.add(minDistance.first)

    val neighbours = getNeighbours(minDistance.first)
    neighbours.forEach { neighbour ->
      val newDistance =  minDistance.second + neighbour.second // neighbour.weight
      if (newDistance < distances.getOrDefault(neighbour.first, Double.MAX_VALUE)) {
        distances[neighbour.first] = newDistance
        routes[neighbour.first] = previousRoute + neighbour.first
      }
    }
  }

  return routes[to]
}

fun main(args: Array<String>) {
  System.out.println(doIt()?.toString())
}

