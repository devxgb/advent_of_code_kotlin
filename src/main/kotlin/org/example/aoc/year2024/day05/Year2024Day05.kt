package org.example.aoc.year2024.day05

import kotlin.collections.map
import org.example.common.InputReader

fun main() {
    val lines = InputReader().readInput("2024", "05")
    val year2024Day05 = Year2024Day05()
    println(year2024Day05.part1(lines))
    println(year2024Day05.part2(lines))
}

class Year2024Day05 {
    fun part1(lines: List<String>): Int {
        val splitIndex = lines.indexOfFirst { it.isEmpty() }
        require(splitIndex > 0)
        val dependencies =
          lines.take(splitIndex).map { dependency ->
              val split = dependency.split("|")
              require(split.size == 2)
              split[0].toInt() to split[1].toInt()
          }
        val testCases = lines.drop(splitIndex + 1)
        return testCases
          .map { testCase -> testCase.split(",").map { it.toInt() } }
          .filter { sequence ->
              val filteredDependencies = dependencies.filter { (it.first in sequence) && (it.second in sequence) }
              val graph = buildGraph(filteredDependencies)
              isValidSequence(sequence, graph.copy())
          }
          .sumOf { testCase -> testCase[testCase.size / 2] }
    }

    fun part2(lines: List<String>): Int {
        val splitIndex = lines.indexOfFirst { it.isEmpty() }
        require(splitIndex > 0)
        val dependencies =
          lines.take(splitIndex).map { dependency ->
              val split = dependency.split("|")
              require(split.size == 2)
              split[0].toInt() to split[1].toInt()
          }
        val testCases = lines.drop(splitIndex + 1)
        return testCases
          .map { testCase -> testCase.split(",").map { it.toInt() } }
          .map { sequence ->
              val filteredDependencies = dependencies.filter { (it.first in sequence) && (it.second in sequence) }
              val graph = buildGraph(filteredDependencies)
              sequence to graph
          }
          .filter { (sequence, graph) -> isValidSequence(sequence, graph.copy()).not() }
          .map { (_, graph) -> graph.getVerticesSorted() }
          .sumOf { testCase -> testCase[testCase.size / 2] }
    }

    private fun buildGraph(dependencies: List<Pair<Int, Int>>): Graph {
        val graph = Graph()
        dependencies.forEach { graph.addEdge(it.first, it.second) }
        return graph
    }

    private fun isValidSequence(sequence: List<Int>, graph: Graph): Boolean {
        for (vertex in sequence) {
            if (graph.contains(vertex).not()) {
                return false
            }
            graph.removeVertexRecursive(vertex)
        }
        return true
    }

    companion object {
        private class Graph {
            private val adjacencyList: HashMap<Int, LinkedHashSet<Int>>
            private val reverseAdjacencyList: HashMap<Int, LinkedHashSet<Int>>
            private val inDegreeList: HashMap<Int, Int>

            constructor() {
                adjacencyList = hashMapOf()
                reverseAdjacencyList = hashMapOf()
                inDegreeList = hashMapOf()
            }

            private constructor(adjacencyList: HashMap<Int, LinkedHashSet<Int>>, reverseAdjacencyList: HashMap<Int, LinkedHashSet<Int>>, inDegreeList: HashMap<Int, Int>) {
                this.adjacencyList = adjacencyList
                this.reverseAdjacencyList = reverseAdjacencyList
                this.inDegreeList = inDegreeList
            }

            fun addEdge(from: Int, to: Int) {
                val fromAdjList = adjacencyList.computeIfAbsent(from) { LinkedHashSet() }
                fromAdjList.add(to)
                val toRevAdjList = reverseAdjacencyList.computeIfAbsent(to) { LinkedHashSet() }
                toRevAdjList.add(from)
                inDegreeList.computeIfAbsent(from) { 0 }
                inDegreeList.computeIfAbsent(to) { 0 }
                inDegreeList.computeIfPresent(to) { _, value -> value + 1 }
            }

            fun isConnected(from: Int, to: Int): Boolean {
                return adjacencyList[from]?.contains(to) ?: false
            }

            fun getInDegree(vertex: Int): Int {
                return requireNotNull(inDegreeList[vertex])
            }

            fun contains(vertex: Int) = inDegreeList.contains(vertex)

            private fun removeVertex(vertex: Int) {
                require(getInDegree(vertex) <= 0)
                adjacencyList[vertex]?.forEach { adj ->
                    require(inDegreeList.contains(adj))
                    val adjIndegree = inDegreeList[adj]
                    requireNotNull(adjIndegree)
                    require(adjIndegree > 0)
                    inDegreeList[adj] = adjIndegree - 1

                    reverseAdjacencyList[adj]?.remove(vertex)
                }
                adjacencyList.remove(vertex)
                inDegreeList.remove(vertex)
            }

            fun removeVertexRecursive(vertex: Int) {
                if (!contains(vertex)) {
                    return
                }
                reverseAdjacencyList[vertex]?.toList()?.forEach { adj -> removeVertexRecursive(adj) }
                removeVertex(vertex)
            }

            fun getVerticesSorted(): List<Int> {
                return inDegreeList.entries.sortedBy { it.value }.map { it.key }
            }

            fun copy(): Graph =
              Graph(
                adjacencyList = HashMap(adjacencyList.mapValues { (_, value) -> LinkedHashSet(value) }),
                reverseAdjacencyList = HashMap(reverseAdjacencyList.mapValues { (_, value) -> LinkedHashSet(value) }),
                inDegreeList = HashMap(inDegreeList),
              )
        }
    }
}
